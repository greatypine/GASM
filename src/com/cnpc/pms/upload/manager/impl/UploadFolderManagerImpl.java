package com.cnpc.pms.upload.manager.impl;

import com.cnpc.pms.base.exception.PMSManagerException;
import com.cnpc.pms.base.file.entity.PMSFile;
import com.cnpc.pms.base.file.manager.PMSFileManager;
import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.base.util.StrUtil;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.inter.common.CodeEnum;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.personal.dto.PMSFileDto;
import com.cnpc.pms.upload.entity.UploadFolder;
import com.cnpc.pms.upload.manager.UploadFolderManager;
import com.cnpc.pms.utils.DateUtils;
import com.cnpc.pms.utils.excel.PinyinUtil;
import net.sourceforge.pinyin4j.PinyinHelper;
import org.apache.commons.fileupload.FileItem;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件管理根文件目录数据字典业务实现类
 * Created by liuxiao on 2016/7/5 0005.
 */
public class UploadFolderManagerImpl extends BizBaseCommonManager implements UploadFolderManager {

    private String folder_path = null;

    /**
     *  新增一个上传文件夹
     * @param uploadFolder 保存的文件夹
     */
    @Override
    public Result saveUploadFolder(UploadFolder uploadFolder) {
        Result result = new Result();
        File file_new = new File(getFolder_path().concat(uploadFolder.getDictCode()));
        if(file_new.exists()){
            result.setCode(CodeEnum.folderErr.getValue());
            result.setMessage(CodeEnum.folderErr.getDescription());
            return result;
        }
        file_new.mkdir();
        saveObject(uploadFolder);
        result.setCode(CodeEnum.success.getValue());
        result.setMessage(CodeEnum.success.getDescription());
        return result;
    }

    @Override
    public List<Map<String, String>> getFileListByFolderName(String strFolderName) {
        List<Map<String,String>> lst_result = new ArrayList<Map<String, String>>();
        String folder_path = getFolder_path().concat(strFolderName);
        File file_folder = new File(folder_path);
        if(!file_folder.exists()){
            file_folder.mkdir();
        }
        File[] array_files = file_folder.listFiles();
        if(array_files == null){
            return lst_result;
        }
        for(File file_tmp : array_files){
            Map<String,String> map_row = new HashMap<String, String>();
            map_row.put("name",file_tmp.getName());
            double size = file_tmp.length() / 1024D;
            String str_unit = "KB";
            if(size > 1024){
                size = size / 1024;
                str_unit = "MB";
            }
            String str_size = String.format("%.2f", size).concat(str_unit);
            map_row.put("size",str_size);
            map_row.put("modified", DateUtils.transferLongToDate("yyyy-MM-dd HH:mm:ss",file_tmp.lastModified()));
            lst_result.add(map_row);
        }
        return lst_result;
    }

    @Override
    public List<UploadFolder> getUploadFolderList(UploadFolder tmp) {
        List<?> lst_data = this.getList();
        if(lst_data != null && lst_data.size() > 0){
            return (List<UploadFolder>)lst_data;
        }
        return new ArrayList<UploadFolder>();
    }

    @Override
    public PMSFile uploadObjects(String folderName, List<?> lst_item) throws IOException {
        String folderPath = this.getFolder_path().concat(folderName).concat(File.separator);
        PMSFileManager pmsFileManager = (PMSFileManager) SpringHelper.getBean("PMSFileManager");
        PMSFile uploadedFile = null;
        Iterator i$ = lst_item.iterator();

        while(i$.hasNext()) {
            Object o = i$.next();
            FileItem item = (FileItem)o;
            if(!item.isFormField()) {
                String id = StrUtil.generateUUID();
                String fileName = item.getName();
                String name = fileName.substring(0, fileName.lastIndexOf("."));
                String fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1);

                long sizeInBytes = item.getSize();
                File uploadFile = new File(folderPath + item.getName());
                if(uploadFile.exists()){
                    uploadFile = new File(folderPath + name +"_"+ DateUtils.dateFormat(new Date(),"yyyyMMddHHmmss") + "." + fileSuffix);
                }else{
                    uploadFile.createNewFile();
                }
                try {
                    item.write(uploadFile);
                } catch (Exception var18) {
                    var18.printStackTrace();
                    throw new PMSManagerException("Error that the file was writen in the disk.");
                }

                uploadedFile = new PMSFile();
                uploadedFile.setId(id);
                uploadedFile.setName(name);
                uploadedFile.setFileSuffix(fileSuffix);
                uploadedFile.setFileSize(sizeInBytes);
                uploadedFile.setLastUploaded(new Date());
                uploadedFile.setFilePath(folderName.concat(File.separator).concat(uploadFile.getName()));
                pmsFileManager.saveObject(uploadedFile);
            }
        }

        return uploadedFile;
    }

    @Override
    public String getFileRoot() {
        return PropertiesUtil.getValue("file.web.root");
    }

    @Override
    public PMSFile saveObjectsForFile(String folderName, List<?> lst_item,String fname) throws IOException {
        SimpleDateFormat sf =new SimpleDateFormat("yyyyMMddHHmmss");
        String folderPath = PropertiesUtil.getValue("file.root").concat(folderName).concat(File.separator);
        PMSFileManager pmsFileManager = (PMSFileManager) SpringHelper.getBean("PMSFileManager");
        PMSFile uploadedFile = null;

        String regEx = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(regEx);

        Iterator i$ = lst_item.iterator();
        while(i$.hasNext()) {
            Object o = i$.next();
            FileItem item = (FileItem)o;
            if(!item.isFormField()) {
                String id = StrUtil.generateUUID();
                String fileName = item.getName();
                String name = fileName.substring(0, fileName.lastIndexOf("."));
                String fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                if(name.contains("\\")) {
                    name = name.substring(name.lastIndexOf("\\") + 1);
                }
                if(fname != null){
                    name = PinyinUtil.getPinYinHeadCharUpper(fname);
                }else{
                    Matcher matcher = pat.matcher(name);
                    if (matcher.find()){
                        name = id;
                    }
                }


                File uploadFile = new File(folderPath, name + "." + fileSuffix);
                if(uploadFile.exists()){
                    File folder = new File(folderPath);
                    final String file_name = name;
                    File[] fileArray = folder.listFiles(new FileFilter() {
                        @Override
                        public boolean accept(File pathname) {
                            return pathname.getName().startsWith(file_name);
                        }
                    });
                    name = name + "_" + (fileArray.length + 1);
                    uploadFile = new File(folderPath, name +"." + fileSuffix);
                }
                try {
                    item.write(uploadFile);
                } catch (Exception var18) {
                    var18.printStackTrace();
                    throw new PMSManagerException("Error that the file was writen in the disk.");
                }

                uploadedFile = new PMSFile();
                uploadedFile.setId(id);
                uploadedFile.setName(name);
                uploadedFile.setFileSuffix(fileSuffix);
                uploadedFile.setFileSize(item.getSize());
                uploadedFile.setLastUploaded(new Date());
                uploadedFile.setFilePath(folderName.concat(File.separator) + name + "." + fileSuffix);
                pmsFileManager.saveObject(uploadedFile);
            }
        }
        return uploadedFile;
    }

    /**
     * 获取上传文件夹路径
     * @return 上传文件夹路径
     */
    public String getFolder_path() {
        if(folder_path == null){
            String FILE_ROOT = PropertiesUtil.getValue("file.root");
            folder_path = FILE_ROOT.concat(File.separator).concat("upload_folder").concat(File.separator);
        }
        return folder_path;
    }

    public void setFolder_path(String folder_path) {
        this.folder_path = folder_path;
    }
    
    
    
    
    
    @Override
    public PMSFileDto uploadObjectsSiteSelection(String folderName, List<?> lst_item) throws IOException {
        String folderPath = this.getFolder_path().concat(folderName).concat(File.separator);
        PMSFileManager pmsFileManager = (PMSFileManager) SpringHelper.getBean("PMSFileManager");
        PMSFile uploadedFile = null;
        PMSFileDto pmsFileDto = null;
        Iterator i$ = lst_item.iterator();

        while(i$.hasNext()) {
            Object o = i$.next();
            FileItem item = (FileItem)o;
            if(!item.isFormField()) {
                String id = StrUtil.generateUUID();
                String fileName = item.getName();
                String name = fileName.substring(0, fileName.lastIndexOf("."));
                String fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1);

                long sizeInBytes = item.getSize();
                File uploadFile = new File(folderPath + id+"."+fileSuffix);
                if(uploadFile.exists()){
                    uploadFile = new File(folderPath + name +"_"+ DateUtils.dateFormat(new Date(),"yyyyMMddHHmmss") + "." + fileSuffix);
                }else{
                    uploadFile.createNewFile();
                }
                try {
                    item.write(uploadFile);
                } catch (Exception var18) {
                    var18.printStackTrace();
                    throw new PMSManagerException("Error that the file was writen in the disk.");
                }

                uploadedFile = new PMSFile();
                uploadedFile.setId(id);
                uploadedFile.setName(name);
                uploadedFile.setFileSuffix(fileSuffix);
                uploadedFile.setFileSize(sizeInBytes);
                uploadedFile.setLastUploaded(new Date());
                uploadedFile.setFilePath(folderName.concat(File.separator).concat(uploadFile.getName()));
                pmsFileManager.saveObject(uploadedFile);
                uploadedFile.setFilePath(getFileRoot()+"upload_folder"+File.separator+uploadedFile.getFilePath());
                
                pmsFileDto = new PMSFileDto();
                pmsFileDto.setId(uploadedFile.getId());
                pmsFileDto.setName(uploadedFile.getName());
                pmsFileDto.setFilePath(uploadedFile.getFilePath());
                
            }
        }

        
        
        return pmsFileDto;
    }
    
    
    
    @Override
    public PMSFileDto uploadObjectsBannerInfo(String folderName, List<?> lst_item) throws IOException {
        String folderPath = this.getFolder_path().concat(folderName).concat(File.separator);
        PMSFileManager pmsFileManager = (PMSFileManager) SpringHelper.getBean("PMSFileManager");
        PMSFile uploadedFile = null;
        PMSFileDto pmsFileDto = null;
        Iterator i$ = lst_item.iterator();

        while(i$.hasNext()) {
            Object o = i$.next();
            FileItem item = (FileItem)o;
            if(!item.isFormField()) {
                String id = StrUtil.generateUUID();
                String fileName = item.getName();
                String name = fileName.substring(0, fileName.lastIndexOf("."));
                String fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1);

                long sizeInBytes = item.getSize();
                File uploadFile = new File(folderPath + id+"."+fileSuffix);
                if(uploadFile.exists()){
                    uploadFile = new File(folderPath + name +"_"+ DateUtils.dateFormat(new Date(),"yyyyMMddHHmmss") + "." + fileSuffix);
                }else{
                    uploadFile.createNewFile();
                }
                try {
                    item.write(uploadFile);
                } catch (Exception var18) {
                    var18.printStackTrace();
                    throw new PMSManagerException("Error that the file was writen in the disk.");
                }

                uploadedFile = new PMSFile();
                uploadedFile.setId(id);
                uploadedFile.setName(name);
                uploadedFile.setFileSuffix(fileSuffix);
                uploadedFile.setFileSize(sizeInBytes);
                uploadedFile.setLastUploaded(new Date());
                uploadedFile.setFilePath(folderName.concat(File.separator).concat(uploadFile.getName()));
                pmsFileManager.saveObject(uploadedFile);
                uploadedFile.setFilePath(getFileRoot()+"upload_folder"+File.separator+uploadedFile.getFilePath());
                
                pmsFileDto = new PMSFileDto();
                pmsFileDto.setId(uploadedFile.getId());
                pmsFileDto.setName(uploadedFile.getName());
                pmsFileDto.setFilePath(uploadedFile.getFilePath());
                
            }
        }

        
        
        return pmsFileDto;
    }
}
