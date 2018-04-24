package com.cnpc.pms.personal.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.classic.Session;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.dynamic.entity.DynamicDto;
import com.cnpc.pms.personal.dao.StoreDao;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.utils.DateUtils;

public class StoreDaoImpl extends BaseDAOHibernate implements StoreDao {
	@Override
	public List<Map<String, Object>> getStoreInfoList(String where, PageInfo pageInfo) {
		// sql查询列，用于分页计算数据总数
		String str_count_sql = "SELECT count(DISTINCT(sto.store_id)) from t_store sto LEFT JOIN t_town town ON FIND_IN_SET(town.id,sto.town_id) WHERE 1=1 and sto.flag=0 "
				+ where;
		System.out.println(str_count_sql);
		// sql查询列，用于页面展示所有的数据
		String find_sql = "SELECT sto.auditor_status, "
				+ "case WHEN sto.estate='运营中' THEN '运营中' WHEN sto.estate='筹备中' THEN '筹备中' WHEN sto.estate='闭店中' THEN '闭店中' ELSE '待开业' END as estate,sto.store_id,sto.storeno,sto.storetypename,sto.city_name,sto.`name`,GROUP_CONCAT(town.name) as town_name,sto.detail_address,sto.address,sto.platformname,sto.number,sto.id,CONCAT('第',sto.ordnumber,'家店') as ordnumber,DATE_FORMAT(sto.create_time,'%Y-%m-%d %H:%i:%s') as create_time from t_store sto left join t_town town ON FIND_IN_SET(town.id,sto.town_id)  WHERE 1=1 and sto.flag=0 "
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

	@Override
	public Store getMaxStoreData() {
		String houseSql = "SELECT * FROM t_store ORDER BY store_id DESC LIMIT 1";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(houseSql);
		List<Store> list = query.addEntity(Store.class).list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public int saveStore(Store store) {
		Date date = new Date();
		// java.sql.Date sdate = new java.sql.Date(date.getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = dateFormat.format(date);
		String addStoreSql = "INSERT INTO t_store (" + "`store_id`," + "`name`," + "`type`," + "`town_id`,"
				+ "`status`," + "`version`," + "`create_user`," + "`create_time`," + "`address`," + "`detail_address`,"
				+ "`mobilephone`," + "`city_name`," + "`skid`" + ")" + "VALUES(" + store.getStore_id() + ",'"
				+ store.getName() + "'," + store.getType() + "," + store.getTown_id() + "," + 0 + "," + 0 + ",'"
				+ store.getCreate_user() + "','" + format + "',"
				+ (store.getAddress() == null ? null : "'" + store.getAddress() + "'") + ","
				+ (store.getDetail_address() == null ? null : "'" + store.getDetail_address() + "'") + ","
				+ (store.getMobilephone() == null ? null : "'" + store.getMobilephone() + "'") + ",'"
				+ store.getCityName() + "'," + store.getSkid() + ")";

		System.out.println(addStoreSql);

		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(addStoreSql);
		int update = query.executeUpdate();
		return update;
	}

	public int removeskid(Long userid) {
		String removeSql = "update t_store set t_store.skid=null where skid=" + userid;
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(removeSql);
		int update = query.executeUpdate();
		return update;
	}

	public int removermid(Long userid) {
		String removeSql = "update t_store set t_store.rmid=null where rmid=" + userid;
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(removeSql);
		int update = query.executeUpdate();
		return update;
	}

	// 根据store_id回写 店长的skid
	public int updateStoreskid(String store_ids, Long userid) {
		// 删除原来用户在门店表中的ID
		removeskid(userid);
		String update_sql = "update t_store set t_store.skid=" + userid + " where store_id in(" + store_ids + ")";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(update_sql);
		int update = query.executeUpdate();
		return update;
	}

	// 根据store_id回写 运营经理的rmid
	public int updateStorermid(String store_ids, Long userid) {
		removermid(userid);
		String update_sql = "update t_store set t_store.rmid=" + userid + " where store_id in(" + store_ids + ")";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(update_sql);
		int update = query.executeUpdate();
		return update;
	}

	@Override
	public void updateStoreSortById(String idString) {
		deleteStoreSortById();
		String sb_sql = "update t_store SET sort=1 WHERE store_id in (" + idString + ")";// 添加排序
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createSQLQuery(sb_sql.toString());
		query.executeUpdate();

	}

	@Override
	public void deleteStoreSortById() {
		String sb_sql = "update t_store SET sort=NULL";// 清空的sql
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createSQLQuery(sb_sql.toString());
		query.executeUpdate();

	}

	@Override
	public int getCountStore(String whString) {
		// sql查询列，用于分页计算数据总数
		String str_count_sql = "SELECT count(1) from t_store sto WHERE 1=1 and sto.flag=0 " + whString;

		// 查询数据量对象
		SQLQuery countQuery = getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createSQLQuery(str_count_sql);
		return Integer.valueOf(countQuery.list().get(0).toString());
	}

	@Override
	public List<Map<String, Object>> getStoreInfoListData(String where, PageInfo pageInfo) {
		// sql查询列，用于页面展示所有的数据
		String find_sql = "SELECT sto.store_id,sto.city_name,sto.`name`,sto.number,sto.platformname,sto.address,sto.town_name from t_store sto WHERE 1=1 and sto.flag=0 "
				+ where + " order by remark ASC,sto.store_id desc";
		// SQL查询对象
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(find_sql);
		// 获得查询数据
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
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
	public List<Map<String, Object>> getStoreById(Long id) {
		String sql = "select a.*,b.name as keeperName,b.employeeId from "
				+ "(select skid,name,store_id from t_store  where rmid=" + id + ") as a"
				+ " INNER JOIN tb_bizbase_user as b on a.skid = b.id";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@Override
	public List<Map<String, Object>> getStoreKeeper(Long id) {
		String sql = "select b.name,mobilephone,b.employeeId,a.skid from "
				+ "(select DISTINCT skid,store_id from t_store  where rmid=" + id + ") as a"
				+ " INNER JOIN tb_bizbase_user as b on a.skid = b.id";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@Override
	public String getMaxStoreNo(Store store) {
		String find_sql = "SELECT MAX(right(storeno,4)) from t_store WHERE city_name='" + store.getCityName()
				+ "' AND (storeno is not NULL OR storeno!='')";
		if ("V".equals(store.getStoretype())) {
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
	public List<Map<String, Object>> getCityNameOfCSZJ(Long employee_id, Long city_id) {
		String sql = "select a.id as ctid,a.cityname as name from  t_dist_citycode a inner join t_dist_city b on a.citycode= b.citycode and b.pk_userid="
				+ employee_id;
		if (city_id != null) {
			sql = sql + " and a.id=" + city_id;
		}
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@Override
	public List<Map<String, Object>> getCityNOOfCSZJ(Long city_id) {
		String sql = "select t.cityno  from  t_dist_citycode t ";
		if (city_id != null) {
			sql = sql + " where t.id=" + city_id;
		}
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@Override
	public List<Map<String, Object>> getAllStoreOfCSZJ(Long employee_no, Long cityId) {
		String whereStr = "";
		String sql = "";
		if (cityId != null) {
			whereStr = " and tdc.id=" + cityId;
		}
		if (employee_no != null && !"".equals(employee_no)) {
			sql = "select t.store_id,t.name,ifnull(tbu.name,'') as keeperName,tbu.employeeId,t.storeno from (select * from t_store where flag=0 and ifnull(estate,'')!='闭店中') t  inner join  (select tdc.id,tdc.cityname from t_dist_city a"
					+ "   INNER JOIN t_dist_citycode tdc on a.citycode = tdc.citycode and a.pk_userid=" + employee_no
					+ whereStr + " ) t1" + "	on t.city_name  = t1.cityname "
					+ " left JOIN tb_bizbase_user as tbu on t.skid = tbu.id";
		} else {
			sql = "select t.store_id,t.name,ifnull(tbu.name,'') as keeperName,tbu.employeeId,t.storeno from (select * from t_store where flag=0 and ifnull(estate,'')!='闭店中') t  inner join  "
					+ " (select tdc.id,tdc.cityname from    t_dist_citycode tdc where id=" + cityId + " ) t1"
					+ " on t.city_name  = t1.cityname " + " left JOIN tb_bizbase_user as tbu on t.skid = tbu.id";
		}

		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@Override
	public List<Map<String, Object>> getAllStoreKeeperOfCSZJ(Long employee_no, Long cityId) {
		String whereStr = "";
		String sql = "";
		if (cityId != null) {
			whereStr = " and tdc.id=" + cityId;
		}
		if (employee_no != null && !"".equals(employee_no)) {
			sql = "select tbu.name as keeperName,tbu.employeeId,tbu.mobilephone,tbu.id from (select distinct skid from t_store t  inner join  (select tdc.id,tdc.cityname from t_dist_city a"
					+ "   INNER JOIN t_dist_citycode tdc on a.citycode = tdc.citycode and a.pk_userid=" + employee_no
					+ whereStr + " ) t1" + "	on t.city_name  = t1.cityname ) t2"
					+ "   INNER JOIN tb_bizbase_user as tbu on t2.skid = tbu.id";
		} else {
			sql = "select tbu.name as keeperName,tbu.employeeId,tbu.mobilephone,tbu.id from (select distinct skid from t_store t  inner join  (select tdc.id,tdc.cityname from "
					+ "    t_dist_citycode tdc where tdc.id=" + cityId + ") t1" + "	on t.city_name  = t1.cityname ) t2"
					+ "   INNER JOIN tb_bizbase_user as tbu on t2.skid = tbu.id";
		}

		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@Override
	public List<Map<String, Object>> getAllStoreOfCRM(Long employee_no, Long cityId, String role) {
		String whereStr = "";
		String cityStr = "";

		if ("CSZJ".equals(role)) {// 城市总监
			if (cityId != null && cityId > 0) {
				cityStr = " and  tdc.id=" + cityId;
			}
			if (employee_no != null && !"".equals(employee_no)) {
				whereStr = "select t.platformid,t.name,tbu.name as employeeName,t.skid,t.number,t.storeno,t.store_id from (select * from t_store where flag=0 and ifnull(estate,'')!='闭店中') t  inner join  (select tdc.id,tdc.cityname from t_dist_city a"
						+

						"   INNER JOIN t_dist_citycode tdc on a.citycode = tdc.citycode and a.pk_userid=" + employee_no
						+ cityStr + " ) t1" + "	 on t.city_name  = t1.cityname "
						+ " left join tb_bizbase_user as tbu on t.skid = tbu.id order by convert(t.name using gbk) asc";
			} else {
				whereStr = "select t.platformid,t.name,tbu.name as employeeName,t.skid,t.number,t.storeno,t.store_id from (select * from t_store where flag=0 and ifnull(estate,'')!='闭店中') t  inner join  (select tdc.id,tdc.cityname from "
						+

						"   t_dist_citycode tdc where tdc.id=" + cityId + " ) t1" + "	on t.city_name  = t1.cityname "
						+ "   left join tb_bizbase_user as tbu on t.skid = tbu.id order by convert(t.name using gbk) asc";
			}

		} else if ("QYJL".equals(role)) {// 区域经理
			whereStr = "select t.platformid,t.name,tbu.name as employeeName,t.store_id,t.storeno,t.skid,t.number from (select * from t_store where flag=0 and ifnull(estate,'')!='闭店中') t left join tb_bizbase_user as tbu on t.rmid = tbu.id where  t.rmid = "
					+ employee_no + " order by convert(t.name using gbk) asc";
		} else if ("ZB".equals(role)) {// 总部
			if (cityId != null) {
				cityStr = " where   tdc.id=" + cityId;
			}
			whereStr = "select t.platformid,t.name,tbu.name as employeeName,t.skid,t.number,t.storeno,t.store_id from (select * from t_store where flag=0 and ifnull(estate,'')!='闭店中') t inner join (select *  from t_dist_citycode tdc "
					+ cityStr + ") t1 on t.city_name  = t1.cityname"
					+ " left join tb_bizbase_user as tbu on t.skid = tbu.id order by convert(t.name using gbk) asc";
		}

		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(whereStr);
		List<Map<String, Object>> storeDate = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

		return storeDate;
	}

	@Override
	public List<Map<String, Object>> getAllStoreGMVMonthOfCRM(Long employee_no, Long cityId, String role, DynamicDto dd,
			String orderBy) {
		String sqlStr = "";
		String countStr = "";
		String cityStr = "";
		String orderStr = "";
		if (cityId != null && cityId > 0) {
			cityStr = " t4.id=" + cityId;
		}
		if (orderBy != null && orderBy.length() > 0) {
			orderStr += " order by t3." + orderBy + " desc ";
		}
		countStr += "select city_name,count(store_name) as storeCount from (select city_name,store_name from ds_storetrade GROUP BY city_name,store_name) t5 where t5.city_name=CONCAT((select t6.cityname from t_dist_citycode t6 where t6.id="
				+ cityId + "),'市')";
		sqlStr += "select t3.city_name,t3.storeno,t3.store_name,t3.pesgmv,t3.`year`,t3.`month`,t3.pesgmvpriord from "
				+ "(select t1.storeno,t1.city_name,t1.store_name, t1.pesgmv,t1.`year` as year,t1.`month`  as month,"
				+ "CASE WHEN round((t1.pesgmv-t2.pesgmv)/t2.pesgmv*100,1) IS NULL THEN -1000 ELSE round((t1.pesgmv-t2.pesgmv)/t2.pesgmv*100,1) END  as pesgmvpriord from ds_storetrade t1 left join ds_storetrade t2 on "
				+ "t1.`month`-1=t2.`month` and t1.`year`=t2.`year` and t1.store_name=t2.store_name GROUP BY t1.store_name,t1.`year`,"
				+ "t1.`month` ORDER BY t1.store_name) t3 WHERE t3.pesgmvpriord is not NULL and t3.store_name not like '%企业购%' and t3.`month`='"
				+ dd.getMonth() + "' and t3.`year`='" + dd.getYear()
				+ "' and t3.city_name=CONCAT((select t4.cityname from t_dist_citycode t4 where " + cityStr + "),'市')"
				+ orderStr;
		if (dd.getSearchstr() != null && Integer.parseInt(dd.getSearchstr()) > 0) {
			sqlStr += " limit 0,10 ";
		}

		SQLQuery queryCount = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(countStr);
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sqlStr);
		List<Map<String, Object>> storeCountDate = queryCount.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
				.list();
		List<Map<String, Object>> storeDate = new ArrayList<Map<String, Object>>();
		if (Integer.parseInt(storeCountDate.get(0).get("storeCount").toString()) > 5) {
			storeDate = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		}

		return storeDate;
	}

	@Override
	public List<Map<String, Object>> getStoreDate(String where) {
		String find_sql = "SELECT sto.`name`,sto.storeno,sto.platformname,sto.platformid,sto.storetypename,sto.city_name FROM t_store sto LEFT JOIN t_town town ON FIND_IN_SET(town.id,sto.town_id) where 1=1 and sto.storetype!='W' AND sto.storeno is not NULL and sto.flag=0 "
				+ where + "GROUP BY sto.store_id ";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(find_sql);
		List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
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
	public List<Map<String, Object>> getStoreListDate(String where) {
		String find_sql = "SELECT store.city_name,store.`name`,IF(store.storetype='W','',store.storeno)as storeno,IF(store.storetype='W','',store.storetypename) as storetypename,store.superMicro,store.estate,DATE_FORMAT(store.open_shop_time,'%Y/%c/%e') as open_shop_time,town.`name` as townname,IFNULL(store.address,store.detail_address) as detail_address,store.ordnumber,IFNULL(usee.mobilephone,usee.phone) as mobilephone,  usee.`name` as shopmanager, "
				+ " coun.`name` as countname,IFNULL(store.nature,'') as nature,IFNULL(store.tenancy_term,'') as tenancyTerm,IFNULL(store.rental,'') as rental,IFNULL(store.payment_method,'') as payment_method FROM	t_store store LEFT JOIN tb_bizbase_user usee ON store.skid = usee.id LEFT JOIN t_town town ON town.id IN (store.town_id) LEFT JOIN t_county coun ON coun.id = town.county_id  "
				+ " WHERE (store.`name` not LIKE '%储备店%' and store.`name` not LIKE '%测试%' and store.`name` not LIKE '%办公室%')  AND store.storeno is not NULL and store.flag=0 and store.auditor_status=3 "
				+ where + " order by store.city_name desc,store.ordnumber";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(find_sql);
		List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
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
	public List<Map<String, Object>> getStoreByCity(Integer target, Long cityId, Long employeeId, String search_str) {

		String searchSql = "";

		if (target == 0 || target == 3) {// 总部、事业群
			String cityStr = "";
			if (cityId != null) {
				cityStr = " where id=" + cityId;
			}
			String storeStr = "";
			if (search_str != null && !"".equals(search_str)) {
				storeStr = " and t.name like '%" + search_str + "%'";
			}
			searchSql = "select t.name,t.store_id,t.platformid,t.number,t.storeno,t1.citycode from t_store t  inner join  (select * from t_dist_citycode "
					+ cityStr + ") t1" + " on t.city_name  = t1.cityname  and t.flag=0 and ifnull(t.estate,'')!='闭店中' "
					+ storeStr + " limit 10";

		} else if (target == 1) {// 城市
			String cityStr = "";
			if (cityId != null) {
				cityStr = "  and tdc.id=" + cityId;
			}
			String storeStr = "";
			if (search_str != null && !"".equals(search_str)) {
				storeStr = " and t.name like '%" + search_str + "%'";
			}
			if (employeeId != null && !"".equals(employeeId)) {
				searchSql = "select t.name,t.store_id,t.platformid,t.number,t.storeno,t1.citycode from t_store t  inner join  (select tdc.id,tdc.cityname,tdc.citycode from t_dist_citycode tdc "
						+ "   INNER JOIN t_dist_city a on a.citycode = tdc.citycode and a.pk_userid=" + employeeId
						+ cityStr + ") t1"
						+ "	on t.city_name  = t1.cityname  and t.flag=0 and ifnull(estate,'')!='闭店中' " + storeStr
						+ " limit 10";
			} else {
				searchSql = "select t.name,t.store_id,t.platformid,t.number,t.storeno,t1.citycode from t_store t  inner join  (select tdc.id,tdc.cityname,tdc.citycode from t_dist_citycode tdc "
						+ "   where tdc.id=" + cityId + ") t1"
						+ "	on t.city_name  = t1.cityname  and t.flag=0 and ifnull(t.estate,'')!='闭店中' " + storeStr
						+ " limit 10";
			}

		}

		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(searchSql);
		// 获得查询数据
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@Override
	public List<Store> findStoreByCity_nameorderByOpentime(String city_name) {
		List<Store> list = null;
		String t_find_sql = "SELECT *  FROM t_store WHERE open_shop_time is not NULL AND city_name='" + city_name
				+ "' ORDER BY open_shop_time ";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		try {
			SQLQuery sqlQuery = session.createSQLQuery(t_find_sql);
			list = sqlQuery.addEntity(Store.class).list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return list;
	}

	@Override
	public Integer findMaxStoreOreNumber(String city_name) {
		List<Store> list = null;
		Integer long1 = 0;
		String sql = "SELECT * FROM t_store WHERE open_shop_time is not NULL AND city_name='" + city_name + "'";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		try {
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			list = sqlQuery.addEntity(Store.class).list();
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
	public List<Store> findStoreIsnullOrdnumber(String city_name) {
		List<Store> list = null;
		String sql = "SELECT *  FROM t_store WHERE open_shop_time is NULL and storeno is not null AND city_name='"
				+ city_name + "' ORDER BY create_time,update_time ";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		try {
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			list = sqlQuery.addEntity(Store.class).list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return list;
	}

	@Override
	public Store findMaxOrdnumber(String string) {
		Store store = null;
		String sql = "SELECT * FROM t_store WHERE city_name='" + string
				+ "' and storetype!='V' ORDER BY ordnumber DESC LIMIT 1 ";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		try {
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			List<Store> list1 = sqlQuery.addEntity(Store.class).list();
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
	public List<Store> findStoreDateEstate() {
		List<Store> list = null;
		String sql = "SELECT * FROM t_store WHERE (estate not in ('闭店中','试运营','运营中','试营业') or estate is NULL) and open_shop_time is not NULL AND DATE_FORMAT(open_shop_time,'%Y-%m-%d')<=DATE_FORMAT(NOW(),'%Y-%m-%d')";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		try {
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			list = sqlQuery.addEntity(Store.class).list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> findTinyDetails(String city_name, String whString, PageInfo pageInfo) {
		List<Map<String, Object>> list = null;
		String sql = "SELECT tin.tiny_village_code,tin.tiny_village_type,tin.tiny_village_name,tin.town_name,tin.village_name,tin.ifcoordinates,tin.house_count,tin.building_count,s.name FROM t_tiny_village_data_details tin LEFT JOIN t_store s ON FIND_IN_SET(tin.town_id,s.town_id) "
				+ " WHERE tin.city_name like '%" + city_name + "%' " + whString;
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		try {
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			list = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
					.setFirstResult(pageInfo.getRecordsPerPage() * (pageInfo.getCurrentPage() - 1))
					.setMaxResults(pageInfo.getRecordsPerPage()).list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> queryBatchResultForVillage() {
		String querySql = "SELECT tttt.name as area_name,tttt.area_no as area_no,tttt.id as area_id,"
				+ "tsss.tinyname as tinyname,tsss.`code` as code_vill,tsss.tinyvillageid as tinid,"
				+ "tttt.employee_a_name as employee_a_name,tttt.employee_b_name as employee_b_name,tttt.employee_a_no as employee_a_no,"
				+ "tttt.employee_b_no as employee_b_no,tsss.sumbuilding as sumbuilding,tsss.sumhouse as sumhouse,"
				+ "gggg.store_id as store_id,gggg.name as store_name,gggg.city_name as city_name,vallage_area as vallage_area  FROM ("
				+ " SELECT tinyvill.`name` as tinyname,tinyvill.id as tinyvillageid,ooooo.code,"
				+ " IFNULL(ss.buildingcou,0)+IFNULL(buildingss.businsscount,0)+IFNULL(bung.bungalow,0) as sumbuilding,"
				+ " IFNULL(hou.buildhouse,0)+IFNULL(buildingss.bushouse,0)+IFNULL(bung.bungalow,0) as sumhouse "
				+ " FROM t_tiny_village tinyvill	LEFT JOIN (SELECT tinyvillage_id,COUNT(*) as buildingcou "
				+ " FROM t_building WHERE `status`=0 AND type=1 GROUP BY tinyvillage_id)ss ON ss.tinyvillage_id=tinyvill.id LEFT JOIN"
				+ "(SElECT buil.tinyvillage_id,count(distinct(hou.id)) as buildhouse FROM t_building buil LEFT JOIN t_house hou "
				+ " ON buil.id=hou.building_id   WHERE buil.`status`=0 AND buil.type=1 AND hou.house_type=1 AND hou.`status`=0 "
				+ " group by buil.tinyvillage_id) hou ON hou.tinyvillage_id=tinyvill.id left join "
				+ " (select buil.tinyvillage_id,count(distinct(buil.id)) as businsscount,sum(buhouse.buildcount) as bushouse from "
				+ " (SELECT building_id,count(*) as buildcount FROM t_house WHERE house_type=2 and status=0 group by building_id) "
				+ "as buhouse left join t_building buil on buhouse.building_id=buil.id group by buil.tinyvillage_id) as buildingss "
				+ " on buildingss.tinyvillage_id=tinyvill.id left join "
				+ " (select tinyvillage_id,count(*) as bungalow from t_house where house_type=0 and status=0 group by "
				+ " tinyvillage_id) as bung on bung.tinyvillage_id=tinyvill.id LEFT JOIN tiny_village_code ooooo "
				+ " ON ooooo.tiny_village_id=tinyvill.id  WHERE tinyvill.`status`=0 AND tinyvill.village_id is not NULL "
				+ " AND tinyvill.town_id is not NULL) tsss LEFT JOIN (SELECT	c.*, d. CODE FROM (SELECT a.store_id,a.`name`,a.area_no,a.id,"
				+ "b.tin_village_id,a.employee_a_no,a.employee_a_name,a.employee_b_no,a.employee_b_name FROM	t_area a INNER JOIN t_area_info b "
				+ " ON a.id = b.area_id AND b.tin_village_id IS NOT NULL AND b. STATUS = 0  INNER JOIN t_tiny_village ttv "
				+ " ON b.tin_village_id = ttv.id AND ttv. STATUS = 0 AND ( ttv.dellable <> 1 OR ttv.dellable IS NULL ) UNION "
				+ " SELECT ta.store_id,ta. NAME,ta.area_no,ta.id,ttv.id AS tin_village_id,ta.employee_a_no,ta.employee_a_name,ta.employee_b_no,"
				+ "ta.employee_b_name FROM t_area ta INNER JOIN t_area_info tai ON ta.id = tai.area_id AND tai. STATUS = 0"
				+ " AND tai.tin_village_id IS NULL INNER JOIN t_tiny_village ttv ON tai.village_id = ttv.village_id AND ttv. STATUS = 0"
				+ " AND ( ttv.dellable <> 1 OR ttv.dellable IS NULL)) c INNER JOIN tiny_village_code d ON c.tin_village_id = d.tiny_village_id) tttt "
				+ " ON tsss.tinyvillageid= tttt.tin_village_id LEFT JOIN tiny_area lllll ON lllll.tiny_village_id=tsss.tinyvillageid "
				+ " and lllll.status='0' LEFT JOIN t_store gggg ON tttt.store_id=gggg.store_id";
		// String querySql = "select * from df_tinyarea_datainfo limit 0,10";
		List<Map<String, Object>> list = null;
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		try {
			SQLQuery sqlQuery = session.createSQLQuery(querySql);
			list = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return list;
	}

	@Override
	public boolean insertIntoVillageInfo(String sql) {
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		boolean query = false;
		try {
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			sqlQuery.executeUpdate();
			query = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return query;
	}

	@Override
	public List<Map<String, Object>> queryAllTinyareaDatainfoUserCountSix() {
		String querySql = "SELECT COUNT(tttt.tinyvillage_name) as customer_count,"
				+ "tttt.tinyvillage_name as t_tiny_village,tttt.tinyvillage_ids as tinyvillage_id "
				+ "FROM ( SELECT tc.*,th.id as houseId,ttv.id as tinyvillage_ids,ttv.name as tinyvillage_name "
				+ "FROM t_customer tc INNER JOIN ( SELECT thc2.* FROM ( SELECT max(id) AS id FROM t_house_customer  "
				+ "GROUP BY customer_id) thc1 INNER JOIN t_house_customer thc2 ON thc1.id = thc2.id) "
				+ "thc ON tc.id = thc.customer_id INNER  JOIN t_house th ON th.id = thc.house_id INNER JOIN "
				+ "t_tiny_village ttv ON ttv.id = th.tinyvillage_id INNER JOIN t_house_style hs ON hs.house_id = th.id WHERE "
				+ "( hs.house_area IS NOT NULL AND hs.house_area != '') AND ( hs.house_toward IS NOT NULL AND "
				+ "hs.house_toward != '') AND ( hs.house_style IS NOT NULL AND hs.house_style != '' ) AND ( "
				+ "hs.house_pic IS NOT NULL AND (trim(hs.house_pic) != '') AND (trim(hs.house_pic) != '无') OR "
				+ "th.house_type = 0) AND ( tc.`name` IS NOT NULL AND tc.`name` != '') AND tc.sex IS NOT NULL AND ( "
				+ "tc.mobilephone IS NOT NULL AND tc.mobilephone != '') AND (tc.age IS NOT NULL) AND (tc.sex IS NOT NULL)) tttt ";
		List<Map<String, Object>> list = null;
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		boolean query = false;
		try {
			SQLQuery sqlQuery = session.createSQLQuery(querySql);
			list = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			query = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return list;
	}

	@Override
	public boolean updateVillageInfoDataUserCountSix(String tinyvillage_id, String customer_count) {
		String updateSql = "UPDATE df_tinyarea_datainfo SET user_count_6 = '" + customer_count
				+ "' where tin_village_id ='" + tinyvillage_id + "'";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		boolean query = false;
		try {
			SQLQuery sqlQuery = session.createSQLQuery(updateSql);
			sqlQuery.executeUpdate();
			query = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return query;
	}

	@Override
	public boolean updateVillageInfoDataUserCountEighteen(String tinyvillage_id, String customer_count) {
		String updateSql = "UPDATE df_tinyarea_datainfo SET user_count_18 = '" + customer_count
				+ "' where tin_village_id ='" + tinyvillage_id + "'";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		boolean query = false;
		try {
			SQLQuery sqlQuery = session.createSQLQuery(updateSql);
			sqlQuery.executeUpdate();
			query = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return query;
	}

	@Override
	public List<Map<String, Object>> queryAllTinyareaDatainfoUserCountEighteen() {
		List<Map<String, Object>> list = null;
		String querySql = "SELECT COUNT(tttt.tinyvillage_name) as customer_count,"
				+ "tttt.tinyvillage_name as t_tiny_village,tttt.tinyvillage_ids as tinyvillage_id "
				+ "FROM ( SELECT tc.*,th.id as houseId,ttv.id as tinyvillage_ids,ttv.name as tinyvillage_name "
				+ "FROM t_customer tc INNER JOIN ( SELECT thc2.* FROM ( SELECT max(id) AS id FROM t_house_customer GROUP BY customer_id "
				+ ") thc1 INNER JOIN t_house_customer thc2 ON thc1.id = thc2.id ) thc ON tc.id = thc.customer_id INNER  JOIN "
				+ "t_house th ON th.id = thc.house_id INNER JOIN t_tiny_village ttv ON ttv.id = th.tinyvillage_id INNER JOIN "
				+ "t_house_style hs ON hs.house_id = th.id WHERE ( hs.house_area IS NOT NULL AND hs.house_area != '') AND ( "
				+ "hs.house_toward IS NOT NULL AND hs.house_toward != '') AND ( hs.house_style IS NOT NULL AND "
				+ "hs.house_style != '') AND ( hs.house_pic IS NOT NULL AND (trim(hs.house_pic) != '') AND (trim(hs.house_pic) != '无') "
				+ "OR th.house_type = 0) AND ( tc.`name` IS NOT NULL AND tc.`name` != '') AND tc.sex IS NOT NULL AND ( tc.mobilephone "
				+ "IS NOT NULL AND tc.mobilephone != '') AND (tc.age IS NOT NULL) AND (tc.sex IS NOT NULL) AND (tc.customer_pic is not "
				+ "null AND tc.customer_pic != '') AND (tc.nationality is not null AND tc.nationality != '') AND (tc.house_property is "
				+ "not null AND tc.house_property != '') AND (tc.income is not null AND tc.income != '') AND (tc.work_overtime is not "
				+ "null AND tc.work_overtime != '') AND (tc.family_num is not null ) AND (tc.preschool_num is not null ) AND "
				+ "(tc.minor_num is not null ) AND (tc.pet_type is not null AND tc.pet_type != '')) tttt GROUP BY tttt.tinyvillage_ids ";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		boolean query = false;
		try {
			SQLQuery sqlQuery = session.createSQLQuery(querySql);
			list = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			query = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> getProvinceNOOfCSZJ(String province_id) {
		String sql = "select left(t.gb_code,6) as gb_code  from  t_province t ";
		if (province_id != null) {
			sql = sql + " where t.id=" + province_id;
		}
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@Override
	public boolean cleanDataInfoTable() {
		String updateSql = "delete from df_tinyarea_datainfo ";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		boolean query = false;
		try {
			SQLQuery sqlQuery = session.createSQLQuery(updateSql);
			sqlQuery.executeUpdate();
			query = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return query;
	}

	@Override
	public List<Map<String, Object>> getCityNOOfCityById(Long city_id) {
		String sql = "select t.cityno from t_dist_citycode t ";

		if (city_id != null) {
			sql = sql + "where t.id ='" + city_id + "' ";
		}
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@Override
	public List<Map<String, Object>> selectStoreKeeperInfoByStoreId(Long storeId) {

		String sql = "select tb.id,tb.name,tb.employeeId,tb.mobilephone,t2.store_id,t2.storeno,t2.storeName from (select t1.store_id,t1.name as storeName,t1.storeno,t1.skid  from (select skid from t_store where store_id ="
				+ storeId
				+ ") t inner join t_store t1 on t.skid = t1.skid) t2 left join tb_bizbase_user tb on t2.skid = tb.id";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	@Override
	public List<Map<String, Object>> selectStoreByCityId(Long city_id) {
		String sql = "SELECT ts.* from t_store ts inner join t_dist_citycode tdc ON ts.city_name=tdc.cityname WHERE tdc.id ="
				+ city_id;
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}

	// 门店运营 列表中的方法
	@Override
	public Map<String, Object> queryStoreStatusList(Store store, PageInfo pageInfo) {
		String wheresql = " 1=1 ";
		if (store != null && store.getCityName() != null && store.getCityName().length() > 0) {
			wheresql += " and a.city_name = '" + store.getCityName() + "'";
		}

		// 判断日期 如果是week 本周 month 本月
		String datestr = store.getName();
		if (datestr != null && datestr.equals("week")) {
			// 本周
			String startdate = DateUtils.getCurrentMonday();
			String enddate = DateUtils.getPreviousSunday();
			wheresql += " and a.open_shop_time >= '" + startdate + " 00:00:00' AND a.open_shop_time <= '" + enddate
					+ " 23:59:59'";
		}
		if (datestr != null && datestr.equals("month")) {
			// 本月
			String curmonth = DateUtils.getCurrMonthDate();
			wheresql += " and DATE_FORMAT(a.open_shop_time,'%Y-%m')='" + curmonth + "' ";
		}

		wheresql += " ORDER BY a.open_shop_time DESC ";
		String sql = "SELECT a.city_name,a.storetypename,a.ordnumber,a.store_id,a.name,a.estate,a.storeno,a.open_shop_time FROM t_store a where "
				+ wheresql;

		// String sql = "SELECT * FROM t_humanresources a where "+sqlwhere ;
		String sql_count = "SELECT count(*) as total FROM (" + sql + ") b";

		Query query_count = this.getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createSQLQuery(sql_count);
		List<?> total = query_count.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		Map<String, Object> maps = (Map<String, Object>) total.get(0);

		pageInfo.setTotalRecords(Integer.valueOf(maps.get("total").toString()));

		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);

		List<?> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
				.setFirstResult(pageInfo.getRecordsPerPage() * (pageInfo.getCurrentPage() - 1))
				.setMaxResults(pageInfo.getRecordsPerPage()).list();
		Map<String, Object> map_result = new HashMap<String, Object>();
		Integer total_pages = (pageInfo.getTotalRecords() - 1) / pageInfo.getRecordsPerPage() + 1;
		map_result.put("pageinfo", pageInfo);
		map_result.put("data", list);
		map_result.put("total_pages", total_pages);
		return map_result;
	}

	// 门店运营 - 导出门店状态 -tab2
	@Override
	public List<Map<String, Object>> exportStoreStatusList(Store store) {
		String wheresql = " 1=1 ";
		if (store != null && store.getCityName() != null && store.getCityName().length() > 0) {
			wheresql += " and a.city_name = '" + store.getCityName() + "'";
		}

		// 判断日期 如果是week 本周 month 本月
		String datestr = store.getName();
		if (datestr != null && datestr.equals("week")) {
			// 本周
			String startdate = DateUtils.getCurrentMonday();
			String enddate = DateUtils.getPreviousSunday();
			wheresql += " and a.open_shop_time >= '" + startdate + " 00:00:00' AND a.open_shop_time <= '" + enddate
					+ " 23:59:59'";
		}
		if (datestr != null && datestr.equals("month")) {
			// 本月
			String curmonth = DateUtils.getCurrMonthDate();
			wheresql += " and DATE_FORMAT(a.open_shop_time,'%Y-%m')='" + curmonth + "' ";
		}

		wheresql += " ORDER BY a.open_shop_time DESC ";
		String sql = "SELECT a.city_name,a.storetypename,a.ordnumber,a.store_id,a.name,a.estate,a.storeno,a.open_shop_time FROM t_store a where "
				+ wheresql;
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}

	@Override
	public List<Map<String, Object>> findStoreXuanZhi(String where, PageInfo pageInfo) {
		// sql查询列，用于页面展示所有的数据
		String find_sql = "SELECT sto.store_id,sto.storetypename,sto.city_name,sto.`name`,GROUP_CONCAT(town.name) as town_name  from t_store sto left join t_town town ON FIND_IN_SET(town.id,sto.town_id) "
				+ " WHERE sto.store_id not in (SELECT store_id FROM t_store_document_info) AND sto.storetype!='V' and IFNULL(sto.estate,'')!='闭店中' AND sto.auditor_status=3 AND (sto.`name` NOT LIKE '%储备店%' AND sto.name NOT LIKE '%办公室%' AND sto.name NOT LIKE '%测试%') and sto.flag=0 "
				+ where + " GROUP BY sto.store_id";
		// SQL查询对象
		String str_count_sql = "SELECT count(1) from (" + find_sql + ") cc";
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

	@Override
	public Store insertStore(Store store) {
		Date date = new Date();
		// java.sql.Date sdate = new java.sql.Date(date.getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = dateFormat.format(date);
		String sql = "INSERT INTO t_store (`store_id`,`name`,`type`,`version`,`town_id`,`county_id`,`city_id`,`province_id`,`status`,  `create_user`, `create_time`, `update_user`, `update_time`,  `address`, `detail_address`, `mobilephone`,`id`,`platformid`,`city_name`, `rmid`, `skid`, `open_shop_time`, `platformname`, `town_name`, `storeno`, `storetype`, `storetypename`,  `estate`, `ordnumber`, `superMicro`, `cityno`, `agency_fee`, `county_ids`, `increase`, `increase_fee`, `nature`, `payment_method`, `rent_area`, `rent_free`, `rental`, `taxes`, `tenancy_term`, `usable_area`, `work_id`, `place_town_id`, `store_position`, `gaode_adcode`, `gaode_address`, `gaode_citycode`, `gaode_provincecode`) VALUES ("
				+ store.getStore_id() + ",'" + store.getName() + "'," + store.getType() + "," + store.getVersion() + ","
				+ (store.getTown_id() == null ? null : "'" + store.getTown_id() + "'") + "," + store.getCounty_id()
				+ "," + store.getCity_id() + "," + store.getProvince_id() + "," + store.getStatus() + ","
				+ (store.getCreate_user() == null ? null : "'" + store.getCreate_user() + "'") + ",'" + format + "',"
				+ (store.getUpdate_user() == null ? null : "'" + store.getUpdate_user() + "'") + ",'" + format + "',"
				+ (store.getAddress() == null ? null : "'" + store.getAddress() + "'") + ","
				+ (store.getDetail_address() == null ? null : "'" + store.getDetail_address() + "'") + ","
				+ (store.getMobilephone() == null ? null : "'" + store.getMobilephone() + "'") + ","
				+ (store.getId() == null ? null : "'" + store.getId() + "'") + ","
				+ (store.getPlatformid() == null ? null : "'" + store.getPlatformid() + "'") + "," + "'"
				+ store.getCityName() + "'," + (store.getRmid() == null ? null : store.getRmid()) + ","
				+ (store.getSkid() == null ? null : store.getSkid()) + ","
				+ (store.getOpen_shop_time() == null ? null : "'" + store.getOpen_shop_time() + "'") + ","
				+ (store.getPlatformname() == null ? null : "'" + store.getPlatformname() + "'") + ","
				+ (store.getTown_name() == null ? null : "'" + store.getTown_name() + "'") + ",'" + store.getStoreno()
				+ "','" + store.getStoretype() + "','" + store.getStoretypename() + "',"
				+ (store.getEstate() == null ? null : "'" + store.getEstate() + "'") + ","
				+ (store.getOrdnumber() == null ? null : "'" + store.getOrdnumber() + "'") + ","
				+ (store.getSuperMicro() == null ? null : "'" + store.getSuperMicro() + "'") + ","
				+ (store.getCityNo() == null ? null : "'" + store.getCityNo() + "'") + ","
				+ (store.getAgency_fee() == null ? null : "'" + store.getAgency_fee() + "'") + ","
				+ (store.getCounty_ids() == null ? null : "'" + store.getCounty_ids() + "'") + ","
				+ (store.getIncrease() == null ? null : "'" + store.getIncrease() + "'") + ","
				+ (store.getIncrease_fee() == null ? null : "'" + store.getIncrease_fee() + "'") + ","
				+ (store.getNature() == null ? null : "'" + store.getNature() + "'") + ","
				+ (store.getPayment_method() == null ? null : "'" + store.getPayment_method() + "'") + ","
				+ (store.getRent_area() == null ? null : "'" + store.getRent_area() + "'") + ","
				+ (store.getRent_free() == null ? null : "'" + store.getRent_free() + "'") + ","
				+ (store.getRental() == null ? null : "'" + store.getRental() + "'") + ","
				+ (store.getTaxes() == null ? null : "'" + store.getTaxes() + "'") + ","
				+ (store.getTenancyTerm() == null ? null : "'" + store.getTenancyTerm() + "'") + ","
				+ (store.getUsable_area() == null ? null : "'" + store.getUsable_area() + "'") + ","
				+ (store.getWork_id() == null ? null : "'" + store.getWork_id() + "'") + ","
				+ (store.getPlace_town_id() == null ? null : "'" + store.getPlace_town_id() + "'") + ","
				+ (store.getStore_position() == null ? null : "'" + store.getStore_position() + "'") + ","
				+ (store.getGaode_adCode() == null ? null : "'" + store.getGaode_adCode() + "'") + ","
				+ (store.getGaode_address() == null ? null : "'" + store.getGaode_address() + "'") + ","
				+ (store.getGaode_cityCode() == null ? null : "'" + store.getGaode_cityCode() + "'") + ","
				+ (store.getGaode_provinceCode() == null ? null : "'" + store.getGaode_provinceCode() + "'") + ")";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		int update = query.executeUpdate();
		return store;
	}

}
