package com.app.helium.SyncedStorage;

import org.json.JSONObject;

import com.app.helium.Application;

public class SyncProxy {
	private String sync_url;
	public SyncStatus sync_status;
	public boolean sync_direction;
	
	public SyncProxy() {

	}

	
	public JSONObject send(String json_string) throws GenericSyncException {
		JSONObject response_json = new JSONObject();
		//TODO:
		// assemble Request Payload
		// 1. Sync Code
		// 2. SyncDirection
		// 3. Authentication/Authorization Info
		// 4. json_string
		// 5. control_info {if any}
		// FINALLY... makeHttpRequest() to server Also delegate HTTP exceptions to "GenericSyncException" 
		
		Application.LogInfo("SyncProxy","Data Ready for Sync: " + json_string);
		return response_json;
	}
	
	private void makeHttpRequest() {
		
		//TODO
		Application.LogInfo("SyncProxy","Data Ready for Sendinin to server: ");
	}
	
}
