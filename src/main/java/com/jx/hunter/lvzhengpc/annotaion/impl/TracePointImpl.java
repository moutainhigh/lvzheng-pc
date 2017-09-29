package com.jx.hunter.lvzhengpc.annotaion.impl;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jx.argo.ActionResult;
import com.jx.argo.BeatContext;
import com.jx.argo.interceptor.PreInterceptor;
import com.jx.blackface.tools.blackTrack.TrackLogUtils;
import com.jx.blackface.tools.blackTrack.entity.TrackInfoEntity;
import com.jx.hunter.lvzhengpc.common.CommonUtils;
import com.jx.hunter.lvzhengpc.utils.CookieUtils;

public class TracePointImpl implements PreInterceptor {

	private static String COOKIE_NAME_USER_ID = "lvuser";
	private static String DEFAULT_MODULE = "web_access";
	private static String DEFAULT_LOG_NAME = "lvzhengpcweb";
	
	@Override
	public ActionResult preExecute(BeatContext beat) {
		
		HttpServletRequest request = beat.getRequest();
		HttpServletResponse response = beat.getResponse();
		TrackInfoEntity trackLog = TrackLogUtils.getTrackLog(request, response);
		
		String userId = CommonUtils.getUserIdFormCookie(COOKIE_NAME_USER_ID, request);
		// 模块名称
		trackLog.setMoudle(DEFAULT_MODULE);
		// 用户ID
		trackLog.setUserId(userId);
		
		TrackLogUtils.removeLog(DEFAULT_LOG_NAME);
		//System.out.println("------嘿嘿-------");
		return null;
	}
	

}
