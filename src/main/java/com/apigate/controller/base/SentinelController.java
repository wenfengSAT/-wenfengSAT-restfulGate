package com.apigate.controller.base;

import com.alibaba.csp.sentinel.slots.block.BlockException;

/**
 * 
 * @Description： 哨兵限流
 * 
 * @author [ wenfengSAT@163.com ] on [2023年8月7日上午11:15:19]
 * @Modified By： [修改人] on [修改日期] for [修改说明]
 *
 */
public abstract class SentinelController<T> {

	/**
	 * 
	 * @Description： 处理限流异常
	 * 
	 * @author [ wenfengSAT@163.com ]
	 * @Date [2023年8月7日上午11:15:30]
	 * @param e
	 * @throws BlockException
	 *
	 */
	public void handleFlowQpsException(BlockException e) throws BlockException {
		throw e;
	}

	/**
	 * 
	 * @Description： 接口发生 异常
	 * 
	 * @author [ wenfengSAT@163.com ]
	 * @Date [2023年8月7日上午11:15:40]
	 * @param e
	 * @throws Throwable
	 *
	 */
	public void apiFallback(Throwable e) throws Throwable {
		throw e;
	}
}
