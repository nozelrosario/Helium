package com.app.helium.Test;

import com.app.helium.SyncedStorage.SyncManager;

public class SyncManagerTest  implements ITestable {
	
	public void runTest() {
		
		//NR: TEST : trigger outgoing sync for data in sync queue
				if(!SyncManager.PUSH_SYNC_BUSY) {
					SyncManager.startSync();	
				}

		//NR: TEST : trigger incoming data sync for Entities registered with SyncManager 
				if(!SyncManager.PULL_SYNC_BUSY) {
					SyncManager.request_Sync();
				}
	}
}
