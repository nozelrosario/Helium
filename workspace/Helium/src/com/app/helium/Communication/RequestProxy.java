package com.app.helium.Communication;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.app.helium.Helper.Util;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;

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
			//NR: Add the String message converters
			restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
			// Set the Content-Type header
			// Make the HTTP POST request, converting the request_data to String, and the String response to a JSONObject
			HttpHeaders requestHeaders = new HttpHeaders();
			requestHeaders.setContentType(new MediaType("application","json"));
			HttpEntity<String> requestEntity = new HttpEntity<String>(request_data.toString(), requestHeaders);
			try {
				ResponseEntity<String> responseEntity = restTemplate.exchange(this.getRequestURL(), HttpMethod.POST, requestEntity, String.class);
				if(responseEntity != null) {
					response_json = new JSONObject(responseEntity.getBody());
					return response_json;
				} else {
					throw(new CommunicationException("REST call Failed !! : Response is Null"));
				}
				
			} catch(RestClientException e) {
				throw(new CommunicationException("REST call Failed !! : " + e.toString()));
			} catch (JSONException e) {
				throw(new CommunicationException("REST call Failed !! : " + e.toString()));
			}
			
		} else {
			throw(new CommunicationException("Device Network connection not online!!"));
		}
		
	}
}
