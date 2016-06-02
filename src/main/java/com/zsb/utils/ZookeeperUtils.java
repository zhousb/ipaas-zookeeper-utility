package com.zsb.utils;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import com.zsb.model.ZkClient;
import com.zsb.model.ZkParam;
import com.zsb.zk.ZkPool;
import com.zsb.zk.ZkPoolFactory;

/**
 * zookeeper 工具类
 * @date 2016年6月1日
 * @author zhoushanbin
 *
 */
public class ZookeeperUtils {
	
	/**
	 * 创建节点的acls
	 * @param scheme
	 * @param userName
	 * @param pwd
	 * @param permision
	 * @return
	 */
	public static List<ACL> getAcls(String scheme,String userName,String pwd,int permision){
		
		List<ACL> acls = new ArrayList<ACL>(1);     
		try {
			Id id = new Id(scheme, DigestAuthenticationProvider.generateDigest(userName+":"+pwd));
			ACL acl = new ACL(permision, id);  
			acls.add(acl);
		} catch (NoSuchAlgorithmException e) {
			
		}  
		
		return acls;
	}
	
	/**
	 * 获取zk 客户端
	 * @param zkAddress
	 * @param zkUser
	 * @param zkPasswd
	 * @param serviceId
	 * @return
	 * @throws Exception
	 */
	public static ZkClient getZkClient(String zkAddress, String zkUser, String zkPasswd, String serviceId) throws Exception{
		
		ZkPool pool = ZkPoolFactory.getZkPool(zkAddress, zkUser, zkPasswd, serviceId);
		
		return pool.getZkClient(zkAddress, zkUser, serviceId);
	}
	
	/**
	 * 获取zk 客户端
	 * @param param
	 * @return
	 * @throws Exception
	 * @author think
	 */
	public static ZkClient getZkClient(ZkParam param) throws Exception{
		
		String zkAddress = param.getZkAddr();
		String zkUser = param.getZkUser();
		String serviceId = param.getServiceId();
		String zkPasswd = param.getZkPwd();
		ZkPool pool = ZkPoolFactory.getZkPool(zkAddress, zkUser, zkPasswd, serviceId);
		
		return pool.getZkClient(zkAddress, zkUser, serviceId);
	}
	
}
