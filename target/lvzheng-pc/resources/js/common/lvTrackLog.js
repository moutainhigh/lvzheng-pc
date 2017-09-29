var localLogName = document.getElementById('lvzhengLog').getAttribute('data');
var lv_referrer = document.referrer;
var lv_pageUrl = window.location.href;
function lv_ajaxsend(tracksrc) {
    (new Image).src = encodeURI(tracksrc);
}

function lv_clickLog(clickfrom) {
	lv_ajaxsend("/lv/empty.png?" + clickfrom + "&pageUrl=" + lv_referrer + "&rand=" + Math.random());
}

function load(){
	lv_ajaxsend("/lv/empty.png?moudle=web_js_access&logName=lvzhengpcjs&t=1&pageUrl=" + lv_pageUrl + "&referer=" + lv_referrer + "&rand=" + Math.random());
}

$(document).ready(function(){


	$("[trace]").on("click",function(){
		var str = "";
		var module = $(this).attr("t-module");
		var value = $(this).attr("t-value");
		str += "moudle="+module;
		str += "&data_action=click";
		str += "&data_value="+value;
		str += "&logName="+localLogName;
		//alert(localLogName+"-->"+str);
		lv_clickLog(str);
	});
	
	
	//绑定右侧商桥的图标，点击事件记录
	$(document).on("click",".nb-icon-bridge0",function(){
		var str = "";
		str += "moudle=shangqiao_right";
		str += "&data_action=click";
		str += "&logName="+localLogName;
		//alert(localLogName+"-->"+str);
		lv_clickLog(str);
	});
	
	//商桥浮动图标，点击咨询
	$(document).on("click","#nb_invite_ok",function(){
		var str = "";
		str += "moudle=shangqiao_float_ok";
		str += "&data_action=click";
		str += "&logName="+localLogName;
		lv_clickLog(str);
	});
	
});
//页面load结束执行统计，暂时不用，用的时候放开即可
//load();
// moudle=自助流程宣传页&userId=23423423&logName=weizhanlog&from=timeline&data_openId=234ljkalsdfq3

