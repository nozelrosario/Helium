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
	
	
	public ArrayList<String> Columns() {
		ArrayList<String> columns = new ArrayList<String>();
		columns.addAll(super.Columns());
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

	public ArrayList<String> ColumnOptions() {
		ArrayList<String> column_options = new ArrayList<String>();
		column_options.addAll(super.ColumnOptions());
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
