package com.app.helium.Services;

import com.app.helium.Application;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class SyncService extends Service {
	  private final IBinder sync_binder = new SyncServiceBinder();

	  @Override
	  public int onStartCommand(Intent intent, int flags, int startId) {
		  // Strat the sync manager and start sync
		Application.logInfo("Sync Service","Started service...and...running");
	    return Service.START_NOT_STICKY;
	  }

	  @Override
	  public IBinder onBind(Intent arg0) {
	    return sync_binder;
	  }

	  public class SyncServiceBinder extends Binder {
		  SyncService getService() {
	      return SyncService.this;
	    }
	  }
}
