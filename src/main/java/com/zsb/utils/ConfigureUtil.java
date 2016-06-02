package com.zsb.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.zsb.config.ConfInZookeeper;
import com.zsb.model.ZkParam;

/**
 * 配置工具
 * @date 2016年6月2日
 * @author zhoushanbin
 *
 */
public class ConfigureUtil {
	
	
	private static Map<String,ConfInZookeeper>  confMap = new ConcurrentHashMap<String, ConfInZookeeper>();
	
	public static ConfInZookeeper getConfInZookeeper(ZkParam param){
		
		String zkAddr = param.getZkAddr();
		String userName = param.getZkUser();
		String serviceId = param.getServiceId();
		String bisCode = param.getBisCode();
		
		String key = calcurateKey(zkAddr, userName, serviceId, bisCode);
		if(confMap.containsKey(key)){
			return confMap.get(key);
		}
		ConfInZookeeper conf = new ConfInZookeeper(param);
		confMap.put(key, conf);
		return conf;
		
	}
	
	private static String calcurateKey(String zkAddr,String userName,String serviceId,String bisCode){
		StringBuffer buf = new StringBuffer();
		buf.append(zkAddr).append("_");
		buf.append(userName).append("_");
		buf.append(serviceId).append("_");
		buf.append(bisCode);
		return buf.toString();
	}
}
