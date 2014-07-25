package com.app.helium.Models;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.json.JSONObject;

import com.app.helium.Models.Types.TravelStatus;
import com.app.helium.SyncedStorage.SyncedStorageProvider;

public class ScheduledTravel extends SyncedStorageProvider {

	public Contact user_id;
	public String from_location;
	public String from_geo_location;
	public String destination_location;
	public String destination_geo_location;
	public String travelling_with;  // should be string pipe list as the contacts might not be shared
	public DateTime travel_date_time;
	public String mode_of_travel;
	public String meeting_point;
	public String travel_route;
	public TravelStatus status;
	public boolean is_recurring;
	public DateTime recurrance_end_date;
	public String recurrance_frequency;
	public DateTime last_travel_date_time;
	public String recurrance_weekdays;
	public int max_companions;
	
	
	
	public ScheduledTravel() {
		super();
		this.initializeTable("ScheduledTravel");
	}
	
	public ArrayList<String> columns() {
		ArrayList<String> columns = new ArrayList<String>();
		columns.addAll(super.columns());
		columns.add("user_id");
		columns.add("from_location");
		columns.add("from_geo_location");
		columns.add("destination_location");
		columns.add("destination_geo_location");
		columns.add("travelling_with");
		columns.add("travel_date_time");
		columns.add("mode_of_travel");
		columns.add("meeting_point");
		columns.add("travel_route");
		columns.add("status");
		columns.add("is_recurring");
		columns.add("recurrance_end_date");
		columns.add("recurrance_frequency");
		columns.add("last_travel_date_time");
		columns.add("recurrance_weekdays");
		columns.add("max_companions");
		
		return columns;
	}

	public ArrayList<String> columnOptions() {
		ArrayList<String> column_options = new ArrayList<String>();
		column_options.addAll(super.columnOptions());
		column_options.add("INTEGER");
		column_options.add("TEXT");
		column_options.add("TEXT");
		column_options.add("TEXT");
		column_options.add("TEXT");
		column_options.add("TEXT");
		column_options.add("INTEGER");
		column_options.add("TEXT");
		column_options.add("TEXT");
		column_options.add("TEXT");
		column_options.add("TEXT");
		column_options.add("TEXT");
		column_options.add("INTEGER");
		column_options.add("TEXT");
		column_options.add("INTEGER");
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
