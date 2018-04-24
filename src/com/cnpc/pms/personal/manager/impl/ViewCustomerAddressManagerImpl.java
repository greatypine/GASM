package com.cnpc.pms.personal.manager.impl;

import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.personal.dao.ViewCustomerAddressDao;
import com.cnpc.pms.personal.manager.ViewCustomerAddressManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ViewCustomerAddressManagerImpl extends BizBaseCommonManager implements ViewCustomerAddressManager {

	@Override
	public Map<String, Object> showCustomerAddressData(QueryConditions conditions) {
		ViewCustomerAddressDao viewCustomerAddressDao=(ViewCustomerAddressDao)SpringHelper.getBean(ViewCustomerAddressDao.class.getName());
		//查询的数据条件
        StringBuilder sb_where = new StringBuilder();
        //分页对象
        PageInfo obj_page = conditions.getPageinfo();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar calendar = Calendar.getInstance();
        String work_month = null;
        String complete_status = "0";
        String grade="";
        //返回的对象，包含数据集合、分页对象等
        Map<String, Object> map_result = new HashMap<String, Object>();

        for (Map<String, Object> map_where : conditions.getConditions()) {
            if ("employee_no".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
                sb_where.append(" AND tc.employee_no like '").append(map_where.get("value")).append("'");
            }

            if ("store_id".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
            	sb_where.append(" AND s.store_id = '").append(map_where.get("value")).append("'");
            }

            if ("query_date".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))) {
                String query_date = map_where.get("value").toString();
                if("1".equals(query_date)){//0：本月 1：上月
                    calendar.add(Calendar.MONTH,-1);
                }
            }

            if("complete_status".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))){
                complete_status = map_where.get("value").toString();
                complete_status = complete_status.replace(",","");
            }
            
            if("grade".equals(map_where.get("key"))
                    && null != map_where.get("value") && !"".equals(map_where.get("value"))){
                grade = map_where.get("value").toString();
                grade = grade.replace(",","");
            }
        }
        work_month = sdf.format(calendar.getTime());
        map_result.put("pageinfo", obj_page);
        map_result.put("header", "客户单体画像查询");
        map_result.put("data", viewCustomerAddressDao.getViewCustomerAddressListData(sb_where.toString(),work_month,complete_status, obj_page,grade));
        return map_result;
	}

	
}
