package com.app.helium.SyncedStorage;

import java.util.ArrayList;

import org.joda.time.DateTime;

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
	public ArrayList<String> Columns() {
		ArrayList<String> columns = new ArrayList<String>();
		columns.add("sync_object_name");
		columns.add("last_sync_date_time");
		columns.add("sync_status");
		columns.add("error_description");
		return columns;
	}

	@Override
	public ArrayList<String> ColumnOptions() {
		ArrayList<String> column_options = new ArrayList<String>();
		column_options.add("TEXT NOT NULL UNIQUE");
		column_options.add("TEXT");
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
				this.sync_status = SyncStatus.valueOf(results.getString(results.getColumnIndex("sync_status")));
				this.error_description = results.getString(results.getColumnIndex("error_description"));
			}
		} else {
			this.id=0;
			this.last_sync_date_time = null;
			this.sync_status = null;
			this.error_description = null;
		}
	}
	
	@Override
	public void OnAfterSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnBeforeSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnAfterInsert() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnBeforeInsert() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnBeforeUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnAfterUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnBeforeDelete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnAfterDelete() {
		// TODO Auto-generated method stub
		
	}

}
