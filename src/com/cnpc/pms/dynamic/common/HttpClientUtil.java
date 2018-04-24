/**
 * gaobaolei
 */
package com.cnpc.pms.dynamic.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


/**
 * @author gaobaolei
 *  接口请求工具
 */
public class HttpClientUtil {
	
	public String getRemoteData(String url,JSONObject param){
		HttpClient client = null;
		HttpPost httpPost  = null;
		String result="";
		try {
			client = new SSLClient();
			httpPost = new HttpPost(url);
			httpPost.addHeader("Content-type","application/json; charset=utf-8");  
			httpPost.setHeader("Accept", "application/json");  
			
			//设置参数  
			
			httpPost.setEntity(new StringEntity(param.toString(), Charset.forName("UTF-8")));  
	       /* List<NameValuePair> list = new ArrayList<NameValuePair>();  
	        Iterator iterator = param.entrySet().iterator();  
	        while(iterator.hasNext()){  
	            Entry<String,String> elem = (Entry<String, String>) iterator.next();  
	            list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));  
	        }  
	        if(list.size() > 0){  
	            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,"UTF-8");  
	            httpPost.setEntity(entity);  
	        }  */
	        
	        HttpResponse response = client.execute(httpPost);  
	        if(response != null){  
	            HttpEntity resEntity = response.getEntity();  
	            if(resEntity != null){  
	                result = EntityUtils.toString(resEntity,"UTF-8");  
	            }  
	        }  
		} catch (ConnectTimeoutException e) {
			result = "connectTimeout";
			return result;
		}catch (SocketTimeoutException e) {
			result = "socketTimeout";
			return result;
		}catch (Exception e) {
			result = "other";
			return result;
		}
		return result;
	} 
	
	
	
	
	
	public String validateRemoteData(String url,String param){
		HttpClient client = null;
		HttpPost httpPost  = null;
		String result="";
		try {
			client = new SSLClient();
			httpPost = new HttpPost(url);
			httpPost.addHeader("Content-type","application/x-www-form-urlencoded");  
			//设置参数  
			httpPost.setEntity(new StringEntity(param.toString(), Charset.forName("UTF-8")));  
	       /* List<NameValuePair> list = new ArrayList<NameValuePair>();  
	        Iterator iterator = param.entrySet().iterator();  
	        while(iterator.hasNext()){  
	            Entry<String,String> elem = (Entry<String, String>) iterator.next();  
	            list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));  
	        }  
	        if(list.size() > 0){  
	            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,"UTF-8");  
	            httpPost.setEntity(entity);  
	        }  */
	        
	        HttpResponse response = client.execute(httpPost);  
	        if(response != null){  
	            HttpEntity resEntity = response.getEntity();  
	            if(resEntity != null){  
	                result = EntityUtils.toString(resEntity,"UTF-8");  
	            }  
	        }  
		} catch (ConnectTimeoutException e) {
			result = "connectTimeout";
			return result;
		}catch (SocketTimeoutException e) {
			result = "socketTimeout";
			return result;
		}catch (Exception e) {
			result = "other";
			return result;
		}
		return result;
	} 
	
	
	
	/**
	 * 调用接口方法 
	 * @param url  URL
	 * @param md5str  md5 sign
	 * @param param 参数body
	 * @return
	 */
	public String insRemoteData(String url,String md5str,String param){
		HttpClient client = null;
		HttpPost httpPost  = null;
		String result="";
		try {
			client = new SSLClient();
			httpPost = new HttpPost(url);
			httpPost.addHeader("Content-type","application/json; charset=utf-8");  
			httpPost.setHeader("md5", md5str);
			httpPost.setHeader("appId","guoan_data");
			httpPost.setHeader("Accept", "application/json");  
		
			//设置参数  
			
			httpPost.setEntity(new StringEntity(param.toString(), Charset.forName("UTF-8")));  
	       /* List<NameValuePair> list = new ArrayList<NameValuePair>();  
	        Iterator iterator = param.entrySet().iterator();  
	        while(iterator.hasNext()){  
	            Entry<String,String> elem = (Entry<String, String>) iterator.next();  
	            list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));  
	        }  
	        if(list.size() > 0){  
	            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,"UTF-8");  
	            httpPost.setEntity(entity);  
	        }  */
	        
	        HttpResponse response = client.execute(httpPost);  
	        if(response != null){  
	            HttpEntity resEntity = response.getEntity();  
	            if(resEntity != null){  
	                result = EntityUtils.toString(resEntity,"UTF-8");  
	            }  
	        }  
		} catch (ConnectTimeoutException e) {
			result = "connectTimeout";
			return result;
		}catch (SocketTimeoutException e) {
			result = "socketTimeout";
			return result;
		}catch (Exception e) {
			result = "other";
			return result;
		}
		return result;
	} 
}
