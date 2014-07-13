package com.app.helium.SyncedStorage;
// NR: TODO : Shabby implementation of Observer Pattern done with lots of redundant code lines.
// NR: TODO : When time permits, combine redundant code using Generics & interfaces.

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
	private static ArrayList<IPullSyncThreadStartedListener> pull_sync_thread_started_listeners = new ArrayList<IPullSyncThreadStartedListener>();
	private static ArrayList<IPullSyncThreadFinishedListener> pull_sync_thread_finished_listeners = new ArrayList<IPullSyncThreadFinishedListener>();
	private static ArrayList<IPushSyncThreadStartedListener> push_sync_thread_started_listeners = new ArrayList<IPushSyncThreadStartedListener>();
	private static ArrayList<IPushSyncThreadFinishedListener> push_sync_thread_finished_listeners = new ArrayList<IPushSyncThreadFinishedListener>();
	
	private static ArrayList<IPullSyncStartedListener> pull_sync_started_listeners = new ArrayList<IPullSyncStartedListener>();
	private static ArrayList<IPullSyncCompleteListener> pull_sync_finished_listeners = new ArrayList<IPullSyncCompleteListener>();
	private static ArrayList<IPushSyncStartedListener> push_sync_started_listeners = new ArrayList<IPushSyncStartedListener>();
	private static ArrayList<IPushSyncCompleteListener> push_sync_finished_listeners = new ArrayList<IPushSyncCompleteListener>();
	
	
	
	public static void initialize() {
		registerSyncEntities();
	}
	
	public static void notifyPushSyncThreadStarted() {
		PUSH_SYNC_ACTIVE_THREADS = PUSH_SYNC_ACTIVE_THREADS + 1;
		PUSH_SYNC_BUSY = (PUSH_SYNC_ACTIVE_THREADS <= 0);
		PushSyncEventInfo event_info = new PushSyncEventInfo();
		for(IPushSyncThreadStartedListener listener : push_sync_thread_started_listeners) {
			listener.run(event_info);
		}
	}
	
	public static void notifyPullSyncThreadStarted(PullSyncQueue sync_register_entry) {
		PULL_SYNC_ACTIVE_THREADS = PULL_SYNC_ACTIVE_THREADS + 1;
		PUSH_SYNC_BUSY = (PUSH_SYNC_ACTIVE_THREADS > 0);
		
		//NR: Fire sync thread started events
		PullSyncEventInfo event_info = new PullSyncEventInfo(sync_register_entry.sync_object_name,sync_register_entry.sync_status,sync_register_entry.last_sync_date_time,sync_register_entry.error_description);
		for(IPullSyncThreadStartedListener listener : pull_sync_thread_started_listeners) {
			listener.run(event_info);
		}
		
	}
	
	public static void notifyPushSyncThreadFinished() {
		PUSH_SYNC_ACTIVE_THREADS = PUSH_SYNC_ACTIVE_THREADS - 1;
		PUSH_SYNC_BUSY = (PUSH_SYNC_ACTIVE_THREADS > 0);
		PushSyncEventInfo event_info = new PushSyncEventInfo();
		for(IPushSyncThreadFinishedListener listener : push_sync_thread_finished_listeners) {
			listener.run(event_info);
		}
		
		//NR: Fire sync Complete events
		if(!PUSH_SYNC_BUSY) {
			for(IPushSyncCompleteListener listener : push_sync_finished_listeners) {
				listener.run();
			}
		}
	}
	
	public static void notifyPullSyncThreadFinished(PullSyncQueue sync_register_entry) {
		PULL_SYNC_ACTIVE_THREADS = PULL_SYNC_ACTIVE_THREADS - 1;
		PUSH_SYNC_BUSY = (PUSH_SYNC_ACTIVE_THREADS > 0);
		//NR: Fire sync complete events
		PullSyncEventInfo event_info = new PullSyncEventInfo(sync_register_entry.sync_object_name,sync_register_entry.sync_status,sync_register_entry.last_sync_date_time,sync_register_entry.error_description);
		for(IPullSyncThreadFinishedListener listener : pull_sync_thread_finished_listeners) {
			listener.run(event_info);
		}
		
		//NR: Fire sync Complete events
		if(!PUSH_SYNC_BUSY) {
			for(IPullSyncCompleteListener listener : pull_sync_finished_listeners) {
				listener.run();
			}
		}
	}
	
	public static void addListener(IPullSyncThreadStartedListener pull_sync_thread_started_listener){
		if(pull_sync_thread_started_listener != null) {
			pull_sync_thread_started_listeners.add(pull_sync_thread_started_listener);
		}
		
	}
	
	public static void addListener(IPullSyncThreadFinishedListener pull_sync_thread_finished_listener){
		if(pull_sync_thread_finished_listener != null) {
			pull_sync_thread_finished_listeners.add(pull_sync_thread_finished_listener);
		}
		
	}
	
	public static void addListener(IPushSyncThreadStartedListener push_sync_thread_started_listener){
		if(push_sync_thread_started_listener != null) {
			push_sync_thread_started_listeners.add(push_sync_thread_started_listener);
		}
		
	}
	
	public static void addListener(IPushSyncThreadFinishedListener push_sync_thread_finished_listener){
		if(push_sync_thread_finished_listener != null) {
			push_sync_thread_finished_listeners.add(push_sync_thread_finished_listener);
		}
		
	}
	
	public static void addListener(IPullSyncStartedListener pull_sync_started_listener){
		if(pull_sync_started_listener != null) {
			pull_sync_started_listeners.add(pull_sync_started_listener);
		}
		
	}
	
	public static void addListener(IPullSyncCompleteListener pull_sync_finished_listener){
		if(pull_sync_finished_listener != null) {
			pull_sync_finished_listeners.add(pull_sync_finished_listener);
		}
		
	}
	
	public static void addListener(IPushSyncStartedListener push_sync_started_listener){
		if(push_sync_started_listener != null) {
			push_sync_started_listeners.add(push_sync_started_listener);
		}
		
	}
	
	public static void addListener(IPushSyncCompleteListener push_sync_finished_listener){
		if(push_sync_finished_listener != null) {
			push_sync_finished_listeners.add(push_sync_finished_listener);
		}
		
	}
	
	private static void registerSyncEntities() {
		sync_entities = new ArrayList<String>();
		sync_entities.add(Contact.class.getName());
		sync_entities.add(Message.class.getName());
		sync_entities.add(ScheduledTravel.class.getName());
		sync_entities.add(TravelBroadcast.class.getName());
	}
	
	public static String registerSync(SyncedStorageProvider sync_object) {
		PushSyncQueue sync_queue_entry = new PushSyncQueue(sync_object);
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
		PushSyncQueue sync_queue_entry = new PushSyncQueue(sync_object);
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
	public static void startPullSync() {
		// NR: trigger Start Sync Events
		for(IPullSyncStartedListener listener : pull_sync_started_listeners) {
			listener.run();
		}
		//NR: Start sync process
		for(String sync_entity : sync_entities) {
			Thread sync_register_thread = new Thread(new SyncRegisterThread(sync_entity));
			sync_register_thread.start();
		}
	}
	
	//NR: Outgoing Sync Client => Server
	public static void startPushSync() {
		// NR: trigger Start Sync Events
		for(IPushSyncStartedListener listener : push_sync_started_listeners) {
			listener.run();
		}
		//NR: Start sync process
		PushSyncQueue pending_sync =  new PushSyncQueue();
		ArrayList<String> batches = pending_sync.getDistinctBatches();
		for(String batch_id : batches){
			//NR: execute sync for each "sync_objects_in_batch" in separate thread
			ArrayList<PushSyncQueue> sync_objects_in_batch = pending_sync.getBatch(batch_id);			
			Thread sync_thread = new Thread(new SyncBatchExecutorThread(sync_objects_in_batch));
			sync_thread.start();
		}
	}
}
