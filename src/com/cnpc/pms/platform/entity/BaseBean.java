package com.cnpc.pms.platform.entity;


import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


public class BaseBean  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 450641423308011628L;
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}
}
