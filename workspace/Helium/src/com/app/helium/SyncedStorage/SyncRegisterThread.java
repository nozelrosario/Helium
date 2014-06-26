package com.app.helium.SyncedStorage;

import org.joda.time.DateTime;

import com.app.helium.Application;

public class SyncRegisterThread implements Runnable {

	public String sync_object_name;
	
	public SyncRegisterThread(String sync_entity_name) {
		this.sync_object_name = sync_entity_name;
	}
	
	@Override
	public void run() {
		SyncManager.notifyPullSyncThreadStarted();
		SyncRegister sync_register_entry = new SyncRegister();
		sync_register_entry.loadRegistryEntry(this.sync_object_name);
		// If new, create entry
		if(sync_register_entry.id <= 0) {
			sync_register_entry.sync_object_name = this.sync_object_name;
			sync_register_entry.last_sync_date_time = new DateTime();
			sync_register_entry.sync_status= SyncStatus.NOT_SYNCED;
			sync_register_entry.error_description=null;
			sync_register_entry.Save();			
		} 
		
		if(sync_register_entry.sync_status != SyncStatus.BUSY) {
			try {
				sync_register_entry.startSync();
			} catch (GenericSyncException e) {
				Application.logError("Sync Failed for Entity : ["+ this.sync_object_name +"] Reason : " + e.toString());
			} /*catch (Exception e) {
				Application.logError("Sync Failed for Entity : ["+ this.sync_object_name +"] Reason : " + e.toString());
			}*/
		}
		SyncManager.notifyPullSyncThreadFinished();
	}

}
