/**
 * 
 */
package com.cnpc.pms.bizbase.rbac.usermanage.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.Type;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.bizbase.rbac.usermanage.dao.UserDAO;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;

/**
 * 自己写的一个dao,用于与中石油身份统一认真接口 Copyright(c) 2011 China National Petroleum
 * Corporation , http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-6-30
 */
public class UserDAOImpl extends BaseDAOHibernate implements UserDAO {

	/** The Constant POSITION_QUERY. */
	public static final String POSITION_QUERY = "select count(1) from tb_bizbase_position t where t.purstruorgid = :orgId and"
			+ " t.cnpcpositionid = :cnpcpositionid";
	public static final String INNERUSER_QUERY = "select t.name,t.code,t.employeeid,t.phone,t.mobilephone,t.email,t.disabledFlag,t2.code as orgCode "
			+ "from tb_bizbase_user t left join tb_bizbase_psorg t2 on t.pk_org=t2.id where t.doctype=0";
	public static final String EXP_USER_INFO_STRING = "select t.name from tb_exp_expertinfo t where t.id = :expId";

	/**
	 * 获取用户的功能权限菜单
	 */
	public static final String USER_ACL_QUERY = "select distinct  t.id,t.activity_code ,t.url as url from tb_bizbase_function t"
			+ "  left join tb_bizbase_role_function t1 on t1.pk_activity = t.id "
			+ "  left join tb_bizbase_role t2 on t2.id = t1.pk_role "
			+ "  left join tb_bizbase_usergroup_role t3 on t3.pk_role = t2.id"
			+ "  left join tb_bizbase_usergroup t4 on t4.id=t3.pk_usergroup "
			+ "  left join tb_bizbase_user t5 on t5.pk_usergroup = t4.id "
			+ "  where t5.code = :userCode and t2.disabledflag=1 ";

	/**
	 * 获取用户的功能权限模块菜单
	 */
	public static final String USER_MODULEACL_QUERY = "select distinct t.activity_code  from tb_bizbase_function t"
			+ "  left join tb_bizbase_role_function t1 on t1.pk_activity = t.id "
			+ "  left join tb_bizbase_role t2 on t2.id = t1.pk_role "
			+ "  left join tb_bizbase_usergroup_role t3 on t3.pk_role = t2.id"
			+ "  left join tb_bizbase_usergroup t4 on t4.id=t3.pk_usergroup "
			+ "  left join tb_bizbase_user t5 on t5.pk_usergroup = t4.id "
			+ "  where t5.code = :userCode and t2.disabledflag=1 and t.parent_code = "
			+ "(select f.id from tb_bizbase_function f where f.parent_code is null )";

	/**
	 * 获取角色组的所有角色,分区之后按长度排序
	 */
//	public static final String USERGROUP_ROLES_QUERY = "select t1.*,row_number() over(partition by substr(t1.code,1,6) order by length(t1.code),t1.code) gid "
//			+ " from tb_bizbase_role t1,tb_bizbase_usergroup_role t2 "
//			+ " where t1.id=t2.pk_role and t2.pk_usergroup = :usergroupId ";
	// 2015-12-01 modify by zhulei 
	public static final String USERGROUP_ROLES_QUERY = "select t1.* "
			+ " from tb_bizbase_role t1,tb_bizbase_usergroup_role t2 "
			+ " where t1.id=t2.pk_role and t2.pk_usergroup = :usergroupId ";


	// public static final String SEARCH_UPDATE =
	// "select t.name,t.code,t.employeeid,t.phone,t.mobilephone,t.email,t.disabledFlag,t2.code as orgCode "
	// +
	// " from tb_bizbase_user t left join tb_bizbase_psorg t2 on t.pk_org=t2.id "
	// +
	// " where  t.doctype = 0 and t.lastmodifydate > to_date('2001-08-20','yyyy-mm-dd')";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.usermanage.dao.UserDAO#examineUserPosition(
	 * java.lang.Long, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public boolean examineUserPosition(Long orgId, Long cnpcPositionId) {
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(POSITION_QUERY);
		query.setLong("orgId", orgId);
		query.setLong("cnpcpositionid", cnpcPositionId);
		List<Map> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		for (Map map : list) {
			BigDecimal count = (BigDecimal) map.get("COUNT(1)");
			if (count.intValue() > 0) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.usermanage.dao.UserDAO#getAllInnerUserByDate
	 * (java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List getAllInnerUserByDate(String updateTime) {
		String condition = "";
		if (null != updateTime && !"".equals(updateTime)) {
			condition = " and t.lastmodifydate >= to_date('" + updateTime + "','yyyy-mm-dd')";
		}
		String queryString = INNERUSER_QUERY + condition;
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(queryString);
		List<Map> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public String getExpUserInfo(Long id) {
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createSQLQuery(EXP_USER_INFO_STRING);
		query.setLong("expId", id);
		List<Map> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if (null == list || list.size() == 0) {
			return "";
		}
		String name = (String) list.get(0).get("NAME");
		return name;
	}

	@SuppressWarnings("unchecked")
	public List<Map> getUserACL(String userCode) {
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(USER_ACL_QUERY);
		query.setString("userCode", userCode);
		List<Map> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.usermanage.dao.UserDAO#getUserModuleCode(java
	 * .lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<String> getUserModuleCode(String userCode) {
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createSQLQuery(USER_MODULEACL_QUERY);
		query.setString("userCode", userCode);
		List<Map> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		List<String> moduleList = new ArrayList<String>();
		for (Map map : list) {
			String code = (String) map.get("activity_code");
			moduleList.add(code);
		}
		return moduleList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.usermanage.dao.UserDAO#getUserButtonCode(java
	 * .lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<String> getUserButtonCode(String userCode) {
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createSQLQuery(USER_ACL_QUERY + " and t.type=2");
		query.setString("userCode", userCode);
		List<Map> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		List<String> buttonList = new ArrayList<String>();
		for (Map map : list) {
			String code = (String) map.get("activity_code");
			buttonList.add(code);
		}
		return buttonList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.usermanage.dao.UserDAO#getRolesByUserGroupId
	 * (java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getRolesByUserGroupId(Long usergroupId) {
		Query query = this.getSession().createSQLQuery(USERGROUP_ROLES_QUERY);
		query.setLong("usergroupId", usergroupId);
		List<Map> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}

	public List<User> getUsersByPositionCodes(Long orgId, List<String> positionCodes) {

		String queryString = "SELECT U.*" + "  FROM TB_BIZBASE_CNPCPOSITION C" + "  LEFT JOIN TB_BIZBASE_USER U"
				+ "    ON C.ID = U.PK_POSITION" + " WHERE C.CODE IN (";
		for (int i = 0; i < positionCodes.size(); i++) {
			String code = positionCodes.get(i);
			if (i == positionCodes.size() - 1) {
				queryString += "'" + code + "'";
			} else {
				queryString += "'" + code + "',";
			}
		}
		queryString += ") AND (U.PK_ORG = '" + orgId + "' OR U.PK_ORG IN"
				+ " (SELECT O.ID FROM TB_BIZBASE_PSORG O WHERE O.PARENT_ID = '" + orgId + "'))";
		SQLQuery query = this.getSession().createSQLQuery(queryString);
		query.addEntity(User.class);
		List<User> ls = query.list();
		return ls;
	}

	public void deleteRecords(Long sheetId) {
		String delsql = "update tb_bizbase_copyrecord t set t.sheetstatus='1' where t.sheetid=?";
		this.getSession().createSQLQuery(delsql).setParameters(new Object[] { sheetId }, new Type[] { Hibernate.LONG })
				.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<User> getUsersBySheetId(Long sheetId) {
		String hql = "select u from User u,CopyRecord c where u.code=c.userCode and c.sheetId=? and c.sheetStatus='0'";
		List<User> list = this.getSession().createQuery(hql)
				.setParameters(new Object[] { sheetId }, new Type[] { Hibernate.LONG }).list();
		return list;
	}

	public List<User> getUsersByPsCodes() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT U.* FROM TB_BIZBASE_CNPCPOSITION ps LEFT JOIN TB_BIZBASE_USER U ON ps.ID=U.PK_POSITION");
		sql.append(" WHERE ps.code IN('KTYWZG','KFYWZG','GCYWZG','HWYWZG','XXYWZG','TJGLG') ORDER BY U.orderNo");
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		query.addEntity(User.class);
		List<User> ls = query.list();
		return ls;
	}

	public List<User> getUsersByOrgAndPosition(Long orgId, Long posId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT U.* FROM TB_BIZBASE_CNPCPOSITION ps LEFT JOIN TB_BIZBASE_USER U ON ps.ID=U.PK_POSITION");
		sql.append(" WHERE ps.id =:posId AND u.pk_org IN (SELECT org.id FROM tb_bizbase_psorg Org start with org.id=:orgId");
		sql.append(" connect by prior org.id=org.parent_id)");
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		query.setLong("posId", posId);
		query.setLong("orgId", orgId);
		query.addEntity(User.class);
		List<User> ls = query.list();
		return ls;
	}

	public List<User> getUsersByName(String userName) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM TB_BIZBASE_USER ");
		sql.append(" WHERE name =:uname ");
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		query.setString("uname", userName);
		query.addEntity(User.class);
		List<User> ls = query.list();
		return ls;
	}

	public List<User> getUserByPsCodes(String psCode) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT U.* FROM TB_BIZBASE_CNPCPOSITION ps LEFT JOIN TB_BIZBASE_USER U ON ps.ID=U.PK_POSITION");
		sql.append(" WHERE ps.code ='");
		sql.append(psCode);
		sql.append("' ORDER BY U.orderNo");
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		query.addEntity(User.class);
		List<User> ls = query.list();
		return ls;
	}

	@Override
	public List<User> getListUserByCity(String cityname) {
		String sql=null;
		sql="SELECT * FROM tb_bizbase_user u LEFT JOIN t_storekeeper s ON u.employeeId = s.employee_no WHERE u.zw = '店长' AND s.zw = '店长' AND s.citySelect LIKE '%"+cityname+"%'";

		/*if(cityname!=null){
			sql="SELECT * FROM tb_bizbase_user u LEFT JOIN t_storekeeper s ON u.employeeId = s.employee_no WHERE u.zw = '店长' AND s.zw = '店长' AND s.citySelect LIKE '%"+cityname+"%'";
		}else{
			sql="SELECT * FROM tb_bizbase_user u LEFT JOIN t_storekeeper s ON u.employeeId = s.employee_no WHERE u.zw = '店长' AND s.zw = '店长'";
			
		}*/
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.addEntity(User.class);
		List<User> ls = query.list();
		return ls;
	}

	
	@Override
	public List<Map<String, Object>> checkUserDuty(String emplyeeNo) {
		String sql="select remark,zw,name,employee_no from t_humanresources as h where h.employee_no='"+emplyeeNo+"'";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	
	@Override
	public List<Map<String, Object>> selectStoreEmployee(Long storeId) {
		String sql=" select a.*,s.address from  (select count(1) as total,zw,name ,store_id,phone from t_humanresources where store_id="+storeId+" and humanstatus=1 and  zw in ('服务专员','事务管理','综合专员','综合管理','国安侠','副店长','店长') group by zw) as a inner join t_store as s on a.store_id=s.store_id";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	
	@Override
	public List<Map<String, Object>> getEmployeeOfStore(String where) {
		
		
		
		String sql="select * from t_humanresources where "+where+" order by topostdate desc";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	
	@Override
	public Map<String, Object> getStoreKeeperInfo(Long storeId) {
		String sql="select u.name,u.mobilephone,s.name as storeName,s.storeno from tb_bizbase_user as u INNER JOIN t_store as s on u.id = s.skid and s.store_id="+storeId;
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data==null?null:lst_data.get(0);
	}

	
	@Override
	public List<Map<String, Object>> getEmployeeOfStore(Long cityId, Long employeeId, String role) {
		String whereStr="";
		if("CSZJ".equals(role)){//城市总监
			if(employeeId!=null&&!"".equals(employeeId)){
				whereStr="select t.store_id,name as storeName from t_store t  inner join  (select tdc.id,tdc.cityname from t_dist_city a"+  
						
					"   INNER JOIN t_dist_citycode tdc on  a.citycode = tdc.citycode and a.pk_userid="+employeeId+" and tdc.id="+cityId+") t1"+
					"	 on t.city_name  = t1.cityname";
			}else{
				whereStr="select t.store_id,name as storeName from t_store t  inner join  (select tdc.id,tdc.cityname from "+  
					"    t_dist_citycode tdc where tdc.id="+cityId+") t1"+
					"	 on t.city_name  = t1.cityname";
			}
			
		}else if("QYJL".equals(role)){//区域经理
			whereStr= "select store_id,name as storeName from t_store where rmid = "+employeeId;
		}
		
		
		
		String  sql = "SELECT a.name,a.employee_no,b.storeName,a.sex,a.authorizedtype,a.topostdate FROM `t_humanresources` a INNER JOIN ("+whereStr+") b on a.store_id = b.store_id and  a.humanstatus =1 ";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	
	@Override
	public List<Map<String, Object>> getEntryOrLeaveEmployeeOfStore(Long cityId, String employeeId, String role,
			String status) {
		String whereStr="";
		if("CSZJ".equals(role)){//城市总监
			whereStr="select t.store_id from t_store t  inner join  (select tdc.id,tdc.cityname from t_general a"+  
					"   INNER JOIN t_general_city_reference b on a.employee_no='"+employeeId+"' and a.id = b.gmid and b.ctid="+cityId+
					"   INNER JOIN t_dist_citycode tdc on b.ctid = tdc.id ) t1"+
					"	 on t.city_name  = t1.cityname";
		}else if("QYJL".equals(role)){//区域经理
			whereStr= "select store_id from t_store where rmid = (select id from tb_bizbase_user where employeeId='"+employeeId+"')";
		}
		String sql = "";
		if("entry".equals(status)){//入职
			  sql = "SELECT count(1) as total,DATE_FORMAT(DATE(a.topostdate),'%c月') as c_date  FROM `t_humanresources` a INNER JOIN ("+whereStr+") b on a.store_id = b.store_id and  a.humanstatus =1 and YEAR(DATE(a.topostdate))=YEAR(CURDATE()) group by DATE_FORMAT(DATE(a.topostdate),'%Y-%m')";
		}else if("leave".equals(status)){//离职
			  sql = "SELECT count(1) as total,DATE_FORMAT(DATE(a.leavedate),'%c月') as c_date FROM `t_humanresources` a INNER JOIN ("+whereStr+") b on a.store_id = b.store_id and  a.humanstatus =2 and YEAR(DATE(a.topostdate))=YEAR(CURDATE()) group by DATE_FORMAT(DATE(a.leavedate),'%Y-%m')";
		}

		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

}
