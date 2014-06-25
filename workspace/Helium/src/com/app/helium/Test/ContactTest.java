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
		Application.LogInfo("Initializing contact data...");
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
			Application.LogInfo("Id is empty...record not saved");
		}
	}

	public void checkData() {
		Contact c = new Contact();
		c.LoadData(test_contact.getID());
		Application.LogInfo("c.login Matches ? : " + c.login.equals(test_contact.login));
		Application.LogInfo("c.first_name Matches ? : " + c.first_name.equals(test_contact.first_name));
		Application.LogInfo("c.last_name Matches ? : " + c.last_name.equals(test_contact.last_name));
		Application.LogInfo("c.phone Matches ? : " + c.phone.equals(test_contact.phone));
		Application.LogInfo("c.email Matches ? : " + c.email.equals(test_contact.email));
		Application.LogInfo("c.profile_picture Matches ? : " + c.profile_picture.equals(test_contact.profile_picture));
		Application.LogInfo("c.status_message Matches ? : " + c.status_message.equals(test_contact.status_message));
		Application.LogInfo("c.availablity Matches ? : " + c.availablity.equals(test_contact.availablity));
		Application.LogInfo("c.latest_geo_location Matches ? : " + c.latest_geo_location.equals(test_contact.latest_geo_location));
		Application.LogInfo("c.is_blocked Matches ? : " + (c.is_blocked == test_contact.is_blocked));
		Application.LogInfo("c.is_approved Matches ? : " + (c.is_approved == test_contact.is_approved));
		Application.LogInfo("c.last_modified_date_time Matches ? : " + c.last_modified_date_time.equals(test_contact.last_modified_date_time));
		Application.LogInfo("c.id Matches ? : " + (c.id == test_contact.id));
		
		try {
			Application.LogInfo("Contacts Json data : " + c.getJSON().toString());
		} catch (ReferenceObjectNotSyncedException e) {
			Application.LogInfo(e.toString());
		}
	}
	
	public void checkUpdate() {		
		Application.LogInfo("Checking Update...modify email to nozel@nozel.com ");
		test_contact.email = "nozel@nozel.com";
		Application.LogInfo("Saving...modified email  ");
		test_contact.Save();
		Application.LogInfo("Loding data from DB again..");
		checkData();
	}
	
	public void checkSync() {
		Application.LogInfo("Calling TriggerSync()  for Contact....");
		test_contact.triggerSync();
		SyncQueue sync_queue_entry = new SyncQueue(test_contact);
		
		Application.LogInfo("sync_queue_entry.batch_id : " + sync_queue_entry.batch_id);
		Application.LogInfo("sync_queue_entry.batch_sequence : " + sync_queue_entry.batch_sequence);
		Application.LogInfo("sync_queue_entry.sync_object_name : " + sync_queue_entry.sync_object_name);
		Application.LogInfo("sync_queue_entry.sync_record_id : " + sync_queue_entry.sync_record_id);
		Application.LogInfo("sync_queue_entry.sync_status : " + sync_queue_entry.sync_status);
		Application.LogInfo("sync_queue_entry Matches current Record : " + (sync_queue_entry.sync_record_id == test_contact.getID()));
	}
	
}
