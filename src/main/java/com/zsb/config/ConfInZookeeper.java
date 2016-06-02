package com.zsb.config;

import java.util.List;
import java.util.Map;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import com.zsb.consts.Consts;
import com.zsb.crypto.CryptoUtils;
import com.zsb.model.Namespace;
import com.zsb.model.ZkClient;
import com.zsb.model.ZkParam;
import com.zsb.utils.NamespaceUtils;
import com.zsb.utils.ZookeeperUtils;
import com.zsb.zk.ZkPool;
import com.zsb.zk.ZkPoolFactory;

public class ConfInZookeeper {
	
	
	private static final Logger LOG = LoggerFactory.getLogger(ConfInZookeeper.class);
	
	private ZkClient client;
	
	private ZkParam param;
	
	private Namespace ns;
	
	private List<ACL> acls;
	
	private String confNode;
	
	public ConfInZookeeper(ZkParam param){
		
		this.param = param;
		
		String zkAddress = param.getZkAddr();
		String zkUser = param.getZkUser();
		String zkPasswd = param.getZkPwd();
		String serviceId = param.getServiceId();
		
		try {
			ZkPool pool = ZkPoolFactory.getZkPool(zkAddress, zkUser, zkPasswd, serviceId);
			client = pool.getZkClient(zkAddress, zkUser, serviceId);
			init();
		}catch (Exception e) {
			LOG.error("",e);
		}
		
	}
	
	
	private void init() throws Exception{
		String userName = param.getZkUser();
		String pwd = param.getZkPwd();
		String serviceId = param.getServiceId();
		String bisCode = param.getBisCode();
		ns = NamespaceUtils.getNamespace(userName, pwd, serviceId, bisCode);
		acls = ZookeeperUtils.getAcls(Consts.ACL_DIGEST, userName, pwd, ZooDefs.Perms.ALL);
		String bisCodeM = CryptoUtils.getMD5String(param.getBisCode());
		
		confNode = ns.getConfHome()+"/"+bisCodeM;
		
	}
	
	/**
	 * 设置配置
	 */
	public boolean setConf(Map<String,String> map){
		
		try {
			createConfNodeIfNeed();
			Gson gson = new Gson();
			client.setNodeData(confNode,gson.toJson(map));
			return true;
		} catch (Exception e) {
			LOG.error("",e);
		}
		
		return false;
	}
	
	
	/**
	 * 获取配置
	 */
	public String getConf(){
		
		try {
			return client.getNodeData(confNode);
			
		} catch (Exception e) {
			LOG.error("",e);
		}
		
		return null;
	}
	
	private void createConfNodeIfNeed() throws Exception{
	
		if(!client.exists(confNode)){
			client.createNode(confNode, acls, "", CreateMode.PERSISTENT);
		}
		
	}
	
}
