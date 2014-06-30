package com.app.helium.JsonComposers;

import java.util.ArrayList;

import org.json.JSONObject;

public class DataRequestComposer {
	private ArrayList<IJSONComposer> applicable_composers = new ArrayList<IJSONComposer>();
	
	public DataRequestComposer() {
		this.applicable_composers.add(new IntegrityComposer());
		this.applicable_composers.add(new AuthenticationComposer());
	}
	
	public JSONObject compose(JSONObject json_data_object) {
		for(IJSONComposer composer : this.applicable_composers) {
			json_data_object = composer.compose(json_data_object);
		}
		return json_data_object;
	}
	
	public JSONObject deCompose(JSONObject json_data_object) throws InvalidCompositionException {
		for(int i = this.applicable_composers.size() ; i >= 0 ; i--) {
			json_data_object = this.applicable_composers.get(i).deCompose(json_data_object);
		}
		return json_data_object;
	}
	
}
