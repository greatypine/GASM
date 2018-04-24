package com.cnpc.pms.personal.dao.impl;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.personal.dao.HouseDao;
import com.cnpc.pms.personal.entity.House;
import com.cnpc.pms.personal.entity.HouseStyle;
import com.cnpc.pms.personal.entity.TinyVillage;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 房屋数据层实现类
 * Created by liuxiao on 2016/5/24 0024.
 */
public class HouseDaoImpl extends BaseDAOHibernate implements HouseDao {

    /**
     * 根据街道id 获取街道中各个小区的住户数量
     * @param town_id 街道id
     * @return 住户数量集合
     */
    @Override
    public List<Map<String, Object>> getHouseCountByTownId(Long town_id) {

        List<Map<String,Object>> lst_result = new ArrayList<Map<String, Object>>();

        StringBuilder sb_sql = new StringBuilder();
        sb_sql.append(" SELECT");
        sb_sql.append("     tt.`name` 街道,");
        sb_sql.append("     tv.`name` 社区,");
        sb_sql.append("     ttv.`name` 小区,");
        sb_sql.append("     tb.`name` 楼号,");
        sb_sql.append("     th.building_unit_no 单元,");
        sb_sql.append("     count(1) 住房数");
        sb_sql.append("     FROM");
        sb_sql.append(" t_house th LEFT JOIN t_building tb ON tb.id = th.building_id");
        sb_sql.append(" LEFT JOIN t_tiny_village ttv  ON ttv.id = th.tinyvillage_id");
        sb_sql.append(" LEFT JOIN t_village tv ON tv.id = ttv.village_id");
        sb_sql.append(" LEFT JOIN t_town tt ON tt.id = tv.town_id");
        sb_sql.append(" WHERE th.status=0 ");
        sb_sql.append(" AND tt.`id` = '").append(town_id).append("'");
        sb_sql.append(" AND tb.type = 1");
        sb_sql.append(" GROUP BY");
        sb_sql.append(" 街道,社区,小区,楼号,单元");

        //SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sb_sql.toString());

        //获得查询数据
        List<?> lst_data = query
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

        if (lst_data == null || lst_data.size() == 0){
            return lst_result;
        }
        for(Object obj_row_map : lst_data){
            lst_result.add((Map<String,Object>)obj_row_map);
        }
        return lst_result;
    }

	@Override
	public House getHouse(Long id) {
		String houseSql="SELECT house.* from t_town town LEFT JOIN t_tiny_village tiny ON town.id=tiny.town_id "+
" LEFT JOIN t_house house ON tiny.id=house.tinyvillage_id WHERE house.address is not null and house.baidu_coordinate_x is null and house.sogou_coordinate_x is null and house.gaode_coordinate_x is null and town.id="+id;
		SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(houseSql);
		 List<House> list = query.addEntity(House.class).list();
		 if(list!=null&&list.size()>0){
			 return list.get(0);
		 }
		return null;
	}

	@Override
	public List<Map<String, Object>> getHouseList(String where, PageInfo pageInfo) {
		//sql查询列，用于分页计算数据总数
        String str_count_sql = "SELECT COUNT(1) FROM t_house house "+
"LEFT JOIN t_tiny_village tiny on house.tinyvillage_id=tiny.id "+
"LEFT JOIN t_village vill ON tiny.village_id=vill.id LEFT JOIN t_town town ON town.id=tiny.town_id left join t_county county on county.id=town.county_id left join t_city city on city.id=county.city_id  "+
"LEFT JOIN t_building build ON house.building_id=build.id WHERE house.tinyvillage_id is not NULL and house.`status`=0 "+where;
        System.out.println(str_count_sql);
        //sql查询列，用于页面展示所有的数据
        String find_sql="SELECT vill.`name` as village_name,tiny.`name` as tinyvillage_name,house.id,house.address,case when (house.building_unit_no='null') then '' ELSE house.building_unit_no END as building_unit_no,build.`name` as building_name,case WHEN house.house_type=1 THEN house.building_house_no WHEN house.house_type=0 THEN house.house_no WHEN house.house_type=2 THEN house.house_no ELSE house.building_house_no END as building_house_no "+
" FROM t_house house "+ 
"LEFT JOIN t_tiny_village tiny on house.tinyvillage_id=tiny.id "+
"LEFT JOIN t_village vill ON tiny.village_id=vill.id LEFT JOIN t_town town ON town.id=tiny.town_id left join t_county county on county.id=town.county_id left join t_city city on city.id=county.city_id  "+
"LEFT JOIN t_building build ON house.building_id=build.id WHERE house.tinyvillage_id is not NULL and house.`status`=0 ";
        StringBuilder sb_sql = new StringBuilder();
        sb_sql.append(find_sql);
        sb_sql.append(where +" order by house.id desc");
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
	public List<Map<String, Object>> getHouseListInfomation(String where, PageInfo pageInfo) {
		//sql查询列，用于分页计算数据总数
        String str_count_sql = " SELECT COUNT(1) from t_house hou LEFT JOIN t_tiny_village tin ON hou.tinyvillage_id=tin.id "+
" LEFT JOIN t_village vill ON tin.village_id=vill.id WHERE hou.baidu_coordinate_x=0 AND hou.gaode_coordinate_x=0 AND hou.sogou_coordinate_x=0 ";

        System.out.println(str_count_sql);
        //sql查询列，用于页面展示所有的数据
        StringBuilder sb_sql = new StringBuilder();
        sb_sql.append(" SELECT hou.id,vill.`name` as village_name,tin.`name` as tinyvillage_name,buil.`name` as building_name,hou.building_unit_no,hou.house_no,hou.building_house_no,hou.address from t_house hou LEFT JOIN t_tiny_village tin ON hou.tinyvillage_id=tin.id "+
" LEFT JOIN t_building buil ON buil.id=hou.building_id LEFT JOIN t_village vill ON tin.village_id=vill.id WHERE hou.baidu_coordinate_x=0 AND hou.gaode_coordinate_x=0 AND hou.sogou_coordinate_x=0");
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
        	System.out.println(obj_data);
        	Map<String,Object> map=(Map<String,Object>)obj_data;
            lst_result.add((Map<String,Object>)obj_data);
        	//lst_result.add(map);
        }
        return lst_result;
	}

	@Override
	public Map<String, Object> getHouseInfomation(Long id) {
		String str_total_count="SELECT COUNT(1) from t_town town LEFT JOIN t_tiny_village tiny ON town.id=tiny.town_id "+
" LEFT JOIN t_house house ON tiny.id=house.tinyvillage_id WHERE house.address is not null and house.baidu_coordinate_x is null and house.sogou_coordinate_x is null and house.gaode_coordinate_x is null and town.id="+id;
/*		String str_error_count="SELECT COUNT(1) from t_house WHERE baidu_coordinate_x =0 AND gaode_coordinate_x =0 AND sogou_coordinate_x =0";
		String str_modified_count="SELECT COUNT(1) from t_house WHERE baidu_coordinate_x is NOT NULL or gaode_coordinate_x is NOT null OR sogou_coordinate_x is NOT NULL";*/
		
		SQLQuery countQuery = getHibernateTemplate().getSessionFactory()
                .getCurrentSession()
                .createSQLQuery(str_total_count);
		Object result1 = countQuery.uniqueResult();
		/*SQLQuery errorQuery = getHibernateTemplate().getSessionFactory()
                .getCurrentSession()
                .createSQLQuery(str_error_count);
		Object result2 = errorQuery.uniqueResult();
		SQLQuery modifiedQuery = getHibernateTemplate().getSessionFactory()
                .getCurrentSession()
                .createSQLQuery(str_modified_count);
		Object result3 = modifiedQuery.uniqueResult();*/
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("total_count", result1);
		/*map.put("error_count", result2);
		map.put("modified_count", result3);*/
		return map;
	}

	@Override
	public int updateHouse( Long id) {
		String update_sql="update t_house SET baidu_coordinate_x=NULL,baidu_coordinate_y=NULL,sogou_coordinate_x=NULL,"+
				"sogou_coordinate_y=NULL,"+
"gaode_coordinate_x=NULL,gaode_coordinate_y=NULL WHERE id="+id;
		 SQLQuery query = getHibernateTemplate().getSessionFactory()
		.getCurrentSession().createSQLQuery(update_sql);
		 int update = query.executeUpdate();
		 return update;
	}

	@Override
	public List<House> getHouseByCondition(Long tinyvillage_id,Long building_id,String str) {
		String t_house="SELECT * from t_house  where  tinyvillage_id="+tinyvillage_id +" And building_id="+building_id+" "+str;
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		 Session session = sessionFactory.getCurrentSession();
		 SQLQuery sqlQuery =session.createSQLQuery(t_house);
		 List<House> list = sqlQuery.addEntity(House.class).list();
		return list;
	}
	
	@Override
	public List<House> getHouseByConditionBySHangye(Long tinyvillage_id,Long building_id,String str) {
		String t_house="SELECT * from t_house  where  tinyvillage_id="+tinyvillage_id +" And building_id="+building_id+" "+str +" ORDER BY house_no DESC LIMIT 1";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		 Session session = sessionFactory.getCurrentSession();
		 SQLQuery sqlQuery =session.createSQLQuery(t_house);
		 List<House> list = sqlQuery.addEntity(House.class).list();
		return list;
	}
	
	

	@Override
	public List<House> getHousegroup(String string) {
		String t_house="SELECT house.* from t_house house  LEFT JOIN t_tiny_village tiny ON tiny.id=house.tinyvillage_id LEFT JOIN t_building build ON house.building_id=build.id where 1=1 "+string+" GROUP BY house.building_id,house.tinyvillage_id";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		 Session session = sessionFactory.getCurrentSession();
		 SQLQuery sqlQuery =(SQLQuery) session.createSQLQuery(t_house);
		 List<House> list = sqlQuery.addEntity(House.class).list();
		return list;
	}

	@Override
	public void deleteHouse(String ids, Long tinyvillage_id, Long building_id) {
		String deleSql="DELETE FROM t_house WHERE id not in ("+ids+") AND id not in (SELECT house_id FROM t_house_style) AND tinyvillage_id="+tinyvillage_id+" AND building_id ="+building_id;
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		 Session session = sessionFactory.getCurrentSession();
		 SQLQuery sqlQuery =session.createSQLQuery(deleSql);
		int executeUpdate = sqlQuery.executeUpdate();
	}

	@Override
	public void updateHousetinyvillage(String builds, Long tinyid,User user) {
		//修改的sql
				String sql="update t_house SET tinyvillage_id="+tinyid+",update_user='"+user.getName()+"' ,update_user_id="+user.getId()+",update_time=NOW() WHERE building_id in(SELECT id FROM t_building WHERE id in ("+builds+"))";
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
	public List<House> findBuildingHouseByBuildingId(Long building_id) {
		String sql="SELECT * FROM t_house WHERE building_id="+building_id+" AND `status`=0 ORDER BY building_unit_no ASC,building_house_no+0 ASC";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		 Session session = sessionFactory.getCurrentSession();
		 SQLQuery sqlQuery =(SQLQuery) session.createSQLQuery(sql);
		 List<House> list = sqlQuery.addEntity(House.class).list();
        return list;
	}

	@Override
	public List<House> findBusinessBuilding(Long tiny_id) {
		String sql="SELECT house.building_id,MAX(house.house_no) as house_no,build.`name` as building_name  FROM t_house house LEFT JOIN t_building build ON house.building_id=build.id  WHERE house.tinyvillage_id="+tiny_id+" and house.house_type=2 AND house.`status`=0 GROUP BY house.building_id";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		 Session session = sessionFactory.getCurrentSession();
		 SQLQuery sqlQuery =(SQLQuery) session.createSQLQuery(sql);
		 List<House> list = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return list;
	}

	@Override
	public String findBusinessMaxHouseNo(Long building_id, Long tinyvillage_id) {
		String sql="SELECT MAX(house_no) FROM t_house WHERE tinyvillage_id="+tinyvillage_id+" AND  building_id="+building_id+" AND house_type=2 GROUP BY building_id";
		HibernateTemplate template = getHibernateTemplate();
		SessionFactory sessionFactory = template.getSessionFactory();
		 Session session = sessionFactory.getCurrentSession();
		 SQLQuery sqlQuery =(SQLQuery) session.createSQLQuery(sql);
		 List list = sqlQuery.list();
		if(list!=null&&list.size()>0){
			Object object = list.get(0);
			return (String) ( object!=null?object:null);
		}
		return null;
	}

	@Override
	public Integer findHouseByCityName(String cityName) {
		String findSql="SELECT count(1) FROM t_house WHERE tinyvillage_id in (SELECT id FROM t_tiny_village WHERE (town_id in (SELECT id FROM t_town  WHERE county_id in (SELECT id FROM t_county WHERE city_id in (SELECT id FROM t_city WHERE `name` LIKE '%"+cityName+"%'))) or village_id in (SELECT id FROM t_village WHERE town_id in (SELECT id FROM t_town  WHERE county_id in (SELECT id FROM t_county WHERE city_id in (SELECT id FROM t_city WHERE `name` LIKE '%"+cityName+"%'))))) AND status=0) AND `status`=0";		
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
	public Integer findHouseByTownIdse(String townIds) {
		String findSql="SELECT COUNT(1) FROM t_house WHERE tinyvillage_id in (SELECT id FROM t_tiny_village WHERE (town_id in ("+townIds+") or village_id in (SELECT id FROM t_village WHERE town_id in ("+townIds+"))) AND status=0) AND `status`=0";		
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
	public Integer findHouseCountByArea(String areaNo) {
		String findSql="SELECT CAST(IFNULL(SUM(sumhouse),0) AS signed) as sumbuilding from df_tinyarea_datainfo where area_no = '"+areaNo+"'";	
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
			return Integer.parseInt(list.get(0)+"");
		}
        return 0;
	}

	@Override
	public Integer findHouseCountByVillageCode(String code) {
		String findSql="SELECT CAST(IFNULL(SUM(sumhouse),0) AS signed) as sumbuilding from df_tinyarea_datainfo where tiny_code = '"+code+"'";	
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
			return Integer.parseInt(list.get(0)+"");
		}
        return 0;
	}
}
