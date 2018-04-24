package com.cnpc.pms.personal.dao.impl;

import java.util.List;

import com.cnpc.pms.base.dao.core.impl.DAORootHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.dao.YyMicrDataDao;
import com.cnpc.pms.personal.dao.YyStoreDao;
import com.cnpc.pms.personal.entity.YyMicrData;
import com.cnpc.pms.personal.entity.YyStore;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

/**
 * 生活宝 微超数据实现类
 * @author zhaoxg 2016-7-18
 *
 */
public class YyMicrDataDaoImpl extends DAORootHibernate implements YyMicrDataDao{

	/**
	 * 生活宝数据信息列表
	 * @param condition 查询条件
	 * @param pageInfo 分页对象
	 * @return 生活宝集合
	 */
	@Override
	public List<YyMicrData> getSHBMicrDataList(String condition,PageInfo pageInfo) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * from yy_micr_data_collect WHERE yy_micr_data_collect.type='生活宝'  GROUP BY yy_micr_data_collect.date having 1=1 ");
		if(condition!=null&&condition.length()>2){
			sql.append(" and date >= '"+condition.split(",")[0]+"'");
			sql.append(" and date <= '"+condition.split(",")[1]+"'");
		}
		sql.append(" order by yy_micr_data_collect.date desc");
		SQLQuery query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql.toString());
		query.addEntity(YyMicrData.class);
		List<YyMicrData> list = query.list();
		pageInfo.setTotalRecords(list.size());
		List<YyMicrData> returnList = query.setFirstResult(
				pageInfo.getRecordsPerPage() * (pageInfo.getCurrentPage() - 1))
				.setMaxResults(pageInfo.getRecordsPerPage()).list();
		return returnList;
	}
	
	/**
	 * 微超数据信息列表
	 * @param condition 查询条件
	 * @param pageInfo 分页对象
	 * @return 微超集合
	 */
	@Override
	public List<YyMicrData> getWCMicrDataList(String condition,PageInfo pageInfo) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * from yy_micr_data_collect WHERE yy_micr_data_collect.type='微超' GROUP BY yy_micr_data_collect.date having 1=1 ");
		if(condition!=null&&condition.length()>2){
			sql.append(" and date >= '"+condition.split(",")[0]+"'");
			sql.append(" and date <= '"+condition.split(",")[1]+"'");
		}
		sql.append(" order by yy_micr_data_collect.date desc");
		SQLQuery query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql.toString());
		query.addEntity(YyMicrData.class);
		List<YyMicrData> list = query.list();
		pageInfo.setTotalRecords(list.size());
		List<YyMicrData> returnList = query.setFirstResult(
				pageInfo.getRecordsPerPage() * (pageInfo.getCurrentPage() - 1))
				.setMaxResults(pageInfo.getRecordsPerPage()).list();
		return returnList;
	}

	/**
	 * 根据日期查询生活宝信息
	 * @param date 日期时间
	 * @return 生活宝集合
	 */
	@Override
	public List<YyMicrData> queryMicrDataShbByDate(String date) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * from yy_micr_data_collect WHERE yy_micr_data_collect.type='生活宝' ");
		if(date!=null&&date.length()>2){
			sql.append(" and yy_micr_data_collect.date ='"+date+"'");
		}
		SQLQuery query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql.toString());
		query.addEntity(YyMicrData.class);
		List<YyMicrData> list = query.list();
		return list;
	}
	
	/**
	 * 根据日期查询微超信息
	 * @param date 日期时间
	 * @return 微超集合
	 */
	@Override
	public List<YyMicrData> queryMicrDataWcByDate(String date) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * from yy_micr_data_collect WHERE yy_micr_data_collect.type='微超' ");
		if(date!=null&&date.length()>2){
			sql.append(" and yy_micr_data_collect.date ='"+date+"'");
		}
		SQLQuery query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql.toString());
		query.addEntity(YyMicrData.class);
		List<YyMicrData> list = query.list();
		return list;
	}
	
	/**
	 * 根据日期以及类型删除运营数据（生活宝或微超）
	 * @param date 日期时间
	 * @param type 类型（生活宝或微超）
	 */
	@Override
	public void delMicrDataByDate(String date,String type){
		StringBuffer sql = new StringBuffer();
		if(date!=null&&date.length()>2){
			sql.append("delete from yy_micr_data_collect WHERE yy_micr_data_collect.type='"+type+"' ");
			sql.append(" and yy_micr_data_collect.date ='"+date+"'");
		}
		SQLQuery query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql.toString());
		query.executeUpdate();
	}
	
}
