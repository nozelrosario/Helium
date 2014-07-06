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
		PullSyncQueue sync_register_entry = new PullSyncQueue();
		sync_register_entry.loadRegistryEntry(this.sync_object_name);
		// If new, create entry
		if(sync_register_entry.id <= 0) {
			sync_register_entry.sync_object_name = this.sync_object_name;
			sync_register_entry.last_sync_date_time = new DateTime();
			sync_register_entry.sync_status= SyncStatus.NOT_SYNCED;
			sync_register_entry.error_description=null;
			sync_register_entry.Save();			
		} 
		SyncManager.notifyPullSyncThreadStarted(sync_register_entry);
		if(sync_register_entry.sync_status != SyncStatus.BUSY) {
			try {
				sync_register_entry.startSync();
			} catch (GenericSyncException e) {
				//NR: registration of failed sync handled by SyncRegister itself. no handling required here.
				Application.logError("Sync Failed for Entity : ["+ this.sync_object_name +"] Reason : " + e.toString());
			} /*catch (Exception e) {
				Application.logError("Sync Failed for Entity : ["+ this.sync_object_name +"] Reason : " + e.toString());
			}*/
		}
		SyncManager.notifyPullSyncThreadFinished(sync_register_entry);
	}

}
