package com.jx.hunter.lvzhengpc.vo;

import java.util.List;

/**
 * @author bruce
 * @date 2016-06-29
 * @email zhangyang226@gmail.com
 * @site http://blog.northpark.cn | http://northpark.cn | orginazation https://github.com/jellyband
 * 
 */

public class MenuShowVO {

private long menu_conf_id;

private long menu_id1;


private String menu_name1;


private String path1;


private Integer flag;//分类标记[1：使用 0：不使用]

private List<Menu2VO> menu2VOlist;

public long getMenu_conf_id() {
	return menu_conf_id;
}

public void setMenu_conf_id(long menu_conf_id) {
	this.menu_conf_id = menu_conf_id;
}

public long getMenu_id1() {
	return menu_id1;
}

public void setMenu_id1(long menu_id1) {
	this.menu_id1 = menu_id1;
}

public String getMenu_name1() {
	return menu_name1;
}

public void setMenu_name1(String menu_name1) {
	this.menu_name1 = menu_name1;
}


public String getPath1() {
	return path1;
}

public void setPath1(String path1) {
	this.path1 = path1;
}

public Integer getFlag() {
	return flag;
}

public void setFlag(Integer flag) {
	this.flag = flag;
}

public List<Menu2VO> getMenu2VOlist() {
	return menu2VOlist;
}

public void setMenu2VOlist(List<Menu2VO> menu2vOlist) {
	menu2VOlist = menu2vOlist;
}



		

}