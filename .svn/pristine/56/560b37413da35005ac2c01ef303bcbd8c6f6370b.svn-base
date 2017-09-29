package com.jx.hunter.lvzhengpc.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class BrandUtils {
	public static Map<String, Object> brandCharactMap = null;
	public static Map<String,Object> brandListMap = null;
	

	public synchronized static Map<String, Object> getBrandCharactMap(){
		if(brandCharactMap == null || brandCharactMap.isEmpty()){
			brandCharactMap = new HashMap<String, Object>();
		}else{
			return brandCharactMap;
		}
		
		// 读取brand.properties文件，保存到map中
		Properties pps = new Properties();
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("brandCharact.properties");
			pps.load(new InputStreamReader(is, "UTF-8"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Enumeration enum1 = pps.propertyNames();//得到配置文件的名字
		while (enum1.hasMoreElements()) {
			String strKey = (String) enum1.nextElement();
			String strValue = pps.getProperty(strKey);
			brandCharactMap.put(strKey, strValue);
		}
		
		return brandCharactMap;
	}
	
	public synchronized static Map<String, Object> getBrandListMap(){
		if(brandListMap == null || brandListMap.isEmpty()){
			brandListMap = new HashMap<String, Object>();
		}else{
			return brandListMap;
		}
		
		// 读取brand.properties文件，保存到map中
		Properties pps = new Properties();
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("brandList.properties");
			pps.load(new InputStreamReader(is, "UTF-8"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Enumeration enum1 = pps.propertyNames();//得到配置文件的名字
		while (enum1.hasMoreElements()) {
			String strKey = (String) enum1.nextElement();
			String strValue = pps.getProperty(strKey);
			brandListMap.put(strKey, strValue);
		}
		
		return brandListMap;
	}
}
