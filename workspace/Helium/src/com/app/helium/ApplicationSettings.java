package com.app.helium;

import android.content.Context;

public class ApplicationSettings {			// Singleton Class
	
	private static ApplicationSettings application_data_instance;
	public Context application_context;
	public String database_name = "Helium";
	public String LogLevel ="Error|Info|Warning|Debug|Verbose";    // Error|Info|Warning|Debug|Verbose
	public String LoggerTag = "Helium";

	
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
