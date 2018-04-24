package com.cnpc.pms.toolkit.manager;

import com.cnpc.pms.base.manager.IManager;

/**
 * 通用顺序号生成管理类
 * 
 * @author GuoJian
 */
public interface GeneralSequenceNoManager extends IManager {
	/**
	 * 得到顺序号（Long）
	 * 
	 * @param key
	 *            顺序号主键
	 * @param stepSize
	 *            顺序号步长
	 * @return Long 生成的顺序号
	 */
	public Long getSequenceNo(String key, Integer stepSize) throws Exception;

	/**
	 * 得到顺序号 根据指定的长度格式化序号（前补0）
	 * 
	 * @param key
	 *            顺序号主键
	 * @param stepSize
	 *            顺序号步长
	 * @param codeLength
	 *            顺序号的长度
	 * @return String 生成的顺序号
	 */
	public String getSequenceNoStr(String key, Integer stepSize,
			Integer codeLength) throws Exception;

	/**
	 * 得到顺序号（Long） 默认步长为 1
	 * 
	 * @param key
	 *            顺序号主键
	 * @return Long 生成的顺序号
	 */
	public Long getDefSequenceNo(String key) throws Exception;

	/**
	 * 生成顺序号 默认步长为1 根据指定的长度对序号进行格式化（前补0）
	 * 
	 * @param key
	 *            顺序号主键
	 * @param codeLength
	 *            顺序号的长度
	 * @return String 生成的顺序号
	 */
	public String getDefSequenceNoStr(String key, Integer codeLength)
			throws Exception;

}
