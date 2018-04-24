package com.cnpc.pms.personal.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.dynamic.entity.MassOrderDto;
import com.cnpc.pms.personal.dao.MassOrderDao;

/**
 * @Function：清洗出的订单Dao实现
 * @author：chenchuang
 * @date:2018年1月9日 下午3:33:54
 *
 * @version V1.0
 */
public class MassOrderDaoImpl extends BaseDAOHibernate implements MassOrderDao {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, Object> queryMassOrder(MassOrderDto massOrderDto, PageInfo pageInfo, String timeFlag) {
		String sqlA = "select CONCAT(a.id,'') as id, a.order_sn,IFNULL(INSERT(a.customer_mobile_phone,4,4,'****'),'') as customer_mobile_phone,a.eshop_name,a.employee_name,"
				+ "a.pubseas_label,a.abnormal_label,a.return_label,a.loan_label,a.create_time,a.sign_time,a.employee_no,IFNULL(a.trading_price,0) as trading_price,IFNULL(a.payable_price,0) as payable_price,IFNULL(ROUND(a.gmv_price,2),0) as gmv_price,"
				+ "a.customer_name,IFNULL(a.addr_name,'') as addr_name,IFNULL(INSERT(a.addr_mobilephone,4,4,'****'),'') as addr_mobilephone,IFNULL(a.addr_address,'') as addr_address,a.channel_name,a.department_name,a.customer_isnew_flag,a.area_code,"
				+ "a.info_employee_a_no,IFNULL(a.order_tag1,'') as order_tag1 from ";

		String sqlB = sqlA;

		if (MassOrderDto.TimeFlag.CUR_DAY.code.equals(timeFlag)) {
			sqlA = sqlA + " df_mass_order_daily a ";
			sqlB = sqlB + " df_mass_order_daily a ";
		} else if (MassOrderDto.TimeFlag.LATEST_MONTH.code.equals(timeFlag)) {
			sqlA = sqlA + " df_mass_order_monthly a ";
			sqlB = sqlB + " df_mass_order_monthly a ";
		} else {
			sqlA = sqlA + " df_mass_order_total a ";
			sqlB = sqlB + " df_mass_order_total a ";
		}

		String sqlTemp2 = " join (select id from ";
		
		if (MassOrderDto.TimeFlag.CUR_DAY.code.equals(timeFlag)) {
			sqlTemp2 = sqlTemp2 + " df_mass_order_daily a ";
		} else if (MassOrderDto.TimeFlag.LATEST_MONTH.code.equals(timeFlag)) {
			sqlTemp2 = sqlTemp2 + " df_mass_order_monthly a ";
		} else {
			sqlTemp2 = sqlTemp2 + " df_mass_order_total a ";
		}

		sqlA = sqlA + sqlTemp2 + " where a.eshop_name NOT LIKE '%测试%' AND a.eshop_white!='QA' and a.store_name NOT LIKE '%测试%' and a.store_white!='QA' AND a.store_status =0 ";
		sqlB = sqlB + " where a.eshop_name NOT LIKE '%测试%' AND a.eshop_white!='QA' and a.store_name NOT LIKE '%测试%' and a.store_white!='QA' AND a.store_status =0 ";

		String sql = "";
		if (StringUtils.isNotEmpty(massOrderDto.getBeginDate())) {
			sql = sql + " and (a.sign_time between '" + massOrderDto.getBeginDate() + " 00:00:00' and '"
					+ massOrderDto.getEndDate() + " 23:59:59')";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getOrder_labels())) {
			sql = sql + " and (";
			String[] names = massOrderDto.getOrder_labels().split(",");
			for (int i = 0; i < names.length; i++) {
				if ("公海订单".equals(names[i].trim())) {
					sql = sql + " a.pubseas_label='1' ";
				} else if ("快周边".equals(names[i].trim())) {
					sql = sql + " a.loan_label='4' ";
				} else if ("异常订单".equals(names[i].trim())) {
					sql = sql + " a.abnormal_label='1' ";
				} else if ("已退款".equals(names[i].trim())) {
					sql = sql + " a.return_label='1' ";
				} else if ("小贷".equals(names[i].trim())) {
					sql = sql + " a.loan_label='1' ";
				} else if ("房".equals(names[i].trim())) {
					sql = sql + " a.loan_label='2' ";
				} else if ("汽车订单".equals(names[i].trim())) {
					sql = sql + " a.loan_label='3' ";
				} else if ("微信礼品卡".equals(names[i].trim())) {
					sql = sql + " a.loan_label='5' ";
				} else if ("集采订单".equals(names[i].trim())) {
					sql = sql + " a.order_tag1 is not null  ";
				}
				if (i == names.length - 1) {
					sql = sql + " )";
				} else {
					sql = sql + " or ";
				}
			}
		}
		if (StringUtils.isNotEmpty(massOrderDto.getOrder_sn())) {
			sql = sql + " and a.order_sn ='" + massOrderDto.getOrder_sn().trim() + "'";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getEshop_id())) {
			sql = sql + " and a.eshop_id ='" + massOrderDto.getEshop_id().trim() + "'";
		}
		if(StringUtils.isNotEmpty(massOrderDto.getStore_no())){
			Map<String,Object> position_obj = queryPlatformidByCode(massOrderDto.getStore_no());
			if (position_obj != null) {
				sql = sql + " and (a.store_code ='" + massOrderDto.getStore_no().trim()+ "' or a.normal_store_id='"+(String) position_obj.get("platformid")+"')";
			}else{
				sql = sql + " and a.store_code ='" + massOrderDto.getStore_no().trim()+ "'";
			}
		}
		if (StringUtils.isNotEmpty(massOrderDto.getEshop_name())) {
			sql = sql + " and a.eshop_name = '" + massOrderDto.getEshop_name().trim() + "'";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getCustomer_isnew())) {
			sql = sql + " and a.customer_isnew_flag in (" + massOrderDto.getCustomer_isnew() + ")";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getCity_name())) {
			sql = sql + " and a.store_city_name = '" + massOrderDto.getCity_name().trim() + "'";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getDepartment_name())) {
			sql = sql + " and a.department_name = '" + massOrderDto.getDepartment_name().trim() + "'";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getDepartment_names())) {
			sql = sql + " and (";
			String[] names = massOrderDto.getDepartment_names().split(",");
			for (int i = 0; i < names.length; i++) {
				if (i == names.length - 1) {
					sql = sql + " a.department_name = '" + names[i].trim() + "')";
				} else {
					sql = sql + " a.department_name = '" + names[i].trim() + "' or ";
				}
			}
		}
		if (StringUtils.isNotEmpty(massOrderDto.getChannel_name())) {
			sql = sql + " and a.channel_name = '" + massOrderDto.getChannel_name().trim() + "'";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getCustomer_name())) {
			sql = sql + " and a.customer_name = '" + massOrderDto.getCustomer_name().trim() + "'";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getCustomer_phone())) {
			sql = sql + " and a.customer_mobile_phone ='" + massOrderDto.getCustomer_phone().trim() + "'";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getAddr_customer_name())) {
			sql = sql + " and a.addr_name = '" + massOrderDto.getAddr_customer_name().trim() + "'";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getAddr_customer_phone())) {
			sql = sql + " and a.addr_mobilephone ='" + massOrderDto.getAddr_customer_phone().trim() + "'";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getEmployee_no())) {
			sql = sql + " and a.employee_no ='" + massOrderDto.getEmployee_no().trim() + "'";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getEmployee_mobile())) {
			sql = sql + " and a.employee_phone ='" + massOrderDto.getEmployee_mobile().trim() + "'";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getEmployee_a_no())) {
			sql = sql + " and a.info_employee_a_no ='" + massOrderDto.getEmployee_a_no().trim() + "'";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getEmployee_a_no_a())) {
			sql = sql + " and a.info_employee_a_no ='" + massOrderDto.getEmployee_a_no_a().trim() + "'";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getArea_code())) {
			sql = sql + " and a.area_code ='" + massOrderDto.getArea_code().trim() + "'";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getEmployee_a_phone())) {
			sql = sql
					+ " and a.info_employee_a_no in (select employee_no from t_humanresources th where th.phone = '"
					+ massOrderDto.getEmployee_a_phone().trim() + "') ";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getEmployee_a_phone_a())) {
			sql = sql
					+ " and a.info_employee_a_no in (select employee_no from t_humanresources th where th.phone = '"
					+ massOrderDto.getEmployee_a_phone_a().trim() + "') ";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getArea_name())) {
			sql = sql + " and a.area_code in (select area_no from t_area ta where ta.name like '%"
					+ massOrderDto.getArea_name().trim() + "%') ";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getVillage_name())) {
			sql = sql
					+ " and a.info_village_code in (select vc.code from tiny_village_code vc where vc.tiny_village_name like '%"
					+ massOrderDto.getVillage_name().trim() + "%') ";
		}

		sqlA = sqlA + sql + " ORDER BY a.sign_time desc ";
		sqlB = sqlB + sql;

		String sql_count = "SELECT COUNT(1) as total FROM (" + sqlB + ") T";

		Query query_count = this.getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createSQLQuery(sql_count);
		Object total = query_count.uniqueResult();
		pageInfo.setTotalRecords(Integer.valueOf(total.toString()));

		int startData = (pageInfo.getCurrentPage() - 1) * pageInfo.getRecordsPerPage();
		int recordsPerPage = pageInfo.getRecordsPerPage();
		sqlA = sqlA + " LIMIT " + startData + "," + recordsPerPage + ") b ON a.id = b.id";

		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sqlA);

		List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

		if (list != null && list.size() > 0) {
			for (Map map : list) {
				String area_code = (String) map.get("area_code");
				String order_sn = (String) map.get("order_sn");
				if (StringUtils.isNotEmpty(area_code)) {
					Map result = this.queryAreaDetailByCode(area_code, order_sn,timeFlag);
					map.put("area_name", result.get("area_name"));
				}
			}
		}

		Map<String, Object> map_result = new HashMap<String, Object>();
		Integer total_pages = (pageInfo.getTotalRecords() - 1) / pageInfo.getRecordsPerPage() + 1;
		map_result.put("pageinfo", pageInfo);
		map_result.put("data", list);
		map_result.put("total_pages", total_pages);
		return map_result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> exportOrder(MassOrderDto massOrderDto, String timeFlag) {
		String sql = "select a.order_sn,IFNULL(a.area_code,'') as area_code,IFNULL(a.info_village_code,'') as village_code,a.info_employee_a_no,IFNULL(INSERT(a.customer_mobile_phone,4,4,'****'),'') as customer_mobile_phone,"
				+ "IFNULL(a.trading_price,0) as trading_price,IFNULL(a.payable_price,0) as payable_price,IFNULL(ROUND(a.gmv_price,2),0) as gmv_price,a.sign_time,a.employee_name,a.employee_phone,"
				+ "a.eshop_name,a.store_name,a.store_code,a.channel_name,a.department_name,a.store_city_name,CASE WHEN a.pubseas_label='1' THEN '是'  ELSE '否' END AS pubseas_label,"
				+ "CASE WHEN a.abnormal_label='1' THEN '是'  ELSE '否' END AS abnormal_label,CASE WHEN a.return_label='1' THEN '是'  ELSE '否' END AS return_label,"
				+ "CASE WHEN a.loan_label='1' THEN '是'  ELSE '否' END AS loan_label,CASE WHEN a.loan_label='3' THEN '是'  ELSE '否' END AS car_label,"
				+ "CASE WHEN a.loan_label='4' THEN '是'  ELSE '否' END AS quick_label,CASE WHEN a.loan_label='5' THEN '是'  ELSE '否' END AS gift_label,"
				+ "CASE WHEN a.customer_isnew_flag='20' THEN '拉新20元' WHEN a.customer_isnew_flag='10' THEN '拉新10元' WHEN a.customer_isnew_flag='0' THEN '拉新'  ELSE '否' END AS customer_isnew_flag,"
				+ "CASE WHEN a.order_tag1 is not null THEN '是'  ELSE '否' END AS order_tag1 from ";

		if (MassOrderDto.TimeFlag.CUR_DAY.code.equals(timeFlag)) {
			sql = sql + " df_mass_order_daily ";
		} else if (MassOrderDto.TimeFlag.LATEST_MONTH.code.equals(timeFlag)) {
			sql = sql + " df_mass_order_monthly ";
		} else {
			sql = sql + " df_mass_order_total ";
		}
		sql = sql + " a where a.eshop_name NOT LIKE '%测试%' AND a.eshop_white!='QA' and a.store_name NOT LIKE '%测试%' and a.store_white!='QA' AND a.store_status =0 ";
		if (StringUtils.isNotEmpty(massOrderDto.getBeginDate())) {
			sql = sql + " and (a.sign_time between '" + massOrderDto.getBeginDate() + " 00:00:00' and '"
					+ massOrderDto.getEndDate() + " 23:59:59')";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getOrder_labels())) {
			sql = sql + " and (";
			String[] names = massOrderDto.getOrder_labels().split(",");
			for (int i = 0; i < names.length; i++) {
				if ("公海订单".equals(names[i].trim())) {
					sql = sql + " a.pubseas_label='1' ";
				} else if ("快周边".equals(names[i].trim())) {
					sql = sql + " a.loan_label='4' ";
				} else if ("异常订单".equals(names[i].trim())) {
					sql = sql + " a.abnormal_label='1' ";
				} else if ("已退款".equals(names[i].trim())) {
					sql = sql + " a.return_label='1' ";
				} else if ("小贷".equals(names[i].trim())) {
					sql = sql + " a.loan_label='1' ";
				} else if ("房".equals(names[i].trim())) {
					sql = sql + " a.loan_label='2' ";
				} else if ("汽车订单".equals(names[i].trim())) {
					sql = sql + " a.loan_label='3' ";
				} else if ("微信礼品卡".equals(names[i].trim())) {
					sql = sql + " a.loan_label='5' ";
				} else if ("集采订单".equals(names[i].trim())) {
					sql = sql + " a.order_tag1 is not null  ";
				}
				if (i == names.length - 1) {
					sql = sql + " )";
				} else {
					sql = sql + " or ";
				}
			}
		}
		if (StringUtils.isNotEmpty(massOrderDto.getOrder_sn())) {
			sql = sql + " and a.order_sn ='" + massOrderDto.getOrder_sn().trim() + "'";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getEshop_id())) {
			sql = sql + " and a.eshop_id ='" + massOrderDto.getEshop_id().trim() + "'";
		}
		if(StringUtils.isNotEmpty(massOrderDto.getStore_no())){
			sql = sql + " and a.store_code ='" + massOrderDto.getStore_no().trim()+ "'";
			Map<String,Object> position_obj = queryPlatformidByCode(massOrderDto.getStore_no());
			if (position_obj != null) {
				sql = sql + " or a.normal_store_id='"+(String) position_obj.get("platformid")+"'";
			}
		}
		if (StringUtils.isNotEmpty(massOrderDto.getEshop_name())) {
			sql = sql + " and a.eshop_name = '" + massOrderDto.getEshop_name().trim() + "'";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getCustomer_isnew())) {
			sql = sql + " and a.customer_isnew_flag in (" + massOrderDto.getCustomer_isnew() + ")";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getCity_name())) {
			sql = sql + " and a.store_city_name = '" + massOrderDto.getCity_name().trim() + "'";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getDepartment_name())) {
			sql = sql + " and a.department_name = '" + massOrderDto.getDepartment_name().trim() + "'";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getDepartment_names())) {
			sql = sql + " and (";
			String[] names = massOrderDto.getDepartment_names().split(",");
			for (int i = 0; i < names.length; i++) {
				if (i == names.length - 1) {
					sql = sql + " a.department_name = '" + names[i].trim() + "')";
				} else {
					sql = sql + " a.department_name = '" + names[i].trim() + "' or ";
				}
			}
		}
		if (StringUtils.isNotEmpty(massOrderDto.getChannel_name())) {
			sql = sql + " and a.channel_name = '" + massOrderDto.getChannel_name().trim() + "'";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getCustomer_name())) {
			sql = sql + " and a.customer_name = '" + massOrderDto.getCustomer_name().trim() + "'";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getCustomer_phone())) {
			sql = sql + " and a.customer_mobile_phone ='" + massOrderDto.getCustomer_phone().trim() + "'";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getAddr_customer_name())) {
			sql = sql + " and a.addr_name = '" + massOrderDto.getAddr_customer_name().trim() + "'";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getAddr_customer_phone())) {
			sql = sql + " and a.addr_mobilephone ='" + massOrderDto.getAddr_customer_phone().trim() + "'";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getEmployee_no())) {
			sql = sql + " and a.employee_no ='" + massOrderDto.getEmployee_no().trim() + "'";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getEmployee_mobile())) {
			sql = sql + " and a.employee_phone ='" + massOrderDto.getEmployee_mobile().trim() + "'";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getEmployee_a_no())) {
			sql = sql + " and a.info_employee_a_no ='" + massOrderDto.getEmployee_a_no().trim() + "'";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getArea_code())) {
			sql = sql + " and a.area_code ='" + massOrderDto.getArea_code().trim() + "'";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getEmployee_a_no_a())) {
			sql = sql + " and a.info_employee_a_no ='" + massOrderDto.getEmployee_a_no_a().trim() + "'";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getEmployee_a_phone())) {
			sql = sql
					+ " and a.info_employee_a_no in (select employee_no from t_humanresources th where th.phone = '"
					+ massOrderDto.getEmployee_a_phone().trim() + "') ";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getEmployee_a_phone_a())) {
			sql = sql
					+ " and a.info_employee_a_no in (select employee_no from t_humanresources th where th.phone = '"
					+ massOrderDto.getEmployee_a_phone_a().trim() + "') ";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getArea_name())) {
			sql = sql + " and a.area_code in (select area_no from t_area ta where ta.name like '%"
					+ massOrderDto.getArea_name().trim() + "%') ";
		}
		if (StringUtils.isNotEmpty(massOrderDto.getVillage_name())) {
			sql = sql
					+ " and a.info_village_code in (select vc.code from tiny_village_code vc where vc.tiny_village_name like '%"
					+ massOrderDto.getVillage_name().trim() + "%') ";
		}
		sql = sql + " ORDER BY a.sign_time desc";

		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryOrderInfoBySN(String order_sn) {
		String sql = "SELECT	CONCAT(dom.id, '') AS id, dom.order_sn,	dom.trading_price, dom.payable_price, dom.total_quantity, dom.create_time, dom.address_name, dom.address_mobilephone, "
				+ "dom.addr_address, IFNULL( dom.customer_mobile_phone, '' ) AS customer_mobile_phone, dom.eshop_name, dom.store_name, dom.employee_name, dom.employee_phone,dom.employee_no, "
				+ "dom.pubseas_label, dom.abnormal_label, dom.return_label,	dom.loan_label,	dom.sign_time, IFNULL(ta.NAME, '') AS area_name, IFNULL(th.NAME, '') AS employee_a_name,"
				+ "IFNULL(th.phone, '') AS emplyee_a_phone FROM	df_mass_order_monthly dom LEFT JOIN t_area ta ON dom.area_code = ta.area_no LEFT JOIN tiny_village_code tvc "
				+ "ON dom.info_village_code = tvc.CODE LEFT JOIN t_humanresources th ON dom.info_employee_a_no = th.employee_no GROUP BY dom.order_sn";

		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		Map<String, Object> order_obj = null;
		List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if (lst_data != null && lst_data.size() > 0) {
			order_obj = (Map<String, Object>) lst_data.get(0);
		}
		return order_obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryAreaDetailByCode(String area_code, String order_sn,String timeFlag) {
		String sql = "SELECT IFNULL(a.area_code,'') AS area_code, IFNULL(a.store_name,'') AS store_name, IFNULL(vc.tiny_village_name,'') AS village_name, "
				+ "IFNULL(ta.`name`,'') AS area_name, vc.tiny_village_id as village_id, vc.code as village_code FROM ";
		if (MassOrderDto.TimeFlag.CUR_DAY.code.equals(timeFlag)) {
			sql = sql + " df_mass_order_daily a ";
		} else if (MassOrderDto.TimeFlag.LATEST_MONTH.code.equals(timeFlag)) {
			sql = sql + " df_mass_order_monthly a ";
		} else {
			sql = sql + " df_mass_order_total a ";
		}		
		sql = sql + "LEFT JOIN t_area ta ON a.area_code = ta.area_no LEFT JOIN tiny_village_code vc ON a.info_village_code = vc. CODE WHERE 1=1 ";

		if (StringUtils.isNotEmpty(area_code) && !area_code.equals("null")) {
			sql = sql + " AND a.area_code = '" + area_code + "'";
		}
		if (StringUtils.isNotEmpty(order_sn)) {
			sql = sql + " AND a.order_sn = '" + order_sn + "'";
		}

		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		Map<String, Object> order_obj = null;
		List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if (lst_data != null && lst_data.size() > 0) {
			order_obj = (Map<String, Object>) lst_data.get(0);
		}
		return order_obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryEmployeeBySN(String order_sn,String timeFlag) {
		String sql = "SELECT IFNULL(a.employee_name,'') AS employee_name, IFNULL(a.employee_phone,'') AS employee_phone, a.info_employee_a_no, IFNULL(th.`name`,'') AS employee_a_name, IFNULL(th.phone,'') as employee_a_phone FROM ";
		if (MassOrderDto.TimeFlag.CUR_DAY.code.equals(timeFlag)) {
			sql = sql + " df_mass_order_daily a ";
		} else if (MassOrderDto.TimeFlag.LATEST_MONTH.code.equals(timeFlag)) {
			sql = sql + " df_mass_order_monthly a ";
		} else {
			sql = sql + " df_mass_order_total a ";
		}				
		sql = sql + "LEFT JOIN t_humanresources th ON a.info_employee_a_no = th.employee_no WHERE	1 = 1 ";

		if (StringUtils.isNotEmpty(order_sn)) {
			sql = sql + " AND a.order_sn = '" + order_sn + "'";
		}

		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		Map<String, Object> order_obj = null;
		List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if (lst_data != null && lst_data.size() > 0) {
			order_obj = (Map<String, Object>) lst_data.get(0);
		}
		return order_obj;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryPlatformidByCode(String storeno){
		String sql = "select t.platformid from t_store t where 1=1 ";
		if(StringUtils.isNotEmpty(storeno)){
			sql = sql + " AND t.storeno='"+storeno+"' ";
		}
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 获得查询数据
		Map<String, Object> order_obj = null;
		List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if (lst_data != null && lst_data.size() > 0) {
			order_obj = (Map<String, Object>) lst_data.get(0);
		}
		return order_obj;
	}

}
