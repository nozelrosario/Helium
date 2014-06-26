package com.app.helium.SyncedStorage;

import org.json.JSONObject;

import com.app.helium.Application;

public class SyncProxy {
	private String sync_url;
	public SyncStatus sync_status;
	
	public SyncProxy() {

	}

	
	//NR: operation_code => Server will decide what sync_operation to do based on the operation_code received
	//NR: json_data_string = > Json data to be sent to server [this should be pure data without control information]]
	public JSONObject send(String operation_code, String json_data_string) throws GenericSyncException {
		JSONObject response_json = new JSONObject();
		//TODO:
		// assemble Request Payload {security_info + authentication_info + operation_code + json_data_string}
		// 1. Sync Code
		// 3. Authentication/Authorization Info
		// 4. json_string
		// 5. control_info {if any}
		// FINALLY... makeHttpRequest() to server Also delegate HTTP exceptions to "GenericSyncException" 
		
		Application.logInfo("SyncProxy","Data Ready for Sync: " + json_data_string);
		return response_json;
	}
	
	private void makeHttpRequest() {
		
		//TODO
		Application.logInfo("SyncProxy","Data Ready for Sendinig to server: ");
	}
	
}
