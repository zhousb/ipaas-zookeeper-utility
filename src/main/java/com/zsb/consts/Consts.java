package com.zsb.consts;


/**
 * @date 2016年5月30日
 * @author zhoushanbin
 *
 */
public class Consts {

	/***
	 * 每个节点有单独的ACL，子节点不能继承父节点的ACL 
	 * ACL有三个维度：schema,id,permision 
	 * Schema有7种：
	 * world: 它下面只有一个id, 叫anyone
	 * auth: 它不需要id
	 * digest:
	 * 它对应的id为username:BASE64(SHA1(password))
	 * ip:
	 * 它对应的id为客户机的IP地址，设置的时候可以设置一个ip段，比如ip:192.168.1.0/16, 表示匹配前16个bit的IP段
	 * super: 在这种scheme情况下，对应的id拥有超级权限
	 * sasl: sasl的对应的id，是一个通过了kerberos认证的用户id
	 * Permission有5种: 
	 * CREATE(c),DELETE(d),READ(r),WRITE(w),ADMIN(a)
	 */
	public static final String ACL_DIGEST = "digest";
	public static final String ACL_WORLD = "world";
	
	/**
	 * ipaas 根节点
	 */
	public static final String IPAAS_ROOT = "/ipaashome";
	
	
	/**
	 * namespace
	 */
	public static final String IPAAS_NS = "ipaashome";
	
	/**
	 * 配置节点名称
	 */
	public static final String USER_CONF = "configure";
	
	/**
	 * 锁节点
	 */
	public static final String USER_LOCK = "lock";
	
	
	
}
