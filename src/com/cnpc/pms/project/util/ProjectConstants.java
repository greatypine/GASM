package com.cnpc.pms.project.util;

/**
 * 常量类
 * 
 * @author ws
 */
public class ProjectConstants {

	/** 所长岗位 */
	public static String GW_SUOZHANG = "suozhang";

	/** 副所长岗位 */
	public static String GW_FUSUOZHANG = "fusuozhang";

	/** 科研所长岗位 */
	public static String GW_KYSZ = "KYSZ";

	/** 业务待办moduleCode院级考评下达 */
	public static String YW_WF_YUAN_PROCEDURE_EVALUATE_MODULECODE = "procedureEvaluateYUAN";

	/** 业务待办moduleCode所级考评下达 */
	public static String YW_WF_SUO_PROCEDURE_EVALUATE_MODULECODE = "procedureEvaluateSUO";

	/** 业务待办moduleCode院级考评材料上报 */
	public static String YW_WF_YUAN_PROCEDURE_EVALUATE_REPORT_MODULECODE = "procedureEvaluateReportYUAN";

	/** 业务待办moduleCode所级考评材料上报 */
	public static String YW_WF_SUO_PROCEDURE_EVALUATE_REPORT_MODULECODE = "procedureEvaluateReportSUO";

	/** 业务待办moduleCode院级验收材料上报 */
	public static String YW_WF_YUAN_PROCEDURE_ACCEPTANCE_REPORT = "procedureAcceptanceReportYUAN";

	/** 业务待办moduleCode所级验收材料上报 */
	public static String YW_WF_SUO_PROCEDURE_ACCEPTANCE_REPORT = "procedureAcceptanceReportSUO";

	/** 工作流moduleCode院级考评验收 */
	public static String WF_YUAN_PROCEDURE_EVALUATE_ACCEPTANCE_MODULECODE = "procedureEvaluateAcceptanceYUAN";

	/** 工作流moduleCode所级考评验收 */
	public static String WF_SUO_PROCEDURE_EVALUATE_ACCEPTANCE_MODULECODE = "procedureEvaluateAcceptanceSUO";

	/** 工作流moduleCode院级考评验收结果 */
	public static String WF_PROCEDURECHECK_END = "procedureCheckEnd";

	/** 科研处下达 */
	public static int PROCEDURE_STATUS_KYCSEND = 1;

	/** 所长下达 */
	public static int PROCEDURE_STATUS_SZSEND = 2;

	/** 审批中 */
	public static int PROCEDURE_STATUS_SUBMIT = 3;

	/** 审批退回 */
	public static int PROCEDURE_STATUS_BACK = 4;

	/** 审批通过 */
	public static int PROCEDURE_STATUS_PASS = 5;

	/** 已上报 */
	public static int PROCEDURE_STATUS_REPORTED = 6;

	/** 报告附件 */
	public static String ATTACHMENT_TYPE_REPORTATTACHMENT = "reportAttachment";

	/** 评审附件 */
	public static String ATTACHMENT_TYPE_OPINIONATTACHMENT = "opinionAttachment";

	/** 考评等级：0，院级 */
	public static int PROCEDURE_CHECK_GRADE_YUAN = 0;

	/** 考评等级：1，所级 */
	public static int PROCEDURE_CHECK_GRADE_SUO = 1;

	/** 考评验收状态: 0,考评 */
	public static int PROCEDURE_CHECK_STATUS_KAOPIN = 0;

	/** 考评验收状态: 1,中期考评 */
	public static int PROCEDURE_CHECK_STATUS_ZHONGQIKAOPIN = 1;

	/** 考评验收状态: 2,验收 */
	public static int PROCEDURE_CHECK_STATUS_YANSHOU = 2;

	/**
	 * 计划任务书状态
	 */
	public static int PROPOSAL_STATUS_MOD = 1; // 可修改
	public static int PROPOSAL_STATUS_READ_ = 0; // 不可修改

	/**
	 * 计划任务书签定状态
	 */
	public static int PROPOSAL_EDITION_INNER = 0; // 院内版本
	public static int PROPOSAL_EDITION_SIGN = 1; // 签定版本
	public static int PROPOSAL_EDITION_CHANGE = 3; // 变更版本

	/** 业务待办moduleCode：项目计划下达 */
	public static String YW_WF_PROJECT_FORECAST_SEND = "projectForecastSend";

	/** 业务待办moduleCode：项目分配通知 */
	public static String YW_WF_PROJECT_ASSIGNMENT = "projectAssignment";

	/**
	 * 计划任务书表格类型
	 */
	public static int PROPOSAL_TABLE_TYPE_JDJH = 1; // 研究进度计划
	public static int PROPOSAL_TABLE_TYPE_ZJF = 2; // 总经费
	public static int PROPOSAL_TABLE_TYPE_XDFFYHBKAP = 3; // 任务下达方费用化拨款安排
	public static int PROPOSAL_TABLE_TYPE_CDFZBHBKAP = 4; // 承担方资本化拨款安排
	public static int PROPOSAL_TABLE_TYPE_CDFZCJFAP = 5; // 任务承担方自筹经费安排
	public static int PROPOSAL_TABLE_TYPE_BKJFYS = 6; // 拨款经费预算
	public static int PROPOSAL_TABLE_TYPE_RYMD = 7; // 项目（课题）组人员名单
	public static int PROPOSAL_TABLE_TYPE_XMFYGC = 8; // 项目费用构成

	/**
	 * 项目立项信息状态：草稿
	 */
	public static final int PROJECT_INFO_STATUS_SAVE = 0;
	/**
	 * 项目立项信息状态：审批中
	 */
	public static final int PROJECT_INFO_STATUS_SUBMIT = 1;
	/**
	 * 项目立项信息状态：审批退回
	 */
	public static final int PROJECT_INFO_STATUS_BACK = 2;

	/**
	 * 项目立项信息状态：审批通过
	 */
	public static final int PROJECT_INFO_STATUS_PASS = 3;

	/**
	 * 项目立项信息状态：已签定
	 */
	public static final int PROJECT_INFO_STATUS_SIGN = 4;

	/**
	 * 签定计划任务书审核状态
	 */
	public static final int INFO_APPRO_STATUS_NOPASS = 0; // 不通过
	public static final int INFO_APPRO_STATUS_PASS = 1; // 通过
	public static final int INFO_APPRO_STATUS_WAIT = 2; // 待审核

	/**
	 * 签定任务书状态：已签订
	 */
	public static final int PROJECT_INFO_STATUS_SIGNED = 10;

	/**
	 * 签定任务书状态：待审核
	 */
	public static final int PROJECT_INFO_STATUS_APPRO = 9;

	/**
	 * 签订任务书流程模块编号
	 */
	public static final String PROJECT_INFO_MODULECODE = "projectInfo";

	/**
	 * 变更任务书流程模块编号
	 */
	public static final String PROJECT_INFO_CHANGE_MODULECODE = "projectInfoChange";

	/**
	 * 审核任务书业务待办模块编号
	 */
	public static final String PROJECT_INFO_APPROVAL_MODULECODE = "projectInfoApproval";
	/**
	 * 待审核计划任务书业务待办模块编号
	 */
	public static final String PROJECT_INFO_WAIT_MODULECODE = "projectInfoApprovalWait";

	/** 科技资料管理数据权限key */
	public static final String PROJECT_RESULTS_DATA_PERMISSIONS = "(@forecast_orgCode# or @createuserid# or @copyRecord-projectForecastId#)";
}
