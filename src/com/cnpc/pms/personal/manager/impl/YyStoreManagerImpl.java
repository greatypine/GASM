package com.cnpc.pms.personal.manager.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.personal.dao.YyMicrDataDao;
import com.cnpc.pms.personal.dao.YyStoreDao;
import com.cnpc.pms.personal.dto.YyHouseDTO;
import com.cnpc.pms.personal.entity.YyMicrData;
import com.cnpc.pms.personal.entity.YyStore;
import com.cnpc.pms.personal.manager.YyStoreManager;

/**
 * 物业事业部数据实现类
 * @author zhaoxg 2016-7-22
 *
 */
@SuppressWarnings("all")
public class YyStoreManagerImpl extends BaseManagerImpl implements YyStoreManager{
	
	
	/**
	 * 根据日期查询物业事业部数据
	 * @param date
	 * @return
	 */
	@Override
	public List<YyStore> queryStoreByDate(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String rt = sdf.format(new Date(Long.parseLong(date)));
		YyStoreDao yyStoreDao = (YyStoreDao) SpringHelper.getBean(YyStoreDao.class.getName());
		List<YyStore> storeList = yyStoreDao.queryStoreByDate(rt);
		return storeList;
	}
	
	
	/**
	 * 保存物业事业部数据
	 * @param storeList 
	 */
	@Override
	public void saveYyStore(List<Map<String, Object>> storeList){
		try {
			
			YyStoreDao yyStoreDao = (YyStoreDao) SpringHelper.getBean(YyStoreDao.class.getName());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			int n=0;
			for(Map<String, Object> d:storeList){
				String name = d.get("name").toString();
				int num = d.get("num")==null?0:Integer.parseInt(d.get("num").toString());
				String address = d.get("address")==null?"":d.get("address").toString();
				String date = d.get("date").toString();
				String remark = d.get("remark")==null?"":d.get("remark").toString();
				
				YyStore y = new YyStore();
				y.setName(name);
				y.setNum(num);
				y.setDate(sdf.parse(date));
				y.setRemark(address+" "+remark);
				
				if(n==0){
					yyStoreDao.delStoreByDate(date);
					n++;
				}
				
				preSaveObject(y);
				saveObject(y);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询物业部数据
	 * @param condition 查询条件
	 * @return
	 */
	@Override
	public Map<String, Object> getStoreList(QueryConditions condition) {
		Map<String,Object> returnMap = new java.util.HashMap<String, Object>();
		PageInfo pageInfo = condition.getPageinfo();
		String healthInfoId = null;
		String name = null;
		String telephone = null;
		StringBuffer cond = new StringBuffer();
		for(Map<String, Object> map : condition.getConditions()){
			if("date".equals(map.get("key"))&&map.get("value")!=null){//组织机构
				cond.append(map.get("value").toString());
			}
		}
		YyStoreDao storeDao = (YyStoreDao) SpringHelper.getBean(YyStoreDao.class.getName());
		List<YyStore> hlList = storeDao.getStoreList(cond.toString(),pageInfo);
//		//3，将查询到的对象集合塞到dto的对象里面
		List<YyHouseDTO> rList = new ArrayList<YyHouseDTO>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(int i = 0 ; i < hlList.size() ; i++){
			YyStore hi = hlList.get(i);
			YyHouseDTO dto = new YyHouseDTO();
			dto.setName("物业事业部数据格式"+sdf.format(hi.getDate()).substring(0,10).replace("-", ""));
			dto.setDate(hi.getDate());
			
			dto.setDate_str(sdf.format(hi.getDate()));
			
			dto.setCreateuser(hi.getCreate_user());
			rList.add(dto);
		}
		returnMap.put("pageinfo", pageInfo);
		returnMap.put("header", "");
		returnMap.put("data", rList);
		return returnMap;
	}
	
	
	
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
	
}
