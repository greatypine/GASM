package com.cnpc.pms.base.dao.core;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnpc.pms.base.paging.IPage;

public class SQLHelper {

	private Logger log = LoggerFactory.getLogger(getClass());
	private String selectString;
	private List<?> params;
	private IPage page;
	private SQLQuery query;
	private boolean needCount = false;

	public SQLHelper(String selectString, List<?> params, IPage page) {
		this.selectString = selectString;
		this.params = params;
		this.page = page;
		if (this.page != null) {
			needCount = true;
		}
	}

	@SuppressWarnings("unchecked")
	public List<List<?>> getListOfList(Session session) {
		prepareQuery(session);
		return query.setResultTransformer(Transformers.TO_LIST).list();
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, ?>> getListOfMap(Session session) {
		prepareQuery(session);
		return query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

	private void prepareQuery(Session session) {
		query = session.createSQLQuery(selectString);
		log.debug("select clause: {}", selectString);
		if (needCount == true) {
			String countString = selectString.toLowerCase();
			// countString = countString.replaceFirst("^select [^(from)]* from ", "select count(*) from ");
			int i = countString.indexOf(" from ");
			countString = "select count(*)" + countString.substring(i);
			log.debug("count clause: {}", countString);
			Query count = session.createSQLQuery(countString);
			setParameters(count);
			Number size = (Number) count.uniqueResult();
			if (size != null) {
				page.setTotalRecords(size.intValue());
			}
			query.setFirstResult(page.getStartRowPosition());
			query.setMaxResults(page.getRecordsPerPage());
		}
		setParameters(query);
	}

	private void setParameters(Query query) {
		if (null != params) {
			for (int i = 0; i < params.size(); i++) {
				query.setParameter(i, params.get(i));
			}
		}
	}
}
