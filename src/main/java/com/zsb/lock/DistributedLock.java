package com.zsb.lock;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zsb.exception.ZkException;
import com.zsb.model.ZkClient;
import com.zsb.utils.NamespaceUtils;
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
    
    private String userName;
    private String pwd;
    private String serviceId;
    
    public DistributedLock(String zkAddress,String bisCode){
    	try {
			zkPool = ZkPoolFactory.getZkPool(zkAddress);
			zkClient = zkPool.getZkClient(zkAddress,"");
			this.bisCode = bisCode;
		} catch (Exception e) {
			LOG.error("创建DistributedLock失败");
		}
    }
    
    public DistributedLock(String zkAddress,int timeOut,String bisCode){
    	try {
			zkPool = ZkPoolFactory.getZkPool(zkAddress,timeOut);
			zkClient = zkPool.getZkClient(zkAddress,"");
			this.bisCode = bisCode;
		} catch (Exception e) {
			LOG.error("创建DistributedLock失败");
		}
    }
    
    public DistributedLock(String zkAddress,int timeOut,String bisCode,String ...authInfo){
    	try {
			zkPool = ZkPoolFactory.getZkPool(zkAddress, timeOut, authInfo);
			zkClient = zkPool.getZkClient(zkAddress, authInfo[0]);
			this.bisCode = bisCode;
		} catch (Exception e) {
			LOG.error("创建DistributedLock失败");
		}
    }
    public DistributedLock(String zkAddress,String zkUser,String zkPasswd,int timeOut,String bisCode){
    	try {
			zkPool = ZkPoolFactory.getZkPool(zkAddress, zkUser, zkPasswd, timeOut);
			zkClient = zkPool.getZkClient(zkAddress, zkUser);
			this.bisCode = bisCode;
		} catch (Exception e) {
			LOG.error("创建DistributedLock失败");
		}
    }
    public DistributedLock(String zkAddress,String zkUser,String zkPasswd,String bisCode){
    	try {
			zkPool = ZkPoolFactory.getZkPool(zkAddress, zkUser, zkPasswd);
			zkClient = zkPool.getZkClient(zkAddress, zkUser);
			this.bisCode = bisCode;
		} catch (Exception e) {
			LOG.error("创建DistributedLock失败");
		}
    }
    public DistributedLock(String zkAddress,String zkUser,String zkPasswd,String serviceId,String bisCode){
    	try {
			zkPool = ZkPoolFactory.getZkPool(zkAddress, zkUser, zkPasswd, serviceId);
			zkClient = zkPool.getZkClient(zkAddress, zkUser,serviceId);
			this.bisCode = bisCode;
		} catch (Exception e) {
			LOG.error("创建DistributedLock失败");
		}
    }
    
    public DistributedLock(String zkAddress,String zkUser,String zkPasswd,String serviceId,int timeOut,String bisCode){
    	try {
			zkPool = ZkPoolFactory.getZkPool(zkAddress, zkUser, zkPasswd, serviceId, timeOut);
			zkClient = zkPool.getZkClient(zkAddress, zkUser,serviceId);
			this.bisCode = bisCode;
		} catch (Exception e) {
			LOG.error("创建DistributedLock失败");
		}
    }
    
	/**
	 * 释放锁
	 * @throws ZkException 
	 * @throws KeeperException 
	 * @throws InterruptedException 
	 */
	public void releaseLock(){
		
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
	public void lock(){
		
	}
	
	/**
	 * 创建竞争者节点
	 * @throws ZkException

	 */
	private void createCompetitorNode() throws ZkException{
		NamespaceUtils.getNamespace(userName, pwd, serviceId);
	}
}