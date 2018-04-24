package com.cnpc.pms.personal.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;


import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.dao.UserLoginLogDao;

public class UserLoginLogDaoImpl extends BaseDAOHibernate  implements UserLoginLogDao{

	@Override
	public List<Map<String, Object>> getUserLoginLogList(String condition,
			PageInfo pageInfo) {
		String sql="SELECT a.userGroupName,sum(a.UV) as UV,sum(a.PV) as PV,sum(a.daqweb) as daqweb,sum(a.crm) as crm,sum(a.map) as map,sum(a.service) "+ 
		"as service,sum(a.report) as report FROM (SELECT ll.userGroupName,ll.userGroupId,ll.userLoginType,ll.accessSystem, "+
		"	CASE WHEN ll.userLoginType='登录' THEN count(ll.id) ELSE 0 END as 'UV', "+
		"	CASE WHEN ll.userLoginType='刷新首页' THEN count(ll.id) ELSE 0 END 'PV', "+
		"	CASE WHEN ll.userLoginType='访问系统' AND ll.accessSystem='数据管理系统' THEN count(ll.id) ELSE 0 END as 'daqweb',"+
		"	CASE WHEN ll.userLoginType='访问系统' AND ll.accessSystem='数据动态系统' THEN count(ll.id) ELSE 0 END as 'crm', "+
		"	CASE WHEN ll.userLoginType='访问系统' AND ll.accessSystem='数据地图系统' THEN count(ll.id) ELSE 0 END as 'map',"+
		"	CASE WHEN ll.userLoginType='访问系统' AND ll.accessSystem='数据服务系统' THEN count(ll.id) ELSE 0 END as 'service',"+
		"	CASE WHEN ll.userLoginType='访问系统' AND ll.accessSystem='云表单系统' THEN count(ll.id) ELSE 0 END as 'report'   "+
		" from t_user_login_log ll GROUP BY userGroupName,userLoginType,ll.accessSystem) a GROUP BY a.userGroupName "+condition;
		
		SQLQuery query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql.toString());
		List<Map<String, Object>> returnList = query.list();
		/*List returnList = query.setFirstResult(
				pageInfo.getRecordsPerPage() * (pageInfo.getCurrentPage() - 1))
				.setMaxResults(pageInfo.getRecordsPerPage()).list();*/
		return returnList;
		
	}


	
	@Override
	public List<Map<String, Object>> getAppUserLoginLogList(String condition,
			PageInfo pageInfo) {
		String sql="SELECT a.userGroupName,sum(a.UV) as appuv,sum(a.appmap) as appmap,sum(a.apparea) as apparea,sum(a.appmessage) as appmessage,sum(a.appinput) "+ 
				" as appinput FROM (SELECT ll.userGroupName,ll.userGroupId,ll.userLoginType,ll.accessSystem, "+
				" 	CASE WHEN ll.userLoginType='APP登录' THEN count(ll.id) ELSE 0 END as 'UV', "+
				" CASE WHEN ll.userLoginType='APP访问功能' AND ll.accessSystem='地图' THEN count(ll.id) ELSE 0 END as 'appmap',"+
				" CASE WHEN ll.userLoginType='APP访问功能' AND ll.accessSystem='混片儿' THEN count(ll.id) ELSE 0 END as 'apparea',"+ 
				" CASE WHEN ll.userLoginType='APP访问功能' AND ll.accessSystem='消息' THEN count(ll.id) ELSE 0 END as 'appmessage',"+
				" CASE WHEN ll.userLoginType='APP访问功能' AND ll.accessSystem='录入' THEN count(ll.id) ELSE 0 END as 'appinput'  "+
				" FROM t_user_login_log ll GROUP BY userGroupName,userLoginType,ll.accessSystem) a GROUP BY a.userGroupName "+condition;
		
		SQLQuery query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql.toString());
		List<Map<String, Object>> returnList = query.list();
		/*List returnList = query.setFirstResult(
				pageInfo.getRecordsPerPage() * (pageInfo.getCurrentPage() - 1))
				.setMaxResults(pageInfo.getRecordsPerPage()).list();*/
		return returnList;
		
	}
	
	
	/**
	 * 日，月UV PV
	 */
	@Override
	public List<Map<String, Object>> getUserLoginLogDayMonthUvList(String condition,
			PageInfo pageInfo) {
		String sql="select b.userGroupName,sum(b.daycount) as daycounts,sum(b.monthcount) as monthcounts from (SELECT a.userGroupName,a.logindate,"+
				" CASE WHEN a.logindate=DATE_FORMAT(CURDATE(),'%Y-%m-%d') THEN sum(a.count) ELSE 0 END as daycount,"+
				" CASE WHEN DATE_FORMAT(a.logindate,'%Y-%m')=DATE_FORMAT(CURDATE(),'%Y-%m') THEN sum(a.count) ELSE 0 END as monthcount"+ 
				" from (SELECT DATE_FORMAT(ll.loginDate,'%Y-%m-%d') as logindate,ll.userGroupName,count(ll.id) as count   "+
				" FROM t_user_login_log ll WHERE ll.userLoginType='登录' GROUP BY ll.userGroupName,logindate) a GROUP BY a.userGroupName,a.logindate) b"+ 
				" GROUP BY b.userGroupName";
		SQLQuery query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql.toString());
		List<Map<String, Object>> returnList = query.list();
		return returnList;
	}
	@Override
	public List<Map<String, Object>> getUserLoginLogDayMonthPvList(String condition,
			PageInfo pageInfo) {
		String sql="select b.userGroupName,sum(b.daycount) as daycounts,sum(b.monthcount) as monthcounts from (SELECT a.userGroupName,a.refreshDate,"+
				" CASE WHEN a.refreshDate=DATE_FORMAT(CURDATE(),'%Y-%m-%d') THEN sum(a.count) ELSE 0 END as daycount,"+
				" CASE WHEN DATE_FORMAT(a.refreshDate,'%Y-%m')=DATE_FORMAT(CURDATE(),'%Y-%m') THEN sum(a.count) ELSE 0 END as monthcount"+ 
				" from (SELECT DATE_FORMAT(ll.refreshDate,'%Y-%m-%d') as refreshDate,ll.userGroupName,count(ll.id) as count   "+
				" FROM t_user_login_log ll WHERE ll.userLoginType='刷新首页' GROUP BY ll.userGroupName,refreshDate) a GROUP BY a.userGroupName,a.refreshDate) b"+ 
				" GROUP BY b.userGroupName";
		SQLQuery query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql.toString());
		List<Map<String, Object>> returnList = query.list();
		return returnList;
	}

	
	
	/**
	 * App日，月UV PV
	 */
	@Override
	public List<Map<String, Object>> getAppUserLoginLogDayMonthUvList(String condition,
			PageInfo pageInfo) {
		String sql="select b.userGroupName,sum(b.daycount) as daycounts,sum(b.monthcount) as monthcounts from (SELECT a.userGroupName,a.logindate,"+
				" CASE WHEN a.logindate=DATE_FORMAT(CURDATE(),'%Y-%m-%d') THEN sum(a.count) ELSE 0 END as daycount,"+
				" CASE WHEN DATE_FORMAT(a.logindate,'%Y-%m')=DATE_FORMAT(CURDATE(),'%Y-%m') THEN sum(a.count) ELSE 0 END as monthcount"+ 
				" from (SELECT DATE_FORMAT(ll.loginDate,'%Y-%m-%d') as logindate,ll.userGroupName,count(ll.id) as count   "+
				" FROM t_user_login_log ll WHERE ll.userLoginType='APP登录' GROUP BY ll.userGroupName,logindate) a GROUP BY a.userGroupName,a.logindate) b"+ 
				" GROUP BY b.userGroupName";
		SQLQuery query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql.toString());
		List<Map<String, Object>> returnList = query.list();
		return returnList;
	}
}
