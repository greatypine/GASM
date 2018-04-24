package com.cnpc.pms.activity.dao.impl;

import com.cnpc.pms.activity.dao.ActivityDao;
import com.cnpc.pms.activity.entity.LastInsertIDDTO;
import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by litianyu on 2017/10/13.
 */
public class ActivityDaoImpl extends BaseDAOHibernate implements ActivityDao {


    @Override
    public List<LastInsertIDDTO> queryActivityLast_Insert_ID() {
        StringBuffer sql = new StringBuffer();
        sql.append("select LAST_INSERT_ID() AS ID");
        //查询
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sql.toString());
        List<Map<String,Object>> lastInsertIDDTOList = query.setResultTransformer(
                Transformers.ALIAS_TO_ENTITY_MAP).list();
        List<LastInsertIDDTO> returnList = new ArrayList<LastInsertIDDTO>();
        for(Map<String,Object> map : lastInsertIDDTOList){
            LastInsertIDDTO bloodOxygen = new LastInsertIDDTO();
            String lAST_INSERT_ID_ = (String)map.get("ID");
            if( StringUtils.isNotBlank(lAST_INSERT_ID_)){
                bloodOxygen.setLAST_INSERT_ID_(lAST_INSERT_ID_);
            }
            returnList.add(bloodOxygen);
        }
        return returnList;
    }
}
