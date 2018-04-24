package com.cnpc.pms.base.init.manager.impl;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.Resource;

import com.cnpc.pms.base.exception.UtilityException;
import com.cnpc.pms.base.init.dao.InitializeDataDAO;
import com.cnpc.pms.base.init.manager.InitializeDataManager;
import com.cnpc.pms.base.init.model.PMSInit;
import com.cnpc.pms.base.init.model.PMSInitData;
import com.cnpc.pms.base.init.model.PMSInits;
import com.cnpc.pms.base.init.script.AutoGenerateContext;
import com.cnpc.pms.base.init.script.IScriptContext;
import com.cnpc.pms.base.init.script.ImportContext;
import com.cnpc.pms.base.init.script.InitResource;
import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.util.ConfigurationUtil;
import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.StrUtil;

/**
 * Init data.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author lushu
 * @since May 7, 2011
 */
public class InitializeDataManagerImpl extends BaseManagerImpl implements InitializeDataManager {

	private static final String CONFIG_INIT_NAME = "init.file";
	private static final String DEFAULT_INIT_LOCATION = "/conf/initData.xml";

	private static final String CONFIG_AUTO_GEN_NAME = "gen.file";
	private static final String DEFAULT_AUTO_GEN_LOCATION = "/conf/genData.xml";

	public void importData() {
		String initFile = PropertiesUtil.getValue(CONFIG_INIT_NAME);
		if (StringUtils.isEmpty(initFile)) {
			initFile = DEFAULT_INIT_LOCATION;
		}
		Resource resource = ConfigurationUtil.getSingleResource(initFile);
		if (resource != null) {
			try {
				PMSInits inits = (PMSInits) ConfigurationUtil.parseXMLObject(PMSInits.class, resource);
				for (PMSInit init : inits.getInits()) {
					if (init.getResource() != null) {
						packagedImport(init);
					} else {
						if (init.getThread() > 1) {
							ImportResource dataFiles;
							if (init.getSlice() > 0) {
								dataFiles = new RecordPoolResource(init);
								log.info("Start congested multiple threads[{}] import mode.", init.getThread());
							} else {
								dataFiles = new FileListResource(init);
								log.info("Start multiple threads[{}] import mode.", init.getThread());
							}
							multiThreadsImport(init, dataFiles);
						} else {
							log.info("Start single thread import mode.");
							seperateImport(init);
						}
					}
				}
			} catch (UtilityException e) {
				log.warn("Fail to digester the init source from: {}", resource, e);
			}
		} else {
			log.error("Fail to get init file of {}.", initFile);
		}
	}

	/**
	 * Resource Mode
	 * 
	 * @param init
	 */
	private void packagedImport(PMSInit init) {
		String description = init.getDescription();
		String resourceLocation = init.getResource();
		ImportContext context = new ImportContext(init);
		if (context.hasError() == false) {
			String script = context.getScript();
			log.debug("Start to import [{}] with resource: {}", description, resourceLocation);
			if (script.length() == 0) {
				log.error("There is no script defined in the init resource file!");
				return;
			}
			log.debug("script: {}", script);
			((InitializeDataDAO) getDao()).importData(context);
		} else {
			log.error("Fail to load init resource[{}] at: {}", description, resourceLocation);
		}
	}

	/**
	 * Normal one by one mode
	 * 
	 * @param init
	 */
	private void seperateImport(PMSInit init) {
		String description = init.getDescription();
		String scriptFile = init.getScript();
		ImportContext context = new ImportContext(init);
		if (context.hasError() == false) {
			String script = context.getScript();
			log.debug("Start to import [{}] with script: {}", description, scriptFile);
			if (script.length() == 0) {
				log.error("There is no script defined in the init script file!");
				return;
			}
			log.debug("script: {}", script);
			for (PMSInitData data : init.getData()) {
				InitResource dataContext = new InitResource(data.getFile(), init.isMapped());
				if (dataContext.hasError() == false) {
					context.load(dataContext);
					log.debug("Start to import [{}] with data: {}", description, data.getFile());
					((InitializeDataDAO) getDao()).importData(context);
				} else {
					log.error("Fail to load init data [{}] at: {}", description, data.getFile());
				}
			}
		} else {
			log.error("Fail to load init script [{}] at: {}", description, scriptFile);
		}
	}

	/**
	 * Multiple threads mode
	 * 
	 * @param init
	 */
	private void multiThreadsImport(PMSInit init, ImportResource dataFiles) {
		String description = init.getDescription();
		String scriptFile = init.getScript();
		ImportContext context = new ImportContext(init);
		if (context.hasError() == false) {
			String script = context.getScript();
			log.debug("Start to import [{}] with script: {}", description, scriptFile);
			if (script.length() == 0) {
				log.error("There is no script defined in the init script file!");
				return;
			}
			log.debug("script: {}", script);
			// ImportResource dataFiles = new FileListResource(init);
			int threadNum = init.getThread();
			CountDownLatch threadSignal = new CountDownLatch(threadNum);
			for (int i = 0; i < threadNum; i++) {
				Thread thread = new ImportThread(threadSignal, context, dataFiles);
				thread.start();
			}
			try {
				threadSignal.await();
			} catch (InterruptedException e) {
				log.error("Error in stop thread {},", threadSignal, e);
			}

		} else {
			log.error("Fail to load init script [{}] at: {}", description, scriptFile);
		}
	}

	static interface ImportResource {
		InitResource getNext();
	}

	class FileListResource implements ImportResource {
		private final Iterator<PMSInitData> dataList;
		private boolean mapped;

		public FileListResource(PMSInit init) {
			this.dataList = init.getData().iterator();
			this.mapped = init.isMapped();
		}

		public synchronized InitResource getNext() {
			if (dataList.hasNext()) {
				PMSInitData data = dataList.next();
				log.info("Fetch next data file: {} ", data.getFile());
				InitResource dataContext = new InitResource(data.getFile(), this.mapped);
				if (dataContext.hasError() == false) {
					log.debug("Start to import [{}] with data: {}", data.getFile());
					return dataContext;
				} else {
					log.error("Fail to load init data [{}] at: {}", data.getFile());
					return null;
				}
			} else {
				log.info("All data files have been fetched out.");
				return null;
			}
		}
	}

	class RecordPoolResource implements ImportResource {
		private final Iterator<PMSInitData> dataList;
		private PMSInitData data;
		private InitResource dataContext = null;
		private int[] groupIndex;
		private boolean unsetGroupIndex = false;
		private String groupBy;
		private boolean adaptableSlice = false;
		private String delimiter;
		private int index;
		private int slice;
		private int size;
		private boolean mapped;

		public RecordPoolResource(PMSInit init) {
			this.dataList = init.getData().iterator();
			this.slice = init.getSlice();
			this.mapped = init.isMapped();
			this.groupBy = init.getGroupBy();
			if (StrUtil.isNotBlank(this.groupBy)) {
				adaptableSlice = true;
				delimiter = init.getDelimiter();
				if (StrUtil.isEmpty(delimiter)) {
					delimiter = ImportContext.DEFAULT_DELIMITER;
				}
				unsetGroupIndex = true;
				// String[] groups = this.groupBy.split(",");
				// this.groupIndex = new int[groups.length];
				// try {
				// for (int i = 0; i < groups.length; i++) {
				// this.groupIndex[i] = Integer.valueOf(groups[i]);
				// }
				// } catch (NumberFormatException e) {
				// if (mapped == false) {
				// throw e;
				// } else {
				// unsetGroupIndex = true; // determine the index later
				// }
				// }
			}
		}

		private boolean shouldKeepTogether(String[] source, String targetLine) {
			String[] target = targetLine.split(delimiter);
			if (groupIndex[groupIndex.length - 1] >= target.length) {
				throw new RuntimeException("The GroupBy Index if over the length of records: " + targetLine);
			}
			for (int i : groupIndex) {
				if (!source[i].equals(target[i])) {
					return false;
				}
			}
			return true;
		}

		private InitResource getPartialResource() {
			int end = index + slice;
			if (end > size) {
				end = size;
			}
			InitResource resource = dataContext.cloneWithoutRecords();
			if (adaptableSlice) {
				String[] last = dataContext.getRecords().get(end - 1).split(delimiter);
				if (groupIndex[groupIndex.length - 1] >= last.length) {
					throw new RuntimeException("The GroupBy Index if over the length of records: "
							+ Arrays.toString(last));
				}
				while (end < size && shouldKeepTogether(last, dataContext.getRecords().get(end))) {
					end++;
				}
			}
			List<String> subList = dataContext.getRecords().subList(index, end);
			resource.setRecords(subList);
			log.info("Fetch next data[{},{}) from file: {} ", new Object[] { index, end, data.getFile() });
			index = end;
			return resource;
		}

		// private void setMappedGroupIndex(String head) {
		// String[] titles = head.split(delimiter);
		// String[] groups = this.groupBy.split(",");
		// for (int i = 0; i < groups.length; i++) {
		// inner: for (int j = 0; j < titles.length; j++) {
		// if (groups[i].equals(titles[j])) {
		// groupIndex[i] = j;
		// break inner;
		// }
		// }
		// }
		// unsetGroupIndex = false;
		// }

		public synchronized InitResource getNext() {
			if (dataContext != null && index < size) {
				return getPartialResource();
			} else {
				if (dataList.hasNext()) {
					data = dataList.next();
					dataContext = new InitResource(data.getFile(), mapped);
					if (dataContext.hasError() == false) {
						log.debug("Start to import [{}] with data: {}", data.getFile());
						if (unsetGroupIndex == true) {
							// setMappedGroupIndex(dataContext.getHead());
							String[] titles = null;
							if (StrUtil.isNotBlank(dataContext.getHead())) {
								titles = dataContext.getHead().split(delimiter);
							}
							groupIndex = PMSInit.getGroupIndex(groupBy, titles);
							unsetGroupIndex = false;
						}
						size = dataContext.getRecords().size();
						index = 0;
						return getPartialResource();
					} else {
						log.error("Fail to load init data [{}] at: {}", data.getFile());
						return null;
					}
				} else {
					log.info("All data files have been fetched out.");
					return null;
				}
			}
		}
	}

	class ImportThread extends Thread {
		private final CountDownLatch threadsSignal;
		private final ImportContext context;
		private final ImportResource resources;

		public ImportThread(CountDownLatch threadsSignal, ImportContext context, ImportResource resources) {
			this.threadsSignal = threadsSignal;
			this.context = context;
			this.resources = resources;
		}

		@Override
		public void run() {
			log.info("{} Start ...", Thread.currentThread().getName());
			InitResource resource;
			while ((resource = resources.getNext()) != null) {
				ImportContext localContext = context.cloneSciprt();
				localContext.load(resource);
				((InitializeDataDAO) getDao()).importDataInThread(localContext);
			}
			threadsSignal.countDown();
			log.info("{} Stop. There are {} thread(s) left.", Thread.currentThread().getName(),
					threadsSignal.getCount());
		}
	}

	class GenerateThread extends Thread {
		private final CountDownLatch threadsSignal;
		private final AutoGenerateContext context;

		public GenerateThread(CountDownLatch threadsSignal, AutoGenerateContext context) {
			this.threadsSignal = threadsSignal;
			this.context = context.cloneScript();
		}

		@Override
		public void run() {
			log.info("{} Start ...", Thread.currentThread().getName());
			((InitializeDataDAO) getDao()).generateData(context);
			threadsSignal.countDown();
			log.info("{} Stop. There are {} thread(s) left.", Thread.currentThread().getName(),
					threadsSignal.getCount());
		}
	}

	public void generateDate() {
		String genFile = PropertiesUtil.getValue(CONFIG_AUTO_GEN_NAME);
		if (StringUtils.isEmpty(genFile)) {
			genFile = DEFAULT_AUTO_GEN_LOCATION;
		}
		Resource resource = ConfigurationUtil.getSingleResource(genFile);
		if (resource != null) {
			try {
				PMSInits inits = (PMSInits) ConfigurationUtil.parseXMLObject(PMSInits.class, resource);
				for (PMSInit init : inits.getInits()) {
					String description = init.getDescription();
					String resourceLocation = init.getScript();
					AutoGenerateContext context = new AutoGenerateContext(init);
					if (context.hasError() == false) {
						String script = context.getScript();
						log.debug("Start to auto-gen [{}] with resource: {}", description, resourceLocation);
						if (script.length() == 0) {
							log.error("There is no script defined in the auto-generate resource file!");
							return;
						}
						log.debug("script: {}", script);
						int threadNum = init.getThread();
						CountDownLatch threadSignal = new CountDownLatch(threadNum);
						for (int i = 0; i < threadNum; i++) {
							Thread thread = new GenerateThread(threadSignal, context);
							thread.start();
						}
						try {
							threadSignal.await();
						} catch (InterruptedException e) {
							log.error("Error in stop thread {},", threadSignal, e);
						}
					} else {
						log.error("Fail to load auto-generate resource[{}] at: {}", description, resourceLocation);
					}
				}
			} catch (UtilityException e) {
				log.warn("Fail to digester the auto-generate from: {}", resource, e);
			}
		} else {
			log.error("Fail to get auto-generate file of {}.", genFile);
		}
	}

	public void importContext(IScriptContext context) {
		((InitializeDataDAO) getDao()).importData(context);
	}

}
