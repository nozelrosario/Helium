package com.app.helium;

import android.content.Context;

public class ApplicationSettings {			// Singleton Class
	
	private static ApplicationSettings application_data_instance;
	public Context application_context;
	public String database_name = "Helium";
	public String database_version;  //NR: TODO use this in DBAdapter for controling database upgrades
	public String LogLevel ="Error|Info|Warning|Debug|Verbose";    // Error|Info|Warning|Debug|Verbose
	public String LoggerTag = "Helium";
	public int sync_interval = 30;  //NR: (seconds) interval of repeated sync attempt
	
	private ApplicationSettings() {
		// load application data from config  if required
	}
	public static ApplicationSettings getInstance() {
		if(application_data_instance == null) {
			synchronized(ApplicationSettings.class) {
				if(application_data_instance == null) {
					application_data_instance = new ApplicationSettings();
				}
			}
		}
		return application_data_instance;
	}
}
