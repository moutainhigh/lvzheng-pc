package com.jx.hunter.lvzhengpc.utils;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jx.blackface.gaea.sell.entity.LvzProductCateEntity;
import com.jx.blackface.gaea.sell.entity.LvzProductEntity;
import com.jx.blackface.gaea.sell.entity.LvzProductInfoEntity;
import com.jx.hunter.lvzhengpc.frame.RSBLL;

public class ProductUtils {
	public static List<LvzProductEntity> getProductList(String childCateCode){
		if(StringUtils.isBlank(childCateCode)){
			return null;
		}
		List<LvzProductEntity> productEntityList = null;
		try {
			productEntityList = RSBLL.getstance().getLvzProductService().getProductEntityByChildcatecode(childCateCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return productEntityList;
	}
	
	
	/**
	 * 通过productid获取productinfo表中数据
	 * @param productId
	 * @return
	 */
	public static LvzProductInfoEntity getProductInfoByProductId(long productId){
		if(productId <= 0 ){
			return null;
		}
		LvzProductInfoEntity productInfoE = null;
		try {
			List<LvzProductInfoEntity> productInfolist = RSBLL.getstance().getInfoService().getProductInfoEntityList("typid='3' and info_id='"+productId+"'", 1, 1, null);
			if(null != productInfolist && !productInfolist.isEmpty()){
				productInfoE = productInfolist.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return productInfoE;
	}
}
