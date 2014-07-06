package com.app.helium.SyncedStorage;

import org.joda.time.DateTime;

public class PullSyncEventInfo {
	public String sync_object_name;
	public SyncStatus sync_status;
	public DateTime last_sync_date_time;
	public String error_description;
	
	public PullSyncEventInfo() {}
	public PullSyncEventInfo(String sync_object_name,SyncStatus sync_status,DateTime last_sync_date_time, String error_description){
		this.sync_object_name = sync_object_name;
		this.sync_status = sync_status;
		this.last_sync_date_time = last_sync_date_time;
		this.error_description = error_description;
	}
}
