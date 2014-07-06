package com.app.helium.SyncedStorage;

public interface IPullSyncThreadFinishedListener {
	public void run(PullSyncEventInfo event_info);
}
