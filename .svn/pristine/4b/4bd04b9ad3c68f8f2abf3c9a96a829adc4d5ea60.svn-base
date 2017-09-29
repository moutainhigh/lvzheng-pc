/**
 * 
 */

package com.jx.hunter.lvzhengpc.annotaion.impl;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.jx.argo.ActionResult;
import com.jx.argo.BeatContext;
import com.jx.argo.interceptor.AllInterceptor;
import com.jx.argo.internal.VelocityViewFactory.VelocityViewResult;
import com.jx.hunter.lvzhengpc.common.MemcachedUtil;
import com.jx.hunter.lvzhengpc.utils.ActionResultUtils;
import com.jx.hunter.lvzhengpc.utils.MD5;



/**
 * simple introduction
 *
 * <p>detailed comment</p>
 * @author chuxuebao 2016年4月28日
 * @see
 * @since 1.0
 */

public class AddMemcacheImpl implements AllInterceptor {
	
	private String pre_suff = "page_annotation_";
	
	@Override
	public ActionResult preExecute(BeatContext beat) {
		String memKey = buildMemKey(beat);
		Object object = MemcachedUtil.get(memKey);
		if(object != null){
			System.out.println("获得到了缓存， memKey=" + memKey);
			String content = String.valueOf(object);
			return ActionResultUtils.renderHtml(content);
		}
		System.out.println("未得到了缓存， memKey=" + memKey);
		return null;
	}

	/**
	 * @param beat
	 * @return
	 */
	private String buildMemKey(BeatContext beat) {
		HttpServletRequest request = beat.getRequest();
		StringBuffer requestURL = request.getRequestURL();
		String queryString = request.getQueryString();
		if(StringUtils.isNotBlank(queryString)){
			requestURL.append("?").append(queryString);
		}
		String sign = MD5.sign(requestURL.toString(), "UTF-8");
		return pre_suff + sign;
	}

	@Override
	public ActionResult postExecute(BeatContext beat, ActionResult executionResult) {
		if(executionResult != null){
			if(executionResult instanceof VelocityViewResult){
				VelocityViewResult newObj = (VelocityViewResult)executionResult;
				String viewName = newObj.getViewName();
				if(StringUtils.isNotBlank(viewName) && !StringUtils.equalsIgnoreCase(viewName, "404")){
					String memKey = buildMemKey(beat);
					System.out.println("放入缓存， memKey=" + memKey);
					Date date = new Date(24 * 60 * 60 * 1000);
					MemcachedUtil.set(memKey, ((VelocityViewResult) executionResult).getContent(beat), date);
				}else{
					System.out.println("viewName = " + viewName);
				}
			}else{
				System.out.println("executionResult is not VelocityViewResult");
			}
		}else{
			System.out.println("executionResult is null");
		}
		return executionResult;
	}
}
