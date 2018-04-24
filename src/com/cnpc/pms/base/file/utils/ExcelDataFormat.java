package com.cnpc.pms.base.file.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cnpc.pms.personal.entity.Building;
import com.cnpc.pms.personal.entity.House;
import com.cnpc.pms.personal.entity.TinyVillage;
import com.cnpc.pms.personal.entity.Village;
import com.google.gson.Gson;

//import org.codehaus.plexus.util.StringUtils;
/*import com.google.common.collect.Lists;
import com.google.common.collect.Maps;*/

public class ExcelDataFormat {
	private static Log logger = LogFactory.getLog(ExcelDataFormat.class);
	public static final String SHEQU = "1-社区信息";
	public static final String LOUFANG = "2-房屋信息(楼房)";
	public static final String SHANGYE = "2-房屋信息(商业楼宇)";
	public static final String PINGFANG = "2-房屋信息(平房)";
	public static final String GUANGCHANG = "2-房屋信息(公园、广场)";
	public static final String OTHER = "2-房屋信息(其它)";
	public static final String HUXING = "3-楼房户型信息";
	public static final String ZHENGLI = "整理表";

	/**
	 * 生成整理后的excel
	 * 
	 * @param filePath
	 * @param houseList
	 * @return
	 */
	public static XSSFWorkbook generatorExcel(String filePath, String picPath, List houseList, List villageList) {

		//
		//
		XSSFWorkbook wb = null;
		try {
			wb = new XSSFWorkbook(new FileInputStream(filePath));
			// WritableWorkbook wwb = Workbook.createWorkbook(new
			// File(filePath+""),wb);
			XSSFSheet wsheet = wb.createSheet(ZHENGLI);
			Row wrow = wsheet.createRow(0);

			// 生成标题行
			int i = 0;
			wrow.createCell(i++).setCellValue("房屋类型");
			wrow.createCell(i++).setCellValue("   地址   ");
			wrow.createCell(i++).setCellValue("楼号");
			wrow.createCell(i++).setCellValue("单元");
			wrow.createCell(i++).setCellValue("房号");
			wrow.createCell(i++).setCellValue("商业楼层");

			// 生成房屋信息列表
			House house = null;
			for (int j = 0; j < houseList.size(); j++) {
				i = 0;
				wrow = wsheet.createRow((int) j + 1);
				house = (House) houseList.get(j);
				wrow.createCell(i++)
						.setCellValue(houseType(house.getHouse_type() == null ? -1 : house.getHouse_type()));
				wrow.createCell(i++).setCellValue(house.getAddress() == null ? "" : house.getAddress());
				wrow.createCell(i++).setCellValue(house.getBuilding_number() == null ? "" : house.getBuilding_number());
				wrow.createCell(i++)
						.setCellValue(house.getBuilding_unit_number() == null ? "" : house.getBuilding_unit_number());
				wrow.createCell(i++).setCellValue(
						house.getBuilding_room_number() == null ? "" : house.getBuilding_room_number() + "");
				wrow.createCell(i++).setCellValue(house.getCommercial_floor_number());
			}
			XSSFSheet wsheet2 = wb.createSheet("社区");
			Row wrow2 = wsheet2.createRow(0);
			// 生成标题行
			int j = 0;
			wrow2.createCell(j++).setCellValue("所属街道");
			wrow2.createCell(j++).setCellValue("社区名");
			wrow2.createCell(j++).setCellValue("总面积");
			wrow2.createCell(j++).setCellValue("居委会地址");
			wrow2.createCell(j++).setCellValue("居委会电话");
			wrow2.createCell(j++).setCellValue("总户数");
			wrow2.createCell(j++).setCellValue("常住人口");
			wrow2.createCell(j++).setCellValue("辖区范围");
			// 生成社区
			j = 0;
			Village village = (Village) villageList.get(0);
			Row wrow3 = wsheet2.createRow(1);
			wrow3.createCell(j++).setCellValue(village.getTown() == null ? "" : village.getTown());
			wrow3.createCell(j++).setCellValue(village.getName() == null ? "" : village.getName());
			wrow3.createCell(j++).setCellValue(village.getSquare_area() == null ? "" : village.getSquare_area());
			wrow3.createCell(j++)
					.setCellValue(village.getCommittee_address() == null ? "" : village.getCommittee_address());
			wrow3.createCell(j++)
					.setCellValue(village.getCommittee_phone() == null ? "" : village.getCommittee_phone());
			wrow3.createCell(j++)
					.setCellValue(village.getHousehold_number() == 0 ? "" : village.getHousehold_number() + "");
			wrow3.createCell(j++).setCellValue(
					village.getResident_population_number() == 0 ? "" : village.getResident_population_number() + "");
			wrow3.createCell(j++).setCellValue(village.getIntroduction() == null ? "" : village.getIntroduction());
			if (new File(picPath).exists()) {
				FileInputStream fis = new FileInputStream(picPath);
				// 将图片添加到xlsx文件
				int picinx = wb.addPicture(fis, wb.PICTURE_TYPE_PNG);
				fis.close();
				// 在对应sheet中创建图片区域
				XSSFCreationHelper helper = wb.getCreationHelper();
				XSSFDrawing drawing = wsheet2.createDrawingPatriarch();
				// add a picture shape
				XSSFClientAnchor anchor = helper.createClientAnchor();
				// 设置图片的左上角在excel中的位置
				anchor.setCol1(0);
				anchor.setRow1(3);
				// 将anchor与图片关联
				XSSFPicture pic = drawing.createPicture(anchor, picinx);
				// 自动刷新图片大小，使得图片左上角在前面设置的位置
				pic.resize();
			}
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			return null;
		}
		return wb;
	}

	private static String houseType(int i) {
		if (i == 0)
			return "平房";
		else if (i == 1)
			return "住宅楼";
		else if (i == 2)
			return "商业楼";
		else
			return "";
	}

	/**
	 * 取得楼房和住宅列表
	 * 
	 * @param sheet
	 * @return
	 */
	private static Map<TinyVillage, List<Map<Building, List<House>>>> getBuildAndHouseList(Sheet sheet) {

		List<Map<Building, List<House>>> list = null;
		List<House> houseList = null;
		List<House> newHouseList = null;
		TinyVillage tv = null;
		Building building = null;
		TreeSet<String> set = null;

		// 取得住宅列表
		Map<TinyVillage, List<Map<Building, List<House>>>> tvbMap = new HashMap();
		Map<Building, List<House>> bhMap = null;
		Map<TinyVillage, List<House>> tvMap = getHouseList(sheet);

		for (Map.Entry<TinyVillage, List<House>> entry : tvMap.entrySet()) {
			tv = entry.getKey();
			houseList = entry.getValue();
			if (houseList == null)
				continue;
			list = new ArrayList();
			set = new TreeSet<String>();
			for (House house : houseList) {
				set.add(house.getBuilding_number());
			}
			Iterator<String> iterator = set.iterator();
			String number = null;

			while (iterator.hasNext()) {
				number = iterator.next();
				building = new Building();
				building.setName(number + "号楼");
				building.setType(1); // 楼房
				newHouseList = new ArrayList();
				bhMap = new HashMap();
				for (House house : houseList) {
					if (number.equals(house.getBuilding_number())) {
						newHouseList.add(house);
					} else {
						continue;
					}
				}
				bhMap.put(building, newHouseList);
				list.add(bhMap);
			}
			tvbMap.put(tv, list);
		}
		return tvbMap;
	}

	/**
	 * 获取每一sheet的房屋集合.
	 * 
	 * @param sheet
	 * @return
	 */
	private static Map<TinyVillage, List<House>> getHouseList(Sheet sheet) {

		List<House> houseList = null;
		Row row = null;
		Cell cell1 = null;
		Cell cell2 = null;
		Cell cell3 = null;
		Cell cell4 = null;
		Cell cell5 = null;
		Cell cell7 = null;// 小区户数
		TinyVillage tinyVillage = null;
		Map<TinyVillage, List<House>> tvMap = new HashMap();

		if (sheet == null) {
			return null;
		}

		for (int j = 2; j <= sheet.getLastRowNum(); j++) {
			row = sheet.getRow(j);
			if (row == null)
				break;
			cell1 = row.getCell(1); // 小区名
			cell2 = row.getCell(2); // 小区地址
			cell7 = row.getCell(7); // 小区户数
			if (j == 2) {
				cell3 = row.getCell(3);
				cell4 = row.getCell(4);
				cell5 = row.getCell(5);
			}
			/*
			 * if(cell3.getCellType() == Cell.CELL_TYPE_NUMERIC){//数字 double
			 * cellvalue = cell3.getNumericCellValue(); java.text.DecimalFormat
			 * formatter = new DecimalFormat("#");
			 * 
			 * }else if(cell3.getCellType() == Cell.CELL_TYPE_STRING){//字符
			 * String cellValue = cell3.getStringCellValue();
			 * 
			 * }
			 * 
			 * if(cell4.getCellType() == Cell.CELL_TYPE_NUMERIC){//数字 double
			 * cellvalue = cell4.getNumericCellValue(); java.text.DecimalFormat
			 * formatter = new DecimalFormat("#");
			 * 
			 * }else if(cell4.getCellType() == Cell.CELL_TYPE_STRING){//字符
			 * String cellValue = cell4.getStringCellValue();
			 * 
			 * }
			 * 
			 * 
			 * if(cell5.getCellType() == Cell.CELL_TYPE_NUMERIC){//数字 double
			 * cellvalue = cell5.getNumericCellValue(); java.text.DecimalFormat
			 * formatter = new DecimalFormat("#");
			 * 
			 * }else if(cell5.getCellType() == Cell.CELL_TYPE_STRING){//字符
			 * String cellValue = cell5.getStringCellValue();
			 * 
			 * }
			 */

			if (row.getCell(3) == null && cell3 != null) { // 楼号
				row.createCell(3);
				row.getCell(3).setCellValue(cell3.toString());
			} else if (row.getCell(3) != null && StringUtils.isBlank(row.getCell(3).toString())) {
				row.getCell(3).setCellValue(cell3.toString());
			} else {
				cell3 = row.getCell(3);
			}

			if (row.getCell(4) == null && cell4 != null) { // 单元
				row.createCell(4);
				row.getCell(4).setCellValue(cell4.toString());
			} else if (row.getCell(4) != null && StringUtils.isBlank(row.getCell(4).toString())) {
				row.getCell(4).setCellValue(cell4.toString());
			} else {
				cell4 = row.getCell(4);
			}
			cell5 = row.getCell(5); // 楼层
			// 没有楼层说明后面没有row了
			if (cell5 == null || StringUtils.isBlank(cell5.toString()))
				break;

			// 如果小区不为空
			if (cell1 != null && StringUtils.isNotBlank(cell1.toString())) {

				// 如果之前的行确定了小区，而且该小区与本行的小区不一致 --》目前小区的房屋续收集完毕，切换小区
				if (tinyVillage != null && !cell1.toString().equals(tinyVillage.getName())) {
					tvMap.put(tinyVillage, houseList);
				}

				if (tinyVillage == null || tinyVillage != null && !cell1.toString().equals(tinyVillage.getName())) {
					// 创建新小区对象
					tinyVillage = new TinyVillage();
					houseList = new ArrayList();
					tinyVillage.setTinyvillage_type(1); // 楼房
					tinyVillage.setName(cell1.toString());
					if (cell7 == null || StringUtils.isBlank(cell7.toString())) {
						throw new RuntimeException("楼房中小区居民户数不能为空,且必须为正整数");
					}
					tinyVillage.setResidents_number(Integer.parseInt(cell7.toString().split("\\.")[0]));
					if (cell2 != null) {
						tinyVillage.setAddress(cell2.toString());
					}
				}
			}
			// 取得房屋信息列表 t_house
			houseList.addAll(getHouseList(row, tinyVillage));
		}
		// return getMapFromHouseList(houseList,TinyVillageList);
		tvMap.put(tinyVillage, houseList);
		return tvMap;
	}

	/**
	 * 获取小区与小区所在房屋的对照列表.
	 * 
	 * @param houseList
	 * @param TinyVillageList
	 * @return
	 */
	/*
	 * private static Map<TinyVillage,List<House>> getMapFromHouseList(
	 * List<House> houseList, List<TinyVillage> TinyVillageList) {
	 * 
	 * TinyVillage tinyVillage = null; House house = null; List<House> list =
	 * null; Map<TinyVillage,List<House>> tvMap = new HashMap();
	 * 
	 * for(int i=0;i<TinyVillageList.size();i++) { //取得小区 tinyVillage =
	 * TinyVillageList.get(i); list = new ArrayList(); //取得小区地址 for (int j=0;
	 * j<houseList.size(); j++) { house = houseList.get(j);
	 * if(isSameTinyVillage(tinyVillage.getName(), house.getAddress())){
	 * list.add(house); } } tvMap.put(tinyVillage, list); // } return tvMap; }
	 */

	/**
	 * 判断是否同属一个小区
	 * 
	 * @param tiny
	 * @param address
	 * @return boolean
	 */
	/*
	 * private static boolean isSameTinyVillage(String tiny,String address){
	 * return address!=null&&address.startsWith(tiny); }
	 */

	/**
	 * 获取每一row的房屋集合(楼房) 楼号cell3，单元cell4，楼层cell5，房号cell6依次分别为一对多关系
	 * 通过三级嵌套循环获取所有房屋地址信息
	 * 
	 * @param row
	 * @return
	 */
	private static List<House> getHouseList(Row row, TinyVillage tinyVillage) {

		List<House> houseList = new ArrayList();
		House house = null;
		String room = null;
		String roomTemp = null;
		Cell cell3 = row.getCell(3); // 楼号
		List<String> unitList = getUnitList(row.getCell(4), ",|、|，", "∽|-|~"); // 单元
		List<String> floorList = getFloorList(row.getCell(5), "∽|~"); // 楼层
		List<String> roomList = getRoomList(row.getCell(6), ",|、|，", "∽|~"); // 房号

		for (int i = 0; i < unitList.size(); i++) {
			for (int j = 0; j < floorList.size(); j++) {
				for (int k = 0; k < roomList.size(); k++) {
					// System.out.printf("village:%s\tunit:%s\tfloor%s\troom%s\n",
					// tinyVillage.getName(), unitList.get(i), floorList.get(j),
					// roomList.get(k));
					// 取房间号
					room = roomList.get(k);
					switch (room.length()) {
					case 3:
						roomTemp = room.substring(1, room.length());
						break;
					case 4:
						roomTemp = room.substring(2, room.length());
						break;
					case 5:
						roomTemp = room.substring(2, room.length());
						break;
					default:
						roomTemp = room;
					}
					if (room.indexOf("甲") != -1 || room.indexOf("乙") != -1) {
						roomTemp = room.substring(1, room.length());
					} else if (room.indexOf("伙单") != -1) {
						roomTemp = room;
					} else if (room.indexOf("办公区") != -1) {
						roomTemp = room;
					} else if (room.indexOf("之") != -1) {
						roomTemp = room;
					} else if (room.indexOf("西") != -1) {
						roomTemp = room;
					} else if (ifLetter(room)) {
						roomTemp = room;
					}
					house = new House();
					house.setHouse_type(1); // 楼房
					if (tinyVillage != null) {
						// house.setAddress(tinyVillage.getAddress()+tinyVillage.getName());
						house.setAddress(tinyVillage.getName());
					}
					house.setBuilding_number(cell3.toString().replaceAll("\\.0", ""));
					house.setBuilding_unit_number(unitList.get(i).toString().replaceAll("\\.0", ""));
					if (roomList.size() == 1 || room.indexOf("%") != -1) {
						System.out.printf("village:%s\tunit:%s\tfloor%s\troom%s\n", tinyVillage.getName(),
								unitList.get(i), floorList.get(j), roomList.get(k));
						/*
						 * if(roomTemp.replaceAll("\\.0", "").indexOf("-")>=0){
						 * house.setBuilding_room_number(
						 * Integer.parseInt(roomTemp.replaceAll("\\.0",
						 * "").replaceAll("\\-", ""))); }else{
						 * house.setBuilding_room_number(
						 * Integer.parseInt(roomTemp.replaceAll("\\.0", ""))); }
						 */
						if (roomTemp.indexOf("商铺") != -1) {
							roomTemp = floorList.get(j).toString().replaceAll("\\.0", "") + "层" + roomTemp;
						} else if (roomTemp.indexOf("底商") != -1) {
							roomTemp = floorList.get(j).toString().replaceAll("\\.0", "") + "层" + roomTemp;
						} else if (roomTemp.indexOf("大厅") != -1) {
							roomTemp = floorList.get(j).toString().replaceAll("\\.0", "") + "层" + roomTemp;
						} else if (roomTemp.indexOf("物业") != -1) {
							roomTemp = floorList.get(j).toString().replaceAll("\\.0", "") + "层" + roomTemp;
						} else if (roomTemp.indexOf("浴场") != -1) {
							roomTemp = floorList.get(j).toString().replaceAll("\\.0", "") + "层" + roomTemp;
						} else if (roomTemp.indexOf("公寓") != -1) {
							roomTemp = floorList.get(j).toString().replaceAll("\\.0", "") + "层" + roomTemp;
						} else if (roomTemp.indexOf("办公区") != -1) {
							roomTemp = floorList.get(j).toString().replaceAll("\\.0", "") + "层" + roomTemp;
						} else if (roomTemp.indexOf("车库") != -1) {
							roomTemp = floorList.get(j).toString().replaceAll("\\.0", "") + "层" + roomTemp;
						} else if (roomTemp.indexOf("库房") != -1) {
							roomTemp = floorList.get(j).toString().replaceAll("\\.0", "") + "层" + roomTemp;
						} else if (roomTemp.indexOf("伙单") != -1) {
							roomTemp = roomTemp;
						} else if (roomTemp.indexOf("健身") != -1) {
							roomTemp = floorList.get(j).toString().replaceAll("\\.0", "") + "层" + roomTemp;
						} else if (roomTemp.indexOf("栋3层别墅") != -1) {
							roomTemp = roomTemp;
						} else if (roomTemp.indexOf("外复式") != -1) {
							roomTemp = floorList.get(j).toString().replaceAll("\\.0", "") + roomTemp;
						} else if (roomTemp.indexOf("内复式") != -1) {
							roomTemp = roomTemp;
						} else if (roomTemp.indexOf("别墅") != -1) {
							roomTemp = roomTemp;
						} else if (ifLetter(roomTemp)) {
							roomTemp = roomTemp.replace("%", floorList.get(j).toString().replaceAll("\\.0", ""));
						}
						house.setBuilding_room_number(roomTemp.replaceAll("\\.0", ""));
					} else {
						System.out.printf("village:%s\tunit:%s\tfloor%s\troom%s\n", tinyVillage.getName(),
								unitList.get(i), floorList.get(j), roomList.get(k));
						if (roomTemp.indexOf("栋3层别墅") != -1) {
							house.setBuilding_room_number(roomTemp.replaceAll("\\.0", ""));
						}
						if (roomTemp.indexOf("3层别墅") != -1) {
							house.setBuilding_room_number(roomTemp.replaceAll("\\.0", ""));
						} else if (roomTemp.indexOf("伙单") != -1) {
							house.setBuilding_room_number(roomTemp.replaceAll("\\.0", ""));
						} else if (roomTemp.indexOf("内复式") != -1) {
							house.setBuilding_room_number(roomTemp.replaceAll("\\.0", ""));
						} else if (roomTemp.indexOf("之") != -1) {
							house.setBuilding_room_number(roomTemp.replaceAll("\\.0", ""));
						} else if (roomTemp.indexOf("西") != -1) {
							house.setBuilding_room_number(roomTemp.replaceAll("\\.0", ""));
						} else {
							house.setBuilding_room_number(floorList.get(j).toString().replaceAll("\\.0", "")
									+ roomTemp.replaceAll("\\.0", ""));
						}

					}
					house.setAddress(getAddressStr(tinyVillage, house));
					house.setBungalow_number(floorList.get(j).toString().replaceAll("\\.0", ""));
					houseList.add(house);
					//
				}
			}
		}
		//
		return houseList;
	}

	/*
	 * private static boolean lengthTag(List<String> roomList){ for(String
	 * s:roomList){ if(s!=null&&s.length()==3) return true; } return false; }
	 */

	/**
	 * 拼楼房完整地址 如果楼房本身构成一个小区，拼字符串的时候把“N号楼”去掉
	 * 
	 * @param tinyVillage
	 * @param house
	 * @return
	 */
	private static String getAddressStr(TinyVillage tinyVillage, House house) {

		String str = house.getBuilding_number() + "号楼";
		if (tinyVillage.getName().contains(str.subSequence(0, str.length())))
			str = tinyVillage.getName() + house.getBuilding_unit_number() + "单元" + house.getBuilding_room_number()
					+ "号";
		else
			str = tinyVillage.getName() + house.getBuilding_number() + "号楼" + house.getBuilding_unit_number() + "单元"
					+ house.getBuilding_room_number() + "号";
		return str;
	}

	/**
	 * 获取每个sheet中的默认小区名cell1和小区地址cell2 小区名和小区地址在每个sheet第二行第一列和第二列
	 * 
	 * @param sheet
	 * @return
	 */
	/*
	 * private static Map<String,String> getDefaultCommunity(Sheet sheet){ Map
	 * communityMap = new HashMap(); Row addressrow = sheet.getRow(2); Cell
	 * addresscell = addressrow.getCell(1); communityMap.put("communityName",
	 * addressrow.getCell(1).toString()); communityMap.put("communityAddress",
	 * addressrow.getCell(2).toString()); return communityMap; }
	 */

	/**
	 * 获取一个cell中单元集合
	 * 
	 * @param cell
	 *            单元格
	 * @param regex
	 *            分隔符
	 * @param waveRegex
	 *            连接符
	 * @return
	 */
	private static List<String> getUnitList(Cell cell, String regex, String waveRegex) {

		List<String> unitList = null;
		if (cell != null) {
			unitList = new ArrayList();
			String[] strArray = getNumberList(cell.toString().replaceAll("\\s", ""), regex);
			for (int i = 0; i < strArray.length; i++) {
				unitList.addAll(getNumberListByWaves(strArray[i], waveRegex));
			}
		}
		return unitList;
	}

	/**
	 * 获取一个cell中楼层集合
	 * 
	 * @param cell
	 * @param waveRegex
	 *            "∽"
	 * @return
	 */
	private static List<String> getFloorList(Cell cell, String waveRegex) {

		List<String> floorList = null;
		if (cell != null) {
			floorList = new ArrayList();
			floorList.addAll(getNumberListByWaves(cell.toString().replaceAll("\\s", ""), waveRegex));
		}
		return floorList;
	}

	private static List<String> getFloorDataList(String string, String waveRegex) {

		List<String> floorList = null;
		if (string != null) {
			floorList = new ArrayList();
			floorList.addAll(getNumberListByWaves(string.replaceAll("\\s", ""), waveRegex));
		}
		return floorList;
	}

	/**
	 * 获取一个cell中房屋集合 101、102、103、2-5层同上 101-103，2-5层同上
	 * 
	 * @param cell
	 * @param regex
	 *            ","
	 * @param waveRegex
	 *            "∽"
	 * @return
	 */
	private static List<String> getRoomList(Cell cell, String regex, String waveRegex) {

		List<String> roomList = null;
		if (cell != null) {
			roomList = new ArrayList();
			if (getStringFromCell(cell).indexOf("外复式") != -1) {
				roomList.add("01外复式");

			} else if (ifLetter(getStringFromCell(cell))) {
				if (getStringFromCell(cell) != null) {
					roomList.addAll(getLetter(getStringFromCell(cell), regex, waveRegex));
				}
				return roomList;
			} else if (getStringFromCell(cell).indexOf("内复式") != -1) {
				roomList.addAll(getLetterNeifushi(getStringFromCell(cell), regex, waveRegex));
			} else if (getStringFromCell(cell).indexOf("复式") != -1) {
				roomList.add(cell.toString());
			} else if (getStringFromCell(cell).indexOf("商铺") != -1) {
				roomList.add("商铺");
			} else if (getStringFromCell(cell).indexOf("底商") != -1) {
				roomList.add("底商");
			} else if (getStringFromCell(cell).indexOf("物业") != -1) {
				roomList.add("物业");
			} else if (getStringFromCell(cell).indexOf("办公区") != -1) {
				roomList.add("办公区");
			} else if (getStringFromCell(cell).indexOf("大厅") != -1) {
				roomList.add("大厅");
			} else if (getStringFromCell(cell).indexOf("浴场") != -1) {
				roomList.add("浴场");
			} else if (getStringFromCell(cell).indexOf("公寓") != -1) {
				roomList.add("公寓");
			} else if (getStringFromCell(cell).indexOf("车库") != -1) {
				roomList.add("车库");
			} else if (getStringFromCell(cell).indexOf("库房") != -1) {
				roomList.add("库房");
			} else if (getStringFromCell(cell).indexOf("伙单") != -1) {
				if (getStringFromCell(cell).indexOf("∽") != -1) {
					String all1 = getStringFromCell(cell).replaceAll("伙单", "");
					List<String> list1 = getFloorDataList(all1, "∽|~");
					for (String string : list1) {
						roomList.add(string + "伙单");
					}
				} else if (getStringFromCell(cell).indexOf("、") != -1) {
					String[] split = getStringFromCell(cell).split("、");
					if (split != null && split.length > 0) {
						for (int i = 0; i < split.length; i++) {
							roomList.add(split[i]);
						}
					}
				} else {
					roomList.add(getStringFromCell(cell));
				}
			} else if (getStringFromCell(cell).indexOf("健身") != -1) {
				roomList.add("健身");
			} else if (getStringFromCell(cell).indexOf("栋3层别墅") != -1) {
				String all = getStringFromCell(cell).replaceAll("栋3层别墅", "");
				List<String> list = getFloorDataList(all, "∽|~");
				for (String string : list) {
					roomList.add(string + "01栋3层别墅");
				}
			} else if (getStringFromCell(cell).indexOf("3层别墅") != -1) {
				roomList.addAll(getThreeHouse(getStringFromCell(cell), regex, waveRegex));
			} else if (getStringFromCell(cell).indexOf("别墅") != -1) {
				roomList.add(cell.toString());
			} else if (getStringFromCell(cell).indexOf("之") != -1) {
				roomList.addAll(getZHiHouse(getStringFromCell(cell), regex, waveRegex));
			} else {
				String[] strArray = getNumberList(getStringFromCell(cell).replaceAll("\\s", ""), regex);
				// int r = strArray.length;
				// if(r>1)r=strArray.length-1;
				for (int i = 0; i < strArray.length; i++) {
					if (VerifyExcelDataFormat.isContainChineseelse(strArray[i]))
						continue;// 汉子

					// 判断当前房号非数字
					if (ifNumber(strArray[i])) {// 是数字
						roomList.addAll(getNumberListByWaves(strArray[i], waveRegex));
					} else if (strArray[i].indexOf("甲") > -1 || strArray[i].indexOf("乙") > -1) {
						roomList.addAll(getNumberListByWaves(strArray[i], waveRegex));
						;
					} else {// 非数字
						roomList.addAll(getNumberListByWavesNotAllNumber(strArray[i], waveRegex));
					}
				}

			}

		}
		return roomList;
	}

	/**
	 * 判断数据是否为数字
	 **/
	private static boolean ifNumber(String str) {
		boolean temp = false;
		try {
			Double.parseDouble(str);
			temp = true;
		} catch (Exception e) {
			temp = false;
		}
		return temp;
	}

	/**
	 * 例如：分解101∽104到集合{101,102,103,104}
	 * 
	 * @param str
	 * @param regex
	 * @return
	 */
	private static List<String> getNumberListByWaves(String str, String regex) {
		String string = "";
		List<String> numList = new ArrayList();
		if (str != null) {
			if (judgehanzi(str)) {
				string = subStringHanzi(str);
			}
			String[] strArray = getNumberList(str, regex);
			if (strArray.length == 1) {
				numList.add(strArray[0]);
				return numList;
			}
			int i, j;
			i = getIntFromStr(strArray[0]);
			j = getIntFromStr(strArray[1]);
			System.out.println(i);
			System.out.println(j);
			while (i <= j) {
				numList.add(string + i);
				i++;
			}

		}
		return numList;
	}

	/**
	 * 例如：分解3A01∽3A04到集合{3A01,3A02,3A03,3A04}
	 * 
	 * @param str
	 * @param regex
	 * @return
	 */
	private static List<String> getNumberListByWavesNotAllNumber(String str, String regex) {
		List<String> numList = new ArrayList();

		String temp = "";

		if (str != null) {
			String[] strArray = getNumberList(str, regex);
			if (strArray.length == 1) {
				numList.add(strArray[0]);
				return numList;
			}
			int i = 0, j = 0;

			if (StringUtils.isNotBlank(strArray[0]) && strArray[0].length() > 2) {
				i = getIntFromStr(strArray[0].substring(strArray[0].length() - 2, strArray[0].length()));
				if (strArray[0].length() > 3) {
					temp = strArray[0].substring(0, strArray[0].length() - 1);
				}
			}

			if (StringUtils.isNotBlank(strArray[1]) && strArray[1].length() > 2) {
				j = getIntFromStr(strArray[1].substring(strArray[1].length() - 2, strArray[1].length()));
			}
			while (i <= j) {
				if (!((i + "").length() > 2)) {
					numList.add("0" + i + "");
				} else {
					numList.add(i + "");
				}

				i++;
			}
		}
		return numList;
	}

	/*
	 * private static boolean isContainRegex(String str,String regex){ return
	 * str==null?false:str.contains(regex); }
	 */

	/**
	 * 把字符串根据regex分割成字符串数组
	 * 
	 * @param str
	 * @param regex
	 * @return String[]
	 */
	private static String[] getNumberList(String str, String regex) {
		if (str == null)
			return null;
		return str.split(regex + "+", 0);
	}

	/*
	 * public static void main(String[] args) throws Exception { String path =
	 * "E:\\01-西城区\\01-椿树街道\\01-数据\\椿树街道-梁家园社区.xlsx"; path =
	 * "E:\\01-西城区\\01-椿树街道\\01-数据\\椿树街道-红线社区.xlsx"; path =
	 * "E:\\01-西城区\\01-椿树街道\\01-数据\\椿树街道-琉璃厂西街社区.xlsx"; // path =
	 * "E:\\01-西城区\\01-椿树街道\\01-数据\\椿树街道-四川营社区.xlsx"; // path =
	 * "E:\\01-西城区\\01-椿树街道\\01-数据\\椿树街道-香炉营社区.xlsx"; path =
	 * "E:\\01-西城区\\01-椿树街道\\01-数据\\椿树街道-香炉营社区.xlsx"; // path =
	 * "D:\\椿树街道-香炉营社区.xlsx"; // path = "D:\\椿树街道-椿树园社区.xlsx"; Map map =
	 * getMapDataFromExcel(path);
	 * Map<TinyVillage,List<Map<Building,List<House>>>> m1 =
	 * (Map<TinyVillage,List<Map<Building,List<House>>>>)map.get("build");
	 * Map<TinyVillage,List<House>> m2 =
	 * (Map<TinyVillage,List<House>>)map.get("bungalow");
	 * Map<TinyVillage,List<House>> m3 =
	 * (Map<TinyVillage,List<House>>)map.get("shangye"); Village m4 =
	 * (Village)map.get("village"); // for
	 * (Map.Entry<TinyVillage,List<Map<Building,List<House>>>> entry :
	 * m1.entrySet()) { // getJsonStr(entry.getKey()); //
	 * getJsonStrLists(entry.getValue()); // } // for (Map.Entry<TinyVillage,
	 * List<House>> entry : m2.entrySet()) { // getJsonStr(entry.getKey()); //
	 * getJsonStr(entry.getValue()); // } // for (Map.Entry<TinyVillage,
	 * List<House>> entry : m3.entrySet()) { // getJsonStr(entry.getKey()); //
	 * getJsonStr(entry.getValue()); // } // getJsonStr(m4); }
	 */

	/**
	 * 打印对象，用于调试
	 * 
	 * @param obj
	 */
	public static void getJsonStr(Object obj) {
		Gson gson = new Gson();
		String json = gson.toJson(obj);

	}

	// /**
	// * 打印集合，用于调试
	// * @param houseList
	// */
	// public static void getJsonStrList(List<House> houseList){
	// Gson gson = new Gson();
	// for(House house:houseList) {
	// String json = gson.toJson(house);
	//
	// }
	// }
	//
	// /**
	// * 打印集合，用于调试
	// * @param houseList
	// */
	// public static void getJsonStrLists(List<Map<Building,List<House>>>
	// houseList) {
	// Gson gson = new Gson();
	// List<House> houseLists = null;
	// for (Map<Building, List<House>> map : houseList) {
	// for (Map.Entry<Building, List<House>> entrys : map.entrySet()) {
	//
	// String json = gson.toJson(entrys.getKey());
	//
	// houseLists = entrys.getValue();
	// for (House house : houseLists) {
	// json = gson.toJson(house);
	//
	// }
	// }
	// }
	// }

	/**
	 * 获取一个sheet中的社区信息
	 * 
	 * @param sheet
	 * @return
	 */
	private static Village getVillage(Sheet sheet) {

		Village village = new Village();
		Row villageRow = null;
		Cell cell1 = null;
		Cell cell3 = null;
		Cell cell5 = null;
		Cell cell5_7 = null;// 门店编码
		Cell cell4_7 = null;// 门店名称
		villageRow = sheet.getRow(4);
		cell1 = villageRow.getCell(1);
		cell3 = villageRow.getCell(3);
		cell5 = villageRow.getCell(5);
		cell4_7 = villageRow.getCell(7);
		// village.setCounty(cell3.toString());
		village.setTown(cell5.toString());
		villageRow = sheet.getRow(5);
		cell1 = villageRow.getCell(1);
		cell3 = villageRow.getCell(3);
		cell5 = villageRow.getCell(5);
		cell5_7 = villageRow.getCell(7);
		village.setStore_code(cell5_7.toString());
		village.setStore_name(cell4_7.toString());
		village.setName(cell1.toString());
		// if(cell3.toString().indexOf("-")>1)
		village.setCommittee_phone(cell3.toString());
		// else
		// village.setCommitteePhone(getStringFromDouble(cell3));
		village.setCommittee_address(cell5.toString());

		villageRow = sheet.getRow(6);
		cell1 = villageRow.getCell(1);
		cell3 = villageRow.getCell(3);
		cell5 = villageRow.getCell(5);
		village.setHousehold_number(getIntFromStr(cell1.toString()));
		// village.setResident_population_number(new
		// Double(getIntFromStr(cell3.toString())));
		village.setResident_population_number(getIntFromStr(cell3.toString()));
		village.setSquare_area(new BigDecimal(getIntFromStr(cell5.toString())) + "");
		villageRow = sheet.getRow(7);
		cell1 = villageRow.getCell(1);
		village.setIntroduction(cell1.toString());

		return village;
	}

	/**
	 * 获取平房sheet的房屋集合
	 * 
	 * @param sheet
	 * @return
	 */
	private static Map<TinyVillage, List<House>> getBungalow(Sheet sheet) {

		House house = null;
		TinyVillage tinyVillage = null;
		Cell cell1 = null;
		Cell cell2 = null;
		Cell cell3 = null;
		String[] roomArray = null;
		List<House> houseList = null;
		Map<TinyVillage, List<House>> map = new HashMap();
		Row row = null;

		for (int j = 2; j <= sheet.getLastRowNum(); j++) {
			row = sheet.getRow(j);
			if (row == null)
				break;
			cell1 = row.getCell(1); // 街/路/胡同
			cell2 = row.getCell(2); // 门牌号
			cell3 = row.getCell(3); // 小区户数
			// 门牌号为空，说明后面没有row了
			if (cell2 == null || StringUtils.isBlank(cell2.toString()))
				break;

			// 如果小区不为空
			if (cell1 != null && StringUtils.isNotBlank(cell1.toString())) {
				// 如果前一小区不为空
				if (tinyVillage != null)
					map.put(tinyVillage, houseList);
				// 新建TinyVillage
				tinyVillage = new TinyVillage();
				tinyVillage.setTinyvillage_type(0); // 平9房
				tinyVillage.setName(cell1.toString());
				if (cell3 == null || StringUtils.isBlank(cell3.toString())) {
					throw new RuntimeException("平房小区居民户数不能为空");
				}
				tinyVillage.setResidents_number(Integer.parseInt(cell3.toString().split("\\.")[0]));
				houseList = new ArrayList();
			}

			// 取得房屋信息列表 t_house (没有类似1~10这样的用范围表示的门牌号）
			// houseList.addAll(getHouseList(row, tinyVillage));
			roomArray = getNumberList(cell2.toString().replaceAll("、|，|\\s", ","), ",");
			if (roomArray != null && roomArray.length > 0) {
				for (int i = 0; i < roomArray.length; i++) {
					if (roomArray[i].length() - roomArray[i].replaceAll("∽", "").length() == 1) {
						List<String> list = subStringpingfang(roomArray[i]);
						for (String string : list) {
							house = new House();
							house.setBungalow_number(string.replaceAll("\\.0", ""));
							String tempStr = "";
							if (tinyVillage != null) {
								tempStr = tinyVillage.getName();
							}
							house.setAddress(tempStr + string.replaceAll("\\.0", "") + "号");
							house.setHouse_type(0); // 平房
							houseList.add(house);
						}
					} else {
						house = new House();
						house.setBungalow_number(roomArray[i].replaceAll("\\.0", ""));
						String tempStr = "";
						if (tinyVillage != null) {
							tempStr = tinyVillage.getName();
						}
						house.setAddress(tempStr + roomArray[i].replaceAll("\\.0", "") + "号");
						house.setHouse_type(0); // 平房
						houseList.add(house);
					}

				}
			}
		}

		if (tinyVillage != null)
			map.put(tinyVillage, houseList);
		return map;
	}

	/**
	 * 获取楼房户型信息
	 * 
	 * @param sheet
	 * @return
	 */
	/*
	 * private static List<House> getRoomDetail(Sheet sheet) { House house =
	 * null; Row houseRow = null; Cell cell1 = null; Cell cell2 = null; Cell
	 * cell3 = null; Cell cell4 = null; Cell cell5 = null; Cell cell6 = null;
	 * Cell cell7 = null; Cell cell8 = null; Cell cell9 = null; Cell cell10 =
	 * null; List<House> houseList = new ArrayList(); Row row = null; for(int
	 * j=2;j<sheet.getLastRowNum();j++){ house = new House(); row =
	 * sheet.getRow(j); cell1 = row.getCell(1); cell2 = row.getCell(2); cell3 =
	 * row.getCell(3); cell4 = row.getCell(4); cell5 = row.getCell(5); cell6 =
	 * row.getCell(6); cell7 = row.getCell(7); cell8 = row.getCell(8); cell9 =
	 * row.getCell(9); cell10 = row.getCell(10);
	 * if(StringUtils.isBlank(cell1.toString())){ break; }
	 * house.setBuildingNumber(cell1.toString());
	 * house.setBuildingUnitNumber(cell2.toString());
	 * house.setBuildingRoomNumber(getIntFromStr(cell4.toString()));
	 * house.setRoomCount(getIntFromStr(cell5.toString()));
	 * house.setUsedPrice(getIntFromStr(cell9.toString()));
	 * house.setRent(getIntFromStr(cell10.toString())); houseList.add(house); }
	 * return houseList; }
	 */

	/**
	 * 从excel中获取所有的sheet数据 包括社区，小区，房间信息 map说明 key value 分别如下： key:village(社区对象)
	 * value:Object<Village> key:build（楼房集合）
	 * value:Map<TinyVillage,List<Map<Building,List<House>>>> key:bungalow（平房集合）
	 * value:Map<TinyVillage,List<House>> key:shangye（商务楼宇集合）
	 * value:Map<TinyVillage,List<House>>
	 * 
	 * @param path
	 * @return
	 */
	public static Map<String, Object> getMapDataFromExcel(String path) throws Exception {

		FileInputStream inp = null;
		// Map map = new HashMap();
		// Sheet sheet = null;
		// try {
		inp = new FileInputStream(path);
		/*
		 * } catch (Exception e) { e.printStackTrace(); String message =
		 * e.getMessage(); throw new RuntimeException(message); }
		 */
		return getMapDataFromExcel(inp);
	}

	/**
	 * 从excel中获取所有的sheet数据 包括社区，小区，房间信息 map说明 key value 分别如下： key:village(社区对象)
	 * value:Object<Village> key:build（楼房集合）
	 * value:Map<TinyVillage,List<Map<Building,List<House>>>> key:bungalow（平房集合）
	 * value:Map<TinyVillage,List<House>> key:shangye（商务楼宇集合）
	 * value:Map<TinyVillage,List<House>>
	 * 
	 * @param inp
	 * @return
	 */
	public static Map<String, Object> getMapDataFromExcel(InputStream inp) {

		Map map = new HashMap();
		Sheet sheet = null;
		Workbook wb = null;
		try {

			byte[] buffer = new byte[1024];
			int len = 0;
			File xlsFile = new File("tmpxls");
			File xlsFile1 = new File("tmpxls1");
			if (xlsFile.exists())
				xlsFile.delete();
			if (xlsFile1.exists())
				xlsFile1.delete();
			FileOutputStream fo = new FileOutputStream(xlsFile);
			FileOutputStream fo1 = new FileOutputStream(xlsFile1);

			while ((len = inp.read(buffer, 0, 1024)) != -1) {
				fo.write(buffer, 0, len);
				fo1.write(buffer, 0, len);
			}

			fo.close();
			fo1.close();

			try {
				wb = new XSSFWorkbook(new FileInputStream(xlsFile));
			} catch (Exception e) {
				wb = new HSSFWorkbook(new FileInputStream(xlsFile1));
			}

			sheet = wb.getSheet(SHEQU);
			map.put("village", getVillage(sheet));
			sheet = wb.getSheet(LOUFANG);
			map.put("build", getBuildAndHouseList(sheet));
			sheet = wb.getSheet(PINGFANG);
			map.put("bungalow", getBungalow(sheet));
			sheet = wb.getSheet(SHANGYE);
			map.put("shangye", getShangye(sheet));
			sheet = wb.getSheet(GUANGCHANG);
			map.put("guangchang", getGuangChang(sheet));
			sheet = wb.getSheet(OTHER);
			map.put("other", getOtherHouse(sheet));
		} catch (Exception e) {
			e.printStackTrace();
			String message = e.getMessage();
			throw new RuntimeException(message);
		} finally {
			if (inp != null) {
				try {
					inp.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}

	/*
	 * private static List<TinyVillage> getTinyVillageList(Sheet sheet){
	 * List<TinyVillage> tvList = new ArrayList(); TinyVillage tinyVillage =
	 * null; Row row = null; Cell cell1 = null; Cell cell2 = null; for(int
	 * j=2;j<sheet.getLastRowNum();j++){ tinyVillage = new TinyVillage(); row =
	 * sheet.getRow(j); cell1 = row.getCell(1); cell2 = row.getCell(2);
	 * if(StringUtils.isBlank(cell1.toString())) continue;
	 * tinyVillage.setName(cell1.toString());
	 * tinyVillage.setAddress(cell2.toString()); tvList.add(tinyVillage); }
	 * return tvList; }
	 */

	/**
	 * 获取商业楼宇房间集合 map key(TinyVillage) value(List<House>)
	 * 
	 * @param sheet
	 * @return
	 */
	private static Map<TinyVillage, List<House>> getShangye(Sheet sheet) {
		List<House> houseList = null;
		Map<TinyVillage, List<House>> map = new HashMap();
		Row row = null;
		Cell cell1 = null; // 商业楼宇名
		Cell cell2 = null; // 地址
		Cell cell3 = null; // 楼层数
		Cell cell4 = null; // 小区户数
		House house = null;
		TinyVillage tinyVillage = null;

		for (int j = 2; j <= sheet.getLastRowNum(); j++) {
			row = sheet.getRow(j);
			cell1 = row.getCell(1);
			cell2 = row.getCell(2);
			cell3 = row.getCell(3);
			cell4 = row.getCell(4);
			// 商业楼宇为空，说明后面没有row了
			if (cell1 == null || StringUtils.isBlank(cell1.toString()))
				break;
			tinyVillage = new TinyVillage();
			tinyVillage.setTinyvillage_type(2); // 商业楼宇
			tinyVillage.setName(cell1.toString());
			tinyVillage.setAddress(cell2.toString());
			if (cell4 != null && !StringUtils.isBlank(cell4.toString())) {
				Pattern pattern = Pattern.compile("[0-9]*");
				Matcher isNum = pattern.matcher(cell4.toString().split("\\.")[0]);
				if (!isNum.matches()) {
					throw new RuntimeException("商业楼宇：" + cell1.toString() + "小区居民户数必须为正整数!");
				}
				tinyVillage.setResidents_number(Integer.parseInt(cell4.toString().split("\\.")[0]));
			}
			houseList = new ArrayList();
			for (int i = 1; i <= getIntFromStr(cell3.toString()); i++) {
				house = new House();
				house.setCommercial_floor_number(i + "");
				house.setHouse_type(2); // 商业楼宇
				house.setAddress(tinyVillage.getAddress() + tinyVillage.getName());
				houseList.add(house);
			}
			map.put(tinyVillage, houseList);
		}
		return map;
	}

	/**
	 * 获取广场、公园房间集合 map key(TinyVillage) value(List<House>)
	 * 
	 * @param sheet
	 * @return
	 */
	private static Map<TinyVillage, List<House>> getGuangChang(Sheet sheet) {
		List<House> houseList = null;
		Map<TinyVillage, List<House>> map = new HashMap();
		Row row = null;
		Cell cell1 = null; // 广场或公园
		Cell cell2 = null; // 地址
		Cell cell3 = null; // 小区户数
		TinyVillage tinyVillage = null;
		for (int j = 2; j <= sheet.getLastRowNum(); j++) {
			row = sheet.getRow(j);
			cell1 = row.getCell(1);
			cell2 = row.getCell(2);
			cell3 = row.getCell(3);
			// 商业楼宇为空，说明后面没有row了
			if (cell1 == null || StringUtils.isBlank(cell1.toString()))
				break;

			tinyVillage = new TinyVillage();
			tinyVillage.setTinyvillage_type(3); // 广场、公园
			tinyVillage.setName(cell1.toString());
			tinyVillage.setAddress(cell2.toString());
			if (cell3 != null && !StringUtils.isBlank(cell3.toString())) {
				Pattern pattern = Pattern.compile("[0-9]*");
				Matcher isNum = pattern.matcher(cell3.toString().split("\\.")[0]);
				if (!isNum.matches()) {
					throw new RuntimeException("广场、公园：" + cell1.toString() + "小区居民户数必须为正整数!");
				}
				tinyVillage.setResidents_number(Integer.parseInt(cell3.toString().split("\\.")[0]));
			}
			houseList = new ArrayList();
			House house = new House();
			house.setHouse_type(3);
			house.setCommercial_floor_number(101 + "");
			house.setAddress(tinyVillage.getAddress() + tinyVillage.getName());
			houseList.add(house);
			map.put(tinyVillage, houseList);
		}
		return map;
	}

	/**
	 * 获取其他类型的房间集合 map key(TinyVillage) value(List<House>)
	 * 
	 * @param sheet
	 * @return
	 */
	private static Map<TinyVillage, List<House>> getOtherHouse(Sheet sheet) {
		List<House> houseList = null;
		Map<TinyVillage, List<House>> map = new HashMap();
		Row row = null;
		Cell cell1 = null; // 其他类型的名称
		Cell cell2 = null; // 地址
		Cell cell3 = null; // 小区户数
		TinyVillage tinyVillage = null;
		for (int j = 2; j <= sheet.getLastRowNum(); j++) {
			row = sheet.getRow(j);
			cell1 = row.getCell(1);
			cell2 = row.getCell(2);
			cell3 = row.getCell(3);
			// 商业楼宇为空，说明后面没有row了
			if (cell1 == null || StringUtils.isBlank(cell1.toString()))
				break;
			tinyVillage = new TinyVillage();
			tinyVillage.setTinyvillage_type(5); // 其他类型的名称
			tinyVillage.setName(cell1.toString());
			tinyVillage.setAddress(cell2.toString());
			if (cell3 != null && !StringUtils.isBlank(cell3.toString())) {
				Pattern pattern = Pattern.compile("[0-9]*");
				Matcher isNum = pattern.matcher(cell3.toString().split("\\.")[0]);
				if (!isNum.matches()) {
					throw new RuntimeException("其他：" + cell1.toString() + "小区居民户数必须为正整数!");
				}
				tinyVillage.setResidents_number(Integer.parseInt(cell3.toString().split("\\.")[0]));
			}
			houseList = new ArrayList();
			House house = new House();
			house.setHouse_type(5);
			house.setCommercial_floor_number("101");
			house.setAddress(tinyVillage.getAddress() + tinyVillage.getName());
			houseList.add(house);
			map.put(tinyVillage, houseList);
		}
		return map;
	}

	/**
	 * 字符串变换成整数
	 * 
	 * @param str
	 * @return
	 */
	private static int getIntFromStr(String str) {
		str = str.replaceAll("[^\\d|^.|^-]", "");
		if (str == null || "".equals(str.trim()))
			return 0;
		return Double.valueOf(str).intValue();
	}

	/**
	 * 数值变换成字符串
	 * 
	 * @return
	 */
	private static String getStringFromDouble(Cell cell) {
		DecimalFormat df = new DecimalFormat("0");
		return df.format(cell.getNumericCellValue());
	}

	/**
	 * 将101∽102,(1、2层独立别墅)301∽302,4∽10层同上中()以及其中的内容替换掉
	 * 
	 * @param cell
	 * @return
	 */
	private static String getStringFromCell(Cell cell) {

		String regex = "\\([^}]*\\)";

		return cell.toString().replaceAll(regex, "");
	}

	/**
	 * 如果楼层为地下室调用此方法
	 */
	public static String[] getHouse_no(String string, String regex) {
		string = string.replace("负", "-");
		// int i= Integer.valueOf(string);
		return string.split(regex + "+", 0);
	}

	/**
	 * 判断楼层是否有汉字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean judgehanzi(String str) {
		if (str.getBytes().length == str.length()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 截取字符串中的汉字
	 * 
	 * @param str
	 * @return
	 */
	public static String subStringHanzi(String str) {
		String reg = "[^\u4e00-\u9fa5]";
		return str.replaceAll(reg, "");
	}

	/**
	 * 拆分1∽24;
	 * 
	 * @param str
	 * @return
	 */
	public static List<String> subStringpingfang(String str) {
		String[] split = str.split("∽");
		List<String> list = new ArrayList<String>();
		for (int i = Integer.parseInt(split[0]); i <= Integer.parseInt(split[1]); i++) {
			list.add(i + "");
		}
		return list;
	}

	/*
	 * public static boolean main(String[] args) { String str="埃里克撒旦法"; for (int
	 * i = 0; i < str.length(); i++) { if ((str.charAt(i) <= 'Z' &&
	 * str.charAt(i) >= 'A') || (str.charAt(i) <= 'z' && str.charAt(i) >= 'a'))
	 * { return true; } else { return false; } } }
	 */
	// 验证是否带字母
	public static boolean ifLetter(String str) {
		if ((str.charAt(0) <= 'Z' && str.charAt(0) >= 'A') || (str.charAt(0) <= 'z' && str.charAt(0) >= 'a')) {
			return true;
		} else {
			return false;
		}
	}

	// B51~B56,1~5层同上处理带字母的楼号
	public static List<String> getLetter(String str, String regex, String waveRegex) {
		List<String> floorList = null;
		if (str != null) {
			floorList = new ArrayList();
			String[] split = str.replaceAll("\\s", "").split(regex);
			if (split.length == 1) {
				floorList.add(str);
			} else if (split.length == 2) {
				if (str.indexOf("、") != -1) {
					for (int m = 0; m < split.length; m++) {
						floorList.add(split[m]);
					}
				} else {
					String[] strings = split[0].split(waveRegex);
					if (strings != null && strings.length > 0) {
						int i, j;
						char charAt = strings[0].charAt(0);
						String arr1 = strings[0].substring(strings[0].length() - 1);
						String arr2 = strings[1].substring(strings[1].length() - 1);
						i = getIntFromStr(arr1);
						j = getIntFromStr(arr2);
						System.out.println(i);
						System.out.println(j);
						while (i <= j) {
							floorList.add(charAt + "%" + i);
							i++;
						}
					}
				}

			} else if (split.length > 2) {
				for (int m = 0; m < split.length - 1; m++) {
					floorList.add(split[m]);
				}
			}
		}
		return floorList;
	}

	// 处理内复式
	public static List<String> getLetterNeifushi(String str, String regex, String waveRegex) {
		List<String> floorList = null;
		if (str != null) {
			floorList = new ArrayList();
			String[] split = str.replaceAll("\\s", "").split(regex);
			if (split.length == 1) {
				floorList.add(str);
			} else if (split.length == 2) {
				String[] strings = split[0].split(waveRegex);
				if (strings != null && strings.length == 1) {
					int i;
					i = getIntFromStr(strings[0]);
					floorList.add(i + "内复式");

				} else if (strings != null && strings.length > 0) {
					int i, j;
					i = getIntFromStr(strings[0]);
					j = getIntFromStr(strings[1]);
					System.out.println(i);
					System.out.println(j);
					while (i <= j) {
						floorList.add(i + "内复式");
						i++;
					}
				}
			}
		}
		return floorList;
	}

	// 处理3层别墅
	public static List<String> getThreeHouse(String str, String regex, String waveRegex) {
		List<String> floorList = null;
		if (str != null) {
			floorList = new ArrayList();
			String[] split = str.replaceAll("\\s", "").split(regex);
			if (split.length == 1) {
				floorList.add(str);
			} else if (split.length == 2) {
				String[] strings = split[0].split(waveRegex);
				if (strings != null && strings.length > 0) {
					int i, j;
					i = getIntFromStr(strings[0]);
					j = getIntFromStr(strings[1]);
					System.out.println(i);
					System.out.println(j);
					while (i <= j) {
						floorList.add(i + "-3层别墅");
						i++;
					}
				}
			}
		}

		return floorList;

	}

	// 验证101之1
	public static List<String> getZHiHouse(String str, String regex, String waveRegex) {
		List<String> floorList = null;
		if (str != null) {
			floorList = new ArrayList();
			String[] split = str.replaceAll("\\s", "").split(regex);
			if (split.length == 1) {
				floorList.add(str);
			} else {
				for (String string : split) {
					floorList.add(string);
				}
			}
		}
		return floorList;
	}

}