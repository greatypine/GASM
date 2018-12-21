/*
 * 
 */
package com.cnpc.pms.bizbase.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cnpc.pms.base.common.model.ClientRequestObject;
import com.cnpc.pms.base.exception.DispatcherException;
import com.cnpc.pms.base.exception.UtilityException;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.operator.Condition;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.security.UserSession;
import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.base.util.StrUtil;
import com.cnpc.pms.bizbase.rbac.resourcemanage.entity.AuthModel;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.platform.entity.SystemUser;
import com.cnpc.pms.platform.entity.SystemUserInfo;

/**
 * The Authority Filter.
 * 
 * Copyright(c) 2014 Yadea Technology Group , http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011/4/26
 */
public class AuthFilter extends OncePerRequestFilter {

	/** LOGIN_URL. */
	private static final String LOGIN_URL = "login.html";

	/** The Constant ERROR_URL. */
	private static final String ERROR_URL = "error.html";

	/** The Constant DISPATCHER_URL. */
	private static final String DISPATCHER_URL = "dispatcher.action";

	/** The Constant log. */
	private static final Logger LOG = LoggerFactory.getLogger(AuthFilter.class);

	/**
	 * 获取日志性能检测使用的Log对象,这个Log仅记录对应的性能日志
	 */
	private static final Logger bLog = LoggerFactory.getLogger(BeachmarkFilter.class);

	/**
	 * Do filter internal.
	 * 
	 * @param servletRequest
	 *            the servlet request
	 * @param servletResponse
	 *            the servlet response
	 * @param filterChain
	 *            the filter chain
	 * @throws ServletException
	 *             the servlet exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @see org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
			FilterChain filterChain) throws ServletException, IOException {

		LOG.trace("Enter doFilter of AuthFilter.");
		// System.out.println(" sso
		// doFilterInternal--begin-------:"+Thread.currentThread().getId());
		String url = servletRequest.getRequestURI();
		String params = servletRequest.getQueryString();
		if (params != null && !"".equals(params)) {
			url = url + "?" + params;
		}
		LOG.debug("url====" + url);
		
		if(url.indexOf("login.html")>=0) {
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}
		
		UserSession userSession = SessionManager.getUserSession();
		
		if(userSession==null) {
			 AttributePrincipal principal=(AttributePrincipal)servletRequest.getUserPrincipal();
	         Map<String, Object> attributes = principal.getAttributes();
	         
	            SystemUser systemuser = null;
	            String username = principal.getName();
	            MultiValueMap<String, String> casParams = new LinkedMultiValueMap<String, String>();
	            RestTemplate restTemplate = (RestTemplate) SpringHelper.getBean("restTemplate");
	            //获取登录用户信息
	            String casurl = PropertiesUtil.getValue("getCasUserURL");
	            HttpHeaders headers = new HttpHeaders();
	            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	            
	    		String contentPath = servletRequest.getContextPath();
	            String queryPath = contentPath + "/bizbase/login.html?username="+username+"&pwd="+attributes.get("password").toString();
	            servletResponse.sendRedirect(queryPath);
	            
	            LOG.debug("loginquerypath====" + queryPath);
	            
	            /*casParams.add("username",username);
	            casParams.add("password", attributes.get("password").toString());
	            HttpEntity<MultiValueMap<String, String>> requestparams = new HttpEntity<MultiValueMap<String, String>>(casParams, headers);
	            ResponseEntity<SystemUser> responseEntity = restTemplate.postForEntity(casurl,requestparams, SystemUser.class);
	            systemuser = responseEntity.getBody();*/
	            //将数据存储到session中
	            /*String eid = (String) attributes.get("eid");
	            userSession = setDataToUserSession(userSession, Long.parseLong(eid),null);
	            SessionManager.setUserSession(userSession);*/
	            
		}
		
		
		if (url.indexOf(LOGIN_URL) >= 0) {
			filterChain.doFilter(servletRequest, servletResponse);
		} else if (url.indexOf("wdow.html") >= 0 || url.indexOf("wdowbid.html") >= 0) {
			filterChain.doFilter(servletRequest, servletResponse);
		} else if (url.indexOf("ntop.html") >= 0) {
			filterChain.doFilter(servletRequest, servletResponse);
		} else if (url.indexOf("ndow.html") >= 0) {
			filterChain.doFilter(servletRequest, servletResponse);
		} else if (url.indexOf("resetpwd.html") >= 0) {
			filterChain.doFilter(servletRequest, servletResponse);
		} else if (url.indexOf("initreset.html") >= 0) {
			filterChain.doFilter(servletRequest, servletResponse);
		} else if (url.indexOf("validation.html") >= 0) {
			filterChain.doFilter(servletRequest, servletResponse);
			// 通知公告
		} else if (url.indexOf("generalPortalContentView.html") >= 0) {
			filterChain.doFilter(servletRequest, servletResponse);
			// 通知公告More
		} else if (url.indexOf("generalPortalContentMore.html") >= 0) {
			filterChain.doFilter(servletRequest, servletResponse);
			// 内容页
		} else if (url.indexOf("content.html") >= 0) {
			filterChain.doFilter(servletRequest, servletResponse);
			// 重要科技活动
		} else if (url.indexOf("generalEventPublisFindView.html") >= 0) {
			filterChain.doFilter(servletRequest, servletResponse);
			// 重要科技活动More
		} else if (url.indexOf("generalEventContentMore.html") >= 0) {
			filterChain.doFilter(servletRequest, servletResponse);
			// 附件下载
		} else if (url.indexOf("DownloaderAction.action") >= 0) {
			filterChain.doFilter(servletRequest, servletResponse);
		} // 针对接口的请求，直接跳过
		else if (url.indexOf("interface.action") >= 0) {
			filterChain.doFilter(servletRequest, servletResponse);
		} else if (url.indexOf("downLoadTemplate.action") >= 0) {
			filterChain.doFilter(servletRequest, servletResponse);
		} /*
			 * else if(url.indexOf("human_city_list.html") >= 0){//人资系统的
			 * 城市员工管理页面 filterChain.doFilter(servletRequest, servletResponse); }
			 */
		else {
			if (url.indexOf(DISPATCHER_URL) >= 0) {
				String requestString = servletRequest.getParameter("requestString");
				if (null != requestString) {
					try {
						ClientRequestObject requestObj = parseClientRequestObject(requestString);
						url = requestObj.getManagerName() + "?" + requestObj.getMethodName();
					} catch (DispatcherException e) {
						LOG.trace(e.getMessage());
					}
				}

			}
			// skip the operation--> reset password;
			if (url.equals("userManager?resetPassword")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.contains("InterManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			/**
			 * 2018-03-22添加周围门店
			 * 
			 * @author sunning
			 */
			if (url.contains("StoreManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			// 跳过是否是初始口令的判断方法
			if (url.equals("userManager?isInitPassword")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			// skip 重要科技活动
			if (url.equals("generalEventManager?getGeneralEventList")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			// 大屏用户登录
			if (url.equals("userManager?isScreenUser")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			// skip 通知公告
			if (url.equals("generalPortalContentManager?getGeneralPortalContentDTOList")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			// skip 通知公告内容页
			if (url.equals("generalPortalContentManager?getContentById")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			// skip 帮助信息
			if (url.equals("generalPortalContentManager?getGeneralPortalContentList")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			/** query查询需要skip掉的 */
			if (url.equals("queryManager?getCachedCondition")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.equals("dictManager?findDictByName")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.equals("HouseManager?getHouseApp")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.equals("functionManager?getStatus")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.equals("queryConfigManager?getMetaInfo")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.equals("queryManager?executeQuery")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}

			// AndoridIOSManager跳过权限检查控制
			// Add by liujunsong 2015/10/19
			if (url.startsWith("AndroidIOSManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			// url属于 用户信息 放行
			if (url.startsWith("AppUserInfoManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.startsWith("UserManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.startsWith("HouseManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			// 取得系统数据
			if (url.startsWith("AppSystemManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			// 人物地址
			if (url.startsWith("AppPersonalAddressManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			// 关注的人
			if (url.startsWith("AppAttentionPeopleManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			// 健康预警
			if (url.startsWith("APPHealthWarningManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			// 健康档案
			if (url.startsWith("APPHealthRecordManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			// app的banner
			if (url.startsWith("AppBannerManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			// 调查问卷
			if (url.startsWith("AppQuestionnaireManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			// App调查问卷
			if (url.indexOf("AppQuestionnaire.html") > -1 || url.indexOf("questionnaire_subject.html") > -1) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			// 被关注的人
			if (url.startsWith("AppBeFocusedOnPeopleManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			// 咨询服务
			if (url.startsWith("AppConsultingServiceManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			// 意见反馈
			if (url.startsWith("AppFeedBackManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			// 快递
			if (url.startsWith("ExpressManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			// 健康生活
			if (url.startsWith("AppHealthyLifeAdRollingManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.startsWith("AppUserManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}

			// 住宅校验小区的入口
			if (url.startsWith("TinyVillageManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			// 住宅校验小区的入口
			if (url.startsWith("HouseManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}

			if (url.startsWith("BuildingManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}

			if (url.startsWith("ViewAddressCustomer")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}

			// 住宅校验平房的入口
			if (url.startsWith("BungalowManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			// 住宅校验楼号的入口
			if (url.startsWith("ResidentialBuildingsManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			// 住宅校验楼房房间号单元的入口
			if (url.startsWith("BldHouseManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			// 写字楼的入口
			if (url.startsWith("OfficeBuildingsManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			// 维修入户检测调查问卷
			if (url.startsWith("QuestionnaireQuestionManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			// 维修入户检测调查问卷(静态页面)
			if (url.startsWith("MaintenanceManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			// 添加空气检测(静态页面)
			if (url.startsWith("AirMonitorManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			// 保存上传图片接口
			if (url.startsWith("MaintePicManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}

			// 国安侠 员工登陆入口 wuyichao
			// ExcelHandleManager
			if (url.startsWith("ExcelHandleManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.startsWith("EmployeeManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.startsWith("EmployeeManagementManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.startsWith("ExpressCompanyManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			// 国安侠 查询客户信息入口
			if (url.startsWith("CustomerManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			// 国安侠 查询客户信息入口
			if (url.startsWith("VillageManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}

			// 国安侠 查询客户信息入口
			if (url.startsWith("CustomerDataManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}

			// demo
			if (url.startsWith("ReportDTOManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}

			// POIGoodAndServicesCategoriesManager
			if (url.startsWith("POIGoodAndServicesCategoriesManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}

			if (url.startsWith("TownManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.startsWith("storeManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.startsWith("BusinessInfoManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.startsWith("PersonDutyManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.startsWith("RelationManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.startsWith("OrderManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.startsWith("AreaManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}

			
			/**
			 * 新增接口配置信息 wuyichao
			 */
			if (url.startsWith("POIManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.startsWith("CustomerCharacterManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.startsWith("CustomerHobbyManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.startsWith("FamilyMemberManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.startsWith("FamilyManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.startsWith("SysDictionaryManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.startsWith("HouseDetailManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.startsWith("UserAdviceManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.startsWith("DailyReportManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}

			else if (url.indexOf("AppUploaderAction.action") >= 0) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}

			// App下载页面
			if (url.endsWith("download.html")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.contains("app_maps.html")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.contains("logout.html")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			/**
			 * 2018-03-22 大屏
			 * 
			 * @author sunning
			 */

			if (url.contains("index_headquarters.html")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}

			/**
			 * 2018-03-22 周围门店
			 * 
			 * @author sunning
			 */
			if (url.contains("store_round.html")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			// App下载页面
			if (url.indexOf("AdvertisementHTML") > -1) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}

			if (url.contains("fileUpload.action")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}

			if (url.contains("selectionFileUpload.action")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}

			if (url.contains("HouseStyleManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}

			if (url.contains("AppMessageManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.contains("ProvinceManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.contains("CustomerDataTemporaryManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.contains("TownManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.contains("OfficeManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.contains("BusinessInfoManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.contains("BusinessTypeManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.contains("CityManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.contains("CountyManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.contains("CustomerTemporaryManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (url.contains("StoreDetailManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}

			if (url.contains("DefaultConfigManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}

			// 社区动态
			if (url.contains("DynamicManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}

			if (url.contains("MessageNewManager")) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			if (userSession == null || userSession.getSessionData() == null
					|| userSession.getSessionData().get("user") == null) {
				String urlString = PropertiesUtil.getValue("ssoLoginURL");
				if (null != urlString && urlString.indexOf("http") < 0) {
					urlString = servletRequest.getContextPath() + urlString;
				}
				PrintWriter out = servletResponse.getWriter();
				out.print("<script>top.location='" + urlString + "'</script>");
				return;
			}
			
			
			
			if(url.indexOf("dologout")>=0) {
				HttpSession session = servletRequest.getSession();
				if (session.getAttribute(UserSession.SESSION_ATTRIBUTE_NAME) != null) {
					session.removeAttribute(UserSession.SESSION_ATTRIBUTE_NAME);
				}
				
				userSession=null;
				SessionManager.setUserSession(null);
				
				servletRequest.getSession().removeAttribute(AuthenticationFilter.CONST_CAS_ASSERTION);
			      SystemUser systemUser = SystemUserInfo.getInstance();
			      servletRequest.getSession().removeAttribute("user");
			      SystemUserInfo.destroy();
			      servletRequest.getSession().invalidate();
					PrintWriter out = servletResponse.getWriter();
			      out.print("<script>window.location='https://loginjs.guoanshuju.com/login/logout?service=GASM'</script>");
			}else {
			
			
			

			boolean result = auth(url, userSession);

			
			if (result) {
				// add by liujunsong 2014-06-18
				// Log文件中记录执行所使用的时间
				// bLog.info("Begin record beachmar . ---->");
				Long begin = new Date().getTime();
				filterChain.doFilter(servletRequest, servletResponse);
				Long end = new Date().getTime();
				User user = (User) userSession.getSessionData().get("user");
				bLog.info("userCode,url,cost=\t" + user.getCode() + "\t" + (end - begin) + "\t" + url);
			} else {
				System.out.println("============= url = " + url);
				int strStart = url.lastIndexOf("/");
				int strEnd = url.indexOf("html");
				String furl = url.substring(strStart + 1, strEnd + 4);
				System.out.println("============= furl = " + furl);
				String errorString = servletRequest.getContextPath() + "/bizbase/" + ERROR_URL + "?furl=" + furl;
				PrintWriter out = servletResponse.getWriter();
				out.print("<script>window.location='" + errorString + "'</script>");
			}
			
			}

		}
		// System.out.println(" sso
		// doFilterInternal----end-------:"+Thread.currentThread().getId());
		LOG.trace("Exit doFilter.");
	}

	/**
	 * Auth. 判断用户是否具有当前执行的url的权限
	 * 
	 * @param url
	 *            the url
	 * @param userSession
	 *            the user session
	 * @return true, if successful
	 */
	@SuppressWarnings("unchecked")
	private boolean auth(String url, UserSession userSession) {

		Hashtable<?, ?> sessiondate = (Hashtable<?, ?>) userSession.getSessionData();
		User user = (User) sessiondate.get("user");
		if (user != null) {
			if (user.isSystemManager()) {
				return true;
			}
		}
		List<String> commonACL = (List<String>) sessiondate.get("commonACL");
		List<String> userACL = (List<String>) sessiondate.get("userACL");
		for (String comAcl : commonACL) {
			// 如果url在commonACL里(需要权限控制),对url进行判断该用户是否具有此权限

			if (url.indexOf(comAcl) >= 0) {
				for (String acl : userACL) {
					if (url.indexOf(acl) >= 0) {
						return true;
					}
				}
				return false;
			}
		}
		// 如果url不在commonACL里(不需要权限控制),直接返回
		return true;
	}

	/**
     * 为门户登录成功的用户添加Session和权限 set Data To Session;
     */
    @SuppressWarnings({ "unchecked", "unused" })
    private UserSession setDataToUserSession(UserSession userSession, Long userid,SystemUser casuser) {

        if (null == userSession) {
            userSession = new UserSession();
        }
        Hashtable sessionData = new Hashtable();

        UserManager userManager = (UserManager) SpringHelper.getBean("userManager");

        // get user and put into session;
        // System.out.println(" sso
        // lisongtao----setDataToUserSession---start----:"+Thread.currentThread().getId());
        User user = (User) userManager.getObject(userid);
        // System.out.println(" sso
        // lisongtao----getUserByUserCode-------:"+Thread.currentThread().getId());
        if (null != user) {
            sessionData.put("user", user);

            // get ACL, and then put into session;
            // System.out.println(" sso
            // lisongtao----userManager.getCommonACL---start----:"+Thread.currentThread().getId());
            List<String> commonACL = userManager.getCommonACL();
            // System.out.println(" sso
            // lisongtao----userManager.getCommonACL---end----:"+Thread.currentThread().getId());
            sessionData.put("commonACL", commonACL);

            List<String> listACL = new ArrayList<String>();
            List<String> codes = new ArrayList<String>();

            String urlTemp = "";
            String code = "";
            // System.out.println("lisongtao:get acl
            // begin"+"--usercode:"+userCode+"- getACL start-thread id"
            // + Thread.currentThread().getId());
            List<AuthModel> auths = userManager.getACL(user.getId());
            // System.out.println("lisongtao:get acl
            // end"+"--usercode:"+userCode+"- getACL end-thread id"
            // + Thread.currentThread().getId());
            for (AuthModel auth : auths) {
                urlTemp = auth.getUrl();
                code = auth.getActivityCode();
                if (urlTemp != null) {
                    listACL.add(urlTemp);
                }
                codes.add(code);
            }
            sessionData.put("codes", codes);
            sessionData.put("userACL", listACL);

            // get dataACL, and then put into session;
            // System.out.println("lisongtao:get acl
            // begin"+"--dataACL:"+userCode+"- data ACL start-thread id"
            // + Thread.currentThread().getId());
            Map<String, IFilter> dataACL = userManager.getDataACL(user.getId());
            Map<String, Set<Condition>> dataACLForAdd = userManager.getDataACLForAdd(user.getId());
            // System.out.println("lisongtao:get acl
            // begin"+"--dataACL:"+userCode+"- data ACL end-thread id"
            // + Thread.currentThread().getId());
            sessionData.put("dataACL", dataACL);
            sessionData.put("dataACLForAdd", dataACLForAdd);
            //sessionData.put("casuser", casuser);

            userSession.setSessionData(sessionData);
        }
        // System.out.println(" sso
        // lisongtao----getUserByUserCode----end---:"+Thread.currentThread().getId());
        return userSession;
    }

	/**
	 * Parses the client request object.
	 * 
	 * @param clientInvokeString
	 *            the client invoke string
	 * @return the client request object
	 * @throws DispatcherException
	 *             the dispatcher exception
	 */
	private ClientRequestObject parseClientRequestObject(String clientInvokeString) throws DispatcherException {
		ClientRequestObject reqObj;
		try {
			reqObj = (ClientRequestObject) StrUtil.fromJson(clientInvokeString, ClientRequestObject.class);
			return reqObj;
		} catch (UtilityException e) {
			throw new DispatcherException("Malform client invoke string submit", e);
		}
	}
}
