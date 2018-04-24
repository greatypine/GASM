package com.cnpc.pms.personal.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.personal.dao.AuditDao;
import com.cnpc.pms.personal.dao.VillageDao;
import com.cnpc.pms.personal.entity.Audit;
import com.cnpc.pms.personal.entity.County;
import com.cnpc.pms.personal.entity.DistCity;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.Town;
import com.cnpc.pms.personal.entity.Village;
import com.cnpc.pms.personal.manager.AuditManager;
import com.cnpc.pms.personal.manager.CountyManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.manager.TownManager;
import com.cnpc.pms.personal.manager.VillageManager;

public class AuditManagerImpl extends BizBaseCommonManager implements AuditManager{

	@Override
	public void insertAuditVillage(Village village) {
		AuditManager auditManager=(AuditManager)SpringHelper.getBean("auditManager");
		VillageManager villageManager=(VillageManager)SpringHelper.getBean("villageManager");
		Audit audit = new Audit();
		audit.setOnly_id(village.getId());
		Map<String, Object> map = villageManager.getVillageTownInfoByVillage_id(village.getId());
		String string = (String)map.get("city_name");
		string=string.replaceAll("辖区", "");
		audit.setCity_name(string);
		audit.setName(village.getName());
		audit.setParent_id(village.getTown_id());
		audit.setGb_code(village.getGb_code());
		audit.setType_id(4);
		audit.setType_name("社区");
		audit.setCheck_id(1);
		preObject(audit);
		if(audit.getCreate_user()==null){
			audit.setCreate_user(village.getCreate_user());
			audit.setCreate_user_id(village.getCreate_user_id());
		}
		auditManager.saveObject(audit);
		
	}

	@Override
	public void insertAuditTown(Town town) {
		AuditManager auditManager=(AuditManager)SpringHelper.getBean("auditManager");
		Audit audit = new Audit();
		audit.setOnly_id(town.getId());
		TownManager townManager=(TownManager)SpringHelper.getBean("townManager");
		Map<String, Object> map = townManager.getTownParentInfoByTown_id(town.getId());
		String string = (String)map.get("city_name");
		string=string.replaceAll("辖区", "");
		audit.setCity_name(string);
		audit.setName(town.getName());
		audit.setParent_id(town.getCounty_id());
		audit.setGb_code(town.getGb_code());
		audit.setType_id(3);
		audit.setType_name("街道");
		audit.setCheck_id(1);
		preObject(audit);
		auditManager.saveObject(audit);
		
	}

	@Override
	public Map<String, Object> showAuditData(QueryConditions conditions) {
		AuditDao auditDao = (AuditDao) SpringHelper.getBean(AuditDao.class.getName());
		//查询的数据条件
        StringBuilder sb_where = new StringBuilder();
        //分页对象
        PageInfo obj_page = conditions.getPageinfo();
        
        for (Map<String, Object> map_where : conditions.getConditions()) {
            if ("check_id".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
                sb_where.append(" AND check_id=").append(map_where.get("value"));
            }
            if ("type_id".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
            	sb_where.append(" AND type_id=").append(map_where.get("value"));
			}
        }
        //返回的对象，包含数据集合、分页对象等
        Map<String, Object> map_result = new HashMap<String, Object>();
        System.out.println(sb_where);
        map_result.put("pageinfo", obj_page);
        map_result.put("header", "社区信息");
        map_result.put("data", auditDao.getAuditList(sb_where.toString(), obj_page));
        return map_result;
	}

	@Override
	public Map<String, Object> findAuditById(Long id) {
		Map<String,Object> map = new HashMap<String, Object>();
		TownManager townManager=(TownManager)SpringHelper.getBean("townManager");
		VillageManager villageManager=(VillageManager)SpringHelper.getBean("villageManager");
		CountyManager countyManager=(CountyManager)SpringHelper.getBean("countyManager");
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("id",id));
		if(lst_vilage_data!=null&&lst_vilage_data.size()>0){
			Audit audit=	(Audit)lst_vilage_data.get(0);
			if(audit.getType_id()==4){
				map.put("name", audit.getName());
				map.put("gb_code",audit.getGb_code());
				Town town = townManager.getTownById(audit.getParent_id());
				map.put("town_name",town.getName());
				Village village = villageManager.getVillageByIdInfo(audit.getOnly_id());
				map.put("town_name_new",town.getName());
				map.put("name_new",village.getName() );
				map.put("gb_code_new",village.getGb_code());
				map.put("check_id",audit.getCheck_id());
				//验证是否为新增数据
				if(audit.getName().equals(village.getName())&&audit.getGb_code().equals(village.getGb_code())){
				map.put("insertOrUp", true);
				}else{
					map.put("insertOrUp", false);
				}
				return map;
			}else if(audit.getType_id()==3){
				County county = countyManager.getCountyById(audit.getParent_id());
				map.put("name", audit.getName());
				map.put("gb_code",audit.getGb_code());
				map.put("county_name",county.getName());
				Town townById = townManager.getTownById(audit.getOnly_id());
				map.put("county_name_new",county.getName());
				map.put("name_new",townById.getName() );
				map.put("gb_code_new",townById.getGb_code());
				map.put("check_id",audit.getCheck_id());
				if(audit.getName().equals(townById.getName())&&audit.getGb_code().equals(townById.getGb_code())){
					map.put("insertOrUp", true);
					}else{
						map.put("insertOrUp", false);
					}
				return map;
			}
		}
		return null;
	}

	@Override
	public Audit saveOrUpdateAudit(Audit audit) {
		AuditManager auditManager=(AuditManager)SpringHelper.getBean("auditManager");
		Audit auditById = getAuditById(audit.getId());
		if(audit.getCheck_id()==3){
			if(auditById.getType_id()==4){
				VillageManager villageManager=(VillageManager)SpringHelper.getBean("villageManager");
				 Village village = villageManager.getVillageByIdInfo(auditById.getOnly_id());
				 village.setGb_code(auditById.getGb_code());
				 village.setName(auditById.getName());
				 villageManager.saveObject(village);
			}else if(auditById.getType_id()==3){
				TownManager townManager=(TownManager)SpringHelper.getBean("townManager");
				Town town = townManager.getTownById(auditById.getOnly_id());
				town.setGb_code(auditById.getGb_code());
				town.setName(auditById.getName());
				townManager.saveObject(town);
			}
		}
		auditById.setCheck_id(audit.getCheck_id());
		auditManager.saveObject(auditById);
		return auditById;
	}

	@Override
	public Audit getAuditById(Long id) {
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("id",id));
		if(lst_vilage_data!=null&&lst_vilage_data.size()>0){
			return	(Audit)lst_vilage_data.get(0);
		}
		return null;
	}

}
