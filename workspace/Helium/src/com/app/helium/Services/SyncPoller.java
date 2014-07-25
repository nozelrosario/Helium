package com.app.helium.Services;

import java.util.Calendar;

import com.app.helium.Application;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class SyncPoller {
	public static PollerStatus status = PollerStatus.STOPPED;
	private static AlarmManager sync_alarm_service;
	private static PendingIntent sync_service_pending_intent;
	// restart service every getSyncInterval() seconds
    private static final long REPEAT_TIME = 1000 * Application.getSyncInterval();
	
    public static void startSyncPolling(Context ctx) {
		if(status != PollerStatus.RUNNING) {
			Application.logInfo("Starting Sync Poller..");
			sync_alarm_service = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
			Intent i = new Intent(ctx, StartSyncServiceReciever.class);
			sync_service_pending_intent =  PendingIntent.getBroadcast(ctx, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
			 Calendar cal = Calendar.getInstance();
			// start getSyncInterval() seconds after boot completed
		    cal.add(Calendar.SECOND, Application.getSyncInterval());
		 // fetch every getSyncInterval() seconds
		    // InexactRepeating allows Android to optimize the energy consumption
		    sync_alarm_service.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), REPEAT_TIME, sync_service_pending_intent);
		    // service.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
		    // REPEAT_TIME, pending);
		    status = PollerStatus.RUNNING;
		    
		} else {
			Application.logInfo("Poller already running...");
		}
	}
    
	public static void stopSyncPolling() {
		sync_alarm_service.cancel(sync_service_pending_intent);
		status = PollerStatus.STOPPED;
	}
}
