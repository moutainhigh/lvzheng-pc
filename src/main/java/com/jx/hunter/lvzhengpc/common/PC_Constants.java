package com.jx.hunter.lvzhengpc.common;

/**
 * @author zhangyang
 *
 */
public class PC_Constants {
	/**
	 * PC常量、枚举定义
	 * @author bruce
	 * @date 2016年7月20日
	 * @email zhangyang226@gmail.com
	 * @site http://blog.northpark.cn | http://northpark.cn | orginazation https://github.com/jellyband
	 * 
	 */
	
	/**
	 * 渠道来源
	 * 用户来源(1：微信。2：58同城。3：线下推广，4新官网注册     25,26 自营系统录单 [666-新用户礼包活动PC] [777-新用户礼包活动M]  )
	 * @author bruce
	 * 
	 */
	public enum Channel {

		WX(1, "微信"), 
		WUBA(2, "58同城"), 
		OFFLINE(3, "线下推广"), 
		PC_NEW(4, "新官网注册"),
		GITF_PC(666,"新用户礼包活动PC"),
		GIFT_M(777,"新用户礼包活动M"),
		GITF_EMAIL(888,"新用户礼包活动PC--邮件"),
		GITF_MID(815,"新用户礼包活动PC--邮件"),
		;

		// 成员变量
		public int key;
		public String value;

		// 构造方法
		private Channel(int key, String value) {
			this.key = key;
			this.value = value;
		}

		// 覆盖方法
		@Override
		public String toString() {
			return this.key + "_" + this.value;
		}
	}
}
