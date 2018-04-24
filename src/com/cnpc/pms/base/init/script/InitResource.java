package com.cnpc.pms.base.init.script;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.cnpc.pms.base.file.manager.PMSFileManager;
import com.cnpc.pms.base.init.script.log.ContextLog;
import com.cnpc.pms.base.init.script.log.IContextLog;
import com.cnpc.pms.base.util.ConfigurationUtil;

public class InitResource {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private String script;
	private List<String> records;
	private String head;
	private IContextLog contextLog;
	private boolean error = false;

	// private PrintWriter resultLog;

	private InitResource() {
	}

	/**
	 * for AutoGenContext, ExcelImportContext with out mapped
	 * 
	 * @param resourceLocation
	 */
	public InitResource(String resourceLocation) {
		Resource initResource = ConfigurationUtil.getSingleResource(resourceLocation);
		if (initResource != null) {
			InputStream is = null;
			InputStreamReader isr = null;
			BufferedReader reader = null;
			StringBuffer script = new StringBuffer();
			records = new ArrayList<String>();

			try {
				is = initResource.getInputStream();
				isr = new InputStreamReader(is, "UTF-8");
				reader = new BufferedReader(isr);
				String line;
				inner: while ((line = reader.readLine()) != null) {
					if (line.indexOf("#") == 0 || line.trim().length() == 0) {
						continue inner;
					}
					if (line.toLowerCase().equals("<script>")) {
						while ((line = reader.readLine()) != null && line.toLowerCase().equals("</script>") == false) {
							script.append("\n").append(line);
						}
					} else {
						records.add(line);
					}
				}
				this.script = script.toString();
				contextLog = ContextLog.getFileLog(getLogFile(resourceLocation));
			} catch (IOException e) {
				log.error("Error in initialze initContext {}", resourceLocation, e);
				error = true;
			} finally {
				try {
					reader.close();
					isr.close();
					is.close();
				} catch (IOException e) {
				}
			}
		} else {
			log.error("Fail to load init resource at: {}", resourceLocation);
			error = true;
		}

	}

	/**
	 * For ImportContext
	 */
	public InitResource(String resourceLocation, boolean mapped) {
		this(resourceLocation);
		if (mapped == true) {
			head = records.remove(0);
		}
	}

	private String getLogFile(String resourceLocation) throws IOException {
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat();
		format.applyPattern("hh-mm-ss");
		String log = PMSFileManager.FILE_ROOT + "/Import" + resourceLocation.replace('/', '-') + "["
				+ format.format(now) + "].log";
		return log;
	}

	public String getScript() {
		return script;
	}

	public List<String> getRecords() {
		return records;
	}

	public IContextLog getContentLog() {
		return contextLog;
	}

	public boolean hasError() {
		return error;
	}

	public void setRecords(List<String> records) {
		this.records = records;
	}

	public String getHead() {
		return head;
	}

	public InitResource cloneWithoutRecords() {
		InitResource resource = new InitResource();
		resource.script = this.script;
		resource.head = this.head;
		resource.contextLog = this.contextLog;
		resource.error = this.error;
		return resource;
	}
}
