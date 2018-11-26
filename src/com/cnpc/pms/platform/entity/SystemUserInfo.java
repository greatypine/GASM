package com.cnpc.pms.platform.entity;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.cnpc.pms.base.util.SpringHelper;

/**
 * @author justin 
 * @2017年3月5日 上午8:02:30
 * @TODO 注释：
 */
public class SystemUserInfo {
	
	private static SystemUser SYSTEM_USER = null;
	
	public static synchronized SystemUser getInstance(){
		SYSTEM_USER = new SystemUser();
		return SYSTEM_USER;
	}

	public static SystemUser getSystemUser() {
		return getSystemUser(null);
	}
	public static SystemUser getSystemUser(HttpServletRequest request) {
		//-----------------------从cas认证信息中获取用户名密码----------------------------
		SystemUser systemuser = null;
		if(request!=null) {
			Object user = request.getSession().getAttribute("user");
			if(user!=null) {
				systemuser = (SystemUser) user;
			}else {
				AttributePrincipal principal=(AttributePrincipal)request.getUserPrincipal();
				String username = principal.getName();
				MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
				Map<String, Object> attributes = principal.getAttributes();
				RestTemplate restTemplate = (RestTemplate) SpringHelper.getBean("restTemplate");
				final String url = "http://10.16.31.229:9999/systemuser/getCurrentUserInfo";
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
				params.add("username",username);
				params.add("password", attributes.get("password").toString());
				HttpEntity<MultiValueMap<String, String>> requestparams = new HttpEntity<MultiValueMap<String, String>>(params, headers);
				ResponseEntity<SystemUser> responseEntity = restTemplate.postForEntity(url,requestparams, SystemUser.class);
				systemuser = responseEntity.getBody();
			}
		}else {
			//如果不穿session就将用户信息在第一次获取成功后存入本地线程池中
		}
		//---------------------请求cas获取用户信息-----------------
		return systemuser;
	}
	public static SystemUser setSystemUser(SystemUser user) {
		SYSTEM_USER = user;
		return SYSTEM_USER;
	}

	public static void destroy() {
		SYSTEM_USER = null;
	}
	
}