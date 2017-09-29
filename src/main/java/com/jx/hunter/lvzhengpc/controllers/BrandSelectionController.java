package com.jx.hunter.lvzhengpc.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.jx.argo.ActionResult;
import com.jx.argo.annotations.Path;
import com.jx.gaea.trade.entity.LvTradeBaseEntity;
import com.jx.hunter.lvzhengpc.common.CommonUtils;
import com.jx.hunter.lvzhengpc.common.LocationPage;
import com.jx.hunter.lvzhengpc.common.MemcachedUtil;
import com.jx.hunter.lvzhengpc.frame.RSBLL;
import com.jx.hunter.lvzhengpc.utils.ActionResultUtils;
import com.jx.hunter.lvzhengpc.utils.BrandUtils;
import com.jx.hunter.lvzhengpc.utils.DingdingUtils;
import com.jx.hunter.lvzhengpc.utils.PageUtils;
import com.jx.hunter.lvzhengpc.vo.LoginUserVO;
import com.jx.hunter.lvzhengpc.vo.LvTradeBaseVo;
import com.jx.service.preferential.plug.utils.EntityUtils;

/**
 * 
 * @ClassName: BrandSelection
 * @Description: TODO(商标筛选)
 * @author: RENQI
 * @date 2016年5月17日 下午2:31:41
 *
 */
@Path("brand")
public class BrandSelectionController extends BaseController {
	private static final String  BRANDSEARCH = "lvzhengpc_brandSearch_";
	private static Map<Integer, String> tradeClassMap = new HashMap<Integer, String>();
	static{
		tradeClassMap.put(1, "化学原料");
		tradeClassMap.put(2, "颜料油漆");
		tradeClassMap.put(3, "日化用品");
		tradeClassMap.put(4, "燃料油脂");
		tradeClassMap.put(5, "医药");
		tradeClassMap.put(6, "金属材料");
		tradeClassMap.put(7, "机械设备");
		tradeClassMap.put(8, "手工器械");
		tradeClassMap.put(9, "科学仪器");
		tradeClassMap.put(10, "医疗器械");
		tradeClassMap.put(11, "灯具空调");
		tradeClassMap.put(12, "运输工具");
		tradeClassMap.put(13, "军火烟火");
		tradeClassMap.put(14, "珠宝钟表");
		tradeClassMap.put(15, "乐器");
		tradeClassMap.put(16, "办公用品");
		tradeClassMap.put(17, "橡胶制品");
		tradeClassMap.put(18, "皮革皮具");
		tradeClassMap.put(19, "建筑材料");
		tradeClassMap.put(20, "家具");
		tradeClassMap.put(21, "厨房洁具");
		tradeClassMap.put(22, "绳网袋篷");
		tradeClassMap.put(23, "纱线丝");
		tradeClassMap.put(24, "布料床单");
		tradeClassMap.put(25, "服装鞋帽");
		tradeClassMap.put(26, "纽扣拉链");
		tradeClassMap.put(27, "地毯席垫");
		tradeClassMap.put(28, "健身器材");
		tradeClassMap.put(29, "食品");
		tradeClassMap.put(30, "方便食品");
		tradeClassMap.put(31, "饲料种籽");
		tradeClassMap.put(32, "啤酒饮料");
		tradeClassMap.put(33, "酒");
		tradeClassMap.put(34, "烟草烟具");
		tradeClassMap.put(35, "广告销售");
		tradeClassMap.put(36, "金融物管");
		tradeClassMap.put(37, "建筑修理");
		tradeClassMap.put(38, "通讯服务");
		tradeClassMap.put(39, "运输贮藏");
		tradeClassMap.put(40, "材料加工");
		tradeClassMap.put(41, "教育娱乐");
		tradeClassMap.put(42, "科技服务");
		tradeClassMap.put(43, "餐饮住宿");
		tradeClassMap.put(44, "医疗园艺");
		tradeClassMap.put(45, "社会服务");
		
	}
	

	/**
	 * 
	 * @Title: brandIndex
	 * @Description: TODO(跳转至商标查询页面)
	 * @param @return 设定文件
	 * @return ActionResult 返回类型
	 * @author: RENQI
	 * @date 2016年5月17日 下午2:47:11
	 * @throws
	 */
	@Path("index")
	public ActionResult brandIndex() {
		return view("/brand/brandSelection");
	}
	
	/**
	 * 
	* @Title: getBrandCharactCombo
	* @Description: TODO(获取行业特点下拉列表数据)
	* @param @return    设定文件
	* @return ActionResult    返回类型
	* @author: RENQI  
	* @date 2016年5月19日 上午9:24:14
	* @throws
	 */
	@Path("getBrandCharactCombo")
	public ActionResult getBrandCharactCombo(){
		String parentId = request().getParameter("parentId");
		Map<String,Object> map = BrandUtils.getBrandCharactMap();
		Object obj = map.get(parentId);
		return ActionResultUtils.renderJson(obj == null ? null : obj.toString());
	}
	
	
	/**
	 * 
	* @Title: getBrandList
	* @Description: TODO(获取结果)
	* @param @return    设定文件
	* @return ActionResult    返回类型
	* @author: RENQI  
	* @date 2016年5月19日 上午9:25:28
	* @throws
	 */
	@Path("getBrandList")
	public ActionResult getBrandList(){
		String parentId = request().getParameter("parentId");
		Map<String,Object> map = BrandUtils.getBrandListMap();
		Object obj = map.get(parentId);
		return ActionResultUtils.renderJson(obj == null ? null : obj.toString());
	}
	
	
	/**
	 * 
	* @Title: gotoBrandIndex
	* @Description: 前往商标搜索首页
	* @param: @return
	* @return: ActionResult
	* @author: RENQI 
	* @date: 2016年7月18日 上午9:42:42
	 */
	@Path("gotoBrandIndex")
	public ActionResult gotoBrandIndex(){
		// 图形验证码
		CommonUtils.geneCode(beat());
		return view("/brand/brandIndex");
	}
	
	/**
	 * 
	* @Title: gotoBrandResult
	* @Description: 前往商标搜索结果页面
	* @param: @return
	* @return: ActionResult
	* @author: RENQI 
	* @date: 2016年7月18日 下午4:09:00
	 */
	@Path("gotoBrandResult")
	public ActionResult gotoBrandResult(){
		return getPageListBrandResult(1);
	}
	
	/**
	 * 
	* @Title: getPageListBrandResult
	* @Description: 分页获取商标搜索数据
	* @param: @return
	* @return: ActionResult
	* @author: RENQI 
	* @date: 2016年7月18日 下午4:18:27
	 */
	@Path("getPageListBrandResult/{pageIndex:\\d+}")
	public ActionResult getPageListBrandResult(int pageIndex){
		String keyWord = request().getParameter("keyWord");
		model().add("keyWord", keyWord);
		String paramValue = "test=1&keyWord="+keyWord;
		int pageSize = 6;
		long count = 0;
		List<LvTradeBaseEntity> list = null;
		List<LvTradeBaseVo> tradeList = null;
		try {
			count = RSBLL.getstance().getTradeBaseService().getCountByNameAndApplyer(keyWord);
			list = RSBLL.getstance().getTradeBaseService().getPageListByNameAndApplyer(keyWord, (pageIndex-1) < 0 ? 0 : (pageIndex-1), pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(list != null){
			tradeList = new ArrayList<LvTradeBaseVo>();
			for(int i=0;i<list.size();i++){
				LvTradeBaseVo tradeVo =  EntityUtils.transferEntity(list.get(i), LvTradeBaseVo.class);
				//类别
				String tradeClsName = tradeClassMap.get(list.get(i).getTrade_class()) == null ? "" : tradeClassMap.get(list.get(i).getTrade_class());
				tradeVo.setTradeClassName(tradeClsName);
				tradeList.add(tradeVo);
			}
			
			model().add("brList", tradeList);
		}
		
		model().add("retCount", count);
		
		// 构建分页
		PageUtils.buildPageModel(model(), pageIndex, count, pageSize, "/brand/getPageListBrandResult",paramValue);
		
		//日志记录打印
		
		
		//memcached 记录 当前用户商标搜索了多少次  key:brandSearch + loginentityId  ---  value:每次加1 如果超过100次则出现验证码，此变量有效期为一天
		LoginUserVO loginEntity = CommonUtils.getLoginEntity(beat());
		Object obj = MemcachedUtil.get(BRANDSEARCH+loginEntity.getUserid());
		int brandSearchCount = 1;
		if(obj == null){//如果不存在当前key
			MemcachedUtil.set(BRANDSEARCH+loginEntity.getUserid(), brandSearchCount, new Date(1000*3600*24));//一天后过期
		}else{
			brandSearchCount = Integer.parseInt(obj.toString()) + 1;
			MemcachedUtil.set(BRANDSEARCH+loginEntity.getUserid(), brandSearchCount);
		}
		model().add("brandSearchCount", brandSearchCount);
		//转到的页面路径
		model().add("LocationPage", new LocationPage("brand/brandResult","index"));
		// 图形验证码
		CommonUtils.geneCode(beat());
		return view("/index");
	}
	

	@Path("checkLogin")
	public ActionResult checkLogin(){
		boolean loginFlag = false;
		LoginUserVO loginEntity = CommonUtils.getLoginEntity(beat());
		JSONObject json = new JSONObject();
		if(loginEntity != null){
			loginFlag = true;
		}
		json.put("loginFlag", loginFlag);
		return ActionResultUtils.renderJson(json);
	}
	
	/**
	 * 
	* @Title: getBrandSearchCount
	* @Description: 获取该用户商标查询次数
	* @param: @return
	* @return: ActionResult
	* @author: RENQI 
	* @date: 2016年7月19日 下午6:02:43
	 */
	@Path("getBrandSearchCount")
	public ActionResult getBrandSearchCount(){
		JSONObject json = new JSONObject();
		LoginUserVO loginEntity = CommonUtils.getLoginEntity(beat());
		if(loginEntity == null){
			json.put("ret", "noLogin");
			return ActionResultUtils.renderJson(json);
		}
		Object obj = MemcachedUtil.get(BRANDSEARCH+loginEntity.getUserid());
		if(obj == null){
			json.put("ret", 0);
		}else{
			json.put("ret", Integer.parseInt(obj.toString()));
		}
		return ActionResultUtils.renderJson(json);
	}
	
	@Path("ldy/brandLdy")
	public ActionResult brandLdy(){
		//转到的页面路径
		model().add("LocationPage", new LocationPage("brand/brandLdy","index"));
		
		return view("/index");
	}
	
	@Path("ldy/sendDingDingMsg")
	public ActionResult sendDingDingMsg(){
		String phone = request().getParameter("phoneNum");
		String keyWord = request().getParameter("keyWord");
		if(StringUtils.isBlank(phone)){
			return ActionResultUtils.renderText("{\"ret\":\""+false+"\"}");
		}
		String content = "您有一个商标查询新客户，电话号码为："+ phone + ",查询关键字为：“" + keyWord + "”。请尽快催单！";
		boolean sendFlag = DingdingUtils.sendDingding("chat9ed9ffae5639b9c8d3b596418baaf97a", "01555218071881", content);
		
		return ActionResultUtils.renderText("{\"ret\":\""+sendFlag+"\"}");
	}
	
}
