$(document).ready(function(){
	$("#pass").blur(function(){
		var o = $(this);
		var v = o.val();
		if(v != ""){
			$("#password").val($.md5(v));
		}
	});
	$("#re_password").blur(function(){
		var o = $(this);
		var v = o.val();
		if(v != ""){
			var sv = $.md5(v);
			
		}
	});
	
	$("#register").click(function(){
		var url = "/reguseraction";
		$.ajax({url:url,
			data:$("#register_form").serialize(),
			dataType:"json",
			success:function(backdata){
				if(backdata.ret == "ok"){
					var redurl = $("#red_url").val();
					if(redurl != ""){
						window.location.href = redurl;
					}else{
						window.location.href = "/";
					}
				}else if(backdata.ret  === "fail"){
					alert(backdata.msg);
				}
			}});
		//$("#register_form").submit();
	});
	
	$("#box_btn").click(function(){
		sendMessage();
	});
});


  
 
