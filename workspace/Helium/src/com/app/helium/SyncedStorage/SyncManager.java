package com.app.helium.SyncedStorage;

import java.util.ArrayList;
import java.util.UUID;

import com.app.helium.Contact;
import com.app.helium.Message;
import com.app.helium.ScheduledTravel;
import com.app.helium.TravelBroadcast;

public final class SyncManager {
	private static ArrayList<String> sync_entities;
	
	public static void initialize() {
		registerSyncEntities();
	}
	
	private static void registerSyncEntities() {
		sync_entities = new ArrayList<String>();
		sync_entities.add(Contact.class.getName());
		sync_entities.add(Message.class.getName());
		sync_entities.add(ScheduledTravel.class.getName());
		sync_entities.add(TravelBroadcast.class.getName());
	}
	
	public static String registerSync(SyncedStorageProvider sync_object) {
		SyncQueue sync_register = new SyncQueue(sync_object);
		if(sync_register.getID() < 0) {
			// new sync entry, so create new record.
			sync_register.batch_id = UUID.randomUUID().toString();
			sync_register.batch_sequence = 1;
			sync_register.sync_object_name = sync_object.getClass().getName();
			sync_register.sync_record_id = sync_object.getID();
			sync_register.sync_status = SyncStatus.NOT_SYNCED;
			sync_register.Save();
		}
		return sync_register.batch_id;
	}
	
	public static void registerSync(SyncedStorageProvider sync_object,String batch_id) {
		SyncQueue sync_register = new SyncQueue(sync_object);
		long max_batch_sequence = sync_register.getMaxSequenceInBatch(batch_id);
		if(sync_register.getID() < 0) {
			// new sync entry, so create new record.
			sync_register.batch_id = batch_id;
			sync_register.batch_sequence = max_batch_sequence + 1;
			sync_register.sync_object_name = sync_object.getClass().getName();
			sync_register.sync_record_id = sync_object.getID();
			sync_register.sync_status = SyncStatus.NOT_SYNCED;
			
		} else {
			// sync already registered, so just update
			sync_register.batch_id = batch_id;
			sync_register.batch_sequence = max_batch_sequence + 1;
			//sync_register.sync_status = SyncStatus.NOT_SYNCED;
		}
		sync_register.Save();
	}
	
	//NR: Incomming Sync Client <= Server
	public static void request_Sync() {
		for(String sync_entity : sync_entities) {
			Thread sync_register_thread = new Thread(new SyncRegisterThread(sync_entity));
			sync_register_thread.start();
		}
		
		// for each Synced Table in sync_entities i.e for eg[Contacts,Messages,ScheduledTravel,TravelBroadcast] do following
		// {
		//		dynamically load classes for each entity & create its objects
		// 		read the SyncInfo table and find [ where sync_object_name = sync_entities[i] ]
		// 		if record not found, insert record with sync_object_name = sync_entities[i] & current_date time stamp & Trigger Sync in threaded way
		// 		Else if record found check  if last_sync_date_time < (current_date_time + buffer) , then trigger Sync in threaded way
		// 		[May be define another method for entire table sync and handle group data response]
		// }
	}
	
	//NR: Outgoing Sync Client => Server
	public static void startSync() {
		SyncQueue pending_sync =  new SyncQueue();
		ArrayList<String> batches = pending_sync.getDistinctBatches();
		for(String batch_id : batches){
			//NR: execute sync for each "sync_objects_in_batch" in seperate thread
			ArrayList<SyncQueue> sync_objects_in_batch = pending_sync.getBatch(batch_id);			
			Thread sync_thread = new Thread(new SyncBatchExecutorThread(sync_objects_in_batch));
			sync_thread.start();
		}
	}
}
