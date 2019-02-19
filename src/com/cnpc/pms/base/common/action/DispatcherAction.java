package com.cnpc.pms.base.common.action;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnpc.pms.base.audit.annotation.Audit;
import com.cnpc.pms.base.audit.entity.AuditLog;
import com.cnpc.pms.base.common.model.ClientRequestObject;
import com.cnpc.pms.base.common.model.ClientResponseObject;
import com.cnpc.pms.base.exception.DispatcherException;
import com.cnpc.pms.base.exception.PMSManagerException;
import com.cnpc.pms.base.exception.UtilityException;
import com.cnpc.pms.base.exception.ValidationException;
import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.manager.core.DynamicType;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.security.UserSession;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.base.util.StrUtil;

/**
 * <p>
 * <b>Action Dispatcher</b>
 * </p>
 * Action dispatcher is in charged to handle request and response between server side and client side.<br/>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author lushu
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
public class DispatcherAction extends HttpServlet {

	/** Serial Version UID. */
	private static final long serialVersionUID = 1L;
	/** The Constant log. */
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	protected static final String REQUEST_STRING = "requestString";
	private boolean auditEnabled = false;
	private IManager auditLogManager;

	protected final Map<String, IManager> BUSINESS_INSTANCE_CACHE = new HashMap<String, IManager>();
	protected final Map<String, MethodAgent> methodAgents = new HashMap<String, MethodAgent>();

	// protected final Map<String, Method> BUSINESS_METHOD_CACHE = new HashMap<String, Method>();
	// protected final Map<String, Class<?>[]> PARAMETER_TYPES = new HashMap<String, Class<?>[]>();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		// request.setCharacterEncoding("UTF-8");// 2013.6.9 Not necessary, already set in CharacterEncodingFilter
		String repeatable = request.getParameter("repeatable");
		if (repeatable != null && repeatable.equals("false")) {
			String token = UUID.randomUUID().toString().replace("-", "");
			request.getSession().setAttribute("token", token);
			response.getWriter().write("{\"result\":true,\"token\":\"" + token + "\"}");
		} else {
			// response.getWriter().write(execute(request));
			execute(request, response);
		}
	}

	/**
	 * <h3>Dispatcher Action Executer</h3> The very handler to handle main http request from client.
	 * 
	 *            Json String to invoke server side managers.
	 * @param request
	 *            the req
	 * @return ResponseString in Json format
	 * @throws IOException
	 */
	protected void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.setCharacterEncoding("UTF-8");
		String requestString = request.getParameter(REQUEST_STRING);// for log
		System.err.println("***********************************打印请求过来的字符串requestString***********************************");
		System.out.println("打印 requestString 的值："+requestString);
		System.out.println("打印 requestString 的值："+requestString);
		System.out.println("打印 requestString 的值："+requestString);
		System.out.println("打印 requestString 的值："+requestString);
		System.out.println("***********************************打印请求过来的字符串requestString***********************************");
		
		ClientResponseObject responseObj = new ClientResponseObject();
		
		try {
			ClientRequestObject requestObj = getClientRequestObject(request);

			
			prepareInvokeTarget(requestObj);

			// Verify token;
			if (true == StrUtil.isNotEmpty(requestObj.getToken())) {
				if (request.getSession().getAttribute("token") != null
						&& request.getSession().getAttribute("token").equals(requestObj.getToken())) {
					request.getSession().setAttribute("token", "");
				} else {
					// TODO: all info needs deal with i18n message
					log.warn("Repeat submit: {}", requestString);
					throw new DispatcherException("Repeat submit.");
				}
			}

			Object resultValue = doJob(requestObj, responseObj);
			// TODO: optimize this API to remove requestObj?
			afterInvoke(resultValue, responseObj, request, requestObj);

		} catch (Exception e) {
			//find* result
			responseObj.setResult(false);
			responseObj.setMessage(e.getMessage());
			if (e instanceof DispatcherException == false) {
				log.error("Unhandle exception in dispatcher action servlet. Check this: {}", e.getMessage(), e);
			}
		}
		// return responseObj.toJSON();
		doResponse(responseObj, response);
	}

	/**
	 * Get the client request object from the Http Request.
	 * 
	 * @param request
	 *            the HttpServletRequest
	 * @return the client request object
	 * @throws DispatcherException
	 *             the dispatcher exception
	 */
	protected ClientRequestObject getClientRequestObject(HttpServletRequest request) throws DispatcherException {
		return getRequestObject(request, ClientRequestObject.class);
	}

	protected <T> T getRequestObject(HttpServletRequest request, Class<T> clazz) throws DispatcherException {
		String clientInvokeString = request.getParameter(REQUEST_STRING);
		T requestObj;
		try {
			requestObj = (T)StrUtil.fromJson(clientInvokeString, clazz);
			return requestObj;
		} catch (UtilityException e) {
			log.error("Malform client invoke String: {}", clientInvokeString);
			throw new DispatcherException("Malform client invoke string: " + clientInvokeString, e);
		}
	}

	/**
	 * Prepare invoke target of the ClientRequestObject
	 *
	 *            the client invoke string
	 * @return the client request object
	 * @throws DispatcherException
	 *             the dispatcher exception
	 */
	protected final void prepareInvokeTarget(ClientRequestObject requestObj) throws DispatcherException {
		if (StrUtil.isBlank(requestObj.getManagerName()) || StrUtil.isBlank(requestObj.getMethodName())) {
			log.error("Missing invoke manager or method: {}", requestObj.getKey());
			throw new DispatcherException("Missing invoke target: " + requestObj.getKey());
		}
		String methodKey = requestObj.getKey();
		if (!methodAgents.containsKey(methodKey)) {
			synchronized (this) {
				if (!methodAgents.containsKey(methodKey)) {
					registerMethods(requestObj);
				}
			}
		}
	}

	protected final void registerMethods(ClientRequestObject requestObj) throws DispatcherException {
		String methodKey = requestObj.getKey();
		String className = requestObj.getManagerName();
		String methodName = requestObj.getMethodName();
		IManager manager = BUSINESS_INSTANCE_CACHE.get(className);
		if (null == manager) {
			manager = SpringHelper.getManagerByClass(className);
			if (null != manager) {
				BUSINESS_INSTANCE_CACHE.put(className, manager);
			} else {
				log.error("Fail to get manager: {}, request string: \n{}", className, requestObj.toString());
				throw new DispatcherException("Incorrect manager invoked: " + methodKey);
			}
		}
		Method method = null;
		// Class<?>[] types = new Class<?>[0];
		if (true == StrUtil.isNotBlank(methodName)) {
			Class<?>[] interfaces = manager.getClass().getInterfaces();
			interfaces: for (Class<?> clazz : interfaces) {
				for (Method m : clazz.getMethods()) {
					if (m.getName().equalsIgnoreCase(methodName)) {
						method = m;
						MethodAgent agent = new MethodAgent(method);
						Class<?>[] types = m.getParameterTypes();
						log.debug("Get method[{}] in [{}], with parameters: {}",
								new Object[] { methodName, clazz.getSimpleName(), types });
						Annotation[][] annos = m.getParameterAnnotations();
						for (int i = 0; i < annos.length; i++) {
							for (Annotation anno : annos[i]) {
								if (anno.annotationType().equals(DynamicType.class)) {
									// NOTE: Can specific the method or value here to determine
									Class<?> newType = manager.getEntityClass();
									log.debug("Replace Original Parameter type {} with {}, index {}", new Object[] {
											types[i], newType, i });
									types[i] = newType;
									break;
								}
							}
						}
						agent.setParameterTypes(types);
						agent.setAudit(method.getAnnotation(Audit.class));
						methodAgents.put(methodKey, agent);
						break interfaces;
					}
				}
			}
		}

		// if (null != method) {
		// BUSINESS_METHOD_CACHE.put(methodKey, method);//commented at 2013.8.20
		// PARAMETER_TYPES.put(methodKey, types);// added at 2013.8.12//commented at 2013.8.20
		// } else {
		if (method == null) {
			log.error("Fail to get method: {}, request string: \n{}", methodKey, requestObj.toString());
			throw new DispatcherException("Incorrect method invoked: " + methodKey);
		}

	}

	protected Object doJob(ClientRequestObject requestObj, ClientResponseObject responseObj) throws DispatcherException {
		MethodAgent agent = methodAgents.get(requestObj.getKey());// added at 2013.8.20
		Method method = agent.getMethod();// BUSINESS_METHOD_CACHE.get(requestObj.getKey());// commented at 2013.8.20
		Class<?>[] parameterTypes = agent.getParameterTypes();// PARAMETER_TYPES.get(requestObj.getKey());//
																// method.getParameterTypes();
		int requestParametersCount = 0;
		if (requestObj.getParameters() != null) {
			requestParametersCount = requestObj.getParameters().length;
		}
		if (parameterTypes.length > requestParametersCount) {
			log.error("Missing invoke parameters, need {}, get {}. Request: {}  ", new Object[] {
					parameterTypes.length, requestObj.getParameters().length, requestObj.toString() });
			throw new DispatcherException("Missing invoke parameters, need " + parameterTypes.length + ", get "
					+ requestObj.getParameters().length);
		}
		for (int i = 0; i < parameterTypes.length; i++) {
			Object obj = requestObj.getParameters()[i];
			if (obj != null) {
				Class<?> paraClass = parameterTypes[i];
				if (obj.getClass() != paraClass && !(paraClass.isInstance(obj))) {
					try {
						requestObj.setParameter(StrUtil.fromJson(obj.toString(), paraClass), i);
					} catch (UtilityException e) {
						log.error(
								"Wrong invoke parameter type at {}[0-based] to {}, supposed type is {}, request value is {} ",
								new Object[] { i, requestObj.getKey(), paraClass.getSimpleName(), obj });
						throw new DispatcherException("Wrong inovke parameter at " + i + " with: "
								+ requestObj.toString());
					}
				}
			}
		}
		// NOTE: optimize SessionManager to move following code to there
		boolean needAudit = auditEnabled && agent.isAudit();
		AuditLog auditLog = null;
		if (needAudit == true) {
			Audit audit = agent.getAudit();
			auditLog = new AuditLog();
			auditLog.setService(requestObj.getManagerName());
			auditLog.setMethod(requestObj.getMethodName());
			auditLog.setOperationTime(new Date());
			auditLog.setStatus(0);
			auditLog.setAudit(audit, requestObj.getParameters());
			UserSession session = SessionManager.getUserSession();
			if (session != null) {
				Map<?, ?> map = session.getSessionData();
				if (map != null) {
					auditLog.setUserName(map.get("userName").toString());
					auditLog.setUserCode(map.get("userCode").toString());
				}
			}
		}
		Object resultValue = null;
		try {
			resultValue = method.invoke(BUSINESS_INSTANCE_CACHE.get(requestObj.getManagerName()),
					requestObj.getParameters());
		} catch (Exception e) {
			if (e.getCause() instanceof ValidationException) {
				responseObj.setResult(true);
				responseObj.setMessage(ValidationException.VALIDATE_MESSAGE_PREFIX + e.getCause().getMessage());
			} else {
				Throwable t = e;
				if (e instanceof InvocationTargetException) {
					t = e.getCause();
				}
				log.error("Exeption in invoke [{}]: {} ", requestObj.getKey(), t.getMessage());
				e.printStackTrace();
				String message;
				if (t instanceof PMSManagerException) {
					message = t.getMessage();
				} else {
					message = "Exception in invoke " + requestObj.getKey() + ", reason: " + t.getMessage();
				}
				if (needAudit == true) {
					auditLog.setStatus(1);
					auditLog.setException(message);
				}
				throw new DispatcherException(message);
			}
		} finally {
			if (needAudit == true) {
				auditLogManager.save(auditLog);
			}
		}
		return resultValue;
	}

	/**
	 * Do jobs after invoke the requested method.
	 * 
	 * @param resultValue
	 * @param responseObj
	 * @param requestObj
	 * @throws DispatcherException
	 */
	protected void afterInvoke(Object resultValue, ClientResponseObject responseObj, HttpServletRequest request,
			ClientRequestObject requestObj) throws DispatcherException {
		String str;
		if (requestObj.getManagerName().toLowerCase().indexOf("treemanager") != -1) {
			str = StrUtil.toJsonWithGson(resultValue);
		} else {
			str = StrUtil.toJson(resultValue);
		}
		responseObj.setResult(true);
		responseObj.setData(str);
	}

	/**
	 * Response to the request.
	 * lty 363
	 * @param responseObj
	 * @param response
	 * @throws IOException
	 */
	protected void doResponse(ClientResponseObject responseObj, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
          
		response.getWriter().write(responseObj.toJSON());
		
		System.out.println("***********************************************");
		System.out.println();
		System.out.println(responseObj.toJSON());
		System.out.println();
		System.out.println("***********************************************");
		
	}

	@Override
	public void init() throws ServletException {
		super.init();
		String audit = super.getServletConfig().getInitParameter("audit");
		if ("true".equalsIgnoreCase(audit) || "on".equalsIgnoreCase(audit) || "yes".equalsIgnoreCase(audit)) {
			auditEnabled = true;
			auditLogManager = SpringHelper.getManagerByClass("auditLogManager");
		}
	}

	@Override
	public void destroy() {
		if (auditEnabled == true) {
			// NOTE: maybe we should log the audit start&end events.
			// Can do this in the specific manager
		}
		super.destroy();
	}
}

class MethodAgent {
	private Method method;
	private Class<?>[] parameterTypes;
	private Audit audit;
	private boolean isAudit;

	public MethodAgent(Method method) {
		super();
		this.method = method;
	}

	public Method getMethod() {
		return method;
	}

	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	public Audit getAudit() {
		return audit;
	}

	public void setAudit(Audit audit) {
		this.audit = audit;
		this.isAudit = (this.audit != null);
	}

	public boolean isAudit() {
		return this.isAudit;
	}
}
