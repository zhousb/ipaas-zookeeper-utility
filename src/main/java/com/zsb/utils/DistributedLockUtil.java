package com.zsb.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.zsb.lock.DistributedLock;
import com.zsb.model.ZkParam;

/**
 * 获取锁工具类
 * @date 2016年6月2日
 * @author zhoushanbin
 *
 */
public final class DistributedLockUtil {
	
	private  static Map<String,DistributedLock> lockMap = new ConcurrentHashMap<String,DistributedLock>();
	
	/**
	 * 获取锁
	 * @param param
	 * @return
	 */
	public static DistributedLock getDistributedLock(ZkParam param){
		
		String zkAddr = param.getZkAddr();
		String userName = param.getZkUser();
		String serviceId = param.getServiceId();
		String bisCode = param.getBisCode();
		String pwd = param.getZkPwd();
		
		String key = calcurateKey(zkAddr, userName, serviceId, bisCode);
		if(lockMap.containsKey(key)){
			return lockMap.get(key);
		}
		DistributedLock lock = new DistributedLock(zkAddr, userName, pwd, serviceId, bisCode);
		lockMap.put(key, lock);
		return lock;
	}
	
	
	/**
	 * 获取锁
	 * @param zkAddr
	 * @param userName
	 * @param pwd
	 * @param serviceId
	 * @param bisCode
	 * @return
	 */
	public static DistributedLock getDistributedLock(String zkAddr,String userName,String pwd,String serviceId,String bisCode){
		String key = calcurateKey(zkAddr, userName, serviceId, bisCode);
		if(lockMap.containsKey(key)){
			return lockMap.get(key);
		}
		DistributedLock lock = new DistributedLock(zkAddr, userName, pwd, serviceId, bisCode);
		lockMap.put(key, lock);
		return lock;
		
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