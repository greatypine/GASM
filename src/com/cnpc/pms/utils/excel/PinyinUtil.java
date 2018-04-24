package com.cnpc.pms.utils.excel;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created by liuxi on 2016/11/14 0014.
 */
public class PinyinUtil {

    public static String getPingYin(String src) throws BadHanyuPinyinOutputFormatCombination {
        char[] t1 = null;
        t1 = src.toCharArray();
        String[] t2 = new String[t1.length];
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        String t4 = "";
        int t0 = t1.length;

        for (int i = 0; i < t0; i++) {
            // 判断是否为汉字字符
            if (java.lang.Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
                t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
                t4 += t2[0];
            } else

                t4 += java.lang.Character.toString(t1[i]);
        }
        return t4;

    }

    // 返回中文的首字母
    public static String getPinYinHeadChar(String str) {
        String convert = "";
        try {
            for (int j = 0; j < str.length(); j++) {
                char word = str.charAt(j);
                String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
                if (pinyinArray != null) {
                    convert += pinyinArray[0].charAt(0);
                } else {
                    convert += word;
                }
            }
            return convert;
        } catch (Exception e) {

        }

        return convert;
    }

    public static String getPinYinHeadCharUpper(String str) {
        String convert = getPinYinHeadChar(str);
        if(convert != null){
            convert = convert.toUpperCase();
        }
        return convert;
    }
    // 将字符串转移为ASCII码
    public static String getCnASCII(String cnStr) {
        StringBuffer strBuf = new StringBuffer();
        byte[] bGBK = cnStr.getBytes();
        for (int i = 0; i < bGBK.length; i++) {
            strBuf.append(Integer.toHexString(bGBK[i] & 0xff));
        }
        return strBuf.toString();
    }
}
