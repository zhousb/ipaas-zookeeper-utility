package com.zsb.event;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * @date 2016年5月31日
 * @author zhoushanbin
 * 
 */
public abstract class ZkWatcher implements Watcher {
	/**
	 * 子类重写此方法实现具体业务逻辑
	 * @param event
	 */
	public abstract void processEvent(ZkWatchEvent event);

	@Override
	public void process(WatchedEvent event) {
		processEvent(new ZkWatchEvent(event));
	}
	public static abstract interface Event {
		public static enum EventType {
			None(-1), NodeCreated(1), NodeDeleted(2), NodeDataChanged(3), NodeChildrenChanged(4);

			private final int intValue;

			private EventType(int intValue) {
				this.intValue = intValue;
			}

			public int getIntValue() {
				return this.intValue;
			}

			public static EventType fromInt(int intValue) {
				switch (intValue) {
				case -1:
					return None;
				case 1:
					return NodeCreated;
				case 2:
					return NodeDeleted;
				case 3:
					return NodeDataChanged;
				case 4:
					return NodeChildrenChanged;
				}
				throw new RuntimeException(
						"Invalid integer value for conversion to EventType");
			}
		}

		public static enum KeeperState {
			Disconnected(0), SyncConnected(3), AuthFailed(4), ConnectedReadOnly(5), SaslAuthenticated(6), Expired(-112);

			private final int intValue;

			private KeeperState(int intValue) {
				this.intValue = intValue;
			}

			public int getIntValue() {
				return this.intValue;
			}

			public static KeeperState fromInt(int intValue) {
				switch (intValue) {
				case 0:
					return Disconnected;
				case 3:
					return SyncConnected;
				case 4:
					return AuthFailed;
				case 5:
					return ConnectedReadOnly;
				case 6:
					return SaslAuthenticated;
				case -112:
					return Expired;
				}
				throw new RuntimeException("Invalid integer value for conversion to KeeperState");
			}
		}
	}
}
