package com.cnpc.pms.personal.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.classic.Session;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.dao.StoreDynamicDao;
import com.cnpc.pms.personal.entity.StoreDynamic;

public class StoreDynamicDaoImpl extends BaseDAOHibernate implements StoreDynamicDao {

	@Override
	public StoreDynamic findMaxOrdnumber(String string) {
		StoreDynamic store = null;
		String sql = "SELECT * FROM t_store_dynamic WHERE city_name='" + string + "' ORDER BY ordnumber DESC LIMIT 1 ";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		try {
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			List<StoreDynamic> list1 = sqlQuery.addEntity(StoreDynamic.class).list();
			if (list1 != null && list1.size() > 0) {
				store = list1.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return store;
	}

	@Override
	public String getMaxStoreDynamicNo(StoreDynamic storeDynamic) {
		String find_sql = "SELECT MAX(right(storeno,4)) from t_store_dynamic WHERE city_name='"
				+ storeDynamic.getCityName() + "' AND (storeno is not NULL OR storeno!='')";
		if ("V".equals(storeDynamic.getStoretype())) {
			find_sql = find_sql + " AND storetype='V'";
		} else {
			find_sql = find_sql + " AND storetype<>'V'";
		}
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(find_sql);
		List<String> list = query.list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> getStoreDynamicInfoList(String where, PageInfo pageInfo) {
		// sql查询列，用于分页计算数据总数
		String str_count_sql = "SELECT count(DISTINCT(sto.store_id)) from t_store_dynamic sto LEFT JOIN t_town town ON FIND_IN_SET(town.id,sto.town_id) WHERE 1=1 and sto.flag=0 "
				+ where;
		System.out.println(str_count_sql);
		// sql查询列，用于页面展示所有的数据
		String find_sql = "SELECT sto.auditor_status,case WHEN sto.auditor_status=2 THEN '驳回' WHEN sto.auditor_status=1  "
				+ "THEN (CASE WHEN sto.estate is NULL THEN '筹备中' WHEN sto.estate='运营中' THEN '运营中' else '筹备中' END ) "
				+ " ELSE IF(sto.estate='筹备中','筹备中','运营中') END as estate,sto.store_id,sto.storeno,sto.storetypename,sto.city_name,sto.`name`,GROUP_CONCAT(town.name) as town_name,sto.detail_address,sto.address,sto.platformname,sto.number,sto.id,CONCAT('第',sto.ordnumber,'家店') as ordnumber,DATE_FORMAT(sto.create_time,'%Y-%m-%d %H:%i:%s') as create_time from t_store_dynamic sto left join t_town town ON FIND_IN_SET(town.id,sto.town_id)  WHERE 1=1 and sto.flag=0 "
				+ where + " GROUP BY sto.store_id order by sto.store_id desc";
		// SQL查询对象
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(find_sql);

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

	// 根据store_id回写 店长的skid
	@Override
	public int updateStoreskid(String store_ids, Long userid) {
		// 删除原来用户在门店表中的ID
		removeskid(userid);
		String update_sql = "update t_store_dynamic set t_store_dynamic.skid=" + userid + " where store_id in("
				+ store_ids + ")";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(update_sql);
		int update = query.executeUpdate();
		return update;
	}

	// 根据store_id回写 运营经理的rmid
	@Override
	public int updateStorermid(String store_ids, Long userid) {
		removermid(userid);
		String update_sql = "update t_store_dynamic set t_store_dynamic.rmid=" + userid + " where store_id in("
				+ store_ids + ")";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(update_sql);
		int update = query.executeUpdate();
		return update;
	}

	public int removeskid(Long userid) {
		String removeSql = "update t_store_dynamic set t_store_dynamic.skid=null where skid=" + userid;
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(removeSql);
		int update = query.executeUpdate();
		return update;
	}

	public int removermid(Long userid) {
		String removeSql = "update t_store_dynamic set t_store_dynamic.rmid=null where rmid=" + userid;
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(removeSql);
		int update = query.executeUpdate();
		return update;
	}

	@Override
	public List<StoreDynamic> findStoreDynamicByCity_nameorderByOpentime(String city_name) {
		List<StoreDynamic> list = null;
		String t_find_sql = "SELECT *  FROM t_store_dynamic WHERE open_shop_time is not NULL AND city_name='"
				+ city_name + "' ORDER BY open_shop_time ";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		try {
			SQLQuery sqlQuery = session.createSQLQuery(t_find_sql);
			list = sqlQuery.addEntity(StoreDynamic.class).list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return list;
	}

	@Override
	public Integer findMaxStoreDynamicOreNumber(String city_name) {
		List<StoreDynamic> list = null;
		Integer long1 = 0;
		String sql = "SELECT * FROM t_store_dynamic WHERE open_shop_time is not NULL AND city_name='" + city_name + "'";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		try {
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			list = sqlQuery.addEntity(StoreDynamic.class).list();
			if (list != null && list.size() > 0) {
				return list.size();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return long1;
	}

	@Override
	public List<StoreDynamic> findStoreDynamicIsnullOrdnumber(String city_name) {
		List<StoreDynamic> list = null;
		String sql = "SELECT *  FROM t_store_dynamic WHERE open_shop_time is NULL and storeno is not null AND city_name='"
				+ city_name + "' ORDER BY create_time,update_time ";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		try {
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			list = sqlQuery.addEntity(StoreDynamic.class).list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return list;
	}

}
