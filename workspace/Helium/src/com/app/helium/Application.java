package com.app.helium;
//NR: TODO : in general try to use some library for data parsing eg. GSON : https://code.google.com/p/google-gson/


import com.app.helium.SyncedStorage.SyncManager;

import android.content.Context;
import android.util.Log;

public class Application {

	private static String logger_tag = ApplicationSettings.getInstance().LoggerTag;
	private static String log_level = ApplicationSettings.getInstance().LogLevel;
	
	//NR: This is the place for application Initialization
	// should contain bootstrapping code realted to application
	// load settings /preferences from file/storage , check sync service , check connection etc.
	public static void initialize(Context ctx) {
		
		SyncManager.initialize();
		setContext(ctx);
	}
		
	public static Context getContext() {
		return ApplicationSettings.getInstance().application_context;
	}
	
	public static void setContext(Context ctx){
		 ApplicationSettings.getInstance().application_context = ctx;
	}
	
	public static String getDatabaseName() {
		return ApplicationSettings.getInstance().database_name;
	}
	
	public static void logError(String error_message){
		if(log_level.contains("Error")){
			Log.e(logger_tag, error_message);
		}		
	}
	
	public static void logWarning(String error_message){
		if(log_level.contains("Warning")){
			Log.w(logger_tag, error_message);
		}		
	}
	
	public static void logInfo(String error_message){
		if(log_level.contains("Info")){
			Log.w(logger_tag, error_message);
		}		
	}
	
	public static void logDebug(String error_message){
		if(log_level.contains("Debug")){
			Log.d(logger_tag, error_message);
		}		
	}
	
	public static void logVerbose(String error_message){
		if(log_level.contains("Verbose")){
			Log.d(logger_tag, error_message);
		}		
	}
	
	public static void logError(String sub_tag, String error_message){
		if(log_level.contains("Error")){
			Log.e(logger_tag + "." + sub_tag, error_message);
		}		
	}
	
	public static void logWarning(String sub_tag, String error_message){
		if(log_level.contains("Warning")){
			Log.w(logger_tag + "." + sub_tag, error_message);
		}		
	}
	
	public static void logInfo(String sub_tag, String error_message){
		if(log_level.contains("Info")){
			Log.w(logger_tag + "." + sub_tag, error_message);
		}		
	}
	
	public static void logDebug(String sub_tag, String error_message){
		if(log_level.contains("Debug")){
			Log.d(logger_tag + "." + sub_tag, error_message);
		}		
	}
	
	public static void logVerbose(String sub_tag, String error_message){
		if(log_level.contains("Verbose")){
			Log.d(logger_tag + "." + sub_tag, error_message);
		}		
	}
}
