
package com.cnpc.pms.personal.manager.impl;

import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.paging.*;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.paging.impl.Sort;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.dynamic.entity.DynamicDto;
import com.cnpc.pms.inter.common.CodeEnum;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.personal.dao.CustomerDao;
import com.cnpc.pms.personal.dao.ExpressDao;
import com.cnpc.pms.personal.entity.BeforeDateCustomer;
import com.cnpc.pms.personal.entity.BeforeDateExpress;
import com.cnpc.pms.personal.entity.DistCity;
import com.cnpc.pms.personal.entity.Express;
import com.cnpc.pms.personal.entity.Humanresources;
import com.cnpc.pms.personal.entity.HumanresourcesChange;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.WorkMonth;
import com.cnpc.pms.personal.entity.WorkRecord;
import com.cnpc.pms.personal.entity.WorkRecordTotal;
import com.cnpc.pms.personal.manager.ExpressManager;
import com.cnpc.pms.personal.manager.HumanresourcesChangeManager;
import com.cnpc.pms.personal.manager.HumanresourcesManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.manager.WorkMonthManager;
import com.cnpc.pms.platform.dao.OrderDao;
import com.cnpc.pms.slice.dao.AreaDao;
import com.cnpc.pms.utils.DateUtils;
import com.cnpc.pms.utils.PropertiesValueUtil;
import com.cnpc.pms.utils.ValueUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 快递业务实现类
 * Created by liuxiao on 2016/6/15 0015.
 */
public class ExpressManagerImpl extends BizBaseCommonManager implements ExpressManager {

	PropertiesValueUtil propertiesValueUtil = null;

    CellStyle cellStyle_common = null;
    
    /**
     * 判断是否存在重复的快递单号 
     */
    @Override
    public int queryExpressByExpressNo(Express express){
        List<?> lst_express_object = this.getList(FilterFactory.getSimpleFilter(" express_no ='"+express.getExpress_no()+"' and status!=1 "));
        if(lst_express_object!=null&&lst_express_object.size()!=0){
            boolean flag = false;
            for(Object o:lst_express_object){
                Express exp=(Express) o;
                if(!exp.getId().equals(express.getId())){
                    flag=true;
                }
            }
            if(flag){
                return 1;
            }else{
                return 0;
            }
        }else{
            return 0;
        }
    }

    @Override
    public Express deleteExpress(Long id){
        Express express=(Express) this.getObject(id);
        express.setStatus(1);
        preSave(express);
        save(express);
        return express;
    }


    /**
     * 查询list列表
     */
    @Override
    public Map<String, Object> queryExpressList(QueryConditions condition) {
        UserManager manager = (UserManager)SpringHelper.getBean("userManager");
        UserDTO userDTO = manager.getCurrentUserDTO();
        Long store_id = userDTO.getStore_id();
        Map<String,Object> returnMap = new java.util.HashMap<String, Object>();
        PageInfo pageInfo = condition.getPageinfo();
        String express_no = null;
        String begindate=null;
        String enddate = null;
        String employee_no = null;
        for(Map<String, Object> map : condition.getConditions()){
            if("express_no".equals(map.get("key"))&&map.get("value")!=null){//查询条件
                express_no = map.get("value").toString();
            }
            if("employee_no".equals(map.get("key"))&&map.get("value")!=null){//查询条件
                employee_no = map.get("value").toString();
            }
            if("express_date".equals(map.get("key"))&&map.get("value")!=null){//查询条件
                String[] dates = map.get("value").toString().split(",");
                if(dates.length!=0){
                    if(dates.length==1){
                        if(dates[0]!=null&&dates[0].length()>0){
                            begindate=dates[0];
                        }
                    }else{
                        if(dates[0]!=null&&dates[0].length()>0){
                            begindate=dates[0];
                        }
                        if(dates[1]!=null&&dates[1].length()>0){
                            enddate=dates[1];
                        }
                    }
                }
            }
        }
        List<?> lst_data = null;
        FSP fsp = new FSP();
        fsp.setSort(SortFactory.createSort("express_date", ISort.DESC));
        StringBuffer sbfCondition = new StringBuffer();
        sbfCondition.append(" status=0 and (express_status!=0 OR express_status is null)  ");
        boolean isGax=false;
        
        if(userDTO.getUsergroup().getCardtype()==0){
        //if(userDTO.getUserGroupId()!=null&&userDTO.getUserGroupId().equals(Long.parseLong("3224"))){
            sbfCondition.append(" and (create_employee_no='"+userDTO.getEmployeeId()+"' or employee_no = '"+userDTO.getEmployeeId()+"')");
            isGax=true;
        }
        if(express_no!=null){
            sbfCondition.append(" and express_no like '%"+express_no+"%'");
        }
        if(begindate!=null){
            sbfCondition.append(" and express_date >='"+begindate+" 00:00:00'");
        }
        if(enddate!=null){
            sbfCondition.append(" and express_date <='"+enddate+" 23:59:59'");
        }

        if(employee_no!=null){
            sbfCondition.append(" and employee_no like '%"+employee_no+"%'");
        }

        
    	if(store_id==null){
            IFilter iFilter =FilterFactory.getSimpleFilter(sbfCondition.toString());
            fsp.setPage(pageInfo);
            fsp.setUserFilter(iFilter);
            lst_data = this.getList(fsp);
        }else{
        	if(!isGax){
                IFilter iFilter =FilterFactory.getSimpleFilter(sbfCondition+" and store_id ="+store_id);
                fsp.setPage(pageInfo);
                fsp.setUserFilter(iFilter);
                lst_data = this.getList(fsp);
        	 }else{
        		IFilter iFilter =FilterFactory.getSimpleFilter(sbfCondition.toString());
                fsp.setPage(pageInfo);
                fsp.setUserFilter(iFilter);
                lst_data = this.getList(fsp);
        	 }
        }

        String store_name = null;
        StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
        if(store_id!=null){
            //查询当前门店
            Store store = (Store)storeManager.getObject(store_id);
            store_name=store.getName();
        }

        List<Express> rList = new ArrayList<Express>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(int i = 0 ; i < lst_data.size() ; i++){
            Express hi = (Express) lst_data.get(i);
            if(hi.getExpress_date()!=null){
                String express_date_str = sdf.format(hi.getExpress_date());
                hi.setExpress_date_str(express_date_str);
            }

            try {
                if(hi.getStore_id()!=null){
                    Store store = (Store)storeManager.getObject(hi.getStore_id());
                    store_name=store.getName();
                    hi.setStore_name(store_name);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            rList.add(hi);
        }
        returnMap.put("pageinfo", pageInfo);
        returnMap.put("header", "");
        returnMap.put("data", rList);
        return returnMap;
    }

    /**
     * 查看快递单列表
     * @param  :   Express
     * @return :   Result
     */
    @Override
    public Result qieryExpressSingleList(PageInfo pageInfo,Express express) {

        Result result = new Result();

        //门店id
        Long store_id = express.getStore_id();
        //当前用户
        String create_user = express.getCreate_user();
        Long createUserId = express.getCreate_user_id();
        String create_employee_no = express.getCreate_employee_no();

        if( store_id == null || StringUtils.isEmpty(create_user) || createUserId == null){
            result.setCode(CodeEnum.paramErr.getValue());
            result.setMessage(CodeEnum.paramErr.getDescription());
            return result;

        }

        FSP fsp  = new FSP();
        //pageInfo.setCurrentPage(1);
        //pageInfo.setRecordsPerPage(10);
        fsp.setSort(SortFactory.createSort("express_status", ISort.ASC)
                .appendSort(SortFactory.createSort("create_time", ISort.DESC))
                .appendSort(SortFactory.createSort("update_time", ISort.ASC)));
        fsp.setUserFilter(FilterFactory.getSimpleFilter("express_status is not null AND status=0 AND store_id = "+store_id+" AND (create_employee_no='"+create_employee_no+"' or employee_no = '"+create_employee_no+"')"));
        fsp.setPage(pageInfo);
        //该门店下的所有的快递
        List<Express> exitDaoExpressList = (List<Express>)super.getList(fsp);

        if( exitDaoExpressList != null && exitDaoExpressList.size() > 0 ){
            result.setExpressList(exitDaoExpressList);
            result.setCode(CodeEnum.success.getValue());
            result.setMessage(CodeEnum.success.getDescription());

        }else{
            result.setCode(CodeEnum.nullData.getValue());
            result.setMessage(CodeEnum.nullData.getDescription());

        }

        return result;
    }



    /**
     * 选中某一个快递单查看详情
     * @param  : id
     * @return : Result
     */
    @Override
    public Result queryExpressSingleDetails(Express express) {

        Result result = new Result();

        Long id = express.getId();

        if( id == null ){

            result.setCode(CodeEnum.nullData.getValue());
            result.setMessage(CodeEnum.nullData.getDescription());

            return result;
        }

        Express express_object = (Express)this.getObject(id);

        if(express_object != null ){

            //处理图片连接
            String imgUrlBase = PropertiesUtil.getValue("file.web.root");
            express_object.setExpressURL(imgUrlBase+express_object.getExpressURL());

            result.setExitExpress(express_object);

            result.setCode(CodeEnum.success.getValue());
            result.setMessage(CodeEnum.success.getDescription());
        }else{

            result.setCode(CodeEnum.nullData.getValue());
            result.setMessage(CodeEnum.nullData.getDescription());

        }

        return result;
    }


    /**
     * 新增快递单
     * @param  : Express
     * @return : Result
     * @info   : 对地址的处理
     */
    @Override
    public Result addExpressSingle(Express express) {

        Result result = new Result();

        //快递编码
//        String bar_code = express.getExpress_no();
        //门店id
        Long store_id = express.getStore_id();
        String expressURL = express.getExpressURL();
        String create_employee_no = express.getCreate_employee_no();

        //当前登陆者
        String create_user = express.getCreate_user();
        Long createUserId = express.getCreate_user_id();;

        ExpressManager expressManager = (ExpressManager)SpringHelper.getBean("expressManager");
//        IFilter filter = FilterFactory.getSimpleFilter("store_id = '"+store_id+"' AND expressURL = '"+expressURL+"'");
//        List<Express> exitExpressList = (List<Express>)expressManager.getList(filter);
//
//        //判断同一快递是否重复添加
//        if( exitExpressList != null && exitExpressList.size() > 0 ){
//
//            result.setCode(CodeEnum.repeatData.getValue());
//            result.setMessage(CodeEnum.repeatData.getDescription());
//
//            return result;
//        }

        Date date = new Date();
        //创建时间
        express.setCreate_time(date);
        //快递地址
        express.setCreate_user(create_user);
        //当前登录用户      --add 2016-03-10
        express.setCreate_user_id(createUserId);

        //快递单完成标识
        //处理传过来的url
        if(expressURL!=null&&expressURL.contains("file_manager")){
            String imgUrl = expressURL.split("file_manager")[1];
            express.setExpressURL(imgUrl.replace("\\", File.separator).replace("/", File.separator));
        }
        express.setCreate_employee_no(create_employee_no);
        express.setExpress_status(0);
        //保存数据库
        expressManager.save(express);


        //返回客户端
        result.setExpress(express);

        result.setCode(CodeEnum.success.getValue());
        result.setMessage(CodeEnum.success.getDescription());

        return result;
    }


    /**
     * 分段加载快递信息
     * @param  : Express
     * @return : Result
     * @info   : 客户端手机界面分段加载显示，并非一次性全部查询
     */
    @Override
    public Result piecewiseLoadExpressList(Express express) {

        Result result = new Result();

        Long store_id = express.getStore_id();

        if( store_id != null ){

            result.setCode(CodeEnum.paramErr.getValue());
            result.setMessage(CodeEnum.paramErr.getDescription());
        }

        String storeId = String.valueOf(store_id);
        FSP fsp  = new FSP();
        fsp.setUserFilter(FilterFactory.getSimpleFilter("store_id",storeId));

        List<Express> exitExpressList = (List<Express>)super.getList(fsp);

        if( exitExpressList != null && exitExpressList.size() > 0 ){

            result.setExpressList(exitExpressList);

            result.setCode(CodeEnum.success.getValue());
            result.setMessage(CodeEnum.success.getDescription());

        }else{

            result.setCode(CodeEnum.nullData.getValue());
            result.setMessage(CodeEnum.nullData.getDescription());

        }

        return result;
    }



    /**
     * 判断是否有未完成快递
     * @param  : Express
     * @return : Result
     * @pageFunction   : 用于快递派送  ---> 安卓端完善订单上面的红点的展示
     */
    @Override
    public Result judgeNotFinishCourier(Express express) {

        Result result = new Result();
        //快递完成的标识要使用 status 来接收  不适用status2  String类型 和  int类型
        Long store_id = express.getStore_id();

        if( store_id == null ){
            result.setCode(CodeEnum.paramErr.getValue());
            result.setMessage(CodeEnum.paramErr.getDescription());

        }

        String storeId = String.valueOf(store_id);

        IFilter filter = FilterFactory.getSimpleFilter("store_id = '"+storeId+"' AND status2 = 1 ");
        ExpressManager expressManager = (ExpressManager)SpringHelper.getBean("expressManager");
        List<Express> exitExpressList = (List<Express>)expressManager.getList(filter);

        //有未完成快递
        if( exitExpressList != null && exitExpressList.size() > 0  ){

            result.setExpressList(exitExpressList);

            result.setCode(CodeEnum.success.getValue());
            result.setMessage(CodeEnum.success.getDescription());

        }else{
            result.setCode(CodeEnum.nullData.getValue());
            result.setMessage(CodeEnum.nullData.getDescription());
        }
        return result;
    }


    @Override
    public Express saveExpress(Express express){
        //当前登录人 如果为国安侠，则保存
        UserManager manager = (UserManager)SpringHelper.getBean("userManager");
        UserDTO userDTO = manager.getCurrentUserDTO();
        User user = manager.findEmployeeByEmployeeNo(userDTO.getEmployeeId());

        if(user!=null&&user.getZw()!=null){
            if(user.getZw().equals("国安侠")){
                express.setCreate_employee_no(userDTO.getEmployeeId());
            }
        }

        express.setExpress_status(1);
        preSaveObject(express);
        save(express);
        return express;
    }


    /**
     * 修改快递信息
     * @param  : Express
     * @return : Result
     */
    @Override
    public Result updateExpressInfo(Express express) {

        Result result = new Result();
        //与App主键冲突定义为s_id
        Long s_id = express.getId();
        String bar_code = express.getExpress_no();
        String receiver_name = express.getRcv_name();
        String receiver_tel = express.getRcv_tel();
        String receiver_address = express.getRcv_address();
        String express_name = express.getExpress_company();
        String expressURL = express.getExpressURL();
        String obj_name =express.getObj_name();
        String employee_no = express.getEmployee_no();
        if( s_id == null ){
            result.setCode(CodeEnum.paramErr.getValue());
            result.setMessage(CodeEnum.paramErr.getDescription());
            return result;
        }

        //判断快递单号重复
        if(express.getExpress_no()!=null&&express.getExpress_no().length()>0){
            int ret = queryExpressByExpressNo(express);
            if( ret != 0 ){
                result.setCode(CodeEnum.repeatData.getValue());
                result.setMessage(CodeEnum.repeatData.getDescription());
                return result;
            }
        }

        IFilter filter = FilterFactory.getSimpleFilter("id = '"+s_id+"'"); // AND status2 = 1
        ExpressManager expressManager = (ExpressManager)SpringHelper.getBean("expressManager");
        List<Express> exitExpressList = (List<Express>)expressManager.getList(filter);
        if( exitExpressList != null && exitExpressList.size() > 0 ){
            Express exitEpress = exitExpressList.get(0);
            String srcImgUrl = exitEpress.getExpressURL();
            if(bar_code!=null&&bar_code.length()>0){
                exitEpress.setExpress_no(bar_code);
                exitEpress.setRcv_name(receiver_name);
                exitEpress.setRcv_tel(receiver_tel);
                exitEpress.setRcv_address(receiver_address);
                exitEpress.setEmployee_id(express.getEmployee_id());
                exitEpress.setEmployee_name(express.getEmployee_name());
                exitEpress.setSend_name(express.getSend_name());
                exitEpress.setSend_tel(express.getSend_tel());
                exitEpress.setSend_address(express.getSend_address());
              /*  
                if(expressURL!=null&&expressURL.contains("file_manager")){
                	String imgUrl = expressURL.split("file_manager")[1];
                	exitEpress.setExpressURL(imgUrl.replace("\\", File.separator).replace("/", File.separator));
                }*/

                //exitEpress.setExpressURL(expressURL);
                exitEpress.setExpress_company(express_name);
                exitEpress.setEmployee_no(employee_no);
                exitEpress.setObj_name(obj_name);
                exitEpress.setExpress_date(express.getExpress_date());
                exitEpress.setRemark(express.getRemark());
                exitEpress.setExpress_type(express.getExpress_type());

                if(bar_code!=null&&bar_code.length()>0){
                    exitEpress.setExpress_status(1);
                }else{
                    exitEpress.setExpress_status(0);
                }

            }
            if(expressURL!=null&&expressURL.contains("file_manager")){
                String imgUrl = expressURL.split("file_manager")[1];
                String expressUrl = imgUrl.replace("\\", File.separator).replace("/", File.separator);
                // "/2016/9" ;"//2016/9"  "///2016/9"  "/////2016/9"
                while(true){
                    if(String.valueOf(expressUrl.charAt(0)).equals(File.separator)){
                        expressUrl = expressUrl.substring(1,expressUrl.length());
                    }else{
                        break;
                    }
                }
                exitEpress.setExpressURL(expressUrl);
            }else{
                exitEpress.setExpressURL(express.getExpressURL());
            }
            exitEpress.setUpdate_user(express.getCreate_user());
            exitEpress.setUpdate_user_id(express.getCreate_user_id());
            Date date = new Date();
            exitEpress.setUpdate_time(date);

            try{
                //判断图片是否更改了 如果更改了 删除原来的图片
                String newImgUrl = exitEpress.getExpressURL();
                if(srcImgUrl!=null&&newImgUrl!=null){
                    String srcEqu = srcImgUrl.substring(srcImgUrl.length()-32, srcImgUrl.length());
                    String newEqu = newImgUrl.substring(newImgUrl.length()-32, newImgUrl.length());
                    if(!srcEqu.equals(newEqu)){
                        //删除src
                        String imgUrlBase = PropertiesUtil.getValue("file.root");
                        deleteFile(imgUrlBase+srcImgUrl);
                    }
                }
            }catch (Exception e) {
            }
            expressManager.save(exitEpress);      //保存数据库
            result.setExpress(exitEpress);    //返回客户端
            result.setCode(CodeEnum.success.getValue());
            result.setMessage(CodeEnum.success.getDescription());
        }else{
            result.setCode(CodeEnum.nullData.getValue());
            result.setMessage(CodeEnum.nullData.getDescription());
        }
        return result;
    }




    /**
     * 新增快递单
     * @param  : Express
     * @return : Result
     * @info   : 对地址的处理
     */
    @Override
    public Result addExpressSingles(Express express) {

        Result result = new Result();

        //快递编码
        String bar_code = express.getExpress_no();
        //门店id
        Long store_id = express.getStore_id();
        String expressURL = express.getExpressURL();
        String filePath1 = "";

        if(expressURL!=null&&expressURL.contains("file_manager")){
            String imgUrl = expressURL.split("file_manager")[1];
            String expressUrl = imgUrl.replace("\\", File.separator).replace("/", File.separator);
            // "/2016/9" ;"//2016/9"  "///2016/9"  "/////2016/9"
            while(true){
                if(String.valueOf(expressUrl.charAt(0)).equals(File.separator)){
                    expressUrl = expressUrl.substring(1,expressUrl.length());
                }else{
                    break;
                }
            }
            filePath1 = expressUrl;
        }else{
            filePath1 = expressURL;
        }


        //当前登陆者
        String create_user = express.getCreate_user();
        Long createUserId = express.getCreate_user_id();

        /*if(  StringUtils.isEmpty(expressURL) || StringUtils.isEmpty(create_user) ||store_id == null
        		|| StringUtils.isEmpty(createUserId)){

     	    result.setCode(CodeEnum.paramErr.getValue());
            result.setMessage(CodeEnum.paramErr.getDescription());

            logger.debug("*************************Exception***********************************");
            logger.debug("类： ExperssManagerImpl ---> 方法： addExpressSingle() ");
            logger.debug(" 社区国标码可能为空，请检查参数 expressURL 是否为空 ---> expressURL ！！！");
            logger.debug("*************************Exception***********************************");


            return result;

        }*/
        String storeId = String.valueOf(store_id);

        ExpressManager expressManager = (ExpressManager)SpringHelper.getBean("expressManager");
        IFilter filter = FilterFactory.getSimpleFilter("store_id = '"+storeId+"' AND expressURL = '"+expressURL+"'");
        List<Express> exitExpressList = (List<Express>)expressManager.getList(filter);

        //判断同一快递是否重复添加
        if( exitExpressList != null && exitExpressList.size() > 0 ){

            result.setCode(CodeEnum.repeatData.getValue());
            result.setMessage(CodeEnum.repeatData.getDescription());

            return result;
        }

        Date date = new Date();
        //创建时间
        express.setCreate_time(date);
        //快递地址
        express.setCreate_user(create_user);
        //当前登录用户      --add 2016-03-10
        express.setCreate_user_id(createUserId);

        //快递单完成标识
        express.setExpressURL(filePath1);

        express.setExpress_date(date);

        //保存数据库
        expressManager.save(express);


        //返回客户端
        result.setExpress(express);

        result.setCode(CodeEnum.success.getValue());
        result.setMessage(CodeEnum.success.getDescription());

        return result;
    }




    /**
     * 查看所有快递
     *
     * @param  ： Express
     * @return : Result
     *
     */
    @Override
    public Result queryAllExpressList(Express express) {

        Result result = new Result();
        //该门店下的所有的快递
        List<Express> exitDaoExpressList = (List<Express>)this.getList();
        if( exitDaoExpressList != null && exitDaoExpressList.size() > 0 ){
            result.setExpressList(exitDaoExpressList);
            result.setCode(CodeEnum.success.getValue());
            result.setMessage(CodeEnum.success.getDescription());
        }else{
            result.setCode(CodeEnum.nullData.getValue());
            result.setMessage(CodeEnum.nullData.getDescription());
        }
        return result;
    }



    /**
     * 选择某一个国安侠派送某一个快递
     *
     * @param  ： Express
     * @return : Result
     *
     */
    @Override
    public Result chooseExpressStaffSendExpress(Express express) {

        Result result = new Result();

        Long id = express.getId();
        Long expressStaff_id = express.getEmployee_id();
        if( id != null || expressStaff_id == null ){
            result.setCode(CodeEnum.paramErr.getValue());
            result.setMessage(CodeEnum.paramErr.getDescription());
            return result;
        }
        IFilter filter = FilterFactory.getSimpleFilter(" id = '"+id+"'");
        ExpressManager expressManager = (ExpressManager) SpringHelper.getBean("expressManager");
        List<Express> exitExpressList = (List<Express>) expressManager.getList(filter);
        if( exitExpressList != null && exitExpressList.size() > 0 ){
            Express exitExpress = exitExpressList.get(0);
            exitExpress.setEmployee_id(expressStaff_id);
            expressManager.save(exitExpress);
            result.setExpress(exitExpress);
            result.setCode(CodeEnum.success.getValue());
            result.setMessage(CodeEnum.success.getDescription());
        }
        return result;
    }


    /**
     * 查找下一条没有填写的快递单
     */
    @Override
    public Express queryExpressByNext(Express express){
        UserManager manager = (UserManager)SpringHelper.getBean("userManager");
        UserDTO userDTO = manager.getCurrentUserDTO();
        Long store_id = userDTO.getStore_id();
        String store_name = userDTO.getStore_name();

        //国安侠只能看到自己的数据
        StringBuffer fiterString  = new StringBuffer();
        fiterString.append(" store_id="+store_id+" and expressURL!=null and express_status =0 ");
        if(userDTO.getZw()!=null&&userDTO.getZw().equals("国安侠")){
            fiterString.append(" and create_employee_no='"+userDTO.getEmployeeId()+"'");
        }

        IFilter filter = FilterFactory.getSimpleFilter(fiterString.toString());
        ExpressManager expressManager = (ExpressManager)SpringHelper.getBean("expressManager");
        List<Express> expressList = (List<Express>) expressManager.getList(filter);
        if(expressList!=null&&expressList.size()>0){
            express=expressList.get(0);
            express.setStore_name(store_name);
            express.setExpress_count(expressList.size());
            //判断如果创建人为综合管理  将创建人返回空 让界面选择国安侠

            String create_employee_no = express.getCreate_employee_no();

            User user = manager.findEmployeeByEmployeeNo(create_employee_no);

            if(user!=null&&user.getZw()!=null){
                if(user.getZw().equals("综合管理")||user.getZw().equals("服务专员")||user.getZw().equals("综合专员")){
                    express.setCreate_employee_no("");
                }
            }



            //处理图片连接
            String imgUrlBase = PropertiesUtil.getValue("file.web.root");
            express.setExpressURL(imgUrlBase+express.getExpressURL());

        }
        return express;
    }


    /**
     * 根据ID查找一条快递信息 赋值门店 
     */
    @Override
    public Express queryExpressById(Long id){
        UserManager manager = (UserManager)SpringHelper.getBean("userManager");
        UserDTO userDTO = manager.getCurrentUserDTO();
        String store_name = userDTO.getStore_name();
        Express express = (Express) this.getObject(id);
        express.setStore_name(store_name);

        //处理图片连接
        String imgUrlBase = PropertiesUtil.getValue("file.web.root");
        express.setExpressURL(imgUrlBase+express.getExpressURL());

        return express;
    }


    /**
     * 删除重复快递信息
     */
    @Override
    public void delExpressMulit(Long id){
        ExpressManager expressManager = (ExpressManager)SpringHelper.getBean("expressManager");
        Express express_object = (Express)this.getObject(id);

        String imgUrl = express_object.getExpressURL();
        //如果图片信息不为空 则物理删除该图片
        if(imgUrl!=null&&imgUrl.length()>0){
            String imgUrlBase = PropertiesUtil.getValue("file.root");
            String deleteImg = imgUrlBase+imgUrl;
            deleteFile(deleteImg);
        }
        expressManager.removeObject(express_object);
    }

    /**
     * 删除单个文件
     * @param   sPath    被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }



    private String getCellValue(Cell cell) {
        String value;
        if(cell == null){
            return null;
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                value = cell.getRichStringCellValue().getString().trim();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                value = cell.getNumericCellValue() == 0D ? null : String.valueOf(cell.getNumericCellValue());
                if (cell.getCellStyle().getDataFormat() == 177||cell.getCellStyle().getDataFormat() == 58||cell.getCellStyle().getDataFormat() == 31){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = org.apache.poi.ss.usermodel.DateUtil
                            .getJavaDate(Double.valueOf(value));
                    value = sdf.format(date);
                    return value;
                }else if(DateUtil.isCellDateFormatted(cell)){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    return sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue()));
                }
                if(value != null && value.contains("E")){
                    value = new BigDecimal(value).toPlainString();
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                value = String.valueOf(cell.getBooleanCellValue()).trim();
                break;
            case Cell.CELL_TYPE_FORMULA:
                value = cell.getCellFormula();
                break;
            default:
                value = "";
        }
        return value;
    }


    /**
     * 格式化日期 将日期格式处理为 yyyy-MM-dd
     * @param str
     * @return
     */
    public String formatDate(String str){
        Calendar calendar=Calendar.getInstance();
        str = str.replaceAll("[^0-9\\s]","-");
        str = str.endsWith("-")?str.substring(0,str.length()-1):str;
        Pattern pat = Pattern.compile("\\d{1,2}-\\d{1,2}");
        Matcher matcher = pat.matcher(str);
        if(matcher.matches()){
            str = calendar.get(calendar.YEAR)+"-" + str;
        }
        return str;
    }



    protected void preSaveObject(Object o) {
        if (o instanceof DataEntity) {
            User sessionUser = null;
            if (null != SessionManager.getUserSession()
                    && null != SessionManager.getUserSession().getSessionData()) {
                sessionUser = (User) SessionManager.getUserSession()
                        .getSessionData().get("user");
            }
            DataEntity dataEntity = (DataEntity) o;
            java.util.Date date = new java.util.Date();
            java.sql.Date sdate = new java.sql.Date(date.getTime());
            // insert处理时添加创建人和创建时间
            if (null == dataEntity.getCreate_time()) {
                dataEntity.setCreate_time(sdate);
                if (null != sessionUser) {
                    dataEntity.setCreate_user(sessionUser.getCode());
                    dataEntity.setCreate_user_id(sessionUser.getId());
                }
            }
            dataEntity.setUpdate_time(sdate);
            if (null != sessionUser) {
                dataEntity.setUpdate_user(sessionUser.getCode());
                dataEntity.setUpdate_user_id(sessionUser.getId());
            }
        }
    }
    @Override
    public Result queryExpressListByCurrentMonth(PageInfo pageInfo, Express express) {
        FSP fsp = new FSP();
        fsp.setPage(pageInfo);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date_start = calendar.getTime();
        Date date_end = new Date();
        StringBuilder sb_where = new StringBuilder();
        if(ValueUtil.checkValue(express.getEmployee_no())){
            sb_where.append(" and employee_no = '"+express.getEmployee_no()+"'");
        }

        if(ValueUtil.checkValue(express.getStore_id())){
            sb_where.append(" and store_id = '"+express.getStore_id()+"'");
        }
        
        IFilter filter = FilterFactory.getBetween("express_date",date_start,date_end)
                .appendAnd(FilterFactory.getStringFilter("status = 0 and (express_status<>0 or express_status is null)" + sb_where.toString()));
        fsp.setUserFilter(filter);
        fsp.setSort(new Sort("express_date",Sort.DESC));
        Result result = new Result();
        List<Express> exitDaoExpressList = (List<Express>)this.getList(fsp);
        if( exitDaoExpressList != null && exitDaoExpressList.size() > 0 ){
            result.setExpressList(exitDaoExpressList);
            result.setCode(CodeEnum.success.getValue());
            result.setMessage(CodeEnum.success.getDescription());

        }else{
            result.setCode(CodeEnum.nullData.getValue());
            result.setMessage(CodeEnum.nullData.getDescription());

        }

        return result;
    }

    
    @Override
	public File exportExpressExcel(String month) throws Exception {
		String str_file_name = "export_express_template";
		String strRootpath = Thread.currentThread().getContextClassLoader().getResource(File.separator).getPath();
		//配置文件中的路径
		String str_filepath = strRootpath.concat(getPropertiesValueUtil().getStringValue(str_file_name).replace("/", File.separator));
		File file_template = new File(str_filepath);

		ExpressDao expressDao = (ExpressDao) SpringHelper.getBean(ExpressDao.class.getName());
		List<Map<String, Object>>  objList = expressDao.queryExpressCount(month); 
		
		String str_file_dir_path = PropertiesUtil.getValue("file.root");
		String str_newfilepath = str_file_dir_path + "express_count.xls";
		File file_new = new File(str_newfilepath);
		if(file_new.exists()){
			file_new.delete();
		}

		FileCopyUtils.copy(file_template, file_new);
		FileInputStream fis_input_excel = new FileInputStream(file_new);
		FileOutputStream fis_out_excel = null;
		Workbook wb_expressinfo = new HSSFWorkbook(new POIFSFileSystem(fis_input_excel));
		try{
			setCellStyle_common(wb_expressinfo);

			Sheet sh_job = wb_expressinfo.getSheetAt(0);
			
			int nIndex=1;
			if(objList!=null&&objList.size()>0){
				for (Map<String, Object> obj : objList) {
					Row obj_row = null;
					int cellIndex = 0;
					if(obj==null){
						continue;
					}
					sh_job.createRow(nIndex);
					obj_row = sh_job.getRow(nIndex);

                    String cityName = obj.get("cityName")==null?"":obj.get("cityName").toString();
					String storename = obj.get("storename")==null?"":obj.get("storename").toString();
                    String storeNo = obj.get("storeNo")==null?"":obj.get("storeNo").toString();
					String employee_name = obj.get("employee_name")==null?"":obj.get("employee_name").toString();
					String employee_no = obj.get("employee_no")==null?"":obj.get("employee_no").toString();
					String express_count = obj.get("express_count")==null?"":obj.get("express_count").toString();

                    setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityName));
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(storename));
                    setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(storeNo));
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(employee_name));
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(employee_no));
					setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(express_count));

					nIndex++;
				}
			}
			
			fis_out_excel = new FileOutputStream(file_new);
			wb_expressinfo.write(fis_out_excel);
		}catch (Exception e){
			throw e;
		} finally {
			if(fis_out_excel!=null){
				fis_out_excel.close();
			}
			if(fis_input_excel!=null){
				fis_input_excel.close();
			}
		}


		return file_new;
	}



    /**
     * 获取配置文件
     * @return 配置文件对象
     */
    public PropertiesValueUtil getPropertiesValueUtil(){
        if(propertiesValueUtil==null){
            propertiesValueUtil = new PropertiesValueUtil(File.separator+"conf"+File.separator+"download_config.properties");
        }
        return propertiesValueUtil;
    }
    
    public void setCellValue(Row obj_row,int nCellIndex,Object value){
        Cell cell=obj_row.createCell(nCellIndex);
        cell.setCellStyle(getCellStyle_common());
        cell.setCellValue(new HSSFRichTextString(value==null?null:value.toString()));
    }

    
    public CellStyle getCellStyle_common(){
        return cellStyle_common;
    }
    
    public void setCellStyle_common(Workbook wb){
        cellStyle_common=wb.createCellStyle();
        cellStyle_common.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        cellStyle_common.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        cellStyle_common.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        cellStyle_common.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        cellStyle_common.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
        cellStyle_common.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);//垂直居中
        cellStyle_common.setWrapText(true);//设置自动换行
    }
    
    
    
    
    
    
    /**
	 * crm 查询当前登录国安侠a的 当月的  所有快递信息
	 * @param employee_no
	 * @param pageInfo
	 * @return
	 */
	@Override
	public Map<String, Object> queryExpressByEmployeeNo(String employee_no,PageInfo pageInfo){
		ExpressDao expressDao = (ExpressDao) SpringHelper.getBean(ExpressDao.class.getName());
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		User user = userManager.findEmployeeByEmployeeNo(employee_no);
		return expressDao.queryExpressByEmployeeNo(user.getStore_id(), employee_no, pageInfo);
	}
	
	
	
	
	
	/**
	  * CRM店长 根据门店 查询 快递代送排序 图表的方法  
	  * @param store_id
	  * @param store_id
	  * @return
	  */
	 @Override
	 public Map<String, Object> queryExpressListByStoreId(Long store_id){
		 ExpressDao expressDao = (ExpressDao) SpringHelper.getBean(ExpressDao.class.getName());
		 Map<String, Object> map = new HashMap<String, Object>();
		 
		 HumanresourcesManager hManager = (HumanresourcesManager) SpringHelper.getBean("humanresourcesManager");
		 List<Humanresources> humanresources = hManager.queryHumanresourceGAXListByStoreId(store_id);
		 
		 map.put("express", expressDao.queryExpressListByStoreId(store_id,humanresources));
		 return map;
	 }

	
	@Override
	public Map<String, Object> queryExpressList_CSZJ_QYJL(Long city_id, String employee_no, String role,String q_date) {
		 ExpressDao expressDao = (ExpressDao) SpringHelper.getBean(ExpressDao.class.getName());
		 Map<String, Object> map = new HashMap<String, Object>();
		 map.put("express", expressDao.queryExpressList_CSZJ_QYJL(city_id, employee_no, role));
		 return map;
	}

	
	@Override
	public void saveBeforeDateExpress_timedTask() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
		Calendar calendar = Calendar.getInstance();
		Date date = new Date();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH,-1);
		String prev_month = simpleDateFormat.format(calendar.getTime());
		Map<String,Object> result = new HashMap<String,Object>();
		ExpressDao expressDao = (ExpressDao) SpringHelper.getBean(ExpressDao.class.getName());
		List<Map<String, Object>> customerList = expressDao.getExpressOfPrevMonthOfStore(prev_month);
		simpleDateFormat = new SimpleDateFormat("yyyy-M");
		for(int i=0;i<customerList.size();i++){
			BeforeDateExpress bDateExpress = new BeforeDateExpress();
			Object amount = customerList.get(i).get("amount");
			Object store_id = customerList.get(i).get("store_id");
			Object store_name = customerList.get(i).get("store_name");
			bDateExpress.setAmount(amount==null?0:Integer.parseInt(amount.toString()));
			bDateExpress.setBind_date(simpleDateFormat.format(calendar.getTime()));
			bDateExpress.setStore_id(Long.parseLong(store_id.toString()));
			bDateExpress.setStore_name(store_name.toString());
			bDateExpress.setCreate_time(date);
			saveObject(bDateExpress);
		}
		
	}

	
	@Override
	public Map<String, Object> queryExpressList_CSZJ_QYJL_before(Long city_id, String employee_no, String role,
			String q_date) {
		 ExpressDao expressDao = (ExpressDao) SpringHelper.getBean(ExpressDao.class.getName());
		 Map<String, Object> map = new HashMap<String, Object>();
		 map.put("express", expressDao.queryExpressList_CSZJ_QYJL_before(city_id, employee_no, role,q_date));
		 return map;
	}


    /**
     * 查询所有快递
     * @param dynamicDto
     * @return
     */
    @Override
    public Map<String, Object> queryExpressList(DynamicDto dynamicDto) {

        dynamicDto.getStoreNumer();
        dynamicDto.getBeginDate();
        ExpressDao expressDao = (ExpressDao) SpringHelper.getBean(ExpressDao.class.getName());
        Map<String, Object> map = new HashMap<String, Object>();
        return null;
    }



    /**
     * 按城市导出快递信息
     * @param express_month
     * @return
     * @throws Exception
     */
    @Override
    public File exportExpressExcelByCity(String express_month,String cityId) throws Exception {
        String str_file_name = "export_express_template";
        String strRootpath = Thread.currentThread().getContextClassLoader().getResource(File.separator).getPath();
        //配置文件中的路径
        String str_filepath = strRootpath.concat(getPropertiesValueUtil().getStringValue(str_file_name).replace("/", File.separator));
        File file_template = new File(str_filepath);
        ExpressDao expressDao = (ExpressDao) SpringHelper.getBean(ExpressDao.class.getName());
        List<Map<String, Object>>  objList = expressDao.queryExpressCount(express_month);
        Long cityid = null;
        if( cityId != null && cityId != "" ){
            cityid = Long.valueOf(cityId);
        }
        List<Map<String,Object>> storeList = expressDao.
                queryExpressByCityId(cityid);
        String cityName_ = null;
        for(int i = 0 ; i < storeList.size() ; i++ ){
            Map<String,Object> map = storeList.get(i);
            cityName_ = map.get("cityname").toString();
        }
        for(int i = 0 ; i < objList.size() ; i++ ){
            Map<String,Object> map = objList.get(i);
            String exit_cityName = map.get("cityName").toString();
            if(!cityName_.equals(exit_cityName)){
                objList.remove(i);
                i--;
            }
        }
        String str_file_dir_path = PropertiesUtil.getValue("file.root");
        String str_newfilepath = str_file_dir_path + "express_count.xls";
        File file_new = new File(str_newfilepath);
        if(file_new.exists()){
            file_new.delete();
        }
        FileCopyUtils.copy(file_template, file_new);
        FileInputStream fis_input_excel = new FileInputStream(file_new);
        FileOutputStream fis_out_excel = null;
        Workbook wb_expressinfo = new HSSFWorkbook(new POIFSFileSystem(fis_input_excel));
        try{
            setCellStyle_common(wb_expressinfo);
            Sheet sh_job = wb_expressinfo.getSheetAt(0);
            int nIndex=1;
            if(objList!=null&&objList.size()>0){
                for (Map<String, Object> obj : objList) {
                    Row obj_row = null;
                    int cellIndex = 0;
                    if(obj==null){
                        continue;
                    }
                    sh_job.createRow(nIndex);
                    obj_row = sh_job.getRow(nIndex);
                    String cityName = obj.get("cityName")==null?"":obj.get("cityName").toString();
                    String storename = obj.get("storename")==null?"":obj.get("storename").toString();
                    String storeNo = obj.get("storeNo")==null?"":obj.get("storeNo").toString();
                    String employee_name = obj.get("employee_name")==null?"":obj.get("employee_name").toString();
                    String employee_no = obj.get("employee_no")==null?"":obj.get("employee_no").toString();
                    String express_count = obj.get("express_count")==null?"":obj.get("express_count").toString();

                    setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(cityName));
                    setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(storename));
                    setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(storeNo));
                    setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(employee_name));
                    setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(employee_no));
                    setCellValue(obj_row, cellIndex ++, ValueUtil.getStringValue(express_count));
                    nIndex++;
                }
            }
            fis_out_excel = new FileOutputStream(file_new);
            wb_expressinfo.write(fis_out_excel);
        }catch (Exception e){
            throw e;
        } finally {
            if(fis_out_excel!=null){
                fis_out_excel.close();
            }
            if(fis_input_excel!=null){
                fis_input_excel.close();
            }
        }
        return file_new;
    }


}
