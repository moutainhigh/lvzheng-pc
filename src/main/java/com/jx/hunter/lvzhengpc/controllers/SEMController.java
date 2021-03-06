package com.jx.hunter.lvzhengpc.controllers;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jx.argo.ActionResult;
import com.jx.argo.annotations.Path;
import com.jx.blackface.gaea.usercenter.entity.BFLoginEntity;
import com.jx.hunter.lvzhengpc.common.CommonUtils;
import com.jx.hunter.lvzhengpc.common.LocationPage;
import com.jx.hunter.lvzhengpc.frame.RSBLL;
import com.jx.hunter.lvzhengpc.service.SEMService;
import com.jx.hunter.lvzhengpc.utils.ActionResultUtils;
import com.jx.hunter.lvzhengpc.utils.MD5;
import com.jx.tools.waq.WAQ;

/***
 * SEMcontroller老官网导流页面
 * @author duxiaofei
 * @date   2016年3月22日
 */
@Path("/sem")
public class SEMController extends BaseController{
	/**
	 * 访问SEM落地页
	 * @return
	 */
	@Path("/index.html")
	public ActionResult gotoIndex(){
		CommonUtils.geneCode(beat());
		return view("sem/land");
	}
	
	/**
	 * 提交默认的(公司注册)订单
	 * @return
	 */
	@Path("/submitOrder.html")
	public ActionResult submitOrder(){
		String userphone = beat().getRequest().getParameter("userphone")==null?"":WAQ.forXSS().HTMLEncode(beat().getRequest().getParameter("userphone"));
		String username  = beat().getRequest().getParameter("username")==null?"": WAQ.forXSS().HTMLEncode(beat().getRequest().getParameter("username"));
		String validatecode  = beat().getRequest().getParameter("validatecode")==null?"": WAQ.forXSS().HTMLEncode(beat().getRequest().getParameter("validatecode"));
		String tokenstr  = beat().getRequest().getParameter("tokenstr")==null?"": WAQ.forXSS().HTMLEncode(beat().getRequest().getParameter("tokenstr"));

		if(StringUtils.isBlank(userphone)){
			return ActionResultUtils.renderJson("{\"error:\"}");
		}
		
		if(tokenstr == null || "".equals(tokenstr)){
			return ActionResultUtils.renderJson("{\"success\":false,\"msg\":\"生成的图形验证码又问题，请刷新页面重试！\"}");
		}
		
		if(StringUtils.isNotBlank(validatecode)){
			String sevali = (String) beat().getRequest().getSession().getAttribute("valicode"+tokenstr);
			if(!StringUtils.equalsIgnoreCase(sevali,validatecode)){
				return ActionResultUtils.renderJson("{\"success\":false,\"msg\":\"图形验证码错误\"}");
			}
		}
		
		BFLoginEntity loginEntity = null;
		try {
			List<BFLoginEntity> loginEntityList = RSBLL.getstance().getLoginService().getLoginEntity("userphone='"+userphone+"'", 1, 1, null);
			if(null != loginEntityList && !loginEntityList.isEmpty()){
				loginEntity = loginEntityList.get(0);
			}else{
				loginEntity = new BFLoginEntity();
				String password = userphone.substring(userphone.length() - 6, userphone.length());
				loginEntity.setUserphone(userphone);
				loginEntity.setAddtime(new Date().getTime());
				loginEntity.setChannel(4);
				loginEntity.setPassword(MD5.sign(password, "utf-8"));
				if(StringUtils.isBlank(username)){
					loginEntity.setUsername(userphone);
				}else{
					loginEntity.setUsername(username);
				}
				long userid = RSBLL.getstance().getLoginService().addLoginEntity(loginEntity);
				loginEntity.setUserid(userid);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			if(null != loginEntity){
				SEMService.getStance().addSEMOrderByLvzheng(loginEntity.getUserid());
				return ActionResultUtils.renderJson("{\"success\":true}");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ActionResultUtils.renderJson("{\"error:\"}");
	}
	
	
	
	
	

	/**
	 * 首页banner-suit
	 * @return
	 */
	@Path("/suit.html")
	public ActionResult suit(){
		
		//转到的页面路径
				model().add("LocationPage", new LocationPage("/sem/suit","index"));
		return view("/index");
	}
	

}
