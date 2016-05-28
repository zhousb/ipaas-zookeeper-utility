package com.zsb.zk;

import java.util.concurrent.ConcurrentHashMap;

import com.zsb.model.ZkClient;

/**
 * zk 连接池 Date: 2016年5月28日 <br>
 * 
 * @author zhoushanbin
 */
public class ZkPool {

	private static ConcurrentHashMap<String, ZkClient> clients = new ConcurrentHashMap<String, ZkClient>();

	public ZkClient getZkClient(String zkAddr, String zkUserName)
			throws Exception {
		return (ZkClient) clients.get(appendKey(zkAddr, zkUserName));
	}

	public ZkClient getZkClient(String zkAddr, String zkUserName,
			String serviceId) throws Exception {
		return (ZkClient) clients.get(appendKey(zkAddr, zkUserName, serviceId));
	}

	private String appendKey(String zkAddr, String zkUserName) {
		return zkAddr + "-" + zkUserName;
	}

	private String appendKey(String zkAddr, String zkUserName, String serviceId) {
		return zkAddr + "-" + zkUserName + "-" + serviceId;
	}

	public void addZkClient(String zkAddr, String zkUserName, ZkClient zkClient) {
		clients.put(appendKey(zkAddr, zkUserName), zkClient);
	}

	public void addZkClient(String zkAddr, String zkUserName, String serviceId,
			ZkClient zkClient) {
		clients.put(appendKey(zkAddr, zkUserName, serviceId), zkClient);
	}

	public boolean exist(String zkAddr, String zkUserName) {
		return clients.containsKey(appendKey(zkAddr, zkUserName));
	}

	public boolean exist(String zkAddr, String zkUserName, String serviceId) {
		return clients.containsKey(appendKey(zkAddr, zkUserName, serviceId));
	}
	
}
