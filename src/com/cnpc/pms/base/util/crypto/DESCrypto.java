// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DESCrypto.java

package com.cnpc.pms.base.util.crypto;

import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DESCrypto
{

    public DESCrypto()
        throws Exception
    {
        this(defaultStrKey);
    }

    public DESCrypto(String strKey)
        throws Exception
    {
        decryptCipher = Cipher.getInstance("DES");
        encryptCipher = Cipher.getInstance("DES");
        if(strKey == null)
        {
            throw new Exception("NullException :strKey should not be null");
        } else
        {
            SecureRandom sr = new SecureRandom();
            SecretKey desKey = generateDESKey(strKey);
            encryptCipher.init(1, desKey, sr);
            decryptCipher.init(2, desKey, sr);
            return;
        }
    }

    public byte[] decrypt(byte source[])
    {
        byte result[] = null;
        try
        {
            result = decryptCipher.doFinal(source);
        }
        catch(IllegalBlockSizeException e)
        {
            LOGGER.error("throw an IllegalBlockSizeException");
        }
        catch(BadPaddingException e)
        {
            LOGGER.error("throw a BadPaddingException");
        }
        return result;
    }

    public OutputStream decrypt(InputStream input)
    {
        OutputStream output = new ByteArrayOutputStream();
        CipherInputStream cis = new CipherInputStream(input, decryptCipher);
        byte buffer[] = new byte[1024];
        try
        {
            int r;
            while((r = cis.read(buffer)) > 0) 
                output.write(buffer, 0, r);
            cis.close();
            input.close();
        }
        catch(IOException e)
        {
            LOGGER.error("throw IOException ");
        }
        return output;
    }

    public byte[] encrypt(byte source[])
    {
        byte result[] = null;
        try
        {
            result = encryptCipher.doFinal(source);
        }
        catch(IllegalBlockSizeException e)
        {
            LOGGER.error("throw an IllegalBlockSizeException");
        }
        catch(BadPaddingException e)
        {
            LOGGER.error("throw a BadPaddingException");
        }
        return result;
    }

    public OutputStream encrypt(InputStream input)
    {
        OutputStream output = new ByteArrayOutputStream();
        CipherInputStream cis = new CipherInputStream(input, encryptCipher);
        byte buffer[] = new byte[1024];
        try
        {
            int r;
            while((r = cis.read(buffer)) > 0) 
                output.write(buffer, 0, r);
            cis.close();
            input.close();
        }
        catch(IOException e)
        {
            LOGGER.error("throw IOException ");
        }
        return output;
    }

    private final SecretKey generateDESKey(String strKey)
        throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException
    {
        byte byteKey[] = getByteKey(strKey);
        DESKeySpec dks = null;
        SecretKeyFactory keyFactory = null;
        SecretKey secretKey = null;
        dks = new DESKeySpec(byteKey);
        keyFactory = SecretKeyFactory.getInstance("DES");
        secretKey = keyFactory.generateSecret(dks);
        return secretKey;
    }

    private final byte[] getByteKey(String strKey)
    {
        byte byteKey[] = new byte[8];
        if(strKey.getBytes().length < 8)
        {
            for(int i = 0; i < strKey.getBytes().length; i++)
                byteKey[i] = strKey.getBytes()[i];

            for(int i = strKey.getBytes().length; i < 8; i++)
                byteKey[i] = 0;

        } else
        {
            for(int i = 0; i < 8; i++)
                byteKey[i] = strKey.getBytes()[i];

        }
        return byteKey;
    }

    private static final String ALGORITHM = "DES";
    private static String defaultStrKey = "cnpc";
    private Cipher decryptCipher;
    private Cipher encryptCipher;
    private static final Logger LOGGER = LoggerFactory.getLogger(DESCrypto.class);

}
