package com.cnpc.pms.personal.manager.impl;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.inter.common.CodeEnum;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.personal.dao.NewsDao;
import com.cnpc.pms.personal.dao.PersonDutyDao;
import com.cnpc.pms.personal.dao.TownDao;
import com.cnpc.pms.personal.dao.VillageDao;
import com.cnpc.pms.personal.dao.impl.VillageDaoImpl;
import com.cnpc.pms.personal.entity.BusinessInfo;
import com.cnpc.pms.personal.entity.City;
import com.cnpc.pms.personal.entity.County;
import com.cnpc.pms.personal.entity.Customer;
import com.cnpc.pms.personal.entity.DistCity;
import com.cnpc.pms.personal.entity.News;
import com.cnpc.pms.personal.entity.PersonDuty;
import com.cnpc.pms.personal.entity.Province;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.Town;
import com.cnpc.pms.personal.entity.Village;
import com.cnpc.pms.personal.entity.VillageBack;
import com.cnpc.pms.personal.manager.AuditManager;
import com.cnpc.pms.personal.manager.CityManager;
import com.cnpc.pms.personal.manager.CountyManager;
import com.cnpc.pms.personal.manager.CustomerManager;
import com.cnpc.pms.personal.manager.NewsManager;
import com.cnpc.pms.personal.manager.PersonDutyManager;
import com.cnpc.pms.personal.manager.ProvinceManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.manager.TownManager;
import com.cnpc.pms.personal.manager.VillageBackManager;
import com.cnpc.pms.personal.manager.VillageManager;
import com.cnpc.pms.personal.util.DataTransfromUtil;
import com.cnpc.pms.utils.ValueUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Spring;

import org.springframework.beans.BeanUtils;

/**
 * 社区业务实现类
 * Created by liuxiao on 2016/6/8 0008.
 */
public class VillageManagerImpl extends BizBaseCommonManager implements VillageManager{

    /**
     * 根据街道ID获取所有的社区ID
     * @param town_id 街道ID
     * @return 社区ID集合
     */
    @Override
    public List<Long> getVillageIdByTownId(Long town_id) {
        List<Long> lst_villageId = new ArrayList<Long>();
        List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("town_id",town_id));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0){
            for(Object obj_data : lst_vilage_data){
                lst_villageId.add(((Village)obj_data).getId());
            }
        }
        return lst_villageId;
    }

    @Override
    /**
     * 根据社区名称获取所有的社区
     * @param villageName 街道ID
     * @return 社区对象
     */
    public Village getVillageByVillageName(String villageName) {

        List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("name",villageName));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0) {
           return (Village)lst_vilage_data.get(0);

        }
        return null;
    }

	@Override
	public Map<String, Object> showVillageData(QueryConditions conditions) {
		VillageDao villageDao = (VillageDao) SpringHelper.getBean(VillageDao.class.getName());
		//查询的数据条件
        StringBuilder sb_where = new StringBuilder();
        //分页对象
        PageInfo obj_page = conditions.getPageinfo();
        //返回的对象，包含数据集合、分页对象等
        Map<String, Object> map_result = new HashMap<String, Object>();

        for (Map<String, Object> map_where : conditions.getConditions()) {
            if ("village_name".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
                sb_where.append(" AND vill.name like '").append(map_where.get("value")).append("'");
            }
            if ("county_name".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
            	sb_where.append(" AND county.name like '").append(map_where.get("value")).append("'");
			}
            if ("town_name".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
            	sb_where.append(" AND town.name like '").append(map_where.get("value")).append("'");
			}
            if ("gb_code".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
            	String str=(String) map_where.get("value");
            	String string=str.substring(1, str.length()-1);
                sb_where.append(" and vill.gb_code ='").append(string.trim()).append("'");
            }
            
        }
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
		if(3224==sessionUser.getUsergroup().getId()){
			if(store!=null){
				sb_where.append(" AND town.id in(").append(store.getTown_id()).append(")");
			}else{
				sb_where.append(" AND 1=0");
			}
			
		}else if(3231==sessionUser.getUsergroup().getId()||3223==sessionUser.getUsergroup().getId()||3225==sessionUser.getUsergroup().getId()||3229==sessionUser.getUsergroup().getId()){
			if(store!=null){
				sb_where.append(" AND town.id in(").append(store.getTown_id()).append(")");
			}else{
				sb_where.append(" AND 1=0");
			}
		}else if(sessionUser.getZw().equals("地址采集")||sessionUser.getCode().equals("sunning1")){
			sb_where.append(" and 1=1 ");
		}else{
			//取得当前登录人 所管理城市
			String cityssql = "";
			List<DistCity> distCityList = userManager.getCurrentUserCity();
			if(distCityList!=null&&distCityList.size()>0){
				for(DistCity d:distCityList){
					cityssql += "'"+d.getCityname()+"',";
				}
				cityssql=cityssql.substring(0, cityssql.length()-1);
			}
			if(cityssql!=""&&cityssql.length()>0){
				String cityId = getCityId(cityssql);
				if(!"".equals(cityId)){
					cityId=cityId.substring(0, cityId.length()-1);
					sb_where.append(" and city.id in ("+cityId+")");
				}else{
					sb_where.append(" and 0=1 ");
				}
				
			}else{
				sb_where.append(" and 0=1 ");
			}
		};
        
        System.out.println(sb_where);
        map_result.put("pageinfo", obj_page);
        map_result.put("header", "社区信息");
        map_result.put("data", villageDao.getVillageList(sb_where.toString(), obj_page));
        return map_result;
	}

	@Override
	public Village deleteVIllageByID(long id) {
		Village village=(Village) this.getObject(id); 
		village.setStatus(1);
    	preSave(village);
    	save(village);
    	return village;
	}

	@Override
	public Map<String, Object> getVillageById(Long id) {
		Map<String,Object> map = new HashMap<String, Object>();
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("id",id));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0) {
         Village village=(Village) lst_vilage_data.get(0);
         map.put("name", village.getName());
         map.put("gb_code", village.getGb_code());
         map.put("square_area", village.getSquare_area());
         map.put("household_number", village.getHousehold_number());
         map.put("resident_population_number", village.getResident_population_number());
         map.put("committee_address", village.getCommittee_address());
         map.put("job", village.getJob());
         if(village.getJob()!=null&&!"".equals(village.getJob())){
        	 CustomerManager customerManager=(CustomerManager)SpringHelper.getBean("customerManager");
        	 String stts="";
        	 String[] split = village.getJob().split(",");
        	 if(split.length>0){
        		 for (int i = 0; i < split.length; i++) {
        			 String[] strings = split[i].split("-");
        			 Customer customer = customerManager.findCustomerById(Long.parseLong(strings[0]));
        			 if("请选择".equals(strings[2])){
        				 stts +=","+customer.getName();
        			 }else{
        				 stts +=","+customer.getName()+"("+strings[2]+")";
        			 }
        			
        		 }
        	 } 
        	 map.put("showSelectName", stts.substring(1,stts.length())); 
         }
         TownDao townDao=(TownDao)SpringHelper.getBean(TownDao.class.getName());
         if(village.getTown_id()!=null&&village.getTown_id()>0){
        	 Town town = townDao.getTownByID(village.getTown_id());
        	 if(town!=null){
        		 map.put("town_gb_code", town.getGb_code());
        		 map.put("town_id", town.getId());
            	 map.put("town_name", town.getName());
        	 }
         }
         
        }
        return map;
	}

	@Override
	public Village updateVillageInfo(Village village) {
		 User sessionUser = null;
			if (null != SessionManager.getUserSession()
					&& null != SessionManager.getUserSession().getSessionData()) {
				sessionUser = (User) SessionManager.getUserSession()
						.getSessionData().get("user");
			}
			UserManager userManager=(UserManager)SpringHelper.getBean("userManager");
			sessionUser=userManager.findUserById(sessionUser.getId());
		VillageManager villageManager=(VillageManager)SpringHelper.getBean("villageManager");
		Village save_village=null;
		if(village.getId()!=null){
			save_village=(Village)this.getObject(village.getId());
			if(village.getGb_code()!=null&&(!save_village.getGb_code().equals(village.getGb_code())||!save_village.getName().equals(village.getName()))){
				AuditManager auditManager=(AuditManager)SpringHelper.getBean("auditManager");
				auditManager.insertAuditVillage(save_village);
			}
		}else{
			save_village=new Village();
		}
		save_village.setName(village.getName());
		save_village.setGb_code(village.getGb_code());
		save_village.setHousehold_number(village.getHousehold_number());
		save_village.setResident_population_number(village.getResident_population_number());
		save_village.setSquare_area(village.getSquare_area());
		save_village.setTown_id(village.getTown_id()==null?0:village.getTown_id());
		save_village.setCommittee_address(village.getCommittee_address());
		save_village.setJob(village.getJob());
		save_village.setEmployee_no(sessionUser.getEmployeeId());
		preObject(save_village);
		villageManager.saveObject(save_village);
		if(village.getId()==null){
			AuditManager auditManager=(AuditManager)SpringHelper.getBean("auditManager");
			auditManager.insertAuditVillage(save_village);
		}
		updateOrinsertDuty(save_village.getId(),save_village.getJob(),2,sessionUser.getEmployeeId());
		
	/*	if(village.getId()!=null){
			VillageBackManager villageBackManager=(VillageBackManager)SpringHelper.getBean("villageBackManager");
			 VillageBack villageBack = villageBackManager.getVillageBackInfoBytown_id(save_village.getId());
			NewsDao newsDao=(NewsDao)SpringHelper.getBean(NewsDao.class.getName());
			newsDao.deleteObject(villageBack);
			NewsManager newsManager=(NewsManager)SpringHelper.getBean("newsManager");
			News news = newsManager.getNewsBy(2, save_village.getId());
			newsDao.deleteObject(news);
			
		}*/
		return save_village;
	}

	@Override
	public Village getVillageByGb_code(String gb_code) {
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("gb_code",gb_code));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0) {
           return (Village)lst_vilage_data.get(0);

        }
        return null;
	}

	@Override
	public Village getVillageByTown_idAndVillage_name(String village_name, Long town_id) {
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("town_id = "+town_id + " and village_name like '%"+village_name+"%'"));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0) {
           return (Village)lst_vilage_data.get(0);
        }
        return null;
	}

	@Override
	public Village getVillageByIdInfo(Long id) {
        List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("id",id));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0) {
           return (Village)lst_vilage_data.get(0);

        }
        return null;
	}

	@Override
	public Map<String, Object> getVillageInfoByGb_code(String gb_code) {
		Map<String, Object> map=new HashMap<String, Object>();
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("gb_code",gb_code));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0) {
           Village village= (Village)lst_vilage_data.get(0);
           map.put("village_name", village.getName());
           map.put("village_gb_code", village.getGb_code());
           map.put("village_id", village.getId());
           TownManager townManager=(TownManager)SpringHelper.getBean("townManager");
           Town town = townManager.getTownById(village.getTown_id());
           map.put("town_id", town.getId());
           map.put("town_name", town.getName());
           map.put("town_gb_code", town.getGb_code());
           return map;
        }
        return null;
	}

	@Override
	public List<Village> getVillageInfoByTown_idAndVillage_name(Village villagea) {
		VillageDao villageDao=(VillageDao)SpringHelper.getBean(VillageDao.class.getName());
		List<Village> list = villageDao.getVillList(villagea.getName(), villagea.getId());
        return list;
	}

	@Override
	public List<Map<String, Object>> getVillageDataByTownId(Long townId) {
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		Village village=null;
		 List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("town_id",townId));
		 if(lst_vilage_data!=null&&lst_vilage_data.size()>0){
			 for (Object object : lst_vilage_data) {
				Map<String, Object> map=new HashMap<String, Object>();
				village=(Village)object;
				map.put("village_name", village.getName());
				map.put("village_id", village.getId());
				list.add(map);
			}
		 }
		return list;
	}
    /**
     * 维护责任表
     * @param id
     * @param str 
     * @param type
     */
	public void updateOrinsertDuty(Long id,String str,Integer type,String employee_no){
		PersonDutyManager personDutyManager=(PersonDutyManager)SpringHelper.getBean("personDutyManager");
		String idString="";
		if(str!=null&&!"".equals(str)){
			String[] slit = str.split(",");
			if(slit.length>0){
				for (int i = 0; i < slit.length; i++) {
					String[] strings = slit[i].split("-");
					PersonDuty personDuty = personDutyManager.findPersonDutyBycustomerIdAnddutyIdAndtype(id, type,Long.parseLong(strings[0]));
					if(personDuty==null){
						personDuty=new PersonDuty();
					}
					personDuty.setDuty_id(id);
					personDuty.setType(type);
					personDuty.setCustomer_id(Long.parseLong(strings[0]));
					personDuty.setDuty("请选择".equals(strings[2])?null:strings[2]);
					personDuty.setEmployee_no(employee_no);
					preObject(personDuty);
					personDutyManager.saveObject(personDuty);
					idString+=","+strings[0];
				}
				
			}
		}
		if(idString.length()>0){
			idString=idString.substring(1,idString.length());
		}
		PersonDutyDao personDutyDao=(PersonDutyDao)SpringHelper.getBean(PersonDutyDao.class.getName());
		personDutyDao.deletePersonDuty(idString, type, id);
	}

	@Override
	public List<Village> getVillageListByTown_idAndVillage_name(Village village) {
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("town_id = "+village.getTown_id() + " and name like '%"+village.getName()+"%'"));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0) {
           return (List<Village>) lst_vilage_data;
        }
        return null;
	}

	@Override
	public Map<String, Object> getVillageTownInfoByVillage_id(Long id) {
		Map<String, Object> map=new HashMap<String, Object>();
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("id",id));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0) {
           Village village= (Village)lst_vilage_data.get(0);
           map.put("village_name", village.getName());
           map.put("village_gb_code", village.getGb_code());
           map.put("village_id", village.getId());
           TownManager townManager=(TownManager)SpringHelper.getBean("townManager");
           Town town = townManager.getTownById(village.getTown_id());
           map.put("town_id", town.getId());
           map.put("town_name", town.getName());
           map.put("town_gb_code", town.getGb_code());
           CountyManager countyManager=(CountyManager)SpringHelper.getBean("countyManager");
           County county = countyManager.getCountyById(town.getCounty_id());
           map.put("county_id", county.getId());
           map.put("county_name", county.getName());
           map.put("county_gb_code", county.getGb_code());
           CityManager cityManager=(CityManager)SpringHelper.getBean("cityManager");
           City city = cityManager.getCityById(county.getCity_id());
           map.put("city_id", city.getId());
           map.put("city_name", city.getName());
           map.put("city_gb_code", city.getGb_code());
          ProvinceManager provinceManager=(ProvinceManager)SpringHelper.getBean("provinceManager");
           Province province = provinceManager.getProvinceById(city.getProvince_id());
           map.put("province_id", province.getId());
           map.put("province_name", province.getName());
           map.put("province_gb_code", province.getGb_code());
           return map;
        }
		return null;
	}

	@Override
	public Village getVillageByTown_idAndGb_code(Village village) {
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("town_id = "+village.getTown_id() + " and gb_code= "+village.getGb_code()));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0) {
           return (Village) lst_vilage_data.get(0);
        }
        return null;
	}

	@Override
	public Result getVillageByVillage_name(Village village) {
		VillageManager villageManager=(VillageManager)SpringHelper.getBean("villageManager");
		Result result = new Result();
		List<Village> list = new ArrayList<Village>();
		 IFilter filter = FilterFactory.getSimpleFilter("1=1");

	        if(ValueUtil.checkValue(village.getName())){
	            filter = filter.appendAnd(FilterFactory.getSimpleFilter("name like '%"+village.getName()+"%'"));
	        }
	        List<?> lst_result = this.getList(filter);
	        result.setCode(CodeEnum.success.getValue());
	        result.setMessage(CodeEnum.success.getDescription());
	        if(lst_result == null || lst_result.size() == 0){
	            result.setVillageList(new ArrayList<Village>());
	        }else{
	        	List<Village> listbus=(List<Village>) lst_result;
	        	for (Village village1 : listbus) {
	        		if(village1.getTown_id()!=null){
	        			Map<String, Object> map = villageManager.getVillageTownInfoByVillage_id(village1.getId());
	        			village1.setProvince_name(map.get("province_name").toString());
	        			village1.setProvince_id(Long.valueOf(String.valueOf(map.get("province_id").toString())).longValue());
	        			village1.setCity_id(Long.valueOf(String.valueOf(map.get("city_id").toString())).longValue());
	        			village1.setCity_name(map.get("city_name").toString());
	        			village1.setTown_id(Long.valueOf(String.valueOf(map.get("town_id").toString())).longValue());
	        			village1.setTown_name(map.get("town_name").toString());
	        			village1.setCounty_id(Long.valueOf(String.valueOf(map.get("county_id").toString())).longValue());
	        			village1.setCounty_name(map.get("county_name").toString());
	        			list.add(village1);
	        		}
				}
	            result.setVillageList(list);
	        }   
		return result;
	}

	@Override
	public Result getVillageByVillage(Village village) {
		Result res = new Result();
		Village villageinfoBy = getVillageByTownidAndVillagenamecc(village.getGb_code());
		if(villageinfoBy!=null){
			if(village.getId()!=null){
				if(!villageinfoBy.getId().equals(village.getId())){
					res.setCode(300);
					res.setMessage( village.getName()+ "已存在");
					return res;
				}
			}
			if(village.getId()==null){
				res.setCode(300);
				res.setMessage( village.getName()+ "已存在");
				return res;
			}
		}
		res.setCode(200);
		res.setMessage( village.getName()+ "不存在");
		return res;
	}

	
	public Village getVillageByTownidAndVillagenamecc(String gb_code) {
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("gb_code ='"+gb_code+"'"));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0) {
           return (Village)lst_vilage_data.get(0);
        }
        return null;
	}
	
	@Override
	public Village getVillageByTownidAndVillagename(String gb_code, Long town_id) {
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("town_id = "+town_id + " and gb_code ='"+gb_code+"'"));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0) {
           return (Village)lst_vilage_data.get(0);
        }
        return null;
	}

	@Override
	public Result saveOrUpdateVillageApp(Village village) {
		NewsDao newsDao=(NewsDao)SpringHelper.getBean(NewsDao.class.getName());
		VillageManager villageManager=(VillageManager)SpringHelper.getBean("villageManager");
		Village villageinfoBy = getVillageByTownidAndVillagenamecc(village.getGb_code());
		Result result = new Result();
		if(village.getId()!=null){
			Village village2 = getVillageByIdInfo(village.getId());
			if(village.getGb_code()!=null&&(!village2.getGb_code().equals(village.getGb_code())||!village2.getName().equals(village.getName()))){
				AuditManager auditManager=(AuditManager)SpringHelper.getBean("auditManager");
				village2.setCreate_user(village.getCreate_user());
				village2.setCreate_user_id(village.getCreate_user_id());
				auditManager.insertAuditVillage(village2);
			}
			BeanUtils.copyProperties(village, village2
					,new String[]{"id","version","status","create_user","create_user_id","create_time"
								,"update_user","update_user_id","update_time","introduction","employee_no","job"});
			if(village2.getEmployee_no()==null){
				village2.setEmployee_no(village.getEmployee_no());
			}
			if(village2.getCreate_user()==null){
				village2.setCreate_user(village.getCreate_user());
				village2.setCreate_user_id(village.getCreate_user_id());
			}
			preObject(village2);
			village2.setUpdate_user(village.getCreate_user());
			village2.setUpdate_user_id(village.getCreate_user_id());
			villageManager.saveObject(village2);
			if(villageinfoBy!=null&&!villageinfoBy.getId().equals(village.getId())){
				newsDao.deleteObject(villageinfoBy);
			}
		}else{
			Village  vill=villageinfoBy;
			if(villageinfoBy==null){
				villageinfoBy=new Village();
				villageinfoBy.setCreate_user(village.getCreate_user());
				villageinfoBy.setCreate_user_id(village.getCreate_user_id());
			}
			BeanUtils.copyProperties(village, villageinfoBy
					,new String[]{"id","version","status","create_user","create_user_id","create_time"
								,"update_user","update_user_id","update_time","introduction","employee_no","job"});
			
			if(villageinfoBy.getEmployee_no()==null){
				villageinfoBy.setEmployee_no(village.getEmployee_no());
			}
			preObject(villageinfoBy);
			villageinfoBy.setUpdate_user(village.getCreate_user());
			villageinfoBy.setUpdate_user_id(village.getCreate_user_id());
			villageManager.saveObject(villageinfoBy);
			if(village.getId()==null&&vill==null){
				AuditManager auditManager=(AuditManager)SpringHelper.getBean("auditManager");
				auditManager.insertAuditVillage(villageinfoBy);
			}else if(village.getGb_code()!=null&&(!villageinfoBy.getGb_code().equals(village.getGb_code())||!villageinfoBy.getName().equals(village.getName()))){
				AuditManager auditManager=(AuditManager)SpringHelper.getBean("auditManager");
				auditManager.insertAuditVillage(villageinfoBy);
			}
		}
		result.setCode(200);
		result.setMessage("保存成功");
		return result;
	}

	
	@Override
	public List<Village> searchVillageByTownAndName(Long townId, String villageName) {
		VillageDao villageDao=(VillageDao)SpringHelper.getBean(VillageDao.class.getName());	
		return villageDao.getVillListLimit(villageName, townId);
	}

	@Override
	public Village verifyUpdate(Village village) {
		Village villageByGb_code = getVillageByGb_code(village.getGb_code());
		if(villageByGb_code!=null){
			if(!villageByGb_code.getId().equals(village.getId())){
				return villageByGb_code;
			}
		}
		return null;
	};
    
	/**
	 * 根据当前登录的用户获取所有的城市id
	 */
	public String getCityId(String string){
		CityManager cityManager=(CityManager)SpringHelper.getBean("cityManager");
		String[] cityName = string.split(",");
		String cityId="";
		for(int i=0;i<cityName.length;i++){
			List<City> list = cityManager.getCityDataByName(cityName[i].replaceAll("'", ""));
			if(list!=null&&list.size()>0){
				for (City city : list) {
					cityId+=city.getId()+",";
				}
			}
		}
		return cityId;
	}

	@Override
	public List<Village> findVillageByTownIds(String townids) {
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("town_id in("+townids+")"));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0) {
           return (List<Village>) lst_vilage_data;
        }
        return null;
	}
	
}
