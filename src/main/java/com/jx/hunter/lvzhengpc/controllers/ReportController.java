package com.jx.hunter.lvzhengpc.controllers;

import java.util.Date;

import com.jx.argo.ActionResult;
import com.jx.argo.BeatContext;
import com.jx.argo.annotations.Path;
import com.jx.hunter.lvzhengpc.common.LocationPage;
import com.jx.hunter.lvzhengpc.utils.Timers;

/***
 * 年报controller
 * @author flower
 */
@Path("/report")
public class ReportController extends BaseController {
	public static final String r_Time = "2016-06-20";
	@Path("/index.html")
	public ActionResult myIndex(){
		getTimer(beat());
		//转到的页面路径
		model().add("LocationPage", new LocationPage("/report","index"));
		return view("/index");
	}
	
	/**
	 * 得到年报时间
	 * @param beat
	 */
	public static void getTimer(BeatContext beat){
		Long time = Timers.getBetweenDay(r_Time, Timers.formatLongDate("yyyy-MM-dd", new Date().getTime()));
		String times = "00";
		if(time >= 0){
			times = time.toString();
			if(!"0".equals(times)){
				if(times.length() == 1){
					times = "0" + times;
				}
			}
		}
		beat.getModel().add("timer", times);
	}
}
