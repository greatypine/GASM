package com.cnpc.pms.utils;

public class OSSConfig {
	 private  String endpoint;       
	    private  String accessKeyId;      
	    private  String accessKeySecret;  
	    private  String bucketName;       
	    private  String picLocation;      
	      
	    public OSSConfig() {  
	        this.endpoint = "oss-cn-beijing.aliyuncs.com";  
			this.bucketName = "guoanshuju";  
			//this.picLocation = "usr/local/";  
			this.accessKeyId = "LTAII1Y7Z7fXhyJF";  
			this.accessKeySecret = "26W34k5jCT4BEmk0RG1lhMTxp9w7Xi";  
	    }  
	  
	    public String getEndpoint() {  
	        return endpoint;  
	    }  
	    public void setEndpoint(String endpoint) {  
	        this.endpoint = endpoint;  
	    }  
	    public String getAccessKeyId() {  
	        return accessKeyId;  
	    }  
	    public void setAccessKeyId(String accessKeyId) {  
	        this.accessKeyId = accessKeyId;  
	    }  
	    public String getAccessKeySecret() {  
	        return accessKeySecret;  
	    }  
	    public void setAccessKeySecret(String accessKeySecret) {  
	        this.accessKeySecret = accessKeySecret;  
	    }  
	    public String getBucketName() {  
	        return bucketName;  
	    }  
	    public void setBucketName(String bucketName) {  
	        this.bucketName = bucketName;  
	    }  
	    public String getPicLocation() {  
	        return picLocation;  
	    }  
	    public void setPicLocation(String picLocation) {  
	        this.picLocation = picLocation;  
	    } 
}
