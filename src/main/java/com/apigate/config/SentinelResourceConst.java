package com.apigate.config;

/**
 * 
 * @Description： 限流接口资源
 * 
 * @author [ wenfengSAT@163.com ] on [2023年8月7日上午11:09:49]
 * @Modified By： [修改人] on [修改日期] for [修改说明]
 *
 */
public interface SentinelResourceConst {

	int QPS = 600;

	String login = "LoginController.login";
}
