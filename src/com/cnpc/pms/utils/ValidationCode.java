package com.cnpc.pms.utils;

import java.util.Random;

public class ValidationCode {
	/**
	 * 生成四位验证码 
	 * @return
	 */
	public static String createCode(){
		String selectChar ="1,2,3,4,5,6,7,8,9,a,b,c,d,e,f,g,h,j,k,l,m,n,p,q,r,s,t,u,v,w,x,y,z,A,B,C,D,E,F,G,H,J,K,L,M,N,P,Q,R,S,T,U,V,W,X,Y,Z";      
		String[] selectchars = selectChar.split(",");
		String code = "";
		for(int n=0;n<4;n++){
			int i = new Random().nextInt(55)+1;
			code+=selectchars[i];
		}
		return code;
	}
}
