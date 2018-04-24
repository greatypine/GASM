/**
 * gaobaolei
 */
package com.cnpc.pms.messageModel.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xmlbeans.impl.values.StringEnumValue;

import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.messageModel.dao.MessageNewDao;
import com.cnpc.pms.messageModel.dao.MessageTemplateDao;
import com.cnpc.pms.messageModel.entity.Message;
import com.cnpc.pms.messageModel.entity.MessageTemplate;
import com.cnpc.pms.messageModel.entity.MessageTemplateCallBack;
import com.cnpc.pms.messageModel.manager.MessageNewManager;
import com.cnpc.pms.messageModel.manager.MessageTemplateManager;
import com.cnpc.pms.personal.entity.Customer;
import com.cnpc.pms.slice.entity.Area;
import com.cnpc.pms.utils.ValueUtil;
import com.ibm.db2.jcc.am.t;

/**
 * @author gaobaolei
 *
 */
public class MessageTemplateManagerImpl extends BizBaseCommonManager implements MessageTemplateManager{

	
	@Override
	public MessageTemplate saveMessageTemplate(MessageTemplate mt) {
		MessageTemplateManager mTemplateManager = (MessageTemplateManager)SpringHelper.getBean("messageTemplateManager");
    	MessageTemplateDao mTemplateDao = (MessageTemplateDao)SpringHelper.getBean(MessageTemplateDao.class.getName());

		MessageTemplate mTemplate=null;
		if(ValueUtil.checkValue(mt.getId())&&ValueUtil.checkValue(mt.getCode())){
			mTemplate = (MessageTemplate)this.getObject(mt.getId());
			
			mTemplateDao.deleteMessageTemplateCallBack(mt.getCode());
        }
		if(mTemplate==null){
			mTemplate = new MessageTemplate();
			mTemplate.setModel(mt.getModel());
			String code = String.valueOf(System.currentTimeMillis());
			mTemplate.setCode(code);
		}
		mTemplate.setName(mt.getName());
		mTemplate.setContent(mt.getContent());
		preObject(mTemplate);
		this.saveObject(mTemplate);//保存消息模板
		
		if("YHHX".equals(mTemplate.getModel())){//用户画像消息模板
			
		}else  if("MMXG".equals(mTemplate.getModel())){//密码修改消息模板
			
		}else{//其他消息模板
			List<String> list = mt.getCallBackFunction();//回调方法
			if(list!=null&&list.size()>0){//保存消息模板的回调
				for(int i=0;i<list.size();i++){
					MessageTemplateCallBack mtcb = new MessageTemplateCallBack();
					mtcb.setType(list.get(i).toString());
					mtcb.setTemplateCode(mTemplate.getCode());
					preObject(mtcb);
					this.saveObject(mtcb);
				}
			}
		}
		
		
		return mt;
	}

	
	@Override
	public Map<String, Object> deleteMessageTemplate(MessageTemplate mt) {
		Map<String, Object> result = new HashMap<String,Object>();
		
		MessageNewManager messageManager = (MessageNewManager)SpringHelper.getBean("messageNewManager");
		MessageNewDao mDao = (MessageNewDao)SpringHelper.getBean(MessageNewDao.class.getName());
		List<Map<String, Object>> list = mDao.queryMessageByCode(mt.getCode());
		if(list!=null&&list.size()>0){
			result.put("exist", true);
		}else{
			List<MessageTemplate>  mTemplateList = (List<MessageTemplate>)this.getList(FilterFactory.getSimpleFilter("code", mt.getCode()));
			if(mTemplateList!=null&&mTemplateList.size()>0){
				for(MessageTemplate mtObj:mTemplateList){
					mtObj.setStatus(1);
					preObject(mtObj);
				    this.saveObject(mtObj);
				}
			    
			}
			result.put("exist", false);
		}
		
		return  result;
	}


	@Override
	public Map<String, Object> queryMessageTemplate(QueryConditions queryConditions) {
		
		Map<String,Object> result = new HashMap<String,Object>();
		//查询的数据条件
        StringBuilder sb_where = new StringBuilder();
        for (Map<String, Object> map_where : queryConditions.getConditions()) {
        	if ("name".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
                sb_where.append(" and name like '").append(map_where.get("value")).append("'");
            }

        }
		  //分页对象
        PageInfo obj_page = queryConditions.getPageinfo();
    	MessageTemplateDao mTemplateDao = (MessageTemplateDao)SpringHelper.getBean(MessageTemplateDao.class.getName());
    	List<Map<String, Object>> list=null;
    	try {
    		 list = mTemplateDao.queryMessageTemplate(sb_where.toString(), obj_page);
    		 result.put("data", list);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("data", null);
		}
    	result.put("pageinfo", obj_page);
		return result;
	}


	
	@Override
	public Map<String, Object> checkTemplateIsExist(MessageTemplate mt) {
		Map<String, Object> result = new HashMap<String,Object>();
		IFilter filter=null;
		if("QT".equals(mt.getModel())){//其他消息定义
			 filter = FilterFactory.getSimpleFilter("model", mt.getName()).appendAnd(FilterFactory.getSimpleFilter("status", 0)).appendAnd(FilterFactory.getSimpleFilter("name", mt.getName()));
		}else{
			 filter = FilterFactory.getSimpleFilter("model", mt.getName()).appendAnd(FilterFactory.getSimpleFilter("status", 0));
		}
		List<?> list = this.getList(filter);
		
		result.put("list", list);
		return result;
	}


	
	@Override
	public Map<String, Object> getMessageTemplateByModel(String model) {
		Map<String, Object> result = new HashMap<String,Object>();
    	MessageTemplateDao mTemplateDao = (MessageTemplateDao)SpringHelper.getBean(MessageTemplateDao.class.getName());

		List<Map<String, Object>> list = mTemplateDao.queryMessageTemplateByModel(model);
		
		if(list!=null){
			if(list.size()>0){
				Map<String, Object> detailMap = new HashMap<String,Object>();
				result.put("templatelist", list);
			}
		}
		return result;
	}


	
	@Override
	public Map<String, Object> getMessageTemplateByCode(String code) {
		Map<String, Object> result = new HashMap<String,Object>();
    	MessageTemplateDao mTemplateDao = (MessageTemplateDao)SpringHelper.getBean(MessageTemplateDao.class.getName());

		List<Map<String, Object>> list = mTemplateDao.getMessageTemplate(code);
		
		if(list!=null){
			if(list.size()>0){
				Map<String, Object> detailMap = new HashMap<String,Object>();
				result.put("templatelist", list);
			}
		}
		return result;
		
	}


	
	@Override
	public Map<String, Object> queryMessageTemplateByCode(String model) {
		Map<String, Object> result = new HashMap<String,Object>();
    	MessageTemplateDao mTemplateDao = (MessageTemplateDao)SpringHelper.getBean(MessageTemplateDao.class.getName());

		List<Map<String, Object>> list = mTemplateDao.queryMessageTemplateByModel(model);
		
		if(list!=null){
			result = list.get(0);
		}
		return result;
	}
	
	
}
