package com.jx.hunter.lvzhengpc.controllers;

import com.jx.argo.ActionResult;
import com.jx.argo.annotations.Path;
import com.jx.blackface.gaea.usercenter.utils.ActionResultUtils;
import com.jx.hunter.lvzhengpc.common.CommonUtils;
import com.jx.hunter.lvzhengpc.common.LocationPage;


@Path("contractTemplate")
public class ContractTemplateController extends BaseController{
	
	@Path("index.html")
	public ActionResult index(){//跳转到公司模板下载页
		//转到的页面路径
		model().add("LocationPage", new LocationPage("contractTemplate/contractTemplate","index"));
		return view("/index");
	}
	
	
	@Path("checkLogin")
	public ActionResult checkLogin(){//登陆成功后才会进入此方法，否则会跳转到登陆页面
		//如果存在此cookie则不跳转
		if(CommonUtils.checkCookieName("lvuser", beat().getRequest())){
			return ActionResultUtils.renderJson("{\"res\":\"true\"}");
		}
		
		return ActionResultUtils.renderJson("{\"res\":\"false\"}"); 
	}

}
