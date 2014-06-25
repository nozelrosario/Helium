package com.app.helium.SyncedStorage;

public class ReferenceObjectNotSyncedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;

	public ReferenceObjectNotSyncedException() {
		super();
	}
	
	public ReferenceObjectNotSyncedException(String message) {
		super(message);
		this.message = message;
	}
	
	public ReferenceObjectNotSyncedException(Throwable cause) {
        super(cause);
    }
	
	public ReferenceObjectNotSyncedException(String table_name, long record_id, String batch_id) {
		this.message = "Parent Record: " + record_id + "of Table: " + table_name + " with Batch_ID: " + batch_id + " Not Synced.";
	}
	
	@Override
	public String toString() {
		return this.message;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
