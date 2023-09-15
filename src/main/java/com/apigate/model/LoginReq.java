package com.apigate.model;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.apigate.model.base.Reqheader;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @Description： 登录
 * 
 * @author [ wenfengSAT@163.com ] on [2023年8月7日下午3:35:03]
 * @Modified By： [修改人] on [修改日期] for [修改说明]
 *
 */
@Setter
@Getter
public class LoginReq implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("header")
	@Valid
	@NotNull(message = "头信息不能为空")
	private Reqheader header;

	@JsonProperty("body")
	@Valid
	@NotNull(message = "请求体不能为空")
	private LoginReqMsgbody body;

	@Setter
	@Getter
	public static class LoginReqMsgbody implements Serializable {

		private static final long serialVersionUID = 1L;

		@NotEmpty(message = "用户ID不能为空")
		private String uid;
	}

}
