package com.jx.hunter.lvzhengpc.service;

import java.util.ArrayList;
import java.util.List;

import com.jx.hunter.lvzhengpc.frame.RSBLL;
import com.jx.hunter.lvzhengpc.vo.FAQSiteMapVO;
import com.jx.service.newcore.contract.IXwsSitemapService;
import com.jx.service.newcore.entity.XwsSitemap;

public class SitemapService {
	
	private  IXwsSitemapService xwsSitemapService=RSBLL.getstance().getIXwsSitemapService();
	
	public List<FAQSiteMapVO> getFAQSiteMap(){
		List<XwsSitemap> list=null;
		try {
			list=xwsSitemapService.getXwsSitemapList("channel=2 and pid = 0", 1, 99, "sort_value");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(list == null || list.size()<=0)
			return null;
		
		List<FAQSiteMapVO> faqSitemaplist=new ArrayList<FAQSiteMapVO>();
		
		for(XwsSitemap sitemap : list){
			FAQSiteMapVO faqFirstCate=new FAQSiteMapVO();
			faqFirstCate.setName(sitemap.getName());
			faqFirstCate.setUrl("http://zhishi.lvzheng.com/wenda/"+sitemap.getUrl());
			List secondlist=getSecondCateByPid(sitemap.getId());
			
			if(secondlist != null && secondlist.size()>0){
				faqFirstCate.setSecondlist(secondlist);
			}
			faqSitemaplist.add(faqFirstCate);
		}
		return faqSitemaplist;
	}
	
	public List<FAQSiteMapVO> getSecondCateByPid(String pid){
		List<FAQSiteMapVO> listresult=new ArrayList<FAQSiteMapVO>();
		List<XwsSitemap> list=null;
		try {
			list=xwsSitemapService.getXwsSitemapList("channel=2 and pid = "+pid , 1, 99, "sort_value");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(list != null && list.size()>0){
			for(XwsSitemap sitemap: list){
				FAQSiteMapVO faqFirstCate=new FAQSiteMapVO();
				faqFirstCate.setName(sitemap.getName());
				faqFirstCate.setUrl("http://zhishi.lvzheng.com/wenda/"+sitemap.getUrl());
				List secondlist=getSecondCateByPid(sitemap.getId());
				if(secondlist != null && secondlist.size()>0){
					faqFirstCate.setSecondlist(secondlist);
				}
				listresult.add(faqFirstCate);
			}
		}
		return  listresult;
	}
	/**
	 * 小微咨询
	 * @return
	 */
	public List<FAQSiteMapVO> getArticleSiteMap(){
		List<XwsSitemap> list=null;
		try {
			list=xwsSitemapService.getXwsSitemapList("channel=2 and pid = 0", 1, 99, "sort_value");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(list == null || list.size()<=0)
			return null;
		
		List<FAQSiteMapVO> faqSitemaplist=new ArrayList<FAQSiteMapVO>();
		
		for(XwsSitemap sitemap : list){
			FAQSiteMapVO faqFirstCate=new FAQSiteMapVO();
			faqFirstCate.setName(sitemap.getName());
			faqFirstCate.setUrl("http://zhishi.lvzheng.com/"+sitemap.getUrl());
			List secondlist=getArticleCateByPid(sitemap);
			
			if(secondlist != null && secondlist.size()>0){
				faqFirstCate.setSecondlist(secondlist);
			}
			faqSitemaplist.add(faqFirstCate);
		}
		return faqSitemaplist;
	}
	
	public List<FAQSiteMapVO> getArticleCateByPid(XwsSitemap first){
		List<FAQSiteMapVO> listresult=new ArrayList<FAQSiteMapVO>();
		List<XwsSitemap> list=null;
		try {
			list=xwsSitemapService.getXwsSitemapList("channel=2 and pid = "+first.getId() , 1, 99, "sort_value");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(list != null && list.size()>0){
			for(XwsSitemap sitemap: list){
				FAQSiteMapVO faqFirstCate=new FAQSiteMapVO();
				faqFirstCate.setName(sitemap.getName());
				faqFirstCate.setUrl("http://zhishi.lvzheng.com/"+first.getUrl()+"/"+sitemap.getUrl());
				List secondlist=getSecondCateByPid(sitemap.getId());
				if(secondlist != null && secondlist.size()>0){
					faqFirstCate.setSecondlist(secondlist);
				}
				listresult.add(faqFirstCate);
			}
		}
		return  listresult;
	}


}
