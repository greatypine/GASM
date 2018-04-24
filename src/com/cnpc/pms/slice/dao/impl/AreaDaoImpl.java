/**
 * gaobaolei
 */
package com.cnpc.pms.slice.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.dynamic.entity.DynamicDto;
import com.cnpc.pms.slice.dao.AreaDao;
import com.cnpc.pms.slice.entity.Area;
import com.cnpc.pms.slice.entity.AreaInfo;

/**
 * @author gaobaolei
 *
 */
public class AreaDaoImpl extends BaseDAOHibernate implements AreaDao {

	@Override
	public List<Map<String, Object>> selectAreaInfo(AreaInfo ai, String actionType) {
		String sql = "";
		if (ai.getTin_village_id() == null) {// 小区-'全部'
			// sql = " SELECT area_id FROM t_area ta, t_area_info tai WHERE
			// ta.id = tai.area_id AND ta.status=0 AND
			// tai.village_id="+ai.getVillage_id();
			sql = "select ifnull(b.name,'') as name,a.area_id,1 as flag,a.village_id  from  t_area_info a left  join t_village b on a.village_id = b.id where a.village_id   = "
					+ ai.getVillage_id() + " and a.status=0 ";
			if ("edit".equals(actionType)) {
				sql = sql + "  and a.area_id!=" + ai.getArea_id();
			}
			sql = sql + " group by a.village_id";
		} else if (ai.getTin_village_id() != null) {
			// if(ai.getBuilding_id()==null){
			// sql = " SELECT area_id FROM t_area ta, t_area_info tai WHERE
			// ta.id = tai.area_id AND ta.status=0 AND
			// tai.tin_village_id="+ai.getTin_village_id()+" AND
			// tai.village_id="+ai.getVillage_id();
			// }else{
			// sql = " SELECT area_id FROM t_area ta, t_area_info tai WHERE
			// ta.id = tai.area_id AND ta.status=0 AND
			// tai.building_id="+ai.getBuilding_id()+" AND
			// tai.build_model="+ai.getBuild_model()+" AND
			// tai.tin_village_id="+ai.getTin_village_id()+" AND
			// tai.village_id="+ai.getVillage_id();
			// sql = " SELECT area_id FROM t_area ta, t_area_info tai WHERE
			// ta.id = tai.area_id AND ta.status=0 AND
			// (tai.tin_village_id="+ai.getTin_village_id()+" OR
			// tai.tin_village_id is null) AND
			// tai.village_id="+ai.getVillage_id();

			sql = "select c.*,2 as flag from ( select a.area_id,a.tin_village_id,ifnull(b.name,'') as name  from t_area_info a left join t_tiny_village b on a.tin_village_id = b.id  where a.status=0  and a.tin_village_id is not null "
					+ " union all "
					+ " select a.area_id,b.id as tin_village_id,ifnull(b.name,'') as name  from (select * from t_area_info where status=0 and tin_village_id is null) a left join t_tiny_village b  on  a.village_id = b.village_id ) c  where c.tin_village_id = "
					+ ai.getTin_village_id() + " ";
			// }

			if ("edit".equals(actionType)) {
				sql = sql + "  and area_id!=" + ai.getArea_id();
			}

			sql = sql + " group by tin_village_id";
		}

		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);

		return query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

	@Override
	public int selectArea(Area area, String actionType) {
		String sql = "SELECT name FROM t_area WHERE status=0 and name='" + area.getName() + "' AND store_id="
				+ area.getStore_id();
		if ("edit".equals(actionType)) {
			sql = sql + "  AND id!=" + area.getId();
		}
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);

		return query.list().size();
	}

	@Override
	public List<Map<String, Object>> selectAreaDto(String whereStr, PageInfo pageInfo) {
		/*
		 * String sql =
		 * " SELECT T.name,T.id,T.store_id,T.villageName,T.tinVillageName,T.employeeName FROM ("
		 * +
		 * "SELECT ta.name,ta.id ,ta.store_id, concat(GROUP_CONCAT(DISTINCT tv.name),'') AS villageName,concat(GROUP_CONCAT(DISTINCT ifnull(ttv.name,(SELECT GROUP_CONCAT(`name`) FROM t_tiny_village WHERE t_tiny_village.village_id = tv.id ))),'') as tinVillageName,concat(GROUP_CONCAT( DISTINCT tbu.name),'') as employeeName FROM t_area ta  "
		 * +"INNER JOIN t_area_info tai ON ta.id = tai.area_id AND ta.status=0 "
		 * +"LEFT JOIN t_village tv ON tai.village_id = tv.id " +
		 * "LEFT JOIN t_tiny_village ttv ON tv.id = ttv.village_id AND tai.tin_village_id = ttv.id "
		 * +
		 * "LEFT JOIN tb_bizbase_user tbu on (ta.employee_a_no = tbu.employeeId or ta.employee_b_no = tbu.employeeId) GROUP BY ta.store_id, ta.name,ta.id) T where 1=1 "
		 * +whereStr;
		 */

		String sql = "select a.area_no,a.name,concat(GROUP_CONCAT(DISTINCT(a.townName)),'') as townName,concat(GROUP_CONCAT(DISTINCT(a.villageName)),'') as villageName,concat(GROUP_CONCAT(a.tinVillageName),'') as tinVillageName,a.id,a.store_id,a.employee_a_no,a.employee_b_no,a.employeeName from "
				+ "(select ttv.name as tinVillageName,ta.store_id,ta.name ,ta.area_no,ta.id,ta.employee_a_no,ta.employee_b_no,concat(ifnull(ta.employee_a_name,'无'),',',ifnull(ta.employee_b_name,'无')) as employeeName,tv.name as villageName,tt.name as townName from  "
				+ "t_area ta " + "INNER JOIN t_area_info tai ON ta.id = tai.area_id "
				+ "AND ta. STATUS = 0  and tai.tin_village_id is null " + "INNER JOIN t_town tt on tai.town_id = tt.id "
				+ "INNER JOIN t_village as tv on tv.id = tai.village_id "
				+ "LEFT JOIN t_tiny_village as ttv on tai.village_id = ttv.village_id where ttv.status=0 and  (ttv.dellable <> 1 or ttv.dellable is null) "
				+ "UNION "
				+ "select ttv.name as tinVillageName,ta.store_id,ta.name ,ta.area_no,ta.id,ta.employee_a_no,ta.employee_b_no,concat(ifnull(ta.employee_a_name,'无'),',',ifnull(ta.employee_b_name,'无')) as employeeName,tv.name as villageName,tt.name as townName from  "
				+ "t_area ta " + "INNER JOIN t_area_info tai ON ta.id = tai.area_id "
				+ "AND ta. STATUS = 0  and tai.tin_village_id is not null "
				+ "INNER JOIN t_town tt on tai.town_id = tt.id "
				+ "INNER JOIN t_tiny_village as ttv on tai.tin_village_id = ttv.id "
				+ "INNER JOIN t_village as tv on tv.id = tai.village_id) as a " + "GROUP BY a.id  " + whereStr;

		sql = sql + " ORDER BY a.id desc ";
		// SQL查询对象
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);

		pageInfo.setTotalRecords(query.list().size());
		// 获得查询数据
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
				.setFirstResult(pageInfo.getRecordsPerPage() * (pageInfo.getCurrentPage() - 1))
				.setMaxResults(pageInfo.getRecordsPerPage()).list();

		// 如果没有数据返回
		if (lst_data == null || lst_data.size() == 0) {
			return new ArrayList<Map<String, Object>>();
		}

		return (List<Map<String, Object>>) lst_data;

	}

	@Override
	public List<Map<String, Object>> selectAreaDto(Map<String, Object> param) throws Exception {
		/*
		 * String sql =
		 * " SELECT T.name AS '片区名称',T.id,T.store_id,T.villageName AS '社区',T.tinVillageName AS '小区',T.employeeName AS '国安侠' FROM ("
		 * +
		 * "SELECT ta.name,ta.id ,ta.store_id, concat(GROUP_CONCAT(DISTINCT tv.name),'') AS villageName,concat(GROUP_CONCAT(DISTINCT ifnull(ttv.name,(SELECT GROUP_CONCAT(`name`) FROM t_tiny_village WHERE t_tiny_village.village_id = tv.id ))),'') as tinVillageName,concat(GROUP_CONCAT( DISTINCT tbu.name),'') as employeeName FROM t_area ta  "
		 * +"INNER JOIN t_area_info tai ON ta.id = tai.area_id AND ta.status=0 "
		 * +"LEFT JOIN t_village tv ON tai.village_id = tv.id " +
		 * "LEFT JOIN t_tiny_village ttv ON tv.id = ttv.village_id AND tai.tin_village_id = ttv.id "
		 * +
		 * "LEFT JOIN tb_bizbase_user tbu on (ta.employee_a_no = tbu.employeeId or ta.employee_b_no = tbu.employeeId) GROUP BY ta.store_id, ta.name,ta.id) T  where T.store_id="
		 * +param.get("storeId")+" ";
		 */
		String sql = "select  ts.city_name as '城市',ts.storeno  as '门店编号',ts.name as '门店名称',b.area_no as '片区编号',b.name AS '片区名称',b.id,b.store_id,b.townName as '街道',b.villageName AS '社区',b.tinVillageName AS '小区',b.employee_a_name AS 'A国安侠',b.employee_b_name AS 'B国安侠' from (select a.name,concat(GROUP_CONCAT(DISTINCT(a.townName)),'') as townName,concat(GROUP_CONCAT(DISTINCT(a.villageName)),'') as villageName,concat(GROUP_CONCAT(a.tinVillageName),'') as tinVillageName,a.id,a.store_id,a.employee_a_no,a.employee_b_no,a.employee_a_name,a.employee_b_name,a.area_no,a.employeeName from "
				+ "(select ttv.name as tinVillageName,ta.store_id,ta.name ,ta.area_no,ta.id,ta.employee_a_no,ta.employee_b_no,tv.name as villageName,ta.employee_a_name,ta.employee_b_name,concat(ifnull(ta.employee_a_name,'无'),',',ifnull(ta.employee_b_name,'无')) as employeeName,tt.name as townName from  "
				+ "t_area ta " + "INNER JOIN t_area_info tai ON ta.id = tai.area_id "
				+ "AND ta. STATUS = 0  and tai.tin_village_id is null " + "INNER JOIN t_town tt on tai.town_id = tt.id "
				+ "INNER JOIN t_tiny_village as ttv on tai.village_id = ttv.village_id and ttv.status=0 and  (ttv.dellable <> 1 or ttv.dellable is null)"
				+ "INNER JOIN t_village as tv on tv.id = tai.village_id " + "UNION "
				+ "select ttv.name as tinVillageName,ta.store_id,ta.name ,ta.area_no,ta.id,ta.employee_a_no,ta.employee_b_no,tv.name as villageName,ta.employee_a_name,ta.employee_b_name,concat(ifnull(ta.employee_a_name,'无'),',',ifnull(ta.employee_b_name,'无')) as employeeName,tt.name as townName from  "
				+ "t_area ta " + "INNER JOIN t_area_info tai ON ta.id = tai.area_id "
				+ "AND ta. STATUS = 0  and tai.tin_village_id is not null "
				+ "INNER JOIN t_town tt on tai.town_id = tt.id "
				+ "INNER JOIN t_tiny_village as ttv on tai.tin_village_id = ttv.id "
				+ "INNER JOIN t_village as tv on tv.id = tai.village_id) as a "
				+ "GROUP BY a.id  ) b left join t_store ts on b.store_id = ts.store_id where 1=1";

		if (param.get("storeId") != null && !"".equals(param.get("storeId"))) {
			sql = sql + " and b.store_id  in (" + param.get("storeId") + ")";
		}
		if (param.get("name") != null && !"".equals(param.get("name"))) {
			sql = sql + " and b.name like '%" + param.get("name") + "%'";
		}

		if (param.get("townName") != null && !"".equals(param.get("townName"))) {
			sql = sql + " and b.townName like '%" + param.get("townName") + "%'";
		}
		if (param.get("villageName") != null && !"".equals(param.get("villageName"))) {
			sql = sql + " and b.villageName like '%" + param.get("villageName") + "%'";
		}
		if (param.get("tinVillageName") != null && !"".equals(param.get("tinVillageName"))) {
			sql = sql + " and b.tinVillageName like '%" + param.get("tinVillageName") + "%'";
		}
		if (param.get("employeeName") != null && !"".equals(param.get("employeeName"))) {
			sql = sql + " and b.employeeName like '%" + param.get("employeeName") + "%' ";
		}
		if (param.get("area_no") != null && !"".equals(param.get("area_no"))) {
			sql = sql + " and b.area_no like '%" + param.get("area_no") + "%' ";
		}
		sql = sql + " ORDER BY b.area_no desc ";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		// 如果没有数据返回
		if (lst_data == null || lst_data.size() == 0) {
			return new ArrayList<Map<String, Object>>();
		}

		return (List<Map<String, Object>>) lst_data;
	}

	/**
	 * 根据国安侠a的 员工编号 查询所划片里的所有小区
	 */
	@Override
	public String querytinvillagebyemployeeno(String employee_no, Long area_id) {
		StringBuffer sbf = new StringBuffer();
		String sqlStr = " ";
		if (area_id != null) {
			sqlStr = sqlStr + " AND t_area.id = " + area_id;
		}
		if (employee_no != null && !"".equals(employee_no)) {
			sqlStr = sqlStr + " AND t_area.employee_a_no='" + employee_no + "' ";
		}

		// sbf.append("SELECT a.tin_village_id as id,ttv.name FROM (SELECT
		// t_area.employee_a_no, t_area.employee_a_name,
		// t_area_info.village_id,");
		// sbf.append(" t_area_info.tin_village_id FROM t_area JOIN t_area_info
		// ON t_area.id = t_area_info.area_id AND t_area.status=0 ");
		//
		// sbf.append(" WHERE t_area_info.tin_village_id is not NULL "+sqlStr+")
		// a LEFT JOIN t_tiny_village ttv ON ttv.id=a.tin_village_id ");
		// sbf.append(" UNION ");
		// sbf.append(" SELECT t_tiny_village.id,t_tiny_village.name from
		// t_tiny_village WHERE t_tiny_village.village_id in(SELECT b.village_id
		// from (SELECT a.village_id,ttv.name FROM (SELECT ");
		// sbf.append(" t_area.employee_a_no, t_area.employee_a_name,
		// t_area_info.village_id,t_area_info.tin_village_id FROM t_area JOIN
		// t_area_info ON t_area.id = t_area_info.area_id AND t_area.status=0
		// ");
		//
		// sbf.append(" WHERE t_area_info.tin_village_id is NULL "+sqlStr+") a
		// LEFT JOIN t_tiny_village ttv ON ttv.id=a.tin_village_id) b)");
		sbf.append("select b.address_relevanceName as name from ");
		sbf.append(
				"(SELECT a.tin_village_id as id,ttv.name FROM (SELECT	t_area.employee_a_no,	t_area.employee_a_name,	t_area_info.village_id,");
		sbf.append(
				" t_area_info.tin_village_id FROM t_area JOIN t_area_info ON t_area.id = t_area_info.area_id AND t_area.status=0 ");

		sbf.append(" WHERE   t_area_info.tin_village_id is not NULL " + sqlStr
				+ ") a LEFT JOIN (select * from t_tiny_village where status=0) ttv ON ttv.id=a.tin_village_id ");
		sbf.append(" UNION ");
		sbf.append(
				" SELECT t_tiny_village.id,t_tiny_village.name from t_tiny_village WHERE t_tiny_village.status=0 and t_tiny_village.village_id in (SELECT  t_area_info.village_id ");
		sbf.append("  FROM 	t_area JOIN t_area_info ON t_area.id = t_area_info.area_id AND t_area.status=0 ");
		sbf.append(" and  t_area_info.tin_village_id is NULL " + sqlStr + ")) a");
		sbf.append(" inner join t_comnunity_block b on a.id = b.communityId ");
		// SQL查询对象
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sbf.toString());
		// 获得查询数据
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		StringBuffer ret = new StringBuffer();
		if (lst_data != null && lst_data.size() > 0) {
			for (Map<String, Object> obj : lst_data) {
				Object o = obj.get("name");
				if (o != null && o.toString().length() > 0) {
					ret.append("'");
					ret.append(o.toString());
					ret.append("',");
				}
			}
		}
		if (ret != null && ret.length() > 0) {
			return ret.toString().substring(0, ret.toString().length() - 1);
		}
		return null;
	}

	/**
	 * 分片区查询片区下的所有小区
	 * 
	 * @param store_id
	 */
	@Override
	public List<Map<String, Object>> queryTinVillageByStoreId(Long store_id) {
		String sql =

				"SELECT t.*,tc.address_relevanceName as tiny_name from ( SELECT " + "		a.store_id, "
						+ "		a.name, " + "		a.tin_village_id AS id " + "	FROM " + "		( "
						+ "			SELECT " + "				t_area.id, " + "				t_area.name, "
						+ "				t_area.store_id, " + "				t_area_info.village_id, "
						+ "				t_area_info.tin_village_id " + "			FROM " + "				t_area "
						+ "			JOIN t_area_info ON t_area.id = t_area_info.area_id AND t_area.status=0"
						+ "			WHERE " + "				t_area.store_id = " + store_id
						+ "			AND t_area_info.tin_village_id IS NOT NULL " + "		) a "
						+ "	LEFT JOIN (select * from t_tiny_village where status=0) ttv ON ttv.id = a.tin_village_id  "
						+ "	UNION " + "		SELECT " + "			a.store_id, " + "			a.name, "
						+ "			tin.id " + "		FROM " + "			( " + "				SELECT "
						+ "					t_area.id, " + "					t_area.store_id, "
						+ "					t_area.name, " + "					t_area_info.village_id, "
						+ "					t_area_info.tin_village_id " + "				FROM "
						+ "					t_area "
						+ "				JOIN t_area_info ON t_area.id = t_area_info.area_id AND t_area.status=0 "
						+ "				WHERE " + "					t_area.store_id = " + store_id
						+ "				AND t_area_info.tin_village_id IS NULL " + "			) a "
						+ "		JOIN (select * from t_tiny_village where status=0) tin ON tin.village_id = a.village_id ) t inner join t_comnunity_block tc on t.id = tc.communityId";

		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);

		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

		return lst_data;

	}

	@Override
	public List<Map<String, Object>> getAllAreaOfEmployee(String employeeNo) {
		String sql = "select * from t_area  where status=0 and employee_a_no='" + employeeNo + "'";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@Override
	public List<Map<String, Object>> getAreaInfoOfStore(Long store_id) {
		String sql = "SELECT a.employeeId,a.name,b.employee_a_no FROM (select * FROM tb_bizbase_user  where store_id="
				+ store_id + " and disabledFlag = 1  AND name NOT LIKE '%测试%' and pk_usergroup = 3224 ) a"
				+ " LEFT JOIN t_area b ON a.employeeId = b.employee_a_no AND b.status=0  AND b.store_id=" + store_id
				+ "  where b.employee_a_no IS NULL ";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@Override
	public List<AreaInfo> queryAreaInfoByAreaId(Long area_id) {
		String sql = "select * from t_area_info where area_id=" + area_id;

		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);

		List<AreaInfo> lst_data = query.addEntity(AreaInfo.class).list();

		return lst_data;
	}

	@Override
	public List<Area> queryAreaByEmployeeAndStore(String employeeNo, Long storeId, String ab) {
		String whereStr = "";
		if ("A".equals(ab)) {
			whereStr = " and employee_a_no = '" + employeeNo + "'";
		} else if ("B".equals(ab)) {
			whereStr = " and employee_b_no = '" + employeeNo + "'";
		}

		String sql = "select * from t_area where status=0 and store_id=" + storeId + whereStr;
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);

		List<Area> lst_data = query.addEntity(Area.class).list();

		return lst_data;
	}

	@Override
	public List<Map<String, Object>> queryNoTinyVillageByStore(Long store_id) {
		String sql = "select  a.id as aid,a.name as aname,b.id as bid,b.name as bname from (select id,name from t_tiny_village  where  status=0  and (dellable <> 1 or dellable is null) and FIND_IN_SET(town_id,(SELECT town_id from t_store where store_id="
				+ store_id + "))) a " + " LEFT JOIN " + " ("
				+ " select t3.id,t3.name from t_area t1 INNER JOIN t_area_info t2 on t1.id = t2.area_id and t1.status=0  and t2.tin_village_id is null and t1.store_id="
				+ store_id
				+ " INNER JOIN t_tiny_village t3 on t3.village_id = t2.village_id and t3.status=0 and (t3.dellable <> 1 or t3.dellable is null) "
				+ " union ALL "
				+ " select t3.id,t3.name from t_area t1 INNER JOIN t_area_info t2 on t1.id = t2.area_id and t1.status=0  and t2.tin_village_id is not null and t1.store_id="
				+ store_id
				+ " INNER JOIN t_tiny_village t3 on t3.id = t2.tin_village_id and t3.status=0 and (t3.dellable <> 1 or t3.dellable is null)) b"
				+ " on a.id = b.id  where b.id is null";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@Override
	public List<Map<String, Object>> queryEmployeeOfGAX(Long storeId) {

		String sql = "select th.employee_no as employeeId,th.name from t_humanresources th WHERE th.humanstatus=1 AND th.zw = '国安侠' and th.store_id="
				+ storeId;
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@Override
	public void updateEmployeeIsNullOfArea(Long storeId, String employeeId) throws Exception {
		String sql = "update t_area set employee_a_no = null,employee_a_name=null where store_id=" + storeId
				+ " and employee_a_no='" + employeeId + "'";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		query.executeUpdate();
	}

	@Override
	public void updateEmployeeIsNullOfAreaInfo(Long storeId, String employeeId) throws Exception {
		String sql = "update t_area_info set employee_a_no = null where store_id=" + storeId + " and employee_a_no='"
				+ employeeId + "'";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		query.executeUpdate();
	}

	@Override
	public List<Map<String, Object>> selectAreaOfStore(Long storeId) {
		String sql = "select * from t_area where  status=0 and store_id=" + storeId;
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@Override
	public Map<String, Object> queryAboutOfArea(DynamicDto dynamicDto, PageInfo pageInfo) {
		//String sql = "select a.name,a.area_no,ifnull(a.employee_a_no,'') as employee_a_no ,ifnull(a.employee_b_no,'') as employee_b_no,ifnull(a.employee_a_name,'') as employee_a_name,ifnull(a.employee_b_name,'') as employee_b_name,b.city_name,b.name as storeName from t_area a inner join t_store b on a.store_id = b.store_id where a.status=0 and ifnull(b.estate,'')!='闭店中' ";
		String whereStr1="";
		String whereStr2="";
        if (dynamicDto.getAreaName() != null && !"".equals(dynamicDto.getAreaName())) {
        	whereStr1 = whereStr1 + " and ta.name like  '%" + dynamicDto.getAreaName() + "%'";
		}

		if (dynamicDto.getAreaNo() != null && !"".equals(dynamicDto.getAreaNo())) {
			whereStr1 = whereStr1 + " and ta.area_no like '%" + dynamicDto.getAreaNo() + "%'";
		}

		if (dynamicDto.getEmployeeName() != null && !"".equals(dynamicDto.getEmployeeName())) {
			whereStr1 = whereStr1 + " and concat(ta.employee_a_name,ta.employee_b_name) like '%" + dynamicDto.getEmployeeName() + "%'";
		}

		if (dynamicDto.getEmployeeNo() != null && !"".equals(dynamicDto.getEmployeeNo())) {
			whereStr1 = whereStr1 + " and concat(ta.employee_a_no,ta.employee_b_no) like '%" + dynamicDto.getEmployeeNo() + "%'";
		}

		if (!"".equals(dynamicDto.getStoreIds())) {
			whereStr1 = whereStr1 + " and ta.store_id in (" + dynamicDto.getStoreIds() + ")";
		}
		
		if(dynamicDto.getTinyvillageName()!=null&&!"".equals(dynamicDto.getTinyvillageName())){
			whereStr2 = whereStr2 + " and ttv.name like '%" + dynamicDto.getTinyvillageName()+"%'";
		}
		
		String sql = "select  t1.*,t2.city_name,t2.name as storeName from "+
        			"(SELECT ta.store_id,ta.name,ta.area_no,tai.tin_village_id,ta.employee_a_no,ta.employee_a_name,ta.employee_b_no,ta.employee_b_name"+
					"	FROM t_area ta INNER JOIN t_area_info tai ON ta.id = tai.area_id "+
					"	AND tai.tin_village_id IS NOT NULL "+
					"	AND tai.status = 0  AND ta.status=0  "+whereStr1+
					"	INNER JOIN t_tiny_village ttv ON tai.tin_village_id = ttv.id "+whereStr2+
					"   AND ttv. STATUS = 0 AND (ttv.dellable <> 1	OR ttv.dellable IS NULL) "+
					" UNION"+
					" SELECT ta.store_id,ta.name,ta.area_no,ttv.id AS tin_village_id,ta.employee_a_no,ta.employee_a_name,ta.employee_b_no,ta.employee_b_name"+
					" FROM t_area ta INNER JOIN t_area_info tai ON ta.id = tai.area_id "+
					" AND tai. STATUS = 0  and ta.status=0  "+whereStr1+
					" AND tai.tin_village_id IS NULL "+
					" INNER JOIN t_tiny_village ttv ON tai.village_id = ttv.village_id "+whereStr2+
					" AND ttv. STATUS = 0 "+
					" AND (ttv.dellable <> 1 	OR ttv.dellable IS NULL ) ) t1 "+
					" inner JOIN t_store t2 on t1.store_id = t2.store_id and ifnull(t2.estate,'')!='闭店中' group by t1.area_no";
		

		Map<String, Object> map_result = new HashMap<String, Object>();
		List<?> list = null;
		Integer total_pages = 0;
		if (pageInfo != null) {
			String sql_count = "SELECT COUNT(1) as total FROM (" + sql + ") T";

			Query query_count = this.getHibernateTemplate().getSessionFactory().getCurrentSession()
					.createSQLQuery(sql_count);
			Object total = query_count.uniqueResult();
			pageInfo.setTotalRecords(Integer.valueOf(total.toString()));

			Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
			list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
					.setFirstResult(pageInfo.getRecordsPerPage() * (pageInfo.getCurrentPage() - 1))
					.setMaxResults(pageInfo.getRecordsPerPage()).list();

			total_pages = (pageInfo.getTotalRecords() - 1) / pageInfo.getRecordsPerPage() + 1;
		} else {
			SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
			list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

		}

		map_result.put("pageinfo", pageInfo);
		map_result.put("data", list);
		map_result.put("status", "success");
		map_result.put("totalPage", total_pages);
		return map_result;
	}

	@Override
	public List<Map<String, Object>> selectTinyArea(AreaInfo ai) {
		String sql = "";
		if (ai.getTin_village_id() == null && ai.getVillage_id() != null) {// 小区-'全部'
			sql = "select t1.name,t1.id,t2.tiny_village_id from t_tiny_village t1 left join (select a.storeno,b.tiny_village_id from t_store a inner join tiny_area b on a.storeno=b.store_no and a.store_id="
					+ ai.getStore_id()
					+ " and b.status=0) t2 on t1.id = t2.tiny_village_id where (t1.dellable <> 1 or t1.dellable is null) and t1.status=0 and t1.village_id="
					+ ai.getVillage_id();

		} else if (ai.getTin_village_id() != null) {

			sql = "select t1.name,t1.id,t2.tiny_village_id from t_tiny_village t1 left join (select a.storeno,b.tiny_village_id from t_store a inner join tiny_area b on a.storeno=b.store_no and a.store_id="
					+ ai.getStore_id() + " and b.status=0) t2 on  t1.id = t2.tiny_village_id where id="
					+ ai.getTin_village_id();
		}
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		return query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

	@Override
	public List<Map<String, Object>> selectTinyVillageOfArea(String areaNo) {
		String sql = "select c.* from  "
				+ "(select ttv.id from t_area_info tai INNER JOIN t_tiny_village ttv on tai.tin_village_id = ttv.id and tai.tin_village_id is not  NULL and tai.status=0 and ttv.status=0  and (ttv.dellable <> 1 or ttv.dellable is null)"
				+ "UNION  "
				+ "select ttv.id  from t_area_info tai INNER JOIN t_tiny_village  ttv on  tai.status=0 and tai.tin_village_id is null and tai.village_id = ttv.village_id   and ttv.status=0  and (ttv.dellable <> 1 or ttv.dellable is null)) c ";

		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@Override
	public List<Map<String, Object>> selectHouseAmountOfArea(String areaNo) {
		String sql = "select sum(c.total) as total from  "
				+ "(select ttv.id,ifnull(ttv.residents_number,0) as total from t_area_info tai INNER JOIN t_tiny_village ttv on tai.tin_village_id = ttv.id and tai.tin_village_id is not  NULL and tai.status=0 and ttv.status=0  and (ttv.dellable <> 1 or ttv.dellable is null)"
				+ "UNION  "
				+ "select ttv.id,ifnull(ttv.residents_number,0) as total  from t_area_info tai INNER JOIN t_tiny_village  ttv on  tai.status=0 and tai.tin_village_id is null and tai.village_id = ttv.village_id   and ttv.status=0  and (ttv.dellable <> 1 or ttv.dellable is null)) c ";

		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@Override
	public List<Map<String, Object>> selectAreaByAreaNo(String areaNo) {
		String sql = "select * from t_area where status=0 and area_no='" + areaNo + "'";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@Override
	public List<Map<String, Object>> findAreaInfoById(Long tinyId) {
		String sql = "	SELECT cc.* FROM ( "
				+ "SELECT crea.name as area_name,crea.area_no,crea.employee_a_name,crea.employee_a_no,tiny.id,tiny.`name`  FROM t_area crea LEFT JOIN t_area_info area ON crea.area_no=area.area_no LEFT JOIN t_village vill on area.village_id=vill.id LEFT JOIN t_tiny_village tiny ON vill.id=tiny.id  "
				+ "WHERE tiny.status =0 " + "		 and crea.`status`=0 " + "	and area.`status`=0 and tiny.id="
				+ tinyId + " UNION "
				+ "SELECT crea.name as area_name,crea.area_no,crea.employee_a_name,crea.employee_a_no,tiny.id,tiny.`name`  FROM t_area crea LEFT JOIN t_area_info area ON crea.area_no=area.area_no LEFT JOIN t_tiny_village tiny ON area.tin_village_id=tiny.id  "
				+ "WHERE tiny.status=0 " + "			 and crea.`status`=0 " + "AND area.`status`=0 and tiny.id="
				+ tinyId + " ) cc GROUP BY cc.area_no";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	
	@Override
	public List<Map<String, Object>> queryOrderOfArea(DynamicDto dynamicDto) {
//		String  sql="select count(DISTINCT(customer_id)) cunsumer,SUM(IFNULL(tor.gmv_price,0)) as gmv,count(tor.order_sn) as amount from df_mass_order_monthly tor "+ 
//					 " where tor.area_code='"+dynamicDto.getAreaNo()+"' and tor.customer_id not like 'fakecustomer%' "+
//					 " and DATE_FORMAT(tor.sign_time,'%Y-%m-%d') >='"+dynamicDto.getBeginDate()+"' and DATE_FORMAT(tor.sign_time,'%Y-%m-%d') <='"+dynamicDto.getEndDate()+"' "+
//					 " and tor.eshop_name NOT LIKE '%测试%' AND tor.eshop_white!='QA'  "+
//					 " and tor.store_name NOT LIKE '%测试%' and tor.store_white!='QA' AND tor.store_status =0";
		
		String  sql="select count(tor.order_sn) as amount from df_mass_order_monthly tor "+ 
				 " where tor.area_code='"+dynamicDto.getAreaNo()+"' and tor.customer_id not like 'fakecustomer%' "+
				 " and tor.sign_time >='"+dynamicDto.getBeginDate()+"' and tor.sign_time <='"+dynamicDto.getEndDate()+"' "+
				 " and tor.eshop_name NOT LIKE '%测试%' AND tor.eshop_white!='QA'  "+
				 " and tor.store_name NOT LIKE '%测试%' and tor.store_white!='QA' AND tor.store_status =0";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	
	@Override
	public List<Map<String, Object>> queryPerfOfAreaByDept(DynamicDto dynamicDto) {
		String sql ="select count(DISTINCT(customer_id)) consumer,SUM(IFNULL(tor.gmv_price,0)) as gmv,count(tor.order_sn) as amount,department_name from df_mass_order_monthly tor "+ 
					 " where tor.area_code='"+dynamicDto.getAreaNo()+"' and tor.customer_id not like 'fakecustomer%' "+
					 " and tor.sign_time >='"+dynamicDto.getBeginDate()+"' and tor.sign_time <='"+dynamicDto.getEndDate()+"' "+
					 " and tor.eshop_name NOT LIKE '%测试%' AND tor.eshop_white!='QA'  "+
					 " and tor.store_name NOT LIKE '%测试%' and tor.store_white!='QA' AND tor.store_status =0 group by DATE_FORMAT(sign_time,'%Y-%m'),department_name ";
		if("gmv".equals(dynamicDto.getDataType())){
			sql = sql+" order by gmv desc limit 5";
		}else if("consumer".equals(dynamicDto.getDataType())){//消费用户
			sql = sql+" order by consumer desc limit 5";
		}
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	
	@Override
	public List<Map<String, Object>> queryPerfOfAreaByChannel(DynamicDto dynamicDto) {
		String sql ="select count(DISTINCT(customer_id)) consumer,SUM(IFNULL(tor.gmv_price,0)) as gmv,count(tor.order_sn) as amount,channel_name from df_mass_order_monthly tor "+ 
				 " where tor.area_code='"+dynamicDto.getAreaNo()+"' and tor.customer_id not like 'fakecustomer%' "+
				 " and tor.sign_time >='"+dynamicDto.getBeginDate()+"' and tor.sign_time <='"+dynamicDto.getEndDate()+"' "+
				 " and tor.eshop_name NOT LIKE '%测试%' AND tor.eshop_white!='QA'  "+
				 " and tor.store_name NOT LIKE '%测试%' and tor.store_white!='QA' AND tor.store_status =0 group by DATE_FORMAT(tor.sign_time,'%Y-%m'), channel_name";
		if("gmv".equals(dynamicDto.getDataType())){
			sql = sql+" order by gmv desc limit 5";
		}else if("consumer".equals(dynamicDto.getDataType())){//消费用户
			sql = sql+" order by consumer desc limit 5";
		}
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	
	@Override
	public List<Map<String, Object>> queryHeatMapOfArea(DynamicDto dynamicDto) {
		String  sql="select count(DISTINCT(customer_id)) consumer,SUM(IFNULL(tor.gmv_price,0)) as gmv,tor.info_village_code as code,tvc.tiny_village_name from df_mass_order_monthly tor "+
				 " left join tiny_village_code tvc on tor.info_village_code = tvc.code "+ 
				 " where tor.info_village_code is not null and tor.area_code='"+dynamicDto.getAreaNo()+"' and tor.customer_id not like 'fakecustomer%' "+
				 " and tor.sign_time >='"+dynamicDto.getBeginDate()+"' and tor.sign_time <='"+dynamicDto.getEndDate()+"' "+
				 " and tor.eshop_name NOT LIKE '%测试%' AND tor.eshop_white!='QA'  "+
				 " and tor.store_name NOT LIKE '%测试%' and tor.store_white!='QA' AND tor.store_status =0 group by info_village_code";
		
		if("gmv".equals(dynamicDto.getDataType())){
			sql = sql+" order by gmv desc"; 
		}else if("consumer".equals(dynamicDto.getDataType())){
			sql = sql+" order by consumer desc"; 
		}else if("amount".equals(dynamicDto.getDataType())){
			sql = sql+" order by amount desc"; 
		}
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
		
	}

	
	
	@Override
	public List<Map<String, Object>> queryGMVOfEveryDayByArea(DynamicDto dynamicDto) {
		String  sql="select SUM(IFNULL(tor.gmv_price,0)) as gmv,DATE_FORMAT(tor.sign_time,'%Y-%m-%d') as signTime from df_mass_order_monthly tor "+ 
				 " where tor.area_code='"+dynamicDto.getAreaNo()+"' and tor.customer_id not like 'fakecustomer%' "+
				 " and tor.sign_time >='"+dynamicDto.getBeginDate()+"' and tor.sign_time <='"+dynamicDto.getEndDate()+"' "+
				 " and tor.eshop_name NOT LIKE '%测试%' AND tor.eshop_white!='QA'  "+
				 " and tor.store_name NOT LIKE '%测试%' and tor.store_white!='QA' AND tor.store_status =0 group by DATE_FORMAT(tor.sign_time,'%Y-%m-%d')";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	
	@Override
	public List<Map<String, Object>> queryConsumerOfEveryDayByArea(DynamicDto dynamicDto) {
		String  sql="select count(DISTINCT(customer_id)) consumer,DATE_FORMAT(tor.sign_time,'%Y-%m-%d') as signTime from df_mass_order_monthly tor "+ 
				 " where tor.area_code='"+dynamicDto.getAreaNo()+"' and tor.customer_id not like 'fakecustomer%' "+
				 " and tor.sign_time >='"+dynamicDto.getBeginDate()+"' and tor.sign_time <='"+dynamicDto.getEndDate()+"' "+
				 " and tor.eshop_name NOT LIKE '%测试%' AND tor.eshop_white!='QA'  "+
				 " and tor.store_name NOT LIKE '%测试%' and tor.store_white!='QA' AND tor.store_status =0 group by DATE_FORMAT(tor.sign_time,'%Y-%m-%d')";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	
	@Override
	public List<Map<String, Object>> queryNewCustomeOfEveryDayByArea(DynamicDto dynamicDto) {
		String  sql="select sum(case when customer_isnew_flag >='10' then 1 else 0 end) as customer,DATE_FORMAT(tor.sign_time,'%Y-%m-%d') as signTime from df_mass_order_monthly tor "+ 
				 " where customer_isnew_flag !='-1' and tor.area_code='"+dynamicDto.getAreaNo()+"' and tor.customer_id not like 'fakecustomer%' "+
				 " and tor.sign_time >='"+dynamicDto.getBeginDate()+"' and tor.sign_time <='"+dynamicDto.getEndDate()+"' "+
				 " and tor.eshop_name NOT LIKE '%测试%' AND tor.eshop_white!='QA'  "+
				 " and tor.store_name NOT LIKE '%测试%' and tor.store_white!='QA' AND tor.store_status =0 group by DATE_FORMAT(tor.sign_time,'%Y-%m-%d')";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	
	@Override
	public List<Map<String, Object>> queryHouseHoldNumber(DynamicDto dynamicDto) {
		String sql = "select sum(ifnull(houseHoldNumber,0)) as houseHoldNumber from  "
				+ "(select ifnull(ttv.residents_number,0) as houseHoldNumber from t_area_info tai INNER JOIN t_tiny_village ttv on tai.area_no='"+dynamicDto.getAreaNo()+"' and tai.tin_village_id = ttv.id and tai.tin_village_id is not  NULL and tai.status=0 and ttv.status=0  and (ttv.dellable <> 1 or ttv.dellable is null)"
				+ "UNION  "
				+ "select ifnull(ttv.residents_number,0) as houseHoldNumber  from t_area_info tai INNER JOIN t_tiny_village  ttv on tai.area_no='"+dynamicDto.getAreaNo()+"' and  tai.status=0 and tai.tin_village_id is null and tai.village_id = ttv.village_id   and ttv.status=0  and (ttv.dellable <> 1 or ttv.dellable is null)) c ";

		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}
	
	

}
