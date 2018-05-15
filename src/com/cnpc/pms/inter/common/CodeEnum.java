package com.cnpc.pms.inter.common;

public enum CodeEnum {
	
	// 用户相关---------------------------------------------------------------------
	           /**注册**/
	registerSuccess(1000000, "注册成功"),
	//nickNameExists(1000001, "昵称已存在"),
	telephoneExists(1000002, "电话号码已存在"),
	nickNameAndtelephoneExists(1000003, "昵称或电话号码已存在"),
	telePhoneNotExist(1000004, "电话号码未注册"),  
	telePhoneExist(1000006, "电话号码已注册"),
	//onlyEnCode(1000007, "昵称不允许中文"),
	//huanxinErr(1000010, "环信注册发生错误"),
	passwordToshort(1000012, "密码过短"),
	    
	           /**登陆**/
	illegalPhone(1000005, "非法手机号"),
	passwordErr(1000008, "密码错误"),
	userErr(1000099,"用户名或者密码错误"),
	userIllegal(1000011, "操作用户非法"),
	StoreNotHave(1000012, "用户缺少门店，请联系管理员"),

	
	// 短信相关---------------------------------------------------------------------
	/** 短信验证码错误(8) */
	identifyingCodeError(2000001, "短信验证码错误"),
	/** CCP信息发送失败 */
	CCPSendFailure(2000002, "CCP信息发送失败"),

	
	
	// 消息相关--------------------------------------------------------------------
	MessageGetNull(5000001, "不能重复关注该用户"),

	// 地址相关--------------------------------------------------------------------
	AddressGetNull(6000001, "没有地址信息"),
	AddressUpdateSuccess(889988,"地址修改成功"),
	
	// 活动相关--------------------------------------------------------------------
	ActivityGetNull(7000001, "没有活动信息"),  //用于healthcare报名及其相关活动

	
	
	// 系统相关---------------------------------------------------------------------
	/** 请求成功(1) */
	success(200, "请求成功"),
	/** 请求失败(0) */
	error(9000001, "请求失败"),
	/** 参数错误(2) */
	paramErr(9000002, "参数错误"),
	/** 空数据(3) */
	nullData(9000003, "空数据"),
	/**错误数据**/
	repeatData(9000033, "数据重复"),
	/** 未知错误(30) */
	unknow(9000004, "服务端发生错误， 请联系管理员"),
	
	/** 包含敏感词汇，请更正(70) healthcare eldercare 暂时不做敏感判断**/
	hasSensitiveWord(9000006, "包含敏感词汇，请更正"),
	//NoPaymentType(9000007, "暂不支持该支付方式"),

	/** token为空或错误(999999) */
	tokenErr(9999999, "请重新登陆"),

	folderErr(3000001,"该编码的文件夹已存在"),
	
	dataExist(111111,"数据已存在");
   

	/**
	 * 值 Integer型
	 */
	private final Integer value;
	/**
	 * 描述 String型
	 */
	private final String description;

	CodeEnum(Integer value, String description) {
		this.value = value;
		this.description = description;
	}

	/**
	 * 获取值
	 * 
	 * @return value
	 */
	public Integer getValue() {
		return value;
	}

	/**
	 * 获取描述信息
	 * 
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	public static CodeEnum getStatusState(Integer value) {
		if (null == value)
			return null;
		for (CodeEnum _enum : CodeEnum.values()) {
			if (value.equals(_enum.getValue()))
				return _enum;
		}
		return null;
	}

	public static CodeEnum getStatusState(String description) {
		if (null == description)
			return null;
		for (CodeEnum _enum : CodeEnum.values()) {
			if (description.equals(_enum.getDescription()))
				return _enum;
		}
		return null;
	}

}