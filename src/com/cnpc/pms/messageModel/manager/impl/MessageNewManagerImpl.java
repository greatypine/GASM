/**
 * gaobaolei
 */
package com.cnpc.pms.messageModel.manager.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cxf.message.MessageUtils;
import org.springframework.beans.BeanUtils;

import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.inter.common.CodeEnum;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.messageModel.dao.MessageNewDao;
import com.cnpc.pms.messageModel.entity.Message;
import com.cnpc.pms.messageModel.entity.MessageSendUtil;
import com.cnpc.pms.messageModel.entity.MessageTemplate;
import com.cnpc.pms.messageModel.manager.MessageNewManager;
import com.cnpc.pms.personal.entity.Customer;
import com.cnpc.pms.personal.entity.Relation;
import com.cnpc.pms.personal.entity.WorkInfo;
import com.cnpc.pms.personal.manager.CustomerManager;
import com.cnpc.pms.personal.manager.RelationManager;
import com.cnpc.pms.platform.entity.Order;
import com.cnpc.pms.slice.dao.AreaDao;
import com.cnpc.pms.slice.dto.AreaDto;
import com.cnpc.pms.utils.PropertiesValueUtil;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.ibm.db2.jcc.am.id;
import com.ibm.db2.jcc.am.me;

/**
 * @author gaobaolei
 *
 */
public class MessageNewManagerImpl extends BizBaseCommonManager implements MessageNewManager{

	
	@Override
	public List<Message> getMessageByTemplateCode(String code) {
		List<Message> list = (List<Message>)this.getList(FilterFactory.getSimpleFilter("code", code));
		return list;
	}

	
	@Override
	public Map<String, Object> queryApproveMessage(QueryConditions queryConditions) {
		MessageNewDao messageNewDao = (MessageNewDao)SpringHelper.getBean(MessageNewDao.class.getName());
		List<AreaDto> list  = new ArrayList<AreaDto>();
		//查询的数据条件
        StringBuilder sb_where = new StringBuilder();
        //分页对象
        PageInfo obj_page = queryConditions.getPageinfo();
        //返回的对象，包含数据集合、分页对象等
        Map<String, Object> map_result = new HashMap<String, Object>();
       
        for (Map<String, Object> map_where : queryConditions.getConditions()) {
        	/*if ("templateCode".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
                sb_where.append(" and tm.templateCode = ").append(map_where.get("value"));
            }else */
            	if ("title".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
                sb_where.append(" and  title like '").append(map_where.get("value")).append("'");
            }

        }
		try {
			 map_result.put("data",messageNewDao.queryApproveMessage(sb_where.toString(), obj_page));
		} catch (Exception e) {
			e.printStackTrace();
		}
		map_result.put("pageinfo", obj_page);
        
        return map_result;
	}

	
	@Override
	public Map<String, Object> queryNoApproveMessage(QueryConditions queryConditions) {
		MessageNewDao messageNewDao = (MessageNewDao)SpringHelper.getBean(MessageNewDao.class.getName());
		//查询的数据条件
        StringBuilder sb_where = new StringBuilder();
        //分页对象
        PageInfo obj_page = queryConditions.getPageinfo();
        //返回的对象，包含数据集合、分页对象等
        Map<String, Object> map_result = new HashMap<String, Object>();
       
        for (Map<String, Object> map_where : queryConditions.getConditions()) {
        	/*if ("templateCode".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
                sb_where.append(" and tm.templateCode = ").append(map_where.get("value"));
            }else */
            	if ("title".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
                sb_where.append(" and title  like '").append(map_where.get("value")).append("'");
            }

        }
		try {
			 map_result.put("data",messageNewDao.queryApproveMessage(sb_where.toString(), obj_page));
		} catch (Exception e) {
			e.printStackTrace();
		}
		map_result.put("pageinfo", obj_page);
        
        return map_result;
	}


	
	@Override
	public Map<String, Object> saveApproveMessage(Message message) {
		UserManager uManager = (UserManager)SpringHelper.getBean("userManager");
		UserDTO userDTO = uManager.getCurrentUserDTO();	
		MessageNewDao messageNewDao = (MessageNewDao)SpringHelper.getBean(MessageNewDao.class.getName());
		User receiveUser = message.getReceiveUser();
		String city = receiveUser.getCode();
		String store = receiveUser.getName();
		String zw  = receiveUser.getZw();
		String employee = receiveUser.getEmployeeId();
		Long sendId = Long.parseLong(message.getSendId().toString());
		Map<String,Object> param = new HashMap<String,Object>();
		
		if(employee==null||"".equals(employee)){//全部员工
			if(city!=null&&!"".equals(city)){
				StringBuilder citySb = new StringBuilder();
				String[] cityArr = city.split(",");
                for(String cityName:cityArr){
                	citySb.append(",'"+cityName+"'");
                }
                citySb = citySb.deleteCharAt(0);
                param.put("city", "("+citySb.toString()+")");
                
			}
			
			if(store!=null&&!"".equals(store)){
				StringBuilder storeSb = new StringBuilder();
				String[] storeArr = store.split(",");
                for(String storeName:storeArr){
                	storeSb.append(","+storeName+"");
                }
                storeSb = storeSb.deleteCharAt(0);
                param.put("store", "("+storeSb.toString()+")");
			}
			
			if(zw!=null&&!"".equals(zw)){
				StringBuilder zwSb = new StringBuilder();
				String[] zwArr = zw.split(",");
                for(String zwName:zwArr){
                	zwSb.append(",'"+zwName+"'");
                }
                zwSb = zwSb.deleteCharAt(0);
                param.put("zw", "("+zwSb.toString()+")");
			}
			
			if((city==null||"".equals(city))&&(store==null||"".equals(store))&&(zw==null||"".equals(zw))){
				
				List<Map<String, Object>> citylist = messageNewDao.queryCityInfo(sendId);
            	if(citylist==null||citylist.size()==0){
            		
            		return null;
            	}else{
            		StringBuilder citySb = new StringBuilder();
            		for(int i=0;i<citylist.size();i++){
            			citySb.append(",'"+citylist.get(i).get("citycode")+"'");
            		}
            		 citySb = citySb.deleteCharAt(0);
            		 param.put("city", "("+citySb.toString()+")");
            	}
			}
		}else{
			StringBuilder employeeSb = new StringBuilder();
			String[] employeeArr = employee.split(",");
            for(String employeeName:employeeArr){
            	employeeSb.append(",'"+employeeName+"'");
            }
            employeeSb = employeeSb.deleteCharAt(0);
            param.put("employee", "("+employeeSb.toString()+")");
		}
		
		List<Map<String, Object>> receiveUserList = messageNewDao.getReceiveEmployee(param);
		boolean result=false;
		Map<String, Object> map = new HashMap<String,Object>();
		
		try {
			User user  = null;
			Object os = "";
			Object client_id = "";
			Object token="";
			Object employeeId ="";
			for(int i=0;i<receiveUserList.size();i++){
				
				employeeId = receiveUserList.get(i).get("employeeId");
				token = receiveUserList.get(i).get("token");
				client_id = receiveUserList.get(i).get("client_id");
				os = receiveUserList.get(i).get("os");
				if(employeeId==null||token==null||client_id==null||os==null){
					continue;
				}
				
				Message message2 = new Message();
				message2.setTitle(message.getTitle());
				message2.setContent(message.getContent());
				message2.setSendId(userDTO.getEmployeeId());
				message2.setPk_id(-100L);
				message2.setType("other_notice");
				message2.setJump_path("message_notice");
				//message2.setTemplateCode(message.getTemplateCode());
				String code = String.valueOf(System.currentTimeMillis());
				message2.setMessageCode(code);
				message2.setReceiveId(employeeId.toString());
				message2.setType(message.getType());
				message2.setIsRead(0);
				preObject(message2);
				this.saveObject(message2);
				
				user = new User();
				user.setOs(os.toString());
				user.setClient_id(client_id.toString());
				user.setToken(token.toString());
				result = MessageSendUtil.getInstance().pushMessage(user, message2);
			}
			
			map.put("result", result);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", result);
			return  map;
		}
		
		return map;
	}

	
	
	@Override
	public Map<String, Object> getApproveMessageDetailInfo(Message message) {
		Map<String, Object> result = new HashMap<String,Object>();
		MessageNewDao messageNewDao = (MessageNewDao)SpringHelper.getBean(MessageNewDao.class.getName());
		List<Map<String, Object>> list = messageNewDao.getApproveMessageDetailInfo(message);
		
		result.put("messageDetai", list.get(0));
		return  result;
	}


	
	@Override
	public Map<String, Object> getMessageCallBack() {
		Map<String, Object> result = new HashMap<String,Object>();
		MessageNewDao messageNewDao = (MessageNewDao)SpringHelper.getBean(MessageNewDao.class.getName());
		List<Map<String, Object>> list = messageNewDao.getMessageCallBack();
		result.put("callbackList", list);
		return result;
	}


	
	@Override
	public Map<String, Object> getReceiveCity(QueryConditions queryConditions) {
		MessageNewDao messageNewDao = (MessageNewDao)SpringHelper.getBean(MessageNewDao.class.getName());
		//查询的数据条件
        StringBuilder sb_where = new StringBuilder();
        //分页对象
        PageInfo obj_page = queryConditions.getPageinfo();
        //返回的对象，包含数据集合、分页对象等
        Map<String, Object> map_result = new HashMap<String, Object>();
       
        for (Map<String, Object> map_where : queryConditions.getConditions()) {
        	if ("cityname".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
                sb_where.append(" and a.cityname like '").append(map_where.get("value")+"'");
            }else if("employee_no".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))){
            	sb_where.append(" and a.pk_userid = ").append(map_where.get("value")+"");
            }
        }
		try {
			 map_result.put("data",messageNewDao.getReceiveCity(sb_where.toString(), obj_page));
		} catch (Exception e) {
			e.printStackTrace();
		}
		map_result.put("pageinfo", obj_page);
        
        return map_result;
	}


	
	@Override
	public Map<String, Object> getReceiveStore(QueryConditions queryConditions) {
		MessageNewDao messageNewDao = (MessageNewDao)SpringHelper.getBean(MessageNewDao.class.getName());
		
		//查询的数据条件
        StringBuilder sb_where = new StringBuilder();
        //分页对象
        PageInfo obj_page = queryConditions.getPageinfo();
        //返回的对象，包含数据集合、分页对象等
        Map<String, Object> map_result = new HashMap<String, Object>();
        StringBuilder citySb = new StringBuilder(); 
        for (Map<String, Object> map_where : queryConditions.getConditions()) {
        	if ("city_id".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
                String[] cityArr = map_where.get("value").toString().split(",");
                for(String city:cityArr){
                	citySb.append(",'"+city+"'");
                }
                citySb = citySb.deleteCharAt(0);
            }else if("name".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))){
            	sb_where.append(" and b.name like '"+map_where.get("value")+"'");
            }else if("employeeId".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))){
            	List<Map<String, Object>> citylist = messageNewDao.queryCityInfo(Long.parseLong(map_where.get("value").toString()));
            	if(citylist==null||citylist.size()==0){
            		return null;
            	}else{
            		for(int i=0;i<citylist.size();i++){
            			citySb.append(",'"+citylist.get(i).get("citycode")+"'");
            		}
            		 citySb = citySb.deleteCharAt(0);
            	}
            	
            }
        }
		try {
			 map_result.put("data",messageNewDao.getReceiveStore(citySb.toString(),sb_where.toString(), obj_page));
		} catch (Exception e) {
			e.printStackTrace();
		}
		map_result.put("pageinfo", obj_page);
        
        return map_result;
	}


	
	@Override
	public Map<String, Object> getReceiveZW(QueryConditions queryConditions) {
		MessageNewDao messageNewDao = (MessageNewDao)SpringHelper.getBean(MessageNewDao.class.getName());
		//查询的数据条件
        StringBuilder sb_where = new StringBuilder();
        //分页对象
        PageInfo obj_page = queryConditions.getPageinfo();
        //返回的对象，包含数据集合、分页对象等
        Map<String, Object> map_result = new HashMap<String, Object>();
       
        for (Map<String, Object> map_where : queryConditions.getConditions()) {
        	if ("zw".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
                sb_where.append(" and zw like ").append(map_where.get("value"));
            }
        }
		try {
			 map_result.put("data",messageNewDao.getReceiveZW(sb_where.toString(), obj_page));
		} catch (Exception e) {
			e.printStackTrace();
		}
		map_result.put("pageinfo", obj_page);
        
        return map_result;
	}


	
	@Override
	public Map<String, Object> getReceiveEmployee(QueryConditions queryConditions) {
		MessageNewDao messageNewDao = (MessageNewDao)SpringHelper.getBean(MessageNewDao.class.getName());
		//查询的数据条件
        StringBuilder sb_where = new StringBuilder();
        //分页对象
        PageInfo obj_page = queryConditions.getPageinfo();
        //返回的对象，包含数据集合、分页对象等
        Map<String, Object> map_result = new HashMap<String, Object>();
        StringBuilder storeSb = new StringBuilder(); 
        StringBuilder zwSb = new StringBuilder(); 
        StringBuilder citySb = new StringBuilder(); 
        for (Map<String, Object> map_where : queryConditions.getConditions()) {
        	if ("store".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
                String[] storeArr = map_where.get("value").toString().split(",");
                for(String store:storeArr){
                	storeSb.append(","+store+"");
                }
                storeSb = storeSb.deleteCharAt(0);
                sb_where.append(" and store_id in ("+storeSb.toString()+")");
            }else if("name".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))){
            	sb_where.append(" and name like '"+map_where.get("value")+"'");
            }else if("zw".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))){
            	 String[] zwArr = map_where.get("value").toString().split(",");
                 for(String zw:zwArr){
                 	zwSb.append(",'"+zw+"'");
                 }
                 zwSb = zwSb.deleteCharAt(0);
                 sb_where.append(" and zw in ("+zwSb.toString()+")");
            }else if("city".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))){
            	 String[] cityArr = map_where.get("value").toString().split(",");
                 for(String cc:cityArr){
                	 citySb.append(",'"+cc+"'");
                 }
                 citySb = citySb.deleteCharAt(0);
                 List<Map<String, Object>> storeList = messageNewDao.queryStoreByCity(citySb.toString());
                 if(storeList==null||storeList.size()==0){
                	 return null;
                 }else{
                	 for(int i=0;i<storeList.size();i++){
                		 storeSb.append(","+storeList.get(i).get("store_id")+"");
                     }
                     storeSb = storeSb.deleteCharAt(0);
                     sb_where.append(" and store_id in ("+storeSb.toString()+")");
                 }
                 
            }else if("employeeId".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))){
            	List<Map<String, Object>> citylist = messageNewDao.queryCityInfo(Long.parseLong(map_where.get("value").toString()));
            	if(citylist==null||citylist.size()==0){
            		return null;
            	}else{
            		for(int i=0;i<citylist.size();i++){
            			citySb.append(",'"+citylist.get(i).get("citycode")+"'");
            		}
            		 citySb = citySb.deleteCharAt(0);
                     List<Map<String, Object>> storeList = messageNewDao.queryStoreByCity(citySb.toString());
                     if(storeList==null||storeList.size()==0){
                    	 return null;
                     }else{
                    	 for(int i=0;i<storeList.size();i++){
                    		 storeSb.append(","+storeList.get(i).get("store_id")+"");
                         }
                         storeSb = storeSb.deleteCharAt(0);
                         sb_where.append(" and store_id in ("+storeSb.toString()+")");
                     }
            	}
            }
        }
		try {
			 map_result.put("data",messageNewDao.getReceiveEmployee(sb_where.toString(), obj_page));
		} catch (Exception e) {
			e.printStackTrace();
		}
		map_result.put("pageinfo", obj_page);
        
        return map_result;
	}



	
	

	
	@Override
	public boolean sendMessageAuto(User user, Message message) {
		if(!"update_password".equals(message.getType())){
			preObject(message);
			saveObject(message);//保存消息
		}
		boolean result = false;
		MessageNewDao messageNewDao = (MessageNewDao)SpringHelper.getBean(MessageNewDao.class.getName());
		 try{
			 result = MessageSendUtil.getInstance().pushMessage(user, message);
	        }catch (Exception e){
	            e.printStackTrace();
	            return false;
	        }
	        return result;
	}


	
	@Override
	public Result queryMessageOfNew(Message message) {
		MessageNewDao messageNewDao = (MessageNewDao)SpringHelper.getBean(MessageNewDao.class.getName());
		Result result = new Result();
		try {
			List<Map<String, Object>> list = messageNewDao.queryMessageNew(message);
			result.setCode(CodeEnum.success.getValue());
			result.setMessage(CodeEnum.success.getDescription());
			result.setData(list);
		} catch (Exception e) {
			e.printStackTrace();
			result.setData(null);
			result.setCode(CodeEnum.error.getValue());
			result.setMessage(CodeEnum.error.getDescription());
			return result;
		}
		
		
		return result;
	}


	
	@Override
	public Result queryMessageOfHistory(Message message,PageInfo pageinfo) {
		MessageNewDao messageNewDao = (MessageNewDao)SpringHelper.getBean(MessageNewDao.class.getName());
		Result result = new Result();
		try {
			List<Map<String, Object>> list = messageNewDao.queryMessageHistory(message, pageinfo);
			result.setData(list);
			result.setCode(CodeEnum.success.getValue());
			result.setMessage(CodeEnum.success.getDescription());
		} catch (Exception e) {
			e.printStackTrace();
			result.setData(null);
			result.setCode(CodeEnum.error.getValue());
			result.setMessage(CodeEnum.error.getDescription());
			return result;
		}
		
		
		return result;
	}


	
	@Override
	public void readMessage(Message message) {
		MessageNewDao messageNewDao = (MessageNewDao)SpringHelper.getBean(MessageNewDao.class.getName());
		messageNewDao.readMessage(message);
	}


	
	@Override
	public void delateMessage(Message message) {
		MessageNewDao messageNewDao = (MessageNewDao)SpringHelper.getBean(MessageNewDao.class.getName());
		messageNewDao.deleteMessage(message);
		
	}



	@Override
	public void sendMessage_area_news(Integer model, String id) {
		//model:消息模型 1：下订单 2：新增用户画像 3：新增拜访记录
		//send_user
		Message message = new Message();
		Map<String, Object> areaMap = new HashMap<String,Object>();
		CustomerManager customerManager=(CustomerManager)SpringHelper.getBean("customerManager");
		RelationManager relationManager = (RelationManager)SpringHelper.getBean("relationManager");
		MessageNewDao messageNewDao = (MessageNewDao)SpringHelper.getBean(MessageNewDao.class.getName());
		UserManager usermanager=(UserManager) SpringHelper.getBean("userManager");
		
		Customer newCustomer = null;
		Map<String, Object> houseMap = null;
		if(model==1){
			
		}else if(model==2){
			
			
			newCustomer = customerManager.findCustomerById(Long.parseLong(id));
			
			message.setTitle("");
			message.setContent(newCustomer.getName()+ " 客户画像被 "+newCustomer.getCreate_user()+" 收录 "+newCustomer.getMobilephone());
			message.setPk_id(Long.parseLong(id));
			message.setType("news_customer");
			message.setSendId(newCustomer.getEmployee_no());
			houseMap = messageNewDao.getCustomerHouse(Long.parseLong(id));
			
		}else if(model==3){
			Relation relation =(Relation) relationManager.getObject(Long.parseLong(id));
			
			newCustomer = customerManager.findCustomerById(relation.getCustomer_id());
			
			message.setTitle("");
			message.setContent(newCustomer.getName()+" 刚刚被 "+relation.getEmployee_name()+" 拜访 "+newCustomer.getMobilephone());
			message.setPk_id(Long.parseLong(id));
			message.setType("news_relation");
			message.setSendId(relation.getEmployee_no());
			houseMap = messageNewDao.getCustomerHouse(relation.getCustomer_id());
		}
		
		
		if(houseMap!=null){
//			if(0==Integer.parseInt(houseMap.get("house_type").toString())){//平房 根据building_id buiding_id 存储house_id 或者building_id ,根据build_model 区分
//				areaMap= messageNewDao.getAreaOfCustomer(Long.parseLong(houseMap.get("building_id").toString()), 1);
//			}else if(1==Integer.parseInt(houseMap.get("house_type").toString())){//楼房
//				areaMap= messageNewDao.getAreaOfCustomer(Long.parseLong(houseMap.get("building_id").toString()), 2);
//			}
			
//			if(areaMap==null){//根据小区ID 
			    Object tinyvillageId = houseMap.get("tinyvillage_id");
			    if(tinyvillageId!=null){
			    	areaMap = messageNewDao.getAreaOfCustomerByTinyVillage(Long.parseLong(tinyvillageId.toString()));//根据小区ID查找片区
					if(areaMap==null){//根据社区ID
							Object villageId = houseMap.get("village_id");
							if(villageId!=null){
								areaMap = messageNewDao.getAreaOfCustomerByVillage(Long.parseLong(villageId.toString()));//根据社区ID查找片区
								if(areaMap!=null){//片区存在
									
									Map<String, Object> storekeeper = messageNewDao.getStorekeeper(Long.parseLong(areaMap.get("store_id").toString()));
									User user=usermanager.findEmployeeByEmployeeNo(storekeeper.get("employeeId").toString());
									message.setReceiveId(storekeeper.get("employeeId").toString());
									preObject(message);
									saveObject(message);//保存消息
									//this.sendMessageAuto(user, message);
									if(areaMap.get("employee_a_no")!=null){
										Message message1 = new Message();
								        BeanUtils.copyProperties(message,message1,new String[]{"id","version","create_time","create_user","create_user_id","receiveId"});
										user=usermanager.findEmployeeByEmployeeNo(areaMap.get("employee_a_no").toString());
										message1.setReceiveId(areaMap.get("employee_a_no").toString());
										preObject(message1);
										saveObject(message1);//保存消息
									}
									
									//this.sendMessageAuto(user, message1);
								}
							 }
						}else{
							Map<String, Object> storekeeper = messageNewDao.getStorekeeper(Long.parseLong(areaMap.get("store_id").toString()));
							User user=usermanager.findEmployeeByEmployeeNo(storekeeper.get("employeeId").toString());
							message.setReceiveId(storekeeper.get("employeeId").toString());
							preObject(message);
							saveObject(message);//保存消息
							//this.sendMessageAuto(user, message);
							
							if(areaMap.get("employee_a_no")!=null){
								Message message1 = new Message();
						        BeanUtils.copyProperties(message,message1,new String[]{"id","version","create_time","create_user","create_user_id","receiveId"});
								user=usermanager.findEmployeeByEmployeeNo(areaMap.get("employee_a_no").toString());
								message1.setReceiveId(areaMap.get("employee_a_no").toString());
								preObject(message1);
								saveObject(message1);//保存消息
							}
							
							//this.sendMessageAuto(user, message1);
						}
					}
//			}else{
//				Map<String, Object> storekeeper = messageNewDao.getStorekeeper(Long.parseLong(areaMap.get("store_id").toString()));
//				User user=usermanager.findEmployeeByEmployeeNo(storekeeper.get("employeeId").toString());
//				message.setReceiveId(storekeeper.get("employeeId").toString());
//				this.sendMessageAuto(user, message);
//				
//				Message message1 = new Message();
//		        BeanUtils.copyProperties(message,message1,new String[]{"id","version","create_time","create_user","create_user_id","receiveId"});
//				user=usermanager.findEmployeeByEmployeeNo(areaMap.get("employee_a_no").toString());
//				message1.setReceiveId(areaMap.get("employee_a_no").toString());
//				this.sendMessageAuto(user, message1);
//			}
			
		}
		
		
	}



	@Override
	public Integer getUnReadMessageAmount(Message message) {
		MessageNewDao messageNewDao = (MessageNewDao)SpringHelper.getBean(MessageNewDao.class.getName());
		return messageNewDao.getUnReadMessage(message);
	}


	
	@Override
	public Result getMessageOfNews(Message message) {
		MessageNewDao messageNewDao = (MessageNewDao)SpringHelper.getBean(MessageNewDao.class.getName());
		Result result = new Result();
		try {
			List<Map<String, Object>> list = messageNewDao.getMessageOfNews(message);
			result.setCode(CodeEnum.success.getValue());
			result.setMessage(CodeEnum.success.getDescription());
			result.setData(list);
		} catch (Exception e) {
			e.printStackTrace();
			result.setData(null);
			result.setCode(CodeEnum.error.getValue());
			result.setMessage(CodeEnum.error.getDescription());
			return result;
		}
		
		
		return result;
	}


	
	@Override
	public void deleteAllMessage(Message message) {
		MessageNewDao messageNewDao = (MessageNewDao)SpringHelper.getBean(MessageNewDao.class.getName());
		messageNewDao.deleteAllMessage(message.getReceiveId());
		
	}


	
	@Override
	public void sendMessage_common(String sendUser, String receiveUser, String model,Object obj) {
		
		Message message = new Message();
		CustomerManager customerManager=(CustomerManager)SpringHelper.getBean("customerManager");
		RelationManager relationManager = (RelationManager)SpringHelper.getBean("relationManager");
		MessageNewDao messageNewDao = (MessageNewDao)SpringHelper.getBean(MessageNewDao.class.getName());
		UserManager usermanager=(UserManager) SpringHelper.getBean("userManager");
		User user=usermanager.findEmployeeByEmployeeNo(receiveUser);//根据员工编号
		
		if(user==null){
			user = (User)usermanager.getObject(Long.parseLong(receiveUser));//根据员工ID
		}
		if("abnormal_order_apply".equals(model)){//异常订单申请
			message.setTitle("异常订单");
			Order order = (Order)obj;
			message.setContent("订单编号 "+obj+" 的订单，申请处理");
		}else if("abnormal_order_approve".equals(model)){//异常订单审批
			
			if(obj!=null){
				List<WorkInfo> list = (List<WorkInfo>)obj;
				message.setTitle("异常订单");
				message.setContent("订单编号 "+list.get(0).getOrder_sn()+" 的订单已经处理完毕");
			}
			
		}
		message.setReceiveId(user.getEmployeeId());
		//message.setType(model);
		message.setType("abnormal_order");
		message.setSendId(sendUser);
		message.setPk_id(-100L);
		message.setJump_path("message_notice");
		preObject(message);
		saveObject(message);//保存消息
		this.sendMessageAuto(user, message);
	}
	
	
	@Override
	public List<Map<String, Object>>  queryMessageListTop5(){
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		UserDTO userDTO = userManager.getCurrentUserDTO();
		MessageNewDao messageNewDao = (MessageNewDao) SpringHelper.getBean(MessageNewDao.class.getName());
		List<Map<String, Object>>  rep_list =messageNewDao.queryMessageByStoreKeeperId(userDTO.getEmployeeId());
		return rep_list;
	}
	
	
	
	@Override
	public void updateMessageNewById(Long id){
		MessageNewDao messageNewDao = (MessageNewDao) SpringHelper.getBean(MessageNewDao.class.getName());
		messageNewDao.updateMessageReadById(id);
	}
	
	
	
	
}
