package com.zsb.model;

import java.io.Serializable;

/**
 * 参数
 * @date 2016年6月2日
 * @author zhoushanbin
 *
 */
public class ZkParam implements Serializable{
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1097456292440129538L;
	private String zkAddr;
	private String zkUser;
	private String zkPwd;
	private String serviceId;
	private String bisCode;
	public String getZkAddr() {
		return zkAddr;
	}
	public void setZkAddr(String zkAddr) {
		this.zkAddr = zkAddr;
	}
	public String getZkUser() {
		return zkUser;
	}
	public void setZkUser(String zkUser) {
		this.zkUser = zkUser;
	}
	public String getZkPwd() {
		return zkPwd;
	}
	public void setZkPwd(String zkPwd) {
		this.zkPwd = zkPwd;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getBisCode() {
		return bisCode;
	}
	public void setBisCode(String bisCode) {
		this.bisCode = bisCode;
	}
	
}
