package com.app.helium.SyncedStorage;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.helium.Application;
import com.app.helium.Communication.CommunicationException;
import com.app.helium.Communication.RequestProxy;
import com.app.helium.Communication.RequestType;
import com.app.helium.JsonComposers.DataRequestComposer;
import com.app.helium.JsonComposers.InvalidCompositionException;

public class SyncProxy {
	public SyncStatus sync_status;
	
	public SyncProxy() {}

	// NR: operation_code => Server will decide what sync_operation to do based
	// on the operation_code received
	// NR: json_data_string = > Json data to be sent to server [this should be
	// pure data without control information]]
	public JSONObject requestSync(RequestType operation_code, JSONObject json_data)
			throws GenericSyncException {
		JSONObject json_request = new JSONObject(), json_response = null, composed_json_request, decomposed_json_response = null;
		try {
			json_request.put("operation_code", operation_code.toString());
			json_request.put("data", json_data);
			// NR: create Json composer for composing & DeComposing the Json
			// request and response.
			DataRequestComposer sync_request_composer = new DataRequestComposer();
			composed_json_request = sync_request_composer.compose(json_request);

			Application.logInfo("SyncProxy","Data Ready for Sync: " + composed_json_request.toString());
			//NR: create object of Request proxy & fire request
			RequestProxy sync_request = new RequestProxy(operation_code);
			json_response = sync_request.send(composed_json_request);
			//NR: Decompose the response recieved
			decomposed_json_response = sync_request_composer
					.deCompose(json_response);
		} catch (CommunicationException e) {
			throw (new GenericSyncException(e.toString()));
		} catch (InvalidCompositionException e) {
			throw (new GenericSyncException(e.toString()));
		} catch (JSONException e) {
			throw (new GenericSyncException(e.toString()));
		}
		return decomposed_json_response;
	}
	
}
