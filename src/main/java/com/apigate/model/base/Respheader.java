package com.apigate.model.base;

import java.io.Serializable;

import com.apigate.constant.ApigateRetCode;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @Description： 请求响应
 * 
 * @author [ wenfengSAT@163.com ] on [2023年8月7日上午11:38:22]
 * @Modified By： [修改人] on [修改日期] for [修改说明]
 *
 */
@Setter
@Getter
public class Respheader implements Serializable {

	private static final long serialVersionUID = 1L;

	private int code;

	private String msg;

	/**
	 * 操作成功返回参数
	 */
	public Respheader() {
		this.setCode(ApigateRetCode.SUCCESS.getCode());
		this.setMsg(ApigateRetCode.SUCCESS.getMsg());
	}

	/**
	 * 操作失败返回参数
	 * 
	 * @param errorCode 操作码
	 */
	public Respheader(ApigateRetCode errorCode) {
		this.setCode(errorCode.getCode());
		this.setMsg(errorCode.getMsg());
	}

	public Respheader(ApigateRetCode errorCode, Object... str) {
		this.setCode(errorCode.getCode());
		this.setMsg(errorCode.getFormat(str));
	}

}
