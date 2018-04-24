package com.cnpc.pms.toolkit.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;

import com.cnpc.pms.toolkit.dao.GeneralSequenceNoDAO;

public class GeneralSequenceNoDAOImpl extends BaseDAOHibernate implements
		GeneralSequenceNoDAO {

	public Long getSequenceNo(String key, Integer stepSize) throws Exception {

		Session ss = null;
		Transaction tr = null;
		Long newSeqNo = null;
		try {
			// 得到新的连接
			ss = this.getHibernateTemplate().getSessionFactory().openSession();
			tr = ss.beginTransaction();
			// 开启事务
			tr.begin();
			SQLQuery q = null;
			// 更新加步长，并获取数据
			q = ss
					.createSQLQuery("UPDATE td_general_seq SET seqVal=seqVal+:stepSize WHERE seqKey=:keyval");
			q.setInteger("stepSize", stepSize);
			q.setString("keyval", key);
			int recCount = q.executeUpdate();
			// 更新不到记当说明没有记录，这时要进行新增，并且重新执行更新步长操作
			if (recCount < 1) {
				try {
					q = ss
							.createSQLQuery("INSERT INTO td_general_seq(seqKey,seqVal,note) VALUES(:seqKey,:seqVal,:note)");
					q.setString("seqKey", key);
					q.setLong("seqVal", 0);
					q.setString("note", "");
					q.executeUpdate();
				} catch (Exception e) {

				}
				q = ss
						.createSQLQuery("UPDATE td_general_seq SET seqVal=seqVal + :stepSize WHERE seqKey=:keyval");
				q.setInteger("stepSize", stepSize);
				q.setString("keyval", key);
				q.executeUpdate();
			}
			// 得到新增步长后的序号
			q = ss
					.createSQLQuery("SELECT seqVal FROM td_general_seq WHERE seqKey=:keyval");
			q.setString("keyval", key);
			q.addScalar("seqVal", Hibernate.LONG);
			List<Map> list = q.setResultTransformer(
					Transformers.ALIAS_TO_ENTITY_MAP).list();
			Map m = list.get(0);

			newSeqNo = (Long) m.get("seqVal");
			// 提交事务
			tr.commit();
			ss.flush();
		} catch (Exception e) {
			throw e;
		} finally {
			if (ss != null) {
				ss.close();
			}
		}
		return newSeqNo;
	}

	public String getSequenceNoStr(String key, Integer stepSize,
			Integer codeLength) throws Exception {
		String codeStr = "";
		Long seqNo = getSequenceNo(key, stepSize);
		codeStr = seqNo.toString();
		int cLen = codeStr.length();
		if (cLen < codeLength) {
			for (int i = 0; i < codeLength - cLen; i++) {
				codeStr = "0" + codeStr;
			}
		}
		return codeStr;
	}

	public Long getDefSequenceNo(String key) throws Exception {
		return getSequenceNo(key, 1);
	}

	public String getDefSequenceNoStr(String key, Integer codeLength)
			throws Exception {
		return getSequenceNoStr(key, 1, codeLength);
	}

}
