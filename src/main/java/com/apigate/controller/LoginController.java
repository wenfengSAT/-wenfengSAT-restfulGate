package com.apigate.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSON;
import com.apigate.controller.base.SentinelController;
import com.apigate.model.LoginReq;
import com.apigate.service.LoginService;
import com.apigate.util.JsonResult;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Description： 登录
 * 
 * @author [ wenfengSAT@163.com ] on [2023年8月7日上午11:10:49]
 * @Modified By： [修改人] on [修改日期] for [修改说明]
 *
 */
@Slf4j
@RestController
@RequestMapping("v1")
public class LoginController extends SentinelController<JsonResult> {

	@Autowired
	private LoginService loginService;

	/**
	 * 
	 * @Description： 登录
	 * 
	 * @author [ wenfengSAT@163.com ]
	 * @Date [2023年8月7日上午11:11:22]
	 * @param req
	 * @return
	 *
	 */
	@PostMapping(value = "/app/acct/login")
	@SentinelResource(value = "LoginController.login", blockHandler = "handleFlowQpsException", fallback = "apiFallback")
	public JsonResult login(@Valid @RequestBody LoginReq req) {
		JsonResult resp = loginService.login(req);
		log.info("login param:[{}] result:[{}]", JSON.toJSONString(req), JSON.toJSONString(resp));
		return resp;
	}

}
