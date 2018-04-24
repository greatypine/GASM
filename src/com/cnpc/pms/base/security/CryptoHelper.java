// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CryptoHelper.java

package com.cnpc.pms.base.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

// Referenced classes of package com.cnpc.pms.base.security:
//            PMSCipher

public class CryptoHelper
{

    public CryptoHelper()
    {
    }

    public static String decrypt(String data)
        throws Exception
    {
        return PMSCipher.getInstance().decrypt(data);
    }

    public static String digest(String data)
    {
        try
        {
            byte inputBytes[] = data.getBytes("UTF8");
            MessageDigest msgdigest = MessageDigest.getInstance("SHA");
            msgdigest.update(inputBytes);
            byte bytes[] = msgdigest.digest();
            String outStr = new String(Base64.encodeBase64(bytes), "UTF8");
            return outStr;
        }
        catch(NoSuchAlgorithmException e)
        {
            return null;
        }
        catch(UnsupportedEncodingException e)
        {
            return null;
        }
    }

    public static String encrypt(String data)
        throws Exception
    {
        return PMSCipher.getInstance().encrypt(data);
    }

    public static void main(String args[])
    {
        try
        {
            String str = args[0];
            String enStr = encrypt(str);
            LOG.debug(enStr);
        }
        catch(Exception ex)
        {
            LOG.debug(ex.getMessage());
        }
    }

    private static final Logger LOG = Logger.getLogger(CryptoHelper.class);

}
