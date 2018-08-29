/**
 * gaobaolei
 */
package com.cnpc.pms.mongodb.common;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gaobaolei
 *
 */
public class MongoDbUtil {

	private MongoDatabase database;
	
	private MongoClient mongoClient;

	private String userName;

	private String password;

	private String host1;

	private String host2;

	private String host3;
	
	
	
	public String getUserName() {
		return userName;
	}



	public void setUserName(String userName) {
		this.userName = userName;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getHost1() {
		return host1;
	}



	public void setHost1(String host1) {
		this.host1 = host1;
	}



	public String getHost2() {
		return host2;
	}



	public void setHost2(String host2) {
		this.host2 = host2;
	}



	public String getHost3() {
		return host3;
	}



	public void setHost3(String host3) {
		this.host3 = host3;
	}

	
	
	
	public MongoDatabase getDatabase() {
		return database;
	}



	public MongoClient getMongoClient() {
		return mongoClient;
	}


	

	public void setDatabase(MongoDatabase database) {
		this.database = database;
	}



	public void setMongoClient(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}



	public void connectMongo(){
//		 try{
//			 System.out.println("-------------------------"+userName+"::::::::::::::::::"+password+"---------------------------------------------------------");
//			 StringBuilder urlSb = new StringBuilder("mongodb://");
//			 if(userName!=null&&!"".equals(userName)&&password!=null&&!"".equals(password)){
//				 urlSb.append(userName).append(":").append(password).append("@");
//			 }
//
//			 //urlSb.append(host1).append("/gemini?safe=true;socketTimeoutMS=150000");
//			 urlSb.append(host1).append(",").append(host2).append(",").append(host3).append("/gemini?safe=true;socketTimeoutMS=150000");
//			 System.out.println("-------------------------"+urlSb+"---------------------------------------------------------");
//		       // 连接到 mongodb 服务
//			     mongoClient =  new MongoClient(new MongoClientURI(urlSb.toString()));
//
//		     }catch(Exception e){
//		    	  mongoClient.close();
//		     }
//
//	         database = mongoClient.getDatabase("gemini");



		ServerAddress sa = new ServerAddress(host1, 27017);

		ServerAddress sa1 = new ServerAddress(host2, 27017);

		ServerAddress sa2 = new ServerAddress(host3, 27017);

		List<ServerAddress> sends = new ArrayList<ServerAddress>();

		sends.add(sa);

		sends.add(sa1);

		sends.add(sa2);

		List<MongoCredential> mongoCredentialList = new ArrayList<MongoCredential>();

		mongoCredentialList.add(MongoCredential.createScramSha1Credential(userName, "gemini",password.toCharArray()));

		database = new MongoClient(sends,mongoCredentialList).getDatabase("gemini");
	}
	
	
	
}
