package com.app.helium.SyncedStorage;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.app.helium.Application;
import com.app.helium.Helper.Util;

import android.database.Cursor;

public class SyncRegister extends StorageProvider {

	public String sync_object_name;
	public DateTime last_sync_date_time;
	public SyncStatus sync_status;
	public String error_description;
	
	public SyncRegister(){
		super();
		this.initializeTable("SyncRegister"); 
	}
	
	@Override
	public ArrayList<String> columns() {
		ArrayList<String> columns = new ArrayList<String>();
		columns.add("sync_object_name");
		columns.add("last_sync_date_time");
		columns.add("sync_status");
		columns.add("error_description");
		return columns;
	}

	@Override
	public ArrayList<String> columnOptions() {
		ArrayList<String> column_options = new ArrayList<String>();
		column_options.add("TEXT NOT NULL UNIQUE");
		column_options.add("INTEGER");
		column_options.add("TEXT");
		column_options.add("TEXT");
		return column_options;
	}
	
	public void loadRegistryEntry(String sync_entity_name) {
		Cursor results = Database.getDataCursor(this.getAllColumns(), "sync_object_name = ?", new String[] {sync_entity_name}, null, null, null, null);
		if(results != null) {
			while(results.moveToNext()) {
				this.id = results.getLong(results.getColumnIndex("id"));
				this.sync_object_name = results.getString(results.getColumnIndex("sync_object_name"));
				this.last_sync_date_time = Util.convertDBFormatToDateTime(results.getLong(results.getColumnIndex("last_sync_date_time")));
				this.sync_status = results.isNull(results.getColumnIndex("sync_status"))? null:SyncStatus.valueOf(results.getString(results.getColumnIndex("sync_status")));
				this.error_description = results.getString(results.getColumnIndex("error_description"));
			}
		} else {
			this.id=0;
			this.last_sync_date_time = null;
			this.sync_status = null;
			this.error_description = null;
		}
	}
	
	
	public void startSync() throws GenericSyncException {
		boolean sync_complete = false;
		String sync_faliure_reason="";
		JSONObject request_json = new JSONObject();
		JSONObject response_json = null;
		try {
			// Mark as Busy
			this.sync_status = SyncStatus.BUSY;
			this.Save();
		
			
			//JSONObject sync_data_json = sync_data_object.getJSON();
		//Prepare Request JSON
			//Expected Request JSON String : {sync_object_name:"sync_entity_name" last_synced_date_time:"last_synced_timestamp" }
			request_json.put("sync_object_name", this.sync_object_name);
			request_json.put("last_synced_date_time", Util.convertDateTimeToDBFormat(this.last_sync_date_time));
		//NR: create new sync proxy and trigger sync
			SyncProxy sync_proxi = new SyncProxy();
			response_json = sync_proxi.send("PULL_DATA_SYNC", request_json.toString());
		//NR: TODO: Expected JSON = {sync_success:true/false , sync_error:"error if any" , data:[ {<IStorageProvider>}, {<IStorageProvider>}, {<IStorageProvider>}, ... ]}
			if(response_json != null) {
				String sync_error = "none";
				JSONArray data = null;
				boolean sync_success = false;
				if(response_json.has("sync_error")) sync_error = response_json.getString("sync_error");
				if(response_json.has("sync_success")) sync_success = response_json.getBoolean("sync_success");
				if(response_json.has("data")) data = response_json.getJSONArray("data");
				
				if(sync_success) {
					if(data != null) {
						for (int i = 0; i < data.length(); i++) { 
						    JSONObject json_data_object = data.getJSONObject(i);
						    if(json_data_object != null) {
						    	SyncedStorageProvider sync_data_object = (SyncedStorageProvider) Class.forName(this.sync_object_name).newInstance();
								sync_data_object.saveSyncData(json_data_object);
						    }
						    
						}
					}
				sync_complete = true;
				} else {
					sync_faliure_reason = "Response JSON not complete, Error on Server : " + sync_error;
				}
			} else {
				sync_faliure_reason = "Response JSON Empty : not valid ";			
			}
			
		} catch (InstantiationException e) {
			sync_faliure_reason =  e.toString();			
		} catch (IllegalAccessException e) {
			sync_faliure_reason =  e.toString();
		} catch (ClassNotFoundException e) {
			sync_faliure_reason =  e.toString();
		} catch (GenericSyncException e) {
			sync_faliure_reason =  e.toString();
		} catch (JSONException e) {
			sync_faliure_reason = "Response JSON not valid :" + e.toString();
		}
		
		if(sync_complete){			
			this.sync_status = SyncStatus.SUCCESS;
			this.last_sync_date_time = new DateTime();
			this.error_description = "none";
			this.Save();
		} else {
			Application.logError("SyncQueue", "Sync Faliure for ["+ this.sync_object_name + "] : " + sync_faliure_reason);
		// NR: Register Failed Sync
			this.sync_status = SyncStatus.ERROR;
			//this.last_sync_date_time = keep as it is;  //NR: @@@@@@ last_sync_date_time should not be changed else, sync logic will fail. @@@@@@@
			this.error_description = sync_faliure_reason;
			this.Save();
			throw new GenericSyncException(this, sync_faliure_reason);
		}
	}
	
	@Override
	public void onAfterSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBeforeSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAfterInsert() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBeforeInsert() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBeforeUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAfterUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBeforeDelete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAfterDelete() {
		// TODO Auto-generated method stub
		
	}

}
