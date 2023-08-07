package com.apigate.exception;

import com.apigate.constant.CodeEnum;

import lombok.Getter;

/**
 * 
 * @Description： 基础异常，所有自定义异常父类
 * 
 * @author [ wenfengSAT@163.com ] on [2023年8月7日上午11:18:29]
 * @Modified By： [修改人] on [修改日期] for [修改说明]
 *
 */
@Getter
public class BaseException extends RuntimeException {

	private static final long serialVersionUID = 2746456762819176173L;

	/**
	 * 返回码
	 */
	protected CodeEnum responseEnum;
	/**
	 * 异常消息参数
	 */
	protected Object[] args;

	public BaseException() {
		super();
	}

	public BaseException(CodeEnum responseEnum) {
		super(responseEnum.getMsg());
		this.responseEnum = responseEnum;
	}

	public BaseException(int code, String msg) {
		super(msg);
		this.responseEnum = new CodeEnum() {
			@Override
			public int getCode() {
				return code;
			}

			@Override
			public String getMsg() {
				return msg;
			}
		};
	}

	public BaseException(CodeEnum responseEnum, Object[] args, String message) {
		super(message);
		this.responseEnum = responseEnum;
		this.args = args;
	}

	public BaseException(CodeEnum responseEnum, Object[] args, String message, Throwable cause) {
		super(message, cause);
		this.responseEnum = responseEnum;
		this.args = args;
	}

}
