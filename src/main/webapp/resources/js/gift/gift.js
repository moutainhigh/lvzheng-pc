//@author bruce
//@赠送的大礼包活动js all In here

//-------------------记录cookie的操作--------------------------
function cookieGO(name) {
  var today = new Date();
  var expires = new Date();
  expires.setTime(today.getTime() + 1000*60*60*24);
  setCookie("giftpop", name, expires);
}

function setCookie(name, value, expire) {   
  window.document.cookie = name + "=" + escape(value) + ((expire == null) ? "" : ("; expires=" + expire.toGMTString()));
}

function getCookie(Name) {   
   var findcookie = Name + "=";
   if (window.document.cookie.length > 0) { // if there are any cookies
     offset = window.document.cookie.indexOf(findcookie);
  if (offset != -1) { // if cookie exists
       offset += findcookie.length;          // set index of beginning of value
    end = window.document.cookie.indexOf(";", offset)          // set index of end of cookie value
    if (end == -1)
      end = window.document.cookie.length;
    return unescape(window.document.cookie.substring(offset, end));
     }
   }
   return null;
}

function popgift() {
  var c = getCookie("giftpop");
  //alert(c);
  if (c != null) {
    return false;
  }
  cookieGO("popgift");
  
  //弹出窗show()
  $("#J_gift_pop_up").addClass('is-visible');
  
}

//-------------------记录cookie的操作--------------------------


$(function(){
	//弹出gift判断
	popgift() ;
	
//弹出gift
	$("#J_gift_pop").click(function(){
		$.ajax({
			
			url:"/gift/draw",
			
			type:"post",
			
			success:function(data){
				$("#couts-box").empty().append(data);
			}
		
		});
		
	})
});

var InterValObj; //timer变量，控制时间  
var count = 120; //间隔函数，1秒执行  
var curCount;//当前剩余秒数  

function sendMessage() {
    curCount = count;  
    var jbPhone =  $("#phoneNum").val(); 
  
    var validatecode = $("#validatecode").val();
    var tokenstr = $("#J_gift_token").val();
    
    if (checkPhoneFormat(jbPhone)) {  
 			//校验手机号的存在性
 			
    		$.ajax({

	            url:"/gift/phoneFlag",
	
	            type:"post",
	            
	            data: {"phoneNum":jbPhone},
	            
	            success:function(data){
					if(data=='0'){
						if(!validatecode){
							alert('请填写图形验证码');
							return false;
						}
						$("#J_send_code").attr("disabled",true);
			    		 $("#J_send_code").html("剩余" + curCount + "s");  
			    		 InterValObj = window.setInterval(SetRemainTime, 1000); // 启动计时器，1秒执行一次  
			            // 向后台发送处理数据  
			            $.ajax({  
			                type: "POST", // 用POST方式传输  
			                dataType: "json", // 数据格式:JSON  
			                url: "/common/sendPhoneCode", // 目标地址  
			                data: "phoneNum=" + jbPhone+"&tokenstr="+tokenstr+"&validatecode="+validatecode,
			                success:function(data){
			                	if(data.flag == "8"){
			                		$("#validateimg").click();
			                		$("#J_send_code").removeAttr("disabled");
			                		alert("请您输入验证码！");
			                		curCount = 0;
			                		return false;
			                	}
			                	if(data.flag == "9"){
			                		$("#validateimg").click();
			                		alert("输入验证码不正确!");
			                		$("#J_send_code").removeAttr("disabled");
			                		curCount = 0;
			                		return false;
			                	}
			                	if(data.flag == "-1"){
			                		$("#validateimg").click();
			                		$("#J_send_code").removeAttr("disabled");
			                		curCount = 0;
			                		alert("验证码发送失败!请重新发送");
			                		return false;
			                	}
			                	if(data.flag == "1" || data.flag == "2"){
			                        // 设置button效果，开始计时  
			                        // 立即领取启用&&发验证码禁用
			                		//$("#J_send_code").attr("disabled",true);
			                        $('#J_gift_btn').attr("cc","1");
			                        
			                	}
			                }
			            });  
					}else{
						//alert('老用户不能参与此活动.');
						
						$("#J_old_pop").addClass('is-visible');
						//关闭弹窗
						$(".cd-box1").hide();
						$(".cd-box2").hide();
						return false;
					}
	            }

        	});
    	
          
    }
}  

//timer处理函数  
function SetRemainTime() {  
    if (curCount == 0) {                  
        window.clearInterval(InterValObj);// 停止计时器  
        //$("#box_btn").attr("class","t_box_btn");
		$("#J_send_code").attr("disabled",false);
        $("#J_send_code").html("重新发送");  
    }else {  
        curCount--;  
       // $("#box_btn").val("请在" + curCount + "秒内输入验证码");  
        $("#J_send_code").html("剩余" + curCount + "s"); 
    }  
} 

function drawgift(){
	var cc = $('#J_gift_btn').attr("cc");
	
	if(cc=='0'){
		alert('请完成发送验证码的校验');
		return false;
	}else if(cc=='1'){//可以领取
		
			 $.ajax({

	             url:"/gift/receive",

	             type:"post",
	             
	             dataType:"json",

	             data:$("#J_form_gift").serialize(),

	             success:function(data){

	                 alert(data.msg);
	            	 //$("#J_succ_pop").addClass('is-visible');
	                 if(data.ret=="ok"){


	                     window.location.href = "/gift/showgift?uid="+data.uid;

	                 }else{
	                	 return false;
	                 }
	                 
	                 

	             }

	         });
			
	}
}


function checkPhoneFormat(phoneNum){
	if(phoneNum == "" || !(/^1[3|4|5|7|8][0-9]\d{8}$/.test(phoneNum))){
		alert("输入的手机为空或格式不正确!");
		return false;
	}
	return true;
}

$(function(){
    setTimeout(function () {
        $(".tshide").hide();
    }, 5000);

});
