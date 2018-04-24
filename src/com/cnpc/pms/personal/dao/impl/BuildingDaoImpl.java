package com.cnpc.pms.personal.dao.impl;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.personal.dao.BuildingDao;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by liuxi on 2017/2/28.
 */
public class BuildingDaoImpl extends BaseDAOHibernate implements BuildingDao {
    @Override
    public List<Map<String, Object>> queryBuilding(String where, PageInfo pageInfo) {
        //sql查询列，用于分页计算数据总数
        String str_form_sql = "FROM t_building tb " +
                "    LEFT JOIN t_tiny_village ttv ON ttv.id = tb.tinyvillage_id" +
                "    LEFT JOIN t_village tv ON tv.id = ttv.village_id LEFT JOIN t_town towt ON ttv.town_id=towt.id LEFT JOIN t_county coue ON towt.county_id=coue.id LEFT JOIN t_city citt ON citt.id=coue.city_id WHERE 1=1 and tb.type=1 and tb.status=0 " + where+" ORDER BY tb.update_time DESC,tb.create_time DESC";
        String str_count_sql = "SELECT COUNT(1) ";

        String str_sql = "SELECT tb.id,tb.`name`,ttv.`name` AS tiny_village_name,tv.`name` AS village_name,towt.`name` as town_name ";
       // str_sql=str_sql.concat(str_form_sql);
       // System.out.println(str_sql);
        //SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(str_sql.concat(str_form_sql));

        //查询数据量对象
        SQLQuery countQuery = getHibernateTemplate().getSessionFactory()
                .getCurrentSession()
                .createSQLQuery(str_count_sql.concat(str_form_sql));
        pageInfo.setTotalRecords(Integer.valueOf(countQuery.list().get(0).toString()));
        //获得查询数据
        List<?> lst_data = query
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .setFirstResult(
                        pageInfo.getRecordsPerPage()
                                * (pageInfo.getCurrentPage() - 1))
                .setMaxResults(pageInfo.getRecordsPerPage()).list();


        //如果没有数据返回
        if(lst_data == null || lst_data.size() == 0){
            return new ArrayList<Map<String, Object>>();
        }


        return (List<Map<String,Object>>)lst_data;
    }

	@Override
	public List<Map<String, Object>> queryBuildingdata(String where, PageInfo pageInfo) {
		//sql查询列，用于分页计算数据总数
        String str_form_sql = "FROM t_building WHERE 1=1 and status=0 and  type=1 " + where;
        String str_count_sql = "SELECT COUNT(1) ";
        String str_sql = "SELECT id,`name` ";
        //SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(str_sql.concat(str_form_sql));

        //查询数据量对象
        SQLQuery countQuery = getHibernateTemplate().getSessionFactory()
                .getCurrentSession()
                .createSQLQuery(str_count_sql.concat(str_form_sql));
        pageInfo.setTotalRecords(Integer.valueOf(countQuery.list().get(0).toString()));
        //获得查询数据
        List<?> lst_data = query
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .setFirstResult(
                        pageInfo.getRecordsPerPage()
                                * (pageInfo.getCurrentPage() - 1))
                .setMaxResults(pageInfo.getRecordsPerPage()).list();
        //如果没有数据返回
        if(lst_data == null || lst_data.size() == 0){
            return new ArrayList<Map<String, Object>>();
        }
        return (List<Map<String,Object>>)lst_data;
	}

	@Override
	public void updateBuildingtinyvillage(String ids, Long tinyid,User user) {
		//修改的sql
		String sql="update t_building SET tinyvillage_id="+tinyid+",update_user='"+user.getName()+"' ,update_user_id="+user.getId()+",update_time=NOW()  WHERE id in("+ids+")";
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
	public Integer findBuildingByCityName(String cityName) {
		String findSql="SELECT count(1) FROM t_building WHERE tinyvillage_id in (SELECT id FROM t_tiny_village WHERE (town_id in (SELECT id FROM t_town  WHERE county_id in (SELECT id FROM t_county WHERE city_id in (SELECT id FROM t_city WHERE `name` LIKE '%"+cityName+"%'))) or village_id in (SELECT id FROM t_village WHERE town_id in (SELECT id FROM t_town  WHERE county_id in (SELECT id FROM t_county WHERE city_id in (SELECT id FROM t_city WHERE `name` LIKE '%"+cityName+"%'))))) AND status=0) AND type=1 AND `status`=0";		
		Session session = getHibernateTemplate().getSessionFactory().openSession();
        List list=null;
		try{
           SQLQuery sqlQuery = session.createSQLQuery(findSql);
           list= sqlQuery.list();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            session.close();
        }
		if(list!=null&&list.size()>0){
			return Integer.parseInt(list.get(0)+"") ;
		}
        return 0;
	}

	@Override
	public Integer findBuildingByTownIdse(String townIds) {
		String findSql="SELECT COUNT(1) FROM t_building WHERE tinyvillage_id in (SELECT id FROM t_tiny_village WHERE (town_id in ("+townIds+") or village_id in (SELECT id FROM t_village WHERE town_id in ("+townIds+"))) AND status=0) AND type=1 AND `status`=0";		
		Session session = getHibernateTemplate().getSessionFactory().openSession();
        List list=null;
		try{
           SQLQuery sqlQuery = session.createSQLQuery(findSql);
           list= sqlQuery.list();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            session.close();
        }
		if(list!=null&&list.size()>0){
			return Integer.parseInt(list.get(0)+"") ;
		}
        return 0;
	}

	@Override
	public Integer findBuildingCountByAreaNo(String areaNo) {
		String findSql="SELECT CAST(IFNULL(SUM(sumbuilding),0) AS signed) as sumbuilding from df_tinyarea_datainfo where area_no = '"+areaNo+"'";	
		Session session = getHibernateTemplate().getSessionFactory().openSession();
        List list=null;
		try{
           SQLQuery sqlQuery = session.createSQLQuery(findSql);
           list= sqlQuery.list();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            session.close();
        }
		if(list!=null&&list.size()>0){
			return Integer.parseInt((list.get(0)+"")) ;
		}
        return 0;
	}

	@Override
	public Integer findBuildingCountByVillageCode(String code) {
		String findSql="SELECT CAST(IFNULL(SUM(sumbuilding),0) AS signed) as sumbuilding from df_tinyarea_datainfo where tiny_code = '"+code+"'";	
		Session session = getHibernateTemplate().getSessionFactory().openSession();
        List list=null;
		try{
           SQLQuery sqlQuery = session.createSQLQuery(findSql);
           list= sqlQuery.list();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            session.close();
        }
		if(list!=null&&list.size()>0){
			return Integer.parseInt((list.get(0)+"")) ;
		}
        return 0;
	}
}
