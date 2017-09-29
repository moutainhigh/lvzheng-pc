package com.jx.hunter.lvzhengpc.controllers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.jx.argo.ActionResult;
import com.jx.argo.ArgoTool;
import com.jx.argo.annotations.Path;
import com.jx.argo.controller.AbstractController;
import com.jx.blackface.gaea.sell.entity.LvzCusEvaluationEntity;
import com.jx.blackface.gaea.sell.entity.LvzProductCateEntity;
import com.jx.blackface.gaea.sell.entity.LvzProductEntity;
import com.jx.hunter.lvzhengpc.common.DataFormat;
import com.jx.hunter.lvzhengpc.common.MemcachedUtil;
import com.jx.hunter.lvzhengpc.frame.RSBLL;
import com.jx.hunter.lvzhengpc.tools.PoiExcelHelper;
import com.jx.hunter.lvzhengpc.utils.PageUtils;

/**
 * 
* @ClassName: CusEvaluation
* @Description: TODO(客户评价)
* @author: RENQI  
* @date 2016年7月6日 上午9:52:07
*
 */
@Path("cusEvaluation")
public class CusEvaluationController extends AbstractController{
	private static final String PINGFENLIST = "lvzhengpc_pingfenlist_";
	private static final String PINGFENLISTCOUNT = "lvzhengpc_pingfenlistcount_";
	@Path("getPageListCusEvalByPrdtId")
	public ActionResult getPageListCusEvalByPrdtId(){
		return getPageListCusEvalByPage(1);
	}
	
	@Path("/getPageListByPage/{pageIndex:\\d+}")
	public ActionResult getPageListCusEvalByPage(int pageIndex){
		List<LvzCusEvaluationEntity> list = null;
		String prdtIdStr = request().getParameter("productId");
		String scope = request().getParameter("scope");
		String prdtCateId = request().getParameter("product_cate_id");
		
		
		String paramValue = "test=1";
		String condition = "1=1";
		int count = 0;
		int pageNum = pageIndex;
		int pageSize = 5;
		
		//评分范围
		if(StringUtils.isNotBlank(scope)){
			//检测是否含有“-”  
			if(scope.contains("~")){//按评分范围查询数据
				String[] scopeArr = scope.split("~");
				if(scopeArr.length == 1){//比如 5-  split后只会有一个
					condition += " and zhpf >='"+ scopeArr[0]+"'";
				}else{
					if(StringUtils.isNotBlank(scopeArr[0])){
						condition += " and zhpf >='"+ scopeArr[0]+"'";
					}
					if(StringUtils.isNotBlank(scopeArr[1])){
						condition += " and zhpf <'"+scopeArr[1]+"'";
					}
				}
				paramValue += "&scope="+scope;
			}
		}
		
		if(StringUtils.isBlank(prdtCateId)){//第一次查询，通过getPageListCusEvalByPrdtId 方法进来的
			LvzProductEntity prdtEntity = null;
			try {
				prdtEntity = RSBLL.getstance().getLvzProductService().getProductEntityById(Long.parseLong(prdtIdStr));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			LvzProductCateEntity prdtCateEntity = null;
			try {
				prdtCateEntity = RSBLL.getstance().getLvzProductCateService().getProductCateEntityByCode(prdtEntity.getChild_cate_code());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			prdtCateId = prdtCateEntity.getCate_id() + "";
		}
		
		condition += "  and product_cate_id='" + prdtCateId+ "'";
		paramValue += "&product_cate_id=" + prdtCateId;
		
		
		model().add("DataFormat", DataFormat.class);
		//判断memchahe中是否存在记录
		Object obj = MemcachedUtil.get(PINGFENLIST+"prdtCateId"+ prdtCateId+"_scope"+scope+"_pageNum"+pageNum);
		Object obj2 = MemcachedUtil.get(PINGFENLISTCOUNT+"prdtCateId"+ prdtCateId+"_scope"+scope+"_pageNum"+pageNum);
		if(obj != null && obj2 != null){
			count = (Integer) obj2;
			list = (List<LvzCusEvaluationEntity>) obj;
			model().add("cusEvalList", list);
			PageUtils.buildPageModel(model(), pageNum, count, pageSize, "/cusEvaluation/getPageListByPage",paramValue);
			return view("/cusEvaluation/cusEvaluation");
		}
		
		try {
			count = RSBLL.getstance().getLvzCusEvaluationService().getCountbyCondition(condition);
			list = RSBLL.getstance().getLvzCusEvaluationService().getListByCondition(condition, pageNum, pageSize, "datetime desc");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(list != null){
			model().add("cusEvalList", list);
			
			if(pageNum == 1){//缓存第一页的数据
				//添加memcache 记录
				boolean flag = MemcachedUtil.set(PINGFENLIST+"prdtCateId"+ prdtCateId+"_scope"+scope+"_pageNum"+pageNum, list,new Date(1000*3600*24));
				MemcachedUtil.set(PINGFENLISTCOUNT+"prdtCateId"+ prdtCateId+"_scope"+scope+"_pageNum"+pageNum, count,new Date(1000*3600*24));
			}
			
		}
		
		// 构建分页
		PageUtils.buildPageModel(model(), pageNum, count, pageSize, "/cusEvaluation/getPageListByPage",paramValue);
		return view("/cusEvaluation/cusEvaluation");
	}
	
	
	
	
	
	
	public static void main(String[] args) {
		//new CusEvaluationController().readExcel();
		
		
		/*将Excel中的数据导入到表中*/
		try {
			new CusEvaluationController().loadToTable();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void loadToTable() throws Exception{
		String filePath = "d://评论上线1(3).xlsx";
		
		PoiExcelHelper helper = PoiExcelHelper.getPoiExcelHelper(filePath);
		ArrayList<String> sheets = helper.getSheetList(filePath);

		for(int k=0;k<sheets.size();k++){
			// 读取excel文件数据  
	        ArrayList<ArrayList<String>> dataList = helper.readExcel(filePath, k);//读取第一个sheet
	        Map<String ,String> map = new HashMap<String, String>();
	        for(int i=1;i<dataList.size();i++){
	        	ArrayList<String> strData = dataList.get(i);
	        	//商品名称
	        	String prdtName = strData.get(2);
	        	if(StringUtils.isBlank(prdtName)){
	        		continue;
	        	}
	        	//通过商品名称查询对应的productid
	        	String contition = " product_name='" +prdtName+ "'";
	        	List<LvzProductEntity> list = null;
	        	try {
					 list =  RSBLL.getstance().getLvzProductService().getProductEntityList(contition, 1, 1, "");
				} catch (Exception e) {
					e.printStackTrace();
				}
	        	//通过code查询所属的二级类id
	        	if(list == null || list.size() == 0){
	        		continue;
	        	}
	        	LvzProductCateEntity cateEntity  = null;
	        	try {
	        		cateEntity = RSBLL.getstance().getLvzProductCateService().getProductCateEntityByCode(list.get(0).getChild_cate_code());
				} catch (Exception e) {
					e.printStackTrace();
				}
	        	if(cateEntity == null){
	        		continue;
	        	}
	        	
	        	StringBuffer cellContent = new StringBuffer("");
	        	//二级类id 及  产品id
	        	cellContent.append(cateEntity.getCate_id()).append("|").append(list.get(0).getProduct_id()).append("|");
				for(int j = 0;j<9;j++){
					cellContent.append(strData.get(j)).append("|");
				}
				System.out.println("sheet:"+k+"   num:"+i+"     "+cellContent.toString());
				map.put(new Date().getTime()+"",cellContent.toString());
	        }
	        //writePro(map,sheets.get(k));
	        if(map != null){
	        	for (String key : map.keySet()) {
					String value = map.get(key);
					String[] str = value.split("\\|");
					//向数据库中插入
			        LvzCusEvaluationEntity cus = new LvzCusEvaluationEntity();
			        cus.setAddtime(new Date());
			        cus.setUpdatetime(new Date());
			        cus.setProduct_cate_id(Long.parseLong(str[0]));
			        cus.setProduct_id(Long.parseLong(str[1]));
			        Date date = new SimpleDateFormat("yyyy.mm.dd").parse(str[2]);
			        cus.setDatetime(date);
			        cus.setPhone(str[3]);
			        cus.setProduct_name(str[4]);
			        cus.setContent(str[5]);
			        cus.setFwtd(Integer.parseInt(str[6]));
			        cus.setZycd(Integer.parseInt(str[7]));
			        cus.setXysd(Integer.parseInt(str[8]));
			        cus.setBsxl(Integer.parseInt(str[9]));
			        int zf = cus.getFwtd() + cus.getZycd() + cus.getXysd() + cus.getBsxl();
			        double pjf = zf/4.0;
			        cus.setZhpf(pjf);
			        cus.setYxj(Integer.parseInt(str[10]));
			        try {
						long n = RSBLL.getstance().getLvzCusEvaluationService().saveCusEvaluationEntity(cus);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
	        }
		}
	}
	
	
	public void readExcel() {
		String filePath = "d://评论上线1(3).xlsx";

		PoiExcelHelper helper = PoiExcelHelper.getPoiExcelHelper(filePath);
		ArrayList<String> sheets = helper.getSheetList(filePath);

		for (int k = 0; k < sheets.size(); k++) {
				// 读取excel文件数据
				ArrayList<ArrayList<String>> dataList = helper.readExcel(
						filePath, k);// 读取第一个sheet
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 1; i < dataList.size(); i++) {


					ArrayList<String> strData = dataList.get(i);
					// 商品名称
					String prdtName = strData.get(2);
					if (StringUtils.isBlank(prdtName))
						continue;
					// 通过商品名称查询对应的productid
					String contition = " product_name='" + prdtName + "'";
					List<LvzProductEntity> list = null;
					try {
						list = RSBLL.getstance().getLvzProductService()
								.getProductEntityList(contition, 1, 1, "");
					} catch (Exception e) {
						e.printStackTrace();
					}
					// 通过code查询所属的二级类id
					if (list == null || list.size() == 0) {
						continue;
					}
					LvzProductCateEntity cateEntity = null;
					try {
						cateEntity = RSBLL
								.getstance()
								.getLvzProductCateService()
								.getProductCateEntityByCode(
										list.get(0).getChild_cate_code());
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (cateEntity == null) {
						continue;
					}

					StringBuffer cellContent = new StringBuffer();
					// 二级类id 及 产品id
					cellContent.append(cateEntity.getCate_id()).append("|")
							.append(list.get(0).getProduct_id()).append("|");
					for (int j = 0; j < 9; j++) {
						cellContent.append(strData.get(j)).append("|");
					}
					System.out.println(cellContent.toString());
					map.put(new Date().getTime() + "", cellContent.toString());
				}
				writePro(map, sheets.get(k));
		}
	}
	
	public static void writePro(Map<String ,String > map,String sheetName){
		Properties props = new Properties();
		OutputStream fos = null;
		OutputStreamWriter sw = null;
		try {
            // 调用 Hashtable 的方法 put，使用 getProperty 方法提供并行性。
			String url = ArgoTool.getConfigFolder()+ArgoTool.getNamespace();
			System.out.println(url);
            // 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
            fos = new FileOutputStream(url+"//pingjia.properties",true);
            sw = new OutputStreamWriter(fos, "utf-8");
            // 以适合使用 load 方法加载到 Properties 表中的格式，
            /*InputStream in = new FileInputStream(url+"//pingjia.properties");
            InputStreamReader sr = new InputStreamReader(in,"utf-8");
            props.load(sr);
            for (Enumeration e = props.propertyNames(); e.hasMoreElements();) {
                String key = (String) e.nextElement(); // 遍历所有元素
                props.put(key, props.getProperty(key));
            }*/
            
            props.putAll(map);
            // 将此 Properties 表中的属性列表（键和元素对）写入输出流
            props.store(sw, new String((sheetName + "   " + new Date()).getBytes(), "utf-8"));
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        	try {
				sw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}
	
	
}
