package com.app.helium.Communication;

//NR: basic Implementation done currently
// TODO: if required, use a separate argument for headers, query string specifiers in constructor
//		 and implement getters & setters for the same.

public enum RequestType {
	PULL_DATA_SYNC   ("controller/sync"),
	PUSH_DATA_SYNC   ("controller/sync"),
    LOGIN            ("controller/login"),
    REGISTER         ("controller/register");

    private static final String BASE_URL = "http://host/application/";   //NR: base url of server TODO: fetch from Application Settings
    private final String operation_url;
    
    RequestType(String url_componenet) {
        this.operation_url = BASE_URL + url_componenet;
    }
    
    public String getURL() { 
    	return operation_url; 
    }

    public String getURL(String query_string) { 
    	return operation_url + "?" + query_string; 
    } 
}
