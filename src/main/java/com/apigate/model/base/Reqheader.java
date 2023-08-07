package com.apigate.model.base;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @Description： 请求头
 * 
 * @author [ wenfengSAT@163.com ] on [2023年8月7日上午11:38:11]
 * @Modified By： [修改人] on [修改日期] for [修改说明]
 *
 */
@Setter
@Getter
public class Reqheader implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 请求流水
	 */
	private String reqSeq;

	/**
	 * 时间戳
	 */
	private long timestamp;

	/**
	 * 签名:签名值，对参数进行签名，防篡改
	 */
	private String sign;

}
