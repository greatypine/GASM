package com.cnpc.pms.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * 日期工具类（如有新需求请自行添加）
 * 
 * Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2013-4-24
 */
public class DateUtils {

	/** 日期格式化为yyyy-MM-dd格式 */
	private static final String NORMAL_FORMAT = "yyyy-MM-dd";

	/**
	 * 将Util的Date类型转换为String类型
	 * 
	 * @param date
	 * @return
	 */
	public static String dateFormat(Date date) {
		SimpleDateFormat format = new SimpleDateFormat(NORMAL_FORMAT);
		return format.format(date);
	}

	/**
	 * 将Util的Date类型转换为String类型
	 *
	 * @param date
	 * @return
	 */
	public static String dateFormat(Date date,String date_format) {
		SimpleDateFormat format = new SimpleDateFormat(date_format);
		return format.format(date);
	}

	/**
	 * 将Util的String类型转换为Date类型
	 * @param str_date 日期字符串
	 * @return 日期对象
	 */
	public static Date str_to_date(String str_date,String date_format) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(date_format);
		return format.parse(str_date);
	}

	/**
	 * Long类型毫秒转换Date类型
	 * @param dateFormat 转换的日期格式
	 * @param millSec 毫秒
     * @return 字符串日期
     */
	public static String transferLongToDate(String dateFormat,Long millSec){
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Date date= new Date(millSec);
		return sdf.format(date);
	}
	
	/**
	 * 取得 月份的第一天是周几
	 * @param year
	 * @param month
	 * @return
	 */
	public static String getFirstDayWeek(int year,int month){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month-1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        int i = cal.get(Calendar.DAY_OF_WEEK);
        String[] str = {"","日","一","二","三","四","五" ,"六"};
        return str[i];
	}
	
	/**
	 * 判断月有几天 参数YYYY-MM
	 * @param month_work
	 * @return
	 */
	public static int getDaysByMonths(String month_work){
		int year = Integer.parseInt(month_work.split("-")[0]);
		int month = Integer.parseInt(month_work.split("-")[1]);
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			return 31;
		case 2:
			return isLeapYear(year)?29:28;
		case 4:
		case 6:
		case 9:
		case 11:
			return 30;
		default:
			return 31;
		}
	}
	
	public static boolean isLeapYear(int year){
		if(year%100==0){
			if(year%400==0){
				return true;
			}
		}else{
			if(year%4==0){
				return true;
			}
		}
		return false;
	}
	
	/** 
     * @param date 当前日期 
     * @return 返回当前日期上月的月初日期
     */ 
    public static String getPreMonthFirstDay(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        Date theDate = (Date) calendar.getTime();
        
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        String day_first = df.format(gcLast.getTime());
        StringBuffer str = new StringBuffer().append(day_first);
        day_first = str.toString();
        
        return day_first;
    }
    
    /**
     * 比较两日期大小
     * @param date1
     * @param date2
     * @return
     */
    public static int compareDate(String date1, String date2) {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            if (dt1.after(dt2)) {
                return 1;
            } else if (dt1.before(dt2)) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
    
    
    
  //获得本周一与当前日期相差的天数  
    public static  int getMondayPlus() {  
        Calendar cd = Calendar.getInstance();  
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);  
        if (dayOfWeek == 1) {  
            return -6;  
        } else {  
            return 2 - dayOfWeek;  
        }  
    }
    //获得当前周- 周一的日期  
    public static  String getCurrentMonday() {  
        int mondayPlus = getMondayPlus();  
        GregorianCalendar currentDate = new GregorianCalendar();  
        currentDate.add(GregorianCalendar.DATE, mondayPlus);  
        Date monday = currentDate.getTime();  
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String preMonday = df.format(monday);  
        return preMonday;  
    }  
    // 获得当前周- 周日  的日期  
    public static String getPreviousSunday() {  
        int mondayPlus = getMondayPlus();  
        GregorianCalendar currentDate = new GregorianCalendar();  
        currentDate.add(GregorianCalendar.DATE, mondayPlus +6);  
        Date monday = currentDate.getTime();  
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String preMonday = df.format(monday);  
        return preMonday;  
    } 
    //取得当前月 
    public static String getCurrMonthDate(){
    	GregorianCalendar currentDate = new GregorianCalendar(); 
    	Date monday = currentDate.getTime(); 
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
        String curMonth = df.format(monday);  
        return curMonth;
    }
    
    /**
     * 返回当前时间的月初日期
     * @param dateFormat
     * @return
     */
    public static String getCurrMonthFirstDate(String dateFormat){
    	SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Date theDate = (Date) calendar.getTime();
        
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        String day_first = df.format(gcLast.getTime());
        StringBuffer str = new StringBuffer().append(day_first);
        day_first = str.toString();
        
        return day_first;
    }
    
    public static String getNextMonthDate(String targetDate, String dateFormat){
    	SimpleDateFormat df = new SimpleDateFormat(dateFormat);
    	Calendar calendar = Calendar.getInstance();
        try{
        	Date date = df.parse(targetDate);//初始日期
        	calendar.setTime(date);//设置日历时间
            calendar.add(Calendar.MONTH,1);//在日历的月份上增加1个月
        }catch(Exception e){
        	 e.printStackTrace();
        }
        return df.format(calendar.getTime());
    }
    
    public static String getNextDate(String targetDate, String dateFormat){
    	SimpleDateFormat df = new SimpleDateFormat(dateFormat);
    	Calendar calendar = Calendar.getInstance();
        try{
        	Date date = df.parse(targetDate);//初始日期
        	calendar.setTime(date);//设置日历时间
            calendar.add(Calendar.DATE,1);//在日历的月份上增加1天
        }catch(Exception e){
        	 e.printStackTrace();
        }
        return df.format(calendar.getTime());
    }
    
    public static void main(String args[]){
    	System.out.println(getCurrMonthFirstDate("yyyy-MM-dd"));
    }
    //获得昨天的日期
    public static String lastDate() {
		Calendar calendar = new GregorianCalendar();
		 Date date=new Date();
		   calendar.setTime(date);
		   calendar.add(calendar.DATE,-1);//把日期往后增加一天.整数往后推,负数往前移动
		   date=calendar.getTime();
		   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		   return formatter.format(date);
	}
	//获得当前日期前几天的日期(不包含当天)
	public static String getBeforeDate(String dateStr,int count){
        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");  
        String maxDateStr = dateStr;  
        String minDateStr = "";  
        Calendar calc =Calendar.getInstance();  
        try {  
            calc.setTime(sdf.parse(maxDateStr));  
            calc.add(calc.DATE, count);  
            Date minDate = calc.getTime();  
            minDateStr = sdf.format(minDate);  
            return minDateStr;
        } catch (ParseException e1) {  
            e1.printStackTrace();  
        }
        return null;
	}
	
	/**
	 * 获取当年的周，从周日开始
	 * @param weeks
	 * @return
	 */
	public static List<String> getDateByWeek(){
		int weeks = getWeekOfYear(new Date());
		List<String> list = new ArrayList<String>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(new Date());
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
		Date date = c.getTime();
		Date date2 = getDateBeforeOneDate(date);
		for (int i = 0; i < weeks; i++) {
			Date weekBefore = getWeekBefore(i, date2);
			list.add(dateFormat.format(weekBefore));
		}

		Collections.sort(list);
		
		return list;  
	}
	
	/**
	 * @author sunning 获取当前时间所在年的周数
	 * @param date
	 * @return
	 */
	public static int getWeekOfYear(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setMinimalDaysInFirstWeek(7);
		c.setTime(date);
		return c.get(Calendar.WEEK_OF_YEAR);
	}
	
	/**
	 * 获取指定日期的前一天
	 * 
	 * @author sunning
	 * @param date
	 * @return
	 */
	public static Date getDateBeforeOneDate(Date date) {
		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(date);// 把当前时间赋给日历
		calendar.add(Calendar.DATE, -1); // 设置为前一天
		return calendar.getTime(); // 得到前一天的时间
	}
	
	public static Date getWeekBefore(int n, Date date) {
		Calendar c = Calendar.getInstance();

		// 过去七天
		c.setTime(date);
		c.add(Calendar.DATE, -7 * n);
		Date d = c.getTime();
		return d;
	}
	
}
