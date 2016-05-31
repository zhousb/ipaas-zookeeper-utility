package com.zsb.event;

import org.apache.zookeeper.WatchedEvent;
import com.zsb.event.ZkWatcher.Event.EventType;
import com.zsb.event.ZkWatcher.Event.KeeperState;

/**
 * @date 2016年5月31日
 * @author zhoushanbin
 * 
 */
public class ZkWatchEvent {

	private KeeperState keeperState;
	private EventType eventType;
	private String path;
	private WatchedEvent event;

	public KeeperState getKeeperState() {
		return keeperState;
	}

	public void setKeeperState(KeeperState keeperState) {
		this.keeperState = keeperState;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public WatchedEvent getEvent() {
		return event;
	}

	public void setEvent(WatchedEvent event) {
		this.event = event;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public ZkWatchEvent(KeeperState keeperState, EventType eventType,
			String path) {
		this.keeperState = keeperState;
		this.eventType = eventType;
		this.path = path;
	}

	public ZkWatchEvent(WatchedEvent event) {
		this.event = event;
		this.keeperState = KeeperState.fromInt(event.getState().getIntValue());
		this.eventType = EventType.fromInt(event.getType().getIntValue());
		this.path = event.getPath();
	}

	public ZkWatchEvent getWrapper() {
		return new ZkWatchEvent(keeperState, eventType, path);
	}
}
