// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Base64Util.java

package com.cnpc.pms.base.util;

import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.binary.Base64;

public class Base64Util
{

    public Base64Util()
    {
    }

    public static byte[] decode(byte encoded[])
    {
        return Base64.decodeBase64(encoded);
    }

    public static String decode(String str)
    {
        String decoded = new String(Base64.decodeBase64(decode(str.getBytes())));
        return decoded;
    }

    public static String decode(String str, String charsetName)
    {
        String decoded = null;
        try
        {
            decoded = new String(Base64.decodeBase64(str.getBytes(charsetName)));
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return decoded;
    }

    public static byte[] encode(byte beforeEncode[])
    {
        return Base64.encodeBase64(beforeEncode);
    }

    public static String encode(String str)
    {
        String encoded = new String(Base64.encodeBase64(encode(str.getBytes())));
        return encoded;
    }

    public static String encode(String str, String charsetName)
    {
        String encoded = null;
        try
        {
            encoded = new String(Base64.encodeBase64(str.getBytes(charsetName)));
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return encoded;
    }
}
