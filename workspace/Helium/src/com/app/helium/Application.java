package com.app.helium;

import com.app.helium.SyncedStorage.SyncManager;

import android.content.Context;
import android.util.Log;

public class Application {

	private static String LoggerTag = ApplicationSettings.getInstance().LoggerTag;
	private static String LogLevel = ApplicationSettings.getInstance().LogLevel;
	
	//NR: This is the place for application Initialization
	// should contain bootstrapping code realted to application
	// load settings /preferences from file/storage , check sync service , check connection etc.
	public static void initialize(Context ctx) {
		
		SyncManager.initialize();
		SetContext(ctx);
	}
		
	public static Context GetContext() {
		return ApplicationSettings.getInstance().application_context;
	}
	
	public static void SetContext(Context ctx){
		 ApplicationSettings.getInstance().application_context = ctx;
	}
	
	public static String GetDatabaseName() {
		return ApplicationSettings.getInstance().database_name;
	}
	
	public static void LogError(String error_message){
		if(LogLevel.contains("Error")){
			Log.e(LoggerTag, error_message);
		}		
	}
	
	public static void LogWarning(String error_message){
		if(LogLevel.contains("Warning")){
			Log.w(LoggerTag, error_message);
		}		
	}
	
	public static void LogInfo(String error_message){
		if(LogLevel.contains("Info")){
			Log.w(LoggerTag, error_message);
		}		
	}
	
	public static void LogDebug(String error_message){
		if(LogLevel.contains("Debug")){
			Log.d(LoggerTag, error_message);
		}		
	}
	
	public static void LogVerbose(String error_message){
		if(LogLevel.contains("Verbose")){
			Log.d(LoggerTag, error_message);
		}		
	}
	
	public static void LogError(String sub_tag, String error_message){
		if(LogLevel.contains("Error")){
			Log.e(LoggerTag + "." + sub_tag, error_message);
		}		
	}
	
	public static void LogWarning(String sub_tag, String error_message){
		if(LogLevel.contains("Warning")){
			Log.w(LoggerTag + "." + sub_tag, error_message);
		}		
	}
	
	public static void LogInfo(String sub_tag, String error_message){
		if(LogLevel.contains("Info")){
			Log.w(LoggerTag + "." + sub_tag, error_message);
		}		
	}
	
	public static void LogDebug(String sub_tag, String error_message){
		if(LogLevel.contains("Debug")){
			Log.d(LoggerTag + "." + sub_tag, error_message);
		}		
	}
	
	public static void LogVerbose(String sub_tag, String error_message){
		if(LogLevel.contains("Verbose")){
			Log.d(LoggerTag + "." + sub_tag, error_message);
		}		
	}
}
