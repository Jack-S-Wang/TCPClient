package com.dascom.operation.utils.tcp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dascom.operation.service.tcp.impl.UpdateWifeServiceImpl;

@Component
public class SubStringResult {
	
	private static final Logger logger = LogManager.getLogger(SubStringResult.class);
	
	public List<String> resultList(String data){
		List<String> list = new ArrayList<String>();
		//截取返回结果
		String res = data.substring(40, data.length());
		//拆分返回结果，存入list
		String[] strs = new String[res.length()-1];
		for(int i=0;i<res.length()-1;i+=2) {
			strs[i] = res.substring(i,i+2);
			list.add(strs[i]);
		}
		return list;
	}
	
	public String getBase64() {
		String path = "C:\\Users\\ds-1233\\Desktop\\uw11_w_releaseC.ota";
		String base64 = null;
        InputStream in = null;
        try {
            File file = new File(path);
            in = new FileInputStream(file);
            byte[] bytes=new byte[(int)file.length()];
            in.read(bytes);
            base64 = Base64.encodeBase64String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return base64;
	}
	public static void main(String[] args) {
		
		SubStringResult sub = new SubStringResult();
		//System.out.println("11111");
		logger.info(sub.getBase64());
		//System.out.println(sub.getBase64());
		
	}
	

}
