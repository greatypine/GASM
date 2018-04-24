package com.cnpc.pms.personal.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.SQLQuery;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.personal.dao.TinyVillageCodeDao;
import com.cnpc.pms.personal.entity.TinyVillageCode;

public class TinyVillageCodeDaoImpl extends BaseDAOHibernate implements TinyVillageCodeDao{

	@Override
	public TinyVillageCode saveTinyVillageCode(TinyVillageCode tinyvillagecode) {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
		String format = dateFormat.format(date);
		String addStoreSql="INSERT INTO tiny_village_code (`code`,`version`,`create_time`,`create_user_id`,`tiny_village_name`,`tiny_village_id`,`update_time`, `update_user_id`) VALUES ('"+tinyvillagecode.getCode()+"','0','"+format+"',"+tinyvillagecode.getCreate_user_id()+",'"+tinyvillagecode.getTiny_village_name().replace("'","''")+"',"+tinyvillagecode.getTiny_village_id()+",'"+format+"',"+tinyvillagecode.getUpdate_user_id()+")";
		SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(addStoreSql);
		int update = query.executeUpdate();
		return tinyvillagecode;
	}

	@Override
	public Integer findMaxTinyVillageCode(String town_gb_code) {
		String Sql = "SELECT MAX(RIGHT(code,10)) FROM tiny_village_code  WHERE LEFT(code,12)='"+town_gb_code+"'";
		SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(Sql);
		List list = query.list();
		Object object = list.get(0);
		return object==null?null:Integer.parseInt(object+"");
	}

}
