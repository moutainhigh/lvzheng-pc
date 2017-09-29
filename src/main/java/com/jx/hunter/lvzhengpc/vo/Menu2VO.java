package com.jx.hunter.lvzhengpc.vo;

import java.util.List;

import com.jx.blackface.gaea.usercenter.entity.LvzProductConfEntity;

/**
 * @author bruce
 * @date 2016-06-29
 * @email zhangyang226@gmail.com
 * @site http://blog.northpark.cn | http://northpark.cn | orginazation https://github.com/jellyband
 * 
 */

public class Menu2VO {

private long menu_conf_id;

private long menu_id2;


private String menu_name2;


private String menu_cateid2;

private String path2;


private Integer flag;//分类标记[1：使用 0：不使用]

private List<LvzProductConfEntity> prolist;

public long getMenu_conf_id() {
	return menu_conf_id;
}

public void setMenu_conf_id(long menu_conf_id) {
	this.menu_conf_id = menu_conf_id;
}

public long getMenu_id2() {
	return menu_id2;
}

public void setMenu_id2(long menu_id2) {
	this.menu_id2 = menu_id2;
}

public String getMenu_name2() {
	return menu_name2;
}

public void setMenu_name2(String menu_name2) {
	this.menu_name2 = menu_name2;
}

public String getMenu_cateid2() {
	return menu_cateid2;
}

public void setMenu_cateid2(String menu_cateid2) {
	this.menu_cateid2 = menu_cateid2;
}

public String getPath2() {
	return path2;
}

public void setPath2(String path2) {
	this.path2 = path2;
}

public Integer getFlag() {
	return flag;
}

public void setFlag(Integer flag) {
	this.flag = flag;
}

public List<LvzProductConfEntity> getProlist() {
	return prolist;
}

public void setProlist(List<LvzProductConfEntity> prolist) {
	this.prolist = prolist;
}




		

}