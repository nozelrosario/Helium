package com.app.helium.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class SyncService extends Service {
	  private final IBinder mBinder = new MyBinder();

	  @Override
	  public int onStartCommand(Intent intent, int flags, int startId) {
		  // Strat the sync manager and start sync
	    return Service.START_NOT_STICKY;
	  }

	  @Override
	  public IBinder onBind(Intent arg0) {
	    return mBinder;
	  }

	  public class MyBinder extends Binder {
		  SyncService getService() {
	      return SyncService.this;
	    }
	  }
}
