/**
 * gaobaolei
 */
package com.cnpc.pms.personal.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.dynamic.entity.AbnormalOrderDto;
import com.cnpc.pms.personal.dao.DsAbnormalOrderDao;

/**
 * @author gaobaolei
 *
 */
public class DsAbnormalOrderDaoImpl extends BaseDAOHibernate implements DsAbnormalOrderDao{

	
	@Override
	public void updateOrder(Long id,String upDate) {
		String sql = "update ds_abnormal_order set status='3', updatetime ='"+upDate+"' where id="+id;
		SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession()
                .createSQLQuery(sql.toString());
		query.executeUpdate();
		
	}

	
	@Override
	public List<Map<String, Object>> queryAbnormalDown(AbnormalOrderDto abnormalOrderDto) {
		String sql = "select a.*,ifnull(b.description,'其他') as description,  case a.status when 0 then '未申诉'  when 1  then '申诉中' when 2 then '已驳回' when '3' then '已通过' end as state,ifnull(b.description,'其他') as description from ds_abnormal_down a  left join (select * from ds_abnormal_type where datatype='manual') b on a.abnortype= b.abnortype  where a.year="+abnormalOrderDto.getYear()+" and a.month="+abnormalOrderDto.getMonth();
//		if(abnormalOrderDto.getAbnortype()!=null){
//			sql=sql+" and a.abnortype ='"+abnormalOrderDto.getAbnortype()+"'";
//		}
//		
//		if(abnormalOrderDto.getOrdersn()!=null){
//			sql=sql+" and a.ordersn ='"+abnormalOrderDto.getOrdersn()+"'";
//		}
//		
//		if(abnormalOrderDto.getDatatype()!=null){
//			sql=sql+" and a.datatype ='"+abnormalOrderDto.getDatatype()+"'";
//		}
//		
//		if(abnormalOrderDto.getStoreno()!=null){
//			sql=sql+" and a.storeno ='"+abnormalOrderDto.getStoreno()+"'";
//		}
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);

		List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}


	
	@Override
	public Map<String, Object> queryBaseAbnormalOrder(AbnormalOrderDto abnormalOrderDto, PageInfo pageInfo) {
		
		String sql = "select a.*,a.abnortype as description from ds_abnormal_down a   where 1=1 ";
		
		if(abnormalOrderDto.getBeginDate()!=null&&!"".equals(abnormalOrderDto.getBeginDate())){
			sql =sql+ " and DATE_FORMAT(a.signedtime,'%Y/%m/%d') >='"+abnormalOrderDto.getBeginDate()+"'";
		}
		
		if(abnormalOrderDto.getEndDate()!=null&&!"".equals(abnormalOrderDto.getEndDate())){
			sql = sql+ " and DATE_FORMAT(a.signedtime,'%Y/%m/%d') <= '"+abnormalOrderDto.getEndDate()+"'";
		}
		
		if(abnormalOrderDto.getAbnortype()!=null&&!"".equals(abnormalOrderDto.getAbnortype())){
			sql=sql+" and a.abnortype ='"+abnormalOrderDto.getAbnortype()+"'";
		}
		
		
		if(abnormalOrderDto.getOrdersn()!=null&&!"".equals(abnormalOrderDto.getOrdersn())){
			sql=sql+" and a.ordersn ='"+abnormalOrderDto.getOrdersn()+"'";
		}
		
		if(abnormalOrderDto.getStoreno()!=null&&!"".equals(abnormalOrderDto.getStoreno())){
			sql=sql+" and a.storeno ='"+abnormalOrderDto.getStoreno()+"'";
		}
		
		if(abnormalOrderDto.getCityname()!=null&&!"".equals(abnormalOrderDto.getCityname())){
			sql=sql+" and a.cityname like '%"+abnormalOrderDto.getCityname()+"%'";
		}
		
		
		if(abnormalOrderDto.getMinPrice()!=null&&!"".equals(abnormalOrderDto.getMinPrice().toString())){
			sql=sql+" and a.tradingprice >= "+abnormalOrderDto.getMinPrice();
		}
		
		if(abnormalOrderDto.getMaxPrice()!=null&&!"".equals(abnormalOrderDto.getMaxPrice().toString())){
			sql=sql+" and a.tradingprice <= "+abnormalOrderDto.getMaxPrice();
		}
		
		if(abnormalOrderDto.getDept()!=null&&!"".equals(abnormalOrderDto.getDept())){
			sql=sql+" and a.deptname like '%"+abnormalOrderDto.getDept()+"%'";
		}
		
		if(abnormalOrderDto.getChannel()!=null&&!"".equals(abnormalOrderDto.getChannel())){
			sql=sql+" and a.channelname like '%"+abnormalOrderDto.getChannel()+"%'";
		}
		sql=sql+" order by a.status,a.updatetime asc";
		String sql_count = "SELECT COUNT(1) as total FROM ("+sql+") T";

		Query query_count = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql_count);
		Object total = query_count.uniqueResult();
		pageInfo.setTotalRecords(Integer.valueOf(total.toString()));

		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);

		List<?> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setFirstResult(
				pageInfo.getRecordsPerPage()
						* (pageInfo.getCurrentPage() - 1)).setMaxResults(pageInfo.getRecordsPerPage()).list();

		Map<String,Object> map_result = new HashMap<String,Object>();
		Integer total_pages = (pageInfo.getTotalRecords()-1)/pageInfo.getRecordsPerPage()+1;
		map_result.put("pageinfo",pageInfo);
		map_result.put("data", list);
		map_result.put("total_pages", total_pages);
		return map_result;
	}


	
	@Override
	public List<Map<String, Object>> queryBaseAbnormalOrderNoPage(AbnormalOrderDto abnormalOrderDto) {
		
		String sql = "select a.*,a.abnortype as description from ds_abnormal_down a    where 1=1 ";
		
		if(abnormalOrderDto.getBeginDate()!=null&&!"".equals(abnormalOrderDto.getBeginDate())){
			sql =sql+ " and DATE_FORMAT(a.signedtime,'%Y/%m/%d') >='"+abnormalOrderDto.getBeginDate()+"'";
		}
		
		if(abnormalOrderDto.getEndDate()!=null&&!"".equals(abnormalOrderDto.getEndDate())){
			sql = sql+ " and DATE_FORMAT(a.signedtime,'%Y/%m/%d') <= '"+abnormalOrderDto.getEndDate()+"'";
		}
		
		if(abnormalOrderDto.getAbnortype()!=null&&!"".equals(abnormalOrderDto.getAbnortype())){
			sql=sql+" and a.abnortype ='"+abnormalOrderDto.getAbnortype()+"'";
		}
		
		
		if(abnormalOrderDto.getOrdersn()!=null&&!"".equals(abnormalOrderDto.getOrdersn())){
			sql=sql+" and a.ordersn ='"+abnormalOrderDto.getOrdersn()+"'";
		}
		
		if(abnormalOrderDto.getStoreno()!=null&&!"".equals(abnormalOrderDto.getStoreno())){
			sql=sql+" and a.storeno ='"+abnormalOrderDto.getStoreno()+"'";
		}
		
		if(abnormalOrderDto.getCityname()!=null&&!"".equals(abnormalOrderDto.getCityname())){
			sql=sql+" and a.cityname like '%"+abnormalOrderDto.getCityname()+"%'";
		}
		
		
		if(abnormalOrderDto.getMinPrice()!=null&&!"".equals(abnormalOrderDto.getMinPrice().toString())){
			sql=sql+" and a.tradingprice >= "+abnormalOrderDto.getMinPrice();
		}
		
		if(abnormalOrderDto.getMaxPrice()!=null&&!"".equals(abnormalOrderDto.getMaxPrice().toString())){
			sql=sql+" and a.tradingprice <= "+abnormalOrderDto.getMaxPrice();
		}
		
		if(abnormalOrderDto.getDept()!=null&&!"".equals(abnormalOrderDto.getDept())){
			sql=sql+" and a.deptname like '%"+abnormalOrderDto.getDept()+"%'";
		}
		
		if(abnormalOrderDto.getChannel()!=null&&!"".equals(abnormalOrderDto.getChannel())){
			sql=sql+" and a.channelname like '%"+abnormalOrderDto.getChannel()+"%'";
		}
		sql=sql+" order by a.status,a.updatetime asc";
		

		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);

		List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

		return  list;
	}


	
	@Override
	public Map<String, Object> queryAbnormalOrderResult(AbnormalOrderDto abnormalOrderDto, PageInfo pageInfo) {
		String  sql="select a.cityname,a.storename, count(a.ordersn) as total,sum(b.gmv_price) as gmv from ds_abnormal_order as a left join df_mass_order_monthly b on a.ordersn = b.order_sn  where a.status!='3' ";
		
		if(abnormalOrderDto.getBeginDate()!=null&&!"".equals(abnormalOrderDto.getBeginDate())){
			sql =sql+ " and DATE_FORMAT(a.signedtime,'%Y-%m-%d') >='"+abnormalOrderDto.getBeginDate()+"'";
		}
		
		if(abnormalOrderDto.getEndDate()!=null&&!"".equals(abnormalOrderDto.getEndDate())){
			sql = sql+ " and DATE_FORMAT(a.signedtime,'%Y-%m-%d') <= '"+abnormalOrderDto.getEndDate()+"'";
		}
		
		if(abnormalOrderDto.getStoreno()!=null&&!"".equals(abnormalOrderDto.getStoreno())){
			sql=sql+" and a.storeno ='"+abnormalOrderDto.getStoreno()+"'";
		}
		
		if(abnormalOrderDto.getCityname()!=null&&!"".equals(abnormalOrderDto.getCityname())){
			sql=sql+" and a.cityname like '%"+abnormalOrderDto.getCityname()+"%'";
		}
		
		sql=sql+" group by a.storeno";
		String sql_count = "SELECT COUNT(1) as total FROM ("+sql+") T";

		Query query_count = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql_count);
		Object total = query_count.uniqueResult();
		pageInfo.setTotalRecords(Integer.valueOf(total.toString()));

		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);

		List<?> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setFirstResult(
				pageInfo.getRecordsPerPage()
						* (pageInfo.getCurrentPage() - 1)).setMaxResults(pageInfo.getRecordsPerPage()).list();

		Map<String,Object> map_result = new HashMap<String,Object>();
		Integer total_pages = (pageInfo.getTotalRecords()-1)/pageInfo.getRecordsPerPage()+1;
		map_result.put("pageinfo",pageInfo);
		map_result.put("data", list);
		map_result.put("total_pages", total_pages);
		return map_result;
	}


	
	@Override
	public List<Map<String, Object>> queryAbnormalOrderResult(AbnormalOrderDto abnormalOrderDto) {
		
		String  sql="select cityname,storeno,storename, count(ordersn) as total,sum(tradingprice) as gmv from ds_abnormal_order as a where a.status!='3' ";
		
		if(abnormalOrderDto.getBeginDate()!=null&&!"".equals(abnormalOrderDto.getBeginDate())){
			sql =sql+ " and DATE_FORMAT(a.signedtime,'%Y-%m-%d') >='"+abnormalOrderDto.getBeginDate()+"'";
		}
		
		if(abnormalOrderDto.getEndDate()!=null&&!"".equals(abnormalOrderDto.getEndDate())){
			sql = sql+ " and DATE_FORMAT(a.signedtime,'%Y-%m-%d') <= '"+abnormalOrderDto.getEndDate()+"'";
		}
		
		if(abnormalOrderDto.getStoreno()!=null&&!"".equals(abnormalOrderDto.getStoreno())){
			sql=sql+" and a.storeno ='"+abnormalOrderDto.getStoreno()+"'";
		}
		
		if(abnormalOrderDto.getCityname()!=null&&!"".equals(abnormalOrderDto.getCityname())){
			sql=sql+" and a.cityname like '%"+abnormalOrderDto.getCityname()+"%'";
		}
		
		sql=sql+" group by storeno";
		
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);

		List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

		return  list;
	}
	
	
	
	
		//当月累计营业额
		@Override
		public Map<String, Object> queryOrderAmountByMonth(int year,int month,String dep_name){
			String sql = "select sum(ds_storetrade_channel.order_amount) as order_amount from ds_storetrade_channel WHERE year = "+year+" AND month = "+month+" and dep_name like '%"+dep_name+"%'";
			SQLQuery query = getHibernateTemplate().getSessionFactory()
					.getCurrentSession().createSQLQuery(sql.toString());
			
			List<Map<String, Object>> lst_data = null;
		     try{
		         lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		     }catch (Exception e){
		         e.printStackTrace();
		     }
			if(lst_data!=null){
				return lst_data.get(0);
			}
			return null;
		}
		

		
		@Override
		public List<Map<String, Object>> queryOrderAmountByGroupCity(int year,int month,String dep_name){
			String sql = "SELECT city_name,sum(order_amount) as totalamount FROM ds_storetrade_channel WHERE dep_name like '%"+dep_name+"%' AND year= "+year+" AND month = "+month+" GROUP BY city_name";
			SQLQuery query = getHibernateTemplate().getSessionFactory()
					.getCurrentSession().createSQLQuery(sql.toString());
			
			List<Map<String, Object>> lst_data = null;
		     try{
		         lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		     }catch (Exception e){
		         e.printStackTrace();
		     }
			if(lst_data!=null){
				return lst_data;
			}
			return null;
		}
		
		
		
		@Override
		public List<Map<String, Object>> queryOrderAmountByChannel(int year,int month,String dep_name){
			String sql = "SELECT channel_name,sum(order_amount) as totalamount FROM ds_storetrade_channel WHERE dep_name like '%"+dep_name+"%' AND year= "+year+" AND month = "+month+" GROUP BY channel_name";
			SQLQuery query = getHibernateTemplate().getSessionFactory()
					.getCurrentSession().createSQLQuery(sql.toString());
			
			List<Map<String, Object>> lst_data = null;
		     try{
		         lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		     }catch (Exception e){
		         e.printStackTrace();
		     }
			if(lst_data!=null){
				return lst_data;
			}
			return null;
		}
		
		
		/**
		 * 查询当月的门店拉新数据 
		 * @param year  年
		 * @param month 月
		 * @param storeno 门店编号
		 * @return
		 */
		@Override
		public Map<String, Object> querynewaddcusbystoreno(int year,int month,String storeno){
			String sql = "SELECT ds_newaddcus.id,ds_newaddcus.storeno,ds_newaddcus.store_name,ds_newaddcus.new_count,ds_newaddcus.year,ds_newaddcus.month FROM ds_newaddcus WHERE year = "+year+" AND month = "+month+" AND storeno='"+storeno+"'";
			SQLQuery query = getHibernateTemplate().getSessionFactory()
					.getCurrentSession().createSQLQuery(sql.toString());
			
			List<Map<String, Object>> lst_data = null;
		     try{
		         lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		     }catch (Exception e){
		         e.printStackTrace();
		     }
			if(lst_data!=null&&lst_data.size()>0){
				return lst_data.get(0);
			}
			return null;
		}
		@Override
		public Map<String, Object> queryorderamountbycareergroup(int year,int month,String storeno,String careergroup){
			String sql = "SELECT sum(order_amount) as order_amount_career from ds_storetrade_channel WHERE storeno = '"+storeno+"' AND year = "+year+" AND month = "+month+" AND dep_name LIKE '%"+careergroup+"%'";
			SQLQuery query = getHibernateTemplate().getSessionFactory()
					.getCurrentSession().createSQLQuery(sql.toString());
			
			List<Map<String, Object>> lst_data = null;
		     try{
		         lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		     }catch (Exception e){
		         e.printStackTrace();
		     }
			if(lst_data!=null&&lst_data.size()>0){
				return lst_data.get(0);
			}
			return null;
		}
		@Override
		public Map<String, Object> queryorderamountbystoreno(int year,int month,String storeno){
			String sql = "SELECT sum(order_amount) as order_amount from ds_storetrade_channel WHERE storeno = '"+storeno+"' AND year = "+year+" AND month = "+month+"";
			SQLQuery query = getHibernateTemplate().getSessionFactory()
					.getCurrentSession().createSQLQuery(sql.toString());
			
			List<Map<String, Object>> lst_data = null;
		     try{
		         lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		     }catch (Exception e){
		         e.printStackTrace();
		     }
			if(lst_data!=null&&lst_data.size()>0){
				return lst_data.get(0);
			}
			return null;
		}
		
		
		
		
		
		
}
