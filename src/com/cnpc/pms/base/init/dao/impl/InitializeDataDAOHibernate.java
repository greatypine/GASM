package com.cnpc.pms.base.init.dao.impl;

import java.util.List;

import javax.transaction.TransactionManager;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.datasource.DataSourcesContainer;
import com.cnpc.pms.base.init.dao.InitializeDataDAO;
import com.cnpc.pms.base.init.script.AutoGenerateContext;
import com.cnpc.pms.base.init.script.IScriptContext;
import com.cnpc.pms.base.util.SpringHelper;

public class InitializeDataDAOHibernate extends BaseDAOHibernate implements InitializeDataDAO {

	protected boolean withTransactionManager = false;

	public InitializeDataDAOHibernate() {
		Object o = SpringHelper.getBean("transactionManager");
		if (o != null && o instanceof org.springframework.transaction.jta.JtaTransactionManager) {
			withTransactionManager = true;
		}
		log.info("Initialize InitializeDataDAOHibernate with JTA? {}", withTransactionManager);
	}

	public void importData(IScriptContext context) {
		if (withTransactionManager == true) {
			importDataWithCMT(context);
		} else {
			importDataWithSimpleTX(context);
		}
	}

	private void importDataWithSimpleTX(IScriptContext context) {
		Session session = this.getSession();
		importDataWithSession(context, session);
	}

	public void importDataInThread(IScriptContext context) {
		Session session = this.getSession();
		log.debug("Thread[{}] get Hibernate Session: {}", Thread.currentThread().getId(), session.hashCode());
		importDataWithSession(context, session);
		session.flush();
		session.close();
		log.debug("Thread[{}] close Session: {}", Thread.currentThread().getId(), session.hashCode());
	}

	public void generateData(AutoGenerateContext context) {
		int target = context.getTarget();
		int batchSize = context.getBatchSize();
		context.initContext();

		Session session = this.getSession();
		Transaction tx = session.beginTransaction();
		long start = System.currentTimeMillis();
		for (int i = 1; i <= target; i++) {
			context.execute(null);
			if (i % batchSize == 0) {
				session.flush();
				commitAndReopenTx(tx);
				session.clear();
				log.info("................. generate {} Records in {} seconds.", i,
						(System.currentTimeMillis() - start) / 1000);
			}
		}
		String summary = "#[" + Thread.currentThread().getName() + "] Generate " + target + " records in "
				+ (System.currentTimeMillis() - start) / 1000 + " seconds.";
		context.appendLog(summary);
		log.info(summary);
		session.flush();
		session.close();
	}

	// Old
	private void importDataWithSession(IScriptContext context, Session session) {
		List<?> records = context.getRecords();
		int batchSize = context.getBatchSize();
		boolean atomic = context.isAtomic();
		context.initContext();

		Transaction tx = session.beginTransaction();
		log.debug("TX: {}, atomic:{}", tx, atomic);
		int count = 1;
		int correctCounter = 0;
		long start = System.currentTimeMillis();
		boolean needRollBack = false;
		for (Object s : records) {
			log.debug("record: {}", s);
			try {
				context.execute(s);
				correctCounter++;
			} catch (Exception e) {
				context.appendLog("#" + count + "[Thread " + Thread.currentThread().getId() + "]:\t" + e.getMessage());
				context.appendLog(s.toString());
				log.error("Fail to import Record:{}", s, e);
				if (atomic == true) {
					needRollBack = true;
					// NOTE: add exit code here 13/3/8
					break;
				}
			}
			if (atomic == false && count % batchSize == 0) {
				session.flush();
				commitAndReopenTx(tx);
				session.clear();
				log.info("................. import {} Records in {} seconds.", count,
						(System.currentTimeMillis() - start) / 1000);
			}
			count++;
		}
		String summary = "#[Thread " + Thread.currentThread().getId() + "] ";
		if (needRollBack == true) {
			session.flush();
			tx.rollback();
			log.warn("Is rolled back? {}", tx.wasRolledBack());
			summary += "Rolled Back!";
			keepTxOpenForClose(tx);
		} else {
			if (context.isCommitImmediately()) {
				session.flush();
				commitAndReopenTx(tx);
			}
			summary += "Import Complete! ";
		}
		summary += " Results: Succeeded/Total " + correctCounter + "/" + (count - 1) + " Records in "
				+ (System.currentTimeMillis() - start) / 1000 + " seconds.";
		context.appendLog(summary);
		log.info(summary);
	}

	private void commitAndReopenTx(Transaction tx) {
		if (tx.isActive() == true) {
			tx.commit();
		}
		keepTxOpenForClose(tx);
	}

	private void keepTxOpenForClose(Transaction tx) {
		if (tx.isActive() == false) {
			tx.begin();
		}
	}

	private void importDataWithCMT(IScriptContext context) {
		List<?> records = context.getRecords();
		int batchSize = context.getBatchSize();
		boolean atomic = context.isAtomic();
		context.initContext();

		DataSourcesContainer container = new DataSourcesContainer();
		TransactionManager tm = (TransactionManager) SpringHelper.getBean("jtaTransactionManager");

		int count = 1;
		int correctCounter = 0;
		long start = System.currentTimeMillis();
		boolean needRollBack = false;
		for (Object s : records) {
			log.debug("record: {}", s);
			try {
				context.execute(s);
				correctCounter++;
			} catch (Exception e) {
				context.appendLog("#" + count + "[Thread " + Thread.currentThread().getId() + "]:\t" + e.getMessage());
				context.appendLog(s.toString());
				log.error("Fail to import Record:{}", s, e);
				if (atomic == true) {
					needRollBack = true;
					// NOTE: add exit code here 13/3/8
					break;
				}
			}
			if (atomic == false && count % batchSize == 0) {
				// commitAndReopenTx(tx);
				try {
					container.flush();// session.flush();TODO，最好在这里判断能否flush，否则触发异常以后，后面的tx无法重新开始
					log.debug("tx {}", tm.getTransaction());
					tm.commit();
					// tx.commit();
					tm.begin();
					// tx = tm.getTransaction();
					log.debug("new start tx {}", tm.getTransaction());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				container.clear();// session.clear();
				log.info("................. import {} Records in {} seconds.", count,
						(System.currentTimeMillis() - start) / 1000);
			}
			count++;
		}
		String summary = "#[Thread " + Thread.currentThread().getId() + "] ";
		if (needRollBack == true) {
			// session.flush();
			// tx.rollback();
			try {
				container.flush();
				tm.rollback();
				// tx.rollback();
				container.clear();
				tm.begin();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// log.warn("Is rolled back? {}", tx.wasRolledBack());
			summary += "Rolled Back!";
			// keepTxOpenForClose(tx);
		} else {
			if (context.isCommitImmediately()) {
				// session.flush();
				// commitAndReopenTx(tx);
				try {
					container.flush();
					tm.commit();// tx.commit();
					tm.begin();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			summary += "Import Complete! ";
		}
		summary += " Results: Succeeded/Total " + correctCounter + "/" + (count - 1) + " Records in "
				+ (System.currentTimeMillis() - start) / 1000 + " seconds.";
		context.appendLog(summary);
		log.info(summary);

	}

}
