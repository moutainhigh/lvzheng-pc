package com.jx.hunter.lvzhengpc.annotaion.impl;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jx.argo.ActionResult;
import com.jx.argo.BeatContext;
import com.jx.argo.interceptor.AllInterceptor;
import com.jx.argo.jxlifecycle.Lifecycle;
import com.jx.blackface.tools.blackTrack.TrackLogLocalRecord;

public class RequestMonitorImpl implements AllInterceptor {
	
	private static String DEFAULT_LOG_NAME = "lvzhengpcmonitor";
	
	@Override
	public ActionResult preExecute(BeatContext beat) {
		// TODO Auto-generated method stub
		System.out.println("-------pre 1");
		return null;
	}

	@Override
	public ActionResult postExecute(BeatContext beat, ActionResult executionResult) {
		try {
			
			Lifecycle life = beat.getLifecycle();
			JSONObject json = (JSONObject)JSON.toJSON(life);
			json.put("request", getUrl(beat.getRequest()));
			json.put("method", beat.getRequest().getMethod());
			TrackLogLocalRecord.log(DEFAULT_LOG_NAME, json.toJSONString());	
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return executionResult;
	}

	private String getUrl(HttpServletRequest request) {
		String requestURL = String.valueOf(request.getRequestURL());
		String queryString = request.getQueryString();
		if (StringUtils.isNotBlank(queryString)) {
			requestURL += "?" + queryString;
		}
		requestURL = StringUtils.replace(requestURL, "http://", "");
		requestURL = StringUtils.replace(requestURL, "https://", "");
		return requestURL;
	}

}
