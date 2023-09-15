package com.apigate.config;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

import cn.hutool.core.collection.CollectionUtil;

/**
 * 
 * @Description： Sentinel切面类配置
 * 
 * @author [ wenfengSAT@163.com ] on [2023年8月7日上午11:16:35]
 * @Modified By： [修改人] on [修改日期] for [修改说明]
 *
 */
@Configuration
public class SentinelAspectConfiguration {

	@Bean
	public SentinelResourceAspect sentinelResourceAspect() {
		return new SentinelResourceAspect();
	}

	/**
	 * 
	 * @Description： 初始化限流规则
	 * 
	 * @author [ wenfengSAT@163.com ]
	 * @Date [2023年8月7日上午11:16:46]
	 * @throws Exception
	 *
	 */
	@PostConstruct
	private void initRules() throws Exception {
		List<FlowRule> ruleList = CollectionUtil.newArrayList();
		for (SentinelResourceEnum SentinelResourcem : SentinelResourceEnum.values()) {
			FlowRule flowRule = new FlowRule();
			flowRule.setResource(SentinelResourcem.getResource());
			flowRule.setCount(SentinelResourcem.getQps());// 限流阈值 qps=10
			flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);// 限流阈值类型（QPS 或并发线程数）
			flowRule.setLimitApp("default");// 流控针对的调用来源，若为 default 则不区分调用来源
			// 流量控制手段（直接拒绝、Warm Up、匀速排队）
			flowRule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER);
			ruleList.add(flowRule);
		}
		FlowRuleManager.loadRules(ruleList);
	}

}
