$(function(){
	$(window).scroll(function(){
		var a=parseInt($(window).scrollTop());
		if(a<1000){
			$(".sp-nav-box").removeClass("new-spnavxf")
		}
		if(a>1000)
			{
			$(".sp-nav-box").addClass("new-spnavxf")
			}
	});

  $(window).scroll(function(){
    var a=parseInt($(window).scrollTop());
    if(a<550){
      $(".bl-nav").removeClass("bl-navxf")
    }
    if(a>550)
      {
      $(".bl-nav").addClass("bl-navxf")
      }
  });

  // 商品列表导航浮动
  $(window).scroll(function(){
    var a=parseInt($(window).scrollTop());

    if(a<700){
      $(".splist-nav").removeClass("splist-navxf")
    }
    if(a>700){
      $(".splist-nav").addClass("splist-navxf")
    }
  });

  var height=0;  
  var scr = 0  
  $(document).ready(function(e) {  
      height = $("div#container").height();//获取内容高  
      screenheight = window.screen.availHeight; //获取屏幕高  
      bodyheight = document.body.clientHeight;//获取body高  
  });  

  $(window).scroll( function() {   
      scr = $(window).scrollTop();  
      if(screenheight+scr> document.body.clientHeight-140){//当滚动条滑动到据底部100px时触发  
          $(".splist-nav").removeClass("splist-navxf")
      }  
  }); 

})


// banner
//$(document).ready(function () {
//    $(".banner-box").hover(function(){
//        $("#btn-prev,#btn-next").fadeIn()
//        },function(){
//        $("#btn-prev,#btn-next").fadeOut()
//        })
//    $dragBln = false;
//    $(".main-image").touchSlider({
//        flexible : true,
//        speed : 500,
//        btn_prev : $("#btn-prev"),
//        btn_next : $("#btn-next"),
//        paging : $(".flicking-con a"),
//        counter : function (e) {
//            $(".flicking-con a").removeClass("on").eq(e.current-1).addClass("on");
//        }
//    });
//    $(".main-image").bind("mousedown", function() {
//        $dragBln = false;
//    })
//    $(".main-image").bind("dragstart", function() {
//        $dragBln = true;
//    })
//    $(".main-image a").click(function() {
//        if($dragBln) {
//            return false;
//        }
//    })
//    timer = setInterval(function() { $("#btn-next").click();}, 5000);
//    $(".banner-box").hover(function() {
//        clearInterval(timer);
//    }, function() {
//        timer = setInterval(function() { $("#btn-next").click();}, 5000);
//    })
//    $(".main-image").bind("touchstart", function() {
//        clearInterval(timer);
//    }).bind("touchend", function() {
//        timer = setInterval(function() { $("#btn-next").click();}, 5000);
//    })
//});


// 原生返回顶部
function gotoTop(acceleration,stime) {
   acceleration = acceleration || 0.1;
   stime = stime || 10;
   var x1 = 0;
   var y1 = 0;
   var x2 = 0;
   var y2 = 0;
   var x3 = 0;
   var y3 = 0; 
   if (document.documentElement) {
       x1 = document.documentElement.scrollLeft || 0;
       y1 = document.documentElement.scrollTop || 0;
   }
   if (document.body) {
       x2 = document.body.scrollLeft || 0;
       y2 = document.body.scrollTop || 0;
   }
   var x3 = window.scrollX || 0;
   var y3 = window.scrollY || 0;
 
   var x = Math.max(x1, Math.max(x2, x3));
   var y = Math.max(y1, Math.max(y2, y3));
 
   var speeding = 1 + acceleration;
   window.scrollTo(Math.floor(x / speeding), Math.floor(y / speeding));
 
   if(x > 0 || y > 0) {
       var run = "gotoTop(" + acceleration + ", " + stime + ")";
       window.setTimeout(run, stime);
   }
}


// 下拉左导航
;(function($){
$.fn.tabso=function( options ){

	var opts=$.extend({},$.fn.tabso.defaults,options );
	
	return this.each(function(i){
		var _this=$(this);
		var $menus=_this.children( opts.menuChildSel );
		var $container=$( opts.cntSelect ).eq(i);
		
		if( !$container) return;
		
		if( opts.tabStyle=="move"||opts.tabStyle=="move-fade"||opts.tabStyle=="move-animate" ){
			var step=0;
			if( opts.direction=="left"){
				step=$container.children().children( opts.cntChildSel ).outerWidth(true);
			}else{
				step=$container.children().children( opts.cntChildSel ).outerHeight(true);
			}
		}
		
		if( opts.tabStyle=="move-animate" ){ var animateArgu=new Object();	}
			
		$menus[ opts.tabEvent]( function(){
			var index=$menus.index( $(this) );
			$( this).addClass( opts.onStyle )
				.siblings().removeClass( opts.onStyle );
			switch( opts.tabStyle ){
				case "fade":
					if( !($container.children( opts.cntChildSel ).eq( index ).is(":animated")) ){
						$container.children( opts.cntChildSel ).eq( index ).siblings().css( "display", "none")
							.end().stop( true, true ).fadeIn( opts.aniSpeed );
					}
					break;
				case "move":
					$container.children( opts.cntChildSel ).css(opts.direction,-step*index+"px");
					break;
				case "move-fade":
					if( $container.children( opts.cntChildSel ).css(opts.direction)==-step*index+"px" ) break;
					$container.children( opts.cntChildSel ).stop(true).css("opacity",0).css(opts.direction,-step*index+"px").animate( {"opacity":1},opts.aniSpeed );
					break;
				case "move-animate":
					animateArgu[opts.direction]=-step*index+"px";
					$container.children( opts.cntChildSel ).stop(true).animate( animateArgu,opts.aniSpeed,opts.aniMethod );
					break;
				default:
					$container.children( opts.cntChildSel ).eq( index ).css( "display", "block")
						.siblings().css( "display","none" );
			}
	
		});
		
		$menus.eq(0)[ opts.tabEvent ]();
		
	});
};	

})(jQuery);



$(function(){
    // 展开收起
    $('a.more').bind('click', function () { 
        var txt = $('a.more').text(); 
        if (txt == '更多') { 
            $('.morediv').show(); 
            $('a.more').text('收起').addClass('top-arr'); 
        } else { 
            $('.morediv').hide(); 
            $('a.more').text('更多').removeClass('top-arr'); 
        } 
    }) 
});