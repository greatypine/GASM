package com.cnpc.pms.personal.manager.impl;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Spring;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.configuration.beanutils.BeanFactory;

import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.ISort;
import com.cnpc.pms.base.paging.SortFactory;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.UserGroup;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserGroupManager;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.personal.entity.DistCity;
import com.cnpc.pms.personal.entity.DistCityCode;
import com.cnpc.pms.personal.entity.HumanVacation;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.manager.DistCityCodeManager;
import com.cnpc.pms.personal.manager.DistCityManager;
import com.cnpc.pms.personal.manager.HumanVacationManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.utils.ActProcessEngine;

public class HumanVacationManagerImpl extends BizBaseCommonManager implements HumanVacationManager {
	
	//申请
	@Override
	public HumanVacation saveHumanVacation(HumanVacation humanVacation) {
		ActProcessEngine actProcessEngine = (ActProcessEngine) SpringHelper.getBean("actProcessEngine");
		RuntimeService runtimeService = actProcessEngine.getRuntimeService();
		TaskService taskService=actProcessEngine.getTaskService();
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("oneVacationProcess");
		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
		System.out.println("员工填写申请流程:task 1 -------------------------"+task.getName());
		
		//指定审批人为当前门店店长 
		User user = queryEmployeeNoByStoreNo(humanVacation.getStore_id());
		if(user!=null&&user.getEmployeeId()!=null) {
			Map<String, Object> saveMap = new HashMap<String, Object>();
			
			saveMap.put("employee_no", user.getEmployeeId());
			humanVacation.setApp_name(user.getName());//记录审批人
			
			String re_content=humanVacation.getRe_content();
			if(re_content!=null&&re_content.length()>0) {
				re_content=humanVacation.getRe_content();
			}else {
				re_content=humanVacation.getEmployee_name()+":申请";
			}
			taskService.addComment(task.getId(), pi.getId(),"申请" ,re_content);
			taskService.complete(task.getId(),saveMap);
		}
		
		humanVacation.setProcess_status(1);//状态 审批中 
		humanVacation.setProcessInstanceId(pi.getId());
		preSaveObject(humanVacation);
		this.saveObject(humanVacation);
		return humanVacation;
	}
	
	
	//重新申请
	@Override
	public HumanVacation saveHumanVacation2(HumanVacation humanVacation) {
		ActProcessEngine actProcessEngine = (ActProcessEngine) SpringHelper.getBean("actProcessEngine");
		TaskService taskService=actProcessEngine.getTaskService();
		Task task = taskService.createTaskQuery().processInstanceId(humanVacation.getProcessInstanceId())
				.singleResult();
		System.out.println("员工重新填写申请流程:task 1 -------------------------"+task.getName());
	
		//指定审批人为当前门店店长 
		User user = queryEmployeeNoByStoreNo(humanVacation.getStore_id());
		if(user!=null&&user.getEmployeeId()!=null) {
			Map<String, Object> saveMap = new HashMap<String, Object>();
			
			saveMap.put("employee_no", user.getEmployeeId());
			humanVacation.setApp_name(user.getName());//记录审批人
			
			String re_content=humanVacation.getRe_content();
			if(re_content!=null&&re_content.length()>0) {
				re_content=humanVacation.getRe_content();
			}else {
				re_content=humanVacation.getEmployee_name()+":重新申请";
			}
			taskService.addComment(task.getId(), humanVacation.getProcessInstanceId(),"重新申请" ,re_content);
			taskService.complete(task.getId(),saveMap);
		}
		
		HumanVacation saveHumanVacation2 = queryHumanVacationInfo(humanVacation.getId());
		saveHumanVacation2.setTopostdate(humanVacation.getTopostdate());
		saveHumanVacation2.setWork_date(humanVacation.getWork_date());
		saveHumanVacation2.setStart_date(humanVacation.getStart_date());
		saveHumanVacation2.setEnd_date(humanVacation.getEnd_date());
		saveHumanVacation2.setVacation_type(humanVacation.getVacation_type());
		saveHumanVacation2.setVacation_reason(humanVacation.getVacation_reason());
		saveHumanVacation2.setWork_relay(humanVacation.getWork_relay());
		saveHumanVacation2.setApp_name(humanVacation.getApp_name());

		
		saveHumanVacation2.setProcess_status(1);//状态 审批中 
		preSaveObject(saveHumanVacation2);
		this.saveObject(saveHumanVacation2);
		return humanVacation;
	}
	
	
	//店长审批
	@Override
	public HumanVacation update_storekeeper_Audit(HumanVacation humanVacation) {
		ActProcessEngine actProcessEngine = (ActProcessEngine) SpringHelper.getBean("actProcessEngine");
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		TaskService taskService=actProcessEngine.getTaskService();
		String manager = humanVacation.getEmployee_no();
		Task task = taskService.createTaskQuery().processInstanceId(humanVacation.getProcessInstanceId())
				.taskAssignee(manager)
				.singleResult();
		System.out.println("店长审批: task 2-------------------------"+task.getName());
		
		//指定审批人为当前城市HR组  
		String group_code = queryHRGroupCodeByStoreNo(humanVacation.getStore_id());
		String hrNames = queryHrNamesByStoreNo(humanVacation.getStore_id());
		HumanVacation updateStatusHumanVacation =null;
		if(group_code!=null&&group_code.length()>0) {
			Map<String, Object> varMaps = new HashMap<String, Object>();
			String outname="店长通过";
			varMaps.put("outname", outname);
			varMaps.put("group_code", group_code);
			
			humanVacation.setApp_name(hrNames);//记录下级审批人姓名
			
			String person_name = "";
			if(humanVacation.getEmployee_name()!=null&&humanVacation.getEmployee_name().length()>0) {
				person_name=humanVacation.getEmployee_name();
			}else {
				person_name=userManager.getCurrentUserDTO().getName();
			}
			
			String re_content=humanVacation.getRe_content();
			if(re_content!=null&&re_content.length()>0) {
				re_content=person_name+":"+humanVacation.getRe_content();
			}else {
				re_content=person_name+":通过";
			}
			taskService.addComment(task.getId(), humanVacation.getProcessInstanceId(),"店长通过" ,re_content);
			taskService.complete(task.getId(), varMaps);
			
			
			updateStatusHumanVacation = queryHumanVacationInfo(humanVacation.getId());
			updateStatusHumanVacation.setApp_name(humanVacation.getApp_name());
			preSaveObject(updateStatusHumanVacation);
			this.saveObject(updateStatusHumanVacation);
		}
		return updateStatusHumanVacation;
	}
	
	
	
	//店长驳回
	@Override
	public HumanVacation update_storekeeper_Audit_Re(HumanVacation humanVacation) {
		ActProcessEngine actProcessEngine = (ActProcessEngine) SpringHelper.getBean("actProcessEngine");
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		TaskService taskService=actProcessEngine.getTaskService();
		String manager = humanVacation.getEmployee_no();
		Task task = taskService.createTaskQuery().processInstanceId(humanVacation.getProcessInstanceId())
				.taskAssignee(manager)
				.singleResult();
		System.out.println("店长驳回: task 2-------------------------"+task.getName());
		Map<String, Object> varMaps = new HashMap<String, Object>();
		String outname="店长驳回";
		varMaps.put("outname", outname);
		
		String re_content=humanVacation.getRe_content();
		
		String person_name = "";
		if(humanVacation.getEmployee_name()!=null&&humanVacation.getEmployee_name().length()>0) {
			person_name=humanVacation.getEmployee_name();
		}else {
			person_name=userManager.getCurrentUserDTO().getName();
		}
		
		if(re_content!=null&&re_content.length()>0) {
			re_content=person_name+":"+humanVacation.getRe_content();
		}else {
			re_content=person_name+":驳回";
		}
		taskService.addComment(task.getId(), humanVacation.getProcessInstanceId(),"店长驳回" ,re_content);
		taskService.complete(task.getId(), varMaps);
		
		
		HumanVacation updateStatusHumanVacation = queryHumanVacationInfo(humanVacation.getId());
		updateStatusHumanVacation.setProcess_status(3);
		updateStatusHumanVacation.setApp_name(null);
		preSaveObject(updateStatusHumanVacation);
		this.saveObject(updateStatusHumanVacation);
		return updateStatusHumanVacation;
	}
		
	
	
	
	//HR审批 
	@Override
	public HumanVacation update_hr_Audit(HumanVacation humanVacation) {
		ActProcessEngine actProcessEngine = (ActProcessEngine) SpringHelper.getBean("actProcessEngine");
		TaskService taskService=actProcessEngine.getTaskService();
		String manager = "YGGXJSZ";
		Task task = taskService.createTaskQuery().processInstanceId(humanVacation.getProcessInstanceId())
				.taskAssignee(manager)
				.singleResult();
		System.out.println("HR审批: task 3 -------------------------"+task.getName());
		Map<String, Object> varMaps = new HashMap<String, Object>();
		String outname="员工关系通过";
		varMaps.put("outname", outname);
		
		
		String re_content=humanVacation.getRe_content();
		if(re_content!=null&&re_content.length()>0) {
			re_content=humanVacation.getEmployee_name()+":"+humanVacation.getRe_content();
		}else {
			re_content=humanVacation.getEmployee_name()+":通过";
		}
		taskService.addComment(task.getId(), humanVacation.getProcessInstanceId(),"HR通过" ,re_content);
		taskService.complete(task.getId(), varMaps);
		
		HumanVacation updateStatusHumanVacation = queryHumanVacationInfo(humanVacation.getId());
		updateStatusHumanVacation.setProcess_status(2);
		updateStatusHumanVacation.setApp_name(null);
		preSaveObject(updateStatusHumanVacation);
		this.saveObject(updateStatusHumanVacation);
		return updateStatusHumanVacation;
	}
	
	
	//HR驳回
	@Override
	public HumanVacation update_hr_Audit_Re(HumanVacation humanVacation) {
		ActProcessEngine actProcessEngine = (ActProcessEngine) SpringHelper.getBean("actProcessEngine");
		TaskService taskService=actProcessEngine.getTaskService();
		String manager = "YGGXJSZ";
		Task task = taskService.createTaskQuery().processInstanceId(humanVacation.getProcessInstanceId())
				.taskAssignee(manager)
				.singleResult();
		System.out.println("HR驳回: task 3 -------------------------"+task.getName());
		Map<String, Object> varMaps = new HashMap<String, Object>();
		String outname="员工关系驳回";
		varMaps.put("outname", outname);
		
		
		String re_content=humanVacation.getRe_content();
		if(re_content!=null&&re_content.length()>0) {
			re_content=humanVacation.getEmployee_name()+":"+humanVacation.getRe_content();
		}else {
			re_content=humanVacation.getEmployee_name()+":驳回";
		}
		taskService.addComment(task.getId(), humanVacation.getProcessInstanceId(),"HR驳回" ,re_content);
		taskService.complete(task.getId(), varMaps);
		
		HumanVacation updateStatusHumanVacation = queryHumanVacationInfo(humanVacation.getId());
		updateStatusHumanVacation.setProcess_status(3);
		updateStatusHumanVacation.setApp_name(null);
		preSaveObject(updateStatusHumanVacation);
		this.saveObject(updateStatusHumanVacation);
		return updateStatusHumanVacation;
	}
	
	
	@Override
	public HumanVacation queryHumanVacationInfo(Long id) {
		HumanVacation humanVacation = (HumanVacation) this.getObject(id);
		String processInstanceId=humanVacation.getProcessInstanceId();
		List<Comment> comments = findCommentByProcessId(processInstanceId);
		humanVacation.setProcesslog(comments);
		return humanVacation;
	}
	
	 
	@Override
	public List<Comment> findCommentByProcessId(String processId){
		ActProcessEngine actProcessEngine = (ActProcessEngine) SpringHelper.getBean("actProcessEngine");
		TaskService taskService=actProcessEngine.getTaskService();
		HistoryService historyService=actProcessEngine.getHistoryService();
		
		List<Comment> list = new ArrayList<Comment>();
		//使用当前的任务ID，查询当前流程对应的历史任务ID
		//获取流程实例ID
		String processInstanceId = processId;
		//使用流程实例ID，查询历史任务，获取历史任务对应的每个任务ID
		List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery()//历史任务表查询
		.processInstanceId(processInstanceId)//使用流程实例ID查询
		.list();
		//遍历集合，获取每个任务ID
		if(htiList!=null && htiList.size()>0){
		for(HistoricTaskInstance hti:htiList){
		//任务ID
		String htaskId = hti.getId();
		//获取批注信息
		List<Comment> taskList = taskService.getTaskComments(htaskId);//对用历史完成后的任务ID
		list.addAll(taskList);
		}
		}
		list = taskService.getProcessInstanceComments(processInstanceId);


		for(Comment com:list){
			System.out.println("---------------------------------------");
			System.out.println("ID:"+com.getId());
			System.out.println("Message:"+com.getFullMessage());
			System.out.println("TaskId:"+com.getTaskId());
			System.out.println("ProcessInstanceId:"+com.getProcessInstanceId());
			System.out.println("---------------------------------------");

		}
		
		
		return list;
	}
	
	
	
	
	
	//测试自己 模拟店长 审批方法  
	@Override
	public HumanVacation updateHumanVacation(HumanVacation humanVacation) {
		/*humanVacation.setProcess_status(1);//状态 审批中 
		preSaveObject(humanVacation);
		this.saveObject(humanVacation);*/
		
		ActProcessEngine actProcessEngine = (ActProcessEngine) SpringHelper.getBean("actProcessEngine");
		TaskService taskService=actProcessEngine.getTaskService();
		Task task = taskService.createTaskQuery().processInstanceId(humanVacation.getEmployee_no()).singleResult();
		System.out.println("店长审批: task 2-------------------------"+task.getName());
		
		
		Map<String, Object> varMaps = new HashMap<String, Object>();
		varMaps.put("", "");
		taskService.complete(task.getId(), varMaps);
		
		return humanVacation;
	}
	
	
	
	
	
	
	
	/**
	 * 请假列表 
	 */
	@Override
	public Map<String, Object> queryHumanVacationList(QueryConditions condition){
		Map<String,Object> returnMap = new java.util.HashMap<String, Object>();
		PageInfo pageInfo = condition.getPageinfo();
		String vacation_type = null;
		for(Map<String, Object> map : condition.getConditions()){
			if("vacation_type".equals(map.get("key"))&&map.get("value")!=null){//查询条件
				vacation_type = map.get("value").toString();
			}
		}
		List<?> lst_data = null;
		FSP fsp = new FSP();
		fsp.setSort(SortFactory.createSort("id", ISort.DESC));
		StringBuffer sbfCondition = new StringBuffer(); 
		
		//这里根据当前登录人 过滤 显示所能看到的数据 
		sbfCondition.append(" 1=1 ");
		
		
		//根据登录角色过滤显示
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		String userGroupCode = userManager.getCurrentUserDTO().getUsergroup().getCode();
		if(userGroupCode!=null&&userGroupCode.length()>0) {
			if(userGroupCode.equals("GAX")) {
				sbfCondition.append(" and employee_no='"+userManager.getCurrentUserDTO().getEmployeeId()+"'");	
			}
			if(userGroupCode.equals("DZ")) {
				sbfCondition.append(" and store_id ="+userManager.getCurrentUserDTO().getStore_id());
			}
		}else {
			sbfCondition.append(" and 1=0 ");
		}
		
		
		if(vacation_type!=null&&vacation_type.length()>0) {
			sbfCondition.append(" and vacation_type = '"+vacation_type+"' ");
		}
		
		IFilter iFilter =FilterFactory.getSimpleFilter(sbfCondition.toString());
		fsp.setPage(pageInfo);
		fsp.setUserFilter(iFilter);
		lst_data = this.getList(fsp);
		returnMap.put("pageinfo", pageInfo);
		returnMap.put("header", "");
		returnMap.put("data", lst_data);
		return returnMap;
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/*@Override
	public List<Map<String, Object>> queryMsgNoticeList(Long num){
		List<Map<String, Object>> retList = new ArrayList<Map<String,Object>>();
		MsgNoticeDao msgNoticeDao = (MsgNoticeDao) SpringHelper.getBean(MsgNoticeDao.class.getName());
		UserManager  userManager = (UserManager) SpringHelper.getBean("userManager");
		UserDTO userDTO = userManager.getCurrentUserDTO();
		if(num!=null){
			//查询通知表t_notice t_notice_receiver
			retList = msgNoticeDao.queryNoticeList(num,userDTO.getEmployeeId(),userDTO.getStore_id());
		}else{
			retList = msgNoticeDao.queryNoticeList(null,userDTO.getEmployeeId(),userDTO.getStore_id());
		}
		
		return retList;
	}
	
	
	@Override
	public MsgNotice queryMsgNoticeById(Long id){
		MsgNotice msgNotice = (MsgNotice) this.getObject(id); 
		return msgNotice;
	}
	
	
	
	@Override
	public MsgNotice saveMsgNotice(MsgNotice msgNotice) {
		preSaveObject(msgNotice);
		this.saveObject(msgNotice);
		return msgNotice;
	}
	
	@Override
	public void updateNoticeReadByNo(Long id){
		MsgNotice msgNotice = (MsgNotice) this.getObject(id);
		MsgNoticeDao msgNoticeDao = (MsgNoticeDao) SpringHelper.getBean(MsgNoticeDao.class.getName());
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		String noticeNo = msgNotice.getNoticeNo();
		String employee_no = userManager.getCurrentUserDTO().getEmployeeId();
		msgNoticeDao.updateNoticeReadByNo(noticeNo, employee_no);
	}*/
	
	
	/*@Override
	public MsgNotice updateMsgNotice(MsgNotice msgNotice) {
		MsgNotice updateMsgNotice = null;
		if(msgNotice!=null&&msgNotice.getId()!=null){
			updateMsgNotice=(MsgNotice) this.getObject(msgNotice.getId());
			UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
			updateMsgNotice.setTitle(msgNotice.getTitle());
			updateMsgNotice.setContent(msgNotice.getContent());
			updateMsgNotice.setNotice_date(msgNotice.getNotice_date());
			updateMsgNotice.setNotice_user(userManager.getCurrentUserDTO().getName());
			preSaveObject(updateMsgNotice);
			this.saveObject(updateMsgNotice);
		}
		return updateMsgNotice;
	}*/

	
	
	
	protected void preSaveObject(Object o) {
		if (o instanceof DataEntity) {
			User sessionUser = null;
			if (null != SessionManager.getUserSession()
					&& null != SessionManager.getUserSession().getSessionData()) {
				sessionUser = (User) SessionManager.getUserSession()
						.getSessionData().get("user");
			}
			DataEntity dataEntity = (DataEntity) o;
			java.util.Date date = new java.util.Date();
			java.sql.Date sdate = new java.sql.Date(date.getTime());
			// insert处理时添加创建人和创建时间
			if (null == dataEntity.getCreate_time()) {
				dataEntity.setCreate_time(sdate);
				if (null != sessionUser) {
					dataEntity.setCreate_user(sessionUser.getCode());
					dataEntity.setCreate_user_id(sessionUser.getId());
				}
			}
			dataEntity.setUpdate_time(sdate);
			if (null != sessionUser) {
				dataEntity.setUpdate_user(sessionUser.getCode());
				dataEntity.setUpdate_user_id(sessionUser.getId());
			}
		}
	}

	
	
	/**
	 * 根据门店ID查找店长 
	 * @param storeId
	 * @return
	 */
	public User queryEmployeeNoByStoreNo(Long storeId) {
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		Store store = storeManager.findStore(storeId);
		if(store!=null&&store.getSkid()!=null) {
			UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
			User user = (User) userManager.getObject(store.getSkid());
			if(user!=null&&user.getEmployeeId()!=null) {
				return user;
			}
		}
		return null;
	}
	/**
	 * 根据门店ID查找城市HR组 (YGGXJSZ)员工关系角色组 
	 */
	public String queryHRGroupCodeByStoreNo(Long storeId) {
		//处理组code    城市_组Code
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		DistCityCodeManager distCityCodeManager = (DistCityCodeManager) SpringHelper.getBean("distCityCodeManager");
		String rtCode="";
		Store store = storeManager.findStore(storeId);
		if(store!=null&&store.getCityNo()!=null) {
			String cityNo = store.getCityNo();
			DistCityCode distCityCode = distCityCodeManager.queryDistCityCodeByCityNo(cityNo);
			rtCode = distCityCode.getCitycode()+"_YGGXJSZ";
		}
		return "YGGXJSZ";
	}
	
	public String queryHrNamesByStoreNo(Long storeId) {
		UserGroupManager userGroupManager = (UserGroupManager) SpringHelper.getBean("userGroupManager");
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		DistCityManager distCityManager = (DistCityManager) SpringHelper.getBean("distCityManager");
		IFilter iFilter =FilterFactory.getSimpleFilter(" code = 'YGGXJSZ' ");
		List<UserGroup> userGroups = (List<UserGroup>) userGroupManager.getList(iFilter);
		Store store = storeManager.findStore(storeId);
		String app_names="";
		if(userGroups!=null) {
			UserGroup userGroup = userGroups.get(0);
			IFilter userIFilter =FilterFactory.getSimpleFilter("pk_usergroup ="+userGroup.getId());
			List<User> users = (List<User>) userManager.getList(userIFilter);
			//取得门店所在城市的user的name 返回 
			for(User user :users) {
				Long userid=user.getId();
				String cityName=store.getCityName();
				List<DistCity> distCities = distCityManager.queryDistCityByUserIdCity(userid, cityName);
				if(distCities!=null&&distCities.size()>0) {
					app_names+=user.getName()+",";
				}
			}
		}
		return app_names;
	}
	
}
