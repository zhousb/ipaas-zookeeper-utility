package com.zsb.model;

public class Namespace {

	private String userHome;
	private String confHome;
	private String lockHome;
	private String subLockHome;
	
	
	public String getSubLockHome() {
		return subLockHome;
	}

	public void setSubLockHome(String subLockHome) {
		this.subLockHome = subLockHome;
	}

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