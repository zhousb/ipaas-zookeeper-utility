package com.zsb.model;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLBackgroundPathAndBytesable;
import org.apache.curator.framework.api.BackgroundPathAndBytesable;
import org.apache.curator.framework.api.BackgroundPathable;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;

import com.zsb.consts.Consts;

/**
 * Date: 2016年5月28日 <br>
 * 
 * @author zhoushanbin
 */
public class ZkClient {

	private String zkAddr;
	private int timeOut;
	private String authSchema;
	private String authInfo;
	private CuratorFramework client;

	public ZkClient(String zkAddr, int timeOut, String... auth)
			throws Exception {

		this.zkAddr = zkAddr;
		if (timeOut > 0) {
			this.timeOut = timeOut;
		}
		if ((null != auth) && (auth.length >= 2)) {
			if (!StringUtils.isBlank(auth[0])) {
				this.authSchema = auth[0];
			}
			if (!StringUtils.isBlank(auth[1])) {
				this.authInfo = auth[1];
			}
		}
		CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory
				.builder().connectString(this.zkAddr).namespace(Consts.IPAAS_NS)
				.connectionTimeoutMs(this.timeOut)
				.retryPolicy(new RetryNTimes(5, 10));
		if ((!StringUtils.isBlank(this.authSchema))
				&& (!StringUtils.isBlank(this.authInfo))) {
			builder.authorization(this.authSchema, this.authInfo.getBytes());
		}
		this.client = builder.build();
		this.client.start();
		this.client.blockUntilConnected(5, TimeUnit.SECONDS);
	}

	public String getNodeData(String nodePath, boolean watch) throws Exception {
		byte[] data;
		if (watch) {
			data = (byte[]) ((BackgroundPathable<?>) this.client.getData()
					.watched()).forPath(nodePath);
		} else {
			data = (byte[]) this.client.getData().forPath(nodePath);
		}
		if ((null == data) || (data.length <= 0)) {
			return null;
		}
		return new String(data, "UTF-8");
	}

	public String getNodeData(String nodePath) throws Exception {
		return getNodeData(nodePath, false);
	}

	public String getNodeData(String nodePath, Watcher watcher)
			throws Exception {
		byte[] data = getNodeBytes(nodePath, watcher);
		return new String(data, "UTF-8");
	}

	public byte[] getNodeBytes(String nodePath, Watcher watcher)
			throws Exception {
		byte[] bytes = null;
		if (null != watcher) {
			bytes = (byte[]) ((BackgroundPathable<?>) this.client.getData()
					.usingWatcher(watcher)).forPath(nodePath);
		} else {
			bytes = (byte[]) this.client.getData().forPath(nodePath);
		}
		return bytes;
	}

	public byte[] getNodeBytes(String nodePath) throws Exception {
		return getNodeBytes(nodePath, null);
	}

	public void createNode(String nodePath, String data, CreateMode createMode)
			throws Exception {
		createNode(nodePath, ZooDefs.Ids.OPEN_ACL_UNSAFE, data, createMode);
	}

	public void createNode(String nodePath, List<ACL> acls, String data,
			CreateMode createMode) throws Exception {
		byte[] bytes = null;
		if (!StringUtils.isBlank(data)) {
			bytes = data.getBytes("UTF-8");
		}
		createNode(nodePath, acls, bytes, createMode);
	}

	public void createNode(String nodePath, List<ACL> acls, byte[] data,
			CreateMode createMode) throws Exception {
		((BackgroundPathAndBytesable<?>) ((ACLBackgroundPathAndBytesable<?>) this.client
				.create().creatingParentsIfNeeded().withMode(createMode))
				.withACL(acls)).forPath(nodePath, data);
	}

	public void createNode(String nodePath, String data) throws Exception {
		createNode(nodePath, data, CreateMode.PERSISTENT);
	}

	public void setNodeData(String nodePath, String data) throws Exception {
		byte[] bytes = null;
		if (!StringUtils.isBlank(data)) {
			bytes = data.getBytes("UTF-8");
		}
		setNodeData(nodePath, bytes);
	}

	public void setNodeData(String nodePath, byte[] data) throws Exception {
		this.client.setData().forPath(nodePath, data);
	}

	@SuppressWarnings("unchecked")
	public List<String> getChildren(String nodePath, Watcher watcher)
			throws Exception {
		return (List<String>) ((BackgroundPathable<?>) this.client
				.getChildren().usingWatcher(watcher)).forPath(nodePath);
	}

	public void createSeqNode(String nodePath) throws Exception {
		((BackgroundPathAndBytesable<?>) ((ACLBackgroundPathAndBytesable<?>) this.client
				.create().creatingParentsIfNeeded()
				.withMode(CreateMode.PERSISTENT_SEQUENTIAL))
				.withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)).forPath(nodePath);
	}

	public boolean exists(String path) throws Exception {
		return null != this.client.checkExists().forPath(path);
	}

	public boolean exists(String path, Watcher watcher) throws Exception {
		if (null != watcher) {
			return null != ((BackgroundPathable<?>) this.client.checkExists().usingWatcher(watcher)).forPath(path);
		}
		return null != this.client.checkExists().forPath(path);
	}

	public boolean isConnected() {
		if ((null == this.client)
				|| (!CuratorFrameworkState.STARTED.equals(this.client
						.getState()))) {
			return false;
		}
		return true;
	}

	public void retryConnection() {
		this.client.start();
	}

	public List<String> getChildren(String path) throws Exception {
		return (List<String>) this.client.getChildren().forPath(path);
	}

	@SuppressWarnings("unchecked")
	public List<String> getChildren(String path, boolean watcher)
			throws Exception {
		if (watcher) {
			return (List<String>) ((BackgroundPathable<?>) this.client
					.getChildren().watched()).forPath(path);
		}
		return (List<String>) this.client.getChildren().forPath(path);
	}

	public void deleteNode(String path) throws Exception {
		this.client.delete().guaranteed().deletingChildrenIfNeeded()
				.forPath(path);
	}

	public void quit() {
		if ((null != this.client)
				&& (CuratorFrameworkState.STARTED
						.equals(this.client.getState()))) {
			this.client.close();
		}
	}

	public ZkClient addAuth(String authSchema, String authInfo)
			throws Exception {
		synchronized (ZkClient.class) {
			this.client.getZookeeperClient().getZooKeeper()
					.addAuthInfo(authSchema, authInfo.getBytes());
		}
		return this;
	}
	/**
	 * 分布式锁
	 * @param lockPath
	 * @return
	 */
	public InterProcessLock getInterProcessLock(String lockPath) {
		return new InterProcessMutex(this.client, lockPath);
	}
	
	public boolean isAlive(){
		//todo
		
		return true;
	}
}
