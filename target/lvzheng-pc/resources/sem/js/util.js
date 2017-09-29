/*******************************************************************************
 * 基础函数列表
 ******************************************************************************/
/**
 * 删除首尾空格
 */
function trim(s) {
	return s.replace(/(^\s*)|(\s*$)/g, "");
}

/**
 * 为空
 */
function isEmpty(s) {
	if (s == undefined || s == null) {
		return true;
	}

	if (typeof (s) == 'string' && trim(s).length == 0) {
		return true;
	}

	return false;
}

/**
 * 不为空
 */
function isNotEmpty(s) {
	return !isEmpty(s);
}

/**
 * 是否包含
 */
function isContain(srcStr, containStr) {
	if (isEmpty(srcStr)) return false;
	
	var srcVal = srcStr.split(",");
	for(var i = 0; i < srcVal.length; i++){
		if(srcVal[i]==containStr){
			return true;
		}
	}
	
	return false;
}

/**
 * 复制对象
 */
function clone(srcObj) {
	if (typeof(srcObj) != 'object') {
		return srcObj;
	} else {
		var newObj = new Object();
		for (key in srcObj) {
			eval("newObj." + key + " = srcObj." + key);
		}
		
		return newObj;
	}
}

/**
 * 检查手机号格式
 * @param phone
 */
function checkPhoneNumber(phone){
	if(isEmpty(phone) || !(/^1[3|4|5|7|8][0-9]\d{8}$/.test(phone))){
		return false;
	}
	return true;
}

/**
 * 检查手机号格式 自带提示
 * @param phone
 */
function checkPhoneNumberAlert(phone){
	if(isEmpty(phone) || !(/^1[3|4|5|7|8][0-9]\d{8}$/.test(phone))){
		alert("请输入正确的手机号码!");
		return false;
	}
	return true;
}