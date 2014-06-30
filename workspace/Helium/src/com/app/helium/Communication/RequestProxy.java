package com.app.helium.Communication;

import org.json.JSONObject;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.app.helium.Helper.Util;

public class RequestProxy {
	
	private String request_url = "";
	
	public RequestProxy(RequestType request_type) {
		this.setRequestURL(request_type.getURL());
	}
	
	public RequestProxy(RequestType request_type , String query_string) {
		this.request_url = request_type.getURL(query_string);
	}
	
	private void setRequestURL(String url) {
		this.request_url = url;
	}
	
	public String getRequestURL() {
		return this.request_url;
	}
	
	public JSONObject send(JSONObject request_data) throws CommunicationException {
		JSONObject response_json = null;
		if(Util.isDeviceConnectivityOnline()) {
			//NR: Create a new RestTemplate instance
			RestTemplate restTemplate = new RestTemplate();
			//NR: Add the Jackson and String message converters
			restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
			// Make the HTTP POST request, marshaling the request to JSON, and the response to a JSONObject
			try {
				response_json = restTemplate.postForObject(this.getRequestURL(), request_data, JSONObject.class);
				return response_json;
			} catch(RestClientException e) {
				throw(new CommunicationException("REST call Failed !! : " + e.toString()));
			}
			
		} else {
			throw(new CommunicationException("Device Network connection not online!!"));
		}
		
	}
}
