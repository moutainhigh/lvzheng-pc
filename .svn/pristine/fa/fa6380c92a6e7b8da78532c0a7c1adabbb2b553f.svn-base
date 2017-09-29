 package com.jx.hunter.lvzhengpc.frame;

import com.jx.argo.ArgoTool;
import com.jx.blackface.gaea.sell.contract.ILvzCusEvaluationService;
import com.jx.blackface.gaea.sell.contract.ILvzPackageSellService;
import com.jx.blackface.gaea.sell.contract.ILvzPackageService;
import com.jx.blackface.gaea.sell.contract.ILvzProdcutTitleService;
import com.jx.blackface.gaea.sell.contract.ILvzProductCateService;
import com.jx.blackface.gaea.sell.contract.ILvzProductInfoService;
import com.jx.blackface.gaea.sell.contract.ILvzProductService;
import com.jx.blackface.gaea.sell.contract.ILvzQAinfoService;
import com.jx.blackface.gaea.sell.contract.ILvzSellProductService;
import com.jx.blackface.gaea.usercenter.contract.IAreasService;
import com.jx.blackface.gaea.usercenter.contract.ICouponsService;
import com.jx.blackface.gaea.usercenter.contract.ILoginService;
import com.jx.blackface.gaea.usercenter.contract.ILvzAddressAreaorderService;
import com.jx.blackface.gaea.usercenter.contract.ILvzAddressConfService;
import com.jx.blackface.gaea.usercenter.contract.ILvzMenuConfService;
import com.jx.blackface.gaea.usercenter.contract.ILvzMenuService;
import com.jx.blackface.gaea.usercenter.contract.ILvzProductConfService;
import com.jx.blackface.gaea.usercenter.contract.IUserCounponsService;
import com.jx.blackface.servicecoreclient.contract.IOrderBFGService;
import com.jx.gaea.trade.contract.ILvTradeBaseService;
import com.jx.service.enterprise.contract.ILvEnterpriseBusinessDataService;
import com.jx.service.enterprise.contract.ILvEnterpriseBusinessService;
import com.jx.service.enterprise.contract.ILvEnterpriseMainBusinessService;
import com.jx.service.enterprise.contract.ILvEnterpriseRoleRelationService;
import com.jx.service.enterprise.contract.ILvEnterpriseService;
import com.jx.service.messagecenter.contract.IMoblieSmsService;
import com.jx.service.newcore.contract.IArticleCateRelationService;
import com.jx.service.newcore.contract.IFAQService;
import com.jx.service.newcore.contract.IFollowupService;
import com.jx.service.newcore.contract.IFriendLinkService;
import com.jx.service.newcore.contract.IProductCategoryService;
import com.jx.service.newcore.contract.IProductPropertiesService;
import com.jx.service.newcore.contract.ISorderChildrenService;
import com.jx.service.newcore.contract.ISorderService;
import com.jx.service.newcore.contract.ISorderSuperService;
import com.jx.service.newcore.contract.IWorkDayService;
import com.jx.service.newcore.contract.IXwsSitemapService;
import com.jx.spat.gaea.client.GaeaInit;
import com.jx.spat.gaea.client.proxy.builder.ProxyFactory;

public class RSBLL {
	
	private String GAEA_WF = "workflow";
	private String GAEA_UC = "usercenter";
	private String GAEA_EP = "enterprise";
	private String GAEA_UN = "union";
	private String GAEA_MSG ="jxmessage";
	private String GAEA_SELL ="sellcore";
	private String GAEA_SERVICE ="servicecore";
	private String SV_NAME_TRADE = "trade";
	
	static{
		String url = ArgoTool.getConfigFolder()+ArgoTool.getNamespace()+"/gaea.config";
		GaeaInit.init(url);
	}
	
	private static RSBLL newstance = null;
	private static Object lock = new Object();
	public static RSBLL getstance(){
		if(newstance == null){
			synchronized (lock) {
				if(newstance == null){
					newstance = new RSBLL();
				}
			}
		}
		return newstance;
	}
	
	/**
	 * 
	 * @return
	 */
	public ILvzProdcutTitleService getLvzProductTitleService(){
		return ProxyFactory.create(ILvzProdcutTitleService.class, "tcp://sellcore/LvzProdcutTitleService");
	} 
	/**定价条目实体*/
	public ILvzSellProductService getLvzSellProductService(){
		ILvzSellProductService lvzSellProductService = ProxyFactory.create(ILvzSellProductService.class, "tcp://"+GAEA_SELL+"/LvzSellProductService");
		return lvzSellProductService;
	}


	/**商品类别实体*/
	public ILvzProductCateService getLvzProductCateService(){
		ILvzProductCateService productCateService = ProxyFactory.create(ILvzProductCateService.class, "tcp://"+GAEA_SELL+"/LvzProductCateService");
		return productCateService;
	}
	/**
	 * 商品实体
	 * @return
	 */
	public ILvzProductService getLvzProductService(){
		ILvzProductService lvzProductService=ProxyFactory.create(ILvzProductService.class, "tcp://"+GAEA_SELL+"/LvzProductService");
		return lvzProductService;
	}
	
	
	/**
	 * 热门问答服务
	 */
	public ILvzQAinfoService getQAinfoService(){
		String url = "tcp://"+GAEA_SELL+"/LvzQAinfoService";
		ILvzQAinfoService iLvzQAinfoService = ProxyFactory.create(ILvzQAinfoService.class, url);
		return iLvzQAinfoService;
	}
	
	/**
	 * 客户评价
	 */
	public ILvzCusEvaluationService getLvzCusEvaluationService(){
		String url = "tcp://"+GAEA_SELL+"/LvzCusEvaluationService";
		ILvzCusEvaluationService lvzCusEvaluationService = ProxyFactory.create(ILvzCusEvaluationService.class, url);
		return lvzCusEvaluationService;
	}
	
	/**
	 * 地区服务
	 * @return
	 */
	public IAreasService getCityService(){
		IAreasService cityService = ProxyFactory.create(IAreasService.class, "tcp://"+GAEA_UC+"/AreasService");
		return cityService;

	}
	/**
	 * 工作日  
	 */
	public IWorkDayService getWorkDayService(){
		return ProxyFactory.create(IWorkDayService.class, "tcp://jxcore/WorkDayService");
	}
	
	/**
	 * 咨询类别商品库类别关系服务
	 * @return
	 */
	public IArticleCateRelationService getArticleCateRelationService(){
		String url = "tcp://jxcore/ArticleCateRelationService";
		IArticleCateRelationService articleCateRelationService = ProxyFactory.create(IArticleCateRelationService.class, url);
		return articleCateRelationService;
	}
	
	/**
	 * 资讯站问答服务
	 * @return
	 */
	public IFAQService getFAQService(){
		String url = "tcp://jxcore/FAQService";
		IFAQService ifaqService = ProxyFactory.create(IFAQService.class, url);
		return ifaqService;
	}
	
	public IUserCounponsService getUserCounponsService(){
		return ProxyFactory.create(IUserCounponsService.class, "tcp://"+GAEA_UC+"/UserCounponsService");
	}
	
	
	public ICouponsService getCounponsService(){
		return ProxyFactory.create(ICouponsService.class, "tcp://"+GAEA_UC+"/CouponsService");
	}
	
	/**
	 * 商品信息服务
	 * @return
	 */
	public ILvzProductInfoService getInfoService() {
		ILvzProductInfoService infoService = ProxyFactory.create(ILvzProductInfoService.class, "tcp://"+GAEA_SELL+"/LvzProductInfoService");
		return infoService;

	}
	public ILoginService getLoginService(){
		return ProxyFactory.create(ILoginService.class, "tcp://usercenter/LoginService");
	}



	public IMoblieSmsService getMoblieSmsService() {
		// TODO Auto-generated method stub
		return ProxyFactory.create(IMoblieSmsService.class, "tcp://jxmessage/MoblieSmsService");
	}
	
	/**
	 * 企业库 - 行业特点信息
	 * @return
	 */
	public ILvEnterpriseMainBusinessService getEpEnterpriseMainBusinessService(){
		return ProxyFactory.create(ILvEnterpriseMainBusinessService.class, "tcp://" + GAEA_EP + "/LvEnterpriseMainBusinessService");
	}
	/**
	 * 企业库 - 业务信息
	 * @return
	 */
	public ILvEnterpriseBusinessService getEpEnterpriseBusinessService(){
		return ProxyFactory.create(ILvEnterpriseBusinessService.class, "tcp://" + GAEA_EP + "/LvEnterpriseBusinessService");
	}
	/**
	 * 企业库 - 业务扩展信息
	 * @return
	 */
	public ILvEnterpriseBusinessDataService getEpEnterpriseBusinessDataService(){
		return ProxyFactory.create(ILvEnterpriseBusinessDataService.class, "tcp://" + GAEA_EP + "/LvEnterpriseBusinessDataService");
	}
	/**
	 * 企业库 - 主表信息
	 * @return
	 */
	public ILvEnterpriseService getEpEnterpriseService(){
		return ProxyFactory.create(ILvEnterpriseService.class, "tcp://" + GAEA_EP + "/LvEnterpriseService");
	}
	
	
	/**** SEM向老官网导流start*/
	public ILvEnterpriseRoleRelationService getEnterpriseRoleRelationService(){
		String url = "tcp://"+GAEA_EP+"/LvEnterpriseRoleRelationService";
		return ProxyFactory.create(ILvEnterpriseRoleRelationService.class, url);
	}
	public IFollowupService getIFollowupService(){
		String url ="tcp://jxcore/FollowupService";
		IFollowupService iso = ProxyFactory.create(IFollowupService.class, url);
		return iso;
	}
	public IProductPropertiesService getIProductPropertiesService(){
		String url ="tcp://jxcore/ProductPropertiesService";
		IProductPropertiesService ipe =  ProxyFactory.create(IProductPropertiesService.class, url);
		return ipe;
	}
	public IProductCategoryService getProductCategoryService(){
		String url ="tcp://jxcore/ProductCategoryService";
		IProductCategoryService ipe =  ProxyFactory.create(IProductCategoryService.class, url);
		return ipe;
	}
	public ISorderChildrenService getISorderChildrenService(){
		String url ="tcp://jxcore/SorderChildrenService";
		ISorderChildrenService isc = ProxyFactory.create(ISorderChildrenService.class, url);
		return isc;
	}
	public ISorderService getISorderService(){
		String url ="tcp://jxcore/SorderService";
		ISorderService iso = ProxyFactory.create(ISorderService.class, url);
		return iso;
	}
	public ISorderSuperService getSorderSuperService(){
		String url = "tcp://jxcore/SorderSuperService";
		ISorderSuperService ics = ProxyFactory.create(ISorderSuperService.class, url);
		return ics;
	}
	/**** SEM向老官网导流end*/
	
	/********新增旧版优惠券*******/
	public ICouponsService getCouponService(){
		ICouponsService ics = ProxyFactory.create(ICouponsService.class, "tcp://" + GAEA_UC + "/CouponsService");
		return ics;
	}
	
	public IOrderBFGService getOrderBFGService(){
		return ProxyFactory.create(IOrderBFGService.class, "tcp://" + GAEA_SERVICE + "/OrderBFGService");
	}
	
	/**友情链接*/
	public IFriendLinkService getFriendLinkService(){
		String url = "tcp://jxcore/FriendLinkService";
		return ProxyFactory.create(IFriendLinkService.class, url);
	}
	//小微说
	public IXwsSitemapService getIXwsSitemapService() {
		String url = "tcp://jxcore/XwsSitemapService";
		IXwsSitemapService ics = ProxyFactory.create(IXwsSitemapService.class, url);
		return ics;
	}
	
	/***** 商品包服务start*/
	public ILvzPackageSellService getPackageSellService(){
		return ProxyFactory.create(ILvzPackageSellService.class, "tcp://" + GAEA_SELL + "/LvzPackageSellService");
	}
	
	public ILvzPackageService getPackageService(){
		return ProxyFactory.create(ILvzPackageService.class, "tcp://" + GAEA_SELL + "/LvzPackageService");
	}
	/**商品包服务end*/

	/*菜单服务**/
	public ILvzMenuService getlvzMenuService(){
		return ProxyFactory.create(ILvzMenuService.class, "tcp://" + GAEA_UC + "/LvzMenuService");
	} 
	
	/*菜单配置服务**/
	public ILvzMenuConfService getlvzMenuConfService(){
		return ProxyFactory.create(ILvzMenuConfService.class, "tcp://" + GAEA_UC + "/LvzMenuConfService");
	} 
	
	/*商品配置服务**/
	public ILvzProductConfService getlvzProductMenuService(){
		return ProxyFactory.create(ILvzProductConfService.class, "tcp://" + GAEA_UC + "/LvzProductConfService");
	} 
	
	/*商标搜索*/
	public ILvTradeBaseService getTradeBaseService(){
	    String url = "tcp://" + SV_NAME_TRADE + "/LvTradeBaseService";
	    ILvTradeBaseService tradeBaseService = ProxyFactory.create(ILvTradeBaseService.class, url);
	    return tradeBaseService;
	  }
	
	/*地址服务**/
	public ILvzAddressConfService getlvzAddressConfService(){
		return ProxyFactory.create(ILvzAddressConfService.class, "tcp://" + GAEA_UC + "/LvzAddressConfService");
	} 
	
	/*地址排序服务**/
	public ILvzAddressAreaorderService getlvzAddressOrdersByService(){
		return ProxyFactory.create(ILvzAddressAreaorderService.class, "tcp://" + GAEA_UC + "/LvzAddressAreaorderService");
	} 

}
