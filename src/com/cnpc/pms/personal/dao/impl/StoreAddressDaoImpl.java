package com.cnpc.pms.personal.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.personal.dao.StoreAddressDao;

public class StoreAddressDaoImpl extends BaseDAOHibernate implements StoreAddressDao{

	@Override
	public List<Map<String, Object>> queryStoreAddressList(Long id) {
        String find_sql="SELECT tsa.id,tsr.store_name,tsa.store_address,tsr.bonus,tsa.total_area,DATE_FORMAT(tsa.create_time,'%Y-%m-%d %H:%i:%s') as create_time,tsa.store_rent,tsa.storestatus,tsr.store_standard_id FROM t_store_address tsa LEFT JOIN t_store_requirement tsr ON tsa.site_requirement_id=tsr.id WHERE tsa.site_selection_id="+id;
        //SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(find_sql);
        //获得查询数据
        List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return lst_data;
	}

}
