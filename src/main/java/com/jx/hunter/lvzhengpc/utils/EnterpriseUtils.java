/**
 * 
 */

package com.jx.hunter.lvzhengpc.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.jx.argo.BeatContext;
import com.jx.blackface.tools.webblack.auth.AuthHelper;
import com.jx.blackface.tools.webblack.checkname.CheckNameHelper;
import com.jx.blackface.tools.webblack.checkname.entity.FoundCheckEntity;
import com.jx.blackface.tools.webblack.query.qichacha.QichachaHelper;
import com.jx.blackface.tools.webblack.query.qichacha.entity.QichachaEntity;
import com.jx.service.messagecenter.util.DateUtils;



/**
 * simple introduction
 *
 * <p>detailed comment</p>
 * @author chuxuebao 2016年3月22日
 * @see
 * @since 1.0
 */

public class EnterpriseUtils {

	/**
	 * 获取登录信息
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> getLoginInfo(BeatContext beat) throws Exception{
		// TODO
		String ipAddr = "";
		String userId = "";
		Map<String, String> loginInfo = new HashMap<String, String>();
		loginInfo.put("userId", userId);
		loginInfo.put("IP", ipAddr);
		return loginInfo;
	}
	
	public static String foundCheck(String industryCharacteristics, String mainBusinessUniteCode, String mainBusinessCode, String fullName, String shopName){
		String sessionId = null;
		try {
			sessionId = AuthHelper.postLogin();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String foundCheck = null;
		if(StringUtils.isNotBlank(sessionId)){
			FoundCheckEntity foundCheckEntity = new FoundCheckEntity();
			foundCheckEntity.setHangye(industryCharacteristics);
			foundCheckEntity.setHbdm(mainBusinessUniteCode);
			foundCheckEntity.setHydm(mainBusinessCode);
			foundCheckEntity.setQuanming(fullName);
			foundCheckEntity.setShijian(DateUtils.getFormatDateStr(DateUtils.getCurrentDate(), DateUtils.DATA_FORMAT_YYYY_MM_DD_HH_MM_SS) );
			foundCheckEntity.setZihao(shopName);
			foundCheckEntity.setZihaohangye(shopName + industryCharacteristics);
			foundCheckEntity.setSuperNameId("");
			try {
				foundCheck = CheckNameHelper.foundCheck(sessionId, foundCheckEntity);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		List<QichachaEntity> searchEnterprise = null;
		if(StringUtils.isBlank(foundCheck)){
			// 工商局又不能用了
			try {
				searchEnterprise = QichachaHelper.searchEnterprise(shopName, "北京");
			} catch (Exception e) {
				e.printStackTrace();
				foundCheck = "{\"errorNum\":1,\"jyz\":[],\"cc\":[{\"entName\":\"系统繁忙\",\"rule\":\"请稍后再试\"}]}";
			}
		}
		if(StringUtils.isBlank(foundCheck)){
			if(searchEnterprise != null && !searchEnterprise.isEmpty()){
				for(QichachaEntity qichachaEntity:searchEnterprise){
					if(!StringUtils.equals(qichachaEntity.getEntStatus(), "吊销")){
						foundCheck = "{\"errorNum\":1,\"jyz\":[],\"cc\":[{\"entName\":\"" + qichachaEntity.getName() + "\",\"rule\":\"全行业字号_查重列表(查重)\"}]}";
						break;
					}
				}
			}else{
				foundCheck = "{\"errorNum\":0,\"jyz\":[],\"cc\":[]}";
			}
		}
		if(StringUtils.isBlank(foundCheck)){
			foundCheck = "{\"errorNum\":1,\"jyz\":[],\"cc\":[{\"entName\":\"系统繁忙\",\"rule\":\"请稍后再试\"}]}";
		}
		return foundCheck;
	}
}
