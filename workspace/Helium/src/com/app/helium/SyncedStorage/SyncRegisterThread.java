package com.app.helium.SyncedStorage;

public class SyncRegisterThread implements Runnable {

	public String sync_object_name;
	
	public SyncRegisterThread(String sync_entity_name) {
		this.sync_object_name = sync_entity_name;
	}
	
	@Override
	public void run() {
		
		SyncRegister sync_register_entry = new SyncRegister();
		sync_register_entry.loadRegistryEntry(this.sync_object_name);
		if(sync_register_entry.id > 0) {
			if(sync_register_entry.sync_status != SyncStatus.BUSY) {
				
			}
			
		} else {
			sync_register_entry.sync_object_name = this.sync_object_name;
			sync_register_entry.last_sync_date_time = null;
			sync_register_entry.sync_status=null;
			sync_register_entry.error_description=null;
			sync_register_entry.Save();
		}
		//		dynamically load classes for each entity & create its objects
		// 		read the SyncInfo table and find [ where sync_object_name = sync_entities[i] ]
		// 		if record not found, insert record with sync_object_name = sync_entities[i] & current_date time stamp & Trigger Sync in threaded way
		// 		Else if record found check  if last_sync_date_time < (current_date_time + buffer) , then trigger Sync in threaded way
		// 		[May be define another method for entire table sync and handle group data response]
		
	}

}
