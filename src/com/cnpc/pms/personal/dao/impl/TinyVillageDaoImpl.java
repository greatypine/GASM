package com.cnpc.pms.personal.dao.impl;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.personal.dao.TinyVillageDao;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.TinyArea;
import com.cnpc.pms.personal.entity.TinyVillage;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.manager.TinyAreaManager;

public class TinyVillageDaoImpl extends BaseDAOHibernate implements TinyVillageDao {

	@Override
	public TinyVillage getTinyVillage() {
		String tinyVillageSql = "SELECT * from t_tiny_village WHERE gaode_coordinate_x is null and baidu_coordinate_x is null and sogou_coordinate_x is NULL limit 1";
		SQLQuery sqlQuery = getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createSQLQuery(tinyVillageSql);
		List<TinyVillage> list = sqlQuery.addEntity(TinyVillage.class).list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> getTinyVillageList(String where, PageInfo pageInfo) {
		// sql查询列，用于分页计算数据总数
		String str_count_sql = "select COUNT(DISTINCT tiny.id)"
				+ " FROM t_tiny_village tiny left join t_village vill on tiny.village_id=vill.id LEFT JOIN t_town town ON town.id=tiny.town_id left join t_county county on county.id=town.county_id left join t_city city on city.id=county.city_id  where 1=1 and tiny.status=0 "
				+ where;
		System.out.println(str_count_sql);
		// sql查询列，用于页面展示所有的数据
		StringBuilder sb_sql = new StringBuilder();
		sb_sql.append(
				" select tiny.id,tiny.`name`,tiny.tinyvillage_type ,tiny.dellable,town.`name` as town_name,tiny.address,vill.`name` as village_name,tiny.create_time,tiny.create_user "
						+ "  FROM t_tiny_village tiny left join t_village vill on tiny.village_id=vill.id   "
						+ " LEFT JOIN t_town town ON town.id=tiny.town_id left join t_county county on county.id=town.county_id left join t_city city on city.id=county.city_id   "
						+ "  where 1=1 and tiny.status=0 ");
		sb_sql.append(where + " order by tiny.id desc");
		// SQL查询对象
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createSQLQuery(sb_sql.toString());

		// 查询数据量对象
		SQLQuery countQuery = getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createSQLQuery(str_count_sql);
		pageInfo.setTotalRecords(Integer.valueOf(countQuery.list().get(0).toString()));
		// 获得查询数据
		List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
				.setFirstResult(pageInfo.getRecordsPerPage() * (pageInfo.getCurrentPage() - 1))
				.setMaxResults(pageInfo.getRecordsPerPage()).list();

		List<Map<String, Object>> lst_result = new ArrayList<Map<String, Object>>();

		// 如果没有数据返回
		if (lst_data == null || lst_data.size() == 0) {
			return lst_result;
		}
		// 转换成需要的数据形式
		for (Object obj_data : lst_data) {
			System.out.println(obj_data);
			Map<String, Object> map = (Map<String, Object>) obj_data;
			for (String key : map.keySet()) {
				if (key.equalsIgnoreCase("create_time")) {
					Object object = map.get(key);
					if (object != null && object instanceof Date) {
						Date date = (Date) object;
						String strdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
						map.put(key, strdate);
					}
				}
			}
			// lst_result.add((Map<String,Object>)obj_data);
			lst_result.add(map);
		}
		return lst_result;
	}

	@Override
	public List<Long> getTownIdList(Long long_store_id) {
		// sql查询列，用于分页计算数据总数
		String str_city_sql = "SELECT t_city.id FROM t_store LEFT JOIN t_town ON t_town.id = t_store.town_id "
				+ "LEFT JOIN t_county ON t_county.id = t_town.county_id "
				+ "LEFT JOIN t_city ON t_city.id = t_county.city_id " + "WHERE t_store.store_id = " + long_store_id;

		// SQL查询对象
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(str_city_sql);
		BigInteger str_city_id = (BigInteger) query.uniqueResult();

		String str_town_sql = "SELECT t_town.id AS id FROM t_county LEFT JOIN t_town ON t_town.county_id = t_county.id WHERE t_county.city_id = "
				+ str_city_id.longValue();

		// 查询数据量对象
		SQLQuery townQuery = getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createSQLQuery(str_town_sql);
		// 获得查询数据
		List<?> lst_data = townQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

		List<Long> lst_result = new ArrayList<Long>();

		// 如果没有数据返回
		if (lst_data == null || lst_data.size() == 0) {
			return lst_result;
		}
		// 转换成需要的数据形式
		for (Object obj_data : lst_data) {
			Map<String, Object> map = (Map<String, Object>) obj_data;
			if (map.get("id") != null) {
				lst_result.add(((BigInteger) map.get("id")).longValue());
			}
		}
		return lst_result;
	}

	@Override
	public List<Map<String, Object>> getTinyVillageInfo(String where, PageInfo pageInfo) {
		// sql查询列，用于分页计算数据总数
		String str_count_sql = " select COUNT(1) from t_tiny_village tiny LEFT JOIN t_village vill ON tiny.village_id=vill.id "
				+ "  LEFT JOIN t_town town ON vill.town_id=town.id  "
				+ " LEFT JOIN t_county coun ON town.county_id=coun.id "
				+ " WHERE tiny.baidu_coordinate_x=0 AND tiny.sogou_coordinate_x=0 AND tiny.gaode_coordinate_x=0";
		System.out.println(str_count_sql);
		// sql查询列，用于页面展示所有的数据
		StringBuilder sb_sql = new StringBuilder();
		sb_sql.append(
				" select tiny.id,coun.`name` as county_name,town.`name` as town_name,vill.`name` as village_name,tiny.`name` as tinyvillage_name,tiny.address from t_tiny_village tiny LEFT JOIN t_village vill ON tiny.village_id=vill.id "
						+ "  LEFT JOIN t_town town ON vill.town_id=town.id  "
						+ " LEFT JOIN t_county coun ON town.county_id=coun.id "
						+ " WHERE tiny.baidu_coordinate_x=0 AND tiny.sogou_coordinate_x=0 AND tiny.gaode_coordinate_x=0");
		// SQL查询对象
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createSQLQuery(sb_sql.toString());

		// 查询数据量对象
		SQLQuery countQuery = getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createSQLQuery(str_count_sql);
		pageInfo.setTotalRecords(Integer.valueOf(countQuery.list().get(0).toString()));
		// 获得查询数据
		List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
				.setFirstResult(pageInfo.getRecordsPerPage() * (pageInfo.getCurrentPage() - 1))
				.setMaxResults(pageInfo.getRecordsPerPage()).list();

		List<Map<String, Object>> lst_result = new ArrayList<Map<String, Object>>();

		// 如果没有数据返回
		if (lst_data == null || lst_data.size() == 0) {
			return lst_result;
		}
		// 转换成需要的数据形式
		for (Object obj_data : lst_data) {
			System.out.println(obj_data);
			Map<String, Object> map = (Map<String, Object>) obj_data;
			lst_result.add((Map<String, Object>) obj_data);
			// lst_result.add(map);
		}
		return lst_result;
	}

	@Override
	public Map<String, Object> getInfomation() {
		// TODO Auto-generated method stub
		String str_total_count = "SELECT COUNT(1) from t_tiny_village WHERE baidu_coordinate_x is null AND sogou_coordinate_x is null AND gaode_coordinate_x is NULL";
		// String str_error_count="SELECT COUNT(1) from t_tiny_village WHERE
		// baidu_coordinate_x =0 AND gaode_coordinate_x =0 AND
		// sogou_coordinate_x =0";
		// String str_modified_count="SELECT COUNT(1) from t_tiny_village WHERE
		// baidu_coordinate_x is NOT NULL or gaode_coordinate_x is NOT null OR
		// sogou_coordinate_x is NOT NULL";

		SQLQuery countQuery = getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createSQLQuery(str_total_count);
		Object result1 = countQuery.uniqueResult();
		/*
		 * SQLQuery errorQuery = getHibernateTemplate().getSessionFactory()
		 * .getCurrentSession() .createSQLQuery(str_error_count); Object result2
		 * = errorQuery.uniqueResult(); SQLQuery modifiedQuery =
		 * getHibernateTemplate().getSessionFactory() .getCurrentSession()
		 * .createSQLQuery(str_modified_count); Object result3 =
		 * modifiedQuery.uniqueResult();
		 */
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total_count", result1);
		/*
		 * map.put("error_count", result2); map.put("modified_count", result3);
		 */
		return map;
	}

	@Override
	public int updateTinyVillage(Long id) {
		String update_sql = "update t_tiny_village SET baidu_coordinate_x=NULL,baidu_coordinate_y=NULL,sogou_coordinate_x=NULL,"
				+ "sogou_coordinate_y=NULL," + "gaode_coordinate_x=NULL,gaode_coordinate_y=NULL WHERE id=" + id;
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(update_sql);
		int update = query.executeUpdate();
		return update;
	}

	@Override
	public List<Map<String, Object>> getAllTinVillageByNameAndTown(String name, Integer tinyvillage_type,
			String town_id, String village_id) {
		String findSql = "select ttv.*,tv.id as village_id,tv.name as village_name from t_tiny_village ttv";
		if (village_id == null || "".equals(village_id)) {
			findSql = findSql
					+ " inner join t_town tt on ttv.town_id = tt.id left join t_village tv on tv.id = ttv.village_id where ttv.town_id="
					+ Long.parseLong(town_id) + " and ttv.name like '%" + name + "%'";
		} else {
			findSql = findSql + " left join t_village tv on ttv.village_id = tv.id where ttv.village_id="
					+ Long.parseLong(village_id) + " and ttv.name like '%" + name + "%'";
		}
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(findSql);

		List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if (list != null && list.size() > 0) {
			return list;
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> getAllTinVillageByNameAndCity(String name, Integer tinyvillage_type,
			Long city_id) {
		String findSql = " select d.id,d.name from t_city a INNER JOIN t_county b on a.id = b.city_id INNER JOIN  t_town  c on b.id = c.county_id INNER JOIN t_tiny_village d on c.id = d.town_id and d.tinyvillage_type="
				+ tinyvillage_type + "  where  d.name like '%" + name + "%' ";
		if (city_id != null) {
			findSql = findSql + " and a.id=" + city_id;
		}
		findSql = findSql + " ORDER BY LENGTH(IFNULL(d.name,'')) limit 0,10";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(findSql);

		List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if (list != null && list.size() > 0) {
			return list;
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> findTinyVillageList(String where, PageInfo pageInfo, String storeno) {
		List<Map<String, Object>> lst_result = new ArrayList<Map<String, Object>>();
		// sql查询列，用于页面展示所有的数据
		StringBuilder sb_sql = new StringBuilder();
		sb_sql.append(" select tiny.id,tiny.`name`,vill.`name` as village_name,coss.mouth,coss.placename ,coss.relevaId"
				+ "  FROM t_tiny_village tiny left join t_village vill on tiny.village_id=vill.id   "
				+ " LEFT JOIN t_town town ON town.id=tiny.town_id left join t_county county on county.id=town.county_id left join t_city city on city.id=county.city_id   "
				+ "LEFT JOIN (SELECT block.communityId as communityId,COUNT(block.address_relevance_id) as mouth,concat(GROUP_CONCAT(releva.placename SEPARATOR ','),'') as placename,concat(GROUP_CONCAT(releva.id SEPARATOR ','),'') as relevaId FROM t_comnunity_block block LEFT JOIN t_address_relevance releva ON block.address_relevance_id=releva.id where releva.storeno='"
				+ storeno
				+ "'  group by block.communityId) as coss ON coss.communityId=tiny.id   where 1=1 and tiny.status=0 and (tiny.dellable<>1 or tiny.dellable is null) ");
		sb_sql.append(where + " order by tiny.id desc");
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		try {
			Query query = session.createSQLQuery(sb_sql.toString());
			List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
					.setFirstResult(pageInfo.getRecordsPerPage() * (pageInfo.getCurrentPage() - 1))
					.setMaxResults(pageInfo.getRecordsPerPage()).list();
			if (lst_data == null || lst_data.size() == 0) {
				return lst_result;
			}
			// 转换成需要的数据形式
			for (Object obj_data : lst_data) {
				lst_result.add((Map<String, Object>) obj_data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return lst_result;
	}
	// 查询数量sql

	public Integer getCount(String where) {
		int result = 0;
		String str_count_sql = "select COUNT(DISTINCT tiny.id)"
				+ " FROM t_tiny_village tiny left join t_village vill on tiny.village_id=vill.id LEFT JOIN t_town town ON town.id=tiny.town_id left join t_county county on county.id=town.county_id left join t_city city on city.id=county.city_id LEFT JOIN (SELECT block.communityId as communityId,COUNT(block.address_relevance_id) as mouth,GROUP_CONCAT(releva.placename SEPARATOR ',') as placename FROM t_comnunity_block block LEFT JOIN t_address_relevance releva ON block.address_relevance_id=releva.id  group by block.communityId) as coss ON coss.communityId=tiny.id   where 1=1 and tiny.status=0 and (tiny.dellable<>1 or tiny.dellable is null)"
				+ where;
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		try {
			Query query = session.createSQLQuery(str_count_sql);
			result = Integer.valueOf(query.uniqueResult().toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return result;
	}

	@Override
	public void uptinyvillagexuhao(String string) {
		numberupdateZero();
		String sql = "update t_tiny_village SET number=1 where id in (" + string + ")";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		try {
			Query query = session.createSQLQuery(sql);
			int executeUpdate = query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

	}

	@Override
	public void numberupdateZero() {
		String sql = "update t_tiny_village SET number=0";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		try {
			Query query = session.createSQLQuery(sql);
			int executeUpdate = query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

	}

	@Override
	public List<Map<String, Object>> getOrderAddressTinyvillageList(String where, PageInfo pageInfo) {
		// sql查询列，用于分页计算数据总数
		String str_count_sql = "select COUNT(DISTINCT tiny.id)"
				+ " FROM t_tiny_village tiny left join t_village vill on tiny.village_id=vill.id LEFT JOIN t_town town ON town.id=tiny.town_id left join t_county county on county.id=town.county_id left join t_city city on city.id=county.city_id  where 1=1 and tiny.status=0 and (tiny.dellable<>1 or tiny.dellable is null) "
				+ where;
		System.out.println(str_count_sql);
		// sql查询列，用于页面展示所有的数据
		StringBuilder sb_sql = new StringBuilder();
		sb_sql.append(
				" select tiny.id,tiny.`name`,tiny.tinyvillage_type ,town.`name` as town_name,tiny.address,vill.`name` as village_name,tiny.create_time,tiny.create_user "
						+ "  FROM t_tiny_village tiny left join t_village vill on tiny.village_id=vill.id   "
						+ " LEFT JOIN t_town town ON town.id=tiny.town_id left join t_county county on county.id=town.county_id left join t_city city on city.id=county.city_id   "
						+ "  where 1=1 and tiny.status=0 and (tiny.dellable<>1 or tiny.dellable is null) ");
		sb_sql.append(where + " order by tiny.number desc");
		// SQL查询对象
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createSQLQuery(sb_sql.toString());

		// 查询数据量对象
		SQLQuery countQuery = getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createSQLQuery(str_count_sql);
		pageInfo.setTotalRecords(Integer.valueOf(countQuery.list().get(0).toString()));
		// 获得查询数据
		List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
				.setFirstResult(pageInfo.getRecordsPerPage() * (pageInfo.getCurrentPage() - 1))
				.setMaxResults(pageInfo.getRecordsPerPage()).list();

		List<Map<String, Object>> lst_result = new ArrayList<Map<String, Object>>();

		// 如果没有数据返回
		if (lst_data == null || lst_data.size() == 0) {
			return lst_result;
		}
		// 转换成需要的数据形式
		for (Object obj_data : lst_data) {
			System.out.println(obj_data);
			Map<String, Object> map = (Map<String, Object>) obj_data;
			for (String key : map.keySet()) {
				if (key.equalsIgnoreCase("create_time")) {
					Object object = map.get(key);
					if (object != null && object instanceof Date) {
						Date date = (Date) object;
						String strdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
						map.put(key, strdate);
					}
				}
			}
			// lst_result.add((Map<String,Object>)obj_data);
			lst_result.add(map);
		}
		return lst_result;
	}

	@Override
	public List<TinyVillage> findMounthTinyVillageDataBystoreId(Long store_id) {
		String tinyVillageSql = "SELECT * FROM t_tiny_village WHERE status=0 AND gaode_coordinate_x is not NULL AND id in ( "
				+ "select t3.id from t_area t1 INNER JOIN t_area_info t2 on t1.id = t2.area_id and t1.status=0 and t2.tin_village_id is null and t1.store_id="
				+ store_id + " INNER JOIN t_tiny_village t3 on t3.village_id = t2.village_id  " + "union ALL "
				+ "select t3.id from t_area t1 INNER JOIN t_area_info t2 on t1.id = t2.area_id and t1.status=0 and t2.tin_village_id is not null and t1.store_id="
				+ store_id + " INNER JOIN t_tiny_village t3 on t3.id = t2.tin_village_id  " + ")";
		SQLQuery sqlQuery = getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createSQLQuery(tinyVillageSql);
		List<TinyVillage> list = sqlQuery.addEntity(TinyVillage.class).list();
		if (list != null && list.size() > 0) {
			return list;
		}
		return null;
	}

	@Override
	public List<TinyVillage> findTinyCoordDataBytownId(String town_id, Long tinyId) {
		String tinyVillageSql = "SELECT t1.* FROM t_tiny_village t1 WHERE t1.`status`=0 AND t1.town_id in (" + town_id
				+ ") AND t1.id!=" + tinyId + " UNION "
				+ "SELECT t1.* FROM t_tiny_village t1 LEFT JOIN t_village t2 ON t1.village_id=t2.id LEFT JOIN t_town t3 ON t2.town_id=t3.id WHERE t1.`status`=0 AND t3.id in ("
				+ town_id + ") AND t1.id!=" + tinyId;
		SQLQuery sqlQuery = getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createSQLQuery(tinyVillageSql);
		List<TinyVillage> list = sqlQuery.addEntity(TinyVillage.class).list();
		if (list != null && list.size() > 0) {
			return list;
		}
		return null;
	}

	@Override
	public List<TinyVillage> findTinyVillageByCityName(String cityName) {
		String findSql = "SELECT * FROM t_tiny_village WHERE (town_id in (SELECT id FROM t_town  WHERE county_id in (SELECT id FROM t_county WHERE city_id in (SELECT id FROM t_city WHERE `name` LIKE '%"
				+ cityName
				+ "%'))) or village_id in (SELECT id FROM t_village WHERE town_id in (SELECT id FROM t_town  WHERE county_id in (SELECT id FROM t_county WHERE city_id in (SELECT id FROM t_city WHERE `name` LIKE '%"
				+ cityName + "%'))))) AND status=0";
		List<TinyVillage> list = null;
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		try {
			SQLQuery sqlQuery = session.createSQLQuery(findSql);
			sqlQuery.addEntity(TinyVillage.class);
			list = sqlQuery.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return list;
	}

	@Override
	public List<TinyVillage> findTinyVillageByTownIds(String townids) {
		String findSql = "SELECT * FROM t_tiny_village WHERE (town_id in (" + townids
				+ ") or village_id in (SELECT id FROM t_village WHERE town_id in (" + townids + "))) AND status=0";
		List<TinyVillage> list = null;
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		try {
			SQLQuery sqlQuery = session.createSQLQuery(findSql);
			sqlQuery.addEntity(TinyVillage.class);
			list = sqlQuery.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return list;
	}

	@Override
	public Map<String, Object> queryVillageInfo(String area_id) {
		String whereStr = "";
		String orderStr = " order by area_name asc ";

		String sql = "select area_name,tin_village_id,tiny_name,tiny_code,area_employee_a_name,area_employee_b_name,"
				+ "sumbuilding,sumhouse,vallage_area,store_name,custmer_count,order_count,user_count_6,user_count_18,"
				+ "store_id,create_time,create_user,create_user_id,update_user,update_user_id,update_time"
				+ " from df_tinyarea_datainfo  ";
		if (area_id != null && !"".equals(area_id)) {
			whereStr = " where area_id='" + area_id + "' ";
			sql += whereStr;
		}
		sql += orderStr;
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		List<Map<String, Object>> lst_data = new ArrayList<Map<String, Object>>();
		Map<String, Object> map_result = new HashMap<String, Object>();
		try {
			SQLQuery query = session.createSQLQuery(sql);
			// 获得查询数据
			lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			map_result.put("data", lst_data);
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			session.close();
		}
		return map_result;
	}

	@Override
	public List<Map<String, Object>> findTinyVillageDataDetails() {
		String sql = "SELECT tinyvill.id as tinyvillageid,tinyvill.tinyvillage_type as tiny_village_type,  "
				+ "IFNULL(ss.buildingcou,0)+IFNULL(buildingss.businsscount,0)+IFNULL(bung.bungalow,0) as sumbuilding,  "
				+ "IFNULL(hou.buildhouse,0)+IFNULL(buildingss.bushouse,0)+IFNULL(bung.bungalow,0) as sumhouse ,tinyvill.name as tinyvillage_name,tinyvill.village_id,tinyvill.town_id "
				+ " FROM (SELECT id,`name`,village_id,town_id FROM t_tiny_village WHERE (town_id is not NULL or village_id is not NULL) AND status=0) tinyvill   "
				+ "	 LEFT JOIN   "
				+ "	(SELECT tinyvillage_id,COUNT(*) as buildingcou FROM t_building WHERE `status`=0 AND type=1 GROUP BY tinyvillage_id)ss ON ss.tinyvillage_id=tinyvill.id LEFT JOIN   "
				+ "		(SElECT buil.tinyvillage_id,count(distinct(hou.id)) as buildhouse FROM t_building buil LEFT JOIN t_house hou ON buil.id=hou.building_id   WHERE buil.`status`=0 AND buil.type=1 AND hou.house_type=1 AND hou.`status`=0 group by buil.tinyvillage_id   "
				+ ") hou ON hou.tinyvillage_id=tinyvill.id   "
				+ "left join (select buil.tinyvillage_id,count(distinct(buil.id)) as businsscount,sum(buhouse.buildcount) as bushouse from (SELECT building_id,count(*) as buildcount FROM t_house WHERE house_type=2 and status=0 group by building_id) as buhouse left join t_building buil on buhouse.building_id=buil.id group by buil.tinyvillage_id   "
				+ ") as buildingss on buildingss.tinyvillage_id=tinyvill.id   "
				+ "left join (select tinyvillage_id,count(*) as bungalow from t_house where house_type=0 and status=0 group by tinyvillage_id) as bung on bung.tinyvillage_id=tinyvill.id  ";
		List<Map<String, Object>> lst_data = null;
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		try {
			SQLQuery query = session.createSQLQuery(sql);
			// 获得查询数据
			lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			session.close();
		}
		return lst_data;
	}

	/**
	 * 除了门店查询所有小区信息
	 */
	@Override
	public Map<String, Object> queryAboutTinyvillage(String where, PageInfo pageInfo) {
		TinyAreaManager tinyAreaManager = (TinyAreaManager) SpringHelper.getBean("tinyAreaManager");
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		String sql = "select tiny.id,tiny.`name`,tiny.tinyvillage_type ,IF(tiny.dellable=1,'有','无') as dellable ,IFNULL(IFNULL(codd.consumer_number,0)+IFNULL(codd.consumer_number_2018,0),0) as consumer_number,town.`name` as town_name,ifnull(tiny.residents_number,0) as residents_number,ifnull(vill.`name`,'') as village_name,"
				+ " case city.`name` WHEN '北京市辖区' THEN '北京' WHEN '北京县' THEN '北京' WHEN '天津市辖区' THEN '天津' WHEN '天津市辖县' THEN '天津' "
				+ "  WHEN '上海市辖区' THEN '上海' WHEN '上海县' THEN '上海' WHEN '重庆市辖区' THEN '重庆' WHEN '重庆县' THEN '重庆' else city.`name`  END as city_name"
				+ " ,codd.`code` as tinyvillage_code "
				+ " FROM t_tiny_village tiny left join t_village vill on tiny.village_id=vill.id "
				+ " LEFT JOIN t_town town ON town.id=tiny.town_id left join t_county county on county.id=town.county_id left join t_city city on city.id=county.city_id "
				+ " LEFT JOIN tiny_village_code codd ON codd.tiny_village_id = tiny.id"
				+ " where  tiny.status=0 AND codd.`code` is not NULL" + where;

		Map<String, Object> map_result = new HashMap<String, Object>();
		List<?> list = null;
		Integer total_pages = 0;
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

		if (list != null && list.size() > 0) {
			for (Object object : list) {
				Map<String, Object> map = (Map<String, Object>) object;
				Object object2 = map.get("id");
				TinyArea tinyArea = tinyAreaManager.findTinyAreaByTinyId(Long.parseLong(object2 + ""));
				if (tinyArea != null) {
					map.put("tiny_map", "有");
					String string = tinyArea.getVallage_area();
					if (string != null && !"".equals(string)) {
						map.put("vallage_area", string);
					} else {
						map.put("vallage_area", 0);
					}
					Store store = storeManager.findStoreByStoreNo(tinyArea.getStoreNo());
					if (store != null) {
						map.put("store_name", store.getName());
					} else {
						map.put("store_name", "");
					}

				} else {
					map.put("store_name", "");
					map.put("vallage_area", 0);
					map.put("tiny_map", "无");
				}
				listData.add(map);
			}
		}
		map_result.put("pageinfo", pageInfo);
		map_result.put("data", listData);
		map_result.put("status", "success");
		map_result.put("totalPage", total_pages);
		return map_result;
	}

	@Override
	public Integer findResidentsHouseCount() {
		String sql = "select sum(ifnull(residents_number,0)) from t_tiny_village where residents_number>0 and status = 0 and (dellable <> 1 or dellable is null)";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		List list = sqlQuery.list();
		if (list != null && list.size() > 0) {
			return Integer.parseInt(list.get(0) + "");
		}
		return null;
	}

	@Override
	public Integer findResidentsHouseCountByProvince(Long province_id) {
		String sql = "select sum(ifnull(ttv.residents_number,0)) from t_tiny_village ttv INNER JOIN t_town ttn ON ttv.town_id = ttn.id INNER JOIN t_county tcy ON ttn.county_id = tcy.id INNER JOIN t_city tcity"
				+ " ON tcy.city_id = tcity.id  where tcity.province_id = '" + province_id
				+ "' and ttv.status = 0 and (ttv.dellable <> 1 or ttv.dellable is null)";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		List list = sqlQuery.list();
		if (list != null && list.size() > 0) {
			return Integer.parseInt(list.get(0) + "");
		}
		return null;
	}

	@Override
	public Integer findResidentsHouseCountByCity(Long city_id) {
		String sql = "select ifnull(sum(ifnull(ttv.residents_number,0)),0) from t_tiny_village ttv "
				+ " INNER JOIN t_town ttn ON ttv.town_id = ttn.id "
				+ " INNER JOIN t_county tcy ON ttn.county_id = tcy.id "
				+ " where tcy.city_id in (SELECT t_city.id FROM t_city WHERE t_city.name like "
				+ " CONCAT('%',(SELECT t_dist_citycode.cityname FROM t_dist_citycode WHERE t_dist_citycode.id="
				+ city_id + "),'%')) and ttv.status = 0 and (ttv.dellable <> 1 or ttv.dellable is null)";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		List list = sqlQuery.list();
		if (list != null && list.size() > 0) {
			return Integer.parseInt(list.get(0) + "");
		}
		return null;
	}

	@Override
	public Integer findTinyVillageCountByStore(Long store_id) {
		String sql = "select count(id) from t_tiny_village  where  status=0  and (dellable <> 1 or dellable is null) and FIND_IN_SET(town_id,(SELECT town_id from t_store where store_id="
				+ store_id + "))";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		List list = sqlQuery.list();
		if (list != null && list.size() > 0) {
			return Integer.parseInt(list.get(0) + "");
		}
		return null;
	}

	@Override
	public Integer findResidentsHouseCountByStore(Long store_id) {
		String sql = "select ifnull(sum(ifnull(residents_number,0)),0) from t_tiny_village  where  status=0  and (dellable <> 1 or dellable is null) and FIND_IN_SET(town_id,(SELECT town_id from t_store where store_id="
				+ store_id + "))";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		List list = sqlQuery.list();
		if (list != null && list.size() > 0) {
			return Integer.parseInt(list.get(0) + "");
		}
		return null;
	}

	@Override
	public Integer findVillageCountByAreaNo(String areaNo) {
		String sql = "select ifnull(count(*),0) from "
				+ " (select ttv.id from t_area_info tai INNER JOIN t_tiny_village ttv on tai.tin_village_id = ttv.id and tai.tin_village_id is not  NULL and tai.status=0 and ttv.status=0  and (ttv.dellable <> 1 or ttv.dellable is null ) where tai.area_no = '"
				+ areaNo + "'" + " UNION "
				+ " select ttv.id  from t_area_info tai INNER JOIN t_tiny_village  ttv on  tai.status=0 and tai.tin_village_id is null and tai.village_id = ttv.village_id   and ttv.status=0  and (ttv.dellable <> 1 or ttv.dellable is null) where tai.area_no = '"
				+ areaNo + "') c ";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		List list = sqlQuery.list();
		if (list != null && list.size() > 0) {
			return Integer.parseInt(list.get(0) + "");
		}
		return null;
	}

	@Override
	public Integer findResidentsHouseCountByArea(String areaNo) {
		String sql = "select ifnull(sum(ifnull(c.residents_number,0)),0) from "
				+ " (select ttv.id,ttv.residents_number as residents_number from t_area_info tai INNER JOIN t_tiny_village ttv on tai.tin_village_id = ttv.id and tai.tin_village_id is not  NULL and tai.status=0 and ttv.status=0  and (ttv.dellable <> 1 or ttv.dellable is null ) where tai.area_no = '"
				+ areaNo + "' " + " UNION "
				+ " select ttv.id,ttv.residents_number as residents_number from t_area_info tai INNER JOIN t_tiny_village  ttv on  tai.status=0 and tai.tin_village_id is null and tai.village_id = ttv.village_id   and ttv.status=0  and (ttv.dellable <> 1 or ttv.dellable is null) where tai.area_no = '"
				+ areaNo + "') c";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		List list = sqlQuery.list();
		if (list != null && list.size() > 0) {
			return Integer.parseInt(list.get(0) + "");
		}
		return null;
	}

	@Override
	public Map<String, Object> findTinyVillageInfoByTinyId(String TinyId, String where) {
		String sql = "select tiny.id,tiny.`name`,tiny.tinyvillage_type ,IF(tiny.dellable=1,'有','无') as dellable,IFNULL(IFNULL(codd.consumer_number,0)+IFNULL(codd.consumer_number_2018,0),0) as consumer_number,town.`name` as town_name,ifnull(tiny.residents_number,0) as residents_number,ifnull(vill.`name`,'') as village_name,"
				+ " case city.`name` WHEN '北京市辖区' THEN '北京' WHEN '北京县' THEN '北京' WHEN '天津市辖区' THEN '天津' WHEN '天津市辖县' THEN '天津' "
				+ "  WHEN '上海市辖区' THEN '上海' WHEN '上海县' THEN '上海' WHEN '重庆市辖区' THEN '重庆' WHEN '重庆县' THEN '重庆' else city.`name`  END as city_name"
				+ " ,codd.`code` as tinyvillage_code "
				+ " FROM t_tiny_village tiny left join t_village vill on tiny.village_id=vill.id "
				+ " LEFT JOIN t_town town ON town.id=tiny.town_id left join t_county county on county.id=town.county_id left join t_city city on city.id=county.city_id "
				+ " LEFT JOIN tiny_village_code codd ON codd.tiny_village_id = tiny.id"
				+ " where  tiny.status=0 AND codd.`code` is not NULL" + where + " and tiny.id=" + TinyId;
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if (list != null && list.size() > 0) {
			return (Map<String, Object>) list.get(0);
		}
		return null;
	}

	@Override
	public Map<String, Object> exportAboutTinyvillage(String where) {
		String sql = "select tiny.id,IFNULL(area.vallage_area,0) as vallage_area,tiny.`name`,tiny.tinyvillage_type ,IF(tiny.dellable=1,'有','无') as dellable,IFNULL(IFNULL(codd.consumer_number,0)+IFNULL(codd.consumer_number_2018,0),0) as consumer_number,town.`name` as town_name,ifnull(tiny.residents_number,0) as residents_number,vill.`name` as village_name,ifnull(store.name,'') as store_name,"
				+ " CASE WHEN area.id is NULL THEN '无' else '有' end as tiny_map,case city.`name` WHEN '北京市辖区' THEN '北京' WHEN '北京县' THEN '北京' WHEN '天津市辖区' THEN '天津' WHEN '天津市辖县' THEN '天津' "
				+ " WHEN '上海市辖区' THEN '上海' WHEN '上海县' THEN '上海' WHEN '重庆市辖区' THEN '重庆' WHEN '重庆县' THEN '重庆' else city.`name`  END as city_name"
				+ " ,codd.`code` as tinyvillage_code "
				+ " FROM t_tiny_village tiny left join t_village vill on tiny.village_id=vill.id "
				+ " LEFT JOIN (SELECT * FROM tiny_area WHERE `status`=0) area ON area.tiny_village_id=tiny.id "
				+ " LEFT JOIN t_town town ON town.id=tiny.town_id left join t_county county on county.id=town.county_id left join t_city city on city.id=county.city_id "
				+ " LEFT JOIN tiny_village_code codd ON codd.tiny_village_id = tiny.id"
				+ " LEFT JOIN (SELECT * FROM t_store WHERE IFNULL(estate,'')!='闭店中' AND flag=0) store ON store.storeno=area.store_no"
				+ " where 1=1 and tiny.status=0 AND codd.`code` is not NULL " + where;

		Map<String, Object> map_result = new HashMap<String, Object>();
		List<?> list = null;
		Integer total_pages = 0;
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

		map_result.put("data", list);
		map_result.put("status", "success");
		map_result.put("totalPage", total_pages);
		return map_result;
	}

	@Override
	public Integer findResidentsHouseCountByVillageCode(String code) {
		String sql = "select IFNULL(residents_number,0) from t_tiny_village where id = (select tiny_village_id from tiny_village_code where code = '"
				+ code + "') and (dellable <> 1 OR dellable IS NULL)";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		List list = sqlQuery.list();
		if (list != null && list.size() > 0) {
			return Integer.parseInt(list.get(0) + "");
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> findTinyVillageInfoByCode(String code) {
		String findSql = "select village.id as tiny_village_id,village.`name` as tiny_village_name,village.address as tiny_village_address,village.dellable as dellable,c.code as tiny_village_code,v.name as village_name,t.name as town_name,v.id as village_id,t.id as town_id "
				+"from t_tiny_village village INNER JOIN tiny_village_code c ON c.tiny_village_id = village.id and c.code = '"+code+"' and village.`status` = 0 "
				+"INNER JOIN t_village v ON village.village_id = v.id INNER JOIN t_town t ON t.id = village.town_id";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(findSql);
		List list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
		
	}

}
