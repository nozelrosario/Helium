package com.app.helium.JsonComposers;

import java.security.GeneralSecurityException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;

import com.app.helium.Application;

public class IntegrityComposer extends JSONComposer {

	private String HMAC_SECRET = "secret_key";
	private String HMAC_ALGORITHM = "HmacMD5";   // or HmacSHA1
	public IntegrityComposer() {
		this.setName("INTEGRITY_CHECK");
	}

	@Override
	public JSONObject compose(JSONObject json_object) {
		String calculatd_hash="";
		JSONObject composed_json = new JSONObject();
		try {
			//NR: Inject Hash on json_object.toString()
			composed_json.put("composer", this.getName());
			composed_json.put("data", json_object);
			calculatd_hash = this.calculateHMAC(json_object.toString());
			composed_json.put("hash", calculatd_hash);			
		} catch (JSONException e) {
			Application.logError("IntegrityComposer",e.toString());
		}
		return composed_json;
	}

	@Override
	public JSONObject deCompose(JSONObject json_object) throws InvalidCompositionException {
		//NR Extract the wrapper data from json_object 
		String composer ="", recieved_hash = "" , calcualted_hash ="";
		JSONObject orignal_json_data ;
		if(json_object != null) {
			if(json_object.has("composer")) {
				try {
					composer = json_object.getString("composer");
					if(composer.equals(this.getName())) {
						if(json_object.has("hash")) {
							recieved_hash = json_object.getString("hash");
							if(json_object.has("data")) {
								orignal_json_data = (JSONObject) json_object.get("data");
								calcualted_hash = this.calculateHMAC(orignal_json_data.toString());
								if(calcualted_hash.equals(recieved_hash)) {
									//NR: data is valid and return data
									return orignal_json_data;
								} else {
									//NR: data is tampered and is invalid.
									throw new InvalidCompositionException("Integrity check Failed !! recieved Hash doesnot match Calculated one.");
								}
							} else {
								//NR: data not found
								throw new InvalidCompositionException(this,"data");
							}
						} else {
							//NR: required attribute "hash" not found
							throw new InvalidCompositionException(this,"hash");
						}
					} else {
						//NR: composing sequence incorrect , received composer = composer instead of this.getName();
						throw new InvalidCompositionException("Composer [ "+ this.getName() + "] cannot decompose object of Composer type [" + composer + "], Please check the order/sequence of composers");
					}
				} catch (JSONException e) {
					Application.logError("IntegrityComposer",e.toString());
					throw new InvalidCompositionException("Integrity check Failed !! Invalid JSON.");
				}
			} else {
				 // expected composer this.getName() , no composer entry found
				throw new InvalidCompositionException(this,"composer");
			}
		} else {
			return null;
		}
		
	}

	@Override
	public void setName(String name) {
		this.composer_name = name;		
	}
	
	public String calculateHMAC(String data) {
		try {
			SecretKeySpec signingKey = new SecretKeySpec(HMAC_SECRET.getBytes(),HMAC_ALGORITHM);
			Mac mac = Mac.getInstance(signingKey.getAlgorithm());
			mac.init(signingKey);
			byte[] rawHmac = mac.doFinal(data.getBytes());
			String result = Base64.encodeToString(rawHmac,Base64.DEFAULT);
			return result;
		} catch (GeneralSecurityException e) {
			Application.logError("Unexpected error while creating hash: " + e.toString());
			throw new IllegalArgumentException();
		}
	}


}
