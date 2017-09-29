package com.jx.hunter.lvzhengpc.controllers;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.jx.argo.ActionResult;
import com.jx.argo.BeatContext;
import com.jx.argo.Model;
import com.jx.argo.annotations.Path;
import com.jx.blackface.gaea.sell.entity.LvzPackageEntity;
import com.jx.blackface.gaea.sell.entity.LvzPackageSellEntity;
import com.jx.blackface.gaea.sell.entity.LvzProductCateEntity;
import com.jx.blackface.gaea.sell.entity.LvzProductEntity;
import com.jx.blackface.gaea.sell.entity.LvzSellProductEntity;
import com.jx.blackface.gaea.usercenter.entity.BFAreasEntity;
import com.jx.blackface.orderplug.buzs.OrderBuzForHunter;
import com.jx.hunter.lvzhengpc.annotaion.CheckLogin;
import com.jx.hunter.lvzhengpc.common.CommonUtils;
import com.jx.hunter.lvzhengpc.common.LocationPage;
import com.jx.hunter.lvzhengpc.frame.RSBLL;
import com.jx.hunter.lvzhengpc.utils.ProductUtils;
import com.jx.service.preferential.plug.buz.PreferentialMatchBuz;
import com.jx.service.preferential.plug.utils.PreferentialUtils;
import com.jx.service.preferential.plug.vo.UnitVO;

/***
 * 订单操作controller
 * @author duxiaofei
 * @date   2016年3月22日
 */
@Path("/order")
@CheckLogin
public class OrderInfoController extends BaseController{

	private static List<Long> ORDER_RECOMMEND_SELLS = new ArrayList<Long>();
	static {
		
		ORDER_RECOMMEND_SELLS.add(38504614216705L);
		ORDER_RECOMMEND_SELLS.add(38504603418881L);
		ORDER_RECOMMEND_SELLS.add(38504626554369L);
		ORDER_RECOMMEND_SELLS.add(38504723975425L);
	}
	
	
	@Path("/index/{sellid:\\d+}.html")
	public ActionResult gotoIndex(long sellid){
		Model model = beat().getModel();
		model.add("isPackage", "false");  //是否购买的商品包标志
		Map<String,Object> sellProductMap = null ;//new HashMap<String, String>();
		long userid = CommonUtils.getLoginuserid(beat());
		try {
			LvzSellProductEntity sellProductEntity = RSBLL.getstance().getLvzSellProductService().getSellProductEntityById(sellid);
			sellProductMap = BeanUtils.describe(sellProductEntity);
			Map<String,Object> map = new HashMap<String, Object>();
			BFAreasEntity aeasEntity = RSBLL.getstance().getCityService().getAeasEntityById(sellProductEntity.getArea_id());
			List<UnitVO> suitCoupons = PreferentialMatchBuz.getInstance().matchPacketFromAccount(userid, sellid,null);
			if(aeasEntity != null){
				map.put("coupons", suitCoupons);
				map.put("aeasname", aeasEntity.getName());
				map.put("aeasid", aeasEntity.getAreaid());
				BFAreasEntity cityEntity = RSBLL.getstance().getCityService().getAeasEntityById(Long.valueOf(aeasEntity.getParentid()));
				map.put("cityname", cityEntity.getName());
				map.put("cityid", cityEntity.getAreaid());
			}
			sellProductMap.put("queryData", "true");
			sellProductMap.putAll(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(null != sellProductMap && !sellProductMap.isEmpty()){
			model.add("sellProductMap", sellProductMap);
			//根据选定的定价条目ID
			getPackageSellInfoBySellid(sellid);
		}
		
		JSONObject recommendCoupons = PreferentialMatchBuz.getInstance().matchPacketFromAccount(userid, ORDER_RECOMMEND_SELLS);
		if (recommendCoupons != null) {
			model().add("recommend_coupons", recommendCoupons.toJSONString());
		}
		//转到的页面路径
		model.add("LocationPage", new LocationPage("order/order","index",false));
		return view("/index");
	}
	public static final int channel = 1;  //官网
	/***
	 * 提交订单
	 * @param sellids
	 * @return
	 */
	@Path("/submitOrder")
	public ActionResult submitOrder(){
		Model model = beat().getModel();
		String sellids = request().getParameter("sellids")==null?"":request().getParameter("sellids");  //定价条目ids
		
		//添加商品包下单
		String packageSellids = beat().getRequest().getParameter("packagesellids");
		if(StringUtils.isNotBlank(packageSellids)){
			if(StringUtils.isBlank(sellids)){
				sellids += getPackageIdAndSellidStr(packageSellids);
			}else{
				sellids +=","+ getPackageIdAndSellidStr(packageSellids);
			}
		}
		
		if(StringUtils.isBlank(sellids)){
			System.out.println("商品id为空!");
			return redirect("/menu");
		}
		String sellcoupons = beat().getRequest().getParameter("sellcoupons");
		Map<Long,Long> sellCouponsMap = PreferentialUtils.getSellCouponsMap(sellcoupons);
		
		//获取登录用户id
		long userid = CommonUtils.getLoginuserid(beat());
		//调用下订单
		Map<String, Object> orderPlace = OrderBuzForHunter.obfhunter.orderPlace(sellids, userid,sellCouponsMap, channel);

		if(null != orderPlace && !orderPlace.isEmpty()){
			Long pid = (Long) orderPlace.get("after");
			String url = "http://pay.lvzheng.com/reqpay/"+pid;
			return redirect(url);
		}
		//如果有问题跳转到首页 应跳转到错误页面
		return redirect("/menu");
	}
	
	public String getPids(List<Long> pidlist){
		StringBuffer sb_pid = new StringBuffer();
		if(null != pidlist && !pidlist.isEmpty()){
			for(Long tempPid : pidlist){
				if("".equals(sb_pid)){
					sb_pid.append(tempPid);
				}else{
					sb_pid.append(",").append(tempPid);
				}
			}
		}
		return sb_pid.toString();
	}
	
	/**点击购买套餐**/
	@Path("/buyorderByPackage/{packagesellid:\\S+}")
	public ActionResult goToOrder(String packagesellid){
		Model model = beat().getModel();
		model.add("isPackage", "true");  //是否购买的商品包标志
		try {
			LvzPackageSellEntity lvzPackageSellEntity = RSBLL.getstance().getPackageSellService().getLvzPackageSellEntity(Long.valueOf(packagesellid));
			if(null != lvzPackageSellEntity){
				List<Map<String,Object>> packageMapList = new ArrayList<Map<String,Object>>();
				String sellids = lvzPackageSellEntity.getSellids();
				if(StringUtils.isNotBlank(sellids)){
					String[] split_sellid = sellids.split(",");
					for(String sellid : split_sellid){
						LvzSellProductEntity sellProductEntityById = RSBLL.getstance().getLvzSellProductService().getSellProductEntityById(Long.valueOf(sellid));
						
						Map<String,Object> sellProductMap = new HashMap<String, Object>();
						try {
							sellProductMap = BeanUtils.describe(sellProductEntityById);
							Map<String,Object> map = new HashMap<String, Object>();
							BFAreasEntity aeasEntity = RSBLL.getstance().getCityService().getAeasEntityById(sellProductEntityById.getArea_id());
							if(aeasEntity != null){
								map.put("aeasname", aeasEntity.getName());
								map.put("aeasid", aeasEntity.getAreaid());
								BFAreasEntity cityEntity = RSBLL.getstance().getCityService().getAeasEntityById(Long.valueOf(aeasEntity.getParentid()));
								map.put("cityname", cityEntity.getName());
								map.put("cityid", cityEntity.getAreaid());
							}
							sellProductMap.put("queryData", "true");
							sellProductMap.putAll(map);
							//向list中添加map
							packageMapList.add(sellProductMap);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				if(!packageMapList.isEmpty()){
					model.add("packageMapList", packageMapList);
				}
				model.add("lvzPackageSellEntity", lvzPackageSellEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//转到的页面路径
		model.add("LocationPage", new LocationPage("order/order","index"));
		return view("/index");
	}
	
	
	
	/******
	 * 展示推荐的商品包信息
	 * @param productid
	 * @param cityid
	 * @param areaid
	 */
	public void getPackageSellInfoBySellid(long sellid){
		//通过前台穿来的商品ID和城市ID，区域ID查找商品定价条目
		LvzSellProductEntity sellEntity = null;
		try {
			sellEntity = RSBLL.getstance().getLvzSellProductService().getSellProductEntityById(sellid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(null != sellEntity){
			List<LvzPackageSellEntity> packageSellList = new ArrayList<LvzPackageSellEntity>();
			Map<String,List<Map<String,String>>> sellProductMap = new HashMap<String, List<Map<String,String>>>();
			try {
				List<LvzPackageSellEntity> packageSellListBySellid = RSBLL.getstance().getPackageSellService().getLvzPackageSellListBySellid(String.valueOf(sellEntity.getSell_id())); 
				if(null != packageSellListBySellid && !packageSellListBySellid.isEmpty()){
					for(LvzPackageSellEntity packageSellE : packageSellListBySellid){
						LvzPackageEntity lvzPackageEntity = RSBLL.getstance().getPackageService().getLvzPackageEntity(packageSellE.getPackage_id());
						//判断商品包是否为前台商品包
						if(lvzPackageEntity.getPackage_type() == 1){
							packageSellList.add(packageSellE);
							List<Map<String,String>> sellProductMapBySellids = getSellProductMapBySellids(packageSellE.getSellids());
							if(null != sellProductMapBySellids && !sellProductMapBySellids.isEmpty()){
								//获取sellids集合
								sellProductMap.put(String.valueOf(packageSellE.getPackagesell_id()), sellProductMapBySellids);
							}
						}
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			
			
			if( packageSellList.size() > 0 ){
				model().add("packageSellList", packageSellList);
				model().add("sellProductMap2", sellProductMap);
				model().add("ProductUtils", ProductUtils.class);
			}
		}
	}
	
	//通过商品包中包含的sellids字段获取定价条目集合
	public List<Map<String,String>> getSellProductMapBySellids(String sellids){
		if(StringUtils.isBlank(sellids)){
			return null;
		}
		List<Map<String,String>> sellList = new ArrayList<Map<String,String>>();
		//通过  , 号分割
		String[] idArr = sellids.split(",");
		for(int i=0;i<idArr.length;i++){
			Map<String,String> sellMap = new HashMap<String, String>();
			if(StringUtils.isBlank(idArr[i])){
				continue;
			}
			
			LvzSellProductEntity sellProductEntity = null;
			try {
				sellProductEntity = RSBLL.getstance().getLvzSellProductService().getSellProductEntityById(Long.parseLong(idArr[i]));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(sellProductEntity != null){
				//sellList.add(sellProductEntity);
				try {
					sellMap.putAll(BeanUtils.describe(sellProductEntity));
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				} catch (InvocationTargetException e1) {
					e1.printStackTrace();
				} catch (NoSuchMethodException e1) {
					e1.printStackTrace();
				}
				try {
					LvzProductEntity productEntityById = RSBLL.getstance().getLvzProductService().getProductEntityById(sellProductEntity.getProduct_id());
					if(null != productEntityById){
						LvzProductCateEntity productCateEntityByCode = RSBLL.getstance().getLvzProductCateService().getProductCateEntityByCode(productEntityById.getChild_cate_code());
						if(null != productCateEntityByCode){
							sellMap.put("cate_name", productCateEntityByCode.getCate_name());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				sellList.add(sellMap);
			}
		}
		return sellList;
	}
	
	/***
	 * 根据packageSellid动态添加一条商品包数据
	 * @return
	 */
	@Path("/postPackageSellList/{packagesellid:\\S+}")
	public ActionResult postPackageSellList(String packagesellid){
		Model model = beat().getModel();
		try {
			LvzPackageSellEntity lvzPackageSellEntity = RSBLL.getstance().getPackageSellService().getLvzPackageSellEntity(Long.valueOf(packagesellid));
			if(null != lvzPackageSellEntity){
				List<Map<String,Object>> packageMapList = new ArrayList<Map<String,Object>>();
				String sellids = lvzPackageSellEntity.getSellids();
				if(StringUtils.isNotBlank(sellids)){
					String[] split_sellid = sellids.split(",");
					for(String sellid : split_sellid){
						LvzSellProductEntity sellProductEntityById = RSBLL.getstance().getLvzSellProductService().getSellProductEntityById(Long.valueOf(sellid));
						Map<String,Object> sellProductMap = new HashMap<String, Object>();
						try {
							sellProductMap = BeanUtils.describe(sellProductEntityById);
							Map<String,Object> map = new HashMap<String, Object>();
							BFAreasEntity aeasEntity = RSBLL.getstance().getCityService().getAeasEntityById(sellProductEntityById.getArea_id());
							if(aeasEntity != null){
								map.put("aeasname", aeasEntity.getName());
								map.put("aeasid", aeasEntity.getAreaid());
								BFAreasEntity cityEntity = RSBLL.getstance().getCityService().getAeasEntityById(Long.valueOf(aeasEntity.getParentid()));
								map.put("cityname", cityEntity.getName());
								map.put("cityid", cityEntity.getAreaid());
							}
							sellProductMap.put("queryData", "true");
							sellProductMap.putAll(map);
							//向list中添加map
							packageMapList.add(sellProductMap);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				if(!packageMapList.isEmpty()){
					model.add("postPackageMapList", packageMapList);
				}
				model.add("postPackageSellEntity", lvzPackageSellEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view("/order/postPackageList");
	}
	
	/***
	 * 根据Sellid动态添加一条定价条目ID
	 * @return
	 */
	@Path("/postSellProduct/{sellid:\\S+}")
	public ActionResult postSellProduct(String sellid){
		Model model = beat().getModel();
		Map<String,Object> sellProductMap = null ;//new HashMap<String, String>();
		long userid = CommonUtils.getLoginuserid(beat());
		try {
			LvzSellProductEntity sellProductEntity = RSBLL.getstance().getLvzSellProductService().getSellProductEntityById(Long.valueOf(sellid));
			sellProductMap = BeanUtils.describe(sellProductEntity);
			Map<String,Object> map = new HashMap<String, Object>();
			BFAreasEntity aeasEntity = RSBLL.getstance().getCityService().getAeasEntityById(sellProductEntity.getArea_id());
			List<UnitVO> suitCoupons = PreferentialMatchBuz.getInstance().matchPacketFromAccount(userid, Long.parseLong(sellid),null);
			if(aeasEntity != null){
				map.put("coupons", suitCoupons);
				map.put("aeasname", aeasEntity.getName());
				map.put("aeasid", aeasEntity.getAreaid());
				BFAreasEntity cityEntity = RSBLL.getstance().getCityService().getAeasEntityById(Long.valueOf(aeasEntity.getParentid()));
				map.put("cityname", cityEntity.getName());
				map.put("cityid", cityEntity.getAreaid());
			}
			sellProductMap.put("queryData", "true");
			sellProductMap.putAll(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(null != sellProductMap && !sellProductMap.isEmpty()){
			model.add("postSellProductMap", sellProductMap);
		}
		return view("/order/postSellProduct");
	}
		
	/***
	 * 通过商品包IDs返回商品包和商品的拼接字符串 sellid|packageid
	 * @param packageSellids
	 * @return
	 */
	public String getPackageIdAndSellidStr(String packageSellids){
		if(StringUtils.isBlank(packageSellids)){
			return "";
		}
		String returnStr = "";
		String[] packageSplit = StringUtils.split(packageSellids,",");
		for(String packageStr : packageSplit){
			try {
				LvzPackageSellEntity lvzPackageSellEntity = RSBLL.getstance().getPackageSellService().getLvzPackageSellEntity(Long.valueOf(packageStr));
				if(null != lvzPackageSellEntity){
					String sellids = lvzPackageSellEntity.getSellids();
					String[] sellSplit = StringUtils.split(sellids, ",");
					for(String sellStr : sellSplit){
						returnStr += sellStr+"|"+packageStr+",";
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(StringUtils.isNotBlank(returnStr)){
			returnStr = returnStr.substring(0, returnStr.length()-1);
		}
		return returnStr;
	}

	
}
