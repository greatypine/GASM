package com.cnpc.pms.personal.manager.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.personal.dao.DfMassOrderTotalDao;
import com.cnpc.pms.personal.dao.TinyVillageCodeDao;
import com.cnpc.pms.personal.entity.TinyVillage;
import com.cnpc.pms.personal.entity.TinyVillageCode;
import com.cnpc.pms.personal.entity.Town;
import com.cnpc.pms.personal.manager.TinyVillageCodeManager;
import com.cnpc.pms.personal.manager.TownManager;
import com.cnpc.pms.platform.dao.DfCustomerOrderDao;

public class TinyVillageCodeManagerImpl extends BaseManagerImpl implements TinyVillageCodeManager {

	@Override
	public TinyVillageCode findTinyVillageCodeByTinyId(Long tinyId) {
		List<?> lst_data = this.getList(FilterFactory.getSimpleFilter("tiny_village_id=" + tinyId));
		if (lst_data != null && lst_data.size() > 0) {
			return (TinyVillageCode) lst_data.get(0);
		}
		return null;
	}

	@Override
	public TinyVillageCode saveTinyVillageCode(TinyVillage tinyVillage) {
		Date date = new Date();
		java.sql.Date sdate = new java.sql.Date(date.getTime());
		// 根据小区查询是否存在小区编码
		TinyVillageCode tinyVillageCode = findTinyVillageCodeByTinyId(tinyVillage.getId());
		if (tinyVillageCode == null) {
			TinyVillageCodeDao tinyVillageCodeDao = (TinyVillageCodeDao) SpringHelper
					.getBean(TinyVillageCodeDao.class.getName());
			tinyVillageCode = new TinyVillageCode();
			// 根据街道id获取街道
			TownManager townManager = (TownManager) SpringHelper.getBean("townManager");
			Town town = townManager.findTownById(tinyVillage.getTown_id());
			Integer maxnum = tinyVillageCodeDao.findMaxTinyVillageCode(town.getGb_code());
			if (maxnum != null) {
				String strss = maxnum + 1 + "";
				if (strss.length() == 1) {
					tinyVillageCode.setCode(town.getGb_code() + "000000000" + strss);
				} else if (strss.length() == 2) {
					tinyVillageCode.setCode(town.getGb_code() + "00000000" + strss);
				} else if (strss.length() == 3) {
					tinyVillageCode.setCode(town.getGb_code() + "0000000" + strss);
				} else if (strss.length() == 4) {
					tinyVillageCode.setCode(town.getGb_code() + "000000" + strss);
				} else if (strss.length() == 5) {
					tinyVillageCode.setCode(town.getGb_code() + "00000" + strss);
				} else if (strss.length() == 6) {
					tinyVillageCode.setCode(town.getGb_code() + "0000" + strss);
				} else if (strss.length() == 7) {
					tinyVillageCode.setCode(town.getGb_code() + "000" + strss);
				} else if (strss.length() == 8) {
					tinyVillageCode.setCode(town.getGb_code() + "00" + strss);
				} else if (strss.length() == 9) {
					tinyVillageCode.setCode(town.getGb_code() + "0" + strss);
				}
			} else {
				tinyVillageCode.setCode(town.getGb_code() + "0000000001");
			}
			tinyVillageCode.setCreate_time(sdate);
			tinyVillageCode.setCreate_user_id(tinyVillage.getCreate_user_id());
			tinyVillageCode.setTiny_village_name(tinyVillage.getName());
			tinyVillageCode.setTiny_village_id(tinyVillage.getId());
			tinyVillageCode.setUpdate_time(sdate);
			tinyVillageCode.setUpdate_user_id(tinyVillage.getUpdate_user_id());
			tinyVillageCodeDao.saveTinyVillageCode(tinyVillageCode);
		} else {
			TinyVillageCodeManager tinyVillageCodeManager = (TinyVillageCodeManager) SpringHelper
					.getBean("tinyVillageCodeManager");
			tinyVillageCode.setTiny_village_name(tinyVillage.getName());
			tinyVillageCode.setTiny_village_id(tinyVillage.getId());
			tinyVillageCode.setUpdate_time(sdate);
			tinyVillageCode.setUpdate_user_id(tinyVillage.getUpdate_user_id());
			tinyVillageCodeManager.saveObject(tinyVillageCode);
		}
		return tinyVillageCode;
	}

	@Override
	public TinyVillageCode findTinyVillageByCode(String code) {
		List<?> lst_data = this.getList(FilterFactory.getSimpleFilter("code", code));
		if (lst_data != null && lst_data.size() > 0) {
			return (TinyVillageCode) lst_data.get(0);
		}
		return null;
	}

	@Override
	public void syncTinyVillageCustomer() {
		DfCustomerOrderDao dfCustomerOrderDao = (DfCustomerOrderDao) SpringHelper
				.getBean(DfCustomerOrderDao.class.getName());
		DfMassOrderTotalDao dfMassOrderTotalDao = (DfMassOrderTotalDao) SpringHelper
				.getBean(DfMassOrderTotalDao.class.getName());
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String date1 = "2018-01-05 11:50:00";
			// 获取系统当前时间
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String date2 = format.format(date);
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.getTime() > dt2.getTime()) {
				dfCustomerOrderDao.syncTinyVillageCustomer();
			}
			dfMassOrderTotalDao.findTinyVillageCustomer();
			Date date3 = new Date();
			String date34 = format.format(date);
			System.out.println("小区以消费用时" + date34);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
