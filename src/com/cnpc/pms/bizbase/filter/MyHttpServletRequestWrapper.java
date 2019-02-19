package com.cnpc.pms.bizbase.filter;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Servlet Filter implementation class MyHttpServletRequestWrapper
 */
public class MyHttpServletRequestWrapper extends HttpServletRequestWrapper{

	private HttpServletRequest request;

    public MyHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.request = request;
    }

    @Override
    public String getParameter(String name) {
        //根据请求的方式的不同去处理
        String method = request.getMethod();
        if("get".equalsIgnoreCase(method)) {
            String value = null;
            try {
                value = new String(request.getParameter(name).getBytes("ISO-8859-1"),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return value;
        }
        if("post".equalsIgnoreCase(method)) {
            try {
                request.setCharacterEncoding("UTF-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return super.getParameter(name);
    }

}
