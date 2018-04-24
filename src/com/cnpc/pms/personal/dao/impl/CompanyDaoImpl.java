package com.cnpc.pms.personal.dao.impl;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.personal.dao.CompanyDao;
import com.cnpc.pms.personal.dao.NewsDao;
import com.cnpc.pms.personal.entity.Company;
import com.cnpc.pms.personal.entity.HouseStyle;

public class CompanyDaoImpl extends BaseDAOHibernate implements CompanyDao{

	@Override
	public Company getCompany(String office_company, String office_floor_of_company, int office_id) {
		String string=null;
		if("".equals(office_floor_of_company)){
			string=" and office_id='"+office_id+"' and office_company like '%"+office_company+"%'";
		}else{
			string=" and office_id='"+office_id+"' and office_floor_of_company='"+office_floor_of_company+"' and office_company = '"+office_company+"'";
		}
		
		String sql="select * from t_company_info where 1=1 "+string;
		
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		 Session session = sessionFactory.getCurrentSession();
		 SQLQuery sqlQuery =session.createSQLQuery(sql);
		 List<Company> list = sqlQuery.addEntity(Company.class).list();
		 if(list!=null&&list.size()>0){
			 return list.get(0);
		 }
		return null;
	}

	@Override
	public void saveOrUpdate(Company company) {
		String sql=null;
		if(company.getCompany_id()==null){
			 sql="INSERT INTO t_company_info (  `office_company`, `office_floor_of_company`, `office_id`) VALUES ('"+company.getOffice_company()+"', '"+company.getOffice_floor_of_company()+"', '"+company.getOffice_id()+"')";
		}else{
			sql="UPDATE t_company_info SET  `office_company`='"+company.getOffice_company()+"', `office_floor_of_company`='"+company.getOffice_floor_of_company()+"', `office_id`='"+company.getOffice_id()+"' WHERE company_id="+company.getCompany_id();
		}
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql);
		query.executeUpdate();
		
	}

	@Override
	public void getComPanybyOffice_idandCompany_id(Integer office, String Companyids) {
		NewsDao newsDao=(NewsDao)SpringHelper.getBean(NewsDao.class.getName());
		String sql="SELECT * from t_company_info WHERE office_id='"+office+"' AND company_id NOT in ("+Companyids+")";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		 Session session = sessionFactory.getCurrentSession();
		 SQLQuery sqlQuery =session.createSQLQuery(sql);
		 List<Company> list = sqlQuery.addEntity(Company.class).list();
		 if(list!=null&&list.size()>0){
			 for (Company company : list) {
				 newsDao.deleteObject(company);
			}
		 }
		
	}
	
	
	
	
	

}
