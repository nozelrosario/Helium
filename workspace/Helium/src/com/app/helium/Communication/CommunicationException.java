package com.app.helium.Communication;

public class CommunicationException extends Exception {

	/**
	 * TODO : add description
	 */
	private static final long serialVersionUID = 1L;
	private String message;

	public CommunicationException() {
		super();
	}
	
	public CommunicationException(String message) {
		super(message);
		this.message = message;
	}
	
	public CommunicationException(Throwable cause) {
        super(cause);
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
