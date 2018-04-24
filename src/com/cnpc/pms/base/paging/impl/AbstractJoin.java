package com.cnpc.pms.base.paging.impl;

import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.paging.IJoin;

public abstract class AbstractJoin implements IJoin {
	private static final long serialVersionUID = 1L;
	/** The next. */
	protected IJoin next;

	public IJoin appendJoin(IJoin join) {
		if (join != null) {
			if (this == join.getNext() || this == join) {
				throw new InvalidFilterException("Duplicated Join appended.");
			}
			if (this.next == null) {
				this.next = join;
			} else {
				this.next.appendJoin(join);
			}
		}
		return this;
	}

	public String getJoinedClasses() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.getSelfJoinClause());
		if (next != null) {
			buf.append(next.getJoinedClasses());
		}
		return buf.toString();
	}

	public IJoin getNext() {
		return next;
	}

	protected abstract String getSelfJoinClause();

	public String getConditions() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.getSelfConditions());
		if (next != null) {
			buf.append(" AND ").append(next.getConditions());
		}
		return buf.toString();
	}

	protected abstract String getSelfConditions();
}
