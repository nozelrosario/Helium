package com.app.helium.SyncedStorage;

import java.util.ArrayList;

import com.app.helium.Application;

public class SyncBatchExecutorThread implements Runnable {

	private ArrayList<SyncQueue> batch_to_sync;
	
	public SyncBatchExecutorThread(ArrayList<SyncQueue> batch_to_sync) {
		this.batch_to_sync = batch_to_sync;
	}
	
	@Override
	public void run() {
		for(SyncQueue sync_object : this.batch_to_sync) {
			try {
				sync_object.startSync();
			} catch (GenericSyncException e) {
				Application.LogError("Skipping other Sync_objects in the batch["+ sync_object.batch_id +"]");
				break;
			}
		}
	}

}
