package com.cnpc.pms.personal.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.dao.AddressRelevanceDao;

public class AddressRelevanceDaoImpl extends BaseDAOHibernate implements AddressRelevanceDao{

	@Override
	public List<Map<String, Object>> getAddressRelevanceList(String where, PageInfo pageInfo) {
		String str_count_sql = "SELECT count(id) FROM t_address_relevance WHERE 1=1 and (publicarea is null or publicarea=0) "+where;
				        System.out.println(str_count_sql);
				        String find_sql="SELECT id,placename ,county_name,city_name FROM t_address_relevance WHERE 1=1 and (publicarea is null or publicarea=0) ";
				        StringBuilder sb_sql = new StringBuilder();
				        sb_sql.append(find_sql);
				        sb_sql.append(where+" order by nunber desc");
				        //SQL查询对象
				        SQLQuery query = getHibernateTemplate().getSessionFactory()
				                .getCurrentSession().createSQLQuery(sb_sql.toString());

				        //查询数据量对象
				        SQLQuery countQuery = getHibernateTemplate().getSessionFactory()
				                .getCurrentSession()
				                .createSQLQuery(str_count_sql);
				        pageInfo.setTotalRecords(Integer.valueOf(countQuery.list().get(0).toString()));
				        //获得查询数据
				        List<?> lst_data = query
				                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
				                .setFirstResult(
				                        pageInfo.getRecordsPerPage()
				                                * (pageInfo.getCurrentPage() - 1))
				                .setMaxResults(pageInfo.getRecordsPerPage()).list();

				        List<Map<String,Object>> lst_result = new ArrayList<Map<String, Object>>();

				        //如果没有数据返回
				        if(lst_data == null || lst_data.size() == 0){
				            return lst_result;
				        }
				        //转换成需要的数据形式
				        for(Object obj_data : lst_data){
				        	lst_result.add((Map<String,Object>)obj_data);
				        }
				        return lst_result;
	}

	@Override
	public void upxuhao(String string) {
		delexuhao() ;
		String sql="update t_address_relevance SET nunber=1 WHERE id in ("+string+")";
		 Session session = getHibernateTemplate().getSessionFactory().openSession();
	        try{
	            Query query = session.createSQLQuery(sql);
	            int executeUpdate = query.executeUpdate();
	        }catch (Exception e){
	            e.printStackTrace();
	        }finally {
	            session.close();
	        }
	}
	
	public void delexuhao() {
		String sql="update t_address_relevance SET nunber=0";
		 Session session = getHibernateTemplate().getSessionFactory().openSession();
	        try{
	            Query query = session.createSQLQuery(sql);
	            int executeUpdate = query.executeUpdate();
	        }catch (Exception e){
	            e.printStackTrace();
	        }finally {
	            session.close();
	        }
	}

	@Override
	public void deleteXiangtong() {
		String sql="delete FROM t_address_relevance where id not in ( SELECT id FROM (select MIN(id) as id  from t_address_relevance group by placename,storeno) as c) or storeno='null'";
		 Session session = getHibernateTemplate().getSessionFactory().openSession();
	        try{
	            Query query = session.createSQLQuery(sql);
	            int executeUpdate = query.executeUpdate();
	        }catch (Exception e){
	            e.printStackTrace();
	        }finally {
	            session.close();
	        }
		
	}

	@Override
	public List<Map<String, Object>> getOrderAddressRelevanceList(String where, PageInfo pageInfo) {
		String str_count_sql = "SELECT count(reaa.id) FROM t_address_relevance reaa LEFT JOIN t_comnunity_block coee ON reaa.id=coee.address_relevance_id "+
				" LEFT JOIN t_tiny_village tiny ON tiny.id= coee.communityId "+
				" LEFT JOIN t_village vill ON vill.id=tiny.village_id WHERE 1=1 "+where;
        System.out.println(str_count_sql);
        String find_sql="SELECT reaa.id,reaa.placename,reaa.publicarea,tiny.`name` as tinyvillage_name,tiny.id as tinyId,vill.`name` as village_name FROM t_address_relevance reaa LEFT JOIN t_comnunity_block coee ON reaa.id=coee.address_relevance_id "+
" LEFT JOIN t_tiny_village tiny ON tiny.id= coee.communityId "+
" LEFT JOIN t_village vill ON vill.id=tiny.village_id WHERE 1=1 ";
        StringBuilder sb_sql = new StringBuilder();
        sb_sql.append(find_sql);
        sb_sql.append(where);
        //SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sb_sql.toString());

        //查询数据量对象
        SQLQuery countQuery = getHibernateTemplate().getSessionFactory()
                .getCurrentSession()
                .createSQLQuery(str_count_sql);
        pageInfo.setTotalRecords(Integer.valueOf(countQuery.list().get(0).toString()));
        //获得查询数据
        List<?> lst_data = query
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .setFirstResult(
                        pageInfo.getRecordsPerPage()
                                * (pageInfo.getCurrentPage() - 1))
                .setMaxResults(pageInfo.getRecordsPerPage()).list();

        List<Map<String,Object>> lst_result = new ArrayList<Map<String, Object>>();

        //如果没有数据返回
        if(lst_data == null || lst_data.size() == 0){
            return lst_result;
        }
        //转换成需要的数据形式
        for(Object obj_data : lst_data){
        	lst_result.add((Map<String,Object>)obj_data);
        }
        return lst_result;
	}

	@Override
	public void deleteComnunityAreaData() {
		String sql="DELETE FROM t_comnunity_block WHERE id NOT IN (SELECT bb.ids FROM (SELECT MIN(id) AS ids FROM t_comnunity_block GROUP BY address_relevance_id "+
	"HAVING COUNT(1) > 1) bb) AND address_relevance_id IN (SELECT cc.address_relevance_id FROM (SELECT address_relevance_id FROM t_comnunity_block "+
	"GROUP BY address_relevance_id HAVING COUNT(1) > 1) cc)";
		 Session session = getHibernateTemplate().getSessionFactory().openSession();
	        try{
	            Query query = session.createSQLQuery(sql);
	            int executeUpdate = query.executeUpdate();
	        }catch (Exception e){
	            e.printStackTrace();
	        }finally {
	            session.close();
	        }
	}

}
