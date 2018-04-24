package com.cnpc.pms.base.init;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.Dialect;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import org.springframework.util.ClassUtils;

import com.cnpc.pms.base.dict.manager.DictManager;
import com.cnpc.pms.base.exception.DictImportException;
import com.cnpc.pms.base.init.manager.InitializeDataManager;
import com.cnpc.pms.base.util.SpringHelper;

/**
 * <p>
 * <b>包装成java命令可以调度的任务.</b>
 * </p>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author hefei
 * @since 2010-12-10
 */
public class InitializeTask {

	/**
	 * java命令可以调度的任务.
	 * 
	 * @param args
	 *            the arguments
	 * @throws Exception
	 *             the exception
	 */
	public static void main(String[] args) throws Exception {
		SpringHelper.init(new String[] {"classpath*:/conf/appContext.xml" });
		if (args.length == 0) {
			System.err.println("With no initialize type");
		} else {
			String arg = args[0];
			InitializeTask task = new InitializeTask();
			if ("dictionary".equals(arg)) {
				task.initDict();
			} else if ("data".equals(arg)) {
				task.initData();
			} else if ("generate".equals(arg)) {
				task.generateDate();
			} else if ("generateSQL".equals(arg)) {
				task.generateSQL("target");
			} else if ("drop".equals(arg)) {
				task.dropTables();
			} else if ("dropDict".equals(arg)) {
				task.dropDict();
			} else if ("dropAll".equals(arg)) {
				task.dropDict();
				task.dropTables();
			} else if ("update".equals(arg)) {
				// Need do nothing.
			} else {
				System.err.println("Unknowned initialize type: " + arg);
			}
		}
	}

	public void initDict() throws DictImportException, IOException {
		DictManager manager = (DictManager) SpringHelper.getBean("dictManager");
		manager.importDict();
	}

	public void dropDict() throws IOException {
		DictManager manager = (DictManager) SpringHelper.getBean("dictManager");
		manager.dropDictTable();
	}

	public void initData() throws IOException {
		InitializeDataManager manager = (InitializeDataManager) SpringHelper.getBean("initializeDataManager");
		manager.importData();
	}

	public void generateDate() throws IOException {
		InitializeDataManager manager = (InitializeDataManager) SpringHelper.getBean("initializeDataManager");
		manager.generateDate();
	}

	public void generateSQL(String path) throws IOException {
		LocalSessionFactoryBean bean = (LocalSessionFactoryBean) SpringHelper.getBean("&sessionFactory");
		Configuration config = bean.getConfiguration();
		Dialect dialect = Dialect.getDialect(config.getProperties());
		if (path != null) {
			String fileName = ClassUtils.getShortName(dialect.toString());
			File file = new File(path, fileName + ".sql");
			FileWriter outFile = new FileWriter(file);
			BufferedWriter out = new BufferedWriter(outFile);
			for (String s : config.generateSchemaCreationScript(dialect)) {
				out.write(s);
				out.write(";\n\r");
			}
			out.close();
			outFile.close();
		} else {
			System.err.println("No Target Directory.");
		}
	}

	public void dropTables() {
		LocalSessionFactoryBean bean = (LocalSessionFactoryBean) SpringHelper.getBean("&sessionFactory");
		bean.dropDatabaseSchema();
	}
}
