package com.app.helium.Test;

import java.util.Random;

import com.app.helium.Application;
import com.app.helium.Availablity;
import com.app.helium.Contact;
import com.app.helium.SyncedStorage.ReferenceObjectNotSyncedException;
import com.app.helium.SyncedStorage.SyncManager;
import com.app.helium.SyncedStorage.SyncQueue;

public class ContactTest {
	public Contact test_contact;
	
	public void runTest() {
		this.createNewContact();
		this.checkData();
		this.checkUpdate();
		this.checkSync();
		SyncManager.startSync();
		SyncManager.request_Sync();
	}
	
	public void createNewContact() {
		Random rn  = new Random();
		String seed = String.valueOf(rn.nextInt());
		this.test_contact = new Contact();
		Application.logInfo("Initializing contact data...");
		test_contact.login= "login" + seed;
		test_contact.first_name = "first_name" + seed;
		test_contact.last_name= "last_name" + seed;
		test_contact.phone = "phone" + seed;
		test_contact.email= "email" + seed;
		test_contact.profile_picture = "profile_picture" + seed;
		test_contact.status_message = "status_message" + seed;
		test_contact.availablity = Availablity.AVAILABLE;
		test_contact.latest_geo_location = "latest_geo_location" + seed;
		test_contact.is_blocked = false;
		test_contact.is_approved = true;
		test_contact.Save();
		
		if(test_contact.id < 0){
			Application.logInfo("Id is empty...record not saved");
		}
	}

	public void checkData() {
		Contact c = new Contact();
		c.loadData(test_contact.getID());
		Application.logInfo("c.login Matches ? : " + c.login.equals(test_contact.login));
		Application.logInfo("c.first_name Matches ? : " + c.first_name.equals(test_contact.first_name));
		Application.logInfo("c.last_name Matches ? : " + c.last_name.equals(test_contact.last_name));
		Application.logInfo("c.phone Matches ? : " + c.phone.equals(test_contact.phone));
		Application.logInfo("c.email Matches ? : " + c.email.equals(test_contact.email));
		Application.logInfo("c.profile_picture Matches ? : " + c.profile_picture.equals(test_contact.profile_picture));
		Application.logInfo("c.status_message Matches ? : " + c.status_message.equals(test_contact.status_message));
		Application.logInfo("c.availablity Matches ? : " + c.availablity.equals(test_contact.availablity));
		Application.logInfo("c.latest_geo_location Matches ? : " + c.latest_geo_location.equals(test_contact.latest_geo_location));
		Application.logInfo("c.is_blocked Matches ? : " + (c.is_blocked == test_contact.is_blocked));
		Application.logInfo("c.is_approved Matches ? : " + (c.is_approved == test_contact.is_approved));
		Application.logInfo("c.last_modified_date_time Matches ? : " + c.last_modified_date_time.equals(test_contact.last_modified_date_time));
		Application.logInfo("c.id Matches ? : " + (c.id == test_contact.id));
		
		try {
			Application.logInfo("Contacts Json data : " + c.getJSON().toString());
		} catch (ReferenceObjectNotSyncedException e) {
			Application.logInfo(e.toString());
		}
	}
	
	public void checkUpdate() {		
		Application.logInfo("Checking Update...modify email to nozel@nozel.com ");
		test_contact.email = "nozel@nozel.com";
		Application.logInfo("Saving...modified email  ");
		test_contact.Save();
		Application.logInfo("Loding data from DB again..");
		checkData();
	}
	
	public void checkSync() {
		Application.logInfo("Calling TriggerSync()  for Contact....");
		test_contact.triggerSync();
		SyncQueue sync_queue_entry = new SyncQueue(test_contact);
		
		Application.logInfo("sync_queue_entry.batch_id : " + sync_queue_entry.batch_id);
		Application.logInfo("sync_queue_entry.batch_sequence : " + sync_queue_entry.batch_sequence);
		Application.logInfo("sync_queue_entry.sync_object_name : " + sync_queue_entry.sync_object_name);
		Application.logInfo("sync_queue_entry.sync_record_id : " + sync_queue_entry.sync_record_id);
		Application.logInfo("sync_queue_entry.sync_status : " + sync_queue_entry.sync_status);
		Application.logInfo("sync_queue_entry Matches current Record : " + (sync_queue_entry.sync_record_id == test_contact.getID()));
	}
	
}
