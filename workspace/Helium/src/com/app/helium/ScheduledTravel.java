package com.app.helium;

import java.util.ArrayList;

import org.json.JSONObject;

import com.app.helium.SyncedStorage.SyncedStorageProvider;

public class ScheduledTravel extends SyncedStorageProvider {

	
	
	
	
	public ScheduledTravel() {
		super();
		this.initializeTable("ScheduledTravel");
	}
	
	public ArrayList<String> Columns() {
		ArrayList<String> columns = new ArrayList<String>();
		columns.addAll(super.Columns());
		columns.add("login");
		columns.add("first_name");
		columns.add("last_name");
		columns.add("phone");
		columns.add("email");
		columns.add("profile_picture");
		columns.add("status_message");
		columns.add("availablity");
		columns.add("latest_geo_location");
		columns.add("is_blocked");
		columns.add("is_approved");
		return columns;
	}

	public ArrayList<String> ColumnOptions() {
		ArrayList<String> column_options = new ArrayList<String>();
		column_options.addAll(super.ColumnOptions());
		column_options.add("TEXT");
		column_options.add("TEXT");
		column_options.add("TEXT");
		column_options.add("TEXT");
		column_options.add("TEXT");
		column_options.add("TEXT");
		column_options.add("TEXT");
		column_options.add("TEXT");
		column_options.add("TEXT");
		column_options.add("TEXT");
		column_options.add("TEXT");
		return column_options;
	}
	
	@Override
	public void LoadData(JSONObject json_data) {
		// TODO Auto-generated method stub
		
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
