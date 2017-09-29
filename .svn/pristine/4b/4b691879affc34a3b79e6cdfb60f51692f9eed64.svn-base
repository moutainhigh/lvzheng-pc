package com.jx.hunter.lvzhengpc.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.jx.argo.ActionResult;
import com.jx.argo.annotations.Path;
import com.jx.blackface.gaea.usercenter.contract.ILoginService;
import com.jx.blackface.gaea.usercenter.entity.BFLoginEntity;
import com.jx.blackface.gaea.usercenter.utils.ActionResultUtils;
import com.jx.hunter.lvzhengpc.common.CommonUtils;
import com.jx.hunter.lvzhengpc.common.LocationPage;
import com.jx.hunter.lvzhengpc.common.PC_Constants;
import com.jx.hunter.lvzhengpc.frame.RSBLL;
import com.jx.hunter.lvzhengpc.utils.CookieUtils;
import com.jx.service.messagecenter.entity.MobileSmsResultExt;
import com.jx.service.preferential.plug.buz.PreferentialFetchBuz;

import net.sf.json.JSONObject;
/**
 * @author bruce
 * @date 2016-07-19
 * 新用户享优惠
 *
 */
@Path("/gift")
public class DrawGiftController extends BaseController{
	
	public static final Long ppid = 40841276016129L;
	
	public static int COOKIE_TIME = 60 * 60 * 24;
	
	public static final List<Long> ppid_onlie = new ArrayList<Long>();
	static{
		ppid_onlie.add(40867018845953L);
		ppid_onlie.add(40866986493953L);
		ppid_onlie.add(40866867851009L);
		ppid_onlie.add(40866777148161L);
		ppid_onlie.add(40866676281089L);
	}
	
	
	
	
	/**
	 * 跳转手机和验证码弹窗内容
	 * @return
	 */
	@Path("/draw")
	public ActionResult detail(){
		
		try {
			//获取token
			CommonUtils.geneCode(beat());
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return view("/gift/giftdata");
	}
	/**
	 * 展示发送的红包
	 * @return
	 */
	@Path("/showgift")
	public ActionResult showgift(){
		
//		String uid = beat().getRequest().getParameter("uid");
//		try {
//			if(StringUtils.isNotEmpty(uid)){
//				
//				List<AccountVO> volist = PreferentialAccountBuz.getInstance().getUserCouponsByStatus(Long.valueOf(uid), 0,1,99,"quota desc");
//				
//				if(CollectionUtils.isNotEmpty(volist)){
//					model().add("volist", volist);
//				}
//			}
//		
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
		//转到的页面路径
		model().add("LocationPage", new LocationPage("/gift/drawgift","index"));
		
		return view("index");
	}
	/**
	 * 判断手机号是否存在
	 * @return
	 */
	@Path("/phoneFlag")
	public ActionResult phoneFlag(){
		
		String rs = "0";
		try {
			
			String phoneNum = beat().getRequest().getParameter("phoneNum");
			List<BFLoginEntity> list = RSBLL.getstance().getLoginService().getLoginEntity(" userphone = '"+phoneNum+"' ", 1, 1, "");
			if(CollectionUtils.isNotEmpty(list)){
				if(list.size()>0){
					rs = "1";
				}
			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return ActionResultUtils.renderText(rs);
	}
	
	/**
	 * 领取优惠券
	 * @return
	 */
	@Path("/receive")
	public ActionResult receive(){
		
		ILoginService ls = RSBLL.getstance().getLoginService();
		String tokenstr = beat().getRequest().getParameter("token");
		String userphone = beat().getRequest().getParameter("userphone");
		String validatecode = beat().getRequest().getParameter("validatecode");
		String phonecode = beat().getRequest().getParameter("phonecode");
		
		JSONObject jr = new JSONObject();
		if(null == tokenstr || "".equals(tokenstr)){
			jr.put("ret", "fail");
			jr.put("msg", "图形验证码失败！请刷新后重试!");
			
			return ActionResultUtils.renderJson(jr.toString());
		}
		
		if(validatecode == null || "".equals(validatecode)){
			
			jr.put("ret", "fail");
			jr.put("msg", "图形验证码错误!");
			
			return ActionResultUtils.renderJson(jr.toString());
		}else{
			String sevali = (String) beat().getRequest().getSession().getAttribute("valicode"+tokenstr);
			if(!StringUtils.equalsIgnoreCase(sevali,validatecode)){
				jr.put("ret", "fail");
				jr.put("msg", "图形验证码错误!");
				
				return ActionResultUtils.renderJson(jr.toString());
			}
			
		}
		if(userphone != null && !"".equals(userphone)){
			String condition = "userphone='"+userphone+"'";
			List<BFLoginEntity> list = null;
			try {
				list = ls.getLoginEntity(condition, 1, 1, "userid");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(null != list && list.size() > 0){
				jr.put("ret", "fail");
				jr.put("msg", "电话已经存在！");
				return ActionResultUtils.renderJson(jr.toString());
			}
		}
		
		try {
			if(phonecode == null || "".equals(phonecode)){
				jr.put("ret", "fail");
				jr.put("msg", "手机验证码错误");
				return ActionResultUtils.renderJson(jr.toString());
			}else if(!RSBLL.getstance().getMoblieSmsService().checkVerifyCode(userphone, phonecode)){
				jr.put("ret", "fail");
				jr.put("msg", "手机验证码错误");
				return ActionResultUtils.renderJson(jr.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		//添加新用户
		
		long userid = 0;
		
		BFLoginEntity bf = new BFLoginEntity();
		bf.setUserphone(userphone);
		bf.setUsername(userphone);
		bf.setAddtime(new Date().getTime());
		bf.setChannel(PC_Constants.Channel.GITF_PC.key);
		
		try {
			userid = ls.addLoginEntity(bf);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(userid > 0){
			int cookieTime = 60 * 60 * 24;
			// 如果选中了记住我则cookie保存7天
			try {
				CookieUtils.saveCookie(userid, response(), cookieTime);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//分配礼包
			try {
				
//				long fetchResult = PreferentialFetchBuz.getInstance().fetchPacket(userid, ppid);
//				
//				if(fetchResult<0){
//					jr.put("ret", "ok");
//					jr.put("msg", "领取红包异常!");
//				}else if(fetchResult==1){
//					jr.put("ret", "ok");
//					jr.put("msg", "已发送红包!");
//				}else if(fetchResult==2){
//					jr.put("ret", "ok");
//					jr.put("msg", "您已领取过该红包!");
//				}
				
				//多个发送
				for (int i = 0; i < ppid_onlie.size(); i++) {
					long fetchResult = PreferentialFetchBuz.getInstance().fetchPacket(userid, ppid_onlie.get(i));
					if(fetchResult<0){
						jr.put("ret", "ok");
						jr.put("msg", "领取红包异常!");
					}else if(fetchResult==1){
						jr.put("ret", "ok");
						jr.put("msg", "已发送红包!");
					}else if(fetchResult==2){
						jr.put("ret", "ok");
						jr.put("msg", "您已领取过该红包!");
					}
					
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			jr.put("uid", userid);
			jr.put("ret", "ok");
			
			
		}else{
			jr.put("ret", "fail");
			jr.put("msg", "添加用户失败!");
		}
		
		
		
		return ActionResultUtils.renderJson(jr.toString());
		
		
		
	}
	
	
	
	////////////////以下为PC端----------引入手机端的活动代码-------------------------------------------------------------------------
	
	/**
	 * PC输入验证码
	 * @return
	 */
	@Path("/pcgift")
	public ActionResult pcgift(){
		
		try {
			//获取token
			CommonUtils.geneCode(beat());
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return view("/gift/pc_vipred");
	}
	
	
	@Path("/midActiveGet")
	public ActionResult midActiveGet(){
		
		
		ILoginService loginService = RSBLL.getstance().getLoginService();
		String userphone = beat().getRequest().getParameter("phoneNum");
		String phonecode = beat().getRequest().getParameter("usercode");
		JSONObject jr = new JSONObject();
		
		//参数为空
		if (StringUtils.isBlank(userphone) || StringUtils.isBlank(phonecode)) {
			jr.put("code", "-1");
			return ActionResultUtils.renderJson(jr.toString());
		}
		
		try {
			//验证码错误
			if(!RSBLL.getstance().getMoblieSmsService().checkVerifyCode(userphone, phonecode)){
				jr.put("code", "-2");
				return ActionResultUtils.renderJson(jr.toString());
			}
			
			
			String condition = "userphone='"+userphone+"'";
			List<BFLoginEntity> list =  loginService.getLoginEntity(condition, 1, 1, "userid");

			//用户存在，老用户
			if (!CollectionUtils.isEmpty(list)) {
				long userid  = list.get(0).getUserid();
				CookieUtils.saveCookie(userid, response(), COOKIE_TIME);
				jr.put("code", "1");
				return ActionResultUtils.renderJson(jr.toString());
			}
			
			
			//添加新用户
			long userid = 0;
			BFLoginEntity bf = new BFLoginEntity();
			bf.setUserphone(userphone);
			bf.setUsername(userphone);
			bf.setAddtime(new Date().getTime());
			bf.setChannel(PC_Constants.Channel.GITF_MID.key);
			userid = loginService.addLoginEntity(bf);
			
			//添加新用户失败
			if (userid <= 0) {
				jr.put("code", "-3");
				return ActionResultUtils.renderJson(jr.toString());
			}
			
			
			CookieUtils.saveCookie(userid, response(), COOKIE_TIME);
			jr.put("code", "2");
			return ActionResultUtils.renderJson(jr.toString());
			
		} catch (Exception e) {
			// TODO: handle exception
			jr.put("code", "-4");
			return ActionResultUtils.renderJson(jr.toString());

		}
		
	}
	

	/**
	 * pc领取优惠券
	 * @return
	 */
	@Path("/pcreceive")
	public ActionResult pcreceive(){
		
		ILoginService ls = RSBLL.getstance().getLoginService();
		String userphone = beat().getRequest().getParameter("phoneNum");
		String phonecode = beat().getRequest().getParameter("usercode");
		
		JSONObject jr = new JSONObject();
		if(userphone != null && !"".equals(userphone)){
			String condition = "userphone='"+userphone+"'";
			List<BFLoginEntity> list = null;
			try {
				list = ls.getLoginEntity(condition, 1, 1, "userid");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(null != list && list.size() > 0){
				jr.put("ret", "fail");
				jr.put("msg", "电话已经存在！");
				return ActionResultUtils.renderJson(jr.toString());
			}
		}
		
		try {
			if(phonecode == null || "".equals(phonecode)){
				jr.put("ret", "fail");
				jr.put("msg", "手机验证码错误");
				return ActionResultUtils.renderJson(jr.toString());
			}else if(!RSBLL.getstance().getMoblieSmsService().checkVerifyCode(userphone, phonecode)){
				jr.put("ret", "fail");
				jr.put("msg", "手机验证码错误");
				return ActionResultUtils.renderJson(jr.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		//添加新用户
		
		long userid = 0;
		
		BFLoginEntity bf = new BFLoginEntity();
		bf.setUserphone(userphone);
		bf.setUsername(userphone);
		bf.setAddtime(new Date().getTime());
		bf.setChannel(PC_Constants.Channel.GITF_EMAIL.key);
		
		try {
			userid = ls.addLoginEntity(bf);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(userid > 0){
			int cookieTime = 60 * 60 * 24;
			// 如果选中了记住我则cookie保存7天
			try {
				CookieUtils.saveCookie(userid, response(), cookieTime);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//分配礼包
			try {
//				long fetchResult = PreferentialFetchBuz.getInstance().fetchPacket(userid, ppid);
//				if(fetchResult<0){
//					jr.put("ret", "ok");
//					jr.put("msg", "领取红包异常!");
//				}else if(fetchResult==1){
//					jr.put("ret", "ok");
//					jr.put("msg", "已发送红包!");
//				}else if(fetchResult==2){
//					jr.put("ret", "ok");
//					jr.put("msg", "您已领取过该红包!");
//				}
//				
				//多个发送
				for (int i = 0; i < ppid_onlie.size(); i++) {
					long fetchResult = PreferentialFetchBuz.getInstance().fetchPacket(userid, ppid_onlie.get(i));
					if(fetchResult<0){
						jr.put("ret", "ok");
						jr.put("msg", "领取红包异常!");
					}else if(fetchResult==1){
						jr.put("ret", "ok");
						jr.put("msg", "已发送红包!");
					}else if(fetchResult==2){
						jr.put("ret", "ok");
						jr.put("msg", "您已领取过该红包!");
					}
					
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			jr.put("uid", userid);
			jr.put("ret", "ok");
			
			
		}else{
			jr.put("ret", "fail");
			jr.put("msg", "添加用户失败!");
		}
		
		
		
		return ActionResultUtils.renderJson(jr.toString());
		
		
		
	}
	
	
	/**
	 * 发送手机验证码||没有图像验证码的
	 * @return
	 */
	@Path("/pcsendPhoneCode")
	public ActionResult sendPhoneCode(){
			
		
		String phoneNum = beat().getRequest().getParameter("phoneNum")==null?"":beat().getRequest().getParameter("phoneNum");
		
		String tokenstr = beat().getRequest().getParameter("tokenstr");
		
		String sessiontoken = (String) beat().getRequest().getSession().getAttribute("token");
		
		if(!sessiontoken.equals(tokenstr)){
			return ActionResultUtils.renderJson("{\"tokenerror\":\"1\"}");
		}else{
			//刷新token
			CommonUtils.geneCode(beat());
		}
		
		
		if(StringUtils.isBlank(phoneNum)){
			return ActionResultUtils.renderJson("{\"error\":}");
		}
		try {
			MobileSmsResultExt  result = RSBLL.getstance().getMoblieSmsService().sendVerifyCode(phoneNum);
			System.out.println("**********"+phoneNum+"**********"+result.getCode()+"==="+result.getMsg()+"==="+result.isResult());
			if(result.isResult()){
				if(result.getCode() == 2){
					return ActionResultUtils.renderJson("{\"flag\":\"2\"}");
				}
				return ActionResultUtils.renderJson("{\"flag\":\"1\"}");
	        }
		} catch (Exception e) {
			e.printStackTrace();
			return ActionResultUtils.renderJson("{\"flag\":\"-1\"}");
		}
		
		return ActionResultUtils.renderJson("{\"flag\":\"-1\"}");
	
	}
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	
}

