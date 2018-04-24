package com.cnpc.pms.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

import com.cnpc.pms.base.util.PropertiesUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

@SuppressWarnings("all")
public class BarCodeUtils {

	public static Map<String, String> createBarCodeImage(){
		String url = "";
		Map<String, String> map = new HashMap<String, String>();
		try {
			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			map.put("uuid", uuid);
			//String text = "http://10.16.31.132:8888/daqWeb/bizbase/codelogin.html?uuid="+uuid;//这是连接地址 
			String text = uuid;//这是uuid
			int width = 200;
			int height = 200;
			String format = "png";
			Hashtable hints = new Hashtable();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			 hints.put(EncodeHintType.MARGIN, 0);
			
			
			BitMatrix bitMatrix = new MultiFormatWriter().encode(text,BarcodeFormat.QR_CODE, width, height, hints);
			
			String str_file_dir_path = PropertiesUtil.getValue("file.root")+"barcode/";
			String imgFileName = uuid+".png";
			String str_newfilepath = str_file_dir_path + "/"+imgFileName;

			File outputFile = new File(str_newfilepath); //生成二维码 路径
			MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);
			
			url = PropertiesUtil.getValue("file.web.root")+"barcode/"+imgFileName;
			map.put("url", url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return map;
	}
	
}
