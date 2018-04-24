package com.cnpc.pms.personal.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.personal.dao.VillageDao;
import com.cnpc.pms.personal.entity.HouseStyle;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.StoreKeeper;
import com.cnpc.pms.personal.entity.Village;
import com.cnpc.pms.personal.manager.StoreKeeperManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.util.DataTransfromUtil;

public class VillageDaoImpl extends BaseDAOHibernate implements VillageDao {

	@Override
	public void deleteDataByVillageId(Long village_id) {
		String t_house_sql = "DELETE FROM t_house WHERE tinyvillage_id in (SELECT id FROM t_tiny_village WHERE village_id="
				+ village_id + ") AND `status`=0 AND (" + "attachment_id IS NOT NULL AND attachment_id!= ''"
				+ ") and id not in ( SELECT house_id  from t_house_style) AND id not in (SELECT house_id FROM t_house_customer )";
		// String t_building_sql="DELETE from t_building WHERE tinyvillage_id in
		// ("+village_id+") and id not in(SELECT building_id FROM t_house WHERE
		// id in (SELECT house_id FROM t_house_customer) or id in(SELECT
		// house_id from t_house_style)) and (attachment_id!='' and
		// attachment_id is not null) ";
		// String t_tiny_village_sql="DELETE from t_tiny_village WHERE
		// attachment_id is NOT NULL and attachment_id!='' and id in
		// ("+village_id+")";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		try {
			SQLQuery sqlQuery = session.createSQLQuery(t_house_sql);
			int executeUpdate = sqlQuery.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		deleteBuildingByVillageId(village_id);
		// SQLQuery t_building_sqlQuery =
		// getHibernateTemplate().getSessionFactory()
		// .getCurrentSession().createSQLQuery(t_building_sql);
		/*
		 * SQLQuery t_tiny_village_sqlQuery =
		 * getHibernateTemplate().getSessionFactory()
		 * .getCurrentSession().createSQLQuery(t_tiny_village_sql);
		 */
		// int executeUpdate2 = t_building_sqlQuery.executeUpdate();
		// t_tiny_village_sqlQuery.executeUpdate();

	}

	@Override
	public String getHousehouseIds() {
		String houseIds = "";
		String t_house = "SELECT * from t_house_style";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		SQLQuery sqlQuery = session.createSQLQuery(t_house);
		List<HouseStyle> list = sqlQuery.addEntity(HouseStyle.class).list();
		if (list != null && list.size() > 0) {
			for (HouseStyle houseStyle : list) {
				houseIds += houseStyle.getHouse_id() + ",";
			}
			houseIds = houseIds.substring(0, houseIds.length() - 1);
			return houseIds;
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> getVillageList(String where, PageInfo pageInfo) {
		// sql查询列，用于分页计算数据总数
		String str_count_sql = "select COUNT(DISTINCT vill.id) "
				+ "from t_village vill LEFT JOIN t_town town ON vill.town_id=town.id "
				+ "LEFT JOIN t_county county ON town.county_id=county.id "
				+ "LEFT JOIN t_city city ON county.city_id=city.id "
				+ "LEFT JOIN t_province province ON city.province_id=province.id WHERE 1=1 and vill.status=0 " + where;
		System.out.println(str_count_sql);
		// sql查询列，用于页面展示所有的数据
		/*
		 * String find_sql=
		 * "select DISTINCT vill.id,vill.`name`,vill.gb_code,vill.household_number,vill.resident_population_number "
		 * + ",province.`name` as province_name, "+
		 * "CASE WHEN city.`name`='市辖区' THEN LEFT(province.`name`,CHAR_LENGTH(province.`name`)-1) "
		 * +
		 * "WHEN city.`name`='县' THEN LEFT(county.`name`,CHAR_LENGTH(county.`name`)-1) "
		 * + " else city.`name` END  as city_name "+
		 * ",county.`name` as county_name,town.`name` as town_name,vill.committee_address "
		 * +
		 * " from t_village vill LEFT JOIN t_town town ON vill.town_id=town.id "
		 * + "LEFT JOIN t_county county ON town.county_id=county.id "+
		 * "LEFT JOIN t_city city ON county.city_id=city.id "+
		 * "LEFT JOIN t_province province ON city.province_id=province.id LEFT JOIN t_store store ON store.town_id=town.id WHERE 1=1 and vill.status=0 "
		 * ;
		 */

		String find_sql = "select DISTINCT vill.id,vill.`name`,vill.gb_code,vill.household_number,vill.resident_population_number  "
				+ ",county.`name` as county_name,town.`name` as town_name,vill.committee_address  "
				+ "from t_village vill LEFT JOIN t_town town ON vill.town_id=town.id  "
				+ "LEFT JOIN t_county county ON town.county_id=county.id left join t_city city on county.city_id=city.id LEFT JOIN t_province province ON city.province_id=province.id "
				+ " WHERE 1=1 and vill.status=0 ";
		StringBuilder sb_sql = new StringBuilder();
		sb_sql.append(find_sql);
		sb_sql.append(where + " order by vill.id desc");
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
			lst_result.add((Map<String, Object>) obj_data);
		}
		return lst_result;
	}

	@Override
	public List getVillList(String name, Long town_id) {
		String houseSql = "SELECT * from t_village WHERE town_id=" + town_id + " AND `name` like '%" + name + "%'";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(houseSql);
		List<Village> list = query.addEntity(Village.class).list();
		return list;
	}

	// 获取当前登录的用户
	public String getUserBysession() {
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		StoreKeeperManager storeKeeperManager = (StoreKeeperManager) SpringHelper.getBean("storeKeeperManager");
		User sessionUser = null;
		if (null != SessionManager.getUserSession() && null != SessionManager.getUserSession().getSessionData()) {
			sessionUser = (User) SessionManager.getUserSession().getSessionData().get("user");
		}
		if ("国安侠".equals(sessionUser.getZw())) {
			Long store_id = sessionUser.getStore_id();
			Store store = storeManager.findStore(store_id);
			Long town_id = DataTransfromUtil.TownIdTransFrom(store.getTown_id());
			return town_id.toString();
		} else if ("国安侠".equals(sessionUser.getZw())) {
			StoreKeeper storeKeeper = storeKeeperManager.findStoreKeeperByEmployeeId(sessionUser.getEmployeeId());
			storeKeeper.getId();
		}
		return null;
	}

	@Override
	public List getVillListLimit(String name, Long town_id) {
		String houseSql = "SELECT * from t_village WHERE town_id=" + town_id + " AND `name` like '%" + name
				+ "%' limit 0,10";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(houseSql);
		List<Village> list = query.addEntity(Village.class).list();
		return list;
	}

	@Override
	public void deleteBuildingByVillageId(Long village_id) {
		String t_building_sql = "  DELETE FROM t_building WHERE tinyvillage_id IN (SELECT id FROM t_tiny_village WHERE village_id="
				+ village_id + " AND `status`=0) "
				+ "AND id NOT IN (SELECT building_id FROM t_house WHERE tinyvillage_id in (SELECT id FROM t_tiny_village WHERE village_id=82898 AND `status`=0) AND `status`=0 GROUP BY building_id) "
				+ "AND (attachment_id != '' AND attachment_id IS NOT NULL) AND `status`=0";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		try {
			SQLQuery t_building_sqlQuery = session.createSQLQuery(t_building_sql);
			int executeUpdate = t_building_sqlQuery.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	@Override
	public List<Village> findVillageByCityName(String cityName) {
		String findSql = "SELECT * FROM t_village WHERE town_id in (SELECT id FROM t_town  WHERE county_id in (SELECT id FROM t_county WHERE city_id in (SELECT id FROM t_city WHERE `name` LIKE '%"
				+ cityName
				+ "%')) AND id in (SELECT town_id FROM t_tiny_village WHERE `status`=0 AND town_id is not NULL GROUP BY town_id))";
		List<Village> list = null;
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		try {
			SQLQuery sqlQuery = session.createSQLQuery(findSql);
			sqlQuery.addEntity(Village.class);
			list = sqlQuery.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return list;
	}

	@Override
	public Integer findVillageCountByCityName(String cityName) {
		String findSql = "select count(tv.id) from t_village tv INNER JOIN ("
				+" select tt.id as id from t_town tt INNER JOIN t_county tc ON tt.county_id  = tc.id INNER JOIN t_city city ON city.id = tc.city_id and city.name like '%"+cityName+"%'"
				+" INNER JOIN t_tiny_village ttv ON ttv.town_id = tt.id and ttv.`status`=0 AND ttv.town_id is not NULL GROUP BY ttv.town_id) t ON tv.town_id = t.id";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		 Session session = sessionFactory.getCurrentSession();
		 SQLQuery sqlQuery =session.createSQLQuery(findSql);
		 List list = sqlQuery.list();
		 if(list!=null&&list.size()>0){
			 return Integer.parseInt(list.get(0)+"");
		 }
		return null;
	}

	@Override
	public Integer findConVillageCountByCityName(String cityName) {
		String findSql = "select count(tv.id) from t_village tv INNER JOIN (SELECT town.id as id FROM t_store store INNER JOIN t_town town ON FIND_IN_SET(store.town_id,town.id)"
				+" WHERE town.id is not NULL and store.flag = 0 and ifnull(store.estate,'')!='闭店中' and store.city_name like '%"+cityName+"%' GROUP BY town.id) t ON tv.town_id = t.id";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		 Session session = sessionFactory.getCurrentSession();
		 SQLQuery sqlQuery =session.createSQLQuery(findSql);
		 List list = sqlQuery.list();
		 if(list!=null&&list.size()>0){
			 return Integer.parseInt(list.get(0)+"");
		 }
		return null;
	}

	@Override
	public Integer findVillageCountByStore(Long storeId) {
		String findSql = "select count(town_id) from t_village where FIND_IN_SET (town_id,(SELECT town_id from t_store where store_id ="+storeId+"))";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		 Session session = sessionFactory.getCurrentSession();
		 SQLQuery sqlQuery =session.createSQLQuery(findSql);
		 List list = sqlQuery.list();
		 if(list!=null&&list.size()>0){
			 return Integer.parseInt(list.get(0)+"");
		 }
		return null;
	}

	@Override
	public Integer findConVillageCountByStore(String storeno) {
		String findSql = "select count(*) from t_village tv INNER JOIN (select ttv.town_id as id from t_tiny_village ttv INNER JOIN tiny_village_code tvc ON ttv.id = tvc.tiny_village_id"
				+" INNER JOIN tiny_area ta ON tvc.code = ta.code and ta.status = 0 and ta.store_no = '"+storeno+"' GROUP BY ttv.town_id ) t ON tv.town_id = t.id  ";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		 Session session = sessionFactory.getCurrentSession();
		 SQLQuery sqlQuery =session.createSQLQuery(findSql);
		 List list = sqlQuery.list();
		 if(list!=null&&list.size()>0){
			 return Integer.parseInt(list.get(0)+"");
		 }
		return null;
	}
	

}
