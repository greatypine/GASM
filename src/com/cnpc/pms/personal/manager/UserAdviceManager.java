package com.cnpc.pms.personal.manager;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.personal.entity.Express;
import com.cnpc.pms.personal.entity.UserAdvice;

/**
 * 
 * 类名: UserAdviceManager  
 * 功能描述: 门店用户建议的接口 
 * 作者: 常鹏飞  
 * 时间: 2016-3-1   
 *   
 */
public interface UserAdviceManager extends IManager {
	
	/**
	 * 用户意见
	 * @param  : UserAdvice
	 * @return : Result
	 */
	public Result userOpinion(UserAdvice userAdvice);
	/**
	 * 用户意见分页列表
	 * @param pageInfo
	 * @param userAdvice
	 * @return
	 */
	public Result qieryUserAdviceList(PageInfo pageInfo,UserAdvice userAdvice) ;
	//用户意见添加
	UserAdvice doSaveUserAdvice(UserAdvice userAdvice);
	//根据id查看用户意见
	UserAdvice findUserAdviceById(Long id);
	//用户意见列表
	Map<String,Object> getUserAdviceDataList(QueryConditions conditions);
	
	
	

}
