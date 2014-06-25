package com.app.helium.Test;

import java.util.Random;

import com.app.helium.Application;
import com.app.helium.Availablity;
import com.app.helium.Contact;
import com.app.helium.SyncedStorage.ReferenceObjectNotSyncedException;

public class ContactTest {
	
	public void runTest() {
		this.createNewContact();
		this.checkData();
		this.updateData();
	}
	
	public void createNewContact() {
		Random rn  = new Random();
		String seed = String.valueOf(rn.nextInt());
		Contact c = new Contact();
		Application.LogInfo("Initializing contact data...");
		c.login= "login" + seed;
		c.first_name = "first_name" + seed;
		c.last_name= "last_name" + seed;
		c.phone = "phone" + seed;
		c.email= "email" + seed;
		c.profile_picture = "profile_picture" + seed;
		c.status_message = "status_message" + seed;
		c.availablity = Availablity.AVAILABLE;
		c.latest_geo_location = "latest_geo_location" + seed;
		c.is_blocked = false;
		c.is_approved = true;
		c.Save();
		
		if(c.id < 0){
			Application.LogInfo("Id is empty...record not saved");
		}
	}

	public void checkData() {
		Contact c = new Contact();
		c.LoadData(1);
		Application.LogInfo("c.login : " + c.login);
		Application.LogInfo("c.first_name : " + c.first_name);
		Application.LogInfo("c.last_name : " + c.last_name);
		Application.LogInfo("c.phone : " + c.phone);
		Application.LogInfo("c.email : " + c.email);
		Application.LogInfo("c.profile_picture : " + c.profile_picture);
		Application.LogInfo("c.status_message : " + c.status_message);
		Application.LogInfo("c.availablity : " + c.availablity);
		Application.LogInfo("c.latest_geo_location : " + c.latest_geo_location);
		Application.LogInfo("c.is_blocked : " + c.is_blocked);
		Application.LogInfo("c.is_approved : " + c.is_approved);
		Application.LogInfo("c.last_modified_date_time : " + c.last_modified_date_time);
		Application.LogInfo("c.id : " + c.id);
		
		try {
			Application.LogInfo("Contacts Json data : " + c.getJSON().toString());
		} catch (ReferenceObjectNotSyncedException e) {
			Application.LogInfo(e.toString());
		}
	}
	
	public void updateData() {
		Contact c = new Contact();
		Application.LogInfo("Loading Data...");
		c.LoadData(1);
		Application.LogInfo("c.id : " + c.id);
		
		Application.LogInfo("Checking Update...modify email  ");
		c.email = "nozel@nozel.com";
		Application.LogInfo("Saving...modified email  ");
		c.Save();
		
		Application.LogInfo("Loding data from DB again..");
		checkData();
	}
	
	
}
