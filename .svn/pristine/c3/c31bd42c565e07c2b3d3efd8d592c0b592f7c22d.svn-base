package com.jx.hunter.lvzhengpc.annotaion.impl;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jx.argo.ActionResult;
import com.jx.argo.BeatContext;
import com.jx.argo.internal.actionresult.StaticActionResult;
import com.jx.argo.jxlifecycle.Lifecycle;
import com.jx.argo.route.ActionMonitor;
import com.jx.blackface.tools.blackTrack.TrackLogLocalRecord;

public class ActionMonitorImpl implements ActionMonitor {
	
	private static String DEFAULT_LOG_NAME = "lvzhengpcmonitor";
	
	@Override
	public void output(BeatContext beat,ActionResult result) {
		
		try {
			
			//静态请求跳过
			if (result instanceof StaticActionResult.DefaultStaticResult) {
				return ;
			}
			//System.out.println("啦啦啦啦啦啦，哈哈哈哈哈哈，嘿嘿嘿嘿嘿嘿");
			
			Lifecycle life = beat.getLifecycle();
			if (StringUtils.contains(life.getRequest(), "makephonecode.html")) {
				return;
			}
			JSONObject json = (JSONObject)JSON.toJSON(life);
			TrackLogLocalRecord.log(DEFAULT_LOG_NAME, json.toJSONString());	
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
