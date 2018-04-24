/**
 * gaobaolei
 */
package com.cnpc.pms.messageModel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.entity.PMSEntity;

/**
 * @author gaobaolei
 * 消息回调字典(需预制)
 */
@Entity
@Table(name="dict_messageCallback")
public class DictMessageCallBack extends PMSEntity{
	
	/**
	 * 回调标识
	 */
	@Column(name="type")
	private String type;
	
	/**
	 * 回调名称
	 */
	@Column(name="name")
	private String name;

	/**
	 * 回调接口
	 */
	@Column(name="intefaceName")
	private String intefaceName;
	
	/**
	 * 回调方法
	 */
	@Column(name="methodName")
	private String methodName;
	
}
