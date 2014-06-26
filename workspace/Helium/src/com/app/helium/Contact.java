package com.app.helium;

import java.util.ArrayList;
import com.app.helium.SyncedStorage.SyncedStorageProvider;

public class Contact extends SyncedStorageProvider{

	public String login;
	public String first_name;
	public String last_name;
	public String phone;
	public String email;
	public String profile_picture;
	public String status_message;
	public Availablity availablity;
	public String latest_geo_location;
	public boolean is_blocked;
	public boolean is_approved;
	
	public Contact() {
		super();
		this.initializeTable("Contacts");
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
