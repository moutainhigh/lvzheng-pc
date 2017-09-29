$(function(){
	$('.selectBtn').on('click',function(){
		var loginFlag = checkLogin();
		if(loginFlag == false){//未登录
			//清空登陆表单
			$('#phoneNum').val('');
			$('#validatecode').val('');
			$('#phoneM').val('');
			$('#username').val('');
			$('#password').val('');
			$('.cd-box1AlertLogin').addClass("is-visible");
			return;
		}
		
		var keyWord = $('#searchKeyWord').val();
		searchBrand(keyWord);
	});
	
	$(".brandSearch-close").click(function(){
    	$("#valicodeinput").val("");
    	$(".cd-box1imgcode").removeClass("is-visible");
    });
	
	$(".alertLogin-close").click(function(){
    	$(".cd-box1AlertLogin").removeClass("is-visible");
    });
	
	
	
	//回车键
	$("body").keydown(function() {
	    if (event.keyCode == "13") {//keyCode=13是回车键
	    	if($('.cd-box1imgcode').hasClass('is-visible')){
	    		$('.searchBtn').click();
	    	}else if($('.cd-box1AlertLogin').hasClass('is-visible')){
	    		if(!($('#pt_login').is(':hidden'))){
	    			$('#pt_button').click();
	    		}else if(!($('#phone_login').is(':hidden'))){
	    			$('#phone_button').click();
	    		}
	    	}else{
	    		$('.selectBtn').click();
	    	}
	    }
	});
	
	
	
})

//检查是否登录
function checkLogin(){
		var loginFlag;
		$.ajax({
			url:"/brand/checkLogin",
			async:false,	
			type:"post",
			dataType:"json",
			success:function(data){
				loginFlag = data.loginFlag;
			},
			error:function(){alert('未响应')}
		});
		
		return loginFlag;
	}

//登录成功后的操作
function loginret(){
	$('.cd-box1AlertLogin').removeClass("is-visible");
	
	var keyWord = $('#searchKeyWord').val();
	searchBrand(keyWord);
}

//查询商标
function searchBrand(keyWord){
	var keyWord = keyWord.trim();
	if(keyWord == ""){
		alert("请输入搜索关键字！");
		return;
	}
	
	//判断是否超过查询次数
	var retData = getBrandSearchCount();
	if(retData.ret == 'noLogin'){//未登录
		alert("请登录后再查询..");
		return;
	}else{
		if(retData.ret <= 88){//查询次数小于等于100 则无须输入图形验证码
			var url = "/brand/gotoBrandResult?keyWord="+keyWord;
			window.location.href = encodeURI(url);
			return;
		}
	}
	
	
	
	$("#valicodedateimg").click();//初始化一个验证码
	$('.cd-box1imgcode').addClass("is-visible");
	$(".searchBtn").unbind('click').on('click',function(){
		var checkret = checkImgCode();
		if(checkret.ret == "ok"){
			$("#valicodeinput").val("");
			$('.cd-box1imgcode').removeClass("is-visible");
			
			var url = "/brand/gotoBrandResult?keyWord="+keyWord;
			window.location.href = encodeURI(url);
		}else if(checkret.ret == "fail"){
			alert(checkret.msg);
			$("#valicodedateimg").click();//初始化一个验证码
			$("#valicodeinput").val("");
		}
	});
	
}

/*校验图形验证码*/
function checkImgCode(){
	var tokenstr = $("#token").val();
	var valCode = $("#valicodeinput").val();
	var checkRet;
	$.ajax({
		url:"/common/checkImgCode",
		type:"post",
		dataType:"json",
		data:{"tokenstr":tokenstr,"valCode":valCode},
		async:false,
		success:function(data){
			checkRet = data;
		},
		error:function(){alert('未响应')}
	});
	
	return checkRet;
}

/*获取该用户商标查询次数*/
function getBrandSearchCount(){
	var ret;
	$.ajax({
		url:"/brand/getBrandSearchCount",
		type:"post",
		dataType:"json",
		async:false,
		success:function(data){
			ret = data;
		},
		error:function(){alert('未响应')}
	});
	return ret;
}

