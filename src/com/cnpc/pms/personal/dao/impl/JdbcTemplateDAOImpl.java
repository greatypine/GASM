package com.cnpc.pms.personal.dao.impl;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cnpc.pms.base.dao.core.impl.DAORootHibernate;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.personal.dao.JdbcTemplateDAO;

@SuppressWarnings("all")
public class JdbcTemplateDAOImpl extends DAORootHibernate implements JdbcTemplateDAO {
	
	@Override
	public List<Map<String,Object>> queryTestList() {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) SpringHelper.getBean("jdbcTemplate");
		List<Map<String,Object>> list = jdbcTemplate.queryForList("select * from t_jc_humanresource");
		System.out.println("=======================================================");
		for(Map<String,Object> object :list) {
			System.out.println(object.get("name").toString()+"---------"+object.get("employee_no").toString());
		}
		return list;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
}
