package com.cnpc.pms.personal.dao.impl;

import com.cnpc.pms.base.dao.core.impl.DAORootHibernate;
import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.IJoin;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.dao.HouseStyleDao;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 房屋户型数据层实现类
 * Created by liuxiao on 2016/5/24 0024.
 */
public class HouseStyleDaoImpl extends BaseDAOHibernate implements HouseStyleDao {

    /**
     * 查询房屋户型接口图
     * @param where 查询条件
     * @param pageInfo 分页对象
     * @return 数据集合
     */
    @Override
    public List<Map<String, Object>> queryHouseStyleData(String where, PageInfo pageInfo) {
        //sql查询列，用于分页计算数据总数
        String str_count_sql = "SELECT COUNT(1) FROM t_house where status = 0";
        //sql查询列，用于页面展示所有的数据
        StringBuilder sb_sql = new StringBuilder();
        sb_sql.append(" SELECT");
        //小区
        sb_sql.append(" tv.id        AS tiny_village_id,");
        sb_sql.append(" tv.`name`    AS tiny_village_name,");
        //小区地址
        sb_sql.append(" tv.address   AS tiny_village_address,");
        //楼房
        sb_sql.append(" b.id         AS building_id,");
        sb_sql.append(" b.`name`     AS building_name,");
        //住宅
        sb_sql.append(" h.id         AS house_id,");
        //单元号
        sb_sql.append(" h.building_unit_no        AS building_house_no,");
        //楼层号
        sb_sql.append(" h.commercial_floor_number AS floor_number,");
        //房间号
        sb_sql.append(" h.building_house_no      AS house_no,");
        //户型主键
        sb_sql.append(" hs.id           AS house_style_id,");
        //面积
        sb_sql.append(" hs.house_area   AS house_area,");
        //朝向
        sb_sql.append(" hs.house_toward AS house_toward,");
        //户型
        sb_sql.append(" hs.house_style  AS house_style,");
        //户型图
        sb_sql.append(" hs.house_pic    AS house_pic");
        //查询的sql语句
        sb_sql.append(" FROM t_house h LEFT JOIN t_house_style hs ON hs.house_id = h.id");
        sb_sql.append("     LEFT JOIN t_building b ON b.id = h.building_id");
        sb_sql.append("     LEFT JOIN t_tiny_village tv ON tv.id = b.tinyvillage_id ");
        sb_sql.append(" WHERE h.status = 0 ");
        sb_sql.append(where);
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
    public void updateBuildingAndHouseStatus(Long tinVillageId) {
        String str_update_build_sql = "UPDATE t_building SET `status` = 1 WHERE tinyvillage_id = '"+tinVillageId+"'";

        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(str_update_build_sql);

        int resultCount = query.executeUpdate();

        if(resultCount > 0){
            StringBuilder sb_update_house_sql = new StringBuilder();
            sb_update_house_sql.append(" UPDATE t_house h,t_house_style hs");
            sb_update_house_sql.append(" SET h.`status` = 1 , hs.`status` = 1");
            sb_update_house_sql.append(" WHERE hs.house_id = h.id");
            sb_update_house_sql.append(" AND h.building_id in (SELECT t_building.id FROM t_building WHERE t_building.tinyvillage_id = '");
            sb_update_house_sql.append(tinVillageId);
            sb_update_house_sql.append("')");
            query = getHibernateTemplate().getSessionFactory()
                    .getCurrentSession().createSQLQuery(sb_update_house_sql.toString());
            query.executeUpdate();
        }
    }

    @Override
    public List<?> getHouseListByTown(Long town_id, String village_id) {
        StringBuilder sb_sql = new StringBuilder();
//        sb_sql.append(" SELECT");
//        sb_sql.append("  ttv.`name` AS 小区名,");
//        sb_sql.append("  ttv.address AS 小区地址,");
//        sb_sql.append("          tb.`name` AS 楼号,");
//        sb_sql.append("  th.building_unit_no AS 单元号,");
//        sb_sql.append("          th.commercial_floor_number AS 楼层,");
//        sb_sql.append("  th.building_house_no AS 房间号,");
//        sb_sql.append("          hs.house_area AS 面积,");
//        sb_sql.append("  hs.house_toward AS 朝向,");
//        sb_sql.append("          hs.house_style AS 户型,");
//        sb_sql.append("  hs.house_pic AS 户型图");
//        sb_sql.append("          FROM");
//        sb_sql.append("  t_house th LEFT JOIN t_building tb ON tb.id = th.building_id");
//        sb_sql.append("  LEFT JOIN t_tiny_village ttv  ON ttv.id = tb.tinyvillage_id");
//        sb_sql.append("  LEFT JOIN t_house_style hs ON hs.house_id = th.id");
//        sb_sql.append("  WHERE");
//        sb_sql.append("          (ttv.town_id = ")
//                .append(town_id)
//                .append(" OR ttv.village_id in (")
//                .append(village_id)
//                .append("))");
//        sb_sql.append("  AND tb.type = 1");
        sb_sql.append("  SELECT");
        sb_sql.append("  ttv.`name` AS 小区名,");
        sb_sql.append("  ttv.address AS 小区地址,");
        sb_sql.append("          tb.`name` AS 楼号,");
        sb_sql.append("  th.building_unit_no AS 单元号,");
        sb_sql.append("          th.commercial_floor_number AS 楼层,");
        sb_sql.append("  th.building_house_no AS 房间号,");
        sb_sql.append("          hs.house_area AS 面积,");
        sb_sql.append("  hs.house_toward AS 朝向,");
        sb_sql.append("          hs.house_style AS 户型,");
        sb_sql.append("  hs.house_pic AS 户型图,");
        sb_sql.append("          tc.`name` AS 姓名,");
        sb_sql.append("  (SELECT dicttext FROM dict_customer_sex WHERE dictcode = tc.sex) AS 性别,");
        sb_sql.append("  tc.age AS 年龄,");
        sb_sql.append("          tc.mobilephone AS 电话,");
        sb_sql.append("  tc.customer_pic AS 照片,");
        sb_sql.append("          tc.nationality AS 民族,");
        sb_sql.append("  (SELECT dicttext FROM dict_house_property_resource WHERE dictcode = tc.house_property) AS 住房性质,");
        sb_sql.append("  tc.job AS 职业,");
        sb_sql.append("          tc.income AS 户收入,");
        sb_sql.append("  (SELECT dicttext FROM dict_work_overtime_resource WHERE dictcode = tc.work_overtime) AS 加班,");
        sb_sql.append("  tc.family_num AS 家庭人口数,");
        sb_sql.append("          tc.preschool_num AS 学龄前人数,");
        sb_sql.append("  tc.minor_num AS 未成年人数,");
        sb_sql.append("          tc.pet_type AS 宠物类型");
        sb_sql.append("  FROM");
        sb_sql.append("  t_house th LEFT JOIN t_building tb ON tb.id = th.building_id");
        sb_sql.append("  LEFT JOIN t_tiny_village ttv  ON ttv.id = th.tinyvillage_id");
        sb_sql.append("  LEFT JOIN t_house_style hs ON hs.house_id = th.id");
        sb_sql.append("  LEFT JOIN t_customer tc ON tc.id = hs.customer_id");
        sb_sql.append("  WHERE (ttv.town_id = ")
                .append(town_id)
                .append(" OR ttv.village_id in (")
                .append(village_id)
                .append("))  AND ttv.tinyvillage_type = 1");

        //SQL查询对象
        SQLQuery query = getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createSQLQuery(sb_sql.toString());

        //获得查询数据
        List<?> lst_data = query
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return lst_data;
    }


	@Override
	public List<Map<String, Object>> getHousePicByTinyId(Long tinVillageId) {
		// TODO Auto-generated method stub
		 String str_housepic_sql = "SELECT DISTINCT(b.house_pic) as house_pic FROM t_house a LEFT JOIN t_house_style b on a.id=b.house_id AND a.status=0 AND a.tinyvillage_id="+tinVillageId+" where b.house_pic is not null and b.house_pic <> ''";


	        SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(str_housepic_sql);
			List<Map<String, Object>> lst_data = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			return lst_data;
	}
}
