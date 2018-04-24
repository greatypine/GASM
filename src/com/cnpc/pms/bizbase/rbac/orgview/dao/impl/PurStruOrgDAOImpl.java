package com.cnpc.pms.bizbase.rbac.orgview.dao.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.bizbase.rbac.orgview.dao.PurStruOrgDAO;
import com.cnpc.pms.bizbase.rbac.orgview.entity.PurStruOrg;
//import com.cnpc.pms.bizbase.rbac.position.dto.PositionDTO;

/**
 * 
 * Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-8-21
 */
public class PurStruOrgDAOImpl extends BaseDAOHibernate implements PurStruOrgDAO {

	//public static final String ORG_QUERY = "select t3.id, t3.code, t3.name, t3.enablestate, t3.path, count(t4.id) as cnt, t3.orderno" + " from (select t2.id, t2.code, t2.name, t2.enablestate, t2.path, t2.orderno from tb_bizbase_psorg t2" + " where t2.parent_id = ? and t2.enablestate = 1) t3" + " left join tb_bizbase_psorg t4 on t4.parent_id = t3.id and t4.enablestate = 1" + " group by t3.id, t3.code, t3.name, t3.enablestate, t3.path, t3.orderno" + " order by t3.orderno, t3.code";
	public static final String ORG_QUERY = "select t3.ID, t3.CODE, t3.NAME, t3.ENABLESTATE, t3.PATH, count(t4.id) CNT, t3.ORDERNO" + " from (select t2.ID, t2.CODE, t2.NAME, t2.ENABLESTATE, t2.PATH, t2.ORDERNO from tb_bizbase_psorg t2" + " where t2.parent_id = ? and t2.enablestate = 1) t3" + " left join tb_bizbase_psorg t4 on t4.parent_id = t3.id and t4.enablestate = 1" + " group by t3.ID, t3.CODE, t3.NAME, t3.ENABLESTATE, t3.PATH, t3.ORDERNO" + " order by t3.orderno, t3.code";

	public static final String ORG_QUERY_LEV = "select t3.id, t3.code, t3.name, t3.enablestate, t3.path, count(t4.id) as cnt, t3.orderno" + " from (select t2.id, t2.code, t2.name, t2.enablestate, t2.path, t2.orderno from tb_bizbase_psorg t2" + " where t2.parent_id = ? and t2.EntityOrgFlag<=? and t2.enablestate = 1) t3" + " left join tb_bizbase_psorg t4 on t4.parent_id = t3.id and t4.enablestate = 1" + " group by t3.id, t3.code, t3.name, t3.enablestate, t3.path, t3.orderno" + " order by t3.orderno, t3.code";

	public static final String PERFORMANCE_ORG_QUERY = " SELECT t2.ID, t2.code, t2.NAME, t2.enablestate, t2.PATH, t2.orderno, 0 cnt FROM tb_bizbase_psorg t2 " + " WHERE t2.parent_id = ? AND t2.enablestate = 1 order by t2.orderno, t2.code ";

	public static final String PERFORMANCE_ORG_DQGS_QUERY = " SELECT   a.ID, a.code, a.NAME, a.enablestate, a.PATH, a.orderno, 0 cnt " + " FROM tb_bizbase_psorg a   WHERE a.localcompanyflag = '0' AND a.virtualorgflag = '1' ";

	//public static final String ORG_QUERY_ALL = "select t3.id, t3.code, t3.name, t3.enablestate, t3.path, count(t4.id) as cnt, t3.orderno" + " from (select t2.id, t2.code, t2.name, t2.enablestate, t2.path, t2.orderno from tb_bizbase_psorg t2" + " where t2.parent_id = ?) t3" + " left join tb_bizbase_psorg t4 on t4.parent_id = t3.id" + " group by t3.id, t3.code, t3.name, t3.enablestate, t3.path, t3.orderno" + " order by t3.orderno, t3.code";
	public static final String ORG_QUERY_ALL = "select t3.ID, t3.CODE, t3.NAME, t3.ENABLESTATE, t3.PATH, count(t4.id) as CNT, t3.ORDERNO" + " from (select t2.ID, t2.CODE, t2.NAME, t2.ENABLESTATE, t2.PATH, t2.ORDERNO from tb_bizbase_psorg t2" + " where t2.parent_id = ?) t3" + " left join tb_bizbase_psorg t4 on t4.parent_id = t3.id" + " group by t3.id, t3.code, t3.name, t3.enablestate, t3.path, t3.orderno" + " order by t3.orderno, t3.code";

	public static final String LOCAL_COMPANY_QUERY = "select t1.id, t1.code, t1.name, t1.path, count(child.id) as cnt from" + " tb_bizbase_psorg child," + " (select t.id, t.code, t.name, t.path from tb_bizbase_psorg t where t.parent_id = (select t2.id from tb_bizbase_psorg t2 where t2.parent_id is null) and t.enablestate = 1) t1" + " where child.parent_id =t1.id and child.enablestate = 1 and child.localcompanyflag = 0 group by t1.id, t1.code, t1.name, t1.path" + " union select org.id,org.code,org.name,org.path, 0 as cnt from" + " tb_bizbase_psorg org" + " where org.parent_id = (select t2.id from tb_bizbase_psorg t2 where t2.parent_id is null) and org.enablestate = 1 and org.localcompanyflag = 0";

	public static final String LOCAL_COMPANY_REPORT_HEAD = "select t.id, t.code, t.shortname, count(children.id) as cnt from tb_bizbase_psorg t " + "left join tb_bizbase_psorg children " + "on children.parent_id = t.id and children.isreportorg = 1 " + "and children.enablestate = 1 and children.localcompanyflag = 0" + "where t.parent_id = :parentId and t.enablestate = 1 group by t.id, t.code, t.shortname order by t.id";

	public static final String LOCAL_COMPANY_REPORT_UNIT = "select t.id, t.code, t.shortname from tb_bizbase_psorg t where t.parent_id = :parentId " + "and t.isreportorg = 1 and t.enablestate = 1" + "and t.entityOrgFlag = 0 order by t.orderNo ";

	public static final String GET_UNIT_CHILDE = "select t.id, t.code, t.shortname from tb_bizbase_psorg t where t.parent_id = :parentId " + "and t.isreportorg = 1 and t.enablestate = 1" + "and t.localcompanyflag = 0 order by t.orderNo ";

	public static final String GET_LOCAL_CHILD = "select t.id, t.code, t.shortname from tb_bizbase_psorg t where t.parent_id = :parentId " + "and t.isreportorg = 1 and t.enablestate = 1" + " order by t.orderNo ";
	public static final String GET_HEAD_CHILD = "select t.id, t.code, t.shortname from tb_bizbase_psorg t where t.parent_id = :parentId " + "and t.isreportorg = 1 and t.enablestate = 1" + " order by t.orderNo ";

	public static final String REPORT_ORGS = "select new com.cnpc.pms.bizbase.rbac.orgview.dto.PurStruOrgDTO(o.id, o.code, o.shortname)" + " from PurStruOrg o where o.parentEntity = :parentId and o.enablestate = 1" + " and o.isReportOrg = 1 order by o.orderNo ";
	public static final String BID_MANAGER_ORGS = "SELECT T2.CODE FROM (SELECT T.CODE,T.ISBIDMANAGEORG FROM TB_BIZBASE_PSORG T" + " START WITH T.PARENT_ID = :ID" + " CONNECT BY PRIOR T.ID = T.PARENT_ID) T2 WHERE T2.ISBIDMANAGEORG=1 ";

	@SuppressWarnings("unchecked")
	public List getOrgQueryResult(Long id) {
		SQLQuery orgQuery = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(ORG_QUERY);
		orgQuery.setLong(0, id);
		List<Map> list = orgQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List getOrgQueryResultByOrgFlag(Long id, String orgFlag) {
		SQLQuery orgQuery = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(ORG_QUERY_LEV);
		orgQuery.setLong(0, id);
		orgQuery.setString(1, orgFlag);
		List<Map> list = orgQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List getAllOrgQueryResult(Long id) {
		SQLQuery orgQuery = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(ORG_QUERY_ALL);
		orgQuery.setLong(0, id);
		List<Map> list = orgQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List getReportOrgs(Long orgId) {
		Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(REPORT_ORGS);
		query.setLong("parentId", orgId);
//		List<PositionDTO> positionDtos = query.list();
//		return positionDtos;
		return null;
	}

	/**
	 * 传入纯sql语句查询所有的数据
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List findBySql(final String sql, final Map params) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				if (params != null && !params.isEmpty()) {
					Iterator it = params.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry entry = (Map.Entry) it.next();
						int key = Integer.parseInt(String.valueOf(entry.getKey()));
						String value = (String) entry.getValue();
						query.setString(key, value);
					}
				}
				List list = query.list();
				return list;
			}
		});
	}

	/**
	 * 得到指定组织机构所属的所
	 */
	public String[] getProjectForecastOrg(Long orgId) {
		String queryPathSql = "SELECT PATH FROM tb_bizbase_psorg where id=:orgId";
		SQLQuery query = this.getSession().createSQLQuery(queryPathSql);
		query.setLong("orgId", orgId);
		String path = (String) query.uniqueResult();
		String queryForecastOrgSql = "SELECT ID||'@@'||CODE||'@@'||NAME||'@@'||SHORTNAME||'@@'||BUSINESSTYPE FROM tb_bizbase_psorg where :path like path||'%' and EntityOrgFlag=2 order by path desc";
		SQLQuery castQuery = this.getSession().createSQLQuery(queryForecastOrgSql);
		castQuery.setString("path", path);
		String forecastOrgInfo = (String) castQuery.uniqueResult();
		String[] rval = null;
		if (StringUtils.isNotEmpty(forecastOrgInfo)) {
			rval = forecastOrgInfo.split("@@");
		}
		return rval;
	}

	/**
	 * 根据组织机构ID得到组织机构的属性
	 */
	public String getOrgFlag(Long orgId) {
		String queryPathSql = "SELECT EntityOrgFlag FROM tb_bizbase_psorg where id=:orgId";
		SQLQuery query = this.getSession().createSQLQuery(queryPathSql);
		query.setLong("orgId", orgId);
		String flag = (String) query.uniqueResult();
		return flag;
	}

	/**
	 * 得到组织机构的全名
	 * 
	 * @param orgId
	 *            组织机构ID
	 * @param nameLev
	 *            从指定层级开始拼接
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getOrgFullName(Long orgId, Integer nameLev) {
		String fullName = "";
		//String querySql = "select name from tb_bizbase_psorg where entityorgflag>=:nameLev start with id=:orgId connect by prior parent_id=id  order by path";
		//edit by liujunsong 2014-4-16
		String querySql = "select name from tb_bizbase_psorg where entityorgflag>=:nameLev and id=:orgId  order by path";
		//edit by liujunsong end.
		SQLQuery query = this.getSession().createSQLQuery(querySql);
		query.setInteger("nameLev", nameLev);
		query.setLong("orgId", orgId);
		query.addScalar("name", Hibernate.STRING);
		List rs = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if (rs != null && rs.size() > 0) {
			for (int i = 0; i < rs.size(); i++) {
				fullName += "-" + ((Map) rs.get(i)).get("name").toString();
			}
		}
		if (fullName.length() > 0)
			fullName = fullName.substring(1);
		return fullName;
	}

	/**
	 * 应该支持找到全部，找到所有分院的，找到指定code下的所有的，根据院找到所有的所
	 * scope如果是"all"找到的是所有的，则忽略后面两个参数,否则传递"other"
	 * orgcode是指定的组织机构CODE
	 */
	public List<PurStruOrg> getOrgByParam(String scope, String orgCode) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from tb_bizbase_psorg where 1=1 and enablestate=1");
		if (scope == null ||scope.equals("all")) {
			sql.append(" and parent_id is null");
		} else {
			if (orgCode != null &&!orgCode.equals("")&& !orgCode.equals("null")) {
				sql.append(" and parent_id=(select id from tb_bizbase_psorg where code='");
				sql.append(orgCode);
				sql.append("')");
			} else {
				sql.append(" and EntityOrgFlag='");
				// 1是分院
				sql.append("1");
				sql.append("'");
			}
		}
		sql.append(" order by orderno asc");
		SQLQuery toDoQuery = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		toDoQuery.addEntity(PurStruOrg.class);
		List<PurStruOrg> list = toDoQuery.list();
		return list;
	}

	public int getChildsByParentId(Long id) {
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) RESULTCOUNT from tb_bizbase_psorg where enablestate=1");
		if(id!=null){
			sql.append(" and parent_id=");
			sql.append(id);
		}
		BigDecimal resultCount = new BigDecimal("0");
		SQLQuery toDoQuery = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		List<Map> list = toDoQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if (list != null && list.size() != 0) {
			Map doto = list.get(0);
			//resultCount = (BigDecimal) doto.get("RESULTCOUNT");
			//edit by liujunsong 2014-4-28
			resultCount = new BigDecimal( doto.get("RESULTCOUNT").toString());
			//edit by liujunsong end
		}
		return resultCount.intValue();
	}
	public List<PurStruOrg> getPurStruOrg(){
		StringBuffer sql = new StringBuffer();
		sql.append("select * from tb_bizbase_psorg where entityorgflag='0' and parent_id is null or entityorgflag='1' order by path asc,name asc");
		SQLQuery toDoQuery = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		toDoQuery.addEntity(PurStruOrg.class);
		List<PurStruOrg> list = toDoQuery.list();
		return list;
	}
	public List<PurStruOrg> getOrgsByParentId(Long id) {

		StringBuffer sql=new StringBuffer();
		sql.append("SELECT * FROM tb_bizbase_psorg t ");
		sql.append(" WHERE t.parent_id=?  AND t.enablestate=1");
		SQLQuery toDoQuery = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		toDoQuery.setLong(0,id);
		toDoQuery.addEntity(PurStruOrg.class);
		List<PurStruOrg> list = toDoQuery.list();
		return list;
	}

	
	

	@Override
	public String queryPurStruOrgCodeForUserCode(String user_code) {
		StringBuilder sql = new StringBuilder();
		sql.append("\n SELECT fieldvalue FROM tb_bizbase_condition t1 LEFT JOIN tb_bizbase_privilege t2 ");
		sql.append("\n ON t1.pk_privilege=t2.id ");
		sql.append("\n LEFT JOIN tb_bizbase_user t3 ON t2.pk_usergroup = t3.pk_usergroup ");
		sql.append("\n WHERE t3.code=:user_code ");
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		query.setString("user_code", user_code);
		Object obj = query.uniqueResult();
		if(obj instanceof String){
			String result = "'".concat(obj.toString().replace(",", "','").concat("'"));
			return result;
		}
		return null;
	}
}
