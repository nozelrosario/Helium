package com.app.helium;

import java.util.ArrayList;

import org.joda.time.DateTime;

import com.app.helium.SyncedStorage.SyncedStorageProvider;

public class Message extends SyncedStorageProvider {

	public Contact from_user;
	public Contact to_user;
	public MessageLinkType message_linked_to;
	public TravelBroadcast linked_broadcast;
	public ScheduledTravel linked_scheduled_travel;
	public DateTime sent_date_time;
	public boolean is_read;
	public DateTime recieved_date_time;
	public String message_text;
	
	public Message() {
		super();
		this.initializeTable("Message");
	}
	
	
	public ArrayList<String> columns() {
		ArrayList<String> columns = new ArrayList<String>();
		columns.addAll(super.columns());
		columns.add("from_user");
		columns.add("to_user");
		columns.add("message_linked_to");
		columns.add("linked_broadcast");
		columns.add("linked_scheduled_travel");
		columns.add("sent_date_time");
		columns.add("status_message");
		columns.add("is_read");
		columns.add("recieved_date_time");
		columns.add("message_text");
		return columns;
	}

	public ArrayList<String> columnOptions() {
		ArrayList<String> column_options = new ArrayList<String>();
		column_options.addAll(super.columnOptions());
		column_options.add("INTEGER");
		column_options.add("INTEGER");
		column_options.add("TEXT");
		column_options.add("INTEGER");
		column_options.add("INTEGER");
		column_options.add("INTEGER");
		column_options.add("TEXT");
		column_options.add("TEXT");
		column_options.add("INTEGER");
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
