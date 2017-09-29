package com.jx.hunter.lvzhengpc.controllers;

import java.util.List;

import com.jx.argo.ActionResult;
import com.jx.argo.annotations.Path;
import com.jx.hunter.lvzhengpc.common.LocationPage;
import com.jx.hunter.lvzhengpc.service.SitemapService;
import com.jx.hunter.lvzhengpc.vo.FAQSiteMapVO;

/**
 * 网站地图
 * @author zx
 *
 */
public class SitemapController extends CommonController{
	
	@Path("sitemap")
	public ActionResult getSitemap(){
		//商品类别
		//小微问答
		List<FAQSiteMapVO> wendalist=new SitemapService().getFAQSiteMap();
		model().add("faqSitemap", wendalist);
		
		//小微咨询
		List<FAQSiteMapVO> articlelist=new SitemapService().getArticleSiteMap();
		model().add("articleSitemap", articlelist);
		
		model().add("LocationPage", new LocationPage("sitemap","index"));
		return view("/index");
	}

}
