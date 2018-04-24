package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.dynamic.entity.DynamicDto;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.personal.entity.Express;

import java.io.File;
import java.util.Map;

/**
 * 快递信息业务接口
 * Created by liuxiao on 2016/6/15 0015.
 */
public interface ExpressManager extends IManager {
    
    /**
     * 查看所有快递
     *
     * @param  ： Express
     * @return : Result
     *
     */
    public Result queryAllExpressList(Express express);



    /**
     * 查看快递单列表
     * @param  :   Express
     * @return :   Result
     */
    public Result qieryExpressSingleList(PageInfo pageInfo,Express express);


    /**
     * 选中某一个快递单查看详情
     * @param  : Express
     * @return : Result
     */
    public Result queryExpressSingleDetails(Express express);


    /**
     * 新增快递单
     * @param  : Express
     * @return : Result
     */
    public Result addExpressSingle(Express express);


    /**
     * 修改快递信息
     * @param  : Express
     * @return : Result
     */
    public Result updateExpressInfo(Express express);


    /**
     * 分段加载快递信息
     * @param  : Express
     * @return : Result
     */
    public Result piecewiseLoadExpressList(Express express);


    /**
     * 判断是否有未完成快递
     * @param  : Express
     * @return : Result
     * @pageFunction   : 用于快递派送  ---> 安卓端完善订单上面的红点的展示
     */
    
    public Result judgeNotFinishCourier(Express express);

    public Result addExpressSingles(Express express);

    public Map<String, Object> queryExpressList(QueryConditions condition);

    public Express deleteExpress(Long id);
    
    public Express queryExpressById(Long id);
    
    public Express saveExpress(Express express);
    
    /**
     * 录入下一条快递单
     * @param express
     * @return
     */
    public Express queryExpressByNext(Express express);
    
    /**
     * 根据ID删除重复的快递单
     * @param id
     */
    public void delExpressMulit(Long id);

    /**
     * 选择某一个国安侠派送某一个快递
     *
     * @param  ： Express
     * @return : Result
     *
     */
    public Result chooseExpressStaffSendExpress(Express express);

    
    
    /**
     * 判断快递号是否重复
     * @param express
     * @return
     */
    public int queryExpressByExpressNo(Express express);



    Result queryExpressListByCurrentMonth(PageInfo pageInfo,Express express);



	File exportExpressExcel(String month) throws Exception;

	/**
	 * 按城市导出快递信息
	 * @param month
	 * @return
	 * @throws Exception
	 */
	File exportExpressExcelByCity(String express_month,String month) throws Exception;
	
	
	
	/**
	 * crm 查询当前登录国安侠a的 当月的  所有快递信息
	 * @param employee_no
	 * @param pageInfo
	 * @return
	 */
	public Map<String, Object> queryExpressByEmployeeNo(String employee_no,PageInfo pageInfo);
	
	
	
	public Map<String, Object> queryExpressListByStoreId(Long store_id);
	
	/**
	 * 
	 * TODO crm-城市总监、区域经理查询快递
	 * 
	 * 2017年6月20日
	 * @author gaobaolei
	 * @param city_id
	 * @param employee_no
	 * @param role
	 * @return
	 */
	public Map<String, Object> queryExpressList_CSZJ_QYJL(Long city_id,String employee_no,String role,String q_date);
	
	/**
	 * 
	 * TODO  定时任务存储上一个月各个门店的快递代送总数
	 * 2017年6月23日
	 * @author gaobaolei
	 */
	public  void saveBeforeDateExpress_timedTask();
	
	/**
	 * 
	 * TODO crm-从预备数据城市总监、区域经理查询快递
	 * 
	 * 2017年6月23日
	 * @author gaobaolei
	 * @param city_id
	 * @param employee_no
	 * @param role
	 * @return
	 */
	public Map<String, Object> queryExpressList_CSZJ_QYJL_before(Long city_id,String employee_no,String role,String q_date);


	public Map<String, Object> queryExpressList(DynamicDto dynamicDto);
}
