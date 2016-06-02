package com.zsb.lock;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zsb.consts.Consts;
import com.zsb.crypto.CryptoUtils;
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
    private InterProcessLock lock;
    
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
		acls = ZookeeperUtils.getAcls(Consts.ACL_DIGEST, userName, pwd, ZooDefs.Perms.ALL);
    }
    
	/**
	 * 释放锁
	 * @throws ZkException 
	 */
	public void releaseLock() throws ZkException{
		
		if(null != lock){
			try {
				lock.release();
			} catch (Exception e) {
				throw new ZkException("释放锁失败",e);
			}
		}
	};
	/**
	 * 尝试获得锁，一直阻塞，直到获得锁为止
	 * @throws ZkException  
	 */
	public void lock() throws ZkException{
		
		try {
			createSubLockHomeIfNeed();
			lock = zkClient.getInterProcessLock(ns.getSubLockHome());
			lock.acquire();
		} catch (Exception e) {
			throw new ZkException("获取锁失败",e);
		}
		
	}
	
	/**
	 * 在指定时间内获取锁
	 * @param timeout
	 * @param unit
	 * @throws ZkException
	 */
	public void lock(int timeout,TimeUnit unit) throws ZkException{
		
		try {
			createSubLockHomeIfNeed();
			lock = zkClient.getInterProcessLock(ns.getSubLockHome());
			if(!lock.acquire(timeout, unit)){
				throw new ZkException("超时，获取锁失败！！！");
			}
		} catch (Exception e) {
			throw new ZkException("获取锁失败",e);
		}
	}
	
	
	private void createSubLockHomeIfNeed() throws Exception{
		if(!zkClient.exists(ns.getSubLockHome())){
			zkClient.createNode(ns.getSubLockHome(), acls, "", CreateMode.PERSISTENT);
		}
	}
}