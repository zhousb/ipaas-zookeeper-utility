package com.zsb.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zsb.consts.Consts;
import com.zsb.crypto.CryptoUtils;

/**
 * 
 * @date 2016年5月31日
 * @author zhoushanbin
 *
 */
public class NamespaceUtils {
	
	private static final Logger LOG = LoggerFactory.getLogger(NamespaceUtils.class);
	
	private static Map<String,Namespace> map = new ConcurrentHashMap<String,Namespace>();
	
	public static synchronized  Namespace getNamespace(String userName,String pwd,String serviceId){
		String key = calculateKey(userName, pwd, serviceId);
		if(map.containsKey(key)){
			return map.get(key);
		}
		Namespace ns = new Namespace();
		ns.setUserHome(getIpaasUserHome(userName, pwd, serviceId));
		ns.setConfHome(getIpaasUserConfHome(userName, pwd, serviceId));
		ns.setLockHome(getIpaasLockHome(userName, pwd, serviceId));
		map.put(key, ns);
		return ns;
	}
	
	
	private static String calculateKey(String userName,String pwd,String serviceId){
		return userName+pwd+serviceId;
	}
	
	public static String getIpassHome(){
		return Consts.IPAAS_ROOT;
	}
	
	private static String getIpaasUserHome(String userName,String pwd,String serviceId){
		
		String home = null;
		try {
			home = getIpassHome()+"/" + CryptoUtils.getMD5String(userName+pwd+serviceId);
		} catch (Exception e) {
			LOG.error("",e);
		}
		return home;
	}
	
	private static String getIpaasUserConfHome(String userName,String pwd,String serviceId){
		return getIpaasUserHome(userName, pwd, serviceId) + "/" + Consts.USER_CONF;
	}
	
	private static String getIpaasLockHome(String userName,String pwd,String serviceId){
		return getIpaasUserHome(userName, pwd, serviceId) + "/" + Consts.USER_LOCK;
	}
}

class Namespace {
	
	private String userHome;
	private String confHome;
	private String lockHome;
	
	public String getUserHome() {
		return userHome;
	}
	public void setUserHome(String userHome) {
		this.userHome = userHome;
	}
	public String getConfHome() {
		return confHome;
	}
	public void setConfHome(String confHome) {
		this.confHome = confHome;
	}
	public String getLockHome() {
		return lockHome;
	}
	public void setLockHome(String lockHome) {
		this.lockHome = lockHome;
	}
	
}
