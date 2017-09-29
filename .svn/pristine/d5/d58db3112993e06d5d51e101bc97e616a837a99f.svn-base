package com.jx.hunter.lvzhengpc.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.jx.argo.ArgoTool;
import com.jx.blackface.gaea.sell.entity.LvzProductCateEntity;
import com.jx.blackface.gaea.sell.entity.LvzProductEntity;
import com.jx.blackface.gaea.usercenter.entity.LvzMenuConfEntity;
import com.jx.blackface.gaea.usercenter.entity.LvzMenuEntity;
import com.jx.blackface.gaea.usercenter.entity.LvzProductConfEntity;
import com.jx.hunter.lvzhengpc.common.MemcachedUtil;
import com.jx.hunter.lvzhengpc.frame.RSBLL;
import com.jx.hunter.lvzhengpc.vo.Bannervo;
import com.jx.hunter.lvzhengpc.vo.Menu2VO;
import com.jx.hunter.lvzhengpc.vo.MenuShowVO;

/**
 * 
 *  去掉了以前的复杂写法，优化菜单读取流程。
 *  在base里面先从缓存读取，没有就查数据库。
 * @author update by bruce
 * @date 2016-07-06 
 * 
 * 
 * */
public class MenuUtils {
	public static MenuUtils menuUtils = new MenuUtils();
	
	

	
	
	/**
	 * 获取菜单数据并放到缓存
	 */
	public List<MenuShowVO>  getMenuList(){
		List<MenuShowVO> volist  = new ArrayList<MenuShowVO>();
		try {
			
			Set<Long> set1 = new HashSet<Long>();
			List<LvzMenuConfEntity> m1 = new ArrayList<LvzMenuConfEntity>();
			List<LvzMenuConfEntity> l1 = RSBLL.getstance().getlvzMenuConfService().getLvzMenuConfEntity(" shelf=1 ", 1, 99, " first_order ");
			if(l1!=null){
				if(l1.size()>0){
					for (int i = 0; i < l1.size(); i++) {
						if(set1.add(l1.get(i).getFirst_menu_id())){
							m1.add(l1.get(i));
						}else{
							continue;
						}
					}
				}
			}
			
			volist = converlist2VO(m1);
			MemcachedUtil.set("menulist", volist);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		//把list转化成volist
		return volist;
	}
	
	
	/**
	 * 把list转化成vo
	 * @param m1
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	public List<MenuShowVO> converlist2VO(List<LvzMenuConfEntity> m1) throws Exception, NumberFormatException {
		List<MenuShowVO> volist  =  new ArrayList<MenuShowVO>();
		if(m1!=null){
			if(m1.size()>0){
				
				for (int i = 0; i < m1.size(); i++) {
					LvzMenuConfEntity mc = m1.get(i);
					MenuShowVO vo = new MenuShowVO();
					//设置常规属性
					vo.setMenu_id1(mc.getFirst_menu_id());
					vo.setFlag(mc.getFlag());
					vo.setMenu_conf_id(mc.getMenu_conf_id());
					LvzMenuEntity menu1 = RSBLL.getstance().getlvzMenuService().getLvzMenuEntityById(mc.getFirst_menu_id());
					if(menu1!=null){
						//设置名称
						vo.setMenu_name1(menu1.getMenu_name());
						//设置路径
						if(1==menu1.getType()){//商品页
							long pid = Long.valueOf(menu1.getPath());
							String path = "/commondetail/detail/"+pid+".html";
							vo.setPath1(path);
						}else if(2==menu1.getType()){//列表页
							long pid = Long.valueOf(menu1.getPath());
							LvzProductCateEntity catM = RSBLL.getstance().getLvzProductCateService().getProductCateEntityById(pid);
							long ppid = catM.getParent_cateid();
								String path = "/navigation/index/"+ppid+".html";
								vo.setPath1(path);
						}else{
							vo.setPath1(menu1.getPath());
						}
					}
					
					//查询一级菜单对应的二级菜单
					List<Menu2VO> vo2list = new ArrayList<Menu2VO>();
					List<LvzMenuConfEntity> l2 = RSBLL.getstance().getlvzMenuConfService().getLvzMenuConfEntity(" shelf=1 and first_menu_id="+mc.getFirst_menu_id(), 1, 99, " second_order ");
					if(l2!=null){
						if(l2.size()>0){
							for (int j = 0; j < l2.size(); j++) {
								LvzMenuConfEntity mc2 = l2.get(j);
								Menu2VO vo2 = new Menu2VO();
								//设置常规属性
								vo2.setMenu_id2(mc2.getSecond_menu_id());
								vo2.setFlag(mc2.getFlag());
								vo2.setMenu_conf_id(mc2.getMenu_conf_id());
								LvzMenuEntity menu2 = RSBLL.getstance().getlvzMenuService().getLvzMenuEntityById(mc2.getSecond_menu_id());
								if(menu2!=null){
									//设置名称
									vo2.setMenu_name2(menu2.getMenu_name());
									//设置路径
									if(1==menu2.getType()){//商品页
										long pid = Long.valueOf(menu2.getPath());
										String path = "/commondetail/detail/"+pid+".html";
										vo2.setPath2(path);
									}else if(2==menu2.getType()){//列表页
										long pid = Long.valueOf(menu2.getPath());
										LvzProductEntity pm = RSBLL.getstance().getLvzProductService().getProductEntityById(pid);
										if(pm!=null){
											String parent_cate_code = pm.getParent_cate_code();
											long catid = RSBLL.getstance().getLvzProductCateService().getCateIdByCateCode(parent_cate_code);
											
											
											LvzProductCateEntity catM = RSBLL.getstance().getLvzProductCateService().getProductCateEntityById(catid);
											
											long ppid = catM.getParent_cateid();
											String path = "/navigation/index/"+ppid+".html";
											vo2.setPath2(path);
										}
									}else{
										vo2.setPath2(menu2.getPath());
									}
								}
								//查询商品列表
								List<LvzProductConfEntity> prolist = RSBLL.getstance().getlvzProductMenuService().getLvzProductConfEntity(" menu_conf_id = "+mc2.getMenu_conf_id()+" and shelf=1 ", 1, 99, "orderindex");
								if(null != prolist){
									if(prolist.size()>0){
										for (int j1 = 0; j1 < prolist.size(); j1++) {
											LvzProductConfEntity pp = prolist.get(j1);
											if(!(3==pp.getPathtype())){
												pp.setPath("/commondetail/detail/"+pp.getPath()+".html");
											}
										}
									}
								}
								vo2.setProlist(prolist);
								
							vo2list.add(vo2);
						}
					}
					
					
				}
					
					
				//设置二级菜单	
				vo.setMenu2VOlist(vo2list);
				volist.add(vo);
					
					
			}
		}
		}
		return volist;
	}
	
	
	
	
	/**
	 * 菜单查找banner
	 * @return
	 */
	public static List<Bannervo> getBanners(){
		String memkey = "baner_memkey_pclvzheng";
		//Properties pro = (Properties) MemcachedUtil.get(memkey);
		List<Bannervo> list = (List<Bannervo>) MemcachedUtil.get(memkey);
		
		if(list == null || list.size() == 0){
			list = new ArrayList<Bannervo>();
			String url = ArgoTool.getConfigFolder()+ArgoTool.getNamespace() + "/banner.properties";// + fileName;
			InputStream in = null;
			try {
				in = new BufferedInputStream(new FileInputStream(url));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			Properties pro = new Properties(); 
			if(in != null){
				try {
					pro.load(in);
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
			Set keyset = pro.keySet();
			Iterator it = keyset.iterator();
			while(it.hasNext()){
				Bannervo vo = new Bannervo();
				String key = (String) it.next();
				if(null != key && !"".equals(key)){
					String vstr = (String) pro.get(key);
					String[] array = vstr.split(",");
					vo.setConurl(array[1]);
					vo.setImgurl(array[0]);
					list.add(vo);
				}
			}
			MemcachedUtil.set(memkey, list, new Date(60*1000));
		}
		
		return list;
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////
	
	public static void main(String[] args) {
		//清空菜单缓存数据
		MemcachedUtil.delete("menulist");
	}
}
