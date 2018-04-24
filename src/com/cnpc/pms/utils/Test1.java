package com.cnpc.pms.utils;

import com.cnpc.pms.utils.excel.ExcelReaderUtil;
import com.cnpc.pms.utils.excel.IRowReader;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class Test1 implements IRowReader {
	
	public static void main(String args[]){
		IRowReader reader = new Test1();
		//ExcelReaderUtil.readExcel(reader, "F://te03.xls");
		try {
			ExcelReaderUtil.readExcel(reader, "C://Users//liu//Desktop//崇文门外店 - 副本.xlsx");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void getRows(int sheetIndex, int curRow, Map<Integer,String> rowlist) {
		System.out.print(curRow+" ");
		if(rowlist.get(10) != null && !"".equals((rowlist.get(10)))){
			System.out.println("1211111111111111111111");
		}
		for (String value : rowlist.values()) {
			System.out.print(value + " ");
		}
		System.out.println();
	}
}
