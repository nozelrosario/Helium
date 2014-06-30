package com.app.helium.SyncedStorage;

import java.util.ArrayList;
import java.util.UUID;

import com.app.helium.Models.Contact;
import com.app.helium.Models.Message;
import com.app.helium.Models.ScheduledTravel;
import com.app.helium.Models.TravelBroadcast;

public final class SyncManager {
	private static ArrayList<String> sync_entities;
	public static boolean PUSH_SYNC_BUSY = false;
	public static boolean PULL_SYNC_BUSY = false;
	private static long PUSH_SYNC_ACTIVE_THREADS = 0;
	private static long PULL_SYNC_ACTIVE_THREADS = 0;
	
	public static void initialize() {
		registerSyncEntities();
	}
	
	public static void notifyPushSyncThreadStarted() {
		PUSH_SYNC_ACTIVE_THREADS = PUSH_SYNC_ACTIVE_THREADS + 1;
		PUSH_SYNC_BUSY = (PUSH_SYNC_ACTIVE_THREADS <= 0);
	}
	
	public static void notifyPullSyncThreadStarted() {
		PULL_SYNC_ACTIVE_THREADS = PULL_SYNC_ACTIVE_THREADS + 1;
		PUSH_SYNC_BUSY = (PUSH_SYNC_ACTIVE_THREADS <= 0);
	}
	
	public static void notifyPushSyncThreadFinished() {
		PUSH_SYNC_ACTIVE_THREADS = PUSH_SYNC_ACTIVE_THREADS - 1;
		PUSH_SYNC_BUSY = (PUSH_SYNC_ACTIVE_THREADS <= 0);
	}
	
	public static void notifyPullSyncThreadFinished() {
		PULL_SYNC_ACTIVE_THREADS = PULL_SYNC_ACTIVE_THREADS - 1;
		PUSH_SYNC_BUSY = (PUSH_SYNC_ACTIVE_THREADS <= 0);
	}
	
	private static void registerSyncEntities() {
		sync_entities = new ArrayList<String>();
		sync_entities.add(Contact.class.getName());
		sync_entities.add(Message.class.getName());
		sync_entities.add(ScheduledTravel.class.getName());
		sync_entities.add(TravelBroadcast.class.getName());
	}
	
	public static String registerSync(SyncedStorageProvider sync_object) {
		SyncQueue sync_queue_entry = new SyncQueue(sync_object);
		if(sync_queue_entry.getID() <= 0) {
			// new sync entry, so create new record.
			sync_queue_entry.batch_id = UUID.randomUUID().toString();
			sync_queue_entry.batch_sequence = 1;
			sync_queue_entry.sync_object_name = sync_object.getClass().getName();
			sync_queue_entry.sync_record_id = sync_object.getID();
			sync_queue_entry.sync_status = SyncStatus.NOT_SYNCED;
			sync_queue_entry.Save();
		}
		return sync_queue_entry.batch_id;
	}
	
	public static void registerSync(SyncedStorageProvider sync_object,String batch_id) {
		SyncQueue sync_queue_entry = new SyncQueue(sync_object);
		long max_batch_sequence = sync_queue_entry.getMaxSequenceInBatch(batch_id);
		if(sync_queue_entry.getID() <= 0) {
			// new sync entry, so create new record.
			sync_queue_entry.batch_id = batch_id;
			sync_queue_entry.batch_sequence = max_batch_sequence + 1;
			sync_queue_entry.sync_object_name = sync_object.getClass().getName();
			sync_queue_entry.sync_record_id = sync_object.getID();
			sync_queue_entry.sync_status = SyncStatus.NOT_SYNCED;
			
		} else {
			// sync already registered, so just update
			sync_queue_entry.batch_id = batch_id;
			sync_queue_entry.batch_sequence = max_batch_sequence + 1;
			// sync_register.sync_status = SyncStatus.NOT_SYNCED;
		}
		sync_queue_entry.Save();
	}
	
	//NR: Incomming Sync Client <= Server
	public static void request_Sync() {
		for(String sync_entity : sync_entities) {
			Thread sync_register_thread = new Thread(new SyncRegisterThread(sync_entity));
			sync_register_thread.start();
		}
	}
	
	//NR: Outgoing Sync Client => Server
	public static void startSync() {
		SyncQueue pending_sync =  new SyncQueue();
		ArrayList<String> batches = pending_sync.getDistinctBatches();
		for(String batch_id : batches){
			//NR: execute sync for each "sync_objects_in_batch" in separate thread
			ArrayList<SyncQueue> sync_objects_in_batch = pending_sync.getBatch(batch_id);			
			Thread sync_thread = new Thread(new SyncBatchExecutorThread(sync_objects_in_batch));
			sync_thread.start();
		}
	}
}
