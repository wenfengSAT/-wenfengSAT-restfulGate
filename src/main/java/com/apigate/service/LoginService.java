package com.apigate.service;

import com.apigate.model.LoginReq;
import com.apigate.util.JsonResult;

/**
 * 
 * @Description： 登录
 * 
 * @author [ wenfengSAT@163.com ] on [2023年8月7日上午11:12:29]
 * @Modified By： [修改人] on [修改日期] for [修改说明]
 *
 */
public interface LoginService {

	/**
	 * 
	 * @Description： 登录
	 * 
	 * @author [ wenfengSAT@163.com ]
	 * @Date [2023年8月7日上午11:14:06]
	 * @param req
	 * @return
	 *
	 */
	JsonResult login(LoginReq req);
}
