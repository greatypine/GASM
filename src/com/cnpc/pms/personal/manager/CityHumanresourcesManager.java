package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.personal.entity.CityHumanresources;
import com.cnpc.pms.personal.entity.Express;
import com.cnpc.pms.personal.entity.Humanresources;
import com.cnpc.pms.personal.entity.ImportHumanresources;
import com.cnpc.pms.personal.entity.XxExpress;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;


public interface CityHumanresourcesManager extends IManager {
	
	public Map<String, Object> queryCityHumanList(QueryConditions condition);
    
    public String saveHumanresourceCSHuman(List<File> lst_import_excel) throws Exception;
    
    public File exportCityHumanExcel() throws Exception;
    
    public CityHumanresources queryCityHumanresourceById(Long id);
    
    public CityHumanresources updateCityHumanresources(CityHumanresources cityHumanresources );
    
    public CityHumanresources saveCityHumanresources(CityHumanresources cityHumanresources );
    
    public CityHumanresources validateCardNumber(String cardnumber);
    
    public CityHumanresources validatePhone(String phone);
    
    public CityHumanresources queryCityHumanresourcesByEmployeeNo(String employee_no);
 
}
