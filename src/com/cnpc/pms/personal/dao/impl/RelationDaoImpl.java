package com.cnpc.pms.personal.dao.impl;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IPage;
import com.cnpc.pms.base.paging.ISort;
import com.cnpc.pms.base.paging.SortFactory;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserDTO;
import com.cnpc.pms.personal.dao.HouseCustomerDao;
import com.cnpc.pms.personal.dao.RelationDao;
import com.cnpc.pms.personal.entity.Customer;
import com.cnpc.pms.personal.entity.Relation;
import com.cnpc.pms.personal.entity.ViewAddressCustomer;
import com.cnpc.pms.personal.manager.CustomerManager;
import com.cnpc.pms.personal.manager.HouseCustomerManager;
import com.cnpc.pms.personal.manager.ViewAddressCustomerManager;
import com.cnpc.pms.platform.dao.OrderDao;
import com.cnpc.pms.utils.ValueUtil;
import com.google.inject.internal.MapMaker;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelationDaoImpl extends BaseDAOHibernate implements RelationDao {

	
	
	@Override
	public Map<String, Object> queryRelationList(IPage pageInfo,UserDTO userDTO,String sbfCondition) {
		Map<String,Object> map_result = new HashMap<String, Object>();
		StringBuilder str_fromtable = new StringBuilder();
		str_fromtable.append("select a.*,max(a.relation_date) as lastdate,count(1) as totalcount from (SELECT r.*,c.name,c.mobilephone FROM	t_relation r LEFT JOIN t_customer c ON c.id = r.customer_id LEFT JOIN tb_bizbase_user u ON u.employeeId=r.employee_no   ");
		str_fromtable.append("WHERE 1=1 ");
		if(userDTO!=null&&userDTO.getStore_id()!=null){
			str_fromtable.append(" and u.store_id="+userDTO.getStore_id());
		}else{
			str_fromtable.append(" and r.employee_no='"+userDTO.getEmployeeId()+"' ");
		}
		
		if(userDTO!=null&&userDTO.getEmployeeId()!=null&&userDTO.getUsergroup().getCardtype()==0){
			str_fromtable.append(" and r.employee_no='"+userDTO.getEmployeeId()+"'");
		}
		
		str_fromtable.append(" ORDER BY r.relation_date DESC) a ");
		str_fromtable.append(sbfCondition.toString());
		str_fromtable.append(" GROUP BY a.name,a.mobilephone ORDER BY a.update_time DESC   ");
		
		StringBuffer sbfcount = new StringBuffer();
		sbfcount.append("select count(1) from (");
		sbfcount.append("select * from (SELECT r.*,c.name,c.mobilephone FROM	t_relation r LEFT JOIN t_customer c ON c.id = r.customer_id LEFT JOIN tb_bizbase_user u ON u.employeeId=r.employee_no   ");
		sbfcount.append("WHERE 1=1 ");
		if(userDTO!=null&&userDTO.getStore_id()!=null){
			sbfcount.append(" and u.store_id="+userDTO.getStore_id());
		}else{
			sbfcount.append(" and r.employee_no='"+userDTO.getEmployeeId()+"' ");
		}
		
		if(userDTO!=null&&userDTO.getEmployeeId()!=null&&userDTO.getUsergroup().getCardtype()==0){
			sbfcount.append(" and r.employee_no='"+userDTO.getEmployeeId()+"'");
		}
		
		sbfcount.append(" ORDER BY r.relation_date DESC) a ");
		sbfcount.append(sbfCondition.toString());
		sbfcount.append(" GROUP BY a.name,a.mobilephone ");
		sbfcount.append(" ) b ");
		
		
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sbfcount.toString());
		pageInfo.setTotalRecords(Integer.valueOf(query.uniqueResult().toString()));
		
		query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(str_fromtable.toString());
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setFirstResult(
				pageInfo.getRecordsPerPage()
						* (pageInfo.getCurrentPage() - 1))
				.setMaxResults(pageInfo.getRecordsPerPage()).list();
		
		List<Map<String, Object>> ret_data = new ArrayList<Map<String,Object>>();
		for(Map<String, Object> o:lst_data){
			String customer_id = o.get("customer_id")==null?"":o.get("customer_id").toString();
			if(customer_id!=""){
				String sqlcount = "SELECT count(1) FROM t_relation WHERE t_relation.status = 0 and t_relation.customer_id="+customer_id;
				SQLQuery querycount = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sqlcount);
				String count = querycount.uniqueResult().toString();
				o.put("totalcount", count);
				ret_data.add(o);
			}
		}
		
		
		map_result.put("pageinfo", pageInfo);
		map_result.put("header", "");
		map_result.put("data", ret_data);
		return map_result;
	}
	
	
	
	/**
     * APP拜访记录中 根据电话和姓名  查询拜访记录列表
     */
	@Override
	public List<Customer> queryRelationListForApp(Long store_id,String employee_no,String cardtype,StringBuffer sbfCondition) {
		Map<String,Object> map_dresult = new HashMap<String, Object>();
		StringBuilder str_fromtable = new StringBuilder();
		str_fromtable.append("select a.*,max(a.relation_date) as lastdate,count(1) as totalcount from (SELECT r.*,c.name,c.mobilephone FROM	t_relation r LEFT JOIN t_customer c ON c.id = r.customer_id LEFT JOIN tb_bizbase_user u ON u.employeeId=r.employee_no   ");
		str_fromtable.append("WHERE 1=1 ");
		if(store_id!=null){
			str_fromtable.append(" and u.store_id="+store_id);
		}else{
			str_fromtable.append(" and r.employee_no='"+employee_no+"' ");
		}
		
		if(employee_no!=null&&cardtype!=null&&cardtype.equals("0")){
			str_fromtable.append(" and r.employee_no='"+employee_no+"'");
		}
		
		str_fromtable.append(" ORDER BY r.relation_date DESC) a ");
		str_fromtable.append(sbfCondition.toString());
		str_fromtable.append(" GROUP BY a.name,a.mobilephone ORDER BY a.update_time DESC   ");
		
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(str_fromtable.toString());
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		
		List<Customer> ret_data = new ArrayList<Customer>();
		for(Map<String, Object> o:lst_data){
			String customer_id = o.get("customer_id")==null?"":o.get("customer_id").toString();
			if(customer_id!=""){
				Customer customer = new Customer();
				String sqlcount = "SELECT count(1) FROM t_relation WHERE t_relation.customer_id="+customer_id;
				SQLQuery querycount = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sqlcount);
				String count = querycount.uniqueResult().toString();
				customer.setMinor_num(Integer.parseInt(count));
				customer.setWork_overtime(o.get("lastdate")==null?"":o.get("lastdate").toString());
				customer.setName(o.get("name")==null?"":o.get("name").toString());
				customer.setMobilephone(o.get("mobilephone")==null?"":o.get("mobilephone").toString());
				customer.setId(Long.parseLong(o.get("customer_id").toString()));
				ret_data.add(customer);
			}
		}
		return ret_data;
	}
	
	
	
	
	
	
	@Override
	public List<Customer> queryRelationListByUserCard(Long store_id,String employee_no,String cardtype){
		List<Customer> custList = new ArrayList<Customer>();
		StringBuilder str_fromtable = new StringBuilder();
		str_fromtable.append("select a.*,max(a.relation_date) as maxdate,count(1) as totalcount from (SELECT r.*,c.name,c.mobilephone FROM	t_relation r LEFT JOIN t_customer c ON c.id = r.customer_id LEFT JOIN tb_bizbase_user u ON u.employeeId=r.employee_no   ");
		str_fromtable.append(" WHERE 1=1 and r.status = 0 ");
		if(store_id!=null){
			str_fromtable.append(" and u.store_id="+store_id);
		}else{
			str_fromtable.append(" and 1=0 ");
		}
		
		if(employee_no!=null&&cardtype.equals("0")&&employee_no!=null){
			str_fromtable.append(" and r.employee_no='"+employee_no+"'");
		}
		
		str_fromtable.append(" ORDER BY r.relation_date DESC) a ");
		str_fromtable.append(" WHERE DATE_FORMAT(a.relation_date,'%Y-%m-%d')>=DATE_ADD(curdate(),interval -day(curdate())+1 day) AND DATE_FORMAT(a.relation_date,'%Y-%m-%d')<=last_day(curdate()) ");
		str_fromtable.append(" GROUP BY a.name,a.mobilephone ");
		
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(str_fromtable.toString());
		
		List<Map<String, Object>> lists = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        
		ViewAddressCustomerManager viewAddressCustomerManager = (ViewAddressCustomerManager)SpringHelper.getBean("viewAddressCustomerManager");
		CustomerManager customerManager = (CustomerManager) SpringHelper.getBean("customerManager");
		HouseCustomerDao houseCustomerDao = (HouseCustomerDao)SpringHelper.getBean(HouseCustomerDao.class.getName());
		if(lists!=null&&lists.size()>0){
			for(Map<String, Object> o:lists){
				String name = o.get("name")==null?"":o.get("name").toString();
				String mobilephone = o.get("mobilephone")==null?"":o.get("mobilephone").toString();
				String customer_id = o.get("customer_id")==null?"":o.get("customer_id").toString();
				String relation_date = o.get("maxdate")==null?"":o.get("maxdate").toString();
				Customer customer = new Customer();
				customer.setName(name);
				
				customer.setWork_overtime(relation_date);
				
				//计算 总数
				if(customer_id!=null&&!"".equals(customer_id)){
					String sqlcount = "SELECT count(1) FROM t_relation WHERE t_relation.customer_id="+customer_id;
					SQLQuery querycount = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sqlcount);
					String count = querycount.uniqueResult().toString();
					customer.setMinor_num(Integer.parseInt(count));
					Map<String,Object> map = houseCustomerDao.selectHouseByCustomer(Long.parseLong(customer_id));
					if(map!=null&&map.size()>0){
						Object houseId = map.get("house_id");
						if(houseId!=null){
							FSP fsp = new FSP();
			                fsp.setSort(SortFactory.createSort("house_id", ISort.DESC));
			                fsp.setUserFilter(FilterFactory.getSimpleFilter("house_id",Long.parseLong(houseId.toString())));
			                List<?> lst_address = viewAddressCustomerManager.getList(fsp);
			                
			                if(lst_address != null && lst_address.size() > 0){
			                	customer.setCustomer_address((ViewAddressCustomer)lst_address.get(0));
			                    if(customer.getCustomer_address().getHouse_type() == 1){
			                    	String building_name = customer.getCustomer_address().getBuilding_name()==null?"":customer.getCustomer_address().getBuilding_name();
			                        String unit_no = customer.getCustomer_address().getUnit_no()==null?"":customer.getCustomer_address().getUnit_no();
			                    	String house_no = customer.getCustomer_address().getHouse_no()==null?"":customer.getCustomer_address().getHouse_no();
			                    	customer.setAddress(customer.getCustomer_address().getTv_name()
			                                .concat(building_name.concat("号楼"))
			                                .concat(unit_no.concat("单元"))
			                                .concat(house_no.concat("号")));
			                    }else if(customer.getCustomer_address().getHouse_type() == 0){
			                    	String house_no = customer.getCustomer_address().getHouse_no()==null?"":customer.getCustomer_address().getHouse_no();
			                    	customer.setAddress(customer.getCustomer_address().getTv_name()
			                                .concat(house_no.concat("号")));
			                    }
			                }
						}
						
					}
					
				}
				
				customer.setMobilephone(mobilephone);
				customer.setId(Long.parseLong(customer_id));
				Customer rc = customerManager.findCustomerById(customer.getId());
				customer.setRelations(rc.getRelations());
				custList.add(customer);
			}
		}
		return custList;
	}
	
	
	@Override
	public Relation queryMaxDateCount(Long customer_id){
		Relation relation = new Relation();
		//计算 总数
		if(customer_id!=null){
			String sqlcount = "SELECT count(1) FROM t_relation WHERE t_relation.customer_id="+customer_id;
			String sqlMaxDate="SELECT case when max(t_relation.relation_date) is null then '' else max(t_relation.relation_date) end  FROM t_relation WHERE t_relation.customer_id="+customer_id;
			SQLQuery querycount = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sqlcount);
			String count = querycount.uniqueResult().toString();
			relation.setTotalcount(count);
			SQLQuery querymaxdate = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sqlMaxDate);
			String maxdate = querymaxdate.uniqueResult().toString();
			relation.setLastdate(maxdate);
		}
		return relation;
	}
	
	

	@Override
	public Map<String, Object> queryRelation(List<Map<String, Object>> lst_conditions, IPage pageInfo) {
		Map<String,Object> map_result = new HashMap<String, Object>();
		StringBuilder str_fromtable = new StringBuilder();
		str_fromtable.append(" from t_relation r LEFT JOIN t_customer c ON c.id = r.customer_id LEFT JOIN tb_bizbase_user u ON u.employeeId = r.employee_no where 1=1 and r.status = 0 ");
		for(Map<String,Object> map_condition : lst_conditions){
			if("store_id".equals(map_condition.get("key")) && ValueUtil.checkValue(map_condition.get("value"))){
				str_fromtable.append(" and u.store_id = '").append(map_condition.get("value")).append("'");
			}

			if("employee_no".equals(map_condition.get("key")) && ValueUtil.checkValue(map_condition.get("value"))){
				String opr = "=";
				String value = map_condition.get("value").toString();
				if(value.contains("%")){
					opr = "like";
				}
				str_fromtable.append(" and r.employee_no ").append(opr).append(" '").append(value).append("'");
			}
			if("start_date".equals(map_condition.get("key")) && ValueUtil.checkValue(map_condition.get("value"))){
				str_fromtable.append(" and DATE_FORMAT(r.relation_date,'%Y-%m-%d') >= STR_TO_DATE('").append(map_condition.get("value")).append("','%Y-%m-%d')");
			}

			if("end_date".equals(map_condition.get("key")) && ValueUtil.checkValue(map_condition.get("value"))){
				str_fromtable.append(" and DATE_FORMAT(r.relation_date,'%Y-%m-%d') <= STR_TO_DATE('").append(map_condition.get("value")).append("','%Y-%m-%d')");
			}
		}
		String count_sql = "select count(1)".concat(str_fromtable.toString());
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(count_sql);
		pageInfo.setTotalRecords(Integer.valueOf(query.uniqueResult().toString()));

		String sql = "select \n" +
				" r.`id` AS id,\n" +
				" c.`name` AS customer_name,\n" +
				" c.mobilephone AS mobilephone,\n" +
				" u.employeeId AS employee_no,\n" +
				" u.`name` AS employee_name,\n" +
				" u.store_id AS store_id,\n" +
				" r.relation_type AS relation_type,\n" +
				" DATE_FORMAT(r.relation_date,'%Y-%m-%d') AS relation_date".concat(str_fromtable.toString()).concat(" order by id desc");
		query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setFirstResult(
				pageInfo.getRecordsPerPage()
						* (pageInfo.getCurrentPage() - 1))
				.setMaxResults(pageInfo.getRecordsPerPage()).list();
		map_result.put("pageinfo", pageInfo);
		map_result.put("header", "");
		map_result.put("data", lst_data);
		return map_result;
	}



	@Override
	public List<Map<String,Object>> findRelation_chart_crm(String employeeNo,Long area_id) {
		String sqlStr = "";
		if(area_id!=null){
			sqlStr=" AND a.id="+area_id;
		}
		if(employeeNo!=null&&!"".equals(employeeNo)){
			sqlStr = sqlStr+" AND LOWER(a.employee_a_no) = LOWER('"+employeeNo+"')";
		}
		String findSql = "select count(1) as total,createtime from (SELECT DATE_FORMAT(d.relation_date,'%Y-%m') as createtime,d.employee_no,d.customer_id FROM t_relation AS d "
				+ " INNER JOIN t_customer AS e ON d.customer_id = e.id AND (d.customer_type IS NULL OR d.customer_type!=1) "
				+ " INNER  JOIN (select thc1.* from t_house_customer thc1 inner join (select max(id) as id from t_house_customer group by customer_id) as thc2 on thc1.id = thc2.id) AS f ON e.id = f.customer_id INNER JOIN t_house AS g ON f.house_id=g.id "
				+" inner join  ("
				+" SELECT b.tin_village_id FROM t_area AS a INNER JOIN t_area_info AS b ON  a.id = b.area_id AND b.tin_village_id IS NOT NULL AND a.status=0 "
				+sqlStr
				+" UNION "
				+" SELECT c.id AS tin_village_id FROM t_area AS a INNER JOIN t_area_info AS b ON  a.id = b.area_id AND b.tin_village_id IS  NULL AND a.status=0 "
				+sqlStr
				+" INNER JOIN t_tiny_village as c ON b.village_id = c.village_id "
				+" )  AS h ON g.tinyvillage_id =h.tin_village_id  AND (DATE_FORMAT(d.relation_date,'%Y-%m')=DATE_FORMAT(CURDATE(),'%Y-%m') "
				+ " OR DATE_FORMAT(d.relation_date,'%Y-%m')=DATE_FORMAT(DATE_ADD(CURDATE(),INTERVAL -1 MONTH),'%Y-%m') "
				+ " OR  DATE_FORMAT(d.relation_date,'%Y-%m')=DATE_FORMAT(DATE_ADD(CURDATE(),INTERVAL -2 MONTH),'%Y-%m') "
				+ " OR  DATE_FORMAT(d.relation_date,'%Y-%m')=DATE_FORMAT(DATE_ADD(CURDATE(),INTERVAL -3 MONTH),'%Y-%m')) "
				+ " where d.status=0 GROUP BY DATE_FORMAT(d.relation_date, '%Y-%m') ,d.customer_id,d.employee_no) e GROUP BY createtime ";
		
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(findSql);

		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		
		return lst_data;
	}



	@Override
	public Map<String, Object> findRelation_allList_crm(PageInfo pageInfo, String employeeNo,Long area_id) {
		Map<String,Object> map_result = new HashMap<String, Object>();
		String sqlStr = "";
		if(area_id!=null){
			sqlStr = " AND a.id ="+area_id;
		}
		if(employeeNo!=null&&!"".equals(employeeNo)){
			sqlStr = sqlStr+" AND LOWER(a.employee_a_no) = LOWER('"+employeeNo+"')";
		}
		String findSql = "SELECT e.name as customerName,DATE_FORMAT(d.relation_date,'%Y年%m月%d日') AS createtime,'个人拜访' AS relationModel,d.relation_type,d.employee_name,d.employee_no,d.customer_id,e.mobilephone,f.house_id,d.create_time,d.id as relation_id ,d.status FROM t_relation AS d INNER JOIN t_customer AS e ON  d.customer_id = e.id AND (d.customer_type IS NULL OR d.customer_type!=1) "
				        +" INNER  JOIN (select thc1.* from t_house_customer thc1 inner join (select max(id) as id from t_house_customer group by customer_id) as thc2 on thc1.id = thc2.id) AS f ON e.id = f.customer_id INNER JOIN t_house AS g ON f.house_id=g.id "
						+" INNER JOIN  ("
						+" SELECT b.tin_village_id FROM t_area AS a INNER JOIN t_area_info AS b ON  a.id = b.area_id AND b.tin_village_id IS NOT NULL AND a.status=0 "
						+sqlStr
						+" UNION "
						+" SELECT c.id AS tin_village_id FROM t_area AS a INNER JOIN t_area_info AS b ON  a.id = b.area_id AND b.tin_village_id IS  NULL AND a.status=0 "
						+sqlStr
						+" INNER JOIN t_tiny_village as c ON b.village_id = c.village_id "
						+" ) AS h ON g.tinyvillage_id =h.tin_village_id   where d.status =0 ORDER BY d.id desc ";
		String countSql = "select count(1) from ( "+findSql+" ) a";
		SQLQuery queryCount = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(countSql.toString());
		pageInfo.setTotalRecords(Integer.valueOf(queryCount.uniqueResult().toString()));
		
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(findSql);
		
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setFirstResult(
				pageInfo.getRecordsPerPage()
						* (pageInfo.getCurrentPage() - 1))
				.setMaxResults(pageInfo.getRecordsPerPage()).list();
		Integer total_pages = (pageInfo.getTotalRecords()-1)/pageInfo.getRecordsPerPage()+1;
		map_result.put("pageinfo", pageInfo);
		map_result.put("header", "");
		map_result.put("data", lst_data);
		map_result.put("total_pages", total_pages);
		return map_result;
	}



	
	@Override
	public Map<String, Object> findrelation_employee_crm(PageInfo pageInfo, String employeeNo) {
		Map<String,Object> map_result = new HashMap<String, Object>();
		String findSql = "SELECT b.name,b.id,b.mobilephone,c.house_id,DATE_FORMAT(a.relation_date,'%Y-%m-%d') as createtime,(case when a.customer_type=1 then '商户拜访' ELSE '个人拜访' END) as customer_type,a.relation_type,a.employee_no,a.status,a.id as relation_id FROM `t_relation` a LEFT JOIN t_customer b on a.customer_id = b.id  LEFT JOIN t_house_customer c on b.id = c.customer_id where a.employee_no='"+employeeNo+"' and a.status=0 order by a.id desc";
		String countSql = "select count(1) from ( "+findSql+" ) a";
		SQLQuery queryCount = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(countSql.toString());
		pageInfo.setTotalRecords(Integer.valueOf(queryCount.uniqueResult().toString()));
		
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(findSql);
		
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setFirstResult(
				pageInfo.getRecordsPerPage()
						* (pageInfo.getCurrentPage() - 1))
				.setMaxResults(pageInfo.getRecordsPerPage()).list();
		Integer total_pages = (pageInfo.getTotalRecords()-1)/pageInfo.getRecordsPerPage()+1;
		map_result.put("pageinfo", pageInfo);
		map_result.put("header", "");
		map_result.put("data", lst_data);
		map_result.put("total_pages", total_pages);
		return map_result;
	}



	
	@Override
	public Map<String, Object> findRelation_customer_crm(PageInfo pageInfo, Long customer_id) {
		Map<String,Object> map_result = new HashMap<String, Object>();
		String findSql = "SELECT a.id, a.employee_name,a.employee_no,a.relation_type,a.customer_type,DATE_FORMAT(a.relation_date,'%Y年%m月%d日') AS relationtime,a.relation_rcv,a.relation_reason,a.status FROM `t_relation` a where a.customer_id="+customer_id+" and a.status=0 order by a.id  desc";
				
		String countSql = "select count(1) from ( "+findSql+" ) b";
		SQLQuery queryCount = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(countSql.toString());
		pageInfo.setTotalRecords(Integer.valueOf(queryCount.uniqueResult().toString()));
		
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(findSql);
		
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setFirstResult(
				pageInfo.getRecordsPerPage()
						* (pageInfo.getCurrentPage() - 1))
				.setMaxResults(pageInfo.getRecordsPerPage()).list();
		Integer total_pages = (pageInfo.getTotalRecords()-1)/pageInfo.getRecordsPerPage()+1;
		
		for(int i=0;i<lst_data.size();i++){
			Long id = Long.parseLong(lst_data.get(i).get("id").toString());
			String subSql="select a.content,a.relation_id,b.dicttext as optiontext,c.dicttext as resourcetext from t_relation_content a left join dict_relation_content_option_resource b on a.option_code=b.dictcode left join dict_relation_content_resource c on a.content_code = c.dictcode  where a.relation_id="+id;
			query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(subSql);
			List<Map<String, Object>> sub_lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			
			lst_data.get(i).put("relation_content", sub_lst_data);
		}
		
		map_result.put("pageinfo", pageInfo);
		map_result.put("header", "");
		map_result.put("data", lst_data);
		map_result.put("total_pages", total_pages);
		return map_result;
	}



	
	@Override
	public Map<String, Object> findRelation_area_employee_app(PageInfo pageInfo, String employeeNo,Long area_id) {
		Map<String,Object> map_result = new HashMap<String, Object>();
		String sqlStr = "";
		if(employeeNo!=null&&!"".equals(employeeNo)){
			sqlStr = " AND LOWER(a.employee_a_no) = LOWER('"+employeeNo+"') ";
		}
		
		if(area_id!=null){
			sqlStr=sqlStr+" AND a.id="+area_id;
		}
		String findSql = "SELECT e.name as customerName,e.address,DATE_FORMAT(d.relation_date,'%Y-%m-%d') AS relationDate,'个人拜访' AS relationModel,d.relation_type,d.employee_name,d.employee_no,d.customer_id,e.mobilephone,f.house_id,d.create_time,d.id as relation_id,d.status  FROM t_relation AS d INNER JOIN t_customer AS e ON d.customer_id = e.id AND (d.customer_type IS NULL OR d.customer_type!=1) "
				        +" INNER  JOIN (select thc1.* from t_house_customer thc1 inner join (select max(id) as id from t_house_customer group by customer_id) as thc2 on thc1.id = thc2.id) AS f ON e.id = f.customer_id INNER JOIN t_house AS g ON f.house_id=g.id "
						+" inner join  ("
						+" SELECT b.tin_village_id FROM t_area AS a INNER JOIN t_area_info AS b ON  a.id = b.area_id AND a.status=0  AND b.tin_village_id IS NOT NULL"
						+sqlStr
						+" UNION "
						+" SELECT c.id AS tin_village_id FROM t_area AS a INNER JOIN t_area_info AS b ON  a.id = b.area_id AND a.status=0  AND b.tin_village_id IS  NULL"
						+sqlStr
						+" INNER JOIN t_tiny_village as c ON b.village_id = c.village_id "
						+" )  AS h ON g.tinyvillage_id =h.tin_village_id where d.status=0  ORDER BY d.id desc ";
		String countSql = "select count(1) from ( "+findSql+" ) a";
		SQLQuery queryCount = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(countSql.toString());
		pageInfo.setTotalRecords(Integer.valueOf(queryCount.uniqueResult().toString()));
		
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(findSql);
		
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setFirstResult(
				pageInfo.getRecordsPerPage()
						* (pageInfo.getCurrentPage() - 1))
				.setMaxResults(pageInfo.getRecordsPerPage()).list();
		Integer total_pages = (pageInfo.getTotalRecords()-1)/pageInfo.getRecordsPerPage()+1;
		map_result.put("pageinfo", pageInfo);
		map_result.put("header", "");
		map_result.put("data", lst_data);
		map_result.put("total_pages", total_pages);
		return map_result;
	}



	
	@Override
	public Map<String, Object> findRelation_customer_crm_app(PageInfo pageInfo, Long customer_id) {
		Map<String,Object> map_result = new HashMap<String, Object>();
		String findSql = "SELECT a.id, a.employee_name,a.employee_no,a.relation_type,a.customer_type,a.relation_date,a.relation_rcv,a.relation_reason,a.status FROM `t_relation` a where a.customer_id="+customer_id+" and a.status=0 order by a.id  desc";
				
		String countSql = "select count(1) from ( "+findSql+" ) b";
		SQLQuery queryCount = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(countSql.toString());
		pageInfo.setTotalRecords(Integer.valueOf(queryCount.uniqueResult().toString()));
		
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(findSql);
		
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setFirstResult(
				pageInfo.getRecordsPerPage()
						* (pageInfo.getCurrentPage() - 1))
				.setMaxResults(pageInfo.getRecordsPerPage()).list();
		Integer total_pages = (pageInfo.getTotalRecords()-1)/pageInfo.getRecordsPerPage()+1;
		OrderDao orderDao = (OrderDao) SpringHelper.getBean(OrderDao.class.getName());
		for(int i=0;i<lst_data.size();i++){
			Long id = Long.parseLong(lst_data.get(i).get("id").toString());
			String subSql="select a.content,a.relation_id,b.dicttext as optiontext,c.dicttext as resourcetext from t_relation_content a left join dict_relation_content_option_resource b on a.option_code=b.dictcode left join dict_relation_content_resource c on a.content_code = c.dictcode  where a.relation_id="+id;
			query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(subSql);
			List<Map<String, Object>> sub_lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			
			lst_data.get(i).put("relation_content", sub_lst_data);
			lst_data.get(i).put("employee_pic", orderDao.getEmployeeByEmployeeIdOfGemini(lst_data.get(i).get("employee_no").toString()));
		}
		
		map_result.put("pageinfo", pageInfo);
		map_result.put("header", "");
		map_result.put("data", lst_data);
		map_result.put("total_pages", total_pages);
		return map_result;
	}



	
	@Override
	public List<Map<String, Object>> getRelationOfYear(Long storeId) {
		String sql="select COUNT(1) as total ,d.relation_date from ( "+
				" SELECT DATE_FORMAT(relation_date, '%c月') AS relation_date,b.employee_no,b.customer_id "+
				" FROM tb_bizbase_user AS a "+
				" INNER JOIN t_relation AS b ON a.employeeId = b.employee_no "+
				" AND a.disabledFlag = 1"+
				" AND a. NAME NOT LIKE '%测试%'"+
				" AND ( a.pk_usergroup = 3224 OR a.employeeId IN ( "+
				" SELECT employee_no FROM t_humanresources  WHERE	zw = '副店长' AND remark = '国安侠' AND store_id = "+storeId+"))"+
				" AND a.store_id = "+storeId+
				" AND YEAR (relation_date) = YEAR (CURDATE())"+
				" AND b. STATUS = 0"+
				" GROUP BY b.employee_no,b.customer_id,DATE_FORMAT(relation_date, '%Y-%m')) d"+
				" group by d.relation_date";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}




	@Override
	public List<Map<String, Object>> getRelationOfArea(Long storeId,String query_date) {
		String sql="SELECT store_area.id AS areaId,store_area.name AS areaName ,SUM(CASE WHEN r.customer_id is null then 0 ELSE 1 END) AS total FROM "+
				"(SELECT b.tin_village_id,a.name,a.id FROM t_area AS a INNER JOIN t_area_info AS b ON a.id = b.area_id "+
				"AND a.store_id= "+storeId+
				" AND b.tin_village_id IS NOT NULL AND a.status=0 "+
				"UNION "+
				"SELECT c.id AS tin_village_id,a.name,a.id FROM t_area AS a "+
				"INNER JOIN t_area_info AS b ON a.id = b.area_id "+
				"AND a.store_id="+storeId+
				" AND b.tin_village_id IS NULL AND a.status=0 "+
				"INNER JOIN t_tiny_village AS c ON b.village_id = c.village_id) AS store_area "+
				"LEFT JOIN t_house as h ON store_area.tin_village_id = h.tinyvillage_id "+
				"LEFT JOIN (select thc1.* from t_house_customer thc1 inner join (select max(id) as id from t_house_customer group by customer_id) as thc2 on thc1.id = thc2.id)  AS hc ON hc.house_id = h.id "+
				"LEFT JOIN  (select r.employee_no,r.customer_id  from t_relation r where   (r.customer_type IS NULL OR r.customer_type!=1) AND DATE_FORMAT(r.relation_date,'%Y-%m')=DATE_FORMAT(CURDATE(),'%Y-%m') and r.status=0 GROUP BY r.employee_no,customer_id) AS r ON r.customer_id =  hc.customer_id  "+
				"GROUP BY areaId,areaName order by areaId";
		
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}



	
	@Override
	public List<Map<String, Object>> getRelationOfEmployee(Long storeId,String query_date) {
		String sql = "select sum(case when d.customer_id is null then 0 else 1 end) total ,d.employeeId,d.name from "+
				 " (select u.employeeId,u.name,r.customer_id from (select * from tb_bizbase_user as bu "+
				 " where bu.store_id="+storeId+" and bu.disabledFlag = 1 and bu.name not like '%测试%' and (bu.pk_usergroup=3224 or  bu.employeeId IN (select employee_no from t_humanresources where zw ='副店长' and remark='国安侠' and store_id="+storeId+"))) as u "+
				 " LEFT JOIN t_relation AS r on u.employeeId = r.employee_no AND r.status=0 AND DATE_FORMAT(r.relation_date,'%Y-%m')=DATE_FORMAT(CURDATE(),'%Y-%m')"+
				 " GROUP BY u.employeeId,r.customer_id) d GROUP BY d.employeeId ";

		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}



	
	@Override
	public List<Map<String, Object>> getRelationOfEmployee_month(String employeeNo,Long area_id) {
		String sqlStr = "";
		if(employeeNo!=null&&!"".equals(employeeNo)){
			sqlStr = " AND LOWER(a.employee_a_no) = LOWER('"+employeeNo+"') ";
		}
		
		if(area_id!=null){
			sqlStr=sqlStr+" AND a.id="+area_id;
		}
		String sql = "select count(1) as total,relationDate from (select DATE_FORMAT(r.relation_date,'%Y-%m') as relationDate,r.customer_id from t_relation as r  "
				+" INNER JOIN t_customer AS e ON r.customer_id = e.id AND (r.customer_type IS NULL OR r.customer_type!=1) "
				+ " INNER  JOIN (select thc1.* from t_house_customer thc1 inner join (select max(id) as id from t_house_customer group by customer_id) as thc2 on thc1.id = thc2.id) AS f ON e.id = f.customer_id INNER JOIN t_house AS g ON f.house_id=g.id "
				+" inner join  ("
				+" SELECT b.tin_village_id FROM t_area AS a INNER JOIN t_area_info AS b ON  a.id = b.area_id AND a.status=0 AND b.tin_village_id IS NOT NULL"
				+sqlStr
				+" UNION "
				+" SELECT c.id AS tin_village_id FROM t_area AS a INNER JOIN t_area_info AS b ON  a.id = b.area_id AND a.status=0  AND b.tin_village_id IS  NULL"
				+sqlStr
				+" INNER JOIN t_tiny_village as c ON b.village_id = c.village_id "
				+" ) AS h on h.tin_village_id = g.tinyvillage_id "
				+" AND (DATE_FORMAT(r.relation_date,'%Y-%m')=DATE_FORMAT(CURDATE(),'%Y-%m')"
				+" OR DATE_FORMAT(r.relation_date,'%Y-%m')=DATE_FORMAT(DATE_ADD(CURDATE(),INTERVAL -1 MONTH),'%Y-%m')" 
				+" OR  DATE_FORMAT(r.relation_date,'%Y-%m')=DATE_FORMAT(DATE_ADD(CURDATE(),INTERVAL -2 MONTH),'%Y-%m')" 
				+" OR  DATE_FORMAT(r.relation_date,'%Y-%m')=DATE_FORMAT(DATE_ADD(CURDATE(),INTERVAL -3 MONTH),'%Y-%m')"
				+" OR  DATE_FORMAT(r.relation_date,'%Y-%m')=DATE_FORMAT(DATE_ADD(CURDATE(),INTERVAL -4 MONTH),'%Y-%m'))"
				+ " where  r.status=0 group by relationDate ,r.customer_id,r.employee_no) d GROUP BY relationDate order by relationDate";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}
	
	
	public Map<String, Object> findrelation_employee_crm_app(PageInfo pageInfo, String employeeNo) {
		Map<String,Object> map_result = new HashMap<String, Object>();
		String findSql = "SELECT a.employee_name,b.name as customerName,b.id as customer_id,b.mobilephone,b.address,c.house_id,DATE_FORMAT(a.relation_date,'%Y-%m-%d') as createtime,(case when a.customer_type=1 then '商户拜访' ELSE '个人拜访' END) as customer_type,a.relation_type,a.employee_no,a.status,a.id as relation_id FROM `t_relation` a LEFT JOIN t_customer b on a.customer_id = b.id  LEFT JOIN t_house_customer c on b.id = c.customer_id where a.employee_no='"+employeeNo+"' and a.status=0 order by a.id desc";
		String countSql = "select count(1) from ( "+findSql+" ) a";
		SQLQuery queryCount = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(countSql.toString());
		pageInfo.setTotalRecords(Integer.valueOf(queryCount.uniqueResult().toString()));
		
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(findSql);
		
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setFirstResult(
				pageInfo.getRecordsPerPage()
						* (pageInfo.getCurrentPage() - 1))
				.setMaxResults(pageInfo.getRecordsPerPage()).list();
		Integer total_pages = (pageInfo.getTotalRecords()-1)/pageInfo.getRecordsPerPage()+1;
		map_result.put("pageinfo", pageInfo);
		map_result.put("header", "");
		map_result.put("data", lst_data);
		map_result.put("total_pages", total_pages);
		return map_result;
	}



	
	@Override
	public List<Map<String, Object>> findRelationOfEmployee_byFiveMonth(String employeeNo) {
		String sql = "select count(1) as total,relationDate from ( SELECT DATE_FORMAT(DATE_ADD(CURDATE(),INTERVAL -4 MONTH),'%c月') as relationDate,customer_id FROM `t_relation` where DATE_FORMAT(relation_date,'%Y-%m')=DATE_FORMAT(DATE_ADD(CURDATE(),INTERVAL -4 MONTH),'%Y-%m') and status=0 and employee_no='"+employeeNo+"' GROUP BY customer_id "+
				 " UNION "+
				 " SELECT DATE_FORMAT(DATE_ADD(CURDATE(),INTERVAL -3 MONTH),'%c月') as relationDate,customer_id FROM `t_relation` where DATE_FORMAT(relation_date,'%Y-%m')=DATE_FORMAT(DATE_ADD(CURDATE(),INTERVAL -3 MONTH),'%Y-%m') and status=0 and employee_no='"+employeeNo+"' GROUP BY customer_id "+
				 " UNION "+
				 " SELECT DATE_FORMAT(DATE_ADD(CURDATE(),INTERVAL -2 MONTH),'%c月') as relationDate,customer_id FROM `t_relation` where DATE_FORMAT(relation_date,'%Y-%m')=DATE_FORMAT(DATE_ADD(CURDATE(),INTERVAL -2 MONTH),'%Y-%m') and status=0 and employee_no='"+employeeNo+"' GROUP BY customer_id "+
				 " UNION "+
				 " SELECT DATE_FORMAT(DATE_ADD(CURDATE(),INTERVAL -1 MONTH),'%c月') as relationDate,customer_id FROM `t_relation` where DATE_FORMAT(relation_date,'%Y-%m')=DATE_FORMAT(DATE_ADD(CURDATE(),INTERVAL -1 MONTH),'%Y-%m') and status=0 and employee_no='"+employeeNo+"' GROUP BY customer_id "+
				 " UNION "+
				 " SELECT DATE_FORMAT(CURDATE(),'%c月') as relationDate,customer_id FROM `t_relation` where DATE_FORMAT(relation_date,'%Y-%m')=DATE_FORMAT(CURDATE(),'%Y-%m') and status=0 and employee_no='"+employeeNo+"' GROUP BY customer_id ) a GROUP BY a.relationDate";

		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;

	}
	
	
	@Override
	public List<Map<String, Object>> selectAllEmployeeOfStore(Long storeId) {
		String sql = "select bu.employeeId,bu.name from tb_bizbase_user as bu where bu.store_id="+storeId+" and bu.disabledFlag = 1 and bu.name not like '%测试%'  and (bu.pk_usergroup=3224 or  bu.employeeId IN (select employee_no from t_humanresources where zw ='副店长' and remark='国安侠' and store_id="+storeId+"))";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}



	
	@Override
	public Integer getRelationCount(String employeeNo) {
		String sql="select count(1) as total  from (SELECT count(1),employee_no,customer_id,DATE_FORMAT(relation_date,'%Y-%m') as r_month FROM `t_relation` where status=0 GROUP BY employee_no,customer_id,DATE_FORMAT(relation_date,'%Y-%m') HAVING employee_no='"+employeeNo+"') a;";
		SQLQuery queryCount = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		return Integer.valueOf(queryCount.uniqueResult().toString());
	}



	
	@Override
	public Integer getRelationCount(Long storeId) {
		String sql="select r.employee_no,r.customer_id,r.relation_date from t_relation r"+ 
					" INNER JOIN "+
					" (select * from tb_bizbase_user as bu where bu.store_id="+storeId+" and bu.disabledFlag = 1 and bu.name not like '%测试%' and (bu.pk_usergroup=3224 or  bu.employeeId IN (select employee_no from t_humanresources where zw ='副店长' and remark='国安侠' and store_id="+storeId+"))) lu"+
					" on r.employee_no = lu.employeeId and r.status=0"+
 					" GROUP BY r.employee_no,r.customer_id,DATE_FORMAT(r.relation_date,'%Y-%m')";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data==null?0:lst_data.size();
	}




	@Override
	public List<Map<String, Object>> getRelationOfYear_CSZJ_QYJL(String employeeId,Long cityId,String role) {
		
		String whereStr= "";
		if("CSZJ".equals(role)){//城市总监
			whereStr="select store_id from t_store t  inner join  (select tdc.id,tdc.cityname from t_general a"+  
					"   INNER JOIN t_general_city_reference b on a.employee_no='"+employeeId+"' and a.id = b.gmid and b.ctid="+cityId+
					"   INNER JOIN t_dist_citycode tdc on b.ctid = tdc.id ) t1"+
					"	 on t.city_name  = t1.cityname";
		}else if("QYJL".equals(role)){//区域经理
			whereStr= "select store_id,name from t_store where rmid = (select id from tb_bizbase_user where employeeId='"+employeeId+"')";
		}
		String sql="select COUNT(1) as total ,d.relation_date from ( "+
				" SELECT DATE_FORMAT(relation_date, '%c月') AS relation_date,b.employee_no,b.customer_id "+
				" FROM tb_bizbase_user AS a "+
				" INNER JOIN ("+whereStr+") c"+
				" ON a.store_id = c.store_id  AND a.disabledFlag = 1 AND a. NAME NOT LIKE '%测试%'"+
				" AND (a.pk_usergroup = 3224 OR a.employeeId IN ( SELECT employee_no FROM t_humanresources t1 INNER JOIN "+
				" ("+whereStr+") t2"+
				" ON t1.store_id=t2.store_id and t1.humanstatus=1 AND t1.zw = '副店长' AND t1.remark = '国安侠'))"+
				" INNER JOIN t_relation AS b ON a.employeeId = b.employee_no "+
				" AND YEAR (relation_date) = YEAR (CURDATE())"+
				" AND b. STATUS = 0"+
				" GROUP BY b.employee_no,b.customer_id,DATE_FORMAT(b.relation_date, '%Y-%m')) d"+
				" group by d.relation_date";
		
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}


	@Override
	public List<Map<String, Object>> getRelationOfStore_CSZJ_QYJL(String employeeId,Long cityId,String role) {
		String whereStr= "";
		if("CSZJ".equals(role)){//城市总监
			whereStr="select t.store_id,t.name,t.skid,tbu.name as employeeName from t_store t  inner join  (select tdc.id,tdc.cityname from t_general a"+  
					"   INNER JOIN t_general_city_reference b on a.employee_no='"+employeeId+"' and a.id = b.gmid and b.ctid="+cityId+
					"   INNER JOIN t_dist_citycode tdc on b.ctid = tdc.id ) t1"+
					"	on t.city_name  = t1.cityname"+
					" 	INNER JOIN tb_bizbase_user as tbu on t.skid = tbu.id";
		}else if("QYJL".equals(role)){//区域经理
			whereStr= "select t.store_id,t.name,t.skid,tbu.name as employeeName from t_store t left  join  tb_bizbase_user as tbu on t.skid = tbu.id  where rmid = (select id from tb_bizbase_user where employeeId='"+employeeId+"')";
		} 
		String sql="SELECT ifnull(temp.total,0) as total,ts.store_id,ts.name,ts.employeeName,ts.skid FROM  (select COUNT(1) as total ,d.store_id from ( "+
				" SELECT b.employee_no,b.customer_id,a.store_id"+
				" FROM tb_bizbase_user AS a "+
				" INNER JOIN ("+whereStr+") c"+
				" ON a.store_id = c.store_id  AND a.disabledFlag = 1 AND a. NAME NOT LIKE '%测试%'"+
				" AND (a.pk_usergroup = 3224 OR a.employeeId IN ( SELECT employee_no FROM t_humanresources t1 INNER JOIN "+
				" ("+whereStr+") t2"+
				" ON t1.store_id=t2.store_id and t1.humanstatus=1 AND t1.zw = '副店长' AND t1.remark = '国安侠'))"+
				" INNER JOIN (select * from t_relation where  status=0 and DATE_FORMAT(relation_date,'%Y-%m')=DATE_FORMAT(CURDATE(),'%Y-%m')) AS b ON a.employeeId = b.employee_no "+
				" GROUP BY b.employee_no,b.customer_id,DATE_FORMAT(b.relation_date, '%Y-%m')) d"+
				" group by d.store_id) temp RIGHT JOIN ("+whereStr+") ts on ts.store_id = temp.store_id ORDER BY total desc limit 20";
		
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}



	
	@Override
	public List<Map<String, Object>> getRelationOfEmployee_CSZJ_QYJL(String employeeId,Long cityId,String role) {
		String whereStr= "";
		if("CSZJ".equals(role)){//城市总监
			whereStr="select store_id from t_store t  inner join  (select tdc.id,tdc.cityname from t_general a"+  
					"   INNER JOIN t_general_city_reference b on a.employee_no='"+employeeId+"' and a.id = b.gmid and b.ctid="+cityId+
					"   INNER JOIN t_dist_citycode tdc on b.ctid = tdc.id ) t1"+
					"	 on t.city_name  = t1.cityname";
		}else if("QYJL".equals(role)){//区域经理
			whereStr= "select store_id,name from t_store where rmid = (select id from tb_bizbase_user where employeeId='"+employeeId+"')";
		}
		String sql = "select sum(case when d.customer_id is null then 0 else 1 end) total ,d.employeeId,d.name from "+
				 " (select u.employeeId,u.name,r.customer_id from ("+
				 " select bu.* from tb_bizbase_user as bu "+
				 " inner join ("+whereStr+") ts"+
				 " on bu.store_id = ts.store_id "+
				 " and bu.disabledFlag = 1 and bu.name not like '%测试%' and (bu.pk_usergroup=3224 or  bu.employeeId IN ( SELECT employee_no FROM t_humanresources t1 INNER JOIN "+
				 " ("+whereStr+") t2"+
				 " ON t1.store_id=t2.store_id and t1.humanstatus=1 AND t1.zw = '副店长' AND t1.remark = '国安侠'))) as u "+
				 " LEFT JOIN t_relation AS r on u.employeeId = r.employee_no AND r.status=0 AND DATE_FORMAT(r.relation_date,'%Y-%m')=DATE_FORMAT(CURDATE(),'%Y-%m')"+
				 " GROUP BY u.employeeId,r.customer_id) d GROUP BY d.employeeId ";

		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}



	
	@Override
	public List<Map<String, Object>> getRelationOfPrevMonthOfStore(String month) {
		String sql="select COUNT(1) as amount ,d.store_id,d.storeName as store_name from ( "+
				" SELECT b.employee_no,b.customer_id,a.store_id,c.name as storeName"+
				" FROM tb_bizbase_user AS a "+
				" INNER JOIN t_store c"+
				" ON a.store_id = c.store_id  AND a.disabledFlag = 1 AND a. NAME NOT LIKE '%测试%'"+
				" AND (a.pk_usergroup = 3224 OR a.employeeId IN ( SELECT employee_no FROM t_humanresources t1 INNER JOIN "+
				" t_store t2"+
				" ON t1.store_id=t2.store_id and t1.humanstatus=1 AND t1.zw = '副店长' AND t1.remark = '国安侠'))"+
				" INNER JOIN (select * from t_relation where  status=0 and DATE_FORMAT(relation_date,'%Y-%m')='"+month+"') AS b ON a.employeeId = b.employee_no "+
				" GROUP BY b.employee_no,b.customer_id,DATE_FORMAT(b.relation_date, '%Y-%m')) d"+
				" group by d.store_id";
		
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}



	
	@Override
	public List<Map<String, Object>> getRelationOfYear_CSZJ_QYJL_before(String employeeId, Long cityId, String role) {
		String whereStr="";
		if("CSZJ".equals(role)){//城市总监
			whereStr="select t.store_id from t_store t  inner join  (select tdc.id,tdc.cityname from t_general a"+  
					"   INNER JOIN t_general_city_reference b on a.employee_no='"+employeeId+"' and a.id = b.gmid and b.ctid="+cityId+
					"   INNER JOIN t_dist_citycode tdc on b.ctid = tdc.id ) t1"+
					"	 on t.city_name  = t1.cityname";
		}else if("QYJL".equals(role)){//区域经理
			whereStr= "select store_id from t_store where rmid = (select id from tb_bizbase_user where employeeId='"+employeeId+"')";
		}
		
		String sql = "select  sum(ifnull(amount,0)) as  total,DATE_FORMAT(STR_TO_DATE(bind_date,'%Y-%m'), '%c月') AS relation_date   from ("+whereStr+") as a left join  t_before_date_relation b on a.store_id = b.store_id  group by bind_date ";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}



	
	@Override
	public List<Map<String, Object>> getRelationOfStore_CSZJ_QYJL_before(String employeeId, Long cityId, String role,String q_date) {
		String whereStr="";
		if("CSZJ".equals(role)){//城市总监
			whereStr="select t.store_id,t.name,t.skid from t_store t  inner join  (select tdc.id,tdc.cityname from t_general a"+  
					"   INNER JOIN t_general_city_reference b on a.employee_no='"+employeeId+"' and a.id = b.gmid and b.ctid="+cityId+
					"   INNER JOIN t_dist_citycode tdc on b.ctid = tdc.id ) t1"+
					"	 on t.city_name  = t1.cityname";
		}else if("QYJL".equals(role)){//区域经理
			whereStr= "select store_id,name,skid from t_store where rmid = (select id from tb_bizbase_user where employeeId='"+employeeId+"')";
		}
		
		String sql = "select  ifnull(b.amount,0) as  total,a.store_id,a.name,tbu.name as employeeName,a.skid   from ("+whereStr+") as a left join  t_before_date_relation b on a.store_id = b.store_id  and b.bind_date='"+q_date+"' left join tb_bizbase_user as tbu on a.skid = tbu.id  order by total desc limit 20";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	
	}



	
	@Override
	public Integer getAllRelationOfStore(String storeId) {
		String sql="select count(1) as total  from (select r.id  from t_relation r"+ 
				" INNER JOIN "+
				" (select * from tb_bizbase_user as bu where bu.store_id in "+storeId+" and bu.disabledFlag = 1 and bu.name not like '%测试%' and (bu.pk_usergroup=3224 or  bu.employeeId IN (select employee_no from t_humanresources where zw ='副店长' and remark='国安侠' and store_id in "+storeId+"))) lu"+
				" on r.employee_no = lu.employeeId and r.status=0"+
				" GROUP BY r.employee_no,r.customer_id,DATE_FORMAT(r.relation_date,'%Y-%m')) tt";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		Integer result = query.uniqueResult()==null?0:Integer.parseInt(query.uniqueResult().toString());
		return result;
	}
}
