// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PMSCipher.java

package com.cnpc.pms.base.security;

import com.cnpc.pms.base.util.SpringHelper;
import java.security.Provider;
import java.security.Security;
import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

public class PMSCipher
{

    public static PMSCipher getInstance()
    {
        return instance;
    }

    public static void main(String args[])
        throws Exception
    {
        PMSCipher kc = getInstance();
        String encrypted = kc.encrypt("this is a test, test if it works. looks like it work fine, good!");
        LOG.debug(encrypted);
        String str = kc.decrypt(encrypted);
        LOG.debug(str);
    }

    private PMSCipher()
    {
        ApplicationContext ctx = SpringHelper.getApplicationContext();
        Provider sunJce = (Provider)ctx.getBean("Provider");
        Security.addProvider(sunJce);
        byte salt[] = {
            113, 57, -35, -63, 24, -16, 33, 72
        };
        char pwd[] = "b9e$fBo4%>@P^w4Avm5(n8X*!0L0or=&.grG".toCharArray();
        pbeParamSpec = new PBEParameterSpec(salt, 10);
        PBEKeySpec pbeKeySpec = new PBEKeySpec(pwd);
        try
        {
            SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            pbeKey = keyFac.generateSecret(pbeKeySpec);
            cipher = Cipher.getInstance("PBEWithMD5AndDES");
        }
        catch(Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }

    public byte[] decrypt(byte data[])
        throws Exception
    {
        cipher.init(2, pbeKey, pbeParamSpec);
        return cipher.doFinal(data);
    }

    public String decrypt(String data)
        throws Exception
    {
        byte cipherText[] = Base64.decodeBase64(data.getBytes("UTF8"));
        byte clearText[] = decrypt(cipherText);
        return new String(clearText, "UTF8");
    }

    public byte[] encrypt(byte data[])
        throws Exception
    {
        cipher.init(1, pbeKey, pbeParamSpec);
        return cipher.doFinal(data);
    }

    public String encrypt(String data)
        throws Exception
    {
        byte cipherText[] = encrypt(data.getBytes("UTF8"));
        return new String(Base64.encodeBase64(cipherText), "UTF8");
    }

    private static final Logger LOG = Logger.getLogger(PMSCipher.class);
    private static PMSCipher instance = new PMSCipher();
    private Cipher cipher;
    private PBEParameterSpec pbeParamSpec;
    private SecretKey pbeKey;

}
