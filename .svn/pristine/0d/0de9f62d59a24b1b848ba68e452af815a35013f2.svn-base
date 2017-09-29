/**
 * 
 */

package com.jx.hunter.lvzhengpc.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.jx.argo.ArgoTool;



/**
 * simple introduction
 *
 * <p>detailed comment</p>
 * @author chuxuebao 2015年9月16日
 * @see
 * @since 1.0
 */

public class ConfigFileUtils {
	
	private static volatile Map<String, Properties> configFiles = new ConcurrentHashMap<String, Properties>();
	
	private static Object lock = new Object();
	
	public static Properties getConfigFile(String fileName){
		if(configFiles.containsKey(fileName) && configFiles.get(fileName) != null){
			return configFiles.get(fileName);
		}
		synchronized (lock) {
			if(!configFiles.containsKey(fileName) || configFiles.get(fileName) == null){
				String url = ArgoTool.getConfigFolder()+ArgoTool.getNamespace() + "/" + fileName;
				InputStream in = null;
				try {
					in = new BufferedInputStream(new FileInputStream(url));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} 
				Properties p = new Properties(); 
				if(in != null){
					try {
						p.load(in);
					} catch (IOException e) {
						e.printStackTrace();
					} 
				}
				if(!p.isEmpty()){
					configFiles.put(fileName, p);
				}
			}
		}
		return configFiles.get(fileName);
	}
	
	
	/** 
	* 去掉url中的路径，留下请求参数部分 
	* 
	* @author bruce
	* @date 2016-5-10
	* @param strURL 
	*            url地址 
	* @return url请求参数部分 
	*/ 
	public static String TruncateUrlPage(String strURL) { 
		String strAllParam = null; 
		String[] arrSplit = null; 

		strURL = strURL.trim().toLowerCase(); 

		arrSplit = strURL.split("[?]"); 
		if (strURL.length() > 1) { 
			if (arrSplit.length > 1) { 
				if (arrSplit[1] != null) { 
					strAllParam = arrSplit[1]; 
				} 
			} 
		} 

		return strAllParam; 
	} 
}
