package com.cnpc.pms.personal.dao.impl;

import java.util.ArrayList;
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
import com.cnpc.pms.personal.dao.TownDao;
import com.cnpc.pms.personal.entity.County;
import com.cnpc.pms.personal.entity.Town;

public class TownDaoImpl extends BaseDAOHibernate implements TownDao {

	@Override
	public Town getTownByID(Long id) {
		String findSql = "select * from t_town where id=" + id;
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(findSql);
		query.addEntity(Town.class);
		List<Town> list = query.list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> getTownList(String where, PageInfo pageInfo) {
		// sql查询列，用于分页计算数据总数
		String str_count_sql = "select COUNT(DISTINCT town.id) "
				+ "from t_town town LEFT JOIN t_county county ON town.county_id=county.id  "
				+ "LEFT JOIN t_city city ON county.city_id=city.id  "
				+ "LEFT JOIN t_province province ON city.province_id=province.id  WHERE 1=1  " + where;
		System.out.println(str_count_sql);
		// sql查询列，用于页面展示所有的数据
		String find_sql = "select DISTINCT town.id,town.`name`,town.gb_code " + ",province.`name` as province_name,  "
				+ "CASE WHEN city.`name`='市辖区' THEN LEFT(province.`name`,CHAR_LENGTH(province.`name`)-1)  "
				+ "WHEN city.`name`='县' THEN LEFT(county.`name`,CHAR_LENGTH(county.`name`)-1)  "
				+ " else city.`name` END  as city_name  " + ",county.`name` as county_name "
				+ " from t_town town LEFT JOIN t_county county ON town.county_id=county.id  "
				+ "LEFT JOIN t_city city ON county.city_id=city.id  "
				+ "LEFT JOIN t_province province ON city.province_id=province.id  WHERE 1=1  ";
		StringBuilder sb_sql = new StringBuilder();
		sb_sql.append(find_sql);
		sb_sql.append(where + " order by town.id desc");
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
	public List<Town> getTown(Long provinceid, String town_name, String province_name, String wString) {
		String findSql = null;
		if (province_name != null) {
			findSql = "SELECT town.* FROM t_province pro LEFT JOIN t_city city ON pro.id=city.province_id "
					+ " LEFT JOIN t_county coun ON city.id=coun.city_id "
					+ " LEFT JOIN t_town town ON town.county_id=coun.id WHERE town.`name` LIKE '%" + town_name
					+ "%' and city.`name` LIKE '%" + province_name + "%' " + wString + " LIMIT 10";
		} else if (provinceid != null && !"".equals(provinceid)) {
			findSql = "SELECT town.* FROM t_province pro LEFT JOIN t_city city ON pro.id=city.province_id "
					+ " LEFT JOIN t_county coun ON city.id=coun.city_id "
					+ " LEFT JOIN t_town town ON town.county_id=coun.id WHERE pro.id= " + provinceid
					+ " and town.`name` LIKE '%" + town_name + "%' " + wString + " LIMIT 10";
		} else {
			findSql = "SELECT town.* FROM t_province pro LEFT JOIN t_city city ON pro.id=city.province_id "
					+ " LEFT JOIN t_county coun ON city.id=coun.city_id "
					+ " LEFT JOIN t_town town ON town.county_id=coun.id WHERE  town.`name` LIKE '%" + town_name + "%' "
					+ wString + " LIMIT 10";
		}
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(findSql);
		query.addEntity(Town.class);
		List<Town> list = query.list();
		return list;
	}

	@Override
	public Map<String, Object> getTownInfoByStore(Long storeId) {
		String sql = "SELECT town_id from t_store where store_id =" + storeId;
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		String town_id = (String) query.uniqueResult();
		String[] townArr = town_id.split(",");
		StringBuilder sb = new StringBuilder();
		for (String id : townArr) {
			sb.append(",").append(id);
		}

		sql = "SELECT tt.id,tt.name,tci.id as cityId FROM  (select id,name,county_id from t_town   where id in("
				+ sb.substring(1)
				+ ")) AS tt INNER JOIN t_county AS tc  ON tt.county_id = tc.id INNER JOIN t_city AS tci ON tc.city_id = tci.id ";
		query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);

		List<?> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

		if (list != null && list.size() > 0) {
			return (Map<String, Object>) list.get(0);
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> selectAllTownByName(Long city_id, String name) {

		String findSql = "select a.id,a.name from t_town a inner join t_county b on a.county_id = b.id inner join t_city c on b.city_id = c.id where 1=1 ";
		if (city_id != null) {
			findSql = findSql + " and c.id=" + city_id;
		}
		if (name != null && !"".equals(name)) {
			findSql += " and a.name like '%" + name + "%'";
		}
		findSql += " limit 0,10";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(findSql);

		List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if (list != null && list.size() > 0) {
			return list;
		}
		return null;

	}

	@Override
	public List<Map<String, Object>> getTownListByCityName(String where, PageInfo pageInfo) {
		// sql查询列，用于分页计算数据总数
		String str_count_sql = "SELECT count(1) from t_town town LEFT JOIN t_county county ON town.county_id=county.id LEFT JOIN t_city city ON county.city_id=city.id WHERE 1=1 "
				+ where;
		System.out.println(str_count_sql);
		// sql查询列，用于页面展示所有的数据
		String find_sql = "SELECT town.id,town.`name` from t_town town LEFT JOIN t_county county ON town.county_id=county.id LEFT JOIN t_city city ON county.city_id=city.id WHERE 1=1 ";
		StringBuilder sb_sql = new StringBuilder();
		sb_sql.append(find_sql);
		sb_sql.append(where + " order by town.sort desc");
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
	public void updateTownSortById(String idString) {
		deleteTownSortById();
		String sb_sql = "update t_town SET sort=1 WHERE id in (" + idString + ")";// 添加排序
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createSQLQuery(sb_sql.toString());
		query.executeUpdate();

	}

	@Override
	public void deleteTownSortById() {
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		try {
			String sb_sql = "update t_town SET sort=NULL";// 清空的sql
			Query query = session.createSQLQuery(sb_sql.toString());
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

	}

	@Override
	public List<County> getCountyData(Long provinceid, String county_name, String province_name, String wString) {
		String findSql = null;
		if (province_name != null && !"请选择".equals(province_name)) {
			findSql = "SELECT coun.* FROM t_province pro LEFT JOIN t_city city ON pro.id=city.province_id "
					+ " LEFT JOIN t_county coun ON city.id=coun.city_id " + "  WHERE coun.`name` LIKE '%" + county_name
					+ "%' and city.`name` LIKE '%" + province_name + "%' " + wString + " LIMIT 10";
		} else if (provinceid != null && !"".equals(provinceid)) {
			findSql = "SELECT coun.* FROM t_province pro LEFT JOIN t_city city ON pro.id=city.province_id "
					+ " LEFT JOIN t_county coun ON city.id=coun.city_id " + "  WHERE pro.id= " + provinceid
					+ " and coun.`name` LIKE '%" + county_name + "%' " + wString + " LIMIT 10";
		} else {
			findSql = "SELECT coun.* FROM t_province pro LEFT JOIN t_city city ON pro.id=city.province_id "
					+ " LEFT JOIN t_county coun ON city.id=coun.city_id " + "  WHERE  coun.`name` LIKE '%" + county_name
					+ "%' " + wString + " LIMIT 10";
		}
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(findSql);
		query.addEntity(County.class);
		List<County> list = query.list();
		return list;
	}

	@Override
	public List<Town> findTownCountByCityName(String cityName) {
		String findSql = "SELECT * FROM t_town  WHERE county_id in (SELECT id FROM t_county WHERE city_id in (SELECT id FROM t_city WHERE `name` LIKE '%"
				+ cityName
				+ "%')) AND id in (SELECT town_id FROM t_tiny_village WHERE `status`=0 AND town_id is not NULL GROUP BY town_id)";
		/*
		 * SQLQuery query = getHibernateTemplate().getSessionFactory()
		 * .getCurrentSession().createSQLQuery(findSql);
		 * 
		 * return query.list();
		 */
		List<Town> list = null;
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		try {
			SQLQuery sqlQuery = session.createSQLQuery(findSql);
			sqlQuery.addEntity(Town.class);
			list = sqlQuery.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return list;
	}

	@Override
	public Integer findTownCountByCityId(Long city_id) {
		String sql = "select count(ttn.id) from t_town ttn INNER JOIN t_county tcy ON ttn.county_id = tcy.id"
				+ " INNER JOIN t_city tcity ON tcy.city_id = tcity.id where tcity.id in (SELECT t_city.id FROM t_city WHERE t_city.name like "
				+ "CONCAT('%',(SELECT t_dist_citycode.cityname FROM t_dist_citycode WHERE t_dist_citycode.id=" + city_id
				+ "),'%'))";
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
	public Integer findConTownCountByCityId(Long city_id) {
		String sql = "select count(*) from (SELECT town.id FROM t_store store INNER JOIN t_town town ON FIND_IN_SET(store.town_id,town.id) and store.flag = 0 and ifnull(store.estate,'')!='闭店中' "
				+ " INNER JOIN t_dist_citycode citycode ON store.city_name = citycode.cityname  WHERE town.id is not NULL and citycode.id = "
				+ city_id + " GROUP BY town.id) t";
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
	public Integer getTownCountByStore(Long storeId) {
		String sql = "SELECT ifnull(town_id,'') from t_store where store_id =" + storeId;
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		String town_id = (String) query.uniqueResult();
		if (!town_id.equals("")) {
			String[] townArr = town_id.split(",");
			int length = townArr.length;
			return length;
		}
		return null;
	}

	@Override
	public Integer getConTownCountByStore(String storeno) {
		String sql = "select count(*) from (select count(town_id) from t_tiny_village where id in(select tiny_village_id from tiny_village_code where code in (select code from tiny_area where status = 0 and store_no = '"
				+ storeno + "')) GROUP BY town_id) t";
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

}
