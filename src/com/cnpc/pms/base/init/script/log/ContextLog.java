package com.cnpc.pms.base.init.script.log;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * Context Log class. General purpose:
 * <ol>
 * <li>
 * Offer file log for data init/autogen log file.</li>
 * <li>
 * Get EditScriptContext context log result for the web page.</li>
 * </ol>
 * 
 * 
 * Copyright(c) 2010 IBM Corporation, http://www.ibm.com
 * 
 * @author lushu
 * @since Mar 11, 2013
 */
public class ContextLog implements IContextLog {

	private Writer wrapped;
	private PrintWriter log;

	private ContextLog(Writer writer) {
		this.wrapped = writer;
		this.log = new PrintWriter(this.wrapped, true);
	}

	public String getLogContent() {
		return wrapped.toString();
	}

	public void appendLog(String s) {
		log.println(s);
	}

	public static IContextLog getMemoryLog() {
		ContextLog contextLog = new ContextLog(new CharArrayWriter());
		return contextLog;
	}

	public static IContextLog getFileLog(String fileName) throws IOException {
		File file = new File(fileName);
		FileWriter txt = new FileWriter(file);
		ContextLog contextLog = new ContextLog(txt);
		return contextLog;
	}

	public static IContextLog getVoidLog() {
		return VoidContextLog;
	}
}