package com.apigate.config;

import lombok.Getter;

/**
 * 
 * @Description： 限流接口资源
 * 
 * @author [ wenfengSAT@163.com ] on [2023年9月15日上午10:46:08]
 * @Modified By： [修改人] on [修改日期] for [修改说明]
 *
 */
@Getter
public enum SentinelResourceEnum {

	Login(500, "LoginController.login", "登录接口"),;

	private int qps;
	private String resource;
	private String desc;

	SentinelResourceEnum(int qps, String resource, String desc) {
		this.qps = qps;
		this.resource = resource;
		this.desc = desc;
	}
}
