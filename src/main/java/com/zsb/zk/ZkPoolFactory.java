package com.zsb.zk;

import org.apache.commons.lang.StringUtils;

import com.zsb.consts.Consts;
import com.zsb.exception.ZkException;
import com.zsb.model.ZkClient;

/**
 * Date: 2016年5月28日 <br>
 * 
 * @author zhoushanbin
 */
public class ZkPoolFactory {
	private static final ZkPool zkPool = new ZkPool();

	public static ZkPool getZkPool(String zkAddress, String zkUser,
			String zkPasswd, int timeOut) throws ZkException {
		
		validateParam(zkAddress, zkUser, zkPasswd);
		if (zkPool.exist(zkAddress, zkUser)) {
			return zkPool;
		}
		ZkClient client = null;
		try {
			client = new ZkClient(zkAddress, timeOut, new String[] { Consts.ACL_DIGEST,
					getAuthInfo(zkUser, zkPasswd) });
			client.addAuth(Consts.ACL_DIGEST, getAuthInfo(zkUser, zkPasswd));
		} catch (Exception e) {
			throw new ZkException("获取zkPool 失败",e);
		}
		zkPool.addZkClient(zkAddress, zkUser, client);
		return zkPool;
	}

	public static ZkPool getZkPool(String zkAddress, String zkUser,
			String zkPasswd, String serviceId, int timeOut)
			throws ZkException {
		validateParam(zkAddress, zkUser, zkPasswd, serviceId);
		if (zkPool.exist(zkAddress, zkUser, serviceId)) {
			return zkPool;
		}
		ZkClient client = null;
		try {
			client = new ZkClient(zkAddress, timeOut, new String[] { Consts.ACL_DIGEST,
					getAuthInfo(zkUser, zkPasswd) });
			client.addAuth(Consts.ACL_DIGEST, getAuthInfo(zkUser, zkPasswd));
		} catch (Exception e) {
			throw new ZkException("获取zkPool 失败",e);
		}
		zkPool.addZkClient(zkAddress, zkUser, serviceId, client);
		return zkPool;
	}

	private static void validateParam(String zkAddress, String zkUser,
			String zkPasswd) throws ZkException {
		if(StringUtils.isBlank(zkAddress)||StringUtils.isEmpty(zkUser)||StringUtils.isEmpty(zkPasswd)){
			throw new ZkException("参数不合法");
		}
	}

	private static void validateParam(String zkAddress, String zkUser,
			String zkPasswd, String serviceId) throws ZkException {
		if(StringUtils.isBlank(zkAddress)||StringUtils.isEmpty(zkUser)||StringUtils.isEmpty(zkPasswd)||StringUtils.isEmpty(serviceId)){
			throw new ZkException("参数不合法");
		}
	}

	private static String getAuthInfo(String zkUser, String zkPasswd) {
		return zkUser + ":" + zkPasswd;
	}

	public static ZkPool getZkPool(String zkAddress, String zkUser,
			String zkPasswd) throws ZkException {
		return getZkPool(zkAddress, zkUser, zkPasswd, 2000);
	}

	public static ZkPool getZkPool(String zkAddress, String zkUser,
			String zkPasswd, String serviceId) throws ZkException {
		return getZkPool(zkAddress, zkUser, zkPasswd, serviceId, 2000);
	}

	public static ZkPool getZkPool(String zkAddress, int timeOut,
			String... authInfo) throws ZkException {
		String zkUser = null;
		String zkPasswd = null;
		if ((null != authInfo) && (authInfo.length >= 2)) {
			if (!StringUtils.isBlank(authInfo[0])) {
				zkUser = authInfo[0];
			}
			if (!StringUtils.isBlank(authInfo[1])) {
				zkPasswd = authInfo[1];
			}
		}
		return getZkPool(zkAddress, zkUser, zkPasswd, 2000);
	}
}
