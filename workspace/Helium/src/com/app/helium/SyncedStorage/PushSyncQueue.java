package com.app.helium.SyncedStorage;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.helium.Application;
import com.app.helium.Communication.RequestType;

import android.database.Cursor;

public class PushSyncQueue extends StorageProvider {

	public String batch_id;
	public long batch_sequence;
	public String sync_object_name;
	public long sync_record_id;
	public SyncStatus sync_status;
	public String error_description;
	
	public PushSyncQueue(){
		super();
		this.initializeTable("PushSyncQueue"); 
	}
	
	public PushSyncQueue(SyncedStorageProvider sync_object){
		super();
		this.initializeTable("PushSyncQueue"); 
		this.loadSyncDetails(sync_object);
	}
	
	public PushSyncQueue(String table_name, long record_id){
		super();
		this.initializeTable("PushSyncQueue"); 
		this.loadSyncDetails(table_name, record_id);
	}
	
	@Override
	public ArrayList<String> columns() {
		ArrayList<String> columns = new ArrayList<String>();
		columns.add("batch_id");
		columns.add("batch_sequence");
		columns.add("sync_object_name");
		columns.add("sync_record_id");
		columns.add("sync_status");
		columns.add("error_description");
		return columns;
	}

	@Override
	public ArrayList<String> columnOptions() {
		ArrayList<String> column_options = new ArrayList<String>();
		column_options.add("TEXT");
		column_options.add("INTEGER");
		column_options.add("TEXT");
		column_options.add("INTEGER");
		column_options.add("TEXT");
		column_options.add("TEXT , UNIQUE(sync_object_name, sync_record_id) ON CONFLICT REPLACE");
		return column_options;
	}
	
	public void loadSyncDetails(SyncedStorageProvider sync_object){
		this.loadSyncDetails(sync_object.getClass().getName(), sync_object.getID());
	}
	
	public void loadSyncDetails(String object_name, long record_id){
		//String[] columns = this.table_columns.toArray(new String[this.table_columns.size()]);
		Cursor data_cursor = Database.getDataCursor(this.getAllColumns(), "sync_object_name=? AND sync_record_id=?", new String[]{object_name,String.valueOf(record_id)}, "", "","","asc");
		if(data_cursor != null) {
			while(data_cursor.moveToNext()) {
				this.id =  data_cursor.getLong(data_cursor.getColumnIndex("id"));
				this.batch_id = data_cursor.getString(data_cursor.getColumnIndex("batch_id"));
				this.batch_sequence = data_cursor.getLong(data_cursor.getColumnIndex("batch_sequence"));
				this.sync_object_name = data_cursor.getString(data_cursor.getColumnIndex("sync_object_name"));
				this.sync_record_id = data_cursor.getLong(data_cursor.getColumnIndex("sync_record_id"));
				this.sync_status = SyncStatus.valueOf(data_cursor.getString(data_cursor.getColumnIndex("sync_status")));
				this.error_description = data_cursor.getString(data_cursor.getColumnIndex("error_description"));
			}
		}
	}

	public long getMaxSequenceInBatch(String batch_id){
		long max_batch_sequence = 0;
		String select_query = "SELECT MAX(batch_sequence) from " + this.getTableName() + " WHERE batch_id=?";
		Cursor data_cursor = Database.executeRawQuery(select_query, new String[]{String.valueOf(batch_id)});
		if(data_cursor != null) {
			while(data_cursor.moveToNext()) {
				max_batch_sequence =  data_cursor.getLong(data_cursor.getColumnIndex("batch_sequence"));
			}
		}
		return max_batch_sequence;
	}
	
	public ArrayList<String> getDistinctBatches() {
		ArrayList<String> batch_ids = new ArrayList<String>();
		String distinct_query = "SELECT DISTINCT batch_id from " + this.getTableName();
		Cursor data_cursor = Database.executeRawQuery(distinct_query, null);
		if(data_cursor != null) {
			while(data_cursor.moveToNext()) {
				batch_ids.add(data_cursor.getString(data_cursor.getColumnIndex("batch_id")));
			}
		}
		return batch_ids;
	}
	
	public void registerFailedSync(String sync_faliure_reason){
		// Update Sync Status in Pending Sync table
			this.sync_status = SyncStatus.ERROR;
			this.error_description = sync_faliure_reason;
			this.Save();
			
	   // Update SyncStatus in the respective <ISyncedStorageProvider>Table
			SyncedStorageProvider sync_data_object;
			try {
				sync_data_object = (SyncedStorageProvider) Class.forName(this.sync_object_name).newInstance();
				sync_data_object.loadData(this.sync_record_id);
				sync_data_object.updateSyncInformation(SyncStatus.ERROR);
			} catch (InstantiationException e) {
				Application.logError("Sync","SyncQueue.registerFailedSync() Failed : " + e.toString());
			} catch (IllegalAccessException e) {
				Application.logError("Sync","SyncQueue.registerFailedSync() Failed : " + e.toString());
			} catch (ClassNotFoundException e) {
				Application.logError("Sync","SyncQueue.registerFailedSync() Failed : " + e.toString());
			}	
	}
	
	public ArrayList<PushSyncQueue> getBatch(String sync_batch_id) {
		ArrayList<PushSyncQueue> sync_objects_in_batch = new ArrayList<PushSyncQueue>();
		//String[] columns = this.table_columns.toArray(new String[this.table_columns.size()]);
		Cursor data_cursor = Database.getDataCursor(this.getAllColumns(), "batch_id=? AND sync_status in (?,?)", new String[]{sync_batch_id, String.valueOf(SyncStatus.ERROR), String.valueOf(SyncStatus.NOT_SYNCED)}, "", "","batch_sequence","asc");
		if(data_cursor != null) {
			while(data_cursor.moveToNext()) {
				PushSyncQueue sync_object = new PushSyncQueue();
				sync_object.id =  data_cursor.getLong(data_cursor.getColumnIndex("id"));
				sync_object.batch_id = data_cursor.getString(data_cursor.getColumnIndex("batch_id"));
				sync_object.batch_sequence = data_cursor.getLong(data_cursor.getColumnIndex("batch_sequence"));
				sync_object.sync_object_name = data_cursor.getString(data_cursor.getColumnIndex("sync_object_name"));
				sync_object.sync_record_id = data_cursor.getLong(data_cursor.getColumnIndex("sync_record_id"));
				sync_object.sync_status = SyncStatus.valueOf(data_cursor.getString(data_cursor.getColumnIndex("sync_status")));
				sync_object.error_description = data_cursor.getString(data_cursor.getColumnIndex("error_description"));
				sync_objects_in_batch.add(sync_object);
			}
		}
		return sync_objects_in_batch;
	}
	
	public void startSync() throws GenericSyncException {
		boolean sync_complete = false;
		String sync_faliure_reason="";
		JSONObject response_json = null;
		try {
			// Mark as Busy
			this.sync_status = SyncStatus.BUSY;
			this.Save();
			//this.Database.destroy();
		
			SyncedStorageProvider sync_data_object = (SyncedStorageProvider) Class.forName(this.sync_object_name).newInstance();
			sync_data_object.loadData(this.sync_record_id);
			JSONObject sync_data_json = sync_data_object.getJSON();
		//NR: create new sync proxy and trigger sync
			SyncProxy sync_proxi = new SyncProxy();
			response_json = sync_proxi.requestSync(RequestType.PUSH_DATA_SYNC,sync_data_json);
		//NR: TODO: Expected JSON = {remote_id:123 , sync_success:true/false , sync_error:"error if any"}
			if(response_json != null) {
				long remote_id = 0;
				String sync_error = "none";
				boolean sync_success = false;
				if(response_json.has("remote_id")) remote_id = response_json.getLong("remote_id");
				if(response_json.has("sync_error")) sync_error = response_json.getString("sync_error");
				if(response_json.has("sync_success")) sync_success = response_json.getBoolean("sync_success");
				
				if(sync_success && (remote_id > 0)) {
					sync_data_object.updateSyncInformation(remote_id, SyncStatus.SUCCESS, sync_error);
				// Delete current queue entry after successful sync.
					this.delete(); 
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
		} catch (ReferenceObjectNotSyncedException e) {
			sync_faliure_reason =  e.toString();
		} catch (GenericSyncException e) {
			sync_faliure_reason =  e.toString();
		} catch (JSONException e) {
			sync_faliure_reason = "Response JSON not valid :" + e.toString();
		}
		
		if(!sync_complete) {
			Application.logError("SyncQueue", "Sync Faliure for[ "+ this.sync_object_name +" ] : " + sync_faliure_reason);
			// NR: Register Failed Sync
			this.registerFailedSync(sync_faliure_reason);
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
