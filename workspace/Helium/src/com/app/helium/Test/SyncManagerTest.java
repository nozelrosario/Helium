package com.app.helium.Test;

import com.app.helium.Application;
import com.app.helium.SyncedStorage.IPullSyncCompleteListener;
import com.app.helium.SyncedStorage.IPullSyncStartedListener;
import com.app.helium.SyncedStorage.IPushSyncCompleteListener;
import com.app.helium.SyncedStorage.IPushSyncStartedListener;
import com.app.helium.SyncedStorage.SyncManager;

public class SyncManagerTest  implements ITestable {
	
	public void runTest() {
		SyncManager.addListener(new PushSyncStartNotify());
		SyncManager.addListener(new PushSyncFinishNotify());
		SyncManager.addListener(new PullSyncStartNotify());
		SyncManager.addListener(new PullSyncFinishNotify());
		//NR : TODO : implement test cases for Thread-Start/Finish events as well. Currently not tested as wiill be used less frequently
		
		
		//NR: TEST : trigger outgoing sync for data in sync queue
				if(!SyncManager.PUSH_SYNC_BUSY) {
					SyncManager.startPushSync();	
				}

		//NR: TEST : trigger incoming data sync for Entities registered with SyncManager 
				if(!SyncManager.PULL_SYNC_BUSY) {
					SyncManager.startPullSync();
				}
	}
	
	// NR: Listener Classes
	
	public class PushSyncStartNotify implements IPushSyncStartedListener {

		@Override
		public void run() {
			Application.logInfo("NOTIFICATION!!!...Start Push Sync Listener called...");		
		}
		
	}
	
	public class PushSyncFinishNotify implements IPushSyncCompleteListener {

		@Override
		public void run() {
			Application.logInfo("NOTIFICATION!!!...Finished Push Sync Listener called...");		
		}
		
	}
	
	public class PullSyncStartNotify implements IPullSyncStartedListener {

		@Override
		public void run() {
			Application.logInfo("NOTIFICATION!!!...Start Pull Sync Listener called...");		
		}
		
	}
	
	public class PullSyncFinishNotify implements IPullSyncCompleteListener {

		@Override
		public void run() {
			Application.logInfo("NOTIFICATION!!!...Finished Pull Sync Listener called...");		
		}
		
	}
}
