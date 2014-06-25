package com.app.helium.SyncedStorage;

import org.json.JSONObject;

public interface ISyncAdapter {
	
	public long getRemoteID();
	public void updateSyncInformation(long remote_id, SyncStatus last_sync_status, String sync_error_info);
	public void updateSyncInformation(SyncStatus last_sync_status);
	public JSONObject getJSON() throws ReferenceObjectNotSyncedException;
	public void LoadData(JSONObject json_data);
	public long getID(long remote_id);
	public void triggerSync();

}
