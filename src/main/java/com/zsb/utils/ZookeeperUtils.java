package com.zsb.utils;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

/**
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
	
	
	
}
