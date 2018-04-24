package com.cnpc.pms.personal.dao.impl;

import java.util.List;

import com.cnpc.pms.base.dao.core.impl.DAORootHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.dao.YyStoreDao;
import com.cnpc.pms.personal.entity.YyMicrData;
import com.cnpc.pms.personal.entity.YyStore;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

/**
 * 物业事业部数据实现类
 * @author zhaoxg 2016-7-22
 */
public class YyStoreDaoImpl extends DAORootHibernate implements YyStoreDao{

	/**
	 * 根据日期查询物业事业部数据
	 * @param date 日期
	 * @return 数据集合
	 */
	@Override
	public List<YyStore> queryStoreByDate(String date) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM yy_house_analysis WHERE 1=1 ");
		if(date!=null&&date.length()>2){
			sql.append(" and  yy_house_analysis.date='"+date+"'");
		}
		SQLQuery query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql.toString());
		query.addEntity(YyStore.class);
		List<YyStore> list = query.list();
		return list;
	}
	
	/**
	 * 物业事业部数据列表
	 * @param condition 查询条件
	 * @param pageInfo 分页对象
	 * @return 集合
	 */
	@Override
	public List<YyStore> getStoreList(String condition,PageInfo pageInfo) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * from yy_house_analysis GROUP BY yy_house_analysis.date having 1=1 ");
		if(condition!=null&&condition.length()>2){
			sql.append(" and date >= '"+condition.split(",")[0]+"'");
			sql.append(" and date <= '"+condition.split(",")[1]+"'");
		}
		sql.append(" ORDER BY yy_house_analysis.date desc");
		SQLQuery query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql.toString());
		query.addEntity(YyStore.class);
		List<YyStore> list = query.list();
		pageInfo.setTotalRecords(list.size());
		List<YyStore> returnList = query.setFirstResult(
				pageInfo.getRecordsPerPage() * (pageInfo.getCurrentPage() - 1))
				.setMaxResults(pageInfo.getRecordsPerPage()).list();
		return returnList;
	}
	
	/**
	 * 根据日期删除物业事业部数据
	 * @param date 日期
	 */
	@Override
	public void delStoreByDate(String date){
		StringBuffer sql = new StringBuffer();
		if(date!=null&&date.length()>2){
			sql.append("DELETE FROM yy_house_analysis WHERE yy_house_analysis.date='"+date+"'");
		}
		SQLQuery query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql.toString());
		query.executeUpdate();
	}
}
