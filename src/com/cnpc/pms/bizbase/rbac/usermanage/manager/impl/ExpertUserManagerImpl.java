/**  
 * @Title: ExpertUserManagerImpl.java
 * @Package com.cnpc.pms.bizbase.rbac.usermanage.manager.impl
 * @Description: TODO(专家manager实现)
 * @author zhaobinbin
 * @date 2013-9-9 下午03:55:44
 */
package com.cnpc.pms.bizbase.rbac.usermanage.manager.impl;

import java.util.List;

import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import org.springframework.beans.BeanUtils;

import com.cnpc.pms.base.entity.PMSAuditEntity;
import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.ExpertUserDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.ExpertUser;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.ExpertUserManager;

public class ExpertUserManagerImpl extends BizBaseCommonManager implements ExpertUserManager {

	public ExpertUserDTO saveExpertUser(ExpertUserDTO obj) {
		ExpertUser entity = null;
		if (obj.getId() != null && 0 != obj.getId()) {
			entity = (ExpertUser) getObject(obj.getId());
			BeanUtils.copyProperties(obj, entity);
		} else {
			entity = new ExpertUser();
			BeanUtils.copyProperties(obj, entity);
		}
		this.preSaveObject(entity);
		saveObject(entity);
		BeanUtils.copyProperties(entity, obj);
		return obj;
	}

	public ExpertUserDTO excuteExpertUser(Long id) {
		ExpertUser ent = (ExpertUser) getObject(id);
		ExpertUserDTO dto = new ExpertUserDTO();
		BeanUtils.copyProperties(ent, dto);
		return dto;
	}

	public List<ExpertUser> excuteExpertUsers(String ids) {
		FSP fsp = new FSP();
		fsp.setUserFilter(FilterFactory.getSimpleFilter(" id in ("+ids+")"));
		List<ExpertUser> ent = (List<ExpertUser>) this.getObjects(fsp);
		return ent;
	}

}
