package com.cnpc.pms.base.file.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.personal.entity.BusinessType;
import com.cnpc.pms.personal.manager.BusinessTypeManager;

import sqlj.runtime.error.RuntimeErrors;

public class ReadExcelToDB {
	// 验证excel内容是否符合规则
	public StringBuffer getVerification(String filePath, String name) {
		Sheet sheet = null;
		Workbook wb = null;
		InputStream stream = null;
		StringBuffer verify = new StringBuffer("文件：" + name + " ");
		try {
			stream = getInputStream(filePath);

			if (name.endsWith("xlsx")) {
				// 2007版本
				wb = new XSSFWorkbook(stream);
			} else if (name.endsWith("xls")) {
				// 2003版本
				wb = new HSSFWorkbook(stream);
			}
			for (int i = 0; i < wb.getNumberOfSheets(); i++) {
				//System.out.println(wb.getSheetName(i));
				if ("写字楼信息".equals(wb.getSheetName(i))) {
					sheet = wb.getSheetAt(i);
				}
			}
			StringBuffer content = verificationContent(sheet);
			verify.append(content.toString());
			if (verify.toString().equals("文件：" + name + " ")) {
				verify = null;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (wb != null) {
				try {
					stream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return verify;
	}

	// 获取输入流
	public InputStream getInputStream(String filePath) throws FileNotFoundException {
		InputStream inputStream = null;
		inputStream = new FileInputStream(new File(filePath));
		return inputStream;
	}

	// 验证值写字楼内容是否符合规则
	public StringBuffer verificationContent(Sheet sheet) {
		Row row = null;
		Cell cell1 = null;// 获取序号
		Cell cell2 = null;// gb_code
		Cell cell3 = null;// 具体地址
		Cell cell4 = null;// 写字楼名称
		Cell cell5 = null;// 面积
		Cell cell6 = null;// 层数
		Cell cell7 = null;// 停车位
		Cell cell8 = null;// 公司所在楼层
		Cell cell9 = null;// 入驻公司
		Cell cell10=null;//街道gb_code
		StringBuffer villageBuffer = new StringBuffer();
		int lastRowNum = sheet.getLastRowNum();
		//System.out.println("一共读取了" + lastRowNum);
		for (int i = 2; i <= lastRowNum; i++) {
			row = sheet.getRow(i);
			if (null == row) {
				break;
			}
			cell1 = row.getCell(0);
			cell10 = row.getCell(5);
			cell2 = row.getCell(7);
			cell3 = row.getCell(8);
			cell4 = row.getCell(9);
			cell5 = row.getCell(11);
			cell6 = row.getCell(12);
			cell7 = row.getCell(13);
			cell8 = row.getCell(15);
			cell9 = row.getCell(14);
			if (cell9 == null || Cell.CELL_TYPE_BLANK == cell9.getCellType()) {
				break;
			}
			if (Cell.CELL_TYPE_BLANK != cell1.getCellType()) {
				if (Cell.CELL_TYPE_NUMERIC != cell10.getCellType()) {
					villageBuffer.append("第" + (i + 1) + "行的街道GB_CODE必须为数字");
					break;
				}
				if (Cell.CELL_TYPE_NUMERIC != cell2.getCellType()) {
					villageBuffer.append("第" + (i + 1) + "行的社区GB_CODE必须为数字,没有请填0");
					break;
				}
				if (Cell.CELL_TYPE_BLANK == cell3.getCellType()) {
					villageBuffer.append("第" + (i + 1) + "行的地址不能为空");
					break;
				}
				if (Cell.CELL_TYPE_BLANK == cell4.getCellType()) {
					villageBuffer.append("第" + (i + 1) + "行的写字楼不能为空");
					break;
				}
				if (Cell.CELL_TYPE_BLANK != cell5.getCellType()) {
					if (Cell.CELL_TYPE_NUMERIC != cell5.getCellType()) {
						villageBuffer.append("第" + (i + 1) + "行的面积不填或为数字");
						break;
					}
				}
				if (Cell.CELL_TYPE_BLANK != cell6.getCellType()) {
					if (Cell.CELL_TYPE_NUMERIC != cell6.getCellType()) {
						villageBuffer.append("第" + (i + 1) + "行的总楼层数不填或为数字");
						break;
					}
				}
				if (Cell.CELL_TYPE_BLANK != cell7.getCellType()) {
					if (Cell.CELL_TYPE_NUMERIC != cell7.getCellType()) {
						villageBuffer.append("第" + (i + 1) + "行的停车位不填或为数字");
						break;
					}
				}
				if (Cell.CELL_TYPE_BLANK != cell8.getCellType()) {
					if (Cell.CELL_TYPE_NUMERIC != cell8.getCellType()) {
						villageBuffer.append("第" + (i + 1) + "行的公司所在楼层不填或为数字");
						break;
					}
				}
				
			}
		}
		return villageBuffer;
	}

	// 判断是否为正整型
	public static boolean isNumber(String str) {
		//System.out.println(str);
		float flo = Float.parseFloat(str);
		int parseInt = (int) flo;
		str = parseInt + "";
		return str.matches("[\\d]+");
	}

	/**
	 * 获取excel的数据
	 * 
	 * @param filePath
	 * @param name
	 * @return
	 */
	public List<Map<String, String>> getExcelContent(String filePath, String name, String[] colums) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Workbook wb = null;
		Sheet sheet = null;
		InputStream stream = null;
		Map<String, String> map = null;
		Row row = null;
		try {
			stream = getInputStream(filePath);
			if (name.endsWith("xlsx")) {
				// 2007版本
				wb = new XSSFWorkbook(stream);
			} else if (name.endsWith("xls")) {
				// 2003版本
				wb = new HSSFWorkbook(stream);
			}
			sheet = wb.getSheetAt(0);
			int rowNum = sheet.getLastRowNum();// 总行数
			for (int i = 1; i <= rowNum; i++) {
				row = sheet.getRow(i);
				if (row == null || !StringUtils.isNotBlank(row.toString())) {
					break;
				}
				map = new HashMap<String, String>();
				for (int j = 0; j < colums.length; j++) {
					map.put(colums[j], getCellFormatValue(row.getCell(j)));
				}
				list.add(map);
			}
			return list;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (wb != null) {
				try {
					stream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public List<Map<String, String>> getDataFormExcel(String filePath, String name) {
		String town_name = "";
		String village_name = "";
		String office_name = "";
		String office_type = "";
		String office_area = "";
		String office_floor = "";
		String office_parking = "";
		String office_company = "";
		String office_floor_of_company = "";
		String office_address = "";
		String village_gb_code = "";
		String town_gb_code="";
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Sheet sheet = null;
		Workbook wb = null;
		InputStream stream = null;
		Map<String, String> map = null;
		Row row = null;
		try {
			stream = getInputStream(filePath);
			if (name.endsWith("xlsx")) {
				// 2007版本
				wb = new XSSFWorkbook(stream);
			} else if (name.endsWith("xls")) {
				// 2003版本
				wb = new HSSFWorkbook(stream);
			}
			for (int i = 0; i < wb.getNumberOfSheets(); i++) {
				//System.out.println(wb.getSheetName(i));
				if ("写字楼信息".equals(wb.getSheetName(i))) {
					sheet = wb.getSheetAt(i);
				}
			}
			int rowNum = sheet.getLastRowNum();// 总行数
			for (int i = 2; i <= rowNum; i++) {
				//System.out.println(i);
				row = sheet.getRow(i);
				if (row == null || !StringUtils.isNotBlank(row.toString())) {
					break;
				}
				office_company = getCellFormatValue(row.getCell(14)); // 入驻公司
				if (StringUtils.isBlank(office_company)) {
					break;
				}
				map = new HashMap<String, String>();
				String office = getCellFormatValue(row.getCell(9));
				if (office != null && StringUtils.isNotBlank(office)) {
					town_name = getCellFormatValue(row.getCell(4));// 获取街道名称
					town_gb_code = conductNumber(row.getCell(5).getNumericCellValue());// 获取街道gb_code
					village_name = getCellFormatValue(row.getCell(6));// 获取社区名称
					village_gb_code = conductNumber(row.getCell(7).getNumericCellValue());// 社区国标码
					office_address = getCellFormatValue(row.getCell(8));// 地址
					office_name = getCellFormatValue(row.getCell(9));// 写字楼名称
					office_type = getCellFormatValue(row.getCell(10));// 物业类别
					office_area = getStringsubngString(getCellFormatValue(row.getCell(11)));// 面积
					office_floor = getStringsubngString(getCellFormatValue(row.getCell(12))); // 层数
					office_parking = getStringsubngString(getCellFormatValue(row.getCell(13))); // 停车位
				}
				office_floor_of_company = getStringsubngString(getCellFormatValue(row.getCell(15))); // 公司所在楼层
				map.put("office_company", office_company);
				map.put("office_floor_of_company", office_floor_of_company);
				map.put("office_address", office_address);
				map.put("office_area", office_area);
				map.put("village_gb_code", village_gb_code);
				map.put("office_name", office_name);
				map.put("office_type", office_type);
				map.put("office_floor", office_floor);
				map.put("office_parking", office_parking);
				map.put("village_name", village_name);
				map.put("town_name", town_name);
				map.put("town_gb_code", town_gb_code);
				list.add(map);
			}
			return list;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (wb != null) {
				try {
					stream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 获取街道商业信息
	 * 
	 * @param filePath
	 * @param name
	 * @return
	 */
	public List<Map<String, String>> getBusinessInfo(String filePath, String name) {
		//System.out.println("将商业信息的数据存到list中");
		Sheet sheet = null;
		Workbook wb = null;
		Row row = null;
		InputStream stream = null;
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		try {
			stream = getInputStream(filePath);

			if (name.endsWith("xlsx")) {
				// 2007版本
				wb = new XSSFWorkbook(stream);
			} else if (name.endsWith("xls")) {
				// 2003版本
				wb = new HSSFWorkbook(stream);
			}
			// sheet=wb.getSheetAt(0);
			//System.out.println(wb.getNumberOfSheets());
			for (int j = 0; j < wb.getNumberOfSheets(); j++) {
				//System.out.println(wb.getSheetName(j));
				if ("商业信息表".equals(wb.getSheetName(j))) {
					sheet = wb.getSheetAt(j);
				}
				;
			}
			int rowNum = sheet.getLastRowNum();
			//System.out.println("从商业文件中读取到"+rowNum+"条数据");
			for (int i = 2; i <= rowNum; i++) {
				row = sheet.getRow(i);
				if (row == null || StringUtils.isBlank(row.toString())) {
					break;
				}
				map = new HashMap<String, String>();
				String town_gb_code = conductNumber(row.getCell(5).getNumericCellValue());
				String village_gb_code = conductNumber(row.getCell(7).getNumericCellValue());
				String address = getCellFormatValue(row.getCell(8));
				String business_name = getCellFormatValue(row.getCell(9));
				String two_level_index = row.getCell(10).getStringCellValue();
				String three_level_index = row.getCell(11).getStringCellValue();
				String four_level_index = row.getCell(12).getStringCellValue();
				String five_level_index = row.getCell(13).getStringCellValue();
				String shop_area = getStringsubngString(row.getCell(14).getNumericCellValue() + "");
				String range_eshop = getStringsubngString(row.getCell(15).getNumericCellValue() + "");
				String isdistribution = row.getCell(16).getStringCellValue();
				String start_business_house = conductTime(row.getCell(17).getDateCellValue());
				String end_business_house = conductTime(row.getCell(18).getDateCellValue());
				//System.out.println(i + "行" + start_business_house + "-" + end_business_house);
				map.put("village_gb_code", village_gb_code);
				map.put("address", address);
				map.put("town_gb_code", town_gb_code);
				map.put("business_name", business_name);
				map.put("two_level_index", two_level_index);
				map.put("three_level_index", three_level_index);
				map.put("four_level_index", four_level_index);
				map.put("five_level_index", five_level_index);
				map.put("shop_area", shop_area);
				map.put("range_eshop", range_eshop);
				map.put("isdistribution", isdistribution);
				map.put("start_business_house", start_business_house);
				map.put("end_business_house", end_business_house);
				list.add(map);
			}
			//System.out.println("读取商业信息文件将文件保存到List集合中的数量为:"+list.size());
			if(list.size()>rowNum-1){
				throw new RuntimeException("list中的数据大于文件中的数据,文件名为:"+name);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (wb != null) {
				try {
					stream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	private String getCellFormatValue(Cell cell) {
		String cellvalue = "";
		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:
			case Cell.CELL_TYPE_FORMULA: {
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					Date date = cell.getDateCellValue();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					cellvalue = sdf.format(date);
				} else {
					cellvalue = String.valueOf(cell.getNumericCellValue());
				}
				break;
			}
			case Cell.CELL_TYPE_STRING:
				cellvalue = cell.getRichStringCellValue().getString();
				break;
			default:
				cellvalue = "";
			}
		} else {
			cellvalue = "";
		}
		return cellvalue;

	}

	/**
	 * 日期处理函数
	 * 
	 * @param date
	 * @return
	 */
	public String conductTime(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("H:mm");
		return date != null ? dateFormat.format(date) : null;
	}

	/**
	 * 解决科学技术法处理函数
	 * 
	 * @param date
	 * @return
	 */
	public String conductNumber(double number) {
		DecimalFormat df = new DecimalFormat("0");
		return df.format(number);
	}

	/**
	 * 根据HSSFCell类型设置数据
	 * 
	 * @param cell
	 * @return
	 */
	// private String getCellFormatValue(HSSFCell cell) {
	// String cellvalue = "";
	// if (cell != null) {
	// // 判断当前Cell的Type
	// switch (cell.getCellType()) {
	// // 如果当前Cell的Type为NUMERIC
	// case HSSFCell.CELL_TYPE_NUMERIC:
	// case HSSFCell.CELL_TYPE_FORMULA: {
	// // 判断当前的cell是否为Date
	// if (HSSFDateUtil.isCellDateFormatted(cell)) {
	// // 如果是Date类型则，转化为Data格式
	//
	// //方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
	// //cellvalue = cell.getDateCellValue().toLocaleString();
	//
	// //方法2：这样子的data格式是不带带时分秒的：2011-10-12
	// Date date = cell.getDateCellValue();
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	// cellvalue = sdf.format(date);
	//
	// }
	// // 如果是纯数字
	// else {
	// // 取得当前Cell的数值
	// cellvalue = String.valueOf(cell.getNumericCellValue());
	// }
	// break;
	// }
	// // 如果当前Cell的Type为STRIN
	// case HSSFCell.CELL_TYPE_STRING:
	// // 取得当前的Cell字符串
	// cellvalue = cell.getRichStringCellValue().getString();
	// break;
	// // 默认的Cell值
	// default:
	// cellvalue = " ";
	// }
	// } else {
	// cellvalue = "";
	// }
	// return cellvalue;
	//
	// }

	public StringBuffer verificationBusinessInfo(String filePath, String name) {
		//System.out.println("验证商业信息");
		StringBuffer buffer = null;
		Sheet sheet = null;
		Workbook wb = null;
		InputStream stream = null;
		try {
			stream = getInputStream(filePath);

			if (name.endsWith("xlsx")) {
				// 2007版本
				wb = new XSSFWorkbook(stream);
			} else if (name.endsWith("xls")) {
				// 2003版本
				wb = new HSSFWorkbook(stream);
			}
			for (int i = 0; i < wb.getNumberOfSheets(); i++) {
				//System.out.println(wb.getSheetName(i));
				if ("商业信息表".equals(wb.getSheetName(i))) {
					sheet = wb.getSheetAt(i);
				}
			}
			buffer = verificationBusiness(sheet);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (wb != null) {
				try {
					stream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return buffer;
	}

	/**
	 * 验证商业信息值类型
	 * 
	 * @param sheet
	 * @return
	 */
	public StringBuffer verificationBusiness(Sheet sheet) {
		Row row = null;
		StringBuffer stringBuffer = new StringBuffer();
		int rowNum = sheet.getLastRowNum();
		//System.out.println("从商业信息sheet中读取到"+rowNum+"条数据");
		BusinessTypeManager businessTypeManager=(BusinessTypeManager)SpringHelper.getBean("businessTypeManager");
		for (int i = 2; i <= rowNum; i++) {
			row = sheet.getRow(i);
			if (null == row) {
				break;
			}
			Cell town_gb_code_cell = row.getCell(5);
			Cell gb_code_cell = row.getCell(7);
			Cell business_name = row.getCell(9);
			Cell two_level_index = row.getCell(10);
			Cell three_level_index = row.getCell(11);
			Cell four_level_index = row.getCell(12);
			Cell five_level_index = row.getCell(13);
			Cell area = row.getCell(14);
			Cell range = row.getCell(15);
			Cell start = row.getCell(17);
			Cell end = row.getCell(18);
			// System.out.println(gb_code_cell.getCellType()+"
			// "+business_name.getCellType()+"
			// "+two_level_index.getCellType()+area.getCellType()+"
			// "+range.getCellType()+" "+start.getCellType());
			// System.out.println(Cell.CELL_TYPE_NUMERIC+"
			// "+Cell.CELL_TYPE_BLANK+" ");
			if (Cell.CELL_TYPE_NUMERIC != town_gb_code_cell.getCellType()) {
				stringBuffer.append("第" + (i + 1) + "行的街道gb_code必须为数字");
				break;
			}
			if (Cell.CELL_TYPE_NUMERIC != gb_code_cell.getCellType()) {
				stringBuffer.append("第" + (i + 1) + "行的社区gb_code必须为数字，如果没有请填0");
				break;
			}
			if (Cell.CELL_TYPE_BLANK == business_name.getCellType()) {
				stringBuffer.append("第" + (i + 1) + "行的商家名称不能为空");
				break;
			}
			if (Cell.CELL_TYPE_BLANK == two_level_index.getCellType()
					|| Cell.CELL_TYPE_BLANK == three_level_index.getCellType()
					|| Cell.CELL_TYPE_BLANK == four_level_index.getCellType()
					|| Cell.CELL_TYPE_BLANK == five_level_index.getCellType()) {
				stringBuffer.append("第" + (i + 1) + "行的指标不能为空");
				break;
			}
			if (Cell.CELL_TYPE_BLANK != area.getCellType()) {
				if (Cell.CELL_TYPE_NUMERIC != area.getCellType()) {
					stringBuffer.append("第" + (i + 1) + "行的经营规模必须为空或为数字");
					break;
				}
			}
			if (Cell.CELL_TYPE_BLANK != range.getCellType()) {
				if (Cell.CELL_TYPE_NUMERIC != range.getCellType()) {
					stringBuffer.append("第" + (i + 1) + "行的距离月店必须为空或为数字");
					break;
				}
			}
			if (Cell.CELL_TYPE_BLANK != start.getCellType()) {
				if (Cell.CELL_TYPE_NUMERIC != start.getCellType()) {
					stringBuffer.append("第" + (i + 1) + "行的营业开始时间必须为空或为数字");
					break;
				}
			}
			if (Cell.CELL_TYPE_BLANK != end.getCellType()) {
				if (Cell.CELL_TYPE_NUMERIC != end.getCellType()) {
					stringBuffer.append("第" + (i + 1) + "行的营业结束时间必须为空或为数字");
					break;
				}
			}
			BusinessType businessType = businessTypeManager.getBusinessTypeByStringArray(two_level_index.getStringCellValue(),
					getArraySplit(three_level_index.getStringCellValue())[1], getArraySplit(four_level_index.getStringCellValue()), five_level_index.getStringCellValue());
			if (businessType == null) {
				throw new RuntimeException("请核对商家名称为：" + business_name + "的指标");
			}

		}

		return stringBuffer;
	}

	// 字符串转去除小数点
	public String getStringsubngString(String sting) {
		//System.out.println(sting);
		if(sting.equals("无信息")){
			return sting;
		}
		if (sting != null && StringUtils.isNotBlank(sting) && !"".equals(sting)) {
			String type = sting.substring(sting.indexOf(".") + 1);
			if (!type.matches("[1-9]+")) {
				return sting.substring(0, sting.indexOf("."));
			}
		}
		return sting;
	}
	// 按下划线切割
		public String[] getArraySplit(String str) {
			String[] strings = str.split("_");
			return strings;
		}
}
