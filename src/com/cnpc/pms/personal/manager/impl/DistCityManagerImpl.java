package com.cnpc.pms.personal.manager.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.personal.dao.DistCityDao;
import com.cnpc.pms.personal.entity.DistCity;
import com.cnpc.pms.personal.entity.DistCityCode;
import com.cnpc.pms.personal.manager.DistCityCodeManager;
import com.cnpc.pms.personal.manager.DistCityManager;

@SuppressWarnings("all")
public class DistCityManagerImpl extends BizBaseCommonManager implements DistCityManager {

	@Override
	public List<DistCity> queryDistCityByUserIdCity(Long userid, String cityname) {
		IFilter iFilter = FilterFactory.getSimpleFilter("pk_userid", userid)
				.appendAnd(FilterFactory.getSimpleFilter("cityname", cityname));
		List<DistCity> distCityList = (List<DistCity>) this.getList(iFilter);
		return distCityList;
	}

	@Override
	public List<DistCity> queryDistCityListByUserId(Long userid) {
		IFilter iFilter = FilterFactory.getSimpleFilter("pk_userid", userid);
		List<DistCity> distCityList = (List<DistCity>) this.getList(iFilter);
		return distCityList;
	}

	protected void preSaveObject(Object o) {
		if (o instanceof DataEntity) {
			User sessionUser = null;
			if (null != SessionManager.getUserSession() && null != SessionManager.getUserSession().getSessionData()) {
				sessionUser = (User) SessionManager.getUserSession().getSessionData().get("user");
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

	@Override
	public DistCity queryDistCitysByUserId(Long userid) {
		// 先查询user
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		User user = (User) userManager.getObject(userid);
		DistCity distCity = new DistCity();
		IFilter iFilter = FilterFactory.getSimpleFilter("pk_userid", userid);
		List<DistCity> lst_data = (List<DistCity>) this.getList(iFilter);
		distCity.setUsername(user.getName());
		distCity.setPk_userid(user.getId());
		if (lst_data != null) {
			if (lst_data.size() == 1) {
				distCity.setCity1(lst_data.get(0).getCityname());
			}
			if (lst_data.size() == 2) {
				distCity.setCity1(lst_data.get(0).getCityname());
				distCity.setCity2(lst_data.get(1).getCityname());
			}
			if (lst_data.size() == 3) {
				distCity.setCity1(lst_data.get(0).getCityname());
				distCity.setCity2(lst_data.get(1).getCityname());
				distCity.setCity3(lst_data.get(2).getCityname());
			}
			if (lst_data.size() == 4) {
				distCity.setCity1(lst_data.get(0).getCityname());
				distCity.setCity2(lst_data.get(1).getCityname());
				distCity.setCity3(lst_data.get(2).getCityname());
				distCity.setCity4(lst_data.get(3).getCityname());
			}
			if (lst_data.size() == 5) {
				distCity.setCity1(lst_data.get(0).getCityname());
				distCity.setCity2(lst_data.get(1).getCityname());
				distCity.setCity3(lst_data.get(2).getCityname());
				distCity.setCity4(lst_data.get(3).getCityname());
				distCity.setCity5(lst_data.get(4).getCityname());
			}
			if (lst_data.size() > 6) {
				distCity.setCity5("全部");
			}
		}
		return distCity;
	}

	@Override
	public DistCity updateDistCity(DistCity distCity) {

		UserManager manager = (UserManager) SpringHelper.getBean("userManager");
		UserDTO userDTO = manager.getCurrentUserDTO();

		Long pk_userid = distCity.getPk_userid();
		String username = distCity.getUsername();

		// 从配置文件改为数据库取
		DistCityCodeManager distCityCodeManager = (DistCityCodeManager) SpringHelper.getBean("distCityCodeManager");

		if (distCity.getIsSelectAll() != null && distCity.getIsSelectAll()) {
			// 选择了全部
			List<DistCityCode> dis = distCityCodeManager.queryAllDistCityList();
			if (dis != null && dis.size() > 0) {
				// 保存之前 删除之前的
				DistCityDao distCityDao = (DistCityDao) SpringHelper.getBean(DistCityDao.class.getName());
				distCityDao.removeDistCityByUserid(pk_userid);

				for (DistCityCode dc : dis) {
					DistCity d = new DistCity();
					d.setCityname(dc.getCityname());
					d.setCitycode(dc.getCitycode());
					d.setPk_userid(pk_userid);
					preSaveObject(d);
					saveObject(d);
				}
			}

		} else {
			String city1 = distCity.getCity1();
			String city2 = distCity.getCity2();
			String city3 = distCity.getCity3();
			String city4 = distCity.getCity4();
			String city5 = distCity.getCity5();
			List<DistCity> distCities = new ArrayList<DistCity>();
			// PropertiesValueUtil propertiesValueUtil = new
			// PropertiesValueUtil("classpath:conf/citycode.properties");
			if (city1 != null && city1.trim().length() > 0) {
				DistCity distCity1 = new DistCity();
				distCity1.setPk_userid(pk_userid);
				DistCityCode dis = distCityCodeManager.queryDistCityCodeByName(city1);
				if (dis == null) {
					return null;
				}
				distCity1.setCitycode(dis.getCitycode());
				distCity1.setCityname(city1);
				distCities.add(distCity1);
			}
			if (city2 != null && city2.trim().length() > 0) {
				DistCity distCity2 = new DistCity();
				distCity2.setPk_userid(pk_userid);
				DistCityCode dis = distCityCodeManager.queryDistCityCodeByName(city2);
				if (dis == null) {
					return null;
				}
				distCity2.setCitycode(dis.getCitycode());
				distCity2.setCityname(city2);
				distCities.add(distCity2);
			}
			if (city3 != null && city3.trim().length() > 0) {
				DistCity distCity3 = new DistCity();
				distCity3.setPk_userid(pk_userid);
				DistCityCode dis = distCityCodeManager.queryDistCityCodeByName(city3);
				if (dis == null) {
					return null;
				}
				distCity3.setCitycode(dis.getCitycode());
				distCity3.setCityname(city3);
				distCities.add(distCity3);
			}
			if (city4 != null && city4.trim().length() > 0) {
				DistCity distCity4 = new DistCity();
				distCity4.setPk_userid(pk_userid);
				DistCityCode dis = distCityCodeManager.queryDistCityCodeByName(city4);
				if (dis == null) {
					return null;
				}
				distCity4.setCitycode(dis.getCitycode());
				distCity4.setCityname(city4);
				distCities.add(distCity4);
			}
			if (city5 != null && city5.trim().length() > 0) {
				DistCity distCity5 = new DistCity();
				distCity5.setPk_userid(pk_userid);
				DistCityCode dis = distCityCodeManager.queryDistCityCodeByName(city5);
				if (dis == null) {
					return null;
				}
				distCity5.setCitycode(dis.getCitycode());
				distCity5.setCityname(city5);
				distCities.add(distCity5);
			}
			// 删除之前的
			DistCityDao distCityDao = (DistCityDao) SpringHelper.getBean(DistCityDao.class.getName());
			distCityDao.removeDistCityByUserid(pk_userid);

			for (DistCity d : distCities) {
				preSaveObject(d);
				saveObject(d);
			}
		}
		return distCity;
	}

	@Override
	public List<Long> queryDistinctUserId() {
		List<DistCity> distCityList = (List<DistCity>) this.getList();
		List<Long> userIds = new ArrayList<Long>();
		if (distCityList != null) {
			for (DistCity d : distCityList) {
				userIds.add(d.getPk_userid());
			}
		}
		HashSet hashSet = new HashSet(userIds);
		userIds.clear();
		userIds.addAll(hashSet);
		return userIds;
	}

	/**
	 * 根据城市 查找所有的 usesrId
	 * 
	 * @param city
	 * @return
	 */
	@Override
	public List<Long> queryDistinctByCity(String city) {
		IFilter iFilter = FilterFactory.getSimpleFilter("cityname like '" + city + "'");
		List<DistCity> distCityList = (List<DistCity>) this.getList(iFilter);
		List<Long> userIds = new ArrayList<Long>();
		if (distCityList != null) {
			for (DistCity d : distCityList) {
				userIds.add(d.getPk_userid());
			}
		}
		HashSet hashSet = new HashSet(userIds);
		userIds.clear();
		userIds.addAll(hashSet);
		return userIds;
	}

	/**
	 * 统计 每个人 都管理多少个城市 数量
	 * 
	 * @return
	 */
	@Override
	public List<?> queryDistCityCount() {
		DistCityDao distCityDao = (DistCityDao) SpringHelper.getBean(DistCityDao.class.getName());
		List<?> list = distCityDao.queryDistCityCount();
		return list;
	}

	@Override
	public void saveDistCity(DistCity d) {
		preSaveObject(d);
		this.saveObject(d);
	}

	@Override
	public DistCity findDistCityByCityName(String cityname) {
		// 获取当前登录人的用户id
		User sessionUser = null;
		if (null != SessionManager.getUserSession() && null != SessionManager.getUserSession().getSessionData()) {
			sessionUser = (User) SessionManager.getUserSession().getSessionData().get("user");
		}
		List<?> lst_data = this.getList(
				FilterFactory.getSimpleFilter("cityname='" + cityname + "' and pk_userid=" + sessionUser.getId()));
		if (lst_data != null && lst_data.size() > 0) {
			return (DistCity) lst_data.get(0);
		}
		return null;
	}

}
