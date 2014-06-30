package com.app.helium.JsonComposers;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.helium.Application;

public class AuthenticationComposer extends JSONComposer {

	public AuthenticationComposer() {
		this.setName("AITHENTICATION_INFO");
	}

	@Override
	public JSONObject compose(JSONObject json_object) {
		JSONObject composed_json = new JSONObject();
		try {
			//NR: Inject Hash on json_object.toString()
			composed_json.put("composer", this.getName());
			composed_json.put("data", json_object);
			//NR: Add security related info
			//composed_json.put("token", securitytoken);			
		} catch (JSONException e) {
			Application.logError("AuthenticationComposer",e.toString());
		}
		return composed_json;
	}

	@Override
	public JSONObject deCompose(JSONObject json_object) throws InvalidCompositionException {
		// NR: Only Client to Server request requires Authentication, Response doesn't need Authentication
		// NR: Hence return the received object as it is. 
		if(json_object != null) {
			return json_object;
		} else {
			throw new InvalidCompositionException("JSON Data Empty!!");
		}
	}

	@Override
	public void setName(String name) {
		this.composer_name = name;		
	}


}
