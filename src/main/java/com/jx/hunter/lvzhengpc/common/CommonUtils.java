package com.jx.hunter.lvzhengpc.common;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.jx.argo.BeatContext;
import com.jx.blackface.gaea.usercenter.entity.BFLoginEntity;
import com.jx.hunter.lvzhengpc.frame.RSBLL;
import com.jx.hunter.lvzhengpc.utils.CookieUtils;
import com.jx.hunter.lvzhengpc.utils.EntityUtils;
import com.jx.hunter.lvzhengpc.utils.TokenHandler;
import com.jx.hunter.lvzhengpc.vo.LoginUserVO;

public class CommonUtils {
	public static final String cookieName = "lvuser";
	
	
	/***
	 * 通过cookie判断用户是否还存在
	 * @param beat
	 * @return
	 */
	public static LoginUserVO getLoginEntity(BeatContext beat){
		LoginUserVO loginVo = null;
		if(CommonUtils.checkCookieName(cookieName, beat.getRequest())){
			String userid = CommonUtils.getUserIdFormCookie(cookieName, beat.getRequest());
			if(StringUtils.isNotBlank(userid)){
				BFLoginEntity loginEntity = null;
				try {
					loginEntity = RSBLL.getstance().getLoginService().getLoginEntityById(Long.valueOf(userid));
				} catch (Exception e) {
					System.out.println("获取用户失败userid:"+userid);
					e.printStackTrace();
				}
				if(null != loginEntity){
					loginVo = EntityUtils.transferEntity(loginEntity, LoginUserVO.class);
				}
			}
		}
		return loginVo;
	}

	public static long getLoginuserid(BeatContext beat){
		long uid = 0l;
		/*****调用通用调取cookie包 Start*******/
		if(CommonUtils.checkCookieName(cookieName, beat.getRequest())){
			String userid = CommonUtils.getUserIdFormCookie(cookieName, beat.getRequest());
			if(StringUtils.isNotBlank(userid)){
				BFLoginEntity loginEntity = null;
				try {
					loginEntity = RSBLL.getstance().getLoginService().getLoginEntityById(Long.valueOf(userid));
				} catch (Exception e) {
					System.out.println("获取用户失败userid:"+userid);
					e.printStackTrace();
				}
				if(null != loginEntity){
					uid = loginEntity.getUserid();
				}
			}
		}
		/*****End*******/
		return uid;
	}
	
	/***
	 * 从cookie中获取用户id
	 * @param request
	 */
	public static String getUserIdFormCookie(String cookieName,HttpServletRequest request){
		String userid="";
		try {
			String cookieValues = CookieUtils.getCookieValueByName(cookieName, request);
			if(StringUtils.isNotBlank(cookieValues)){
				if(StringUtils.contains(cookieValues, ":")){
					String[] splitCookieValues = StringUtils.split(cookieValues, ":");
					if(splitCookieValues.length > 0 && StringUtils.isNotBlank(splitCookieValues[0])){
						userid = splitCookieValues[0];
					}
				}else{
					userid = cookieValues;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userid;
	}
	
	/***
	 * 检查此cookie是否存在
	 * @param cookieName
	 * @param request
	 * @return true 存在 false 不存在
	 */
	public static boolean checkCookieName(String cookieName,HttpServletRequest request){
		boolean checkFlag = false;
		try {
			checkFlag = CookieUtils.checkCookieName(cookieName, request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return checkFlag;
	}
	
	public static boolean isChinese(String text){
		if(StringUtils.isBlank(text)){
			return false;
		}
		Pattern p_str = Pattern.compile("[\\u4e00-\\u9fa5]+");
		Matcher m = p_str.matcher(text);
		if (!m.find() || !m.group(0).equals(text)) {
			return false;
		}
		return true;
	}

	public static void geneCode(BeatContext beat) {
		// TODO Auto-generated method stub
		String token = TokenHandler.generateToken(beat.getRequest().getSession());
    	beat.getModel().add("token", token);
    	beat.getRequest().getSession().setAttribute("token", token);
	}
	
	public static JSONObject checkImgCode(BeatContext beat){
		String tokenstr = beat.getRequest().getParameter("tokenstr");
		String validatecode = beat.getRequest().getParameter("valCode");
		JSONObject jr = new JSONObject();
		if(StringUtils.isBlank(tokenstr)){
			jr.put("ret", "fail");
			jr.put("msg", "图形验证码失败！请刷新后重试!");
			return jr;
		}
		if(StringUtils.isBlank(validatecode)){
			jr.put("ret", "fail");
			jr.put("msg", "图形验证码错误!");
			return jr;
		}
		String sevali = (String) beat.getRequest().getSession().getAttribute("valicode"+tokenstr);
		if(StringUtils.equalsIgnoreCase(sevali,validatecode)){
			jr.put("ret", "ok");
		}else{
			jr.put("ret", "fail");
			jr.put("msg", "图形验证码错误!");
		}
		return jr;
	}
	
	
	public static boolean isActiveTrueTime(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 20);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		
		long startTime = cal.getTime().getTime();
		long endTime = startTime + 1000*60*60*4;
		
		long nowTime = System.currentTimeMillis();
		
		if (nowTime > startTime && nowTime < endTime)
			return true;
		
		return false;
	}
	
	public static void activeFloatFlag(BeatContext beat) {
		int flag = isActiveTrueTime() ? 1 : 0;
		beat.getModel().add("active_time", flag);
	}
	
	public static void activeFlag(BeatContext beat) {
		try {

			HttpServletRequest request = beat.getRequest();
			HttpServletResponse response = beat.getResponse();
			String flag = "0";
			if (isActiveTrueTime()){
			
				String referer = request.getHeader("Referer");
				if (!StringUtils.contains(referer, ".lvzheng.com")){
					String actFlag = CookieUtils.getCookieValueByName("lv_active_f", request);
					if (StringUtils.isBlank(actFlag)) {
						CookieUtils.write(response, "lv_active_f","1", getExpiresTime());
						flag = "1";
					}
				}
			}
			beat.getModel().add("active_flag", flag);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private static int getExpiresTime() {
		Calendar starCalendar = Calendar.getInstance();
		starCalendar.set(Calendar.MILLISECOND, 0);
		Date startDate=starCalendar.getTime();
		
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(startDate);
		endCalendar.set(endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DATE), 23, 59, 59);
		
		Date endDate=endCalendar.getTime();
		int expire=(int) ((endDate.getTime()-startDate.getTime())/1000);
		return expire;
	}
	
}
