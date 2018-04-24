package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.personal.entity.Attachment;
import com.cnpc.pms.personal.entity.Relation;

import java.util.List;
import java.util.Map;

/**
 * 国安侠客户关系拜访记录
 */
public interface RelationManager extends IManager {
	
	/**
	 * pc查询 用户拜访记录 列表 
	 */
	public Map<String, Object> queryRelationList(QueryConditions queryConditions);

    Map<String,Object> queryRelation(QueryConditions queryConditions);

    Relation findRelationById(Long id);
    
    public List<Relation> findRelationsBycustomerId(Long customer_id);
    
    public Result queryRelationListByUserCard(Long store_id,String employee_no,String cardtype);
    
    public Result queryRelationListForApp(Long store_id,String employee_no,String cardtype,String name,String mobilephone);
    
    /**
     * 
     * TODO 查询最新的拜访记录 （crm）
     * 2017年4月12日
     * @author gaobaolei
     * @param employeeNo
     * @return
     */
    public Map<String, Object> findRelation_newestList_crm(String employeeNo,Integer pageSize,Integer currentPage,Long area_id);
    
    /**
     * 
     * TODO 查找拜访记录柱状图数据 
     * 2017年4月12日
     * @author gaobaolei
     * @param employeeNo
     * @return
     */
    public Map<String, Object> findRelation_chart_crm(String employeeNo,Long area_id);
    
   
   /**
    *  
    * TODO  查询用户中心信息拜访记录
    * 2017年4月13日
    * @author gaobaolei
    * @param employeeNo
    * @param pageSize
    * @param currentPage
    * @return
    */
    public Map<String, Object> findRelation_employee_crm(String employeeNo,Integer pageSize,Integer currentPage);
    
    /**
     * 
     * TODO 查询客户的被拜访记录
     * 2017年4月14日
     * @author gaobaolei
     * @param customer_id
     * @param pageSize
     * @param currentPage
     * @return
     */
    public Map<String, Object> findRelation_customer_crm(Long customer_id,Integer pageSize,Integer currentPage);
    /**
     * 
     * TODO 删除拜访记录
     * 2017年5月3日
     * @author gaobaolei
     * @param id
     * @return
     */
    public Relation delRelation(int id);
    
    /**
     * 
     * TODO 查询门店所有员工的拜访记录
     * 2017年5月12日
     * @author gaobaolei
     * @param storeId
     * @return
     */
    public Map<String, Object> getRelationOfEmployee(Long storeId,String query_date);
    
    /**
     * 
     * TODO crm-运营经理查询管辖门店各个国安侠的拜访记录 
     * 2017年6月19日
     * @author gaobaolei
     * @param employeeId
     * @param query_date
     * @return
     */
    public Map<String, Object> getRelationOfEmployee_CSZJ_QYJL(String  employeeId,Long cityId,String query_date,String role);
    
    /**
     * 
     * TODO 定时任务存储上一个月各个门店的拜访记录总数
     * 2017年6月23日
     * @author gaobaolei
     */
    public void saveBeforeDateRelation_timedTask();
    
}
