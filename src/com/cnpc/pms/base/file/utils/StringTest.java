package com.cnpc.pms.base.file.utils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhangjy on 2015/8/3.
 */
public class StringTest {

    public static void main(String[] args) throws IOException {
//        System.out.println("101∽1012, 2∽16层同上".split(",")[1]);
//        System.out.println("1012".substring(2,"1012".length()));
//        System.out.println("1  甲1  14  12  10 甲10  8  2  甲2 7 、甲5 、5 、1".replaceAll("  ",",").replaceAll(" ",",").replaceAll("、",",").split(",")[9]);
//        ExcelDataFormat.getJsonStr("101，102，103 105 5、7、9、11、13、17".split("[，|\\s|、]"));
//        ExcelDataFormat.getJsonStr("301-305∽ 1∽12".split("[-|∽]"));
//        ExcelDataFormat.getJsonStr("301-305∽ 1∽12".replaceAll("\\s","").replaceAll("[-|∽|，]", ","));
//        ExcelDataFormat.getJsonStr("12".split("∽|-"));
//        ExcelDataFormat.getJsonStr("12∽15 1-5".contains("∽"));
//        ExcelDataFormat.getJsonStr("88.0".replaceAll(".0",""));
//        ExcelDataFormat.getJsonStr("安庆们".contains("街道"));
//        ExcelDataFormat.getJsonStr("安庆们街道".replaceAll("街道", "").replaceAll("办事处", ""));
        //ExcelDataFormat.getJsonStr("安庆们".endsWith("乡"));
        //ExcelDataFormat.getJsonStr("1~7".split("∽|-|~"));
        //System.out.println(UUID.randomUUID());
//        System.out.println("/piclib/excel/".substring(1,"/piclib/excel/".length()-1));
//        System.out.println(getPaths("/piclib/excel/")[1]);
//        System.out.println("xxx.xlsx".endsWith("xlsx")||"".endsWith(""));
//        System.out.println("02-地图2".indexOf("地图"));
//        String s = "fd486a0ecb574eb9a4b27effe2084713-红线社区.png";
//        System.out.println(s.substring(s.indexOf("-")+1,s.length()));
//        s = "ftp://10.16.0.100/piclib/椿树街道房屋数据.7z";
//        System.out.println(s.lastIndexOf("/"));
//        System.out.println(s.lastIndexOf("."));
//        System.out.println(s.substring(s.lastIndexOf("/") + 1, s.lastIndexOf(".")));
//        System.out.println(s.replaceAll("\\.zip|\\.7z", ""));
//        System.out.println(isNumber("12"));
//
//        System.out.println("123:"+isDecimal("123"));
//        System.out.println("0.123:"+isDecimal("0.123"));
//        System.out.println(".123:"+isDecimal(".123"));
//        System.out.println("1.23:"+isDecimal("1.23"));
//        System.out.println("123.:"+isDecimal("123."));
//        System.out.println("00.123:"+isDecimal("00.123"));
//        System.out.println("123.0:"+isDecimal("123.0"));
//        System.out.println("123.00:"+isDecimal("123.00"));
//        System.out.println("0123:"+isDecimal("0123"));
        System.out.println(isTelephone("13560102331"));
        System.out.println(isMobileNO("13560102331"));
        System.out.println(isContainChinese("ggdsfg"));
        System.out.println("家5号afds".toCharArray().length);
        String test = "E:\\01-椿树街道 - 副本\\01-数据\\椿树街道-椿树园社区.xlsx 文件错误 ";
        System.out.println(test.length());
    }
    public static String[] getPaths(String s){
        s = s.substring(1,s.length()-1);
        return s.split("/");
    }
    //判断是否为正整型
    public static boolean isNumber(String str) {
        return str.matches("[\\d]+");
    }
    //判断是否为正小数
    public static boolean isDecimal(String str){
        if(isNumber(str))return false;
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

    // 判断邮箱
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    // 判断日期格式:yyyy-mm-dd

    public static boolean isValidDate(String sDate) {
        String datePattern1 = "\\d{4}-\\d{2}-\\d{2}";
        String datePattern2 = "^((\\d{2}(([02468][048])|([13579][26]))"
                + "[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|"
                + "(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?"
                + "((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?("
                + "(((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?"
                + "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
        if ((sDate != null)) {
            Pattern pattern = Pattern.compile(datePattern1);
            Matcher match = pattern.matcher(sDate);
            if (match.matches()) {
                pattern = Pattern.compile(datePattern2);
                match = pattern.matcher(sDate);
                return match.matches();
            } else {
                return false;
            }
        }
        return false;
    }
    //验证金额
    public static boolean isNumbers(String str)
    {
        Pattern pattern= Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后一位的数字的正则表达式
        Matcher match=pattern.matcher(str);
        if(match.matches()==false)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }
}
