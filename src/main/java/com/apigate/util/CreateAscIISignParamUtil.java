package com.apigate.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Description： 生成参数 字典排序
 * 
 * @author [ wenfengSAT@163.com ] on [2023年8月7日下午3:35:44]
 * @Modified By： [修改人] on [修改日期] for [修改说明]
 *
 */
@Slf4j
public class CreateAscIISignParamUtil {

	/**
	 * 
	 * @Description： 生成参数 字典排序
	 * 
	 * @author [ Wenfeng.Huang@desay-svautomotive.com ]
	 * @Date [2021年6月16日下午2:31:17]
	 * @param map
	 * @return
	 *
	 */
	public static String getSignParam(Map<String, Object> map) {
		String result = "";
		try {
			List<Map.Entry<String, Object>> infoIds = new ArrayList<Map.Entry<String, Object>>(map.entrySet());
			// 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
			Collections.sort(infoIds, new Comparator<Map.Entry<String, Object>>() {
				public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
					return (o1.getKey()).toString().compareTo(o2.getKey());
				}
			});
			// 构造签名键值对的格式
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, Object> item : infoIds) {
				if (StrUtil.isNotEmpty(item.getKey())) {
					String key = item.getKey();
					String val = StrUtil.toString(item.getValue());
					if (StrUtil.isNotEmpty(val)) {
						sb.append(key + "=" + val + "&");
					}
				}
			}
			result = StrUtil.sub(sb, 0, sb.length() - 1);
		} catch (Exception e) {
			log.error("CreateAscIISignParamUtil error = [{}]", e.getMessage(), e);
			return null;
		}
		return result;
	}
}
