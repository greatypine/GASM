package com.cnpc.pms.base.file.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.ibm.db2.jcc.am.i;


public class readXls {
	public static final String SHEQU = "1-社区信息";
    public static final String LOUFANG = "2-房屋信息(楼房)";
    public static final String SHANGYE = "2-房屋信息(商业楼宇)";
    public static final String PINGFANG = "2-房屋信息(平房)";
    public static final String HUXING = "3-楼房户型信息";
    
	/*
	 * 读取Excel
	 * */
	public void getExcel(String filePath){
		
		try {
			InputStream inputStream = new FileInputStream(filePath);
			 HSSFWorkbook hssfWorkbook = new HSSFWorkbook(inputStream);
			for(int i=1;i<hssfWorkbook.getNumberOfSheets();i++){
				 String sheetName = hssfWorkbook.getSheetName(i);
				 System.out.println(sheetName);
			}
			 
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;
		
		
		
		//return null;
		
	}
	
	/*
	 * 读取所有的工作簿
	 * */
	public List getSheel(InputStream inputStream){
		
		return null;
		
	}
	
	

}
