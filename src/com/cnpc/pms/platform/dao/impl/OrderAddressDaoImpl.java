package com.cnpc.pms.platform.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.core.impl.DAORootHibernate;
import com.cnpc.pms.platform.dao.OrderAddressDao;

public class OrderAddressDaoImpl extends DAORootHibernate implements OrderAddressDao{

	@Override
	public List<Map<String, Object>> getOrderAddress() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String stringData = dateFormat.format(date);
		List<Map<String,Object>> lst_result = new ArrayList<Map<String,Object>>();
		String sql = "SELECT ass.ad_code,ass.ad_name,ass.city_code,ass.city_name,ass.province_code,ass.province_name,ass.placename,ss.code,ass.latitude,ass.longitude FROM t_order_address ass LEFT JOIN t_order ordd ON ass.id=ordd.order_address_id LEFT JOIN t_store ss ON ss.id=ordd.store_id "+
"WHERE ass.placename is not NULL AND ass.placename!='' AND ss.code is not NULL AND ss.code!=''  AND DATE_FORMAT(ass.create_time,'%Y-%m-%d %H:%i:%s')<='"+stringData+"' and DATE_FORMAT(ass.create_time,'%Y-%m-%d %H:%i:%s')>=date_sub('"+stringData+"',interval 1 day) GROUP BY ass.placename,ss.code";
        Session session = getHibernateTemplate().getSessionFactory().openSession();
        try{
            Query query = session.createSQLQuery(sql);
            List<?> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
            if(lst_data!=null&&lst_data.size()>0){
            	for(Object obj_data : lst_data){
                	lst_result.add((Map<String,Object>)obj_data);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            session.close();
        }
        return lst_result;
	}

	public static void main(String[] args) {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String string = dateFormat.format(date);
		System.out.println(string);
	}
}
