package com.cnpc.pms.base.dto;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.cnpc.pms.base.entity.IEntity;
import com.cnpc.pms.base.query.model.PMSColumn;
import com.cnpc.pms.base.util.BeanUtil;

/**
 * PMS abstract DTO.<br/>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author He Wang
 * @since 2011/03/30
 */
public abstract class PMSDTO {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 367433714269639049L;

	/**
	 * convert entity to DTO.
	 * 
	 * @param entity
	 * @param commonColumns
	 *            common columns.
	 * @param formatAndTranslateColumns
	 *            the format & dict columns.
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 */
	public void fromEntity(IEntity entity, List<PMSColumn> commonColumns)
		throws Exception {
		if (null != commonColumns) {
			BeanUtil.copyProperties(entity, this, commonColumns);
//		} else if (null == formatAndTranslateColumns) {
//			BeanUtil.copyProperties(this, entity);
//		} else {
//			BeanUtil.fromEntityToDTO(entity, this, commonColumns, formatAndTranslateColumns);
		}
	}
}
