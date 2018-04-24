// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   KaptcherHelper.java

package com.cnpc.pms.base.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class KaptcherHelper
{

    public KaptcherHelper()
    {
    }

    public static boolean checkVerifyCode(String kaptchaReceived, HttpServletRequest request)
    {
        String kaptchaExpected = (String)request.getSession().getAttribute("KAPTCHA_SESSION_KEY");
        return kaptchaExpected != null && kaptchaExpected.equalsIgnoreCase(kaptchaReceived);
    }
}
