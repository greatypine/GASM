package com.cnpc.pms.bid.dao.impl;

import org.hibernate.SQLQuery;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.bid.dao.BaseFileDAO;
import com.cnpc.pms.bid.manager.dto.BaseFileDTO;

public class BaseFileDAOImpl extends BaseDAOHibernate implements BaseFileDAO {

	public void addBaseFile(BaseFileDTO baseFileDTO) {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("INSERT INTO TS_BASE_FILE");
		sqlStr.append("  (ID, VERSION, FILEPATH, FILESIZE, FILESUFFIX, LASTUPLOADED, NAME)");
		sqlStr.append("VALUES");
		sqlStr.append("  (?, ?, ?, ?, ?, ?, ?)");
		SQLQuery toDoQuery = this.getSession().createSQLQuery(sqlStr.toString());
		toDoQuery.setString(0, baseFileDTO.getId());
		toDoQuery.setInteger(1, 0);
		toDoQuery.setString(2, baseFileDTO.getFilePath());
		toDoQuery.setLong(3, baseFileDTO.getFileSize());
		toDoQuery.setString(4, baseFileDTO.getFileSuffix());
		toDoQuery.setDate(5, baseFileDTO.getLastUploaded());
		toDoQuery.setString(6, baseFileDTO.getName());
//		toDoQuery.setLong(8, baseFileDTO.getBusinessId());
		toDoQuery.executeUpdate();
	}

}
