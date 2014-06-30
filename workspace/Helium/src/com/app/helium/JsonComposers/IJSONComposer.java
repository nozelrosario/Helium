package com.app.helium.JsonComposers;

import org.json.JSONObject;

public interface IJSONComposer {
 
	public JSONObject compose(JSONObject json_object);
	public JSONObject deCompose(JSONObject json_object) throws InvalidCompositionException;
	public void setName(String name);
	public String getName();
	
}
