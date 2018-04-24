package com.cnpc.pms.communityArea.manager.impl;

import com.cnpc.pms.base.paging.*;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.communityArea.entity.ComnunityArea;
import com.cnpc.pms.communityArea.manager.ComnunityAreaManager;
import com.cnpc.pms.personal.entity.AddressRelevance;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.manager.AddressRelevanceManager;
import com.cnpc.pms.personal.manager.StoreManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by litianyu on 2017/9/11.
 */
public class ComnunityAreaManagerImpl extends BizBaseCommonManager implements ComnunityAreaManager {

    @Override
    public Map<String, Object> queryComnunityAreaList(QueryConditions condition) {
        UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
        ComnunityAreaManager comnunityAreaManager = (ComnunityAreaManager) SpringHelper.getBean("comnunityAreaManager");
        Map<String,Object> returnMap = new java.util.HashMap<String, Object>();
        PageInfo pageInfo = condition.getPageinfo();
        String community_name = null;
        String address_relevanceName = null;
        String communityAreastatus=null;
        for(Map<String, Object> map : condition.getConditions()){
            if("community_name".equals(map.get("key"))&&map.get("value")!=null){//查询条件
                community_name = map.get("value").toString();
            }
            if("address_relevanceName".equals(map.get("key"))&&map.get("value")!=null){//查询条件
                address_relevanceName = map.get("value").toString();
            }
            if("communityAreastatus".equals(map.get("key"))&&map.get("value")!=null){//查询条件
                communityAreastatus = map.get("value").toString();
            }
        }
        FSP fsp = new FSP();
        fsp.setSort(SortFactory.createSort("id", ISort.DESC));
        StringBuffer sbfCondition = new StringBuffer();
        if(community_name!=null){
            sbfCondition.append(" and community_name =  '"+community_name+"'");
        }
        if(address_relevanceName!=null){
            sbfCondition.append(" and name like '%"+address_relevanceName+"%'");
        }
        if(communityAreastatus!=null){
            sbfCondition.append(" and communityAreastatus = '"+communityAreastatus+"'");
        }
        sbfCondition.append(" order by id DESC ");
        IFilter iFilter = FilterFactory.getSimpleFilter(sbfCondition.toString());
        fsp.setPage(pageInfo);
        fsp.setUserFilter(iFilter);
        List<ComnunityArea> lst_data = (List<ComnunityArea>) this.getList(fsp);
        List<ComnunityArea> ret_data = new ArrayList<ComnunityArea>();
        if(lst_data!=null&&lst_data.size()>0){
            //处理门店 动态查找。
        }
        returnMap.put("pageinfo", pageInfo);
        returnMap.put("header", "");
        returnMap.put("data", ret_data);
        return returnMap;
    }

	@Override
	public ComnunityArea saveOrUPdateComnunityArea(ComnunityArea comnunityArea) {
		AddressRelevanceManager addressRelevanceManager=(AddressRelevanceManager)SpringHelper.getBean("addressRelevanceManager");
		//小区id
		Long communityId = comnunityArea.getCommunityId();
		String pids = comnunityArea.getPids(); //区块id的字符串
		//根据小区和区块id查询当前门店订单区块不在该小区下的区块并删除()
		ComnunityAreaManager comnunityAreaManager=(ComnunityAreaManager)SpringHelper.getBean("comnunityAreaManager");
		//根据小区和区块id查找区块已经被分到其他小区的区块
		List<ComnunityArea> idAndNOtINTinyID = getComnunityAreaByaddress_idAndNOtINTinyID(comnunityArea);
		if(idAndNOtINTinyID!=null&&idAndNOtINTinyID.size()>0){
			for (ComnunityArea comnunityAreaesess : idAndNOtINTinyID) {
				comnunityAreaManager.removeObject(comnunityAreaesess);
			}
		}
		//根据门店的编号查找门店下的所以订单区块id集合
		//获取当前用户绑定的门店
		 User sessionUser = null;
			if (null != SessionManager.getUserSession()
					&& null != SessionManager.getUserSession().getSessionData()) {
				sessionUser = (User) SessionManager.getUserSession()
						.getSessionData().get("user");
			}
			UserManager userManager=(UserManager)SpringHelper.getBean("userManager");
			sessionUser=userManager.findUserById(sessionUser.getId());
			StoreManager storeManager=(StoreManager)SpringHelper.getBean("storeManager");
			Store store = storeManager.findStore(sessionUser.getStore_id());
		List<AddressRelevance> dataByStoreNo = addressRelevanceManager.getAddressRelevanceDataByStoreNo(store.getStoreno());
		String string="";
		if(dataByStoreNo!=null&&dataByStoreNo.size()>0){
			for (AddressRelevance addressRelevance : dataByStoreNo) {
				string+=","+addressRelevance.getId();
			}
			if(string.length()>0){
				string=string.substring(1, string.length());
			}
		}
		
		List<ComnunityArea> findComnunityArea = findComnunityArea(communityId,pids,string);
		if(findComnunityArea!=null&&findComnunityArea.size()>0){
			for (ComnunityArea comnunityArea2 : findComnunityArea) {
				comnunityAreaManager.removeObject(comnunityArea2);
			}
		}
		
		//添加或修改原来的数据
		String[] split = pids.split(",");
		if(split!=null&&split.length>0){
			for(int i=0;i<split.length;i++){
				ComnunityArea idAndPId = findComnunityAreaBytinyvillageIdAndPId(communityId,split[i]);
				if(idAndPId==null){
					idAndPId=new ComnunityArea();
				}
				AddressRelevance addressRelevance = addressRelevanceManager.findAddressRelevanceById(Long.parseLong(split[i]));
				idAndPId.setCommunityId(communityId);
				idAndPId.setAddress_relevance_id(addressRelevance.getId());
				idAndPId.setAddress_relevanceName(addressRelevance.getPlacename());
				preObject(idAndPId);
				comnunityAreaManager.saveObject(idAndPId);
			}
		}	
		return comnunityArea;
	}

	@Override
	public List<ComnunityArea> findComnunityArea(Long tinyvillageId, String pids,String adderssIdS) {
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("communityId="+tinyvillageId+" and address_relevance_id not in("+pids+")  and address_relevance_id in (" +adderssIdS+")"));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0) {
           return (List<ComnunityArea>)lst_vilage_data;
        }
        return null;
	}

	@Override
	public ComnunityArea findComnunityAreaBytinyvillageIdAndPId(Long tinyvillageId,
			String pid) {
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("communityId="+tinyvillageId+" and address_relevance_id="+pid));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0) {
           return (ComnunityArea)lst_vilage_data.get(0);
        }
        return null;
	}

	@Override
	public ComnunityArea SaveComnunityArea(ComnunityArea comnunityArea) {
		AddressRelevanceManager addressRelevanceManager=(AddressRelevanceManager)SpringHelper.getBean("addressRelevanceManager");
		ComnunityAreaManager comnunityAreaManager=(ComnunityAreaManager)SpringHelper.getBean("comnunityAreaManager");
		//如果为公共区块时执行此方法：判断公共区块是否绑定了小区，如果被绑定，则移除绑定关系，然后将该区块更新为公共区块
		if(comnunityArea.getPublicarea()==null){
			AddressRelevance addressRelevance1 = addressRelevanceManager.findAddressRelevanceById(comnunityArea.getAddress_relevance_id());
			addressRelevance1.setPublicarea(0);
			preObject(addressRelevance1);
			addressRelevanceManager.saveObject(addressRelevance1);
			//根据区块id查找是否绑定其他小区，如果绑定则删除后重新绑定
			List<ComnunityArea> list = findComnunityAreaByAddressId(comnunityArea.getAddress_relevance_id());
			if(list==null){
				AddressRelevance addressRelevance = addressRelevanceManager.findAddressRelevanceById(comnunityArea.getAddress_relevance_id());
				comnunityArea.setAddress_relevanceName(addressRelevance.getPlacename());
				preObject(comnunityArea);
				comnunityAreaManager.saveObject(comnunityArea);
				return comnunityArea;
			}else if(list!=null&&list.size()==1){
				ComnunityArea comnunityArea2 = list.get(0);
				AddressRelevance addressRelevance = addressRelevanceManager.findAddressRelevanceById(comnunityArea.getAddress_relevance_id());
				comnunityArea2.setAddress_relevance_id(addressRelevance.getId());
				comnunityArea2.setAddress_relevanceName(addressRelevance.getPlacename());
				comnunityArea2.setCommunityId(comnunityArea.getCommunityId());
				preObject(comnunityArea2);
				comnunityAreaManager.saveObject(comnunityArea2);
				return comnunityArea2;
			}else if(list!=null&&list.size()>1){
				for (ComnunityArea comnunityArea2 : list) {
					comnunityAreaManager.removeObject(comnunityArea2);
				}
				AddressRelevance addressRelevance = addressRelevanceManager.findAddressRelevanceById(comnunityArea.getAddress_relevance_id());
				comnunityArea.setAddress_relevanceName(addressRelevance.getPlacename());
				preObject(comnunityArea);
				comnunityAreaManager.saveObject(comnunityArea);
				return comnunityArea;
			}
		}else if(comnunityArea.getPublicarea()==1){
			List<ComnunityArea> list = findComnunityAreaByAddressId(comnunityArea.getAddress_relevance_id());
			if(list!=null&&list.size()>0){
				for (ComnunityArea comnunityArea3 : list) {
					comnunityAreaManager.removeObject(comnunityArea3);
				}
			}
			AddressRelevance addressRelevance = addressRelevanceManager.findAddressRelevanceById(comnunityArea.getAddress_relevance_id());
			addressRelevance.setPublicarea(1);
			preObject(addressRelevance);
			addressRelevanceManager.saveObject(addressRelevance);
			return comnunityArea;
		}
		return null;
	}

	@Override
	public List<ComnunityArea> findComnunityAreaByAddressId(Long addressId) {
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("address_relevance_id="+addressId));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0) {
           return (List<ComnunityArea>)lst_vilage_data;
        }
        return null;
	}

	@Override
	public List<ComnunityArea> findComnunityAreaByTinyvillageId(Long tinyvillageId) {
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("communityId="+tinyvillageId));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0) {
           return (List<ComnunityArea>)lst_vilage_data;
        }
        return null;
	}

	@Override
	public ComnunityArea deleteComnunity(Long address_relevance_id) {
		ComnunityAreaManager comnunityAreaManager=(ComnunityAreaManager)SpringHelper.getBean("comnunityAreaManager");
		List<ComnunityArea> list = findComnunityAreaByAddressId(address_relevance_id);
		if(list!=null&&list.size()>0){
			for (ComnunityArea comnunityArea : list) {
				comnunityAreaManager.removeObject(comnunityArea);
			}
		}
		AddressRelevanceManager addressRelevanceManager=(AddressRelevanceManager)SpringHelper.getBean("addressRelevanceManager");
		AddressRelevance addressRelevance = addressRelevanceManager.findAddressRelevanceById(address_relevance_id);
		if(addressRelevance!=null){
			addressRelevanceManager.removeObject(addressRelevance);
		}
		return null;
	}

	@Override
	public List<ComnunityArea> getComnunityAreaByaddress_idAndNOtINTinyID(ComnunityArea comnunityArea) {
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("communityId not in ("+comnunityArea.getCommunityId()+") and address_relevance_id in("+comnunityArea.getPids()+")" ));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0) {
           return (List<ComnunityArea>)lst_vilage_data;
        }
        return null;
	}

	

}
