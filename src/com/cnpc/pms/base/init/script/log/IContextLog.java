package com.cnpc.pms.base.init.script.log;

/**
 * 
 * 
 * Copyright(c) 2010 IBM Corporation, http://www.ibm.com
 * 
 * @author lushu
 * @since Mar 11, 2013
 */
public interface IContextLog {
	void appendLog(String s);

	String getLogContent();

	public static IContextLog VoidContextLog = new IContextLog() {
		public void appendLog(String s) {
		}

		public String getLogContent() {
			return null;
		}
	};
}
