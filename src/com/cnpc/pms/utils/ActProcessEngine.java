package com.cnpc.pms.utils;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

public class ActProcessEngine {

	private String userName;
	
	private String passWord;
	
	private String host1;
	
	private String dbName;
	
	private ProcessEngine processEngine;
	
	private RepositoryService repositoryService;
	
	private RuntimeService runtimeService;
	
	private TaskService taskService;
	
	private HistoryService historyService;
	
	public void initProcessEngineInstance(){
		try {
			StringBuffer jdbcurl = new StringBuffer("jdbc:mysql://");
			ProcessEngineConfiguration config = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
	        config.setJdbcDriver("com.mysql.jdbc.Driver");
	        jdbcurl.append(host1).append("/").append(dbName).append("?useUnicode=true&amp;characterEncoding=utf8");
	        config.setJdbcUrl(jdbcurl.toString());
	        System.out.println("-----------------------------jdbcurl--------------------------------");
	        System.out.println(jdbcurl);
	        config.setJdbcUsername(userName);
	        config.setJdbcPassword(passWord);
	        config.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
	        processEngine = config.buildProcessEngine();
	        System.out.println("-----------------------------processEngine--------------------------------");
	        System.out.println(processEngine);
	        
	        
	        repositoryService = processEngine.getRepositoryService();
	        runtimeService = processEngine.getRuntimeService();
	        taskService = processEngine.getTaskService();
	        historyService=processEngine.getHistoryService();
	        
	        //repositoryService.createDeployment().addClasspathResource("actconf/oneVacationProcess.bpmn").name("请假流程1").category("请假1").deploy();
	        //repositoryService.createDeployment().addClasspathResource("actconf/threeVacationProcess.bpmn").name("请假流程2").category("请假2").deploy();
	        //repositoryService.createDeployment().addClasspathResource("actconf/moreVacationProcess.bpmn").name("请假流程3").category("请假3").deploy();
	       
	        
	        ///////////////以下代码为测试///////////////
	        /*
			RepositoryService rs = processEngine.getRepositoryService();
			RuntimeService runtimeService = processEngine.getRuntimeService();
			TaskService taskService = processEngine.getTaskService();
			rs.createDeployment().addClasspathResource("actconf/first_bpmn.bpmn").deploy();
			
			ProcessInstance pi = runtimeService.startProcessInstanceByKey("myProcess");
			
			Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
			System.out.println("write: task 1-------------------------"+task.getName());
			taskService.complete(task.getId());
			
			task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
			System.out.println("audit: task 2-------------------------"+task.getName());
			taskService.complete(task.getId());
			
			task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
			System.out.println("result: task result-------------------"+task);
			
			processEngine.close();
			*/
			
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getPassWord() {
		return passWord;
	}


	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}


	public String getHost1() {
		return host1;
	}


	public void setHost1(String host1) {
		this.host1 = host1;
	}


	public ProcessEngine getProcessEngine() {
		return processEngine;
	}


	public void setProcessEngine(ProcessEngine processEngine) {
		this.processEngine = processEngine;
	}


	public String getDbName() {
		return dbName;
	}


	public void setDbName(String dbName) {
		this.dbName = dbName;
	}


	public RepositoryService getRepositoryService() {
		return repositoryService;
	}


	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}


	public RuntimeService getRuntimeService() {
		return runtimeService;
	}


	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}


	public TaskService getTaskService() {
		return taskService;
	}


	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}


	public HistoryService getHistoryService() {
		return historyService;
	}


	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}
	
}
