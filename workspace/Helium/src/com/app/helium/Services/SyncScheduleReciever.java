package com.app.helium.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SyncScheduleReciever extends BroadcastReceiver {

	// Start Sync polling after device BOOT.
	  @Override
	  public void onReceive(Context context, Intent intent) {
	    SyncPoller.startSyncPolling(context);
	  }
	  
	} 
