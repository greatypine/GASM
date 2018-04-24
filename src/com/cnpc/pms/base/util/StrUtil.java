// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StrUtil.java

package com.cnpc.pms.base.util;

import com.cnpc.pms.base.exception.UtilityException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Referenced classes of package com.cnpc.pms.base.util:
//            SpringHelper

public final class StrUtil
{
    public StrUtil()
    {
    }

    public static String generateUUID()
    {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static boolean isEmpty(String str)
    {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str)
    {
        return !isEmpty(str);
    }

    public static boolean isBlank(String str)
    {
        int strLen;
        if(str == null || (strLen = str.length()) == 0)
            return true;
        for(int i = 0; i < strLen; i++)
            if(!Character.isWhitespace(str.charAt(i)))
                return false;

        return true;
    }

    public static boolean isNotBlank(String str)
    {
        return !isBlank(str);
    }

    public static String capitalize(String s)
    {
        if(s == null || s.length() == 0)
            return s;
        if(s.length() > 1 && Character.isUpperCase(s.charAt(0)))
        {
            return s;
        } else
        {
            char chars[] = s.toCharArray();
            chars[0] = Character.toUpperCase(chars[0]);
            return new String(chars);
        }
    }

    public static List strToList(String str)
    {
        return strToList(str, ",");
    }

    public static List strToList(String str, String token)
    {
        List list = Arrays.asList(str.split(token));
        return list;
    }

    public static String joinArray(Object array[], String conjunction)
    {
        return StringUtils.join(array, conjunction);
    }

    public static String[] splitToEnd(String s, String expr)
    {
        if(s.endsWith(expr))
        {
            String charm = new String(new byte[] {
                (byte)(~expr.getBytes()[0])
            });
            String result[] = (new StringBuilder()).append(s).append(charm).toString().split(expr);
            result[result.length - 1] = "";
            return result;
        } else
        {
            return s.split(expr);
        }
    }

    public static byte[] stringToBytes(String string)
    {
        return org.apache.commons.codec.binary.StringUtils.getBytesUtf8(string);
    }

    public static String bytesToString(byte bytes[])
    {
        return org.apache.commons.codec.binary.StringUtils.newStringUtf8(bytes);
    }

    public static String encodeHtml(String str)
    {
        if(str == null)
            return "";
        int len = str.length();
        StringBuffer strBuf = new StringBuffer(len + 50);
        for(int i = 0; i < len; i++)
        {
            char c = str.charAt(i);
            switch(c)
            {
            case 38: // '&'
                strBuf.append("&amp;");
                break;

            case 60: // '<'
                strBuf.append("&lt;");
                break;

            case 62: // '>'
                strBuf.append("&gt;");
                break;

            case 34: // '"'
                strBuf.append("&quot;");
                break;

            default:
                strBuf.append(c);
                break;
            }
        }

        return strBuf.toString();
    }

    public static String encodeURL(String str)
    {
        try
        {
            return URLEncoder.encode(str, "UTF-8");
        }
        catch(Exception e)
        {
            return str;
        }
    }

    public static String encodeJavascript(String str)
    {
        if(str == null)
            return "";
        int len = str.length();
        StringBuffer strBuf = new StringBuffer(len + 50);
        for(int i = 0; i < len; i++)
        {
            char c = str.charAt(i);
            switch(c)
            {
            case 92: // '\\'
                strBuf.append("\\\\");
                break;

            case 34: // '"'
                strBuf.append("\\\"");
                break;

            case 39: // '\''
                strBuf.append("\\'");
                break;

            case 37: // '%'
                strBuf.append((new StringBuilder()).append("\\x").append(Integer.toHexString(37)).toString());
                break;

            case 13: // '\r'
                strBuf.append("\\r");
                break;

            case 10: // '\n'
                strBuf.append("\\n");
                break;

            default:
                strBuf.append(c);
                break;
            }
        }

        return strBuf.toString();
    }

    public static String formatDate(String dateFormat, Date date)
    {
        DateFormat dateformat = new SimpleDateFormat(dateFormat);
        return dateformat.format(date);
    }

    public static String formatNumber(String numberFormat, Object numberObject)
    {
        DecimalFormat decimalFormat = new DecimalFormat(numberFormat);
        return decimalFormat.format(numberObject);
    }

    public static String getI18N(String text)
    {
        StringBuffer buf = new StringBuffer(text);
        for(int startIndex = buf.indexOf("${"); startIndex != -1;)
        {
            int endIndex = buf.indexOf("}", startIndex + PLACEHOLDER_PREFIX_LENGTH);
            if(endIndex != -1)
            {
                String placeholder = buf.substring(startIndex + PLACEHOLDER_PREFIX_LENGTH, endIndex);
                int nextIndex = endIndex + PLACEHOLDER_SUFFIX_LENGTH;
                try
                {
                    String propVal = SpringHelper.getMessage(placeholder);
                    log.debug("SpringHelper.getMessage [{}] with: {}", placeholder, propVal);
                    if(propVal != null)
                    {
                        buf.replace(startIndex, endIndex + PLACEHOLDER_SUFFIX_LENGTH, propVal);
                        nextIndex = startIndex + propVal.length();
                    } else
                    {
                        log.error("Could not resolve placeholder '{}' as i18n resources.", placeholder);
                    }
                }
                catch(Throwable ex)
                {
                    log.error("Could not resolve placeholder '{}' as i18n resource: {}", placeholder, ex);
                }
                startIndex = buf.indexOf("${", nextIndex);
            } else
            {
                startIndex = -1;
            }
        }

        return buf.toString();
    }

    public static Object fromJson(String json, Class clazz)
        throws UtilityException
    {
        try
        {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, clazz);
        }
        catch(Exception e)
        {
            log.error("Fail to get Object[type:{}] from JSON: {}", clazz.getCanonicalName(), json);
            e.printStackTrace();
            throw new UtilityException((new StringBuilder()).append("Fail to get Object as ").append(clazz.getSimpleName()).append(" from String: ").append(json).toString(), e);
        }
    }

    public static String toJson(Object src)
    {
        try
        {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(src);
        }
        catch(Exception e)
        {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static String toJson(Object src, Type typeOfSrc)
    {
        Gson gson = new Gson();
        return gson.toJson(src, typeOfSrc);
    }

    public static String toJsonWithGson(Object src)
    {
        Gson gson = (new GsonBuilder()).excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(src);
    }

    public static String serializeObject(Serializable o)
        throws IOException
    {
        if(o == null)
        {
            return null;
        } else
        {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(o);
            out.close();
            byte buf[] = bos.toByteArray();
            return encodeHex(buf);
        }
    }

    public static final String encodeHex(byte bytes[])
    {
        StringBuffer buf = new StringBuffer(bytes.length * 2);
        for(int i = 0; i < bytes.length; i++)
        {
            if((bytes[i] & 0xff) < 16)
                buf.append("0");
            buf.append(Long.toString(bytes[i] & 0xff, 16));
        }

        return buf.toString();
    }

    public static Serializable deserializeObject(String hexStr)
        throws IOException, ClassNotFoundException
    {
        byte bytes[] = decodeHex(hexStr);
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
        Serializable obj = (Serializable)in.readObject();
        in.close();
        return obj;
    }

    public static final byte[] decodeHex(String hex)
    {
        char chars[] = hex.toCharArray();
        byte bytes[] = new byte[chars.length / 2];
        int byteCount = 0;
        for(int i = 0; i < chars.length; i += 2)
        {
            int newByte = 0;
            newByte |= hexCharToByte(chars[i]);
            newByte <<= 4;
            newByte |= hexCharToByte(chars[i + 1]);
            bytes[byteCount] = (byte)newByte;
            byteCount++;
        }

        return bytes;
    }

    private static final byte hexCharToByte(char ch)
    {
        byte result = 0;
        if(ch >= '1' && ch <= '9')
            result = (byte)(ch - 48);
        else
        if(ch >= 'a' && ch <= 'f')
            result = (byte)(ch - 87);
        return result;
    }

    /**
     * @deprecated Method compositeSplitStringToEncodingString is deprecated
     */

    public static String compositeSplitStringToEncodingString(String splitString, String encoding)
        throws UnsupportedEncodingException
    {
        if(splitString != null && splitString.length() > 0)
        {
            char resChars[] = splitString.toCharArray();
            byte resBytes[] = new byte[resChars.length];
            for(int i = 0; i < resChars.length; i++)
                resBytes[i] = (byte)resChars[i];

            return new String(resBytes, encoding);
        } else
        {
            return null;
        }
    }

    /**
     * @deprecated Method encodingTransfer is deprecated
     */

    public static String encodingTransfer(String inputString, String fromEncoding, String toEncoding)
    {
        if(inputString == null || inputString.length() == 0)
            return inputString;
        try
        {
            return new String(inputString.getBytes(fromEncoding), toEncoding);
        }
        catch(Exception e)
        {
            return inputString;
        }
    }

    /**
     * @deprecated Method Iso2Utf is deprecated
     */

    public static String Iso2Utf(String original)
    {
        if(isEmpty(original))
            return original;
        String output = null;
        try
        {
            output = new String(original.getBytes("ISO8859-1"), "UTF-8");
        }
        catch(UnsupportedEncodingException e)
        {
            log.error("Fail to transform String from ISO8859 to UTF-8: {}", original);
        }
        return output;
    }

    /**
     * @deprecated Method replace is deprecated
     */

    public static String replace(String str, String token, String sub)
    {
        if(str == null || token == null || sub == null || str.length() == 0 || token.length() == 0 || sub.length() == 0)
            return str;
        if(str.indexOf(token) == -1)
            return str;
        int len = token.length();
        StringBuffer buf = new StringBuffer();
        int lastPos;
        int curPos;
        for(lastPos = 0; (curPos = str.indexOf(token, lastPos)) != -1; lastPos = curPos + len)
        {
            buf.append(str.substring(lastPos, curPos));
            buf.append(sub);
        }

        if(lastPos < str.length())
            buf.append(str.substring(lastPos));
        return buf.toString();
    }

    public static final String PLACEHOLDER_PREFIX = "${";
    public static final int PLACEHOLDER_PREFIX_LENGTH = "${".length();
    public static final String PLACEHOLDER_SUFFIX = "}";
    public static final int PLACEHOLDER_SUFFIX_LENGTH = "}".length();
    private static final Logger log = LoggerFactory.getLogger(StrUtil.class);

}
