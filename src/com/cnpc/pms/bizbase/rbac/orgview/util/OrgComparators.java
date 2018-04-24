package com.cnpc.pms.bizbase.rbac.orgview.util;

import java.util.Comparator;

import com.cnpc.pms.bizbase.rbac.orgview.dto.PurStruOrgDTO;

public class OrgComparators implements Comparator<Object> {

	public int compare(Object o1, Object o2) {
		if (o1 instanceof PurStruOrgDTO) {
			return compare((PurStruOrgDTO) o1, (PurStruOrgDTO) o2);
		} else {
			System.err.println("未找到合适的比较器");
			return 1;
		}
	}

	public int compare(PurStruOrgDTO o1, PurStruOrgDTO o2) {
		String code1 = o1.getCode();
		String code2 = o2.getCode();
		return code1.compareTo(code2);

	}
}
