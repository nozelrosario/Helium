package com.app.helium.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartSyncServiceReciever extends BroadcastReceiver {

	  @Override
	  public void onReceive(Context context, Intent intent) {
	    Intent service = new Intent(context, SyncService.class);
	    context.startService(service);
	  }
} 
