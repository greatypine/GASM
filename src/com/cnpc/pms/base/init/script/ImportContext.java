package com.cnpc.pms.base.init.script;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;

import com.cnpc.pms.base.init.model.PMSInit;
import com.cnpc.pms.base.util.StrUtil;

/**
 * 
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author lushu
 * @since Aug 9, 2011
 */
public class ImportContext extends AutoGenerateContext {

	public static final String DEFAULT_DELIMITER = ",";

	// In script+data mode, the data file can contains some detail codes.
	// These codes will be stored at addedScript and added to the script
	private boolean singleMode = true;
	private String addedScript;

	public ImportContext(PMSInit init) {
		createFromPMSInit(init);// See AutoGenerateContext

		this.setDelimiter(init.getDelimiter());
		String resourceLocation;
		if (init.getData().size() == 0) {
			resourceLocation = init.getResource();
		} else {
			resourceLocation = init.getScript();
			singleMode = false;
		}

		if (StrUtil.isBlank(resourceLocation) == true) {
			error = true;
			log.error("Fail to find the script:{}", init.getDescription());
		} else {
			InitResource resource = new InitResource(resourceLocation, init.isMapped());
			script = resource.getScript();
			if (singleMode == true) {
				load(resource);
			}
		}

	}

	protected ImportContext() {
	}

	public void load(InitResource resource) {
		addedScript = resource.getScript();
		records = resource.getRecords();
		contextLog = resource.getContentLog();
		error = error || resource.hasError();
		this.setHead(resource.getHead());
	}

	public ImportContext cloneSciprt() {
		ImportContext context = new ImportContext();
		copyProperties(context);// See AutoGenerateContext
		context.delimiter = this.delimiter;
		context.singleMode = this.singleMode;
		context.error = this.error;
		return context;
	}

	@Override
	public String getScript() {
		if (singleMode || StrUtil.isBlank(this.addedScript)) {
			return this.script;
		} else {
			return this.addedScript + this.script;
		}
	}

	@Override
	protected void setDelimiter(String delimiter) {
		if (StrUtil.isEmpty(delimiter)) {
			this.delimiter = DEFAULT_DELIMITER;
		} else {
			super.setDelimiter(delimiter);
		}
	}

	@Override
	protected void addSpecialJSObject() {
		Object wrappedOut = Context.javaToJS(log, shell);
		ScriptableObject.putProperty(shell, "log", wrappedOut);
	}

	@Override
	public Object execute(Object o) {
		String s = (String) o;
		String[] line = StrUtil.splitToEnd(s, this.delimiter);
		shell.defineProperty("r", line, ScriptableObject.DONTENUM);
		NativeObject nobj = this.getMappedRecord(s);
		if (null != nobj) {
			shell.defineProperty("m", nobj, ScriptableObject.DONTENUM);
		}
		shell.defineProperty("_s", s, ScriptableObject.DONTENUM);
		return super.execute(null);
	}
}
