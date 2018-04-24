package com.cnpc.pms.base.init.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetNext;
import org.apache.commons.digester.annotations.rules.SetProperty;

@ObjectCreate(pattern = "inits/init")
public class PMSInit {
	@SetProperty(pattern = "inits/init", attributeName = "script")
	private String script;

	@SetProperty(pattern = "inits/init", attributeName = "resource")
	private String resource;

	@SetProperty(pattern = "inits/init", attributeName = "description")
	private String description;

	@SetProperty(pattern = "inits/init", attributeName = "batchSize")
	private int batchSize;

	@SetProperty(pattern = "inits/init", attributeName = "injectBeans")
	private String injectBeans;

	// how many records should generate
	@SetProperty(pattern = "inits/init", attributeName = "target")
	private int target;

	@SetProperty(pattern = "inits/init", attributeName = "thread")
	private int thread;

	@SetProperty(pattern = "inits/init", attributeName = "slice")
	private int slice;

	@SetProperty(pattern = "inits/init", attributeName = "groupBy")
	private String groupBy;

	@SetProperty(pattern = "inits/init", attributeName = "commitImmediately")
	private boolean commitImmediately;

	@SetProperty(pattern = "inits/init", attributeName = "delimiter")
	private String delimiter;

	@SetProperty(pattern = "inits/init", attributeName = "mapped")
	private boolean mapped;

	private final List<PMSInitData> data = new ArrayList<PMSInitData>();

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public int getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}

	public String getInjectBeans() {
		return injectBeans;
	}

	public void setInjectBeans(String injectBeans) {
		this.injectBeans = injectBeans;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public int getThread() {
		return thread;
	}

	public void setThread(int thread) {
		this.thread = thread;
	}

	public int getSlice() {
		return slice;
	}

	public void setSlice(int slice) {
		this.slice = slice;
	}

	public List<PMSInitData> getData() {
		return data;
	}

	public boolean isCommitImmediately() {
		return commitImmediately;
	}

	public void setCommitImmediately(boolean commitImmediately) {
		this.commitImmediately = commitImmediately;
	}

	@SetNext
	public void addData(PMSInitData data) {
		this.data.add(data);
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	public String getGroupBy() {
		return groupBy;
	}

	public boolean isMapped() {
		return mapped;
	}

	public void setMapped(boolean mapped) {
		this.mapped = mapped;
	}

	/**
	 * Convert groupBy 'keys' into a int array
	 * 
	 * @param groupBy
	 * @param titles
	 * @return
	 */
	public static int[] getGroupIndex(String groupBy, String[] titles) {
		String[] groups = groupBy.split(",");
		int[] groupIndex = new int[groups.length];
		try {
			for (int i = 0; i < groups.length; i++) {
				groupIndex[i] = Integer.valueOf(groups[i]);
			}
		} catch (NumberFormatException e) {
			if (titles == null) {
				throw e;
			} else {
				for (int i = 0; i < groups.length; i++) {
					inner: for (int j = 0; j < titles.length; j++) {
						if (groups[i].equalsIgnoreCase(titles[j])) {
							groupIndex[i] = j;
							break inner;
						}
					}
				}
			}
		}
		return groupIndex;
	}
}
