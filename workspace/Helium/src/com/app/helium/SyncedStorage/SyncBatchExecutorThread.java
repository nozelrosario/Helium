package com.app.helium.SyncedStorage;

import java.util.ArrayList;

import com.app.helium.Application;

public class SyncBatchExecutorThread implements Runnable {

	private ArrayList<PushSyncQueue> batch_to_sync;
	
	public SyncBatchExecutorThread(ArrayList<PushSyncQueue> batch_to_sync) {
		this.batch_to_sync = batch_to_sync;
	}
	
	@Override
	public void run() {
		SyncManager.notifyPushSyncThreadStarted();
		for(PushSyncQueue sync_object : this.batch_to_sync) {
			try {
				sync_object.startSync();
			} catch (GenericSyncException e) {
				Application.logError("Failded Sync for : " + sync_object.sync_object_name + "[" + sync_object.sync_record_id + "], Skipping other Sync_objects in the batch["+ sync_object.batch_id +"]");
				break;
			} /*catch (Exception e) {
				Application.logError("Failded Sync for : " + sync_object.sync_object_name + "[" + sync_object.sync_record_id + "], Skipping other Sync_objects in the batch["+ sync_object.batch_id +"]");
				break;
			}*/
		}
		SyncManager.notifyPushSyncThreadFinished();
	}

}
