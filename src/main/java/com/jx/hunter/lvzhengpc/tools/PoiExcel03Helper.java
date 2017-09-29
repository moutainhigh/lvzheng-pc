package com.jx.hunter.lvzhengpc.tools;

import java.io.FileInputStream;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/***
 * 操作2003版本的excel，文件结尾为.xls
 * @author flower
 */
public class PoiExcel03Helper extends PoiExcelHelper{
	/** 获取sheet列表 */  
    public ArrayList<String> getSheetList(String filePath) {  
        ArrayList<String> sheetList = new ArrayList<String>(0);  
        try {  
            HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(filePath)); 
            int i = 0;  
            while (true) {  
                try {  
                    String name = wb.getSheetName(i);  
                    sheetList.add(name);  
                    i++;  
                } catch (Exception e) {  
                    break;  
                }  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return sheetList;  
    }  
  
    /** 读取Excel文件内容 */  
    public ArrayList<ArrayList<String>> readExcel(String filePath, int sheetIndex, String rows, String columns) {  
        ArrayList<ArrayList<String>> dataList = new ArrayList<ArrayList<String>> ();  
        try {  
            HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(filePath));  
            HSSFSheet sheet = wb.getSheetAt(sheetIndex);  
              
            dataList = readExcel(sheet, rows, getColumnNumber(sheet, columns));  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return dataList;  
    }  
      
    /** 读取Excel文件内容 */  
    public ArrayList<ArrayList<String>> readExcel(String filePath, int sheetIndex, String rows, int[] cols) {  
        ArrayList<ArrayList<String>> dataList = new ArrayList<ArrayList<String>> ();  
        try {  
            HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(filePath));  
            HSSFSheet sheet = wb.getSheetAt(sheetIndex);  
              
            dataList = readExcel(sheet, rows, cols);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return dataList;  
    }
}
