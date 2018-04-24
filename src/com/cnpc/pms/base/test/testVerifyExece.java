package com.cnpc.pms.base.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cnpc.pms.base.file.utils.ReadExcelToDB;

public class testVerifyExece {
	/*public static void main(String[] args) {
		String filePath="C:/Users/sunning/Desktop/街道商业信息表-新模板.xlsx";
		String fileName="街道商业信息表-新模板.xlsx";
		ReadExcelToDB readExcelToDB = new ReadExcelToDB();
		StringBuffer buffer = readExcelToDB.getVerification(filePath, fileName);
		System.out.println(buffer.toString());
		List<Map<String, String>> list = readExcelToDB.getDataFormExcel(filePath, fileName);
		System.out.println("入驻公司数量"+list.size());
		for (Map<String, String> map : list) {
			String office_company=map.get("office_company");
			String office_floor_of_company=map.get("office_floor_of_company");
			String office_address=map.get("office_address");
			String office_area=map.get("office_area");
			String office_name=map.get("office_name");
			String office_type=map.get("office_type");
			String office_floor=map.get("office_floor");
			String office_parking=map.get("office_parking");
			String village_name=map.get("village_name");
			String town_name=map.get("town_name");
			String village_gb_code=map.get("village_gb_code");
			System.out.println("街道:"+town_name+";社区:"+village_name+"; 社区国标码："+village_gb_code+"; 地址："+office_address+"; 写字楼："+office_name+"; 物业级别："+office_type+"; 面积："+office_area
					+"; 总层数："+office_floor+"; 停车位："+office_parking+"; 入驻公司："+office_company+"; 公司所在楼层："+office_floor_of_company);
			
			
		}
		List<Map<String, String>> businessInfo = readExcelToDB.getBusinessInfo(filePath, fileName);
		for (Map<String, String> map : businessInfo) {
			String village_gb_code=map.get("village_gb_code");
			String address=map.get("address");
			String business_name=map.get("business_name");
			String two_level_index=map.get("two_level_index");
			String three_level_index=map.get("three_level_index");
			String four_level_index=map.get("four_level_index");
			String five_level_index=map.get("four_level_index");
			String shop_area=map.get("shop_area");
			String range_eshop=map.get("range_eshop");
			String isdistribution=map.get("isdistribution");
			String start_business_house=map.get("start_business_house");
			String end_business_house=map.get("end_business_house");
			System.out.println(village_gb_code+" "+address+" "+business_name+" "+two_level_index+" "+three_level_index+" "+four_level_index+" "+five_level_index
					+" "+shop_area+" "+range_eshop+" "+isdistribution+" "+start_business_house+" "+end_business_house);
		}
		
	}*/
	
	
	public static void main(String[] args) {
		//Date date=new Date();
		String filePath="C:/Users/sunning/Desktop/已验证数据/商业信息/北京/昌平区--天通苑北街道商业信息表.xlsx";
		ReadExcelToDB readExcelToDB = new ReadExcelToDB();
		//String time = readExcelToDB.conductTime(date);
		//System.out.println(time);
		Workbook wb=null;
		Sheet sheet=null;
		try {
			InputStream inputStream = readExcelToDB.getInputStream(filePath);
			wb = new XSSFWorkbook(inputStream);
			for (int i = 0; i < wb.getNumberOfSheets(); i++) {
	        	System.out.println(wb.getSheetName(i));
				if("商业信息表".equals(wb.getSheetName(i))){
					sheet=wb.getSheetAt(i);
				}
			}
			StringBuffer buffer = readExcelToDB.verificationBusiness(sheet);
			if("".equals(buffer.toString())){
				System.out.println("验证通过");
			}
			
			
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
