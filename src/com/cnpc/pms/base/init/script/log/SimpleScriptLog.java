package com.cnpc.pms.base.init.script.log;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.io.Writer;

public class SimpleScriptLog implements IScriptLog {

	private final Writer scriptLogWriter;
	private final PrintWriter scriptLog;

	/**
	 * 构造函数
	 */
	public SimpleScriptLog() {
		scriptLogWriter = new CharArrayWriter();
		scriptLog = new PrintWriter(scriptLogWriter, true);
	}

	/**
	 * 获取脚本日志工具输出
	 * 
	 * @return 脚本日志工具输出
	 */
	public String getLogContent() {
		return scriptLogWriter.toString();
	}

	protected String formatMessage(String msg, Object... objects) {
		String result = msg;
		for (Object o : objects) {
			result = result.replaceFirst("\\{\\}", o.toString());
		}
		return result;
	}

	protected void log(String msg, Object... objects) {
		scriptLog.println(formatMessage(msg, objects));
	}

	public void debug(String msg, Object... objects) {
		log("[DEBUG]" + msg, objects);
	}

	public void info(String msg, Object... objects) {
		log("[INFO ]" + msg, objects);
	}

	public void warn(String msg, Object... objects) {
		log("[WARN ]" + msg, objects);
	}

	public void error(String msg, Object... objects) {
		log("[ERROR]" + msg, objects);
	}

}