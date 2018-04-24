package com.cnpc.pms.base.file.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Created by zhangjy on 2015/9/10.
 */
public class VerifyExcelDataFormat {

	public static final String SHEQU = "1-社区信息";
	public static final String LOUFANG = "2-房屋信息(楼房)";
	public static final String SHANGYE = "2-房屋信息(商业楼宇)";
	public static final String PINGFANG = "2-房屋信息(平房)";
	public static final String GUANGCHANG = "2-房屋信息(公园、广场)";
	public static final String OTHER = "2-房屋信息(其它)";
	public static final String HUXING = "3-楼房户型信息";
	public static final String ZHENGLI = "整理表";

	public static StringBuffer verifyExcelFile(InputStream inp, String name) {
		StringBuffer verify = new StringBuffer("文件：" + name + " ");
		Sheet sheet = null;
		Workbook wb = null;
		try {
			if (name.endsWith("xlsx")) {
				// 2007版本
				wb = new XSSFWorkbook(inp);
			} else if (name.endsWith("xls")) {
				// 2003版本
				wb = new HSSFWorkbook(inp);
			}
			sheet = wb.getSheet(SHEQU);
			verify.append(verifyVillage(sheet, name));
			if (name.indexOf("-") < 0) {
				return new StringBuffer(name + " 文件名错误 ");
			}
			if (sheet == null) {
				return new StringBuffer(name + " 文件错误 ");
			}
			sheet = wb.getSheet(LOUFANG);
			verify.append(verifyZhuzhai(sheet));
			sheet = wb.getSheet(SHANGYE);
			verify.append(verifyShangye(sheet));
			sheet = wb.getSheet(PINGFANG);
			verify.append(verifyPingfang(sheet));
			sheet = wb.getSheet(GUANGCHANG);
			verify.append(verifyGuangChang(sheet));
			sheet = wb.getSheet(OTHER);
			verify.append(verifyOtherHouse(sheet));

			if (verify.toString().equals("文件：" + name + " ")) {
				verify = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (wb != null) {
				try {
					inp.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return verify;
	}

	private static StringBuffer verifyVillage(Sheet sheet, String name) {

		StringBuffer villageBuffer = new StringBuffer();
		Row villageRow = null;
		Cell cell1 = null;
		Cell cell3 = null;
		Cell cell5 = null;
		Cell cell4_7 = null;// 门店名称
		Cell cell5_7 = null;// 门店编码

		villageRow = sheet.getRow(6);
		cell1 = villageRow.getCell(1);
		cell3 = villageRow.getCell(3);
		cell5 = villageRow.getCell(5);
		villageRow = sheet.getRow(5);
		cell5_7 = villageRow.getCell(7);
		villageRow = sheet.getRow(4);
		cell4_7 = villageRow.getCell(7);
		if (StringUtils.isNotBlank(cell4_7.toString()) && StringUtils.isBlank(cell4_7.toString())) {
			villageBuffer.append(" 门店名称不能为空: " + cell4_7.toString());
		}
		if (StringUtils.isNotBlank(cell1.toString().replaceAll("不详", ""))
				&& isNumber(cell1.toString().replaceAll("\\.0", "")) == false) {
			villageBuffer.append(" 总户数(户)必须为正整数: " + cell1.toString());
		}
		if (StringUtils.isNotBlank(cell3.toString().replaceAll("不详", ""))
				&& isNumber(cell3.toString().replaceAll("\\.0", "")) == false) {
			villageBuffer.append(" 常住人口(人)必须为正整数: " + cell3.toString());
		}
		if (StringUtils.isNotBlank(cell5.toString()) && StringUtils.isNotBlank(cell5.toString().replaceAll("不详", ""))
				&& isPositiveDecimal(cell5.toString()) == false) {
			villageBuffer.append(" 总面积(平方公里)必须为数字: " + cell5.toString());
		}
		if (StringUtils.isNotBlank(cell5_7.toString()) && StringUtils.isBlank(cell5_7.toString())) {
			villageBuffer.append(" 门店编码不能为空: " + cell5_7.toString());
		}

		// String code = name.split("-")[0];
		// List<Village> vlist = new ExcelManager().getVillage(code);
		// if(vlist==null||vlist.size()==0) villageBuffer.append(" "+name+" 不存在
		// ");
		// if(vlist!=null&&vlist.size()>1) villageBuffer.append(" "+name+" 存在多个
		// ");
		// if(vlist.get(0).getApproveStatus()==2) villageBuffer.append("
		// "+name+" 已审核 ");

		return villageBuffer;
	}

	private static StringBuffer verifyZhuzhai(Sheet sheet) {

		StringBuffer zhuzhaiBuffer = new StringBuffer();
		Row zhuzhaiRow = null;
		Cell cell1 = null;
		Cell cell3 = null;
		Cell cell4 = null;
		Cell cell5 = null;
		Cell cell6 = null;
		Cell cell7 = null;
		if (sheet == null) {
			return zhuzhaiBuffer;
		}

		for (int j = 2; j < sheet.getLastRowNum(); j++) {
			zhuzhaiRow = sheet.getRow(j);
			if (zhuzhaiRow == null)
				break;
			cell1 = zhuzhaiRow.getCell(1); // 小区
			cell3 = zhuzhaiRow.getCell(3); // 楼号
			cell4 = zhuzhaiRow.getCell(4); // 单元
			cell5 = zhuzhaiRow.getCell(5); // 楼层
			cell6 = zhuzhaiRow.getCell(6); // 房间号
			cell7 = zhuzhaiRow.getCell(7); // 小区户数
			// 整行为空，处理结束
			if (verifyZhuZhaiRow(cell3, cell4, cell5, cell6, cell7))
				break;

			// 小区不为空，楼号为空出错
			if (StringUtils.isNotBlank(cell1.toString()) && StringUtils.isBlank(cell3.toString())) {
				zhuzhaiBuffer.append(" 楼号第" + (j - 1) + "行不可为空");
			}
			if (StringUtils.isNotBlank(cell3.toString())
					&& verifyLouHao(cell3.toString().replaceAll("\\.0", "")) == false) {
				zhuzhaiBuffer.append(" 楼号第" + (j - 1) + "行格式错误：" + cell3.toString());
			}

			// 楼号不为空，单元为空出错
			if (StringUtils.isNotBlank(cell3.toString()) && StringUtils.isBlank(cell4.toString())) {
				zhuzhaiBuffer.append(" 单元第" + (j - 1) + "行不可为空");
			}
			if (StringUtils.isNotBlank(cell4.toString())
					&& verifyDanYuan(cell4.toString().replaceAll("\\.0", "")) == false) {
				// P.S. 不对单元进行校验
				zhuzhaiBuffer.append(" 单元第" + (j - 1) + "行格式错误：" + cell4.toString());
			}

			// 楼层为空出错
			if (StringUtils.isBlank(cell5.toString())) {
				zhuzhaiBuffer.append(" 楼层第" + (j - 1) + "行不可为空");
			}
			if (StringUtils.isNotBlank(cell5.toString())
					&& verifyLouCeng(cell5.toString().replaceAll("\\.0", "")) == false) {
				// zhuzhaiBuffer.append(" 楼层第" + (j - 1) + "行格式错误：" +
				// cell5.toString());
			}

			// 房间号为空出错
			if (StringUtils.isBlank(cell6.toString())) {
				zhuzhaiBuffer.append(" 房号第" + (j - 1) + "行不可为空");
			}
			if (StringUtils.isNotBlank(cell6.toString())
					&& verifyFangHao(cell6.toString().replaceAll("\\.0", "")) == false) {
				// zhuzhaiBuffer.append(" 房号第" + (j - 1) + "行格式错误：" +
				// cell6.toString());
			}
			// 验证小区户数
			if (StringUtils.isBlank(cell7.toString())) {
				zhuzhaiBuffer.append(" 小区居民户数第" + (j - 1) + "行不可为空");
			}
			if (!isPositiveDecimal(cell7.toString())) {
				zhuzhaiBuffer.append(" 小区居民户数第" + (j - 1) + "行必须为数字");
			}
			// if(verifyZhuZhaiRowNotBlank(cell3, cell4, cell5, cell6)) {
			// 楼号
			/*
			 * if (StringUtils.isBlank(cell3.toString())) {
			 * zhuzhaiBuffer.append(" 楼号第" + (j - 1) + "行不可为空"); } else { if
			 * (verifyLouHao(cell3.toString().replaceAll("\\.0", "")) == false)
			 * { zhuzhaiBuffer.append(" 楼号第" + (j - 1) + "行格式错误：" +
			 * cell3.toString()); } }
			 */
			// 单元
			/*
			 * if (StringUtils.isBlank(cell4.toString())) {
			 * zhuzhaiBuffer.append(" 单元第" + (j - 1) + "行不可为空"); } else { if
			 * (verifyDanYuan(cell4.toString().replaceAll("\\.0", "")) == false)
			 * { zhuzhaiBuffer.append(" 单元第" + (j - 1) + "行格式错误：" +
			 * cell4.toString()); } }
			 */
			// 楼层
			/*
			 * if (StringUtils.isBlank(cell5.toString())) {
			 * zhuzhaiBuffer.append(" 楼层第" + (j - 1) + "行不可为空"); } else { if
			 * (verifyLouCeng(cell5.toString().replaceAll("\\.0", "")) == false)
			 * { zhuzhaiBuffer.append(" 楼层第" + (j - 1) + "行格式错误：" +
			 * cell5.toString()); } }
			 */
			// 房号
			/*
			 * if (StringUtils.isBlank(cell6.toString())) {
			 * zhuzhaiBuffer.append(" 房号第" + (j - 1) + "行不可为空"); } else { if
			 * (verifyFangHao(cell6.toString().replaceAll("\\.0", "")) == false)
			 * { zhuzhaiBuffer.append(" 房号第" + (j - 1) + "行格式错误：" +
			 * cell6.toString()); } }
			 */
			// }
		}
		return zhuzhaiBuffer;
	}

	private static StringBuffer verifyShangye(Sheet sheet) {
		StringBuffer shangyeBuffer = new StringBuffer();
		Row shangyeRow = null;
		Cell cell1 = null;
		Cell cell2 = null;
		Cell cell3 = null;
		for (int j = 2; j < sheet.getLastRowNum(); j++) {
			shangyeRow = sheet.getRow(j);
			cell1 = shangyeRow.getCell(1);
			cell2 = shangyeRow.getCell(2);
			cell3 = shangyeRow.getCell(3);
			if (verifyShangYeRow(cell1, cell2, cell3))
				break;
			if (verifyShangYeRowNotBlank(cell1, cell2, cell3)) {
				if (StringUtils.isBlank(cell3.toString())) {
					shangyeBuffer.append(" 商业楼层第" + (j - 1) + "行不可为空");
				} else {
					if (verifyShangYeLouCeng(cell3.toString().replaceAll("\\.0", "")) == false) {
						shangyeBuffer.append(" 商业楼层第" + (j - 1) + "行格式错误：" + cell3.toString());
					}
				}
			}
		}
		return shangyeBuffer;
	}

	private static StringBuffer verifyPingfang(Sheet sheet) {
		StringBuffer pingfangBuffer = new StringBuffer();
		Row pingfangRow = null;
		Cell cell1 = null;
		Cell cell2 = null;
		Cell cell3 = null;
		for (int j = 2; j < sheet.getLastRowNum(); j++) {
			pingfangRow = sheet.getRow(j);
			if (pingfangRow == null)
				break;
			cell1 = pingfangRow.getCell(1);
			cell2 = pingfangRow.getCell(2);
			cell3 = pingfangRow.getCell(3);
			if (verifyMenPaiRow(cell1, cell2))
				break;
			if (verifyMenPaiRowNotBlank(cell1, cell2)) {
				if (StringUtils.isBlank(cell2.toString())) {
					pingfangBuffer.append(" 平房门牌号第" + (j - 1) + "行不可为空");
				} else {
					if (verifyMenPaiHao(cell2.toString()) == false) {
						pingfangBuffer.append(" 平房门牌号第" + (j - 1) + "行格式错误："
								+ cell2.toString()/* .substring(0,30) */);
					}
				}
				if (StringUtils.isBlank(cell3.toString())) {
					pingfangBuffer.append(" 平房小区居民户数第" + (j - 1) + "行不可为空");
				} else {
					if (!isPositiveDecimal(cell3.toString())) {
						pingfangBuffer.append(" 平房小区居民户数第" + (j - 1) + "行必须为数字");
					}
				}

			}
		}
		return pingfangBuffer;
	}

	/**
	 * 验证广场/公园格式和值
	 * 
	 * @param sheet
	 * @return
	 */
	private static StringBuffer verifyGuangChang(Sheet sheet) {
		StringBuffer guangchangBuffer = new StringBuffer();
		Row guangchangRow = null;
		Cell cell1 = null;
		Cell cell2 = null;
		for (int j = 2; j < sheet.getLastRowNum(); j++) {
			guangchangRow = sheet.getRow(j);
			if (guangchangRow == null)
				break;
			cell1 = guangchangRow.getCell(1);
			cell2 = guangchangRow.getCell(2);
			if (verifyMenPaiRow(cell1, cell2))
				break;
			if (verifyMenPaiRowNotBlank(cell1, cell2)) {
				if (StringUtils.isBlank(cell1.toString())) {
					guangchangBuffer.append(" 公园、广场名称第" + (j - 1) + "行不可为空");
				}
				if (StringUtils.isBlank(cell2.toString())) {
					guangchangBuffer.append(" 公园、广场地址第" + (j - 1) + "行不可为空");
				}
			}
		}
		return guangchangBuffer;
	}

	/**
	 * 其他
	 * 
	 * @param sheet
	 * @return
	 */
	private static StringBuffer verifyOtherHouse(Sheet sheet) {
		StringBuffer otherHouseBuffer = new StringBuffer();
		Row otherHouseRow = null;
		Cell cell1 = null;
		Cell cell2 = null;
		for (int j = 2; j < sheet.getLastRowNum(); j++) {
			otherHouseRow = sheet.getRow(j);
			if (otherHouseRow == null)
				break;
			cell1 = otherHouseRow.getCell(1);
			cell2 = otherHouseRow.getCell(2);
			if (verifyMenPaiRow(cell1, cell2))
				break;
			if (verifyMenPaiRowNotBlank(cell1, cell2)) {
				if (StringUtils.isBlank(cell1.toString())) {
					otherHouseBuffer.append(" 其它名称第" + (j - 1) + "行不可为空");
				}
				if (StringUtils.isBlank(cell2.toString())) {
					otherHouseBuffer.append(" 其它地址第" + (j - 1) + "行不可为空");
				}
			}
		}
		return otherHouseBuffer;
	}

	// 判断是否为正整型
	public static boolean isNumber(String str) {
		return str.matches("[\\d]+");
	}

	// 判断是否为字母
	public static boolean isZiMu(String str) {
		return str.matches("[\\w]+");
	}

	// 判断是否为正小数
	public static boolean isDecimal(String str) {
		if (isNumber(str))
			return false;
		return Pattern.compile("([1-9]+[0-9]*|0)(\\.[\\d]+)?").matcher(str).matches();
	}

	// 判断电话
	public static boolean isTelephone(String phoneNumber) {
		String phone = "\\d+-?\\d+";
		Pattern p = Pattern.compile(phone);
		Matcher m = p.matcher(phoneNumber);
		return m.matches();
	}

	// 判断手机号
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	private static boolean verifyLouHao(String cell) {
		// return isNumber(cell.replaceAll("\\s", ""));
		return true;
	}

	private static boolean verifyDanYuan(String cell) {
		boolean tag = true;
		String[] lcs = cell.replaceAll("\\s", "").split(",|、|，|∽|-|~");
		for (String s : lcs) {
			tag = isNumber(s) || isZiMu(s) || isContainChinese(s);
		}
		return tag;
	}

	private static boolean verifyLouCeng(String cell) {
		boolean tag = true;
		String[] lcs = cell.replaceAll("\\s", "").split(",|、|，|∽|-|~");
		for (String s : lcs) {
			tag = isNumber(s) || isZiMu(s) || isContainChinese(s);
		}
		return tag;
	}

	private static boolean verifyFangHao(String cell) {
		boolean tag = true;
		// 按照，、,拆分字符串
		if (cell != null && StringUtils.isNotBlank(cell)) {
			String[] lc = cell.replaceAll("\\s", "").split(",|、|，");
			if (lc.length == 1 && isContainChinese(lc[0])) {
				return false;
			}
			// 检验除掉最后一个字符串（“同上”）的其他房号，例：101、103、105、2-5层同上
			// 必须在同一楼层
			if (lc != null && lc.length > 2) {// 由于数据原因 这个判断加一个限制条件
				if (lc[0].length() > 2) {
					String floor = lc[0].substring(0, lc[0].length() - 2);
					for (int i = 1; i < lc.length - 1; i++) {
						if (!floor.equals(lc[i].substring(0, lc[i].length() - 2)))
							return false;
					}
				}
			}
			// 不能有重复
			Set<String> set = new HashSet<String>();
			for (String str : lc)
				set.add(str);
			if (set.size() != lc.length)
				return false;

			// 按照~-∽拆分区间
			for (int i = 0; i < lc.length - 1; i++) {
				String[] lcs = lc[i].split("∽|~");
				if (lcs.length == 1) {
					if (!isNumber(lcs[0]))
						tag = false;
				}
				if (lcs.length > 1) {
					if (!isNumber(lcs[0]))
						tag = false;
					if (!isNumber(lcs[1]))
						tag = false;
					// 不再同一楼层
					if (lcs[0].length() != lcs[1].length())
						tag = false;

					if (lcs[1].length() > 2) {
						if (!lcs[1].substring(0, lcs[1].length() - 2).equals(lcs[1].substring(0, lcs[1].length() - 2)))
							tag = false;
					}
				}
			}
		}
		return tag;
	}

	private static boolean verifyShangYeLouCeng(String cell) {
		return isNumber(cell);
	}

	private static boolean verifyMenPaiHao(String cell) {
		boolean tag = true;
		String[] mph = cell.split(",|、|，");
		for (String s : mph) {
			if (s.length() - s.replaceAll("-|—", "").length() > 2) {
				tag = false;
			}
			if (s.length() > s.replaceAll(",|，|、", "").length()) {
				tag = false;
			}
		}
		return tag;
	}

	public static boolean isContainChinese(String str) {
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}
	/*
	 * private static String getStringFromDouble(Cell cell){ DecimalFormat df =
	 * new DecimalFormat("0"); return df.format(cell.getNumericCellValue()); }
	 */

	/**
	 * 如果楼号、单元、楼层、房号都为空，返回true
	 * 
	 * @param cell3
	 * @param cell4
	 * @param cell5
	 * @param cell6
	 * @return boolean
	 */
	private static boolean verifyZhuZhaiRow(Cell cell3, Cell cell4, Cell cell5, Cell cell6, Cell cell7) {
		boolean tag = false;
		if (cell3 == null && cell4 == null && cell5 == null && cell6 == null && cell7 == null) {
			tag = true;
		} else {
			if ((cell3 == null || StringUtils.isBlank(cell3.toString()))
					&& (cell4 == null || StringUtils.isBlank(cell4.toString()))
					&& (cell5 == null || StringUtils.isBlank(cell5.toString()))
					&& (cell6 == null || StringUtils.isBlank(cell6.toString()))
					&& (cell7 == null || StringUtils.isBlank(cell7.toString()))) {
				tag = true;
			}
		}
		return tag;
	}

	private static boolean verifyShangYeRow(Cell cell1, Cell cell2, Cell cell3) {
		boolean tag = false;
		if ((cell1 == null || StringUtils.isBlank(cell1.toString()))
				&& (cell2 == null || StringUtils.isBlank(cell2.toString()))
				&& (cell3 == null || StringUtils.isBlank(cell3.toString()))) {
			tag = true;
		}
		return tag;
	}

	private static boolean verifyMenPaiRow(Cell cell1, Cell cell2) {
		boolean tag = false;
		if ((cell1 == null || StringUtils.isBlank(cell1.toString()))
				&& (cell2 == null || StringUtils.isBlank(cell2.toString()))) {
			tag = true;
		}
		return tag;
	}

	/*
	 * private static boolean verifyZhuZhaiRowNotBlank(Cell cell3,Cell
	 * cell4,Cell cell5,Cell cell6){ boolean tag = false;
	 * if(StringUtils.isNotBlank(cell3.toString())
	 * &&StringUtils.isNotBlank(cell4.toString())
	 * &&StringUtils.isNotBlank(cell5.toString())
	 * &&StringUtils.isNotBlank(cell6.toString())){ tag = true; } return tag; }
	 */

	private static boolean verifyShangYeRowNotBlank(Cell cell1, Cell cell2, Cell cell3) {
		boolean tag = false;
		if (StringUtils.isNotBlank(cell1.toString()) && StringUtils.isNotBlank(cell2.toString())
				&& StringUtils.isNotBlank(cell3.toString())) {
			tag = true;
		}
		return tag;
	}

	private static boolean verifyMenPaiRowNotBlank(Cell cell1, Cell cell2) {
		boolean tag = false;
		if (StringUtils.isNotBlank(cell1.toString()) && StringUtils.isNotBlank(cell2.toString())) {
			tag = true;
		}
		return tag;
	}

	/**
	 * 房间号可以为701甲或701乙
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isContainChineseelse(String str) {
		if (str.indexOf("甲") > -1 || str.indexOf("乙") > -1 || str.indexOf("西") > -1) {
			return false;
		}
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}

	private static boolean isMatch(String regex, String orginal) {
		if (orginal == null || orginal.trim().equals("")) {
			return false;
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher isNum = pattern.matcher(orginal);
		return isNum.matches();
	}

	// 判断为正数
	public static boolean isPositiveDecimal(String orginal) {
		return isMatch("^[+-\\\\d]?(\\d+|\\d+\\.\\d+)$", orginal);
	}

}