package com.jx.hunter.lvzhengpc.controllers;

import java.util.List;

import com.jx.argo.ActionResult;
import com.jx.argo.controller.AbstractController;
import com.jx.argo.route.ActionMonitor;
import com.jx.argo.utils.GuiceUtils;
import com.jx.blackface.gaea.usercenter.entity.BFLoginEntity;
import com.jx.blackface.gaea.usercenter.utils.CookieUtils;
import com.jx.hunter.lvzhengpc.annotaion.TracePoint;
import com.jx.hunter.lvzhengpc.annotaion.impl.ActionMonitorImpl;
import com.jx.hunter.lvzhengpc.common.MemcachedUtil;
import com.jx.hunter.lvzhengpc.frame.RSBLL;
import com.jx.hunter.lvzhengpc.utils.MenuUtils;
import com.jx.hunter.lvzhengpc.vo.MenuShowVO;
import com.jx.service.newcore.contract.IFriendLinkService;
import com.jx.service.newcore.entity.FriendLinkEntity;
@TracePoint
//@RequestMonitor
public class BaseController extends AbstractController{
	static {
		//初始化注册绑定性能监控实现
		GuiceUtils.bandingInjector(ActionMonitor.class, ActionMonitorImpl.class);
	}
	
	
	protected static int FRIEND_INDEX=11;//首页
	protected static int FRIEND_LIST=12;//列表页
	
	/**
	 * 友情链接
	 * @param includepage
	 */
	protected void getFriendLinks(int includepage){
		IFriendLinkService friendservice=RSBLL.getstance().getFriendLinkService();
		String condition="status = 1 and showtype=0 and inclupage="+includepage;
		List<FriendLinkEntity> friendlist=null;
		try {
			friendlist=friendservice.getFriendLinkEntity(condition, 1, 99, "updatetime desc");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if( friendlist != null && friendlist.size()>0 ){
			model().add("friendlinks", friendlist);
		}
	}
	@Override
	protected ActionResult redirect(String redirectUrl) {
		return super.redirect(redirectUrl);
	}

	public ActionResult view(String view){
		long uid = CookieUtils.getUseridFromCookie(beat().getRequest());
		BFLoginEntity bfe = null;
		if(uid > 0){
			try {
				bfe = RSBLL.getstance().getLoginService().getLoginEntityById(uid);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(null != bfe){
			model().add("logen", bfe);
		}
		
		try {
			//把菜单填充到map
//			model().add("one_", MenuUtils.menuUtils.getMenuOneByCached());
//			model().add("one", MenuUtils.menuUtils.getMenuTwoByCached());
			//先从缓存加载,没有再去数据库取
			List<MenuShowVO> menulist = (List<MenuShowVO>) MemcachedUtil.get("menulist");
			if(null==menulist||menulist.isEmpty()||menulist.size()==0){
				menulist = MenuUtils.menuUtils.getMenuList();
			}
			model().add("menulist", menulist);
		} catch (Exception e) {
			System.out.println("菜单加载失败!");
			e.printStackTrace();
		}
		
		return super.view(view);
	}
	
//	/***
//	 * 发送手机验证码
//	 * @return 1发送成功 2发送的语音验证码 -1发送失败 error 获取手机号失败
//	 */
//	@Path("/common/sendPhoneCode")
//	public ActionResult sendPhoneCode(){
//		String phoneNum = beat().getRequest().getParameter("phoneNum")==null?"":WAQ.forXSS().HTMLEncode(beat().getRequest().getParameter("phoneNum"));
//		if(StringUtils.isBlank(phoneNum)){
//			return ActionResultUtils.renderJson("{\"error\":}");
//		}
//		try {
//			MobileSmsResultExt  result = RSBLL.getstance().getMoblieSmsService().sendVerifyCode(phoneNum);
//			System.out.println("********************"+result.getCode()+"==="+result.getMsg()+"==="+result.isResult());
//			if(result.isResult()){
//				if(result.getCode() == 2){
//					return ActionResultUtils.renderJson("{\"flag\":\"2\"}");
//				}
//				return ActionResultUtils.renderJson("{\"flag\":\"1\"}");
//	        }
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ActionResultUtils.renderJson("{\"flag\":\"-1\"}");
//		}
//		return ActionResultUtils.renderJson("{\"flag\":\"-1\"}");
//	}
//	
//	/***
//	 * 校验手机发送的验证码是否正确
//	 * @return true正确 false 错误 error 手机或验证码为空
//	 */
//	@Path("/common/checkPhoneAndCode")
//	public ActionResult checkPhoneAndCode(){
//		String phoneNum = beat().getRequest().getParameter("phoneNum")==null?"":WAQ.forXSS().HTMLEncode(beat().getRequest().getParameter("phoneNum"));
//		String code = beat().getRequest().getParameter("code")==null?"":WAQ.forXSS().HTMLEncode(beat().getRequest().getParameter("code"));
//		
//		if(StringUtils.isBlank(phoneNum) || StringUtils.isBlank(code)){
//			return ActionResultUtils.renderJson("{\"error\":}");
//		}
//		try {
//			Boolean  result = RSBLL.getstance().getMoblieSmsService().checkVerifyCode(phoneNum, code);
//			if(result){
//				return ActionResultUtils.renderJson("{\"success\":\"true\"}");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("调用校验手机和验证码方法：checkVerifyCode失败手机号:" + phoneNum +"验证码:"+code);
//		}
//		return ActionResultUtils.renderJson("{\"success\":\"false\"}");
//	}
	
	
}
