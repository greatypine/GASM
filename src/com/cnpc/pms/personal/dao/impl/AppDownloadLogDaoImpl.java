package com.cnpc.pms.personal.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;


import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.dao.AppDownloadLogDao;
import com.cnpc.pms.personal.dao.UserLoginLogDao;

public class AppDownloadLogDaoImpl extends BaseDAOHibernate  implements AppDownloadLogDao{

	@Override
	public List<Map<String, Object>> getAppDownloadLogList(String condition,
			PageInfo pageInfo) {
		String sql="select b.downloadVersion,b.downloadtype,sum(b.curday) as curdays,sum(b.curmonth) as curmonths,sum(b.totalcount) as totalcounts from (select a.downloadVersion,a.downloadtype,"+
					" CASE WHEN a.curdate = DATE_FORMAT(CURDATE(),'%Y-%m-%d') THEN a.count ELSE 0 END as 'curday',"+
					" CASE WHEN a.curmonth = DATE_FORMAT(CURDATE(),'%Y-%m') THEN a.count ELSE 0 END as 'curmonth',"+
					" a.count as totalcount  from ("+
					" SELECT ad.downloadVersion,ad.downloadtype,DATE_FORMAT(ad.downloadDate,'%Y-%m-%d') as curdate,DATE_FORMAT(ad.downloadDate,'%Y-%m') as curmonth,count(1) as count"+
					" from t_app_download_log ad GROUP BY ad.downloadVersion,ad.downloadtype,curdate) a) b GROUP BY b.downloadVersion,b.downloadtype having "+condition+" ORDER BY b.downloadVersion DESC ";
		SQLQuery query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(sql.toString());
		List<Map<String, Object>> returnList = query.list();
		/*List returnList = query.setFirstResult(
				pageInfo.getRecordsPerPage() * (pageInfo.getCurrentPage() - 1))
				.setMaxResults(pageInfo.getRecordsPerPage()).list();*/
		return returnList;
		
	}

	

}
