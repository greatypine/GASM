package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.personal.entity.BeforeDateCustomer;
import com.cnpc.pms.personal.entity.Customer;
import com.cnpc.pms.personal.entity.CustomerData;
import com.cnpc.pms.personal.entity.ViewAddressCustomer;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 客户信息业务接口
 * Created by liuxiao on 2016/6/13 0013.
 */
public interface CustomerManager extends IManager {

    /**
     * 根据导入的Excel文件导入数据
     * @param lst_customer_excel Excel文件集合
     */
    void saveOrUpdateCustomerByExcel(List<File> lst_customer_excel) throws Exception;

    /**
     * 查询客户集合
     * @param customer 客户对象，要查询的条件
     * @return 客户集合
     */
    Result findCustomerList(Customer customer);

    /**
     * 新增或者修改客户
     * @param customer 客户对象
     * @return 结果和完成后的对象
     */
    Customer saveOrUpdateCustomer(Customer customer);

    /**
     * 查找客户对象
     * @param customer 要查找的参数
     * @return 返回客户
     */
    Customer findCustomer(Customer customer);

    /**
     * 查找客户对象以及客户最新的住房信息
     * @param customer 客户对象
     * @return 客户对象以及客户最新的住房信息
     */
    Customer findCustomerInfo(Customer customer);

    /**
     * 保存或者修改客户以及房屋信息
     * @param customer 客户对象
     * @return 保存后的对象
     */
    Customer saveOrUpdateCustomerAndHouse(Customer customer);

    /**
     * 保存或者修改客户以及房屋信息
     * @return 保存后的对象
     */
    Customer findCustomerIdAndHouseId(Long customer_id,Long house_id);

    List<Customer> findCustomerByHouseId(Long house_id);

    Customer saveCustomerAndAddress(Customer customer,CustomerData customerData);
    
    Customer findCustomerById(Long id);
    

    Map<String,Object> queryAchievements(QueryConditions queryConditions);
    
    public Customer saveOrUpdateCustomerRelations(Customer customer);
    
    public Customer saveCustomerAndRelation(Customer customer);
    
    
    public Result findCustomerListByNamePhone(Customer customer);
    
    /**
     * 
     * TODO  根据customer_id、house_id 查询用户画像
     * 2017年4月13日
     * @author gaobaolei
     * @param customer_id
     * @param house_id
     * @param phone
     * @return
     */
    public Customer findCustomerByCustomerIdAndHouseId_crm(Long customer_id, Long house_id,String phone) ;
    
    /**
     * 
     * TODO 根据电话查询用户画像 
     * 2017年4月13日
     * @author gaobaolei
     * @param customer_id
     * @param house_id
     * @param phone
     * @return
     */
    public Customer findCustomerByCustomerPhone_crm(Long customer_id, Long house_id,String phone);
    
    /**
     * 
     * TODO 获取国安侠负责的用户量 
     * 2017年4月14日
     * @author gaobaolei
     * @param employeeNo
     * @return
     */
    public Map<String, Object> getCustomerCount(String employeeNo);
    
    /**
     * 
     * TODO 获取片区总户数和 服务户数
     * 2017年4月14日
     * @author gaobaolei
     * @param employee_no
     * @return
     */
    public Map<String,Object> getHouseCountAndServiceHouse(String employee_no,Long area_id);
    
    /**
     * 
     * TODO 根据电话检查用户画像 
     * 2017年5月4日
     * @author gaobaolei
     * @param phone
     * @return
     */
    public Map<String, Object> checkCustomerByPhone(String phone);
    
    /**
     * 
     * TODO 查询当前门店今年的拜访记录和用户画像 
     * 2017年5月12日
     * @author gaobaolei
     * @param storeId
     * @return
     */
    public Map<String,Object> queryRelationAndCustomerOfYear(Long storeId);
    
    /**
     * 
     * TODO 查询当前门店的所有片区的拜访记录和用户画像
     * 2017年5月12日
     * @author gaobaolei
     * @param storeId
     * @return
     */
    public Map<String, Object> queryRelationAndCustomerOfArea(Long storeId,String query_date);
    
    /**
     * 
     * TODO 查询门店当月的国安侠用户画像 
     * 2017年5月15日
     * @author gaobaolei
     * @param storeId
     * @return
     */
    public Map<String, Object> queryCustomerOfEmployee(Long storeId,String query_date);
    
    void updateCustomerSortById(String ids);
    
    /**
     * 
     * TODO crm-运营经理查询区域内门店当年的用户画像
     * 2017年6月16日
     * @author gaobaolei
     * @param employeeId
     * @return
     */
    public Map<String,Object> queryRelationAndCustomerOfYear_CSZJ_QYJL(String employeeId,Long city_id,String role);
    
    /**
     * 
     * TODO crm-运营经理查询管辖区域内的 各个门店的用户画像
     * 2017年6月16日
     * @author gaobaolei
     * @param storeId
     * @param query_date
     * @return
     */
    public Map<String, Object> queryRelationAndCustomerOfStore_CSZJ_QYJL(String employeeId,Long city_id,String role,String q_date);
    
    /**
     * 
     * TODO crm-运营经理查询管辖区域内的各个国安侠的用户画像 
     * 2017年6月19日
     * @author gaobaolei
     * @param employeeId
     * @return
     */
    public Map<String, Object> queryRelationAndCustomerOfEmployee_CSZJ_QYJL(String employeeId,Long city_id,String queryDate,String role);
    
    /**
     * 
     * TODO 定时任务存储上一个月各个门店的用户画像总数
     * 2017年6月22日
     * @author gaobaolei
     * @param customerj
     */
    public  void saveBeforeDateCustomer_timedTask();
    
    
    
    
    /**
     * 
     * TODO crm-运营经理查询区域内门店当年的用户画像
     * 2017年6月23日
     * @author gaobaolei
     * @param employeeId
     * @return
     */
    public Map<String,Object> queryRelationAndCustomerOfYear_CSZJ_QYJL_before(String employeeId,Long city_id,String role);
    
    
    /**
     * 
     * TODO crm-从预备数据运营经理查询管辖区域内的 各个门店的用户画像
     * 2017年6月23日
     * @author gaobaolei
     * @param storeId
     * @param query_date
     * @return
     */
    public Map<String, Object> queryRelationAndCustomerOfStore_CSZJ_QYJL_before(String employeeId,Long city_id,String role,String q_date);
    
    /**
     * 
     * TODO  获取店长
     * 2017年9月25日
     * @author gaobaolei
     * @param employeeId
     * @return
     */
    public Map<String, Object> getStoreKepper(String employeeId);
    
    /**
     * 
     * TODO 查询小区内所有用户画像 
     * 2018年1月9日
     * @author gaobaolei
     * @param idsStr
     * @return
     */
    public Map<String,Object> selectCustomerByTinyVillage(String idsStr);
    
    /**
     * 
     * TODO 直接修改客户画像的家庭地址 
     * 2018年1月9日
     * @author gaobaolei
     * @param addressCustomer
     * @return
     */
    public Map<String, Object>  editCustomerAddress(ViewAddressCustomer addressCustomer);
    
   
}