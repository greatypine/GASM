package com.cnpc.pms.base.init.script.log;

/**
 * 
 * 
 * Copyright(c) 2010 IBM Corporation, http://www.ibm.com
 * 
 * @author lushu
 * @since Mar 11, 2013
 */
public interface IScriptLog {
	void debug(String msg, Object... objects);

	void info(String msg, Object... objects);

	void warn(String msg, Object... objects);

	void error(String msg, Object... objects);

	String getLogContent();
}
