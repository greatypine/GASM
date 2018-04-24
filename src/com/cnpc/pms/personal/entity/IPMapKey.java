package com.cnpc.pms.personal.entity;

import java.io.Serializable;

public class IPMapKey implements Serializable{
	private static final long serialVersionUID = 1L;
	
	// 主键属性  
    private String customer_id;  
    // 主键属性  
    private String product_id;  
	
    /** 
     * 无参数的public构造方法，必须要有 
     */  
    public IPMapKey() {  
          
    }  
    /** 
     * 重写了一个带参数的构造方法 
     */  
    public IPMapKey(String customer_id, String product_id) {  
    	 this.customer_id = customer_id;  
         this.product_id = product_id;  
    }
    
    
    /** 
     * 覆盖hashCode方法，必须要有 
     */  
    @Override  
    public int hashCode() {  
        final int PRIME = 31;  
        int result = 1;  
        result = PRIME * result + (customer_id == null ? 0 : customer_id.hashCode());  
        result = PRIME * result + (product_id == null ? 0 : product_id.hashCode());  
        return result;  
    }
    
    
    /** 
     * 覆盖equals方法，必须要有 
     */  
    @Override  
    public boolean equals(Object obj) {  
        if(this == obj) return true;  
        if(obj == null) return false;  
        if(!(obj instanceof IPMapKey)) return false;  
        IPMapKey objKey = (IPMapKey)obj;  
        if(customer_id.equalsIgnoreCase(objKey.customer_id) &&  
        		product_id.equalsIgnoreCase(objKey.product_id)) {  
            return true;  
        }  
        return false;  
    } 
    
    
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	} 
	
}
