package com.app.helium.SyncedStorage;

public class PushSyncEventInfo {
	public String sync_batch_id;
	public SyncStatus sync_status;
	public String error_description;
	
	public PushSyncEventInfo() {}
	public PushSyncEventInfo(String sync_batch_id,SyncStatus sync_status, String error_description){
		this.sync_batch_id = sync_batch_id;
		this.sync_status = sync_status;
		this.error_description = error_description;
	}
}
