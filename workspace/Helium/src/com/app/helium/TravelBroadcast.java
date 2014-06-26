package com.app.helium;

import java.util.ArrayList;

import org.json.JSONObject;

import com.app.helium.SyncedStorage.SyncedStorageProvider;

public class TravelBroadcast extends SyncedStorageProvider{

	
	
	
	
	public TravelBroadcast() {
		super();
		this.initializeTable("TravelBroadcast");
	}
	
	
	public ArrayList<String> columns() {
		ArrayList<String> columns = new ArrayList<String>();
		columns.addAll(super.columns());
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

	public ArrayList<String> columnOptions() {
		ArrayList<String> column_options = new ArrayList<String>();
		column_options.addAll(super.columnOptions());
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
	public void loadData(JSONObject json_data) {
		// TODO Auto-generated method stub
		
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
