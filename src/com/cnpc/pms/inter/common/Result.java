package com.cnpc.pms.inter.common;

import com.cnpc.pms.base.util.JSonHelper;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.personal.entity.*;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * @author ：李天宇
 * @version ：V1.0
 * @Title : Result.java
 * @Package ：com.cnpc.pms.inter.common
 * @Description: 返回对象
 * @date ：2015年11月4日  上午10:35:04
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Result implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 成功消息
     */
    public static final String SUCCESS_MSG = "操作成功！";
    /**
     * 失败消息
     */
    public static final String ERROR_MSG = "操作失败:发生未知异常！";

    /**
     * 结果状态码(可自定义结果状态码) 1:操作成功 0:操作失败
     */
    private int code;
    /**
     * 响应结果描述
     */
    private String message;

    /**
     * 返回数据
     */
    private Object data;

    public Result() {
        super();
    }

    /**
     * @param code 结果状态码(可自定义结果状态码或者使用内部静态变量 1:操作成功 0:操作失败 2:警告)
     */
    public Result(int code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    /**
     * @param code 结果状态码(可自定义结果状态码或者使用内部静态变量 1:操作成功 0:操作失败 2:警告)
     */
    public Result(int code, String message, Object data) {
        super();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 默认操作成功结果.
     */
    public static Result successResult() {
        return new Result(CodeEnum.success.getValue(), CodeEnum.success.getDescription());
    }

    /**
     * 默认操作失败结果.
     */
    public static Result errorResult() {
        return new Result(CodeEnum.error.getValue(), CodeEnum.error.getDescription());
    }

    /**
     * 结果状态码
     */
    public int getCode() {
        return code;
    }

    /**
     * 结果状态码
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 响应结果描述
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置响应结果描述
     * <p/>
     * 响应结果描述
     */
    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    //nonDefaultMapper().toJson(this);
    @SuppressWarnings("static-access")
    @Override
    public String toString() {
        return new JSonHelper().toString();
    }

    //用户验证的token
    private String token;

    //验证码
    private String identifyingCode;

    /*社区**/
    private Village village;


    //快递实体
    private Express express;

    //快递
    private List<Express> expressList;
    
    //用户意见
    private List<UserAdvice> userAdviceList;
    
  public List<UserAdvice> getUserAdviceList() {
		return userAdviceList;
	}

	public void setUserAdviceList(List<UserAdvice> userAdviceList) {
		this.userAdviceList = userAdviceList;
	}

	//社区
    private List<Village> villageList;
    
  //房屋
    private List<Map<String,Object>> houseList;
    
    
    private Map<String, Object> dataMap;

	

	public List<Map<String, Object>> getHouseList() {
		return houseList;
	}

	public void setHouseList(List<Map<String, Object>> houseList) {
		this.houseList = houseList;
	}

	//街道
    private List<Town> townList;

    
    public List<Village> getVillageList() {
		return villageList;
	}

	public void setVillageList(List<Village> villageList) {
		this.villageList = villageList;
	}

	public List<Town> getTownList() {
		return townList;
	}

	public void setTownList(List<Town> townList) {
		this.townList = townList;
	}

	private AppVersion appVersion;

    private Express exitExpress;

    public Express getExitExpress() {
        return exitExpress;
    }

    public void setExitExpress(Express exitExpress) {
        this.exitExpress = exitExpress;
    }

    public List<Express> getExpressList() {
        return expressList;
    }

    public void setExpressList(List<Express> expressList) {
        this.expressList = expressList;
    }


    private UserDTO user;
    //添加 客户类型
    private Customer customer;
    //添加 性格类型
    private CustomerCharacter customerCharacter;
    //添加 爱好类型
    private CustomerHobby customerHobby;

    //楼房
    private Building building;

    //客户集合
    private List<Customer> listCustomer;
    
  //写字楼集合
    private List<Office> listOffice;
    
    
    public List<Office> getListOffice() {
		return listOffice;
	}

	public void setListOffice(List<Office> listOffice) {
		this.listOffice = listOffice;
	}

	private List<Store> storeList;
    
    //商业信息结合
    private List<BusinessInfo> listBusinessInfo;
    


    public List<BusinessInfo> getListBusinessInfo() {
		return listBusinessInfo;
	}

	public void setListBusinessInfo(List<BusinessInfo> listBusinessInfo) {
		this.listBusinessInfo = listBusinessInfo;
	}

	public List<Customer> getListCustomer() {
        return listCustomer;
    }

    public void setListCustomer(List<Customer> listCustomer) {
        this.listCustomer = listCustomer;
    }

    private List<User> userList;
    
    private List<ExpressCompany> expressCompany;

    
    public List<ExpressCompany> getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(List<ExpressCompany> expressCompany) {
		this.expressCompany = expressCompany;
	}

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    //门店信息
    private Store store;

    //街道信息
    private Town town;

    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    //通过姓名查询客户手机号 和地址信息时  给前台返回的数据
    private List<String> listCustomerInfo;


    public List<String> getListCustomerInfo() {
        return listCustomerInfo;
    }

    public void setListCustomerInfo(List<String> listCustomerInfo) {
        this.listCustomerInfo = listCustomerInfo;
    }

    //
    private List<Village> listVillage;


    public List<Village> getListVillage() {
        return listVillage;
    }

    public void setListVillage(List<Village> listVillage) {
        this.listVillage = listVillage;
    }

    public CustomerCharacter getCustomerCharacter() {
        return customerCharacter;
    }

    public void setCustomerCharacter(CustomerCharacter customerCharacter) {
        this.customerCharacter = customerCharacter;
    }

    public CustomerHobby getCustomerHobby() {
        return customerHobby;
    }

    public void setCustomerHobby(CustomerHobby customerHobby) {
        this.customerHobby = customerHobby;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getIdentifyingCode() {
        return identifyingCode;
    }

    public void setIdentifyingCode(String identifyingCode) {
        this.identifyingCode = identifyingCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    //小区的集合
    private List<TinyVillage> tinyVillageList;

    public List<TinyVillage> getTinyVillageList() {
        return tinyVillageList;
    }

    public void setTinyVillageList(List<TinyVillage> tinyVillageList) {
        this.tinyVillageList = tinyVillageList;
    }

    public Village getVillage() {
        return village;
    }

    public void setVillage(Village village) {
        this.village = village;
    }

    public Express getExpress() {
        return express;
    }

    public void setExpress(Express express) {
        this.express = express;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

	public List<Store> getStoreList() {
		return storeList;
	}

	public AppVersion getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(AppVersion appVersion) {
		this.appVersion = appVersion;
	}

	public void setStoreList(List<Store> storeList) {
		this.storeList = storeList;
	}

	public Map<String, Object> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}
}
