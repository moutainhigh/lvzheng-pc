package com.jx.hunter.lvzhengpc.controllers;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.jx.argo.ActionResult;
import com.jx.argo.annotations.Path;
import com.jx.blackface.fileplug.buzs.FileDownloadBuz;
import com.jx.hunter.lvzhengpc.common.CommonUtils;
import com.jx.hunter.lvzhengpc.common.LocationPage;
import com.jx.hunter.lvzhengpc.common.MemcachedUtil;
import com.jx.hunter.lvzhengpc.frame.RSBLL;
import com.jx.hunter.lvzhengpc.utils.ActionResultUtils;
import com.jx.hunter.lvzhengpc.utils.DingdingUtils;
import com.jx.hunter.lvzhengpc.utils.MenuUtils;
import com.jx.hunter.lvzhengpc.utils.Timers;
import com.jx.hunter.lvzhengpc.utils.WorkDayUtil;
import com.jx.hunter.lvzhengpc.vo.Bannervo;
import com.jx.service.dic.entity.DicFileEntity;
import com.jx.service.enterprise.entity.LvEnterpriseMainBusinessEntity;
import com.jx.service.preferential.plug.buz.PreferentialMatchBuz;
import com.jx.service.preferential.plug.vo.PacketVO;

public class CommonController extends BaseController{
	
	private int DEF_MAIN_BUS_SIZE = 99;
	private static final int totalBase = 188921;//总用户基数
	private static final int totalOrderBase = 56827;//总用户基数
	private static int todayadd_ = 0; 
	private static int userCount_ = 0;//总用户数
	private static int orderCount_ = 0;//总下单用户基数

	/**
	 * 旧的表达式映射
	 */
	static LinkedHashMap<String,String> repxMap = new LinkedHashMap<String,String>();
	
	static{
		repxMap.put("reg_sz",         "/commondetail/detail/38229817543169.html?cityid=2&areaid=201");
		repxMap.put("reg_bj",         "/commondetail/detail/38229817543169.html?cityid=1&areaid=101");
		repxMap.put("bookkeeping_sz", "/commondetail/detail/38253214893569.html?cityid=2&areaid=201");
		repxMap.put("bookkeeping_bj", "/commondetail/detail/38253214893569.html?cityid=1&areaid=101");
		repxMap.put("trademark_sz",   "/commondetail/detail/38231039879425.html?cityid=2&areaid=201");
		repxMap.put("trademark_bj",   "/commondetail/detail/38231039879425.html?cityid=1&areaid=101");
		repxMap.put("change_sz",      "/commondetail/detail/38230152382977.html?cityid=2&areaid=201");
		repxMap.put("change_bj",      "/commondetail/detail/38230152382977.html?cityid=1&areaid=101");
		
		repxMap.put("reg",         "/commondetail/detail/38229817543169.html?cityid=1&areaid=101");
		repxMap.put("bookkeeping", "/commondetail/detail/38253214893569.html?cityid=1&areaid=101");
		repxMap.put("trademark",   "/commondetail/detail/38231039879425.html?cityid=1&areaid=101");
		repxMap.put("change",      "/commondetail/detail/38230152382977.html?cityid=1&areaid=101");
		
		
	}
	
	
	/**
	 * 首页
	 * @return
	 * @throws Exception
	 */
	@Path("/")
	public ActionResult index() throws Exception{
		getFriendLinks(FRIEND_INDEX);
		//动态生成banner－－－start－－－－
		 List<Bannervo> blist = MenuUtils.menuUtils.getBanners();
		 if(blist != null){
			 model().add("bannerlist", blist);
		 }
		//动态生成banner－－－end－－－－
		
		
		
		/**
		 * 默认行业特点+缓存
		 */
		List<LvEnterpriseMainBusinessEntity> mainBusinessList = null;
		Object bus_obj = MemcachedUtil.get("mainBusinessList_");
		if(bus_obj!=null){
			mainBusinessList = (List<LvEnterpriseMainBusinessEntity>) bus_obj;
		}
		if(CollectionUtils.isNotEmpty(mainBusinessList)){
			System.out.println("get-main_bus--from ---cached---");
		}else{
			
			try {
				mainBusinessList = RSBLL.getstance().getEpEnterpriseMainBusinessService().getDefaultMainBusinessList(DEF_MAIN_BUS_SIZE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(mainBusinessList != null){
				MemcachedUtil.set("mainBusinessList_", mainBusinessList,new Date(1000*3600*24));
			}
		}
		if(mainBusinessList != null){
			model().add("mainBusinessList", mainBusinessList);
		}
		
		
		
		model().add("nav", "home");
		//转到的页面路径
		model().add("LocationPage", new LocationPage("myindex/myindex","index"));
		
		// 图形验证码
		CommonUtils.geneCode(beat());
		
		//年报倒计时
		ReportController.getTimer(beat());
		//活动弹窗
		CommonUtils.activeFlag(beat());
		//活动右侧的小图标
		CommonUtils.activeFloatFlag(beat());
		
		//获取一个最高优惠券
		List<PacketVO>   packetList = PreferentialMatchBuz.getInstance().matchBiggestQuotaPacket(CommonUtils.getLoginuserid(beat()),1);
		if(CollectionUtils.isNotEmpty(packetList)){
			PacketVO packetVO = packetList.get(0);
			
			model().add("packetVO", packetVO);
		}
		
		
		//获取网站用户量
		int todayadd = RSBLL.getstance().getLoginService().getCount(" addtime >= "+Timers.stringToMillis(Timers.nowdate()));
		
//		System.out.println("今天新增用户数:"+todayadd);
		
		if(todayadd!=todayadd_){
			todayadd_ = todayadd;
		}
		
		
		int userCount =totalBase+ todayadd_*getRandomOne(5)+getRandomOne(5);
		
		//如果第一次进，设置静态数
		if(userCount_==0){
			userCount_ = userCount;
		}
		//必须是递增的
		if(userCount_<userCount){
			userCount_ = userCount;
		}
		
		
//		System.out.println("用户总数:"+userCount_);
		
		
		//下单用户数目造假为总用户数的2/3
		int orderusers = totalOrderBase + Math.round(todayadd_ * 1 /3 );
		//如果第一次进，设置静态数
		if(orderCount_==0){
			orderCount_ = orderusers;
		}
		//必须是递增的
		if(orderCount_<orderusers){
			orderCount_ = orderusers;
		}
		
//		System.out.println("下单用户总数:"+orderCount_);
		model().add("userCount", userCount_);
		model().add("orderCount", orderCount_);
		
		
		return view("/index");
	}

	
	
	@Path("/order")
	public ActionResult order(){
		return view("/order");
	}
	
	@Path("/detail")
	public ActionResult detail(){
		return view("/detail");
	}
	
	@Path("/changeCity")
	public ActionResult changeCity(){
		return redirect301("http://"+beat().getRequest().getServerName()+"/");
	}
	
	/**
	 * 旧 URL 兼容跳转
	 * @return
	 */
	@Path("/{repx:\\S+}.html")
	public ActionResult hot(String repx){
		
		String rs = repxMap.get(repx);
		
		if(StringUtils.isEmpty(rs)){
			rs = "/";
		}
		
		String param = beat().getRequest().getQueryString();
		
		
		if(StringUtils.isNotEmpty(param)){
			
			rs = "http://"+beat().getRequest().getServerName()+rs+"?"+param;
		}else{
			rs = "http://"+beat().getRequest().getServerName()+rs;
		}
		
		
		
		return  redirect301(rs);
	}


	@Path("/zt-reg.html")
	public ActionResult ztreg(){
		model().add("nav", "zt-reg");
		//转到的页面路径
				model().add("LocationPage", new LocationPage("/zt-reg","index"));
		return view("/index");
	}
	
	
	@Path("/zt-reg2.html")
	public ActionResult ztreg2(){
		model().add("nav", "zt-reg2");
		//转到的页面路径
				model().add("LocationPage", new LocationPage("/zt-reg2","index"));
		return view("/index");
	}
	
	
	@Path("/vips.html")
	public ActionResult vips(){
		model().add("currenturl", "/vips.html");
		model().add("LocationPage", new LocationPage("/vip","index"));
		return view("/index");
	}
	/**
	 * 关于我们
	 * @return
	 */
	@Path("/about.html")
	public ActionResult about(){
		model().add("currenturl", "/about.html");
		//转到的页面路径
				model().add("LocationPage", new LocationPage("/about","index"));
		return view("/index");
	}
	
	/**
	 * 人才招聘
	 * @return
	 */
	@Path("/join.html")
	public ActionResult invite(){
		model().add("currenturl", "/join.html");
		//转到的页面路径
				model().add("LocationPage", new LocationPage("/join","index"));
		return view("/index");
	}
	
	/**
	 * 加盟+联系我们
	 * @return
	 */
	@Path("/contact.html")
	public ActionResult joinus(){
		model().add("currenturl", "/contact.html");
		//转到的页面路径
				model().add("LocationPage", new LocationPage("/contact","index"));
		return view("/index");
	}
	
	/**
	 * 发展历程
	 * @return
	 */
	@Path("/history.html")
	public ActionResult contact(){
		model().add("currenturl", "/history.html");
		//转到的页面路径
		model().add("LocationPage", new LocationPage("/history","index"));
		return view("/index");
	}
	@Path("/imgurl/{imgid:\\d+}")
	public ActionResult imgurl(long imgid){
		String maxLength = request().getParameter("maxLength");
		DicFileEntity fileEntity = FileDownloadBuz.getInstance().getLvFileEntity(imgid+"", maxLength);
		if (fileEntity != null) {
			return ActionResultUtils.renderFile(fileEntity.getFileData(), fileEntity.getFileName());
		}
		return ActionResultUtils.renderJson("");
	}

	
	//一元法律咨询

	@Path("/law/law.html")
	public ActionResult law(){
		//model().add("nav", "law");
		//转到的页面路径
				model().add("LocationPage", new LocationPage("law","index"));
		return view("/index");
	}
	

	//海淀专题页
	@Path("/hd/hdtc.html")
	public ActionResult hdtc(){
		//pid设置
		model().add("productid", "38229817543169");
		//日期推算
		try {
			
			Date date = WorkDayUtil.getDeadLineDate(new Date(), 25);
			String endday = Timers.parseDate2Day(date);
			model().add("endday", endday);
		} catch (Exception e) {
			// TODO: handle exception
		}
		//转到的页面路径
		model().add("LocationPage", new LocationPage("/hdtc","index"));
		return view("/index");
	}
	//////////////////////////////////////////////////////////////////////////
	//公司注册落地页
	@Path("/ldy/companyRegLdy.html")
	public ActionResult companyRegLdy(){
		int phoneFlag = StringUtils.isBlank(request().getParameter("p")) ? 0 : Integer.valueOf(request().getParameter("p")) ;
		model().add("ldy_phone", getPhoneNumber(phoneFlag));
		//转到的页面路径
		model().add("LocationPage", new LocationPage("/companyRegLdy","index"));
		return view("/companyRegLdy");
	}
	
	//公司注册落地页 电话咨询 - 钉钉消息
	@Path("/ldy/sendDingDingMsg")
	public ActionResult sendDingDingMsg(){
		String phone = request().getParameter("phone");
		int type = request().getParameter("type") == null ? 0 : Integer.parseInt(request().getParameter("type"));
		
		String content = "";
		String dingdingCode = "";
		if (type == 1) {
			dingdingCode = "chatba99ba1ac9052105cc357fe4295be32b";
			content = "您有一个公司注册＋地址套餐新客户，电话号码为："+phone+",请尽快催单！";
		} else if (type == 2) {
			String msg = request().getParameter("msg");

			dingdingCode = "chat9ed9ffae5639b9c8d3b596418baaf97a";
			if (StringUtils.isNotBlank(msg)) {
				content = "您有一个商标查询新客户，电话号码为："+ phone + ",查询关键字为：“" + msg + "”。请尽快催单！";
			} else {
				content = "您有一个商标注册新客户，电话号码为："+phone+"，请尽快催单！";
			}
			
			
		} else if (type == 3) {
			dingdingCode = "chatba99ba1ac9052105cc357fe4295be32b";
			content = "您有一个公司变更新客户，电话号码为："+phone+",请尽快催单！";
			
		} else if (type == 4) {
			dingdingCode = "chatba99ba1ac9052105cc357fe4295be32b";
			content = "您有一个代理记账新客户，电话号码为："+phone+",请尽快催单！";
			
		} else if (type == 5) {
			dingdingCode = "chatba99ba1ac9052105cc357fe4295be32b";
			content = "您有一个刻章新客户，电话号码为："+phone+",请尽快催单！";
			
		} else {
			dingdingCode = "chatba99ba1ac9052105cc357fe4295be32b";
			content = "您有一个公司注册新客户，电话号码为："+ phone + ",请尽快催单！";
		} 
				
		boolean sendFlag = DingdingUtils.sendDingding(dingdingCode, "01555218071881", content);
		
		return ActionResultUtils.renderText("{\"ret\":\""+sendFlag+"\"}");
	}
	
	/**
	 * @desc 随机取出一个数【size 为  10 ，取得类似0-9的区间数】
	 * @return
	 */
	public static Integer getRandomOne(int max){
		
		
		Random ramdom =  new Random();
		int number = -1;
		
		//size 为  10 ，取得类似0-9的区间数
		number = Math.abs(ramdom.nextInt() % max  );
		
		return number;
    
	}
	
	
	//扶持计划
    //@date 2016-08-08
	@Path("/supportpj.html")
	public ActionResult support(){
		

		return view("/supportpj/supportpj");
	}
	
	//地址加注册  
    //@date 2016-08-11
	@Path("/ldy/tc.html")
	public ActionResult tcActive(){
		int phoneFlag = StringUtils.isBlank(request().getParameter("p")) ? 0 : Integer.valueOf(request().getParameter("p")) ;
		model().add("ldy_phone", getPhoneNumber(phoneFlag));
		//转到的页面路径
		model().add("LocationPage", new LocationPage("/tcactive/zctc_land","index"));
		return view("/tcactive/zctc_land");
	}
	
	
	@Path("/ldy/shangbiao.html")
	public ActionResult sbActive(){
		int phoneFlag = StringUtils.isBlank(request().getParameter("p")) ? 7549 : Integer.valueOf(request().getParameter("p")) ;
		model().add("ldy_phone", getPhoneNumber(phoneFlag));
		//转到的页面路径
		model().add("LocationPage", new LocationPage("/ldy/sb_land","index"));
		return view("/ldy/sb_land");
	}
	
	@Path("/ldy/biangeng.html")
	public ActionResult bgActive(){
		
		
		int phoneFlag = StringUtils.isBlank(request().getParameter("p")) ? 0 : Integer.valueOf(request().getParameter("p")) ;
		model().add("ldy_phone", getPhoneNumber(phoneFlag));
		
		//转到的页面路径
		model().add("LocationPage", new LocationPage("/ldy/bg_land","index"));
		return view("/ldy/bg_land");
	}
	
	
	//三证合一
	@Path("/merge.html")
	public ActionResult merge(){
		int phoneFlag = StringUtils.isBlank(request().getParameter("p")) ? 0 : Integer.valueOf(request().getParameter("p")) ;
		model().add("ldy_phone", getPhoneNumber(phoneFlag));
		//转到的页面路径
		model().add("LocationPage", new LocationPage("/merge/merge","index"));
		return view("/index");
	}
	
	//公司注册  caike 使用
	@Path("/ldy/companyRegLdy_ck.html")
	public ActionResult companyRegLdy_ck(){
		//转到的页面路径
		model().add("LocationPage", new LocationPage("/companyRegLdy_ck","index"));
		return view("/companyRegLdy_ck");
	}
	
	@Path("/ldy/book.html")
	public ActionResult book(){
		int phoneFlag = StringUtils.isBlank(request().getParameter("p")) ? 0 : Integer.valueOf(request().getParameter("p")) ;
		model().add("ldy_phone", getPhoneNumber(phoneFlag));
		//转到的页面路径
		model().add("LocationPage", new LocationPage("/ldy/book_land","index"));
		return view("/ldy/book_land");
	}
	
	@Path("/ldy/kezhang.html")
	public ActionResult kz(){
		int phoneFlag = StringUtils.isBlank(request().getParameter("p")) ? 0 : Integer.valueOf(request().getParameter("p")) ;
		model().add("ldy_phone", getPhoneNumber(phoneFlag));
		//转到的页面路径
		model().add("LocationPage", new LocationPage("/ldy/kz_land","index"));
		return view("/ldy/kz_land");
	}
	
	@Path("/ldy/midautumn.html")
	public ActionResult midautumn(){
		//转到的页面路径
		model().add("LocationPage", new LocationPage("/ldy/mid_land","index"));
		return view("/ldy/mid_land");
	}
	
	
	private String getPhoneNumber(int flag) {
		if (flag == 7545) {
			return "80697545";
		} else if (flag == 7543) {
			return "80697543";
		} else if (flag == 7549) {
			return "80697549";
		}
		return "80697544";
	}
}
