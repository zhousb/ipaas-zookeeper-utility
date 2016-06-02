package com.zsb.lock;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.zsb.consts.Consts;
import com.zsb.crypto.CryptoUtils;
import com.zsb.event.ZkWatchEvent;
import com.zsb.event.ZkWatcher;
import com.zsb.exception.ZkException;
import com.zsb.model.Namespace;
import com.zsb.model.ZkClient;
import com.zsb.utils.NamespaceUtils;
import com.zsb.utils.ZookeeperUtils;
import com.zsb.zk.ZkPool;
import com.zsb.zk.ZkPoolFactory;

/**
 * 分布式zk锁
 * @date 2016年5月31日
 * @author zhoushanbin
 *
 */
public class DistributedLock {
	
    private static final Logger LOG =  LoggerFactory.getLogger(DistributedLock.class);
    
    private ZkPool zkPool;
    private ZkClient zkClient;
    
    private String bisCode;
    private String bisCodeMD5;
    private String userName;
    private String pwd;
    private String serviceId;
    private Namespace ns;
    private List<ACL> acls;
    
    private String lockName;
    
    
    public DistributedLock(String zkAddress,String bisCode){
    	try {
			zkPool = ZkPoolFactory.getZkPool(zkAddress);
			zkClient = zkPool.getZkClient(zkAddress,"");
			this.bisCode = bisCode;
			init();
		} catch (Exception e) {
			LOG.error("创建DistributedLock失败");
		}
    }
    
    public DistributedLock(String zkAddress,int timeOut,String bisCode){
    	try {
			zkPool = ZkPoolFactory.getZkPool(zkAddress,timeOut);
			zkClient = zkPool.getZkClient(zkAddress,"");
			this.bisCode = bisCode;
			init();
		} catch (Exception e) {
			LOG.error("创建DistributedLock失败");
		}
    }
    
    public DistributedLock(String zkAddress,int timeOut,String bisCode,String ...authInfo){
    	try {
			zkPool = ZkPoolFactory.getZkPool(zkAddress, timeOut, authInfo);
			zkClient = zkPool.getZkClient(zkAddress, authInfo[0]);
			this.bisCode = bisCode;
			this.userName = authInfo[0];
			this.pwd = authInfo[1];
			init();
		} catch (Exception e) {
			LOG.error("创建DistributedLock失败");
		}
    }
    public DistributedLock(String zkAddress,String zkUser,String zkPasswd,int timeOut,String bisCode){
    	try {
			zkPool = ZkPoolFactory.getZkPool(zkAddress, zkUser, zkPasswd, timeOut);
			zkClient = zkPool.getZkClient(zkAddress, zkUser);
			this.bisCode = bisCode;
			this.userName = zkUser;
			this.pwd = zkPasswd;
			init();
		} catch (Exception e) {
			LOG.error("创建DistributedLock失败");
		}
    }
    public DistributedLock(String zkAddress,String zkUser,String zkPasswd,String bisCode){
    	try {
			zkPool = ZkPoolFactory.getZkPool(zkAddress, zkUser, zkPasswd);
			zkClient = zkPool.getZkClient(zkAddress, zkUser);
			this.bisCode = bisCode;
			this.userName = zkUser;
			this.pwd = zkPasswd;
			init();
		} catch (Exception e) {
			LOG.error("创建DistributedLock失败");
		}
    }
    public DistributedLock(String zkAddress,String zkUser,String zkPasswd,String serviceId,String bisCode){
    	try {
			zkPool = ZkPoolFactory.getZkPool(zkAddress, zkUser, zkPasswd, serviceId);
			zkClient = zkPool.getZkClient(zkAddress, zkUser,serviceId);
			this.bisCode = bisCode;
			this.userName = zkUser;
			this.pwd = zkPasswd;
			this.serviceId = serviceId;
			init();
		} catch (Exception e) {
			LOG.error("创建DistributedLock失败");
		}
    }
    
    public DistributedLock(String zkAddress,String zkUser,String zkPasswd,String serviceId,int timeOut,String bisCode){
    	try {
			zkPool = ZkPoolFactory.getZkPool(zkAddress, zkUser, zkPasswd, serviceId, timeOut);
			zkClient = zkPool.getZkClient(zkAddress, zkUser,serviceId);
			this.bisCode = bisCode;
			this.userName = zkUser;
			this.pwd = zkPasswd;
			this.serviceId = serviceId;
			init();
		} catch (Exception e) {
			LOG.error("创建DistributedLock失败");
		}
    }
    
    
    private void init() throws Exception{
    	userName = StringUtils.defaultIfBlank(userName, "ipaas");
		pwd = StringUtils.defaultIfBlank(pwd, "ipaas@1234!QAZ");
		serviceId = StringUtils.defaultIfBlank(serviceId, "ipaas-serviceId");
		bisCode = StringUtils.defaultIfBlank(bisCode, "ipaasbis");
		bisCodeMD5 = CryptoUtils.getMD5String(bisCode);
		LOG.debug("bisCodeMD5="+bisCodeMD5);
		ns = NamespaceUtils.getNamespace(userName, pwd, serviceId,bisCodeMD5);
		SecureRandom rm = new SecureRandom();
		ns.setLockPre(String.valueOf(rm.nextInt(Integer.MAX_VALUE))+"#");
		acls = ZookeeperUtils.getAcls(Consts.ACL_DIGEST, userName, pwd, ZooDefs.Perms.ALL);
    }
    
	/**
	 * 释放锁
	 * @throws ZkException 
	 */
	public void releaseLock() throws ZkException{
		
		if(StringUtils.isBlank(lockName)){
			return;
		}
		try {
			
			boolean f = zkClient.exists(ns.getSubLockHome()+"/"+lockName);
			if(f){
				zkClient.deleteNode(ns.getSubLockHome()+"/"+lockName);
			}
			LOG.debug("释放锁"+lockName);
		} catch (Exception e) {
			throw new ZkException("releaseLock error!",e);
		}
		
	};
	/**
	 * 尝试获得锁，能获得就立马获得锁，如果不能获得就立马返回
	 * @throws ZkException 
	 */
	public boolean tryLock(){
		return false;
	}
	/**
	 * 尝试获得锁，如果有锁就返回，如果没有锁就等待，如果等待了一段时间后还没能获取到锁，那么就返回
	 * @param timeout 单位：秒
	 * @return
	 */
	public boolean tryLock(int timeout){
		return false;
	}
	/**
	 * 尝试获得锁，一直阻塞，直到获得锁为止
	 * @throws ZkException  
	 */
	public void lock() throws ZkException{
		try {
			
			createCompetitorNode();
			List<String> comps = getAllCompetitors();
			lockName = findLockNodeName(comps,ns.getLockPre());
			//判断创建的节点是否为最小节点
			if(isMinNode(comps,lockName)){
				//获得锁
				LOG.debug("获得锁 ： "+lockName);
				return;
			}
			else{
				LOG.info("等待锁："+lockName);
				List<String> list = getNodesBeforeLockNode(comps,lockName);
				Gson gson = new Gson();
				LOG.debug("等待以下节点释放："+gson.toJson(list));
				
				CountDownLatch latch = new CountDownLatch(list.size());
				//等待获得锁
				zkClient.exists(ns.getSubLockHome(), new NodeWatcher(latch));
				
				//latch.wait();
			}
		} catch (Exception e) {
			LOG.error("",e);
			throw new ZkException("lock error!!", e);
		}
	}
	
	
	class NodeWatcher extends ZkWatcher{
		
		private CountDownLatch latch2;
		
		public NodeWatcher(CountDownLatch latch){
			setLatch2(latch);
		}
		
		@Override
		public void processEvent(ZkWatchEvent event) {
			
			try {
				System.out.println("####################:"+event.getEventType().getIntValue());
				zkClient.exists(ns.getSubLockHome(),new Watcher(){

					@Override
					public void process(WatchedEvent event) {
						System.out.println("########################");
						System.out.println(event.getPath());
					}
					
				});
				
			} catch (Exception e) {
				LOG.error("",e);
			}
			
		}

		public CountDownLatch getLatch2() {
			return latch2;
		}

		public void setLatch2(CountDownLatch latch2) {
			this.latch2 = latch2;
		}
		
	}
	
	/**
	 * 创建竞争者节点
	 * @throws ZkException

	 */
	private void createCompetitorNode() throws Exception{
		zkClient.createNode(ns.getLockNodePath(), acls, "", CreateMode.EPHEMERAL_SEQUENTIAL);
		
		
		
		String da = zkClient.getNodeData(ns.getSubLockHome(),new Watcher(){

			@Override
			public void process(WatchedEvent event) {
				System.out.println("asdfasdfasdfasdfasdfasdfasdfasdfasdfa11111111");
			}
			
		});
		System.out.println("111111111111111111111111111111111"+da);
		String da1 = zkClient.getNodeData(ns.getSubLockHome(),new Watcher(){

			@Override
			public void process(WatchedEvent event) {
				System.out.println("asdfasdfasdfasdfasdfasdfasdfasdfasdfa222222");
			}
			
		});
		System.out.println("1111111111111111111111111111111112222"+da1);
	}
	
	private List<String> getAllCompetitors() throws Exception{
		List<String> competitors = zkClient.getChildren(ns.getSubLockHome());
		Gson gson = new Gson();
		Collections.sort(competitors);
		LOG.debug("所有竞争者："+gson.toJson(competitors));
		return competitors;
	}
	
	private boolean isMinNode(List<String> competitors,String lockName){
		
		List<String> list = getNodesSufix(competitors);
		if(lockName.endsWith(list.get(0))){
			return true;
		}
		return false;
	}
	
	private String findLockNodeName(List<String> competitors,String lockPre){
		for(String node:competitors){
			if(!node.startsWith(lockPre)){
				continue;
			}
			return node;
		}
		return null;
	}
	
	
	private List<String> getNodesSufix(List<String> comps){
		
		List<String> sufixs = new ArrayList<String>();
		
		for(String node:comps){
			sufixs.add(node.substring(node.lastIndexOf("#"), node.length()));
			
		}
		Collections.sort(sufixs);
		return sufixs;
	}
	
	private List<String> getNodesBeforeLockNode(List<String> comps,String lockName){
		
		String lockPre = ns.getLockPre();
		System.out.println("lockPre="+lockPre);
		String lockSufix = lockName.substring(lockName.lastIndexOf("#"), lockName.length());
		List<String> sufixs = getNodesSufix(comps);
		List<String> nodes = new ArrayList<String>();
		List<String> nodesSufix = new ArrayList<String>();
		
		for(int i=0;i<sufixs.size(); i++){
			if(lockSufix.equals(sufixs.get(i))){	
				break;
			}
			else{
				nodesSufix.add(sufixs.get(i));
			}
		}
		
		for(String node:comps){
			String sufix = node.substring(node.lastIndexOf("#"),node.length());
			if(nodesSufix.contains(sufix)){
				nodes.add(node);
			}
		}
		return nodes;
	}
}