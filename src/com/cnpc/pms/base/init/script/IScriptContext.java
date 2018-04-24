package com.cnpc.pms.base.init.script;

import java.util.List;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

/**
 * 
 * 
 * Copyright(c) 2010 IBM Corporation, http://www.ibm.com
 * 
 * @author lushu
 * @since Dec 7, 2011
 */
public interface IScriptContext {

	String getScript();

	List<?> getRecords();

	void setHead(String head);

	void initContext();

	Object execute(Object line);

	void appendLog(String s);

	int getBatchSize();

	boolean isCommitImmediately();

	boolean isAtomic();

	// @Deprecated String getInjectBeans();
	// NativeObject getMappedRecord(String s);
	@Deprecated
	void flushLog();

	@Deprecated
	Context getContext();

	@Deprecated
	ScriptableObject getShell();

	@Deprecated
	String getDelimiter();

	@Deprecated
	String getHead();

}
