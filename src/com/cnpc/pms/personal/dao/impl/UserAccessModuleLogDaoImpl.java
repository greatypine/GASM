package com.cnpc.pms.personal.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;


import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.dao.UserAccessModuleLogDao;
import com.cnpc.pms.personal.dao.UserLoginLogDao;

public class UserAccessModuleLogDaoImpl extends BaseDAOHibernate  implements UserAccessModuleLogDao{

	@Override
	public List<Map<String, Object>> getUserAccessModuleLogList(String condition,
			PageInfo pageInfo) {
		String sql="SELECT a.accessSystem,a.userGroupName,sum(a.order_record) as order_record,sum(a.emp_record) as emp_record,"+
				" sum(a.other1) as other1,sum(a.other2) as other2,sum(a.other3) as other3,sum(a.other4) as other4 from (SELECT tal.accessSystem,tal.userGroupName,"+
				" CASE WHEN tal.accessModule='订单档案' AND tal.accessSystem='数据动态系统' then count(tal.id) ELSE 0 END AS 'order_record',"+
				" CASE WHEN tal.accessModule='员工档案' AND tal.accessSystem='数据动态系统' then count(tal.id) ELSE 0 END AS 'emp_record',"+
				" CASE WHEN tal.accessModule='其它1' AND tal.accessSystem='数据动态系统' then count(tal.id) ELSE 0 END AS 'other1',"+
				" CASE WHEN tal.accessModule='其它2' AND tal.accessSystem='数据动态系统' then count(tal.id) ELSE 0 END AS 'other2',"+
				" CASE WHEN tal.accessModule='其它3' AND tal.accessSystem='数据动态系统' then count(tal.id) ELSE 0 END AS 'other3',"+
				" CASE WHEN tal.accessModule='其它4' AND tal.accessSystem='数据动态系统' then count(tal.id) ELSE 0 END AS 'other4' "+
				" FROM t_access_module_log tal GROUP BY accessSystem,userGroupName,accessModule) a GROUP BY a.accessSystem,a.userGroupName "+condition;
		
		SQLQuery query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql.toString());
		List<Map<String, Object>> returnList = query.list();
		return returnList;
		
	}

	
}
