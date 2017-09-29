var recommend_coupons_json;


$(function(){
	
	/***********优惠券选择  start **************/
    $('[name="coupon-select"]').live("click",function(e){
        $('[name="coupon-select"]').find('ul').hide();
        $(this).find('ul').show();
        e.stopPropagation();
    });
    $('[name="coupon-select"] li').live("hover",function(e){
        $(this).toggleClass('on');
        e.stopPropagation();
    });
    $('[name="coupon-select"] li').live("click",function(e){
        var val = $(this).text();
        var dataVal = $(this).attr("data-value");
        var dataId = $(this).attr("data-id");
        
        $(".coupon_input").each(function(){
        	var input_data_id = $(this).attr("data-id");
        	if (input_data_id == dataId) {
        		$(this).val("不使用优惠券")
        		.attr("data-discount","")
                .attr("data-id","");
        	}
        });
        
        $(this).parents('[name="coupon-select"]').find('input').val(val)
        .attr("data-discount",dataVal)
        .attr("data-id",dataId);
        $('[name="coupon-select"] ul').hide();
        
        reSetPrice();
        
        e.stopPropagation();
    });
    $(document).click(function(){
        $('[name="coupon-select"] ul').hide();
    });
    
    /***********优惠券选择  end **************/
	
	
})

