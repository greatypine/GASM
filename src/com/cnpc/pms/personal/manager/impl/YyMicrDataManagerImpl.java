package com.cnpc.pms.personal.manager.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.entity.PMSAuditEntity;
import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.personal.dao.YyMicrDataDao;
import com.cnpc.pms.personal.dto.YyMicrDataDTO;
import com.cnpc.pms.personal.entity.YyMicrData;
import com.cnpc.pms.personal.manager.YyMicrDataManager;
import com.ibm.db2.jcc.am.y;

/**
 * 生活宝 微超数据实现类
 * @author zhaoxg 2016-7-18
 */
@SuppressWarnings("all")
public class YyMicrDataManagerImpl extends BaseManagerImpl implements YyMicrDataManager{

	/**
	 * 根据日期查询生活宝数据
	 * @param date 日期
	 */
	@Override
	public List<YyMicrData> queryMicrDataShbByDate(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String rt = sdf.format(new Date(Long.parseLong(date)));
		YyMicrDataDao micrDataDao = (YyMicrDataDao) SpringHelper.getBean(YyMicrDataDao.class.getName());
		List<YyMicrData> hlList = micrDataDao.queryMicrDataShbByDate(rt);
		return hlList;
	}
	
	/**
	 * 根据日期查询微超数据
	 * @param date 日期
	 */
	@Override
	public List<YyMicrData> queryMicrDataWcByDate(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String rt = sdf.format(new Date(Long.parseLong(date)));
		YyMicrDataDao micrDataDao = (YyMicrDataDao) SpringHelper.getBean(YyMicrDataDao.class.getName());
		List<YyMicrData> hlList = micrDataDao.queryMicrDataWcByDate(rt);
		return hlList;
	}
	
	/**
	 * 保存生活宝 微超数据
	 * @param shbList 数据集合
	 */
	@Override
	public YyMicrData saveMicrDataShb(List<Map<String, Object>> shbList) {
		try {
			YyMicrDataDao micrDataDao = (YyMicrDataDao) SpringHelper.getBean(YyMicrDataDao.class.getName());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			int n=0;
			for(Map<String, Object> d:shbList){
				String name = d.get("name").toString();
				String micr_type=d.get("micr_type").toString();
				if((d.get("week")!=null&&d.get("week").toString().length()>0)||(d.get("month")!=null&&d.get("month").toString().length()>0)||(d.get("total")!=null&&d.get("total").toString().length()>0)){
					double week = 0;
					if(d.get("week")!=null&&d.get("week").toString().length()>0){
						week = Double.parseDouble(d.get("week").toString());
					}
					double month = 0;
					if(d.get("month")!=null&&d.get("month").toString().length()>0){
						month = Double.parseDouble(d.get("month").toString());
					}
					double total = 0;
					if(d.get("total")!=null&&d.get("total").toString().length()>0){
						total = Double.parseDouble(d.get("total").toString());
					}
					String date = d.get("date").toString();
					
					YyMicrData y = new YyMicrData();
					y.setName(name);
					y.setMicr_type(micr_type);
					y.setWeek(week);
					y.setMonth(month);
					y.setTotal(total);
					if(name.equals("微超")){
						y.setType(name);
					}else{
						y.setType("生活宝");
					}
					y.setDate(sdf.parse(date));
					
					if(n==0){
						micrDataDao.delMicrDataByDate(date,y.getType());
						n++;
					}
					preSaveObject(y);
					saveObject(y);
				}else{
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(shbList.size());
		System.out.println();
		return null;
	}
	
	/**
	 * 生活宝数据信息列表
	 * @param condition 查询条件
	 * @return 生活宝集合
	 */
	@Override
	public Map<String, Object> getSHBMicrDataList(QueryConditions condition) {
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
		YyMicrDataDao micrDataDao = (YyMicrDataDao) SpringHelper.getBean(YyMicrDataDao.class.getName());
		List<YyMicrData> hlList = micrDataDao.getSHBMicrDataList(cond.toString(),pageInfo);
//		//3，将查询到的对象集合塞到dto的对象里面
		List<YyMicrDataDTO> rList = new ArrayList<YyMicrDataDTO>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(int i = 0 ; i < hlList.size() ; i++){
			YyMicrData hi = hlList.get(i);
			YyMicrDataDTO dto = new YyMicrDataDTO();
			dto.setName("生活宝数据格式"+sdf.format(hi.getDate()).substring(0,10).replace("-", ""));
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

	/**
	 * 微超数据信息列表
	 * @param condition 查询条件
	 * @return 集合
	 */
	@Override
	public Map<String, Object> getWCMicrDataList(QueryConditions condition) {
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
		YyMicrDataDao micrDataDao = (YyMicrDataDao) SpringHelper.getBean(YyMicrDataDao.class.getName());
		List<YyMicrData> hlList = micrDataDao.getWCMicrDataList(cond.toString(),pageInfo);
//		//3，将查询到的对象集合塞到dto的对象里面
		List<YyMicrDataDTO> rList = new ArrayList<YyMicrDataDTO>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(int i = 0 ; i < hlList.size() ; i++){
			YyMicrData hi = hlList.get(i);
			YyMicrDataDTO dto = new YyMicrDataDTO();
			dto.setName("微超数据格式"+sdf.format(hi.getDate()).substring(0,10).replace("-", ""));
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
