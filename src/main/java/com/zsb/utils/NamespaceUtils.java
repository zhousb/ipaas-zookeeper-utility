package com.zsb.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zsb.consts.Consts;
import com.zsb.crypto.CryptoUtils;
import com.zsb.model.Namespace;

/**
 * 
 * @date 2016年5月31日
 * @author zhoushanbin
 * 
 */
public class NamespaceUtils {

	private static final Logger LOG = LoggerFactory
			.getLogger(NamespaceUtils.class);

	private static Map<String, Namespace> map = new ConcurrentHashMap<String, Namespace>();

	public static synchronized Namespace getNamespace(String userName,
			String pwd, String serviceId,String bisCode) {
		String key = calculateKey(userName, pwd, serviceId,bisCode);
		if (map.containsKey(key)) {
			return map.get(key);
		}
		Namespace ns = new Namespace();
		ns.setUserHome(getIpaasUserHome(userName, pwd, serviceId));
		ns.setConfHome(getIpaasUserConfHome(userName, pwd, serviceId));
		ns.setLockHome(getIpaasLockHome(userName, pwd, serviceId));
		ns.setSubLockHome(getIpaasSubLockHome(userName, pwd, serviceId, bisCode));
		map.put(key, ns);
		return ns;
	}

	private static String calculateKey(String userName, String pwd,
			String serviceId,String bisCode) {
		return userName + pwd + serviceId + bisCode;
	}

	public static String getIpassHome() {
		return Consts.IPAAS_ROOT;
	}

	private static String getIpaasUserHome(String userName, String pwd,
			String serviceId) {

		String home = null;
		try {
			home ="/"+ CryptoUtils.getMD5String(userName + pwd + serviceId);
		} catch (Exception e) {
			LOG.error("", e);
		}
		return home;
	}

	private static String getIpaasUserConfHome(String userName, String pwd,
			String serviceId) {
		return getIpaasUserHome(userName, pwd, serviceId) + "/"
				+ Consts.USER_CONF;
	}

	private static String getIpaasLockHome(String userName, String pwd,
			String serviceId) {
		return getIpaasUserHome(userName, pwd, serviceId) + "/"
				+ Consts.USER_LOCK;
	}

	private static String getIpaasSubLockHome(String userName, String pwd,
			String serviceId,String bisCode){
		//String bisCodeM = null;
		try {
			//bisCodeM = CryptoUtils.getMD5String(bisCode);
		} catch (Exception e) {
			LOG.error("",e);
		}
		return getIpaasLockHome(userName, pwd,serviceId) + "/" + bisCode; 
	}
}
