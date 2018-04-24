package com.cnpc.pms.personal.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.dynamic.entity.EshopPurchaseDto;
import com.cnpc.pms.personal.dao.EshopPurchaseDao;

/**
 * @Function：清洗出的企业购数据Day实现
 * @author：chenchuang
 * @date:2017-12-25 下午5:38:51
 *
 * @version V1.0
 */
public class EshopPurchaseDaoImpl extends BaseDAOHibernate implements EshopPurchaseDao{

	@Override
	public Map<String, Object> queryBaseEshopPurchase(EshopPurchaseDto shopPurchaseDto, PageInfo pageInfo) {
		String sql = "select CONCAT(a.eshop_id,'') as eshop_id,a.eshop_code,a.eshop_name,a.eshop_model from df_eshop_purchase a where 1=1 ";
		
		if(StringUtils.isNotEmpty(shopPurchaseDto.getEshopId())){
			sql=sql+" and a.eshop_id ='"+shopPurchaseDto.getEshopId()+"'";
		}
		
		if(StringUtils.isNotEmpty(shopPurchaseDto.getEshopCode())){
			sql=sql+" and a.eshop_code ='"+shopPurchaseDto.getEshopCode()+"'";
		}
		
		if(StringUtils.isNotEmpty(shopPurchaseDto.getEshopModel())){
			sql=sql+" and a.eshop_model ='"+shopPurchaseDto.getEshopModel()+"'";
		}
		
		if(StringUtils.isNotEmpty(shopPurchaseDto.getEshopName())){
			sql=sql+" and a.eshop_name like '%"+shopPurchaseDto.getEshopName().trim()+"%'";
		}
		sql=sql+" order by a.create_time desc, a.eshop_code asc";
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String,Object>> queryAllEshopPurchases(){
		String sql = "select CONCAT(eshop_id,'') as eshop_id from df_eshop_purchase where 1=1 ";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		List<Map<String,Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}
	
	@Override
	public void deleteEshopPurchase(String eshopId){
		String sql = "delete from df_eshop_purchase where eshop_id =:eshopId";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		query.setParameter("eshopId", eshopId);
		query.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryBusinessModelType(){
		String sql="select distinct(eshop_model) as eshop_model from df_eshop_purchase";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return lst_data;
	}
	
	@Override
	public void saveEshopPurchase(String models){
		String[] modelArr = models.split("_"); 
		String sql = "insert into df_eshop_purchase(eshop_id,eshop_code,eshop_name,eshop_model) " +
				"values('"+modelArr[0]+"','"+modelArr[1]+"','"+modelArr[2]+"','"+modelArr[3]+"')";
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		query.executeUpdate();
	}

}
