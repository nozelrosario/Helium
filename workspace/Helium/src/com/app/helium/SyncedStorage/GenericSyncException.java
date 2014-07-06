package com.app.helium.SyncedStorage;

public class GenericSyncException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;

	public GenericSyncException() {
		super();
	}
	
	public GenericSyncException(String message) {
		super(message);
		this.message = "Sync Failed : Faliure Reason : " + message;
		//Application.LogError("Sync Failed : Faliure Reason : " + this.message);
	}
	
	public GenericSyncException(Throwable cause) {
        super(cause);
    }
	
	public GenericSyncException(PushSyncQueue sync_object, String reason) {
		this.message = "Sync Failed For => Record: " + sync_object.sync_record_id + "of Object: " + sync_object.sync_object_name + " with Batch_ID: " + sync_object.batch_id + "  Faliure Reason : " + reason;
		//Application.LogError(this.message);
	}
	
	public GenericSyncException(PullSyncQueue sync_register_object, String reason) {
		this.message = "Sync Failed For =>  Entity : " + sync_register_object.sync_object_name + "  Faliure Reason : " + reason;
		//Application.LogError(this.message);
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

