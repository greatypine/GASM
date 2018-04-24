package com.cnpc.pms.personal.manager;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.personal.dto.CompanyDTO;
import com.cnpc.pms.personal.entity.Company;
import com.cnpc.pms.personal.entity.Office;

public interface OfficeManager extends IManager{
	/**
	 * 根据写字楼名称和社区id查看写字楼信息
	 * @param office_name
	 * @return
	 */
	Office getOfficeByOffice_name(String office_name,Long village_id);
	 /**
     * 获取写字楼对象
     * 
     * @param conditions 查询条件
     * @return
     */
    Map<String, Object> getOfficeList(QueryConditions conditions);

    
    
    Result saveOffice(CompanyDTO companydto);
    
    

    
    /**
     * 获取写字楼对象
     * 
     * @param id 写字楼id
     * @return
     */
    Map<String, Object> getOfficeById(Integer id);
    /**
     * 获取写字楼对象
     * 
     * @param office 写字楼信息
     * @return
     */
    Office updateOffice(Office office);
    /**
     * 导出excel表格
     * @return
     */
    File exportOfficeExce(Map<String ,Object > param) throws IOException, Exception;
    
    /**
     * 全信息导出excel表格
     * @return
     */
    File exportWholeOfficeExce(Map<String ,Object > param) throws IOException, Exception;
    /**
     * chek完写字楼信息，信息重新导入
     */
	void saveList(List<Office> officeList,List<Company> companyList);

    /**
	 * 根据写字楼名称和社区id和地址信息查看写字楼信息
	 * @param office_name
	 * @return
	 */
    Result getOfficeByOfficeAddress_name(CompanyDTO companydto);
    
    /**
	 * 根据写字楼名称和社区id查看写字楼信息
	 * @param office_name
	 * @return
	 */
	Office getOfficeByOffice_name_address(String office_name,Long village_id,String address);
	
	Result saveOfficeData(Office office);
	
	Result getOfficeByOfficeAddress_name_data(Office office1);
	
	Result getOfficeByOfficeAddressandnamedata(Office office1);
	
	Result findOfficeByOfficeAddressandnamedata(Office office1);
	
	Result getOfficeByOfficeAddressnamedata(Office office1);
	
	Result save_OfficeData(Office office1);
	
	/**
     * 获取写字楼对象
     * 
     * @param id 写字楼id
     * @return
     */
    Office getOfficeDataById(Integer id);
	
	Office versionOfficeInfo(Office office);
	
	
	

}
