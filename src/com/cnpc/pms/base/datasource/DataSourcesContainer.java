package com.cnpc.pms.base.datasource;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnpc.pms.base.springbean.helper.ArtifactBeanHelper;
import com.cnpc.pms.base.springbean.helper.DataSourceConfigurer;
import com.cnpc.pms.base.util.SpringHelper;

/**
 * 
 * 
 * Copyright(c) 2010 IBM Corporation, http://www.ibm.com
 * 
 * @author lushu
 * @since Jun 27, 2013
 */
public class DataSourcesContainer {

	protected final static Logger log = LoggerFactory.getLogger(DataSourcesContainer.class);
	private final static Map<String, SessionFactory> sessionFactories = new HashMap<String, SessionFactory>();

	static {
		String sfBeanName = DataSourceConfigurer.getDefaultBeanName(ArtifactBeanHelper.SESSIONFACTORY_BEAN_TYPE);
		SessionFactory sessionFactory = (SessionFactory) SpringHelper.getBean(sfBeanName);
		sessionFactories.put(DataSourceConfigurer.DEFAULT_DATA_SOURCE, sessionFactory);
		log.debug("Add default SessionFactory {}", sfBeanName);
		for (String annotation : DataSourceConfigurer.getInstance().getOtherDataAnnotation()) {
			String beanName = SpringHelper.getDecoratedNameBySource(sfBeanName, annotation);
			sessionFactory = (SessionFactory) SpringHelper.getBean(beanName);
			sessionFactories.put(annotation, sessionFactory);
			log.debug("|-Add new SessionFactory {} = {}", annotation, beanName);
		}
	}

	private abstract class DelegatedTask<T> {

		private Map<String, T> map;

		public DelegatedTask(Map<String, T> map) {
			this.map = map;
		}

		protected abstract void doTask(T t, String annotation);

		public void execute() {
			for (Map.Entry<String, T> entry : map.entrySet()) {
				doTask(entry.getValue(), entry.getKey());
			}
		}
	}

	private DelegatedTask<Session> flush, clear, close;

	private Map<String, Session> sessions = new HashMap<String, Session>();
	private Map<String, Transaction> transactions = new HashMap<String, Transaction>();

	public DataSourcesContainer() {
		for (Map.Entry<String, SessionFactory> entry : sessionFactories.entrySet()) {
			Session session = entry.getValue().getCurrentSession();
			sessions.put(entry.getKey(), session);
			transactions.put(entry.getKey(), session.getTransaction());
		}

		flush = new DelegatedTask<Session>(sessions) {
			@Override
			protected void doTask(Session session, String annotation) {
				log.debug("Before Flush[{}] {}", annotation, getSessionInfo(session));
				session.flush();
				log.debug("After  Flush[{}] {}", annotation, getSessionInfo(session));
			}

		};
		clear = new DelegatedTask<Session>(sessions) {
			@Override
			protected void doTask(Session session, String annotation) {
				log.debug("Before Clear[{}] {}", annotation, getSessionInfo(session));
				session.clear();
				log.debug("After  Clear[{}] {}", annotation, getSessionInfo(session));
			}
		};
		close = new DelegatedTask<Session>(sessions) {
			@Override
			protected void doTask(Session session, String annotation) {
				log.debug("Before Close[{}] {}", annotation, getSessionInfo(session));
				session.close();
				log.debug("After  Close[{}] {}", annotation, getSessionInfo(session));
			}
		};

	}

	public void flush() {
		flush.execute();
	}

	public void clear() {
		clear.execute();
	}

	public void close() {
		close.execute();
	}

	public void print() {
		if (log.isInfoEnabled()) {
			log.info(":::TxContainer[{}] Infomation:::", this.hashCode());
			for (Map.Entry<String, Session> entry : sessions.entrySet()) {
				Transaction tx = transactions.get(entry.getKey());
				log.info("|-Session[{}]={}, {}", new Object[] { entry.getKey(), entry.getValue().hashCode(),
						getTxInfo(tx) });
			}
			log.info("\\:::");
		}
	}

	private String getTxInfo(Transaction tx) {
		StringBuffer sb = new StringBuffer();
		sb.append("Tx: ").append(tx.getClass().getSimpleName()).append("@").append(Integer.toHexString(tx.hashCode()));
		sb.append("[").append("A:").append(tx.isActive() ? "T" : "F");
		sb.append(", R:").append(tx.wasRolledBack() ? "T" : "F");
		sb.append(", C:").append(tx.wasCommitted() ? "T" : "F").append("]");
		return sb.toString();
	}

	private String getSessionInfo(Session s) {
		StringBuffer sb = new StringBuffer();
		sb.append(s.getClass().getSimpleName()).append("@").append(Integer.toHexString(s.hashCode()));
		sb.append("[").append("C:").append(s.isConnected() ? "T" : "F");
		sb.append(", D:").append(s.isDirty() ? "T" : "F");
		sb.append(", O:").append(s.isOpen() ? "T" : "F");
		sb.append(", EntityMode:").append(s.getEntityMode());
		sb.append(", FlushMode:").append(s.getFlushMode());
		sb.append("]");
		return sb.toString();
	}
}
