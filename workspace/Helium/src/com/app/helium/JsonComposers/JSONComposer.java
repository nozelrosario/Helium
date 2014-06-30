package com.app.helium.JsonComposers;

import org.json.JSONObject;

public abstract class JSONComposer implements IJSONComposer {
	
	protected String composer_name="";
	public abstract JSONObject compose(JSONObject json_object);
	public abstract JSONObject deCompose(JSONObject json_object) throws InvalidCompositionException;
	public abstract void setName(String name);
	@Override
	public String getName() {
		return this.composer_name;
	}

}
