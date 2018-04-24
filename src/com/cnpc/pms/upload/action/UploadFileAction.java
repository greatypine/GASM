package com.cnpc.pms.upload.action;

import com.cnpc.pms.base.common.action.DispatcherAction;
import com.cnpc.pms.base.common.model.ClientRequestObject;
import com.cnpc.pms.base.common.model.ClientResponseObject;
import com.cnpc.pms.base.exception.DispatcherException;
import com.cnpc.pms.base.util.StrUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by liu on 2016/7/6 0006.
 */
public class UploadFileAction extends DispatcherAction {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String img_type = request.getParameter("img_type");
        if(img_type == null || "".equals(img_type)){
            response.setContentType("text/html");
            PrintWriter writer = response.getWriter();
            try{
                execute(request, response);
                writer.write("<script type='text/javascript'>window.parent.finishedUpload();</script>" );
            }catch (Exception e){
                e.printStackTrace();
                writer.write("<script type='text/javascript'>window.parent.uploadError();</script>" );
            }

            writer.flush();
            writer.close();
        }else{
            execute(request, response);
        }

    }

    @Override
    protected ClientRequestObject getClientRequestObject(HttpServletRequest request) throws DispatcherException {
        FileItemFactory factory = new DiskFileItemFactory();// Create a factory for disk-based file items
        ServletFileUpload upload = new ServletFileUpload(factory);// Create a new file upload handler
        // Parse the request
        List<?> items;
        try {
            items = upload.parseRequest(request);
        } catch (FileUploadException e) {
            throw new DispatcherException("Fail to get uploaded file: " + e.getMessage());
        }

        Iterator i$ = items.iterator();
        String fileName = null;
        String img_type = null;
        String fname = null;
        while(i$.hasNext()) {
            Object o = i$.next();
            FileItem item = (FileItem) o;
            if(item.isFormField()){
                if("file_name".equals(item.getFieldName())){
                    fileName = item.getString();
                }
                if("img_type".equals(item.getFieldName())){
                    img_type = item.getString();
                }
                if("fname".equals(item.getFieldName())){
                    try {
                        fname = new String(item.getString().getBytes("ISO-8859-1"),"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        throw new DispatcherException(e.getMessage());
                    }
                }
            }
        }

        if(img_type == null){
            img_type = request.getParameter("img_type");
        }
        if(fileName == null){
            fileName = request.getParameter("file_name");
        }

        Object[] paras = null;
        String clientInvokeString = request.getParameter(REQUEST_STRING);
        ClientRequestObject requestObj = new ClientRequestObject();
        requestObj.setManagerName("UploadFolderManager");
        if(img_type == null || "".equals(img_type)){
            paras = new Object[] {fileName, items };
            requestObj.setMethodName("uploadObjects");
        }else{
            paras = new Object[] {fileName, items ,fname};
            requestObj.setMethodName("saveObjectsForFile");
        }
        requestObj.setParameters(paras);
        return requestObj;
    }

    @Override
    protected void doResponse(ClientResponseObject responseObj, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.getWriter().write(responseObj.toJSON());
    }

}
