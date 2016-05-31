package com.zsb.lock;

import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.recipes.locks.InterProcessLock;

/**
 * 互斥锁
 * @date 2016年5月31日
 * @author zhoushanbin
 * 
 */
public class MutexLock {

	private InterProcessLock processLock;

	public MutexLock(InterProcessLock processLock) {
		this.processLock = processLock;
	}

	public void acquire() throws Exception {
		this.processLock.acquire();
	}

	public boolean acquire(long time, TimeUnit unit) throws Exception {
		return this.processLock.acquire(time, unit);
	}

	public void release() throws Exception {
		this.processLock.release();
	}

	public boolean isAcquiredInThisProcess() {
		return this.processLock.isAcquiredInThisProcess();
	}
}
