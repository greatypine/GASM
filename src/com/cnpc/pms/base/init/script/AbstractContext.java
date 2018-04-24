package com.cnpc.pms.base.init.script;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnpc.pms.base.init.script.log.IContextLog;
import com.cnpc.pms.base.util.StrUtil;

/**
 * 
 * 
 * Copyright(c) 2010 IBM Corporation, http://www.ibm.com
 * 
 * @author lushu
 * @since Dec 7, 2011
 */
public abstract class AbstractContext implements IScriptContext {

	public static final Integer BATCH_SIZE = 100;

	public static final Map<String, String> REPLACE_CHARACTER = new HashMap<String, String>();
	static {
		REPLACE_CHARACTER.put("\\\\t", "\t");
	}

	protected Logger log = LoggerFactory.getLogger(getClass());

	protected String script;
	protected String description;
	protected List<?> records;
	protected Context context;
	protected ScriptableObject shell;
	protected int batchSize;
	protected String injectBeans;
	protected String delimiter;
	protected boolean commitImmediately = false;
	protected boolean error = false;
	protected String[] titles;
	protected IContextLog contextLog;

	public String getScript() {
		return this.script;
	}

	public List<?> getRecords() {
		return this.records;
	}

	public void setHead(String head) {
		if (StrUtil.isNotBlank(head)) {
			titles = StrUtil.splitToEnd(head, this.delimiter);
		}
	}

	public void initContext() {
		context = Context.enter();
		shell = new EntityAgent();
		context.initStandardObjects(shell);
		((EntityAgent) shell).importBean(injectBeans);
		addSpecialJSObject();
	}

	protected void addSpecialJSObject() {
	}

	public void appendLog(String s) {
		contextLog.appendLog(s);
	}

	public int getBatchSize() {
		return this.batchSize;
	}

	public boolean isCommitImmediately() {
		return this.commitImmediately;
	}

	public boolean isAtomic() {
		return false;
	}

	public Object execute(Object noUse) {
		return context.evaluateString(shell, script, description, 1, null);
	}

	public boolean hasError() {
		return this.error;
	}

	protected void setDelimiter(String delimiter) {
		if (delimiter != null) {
			for (Map.Entry<String, String> entry : REPLACE_CHARACTER.entrySet()) {
				delimiter = delimiter.replaceAll(entry.getKey(), entry.getValue());
			}
		}
		this.delimiter = delimiter;
	}

	protected NativeObject getMappedRecord(String s) {
		if (titles == null) {
			return null;
		} else {
			NativeObject obj = new NativeObject();
			String[] line = StrUtil.splitToEnd(s, this.delimiter);
			for (int i = 0; i < titles.length; i++) {
				obj.defineProperty(titles[i], line[i], NativeObject.READONLY);
			}
			return obj;
		}
	}

	@Deprecated
	public String getHead() {
		return null;
	}

	@Deprecated
	public Context getContext() {
		return this.context;
	}

	@Deprecated
	public ScriptableObject getShell() {
		return this.shell;
	}

	@Deprecated
	public String getDelimiter() {
		return this.delimiter;
	}

	@Deprecated
	public void flushLog() {
		// resultLog.flush();
	}
}
