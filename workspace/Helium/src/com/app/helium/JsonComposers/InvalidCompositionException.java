package com.app.helium.JsonComposers;

public class InvalidCompositionException extends Exception {

	/**
	 * TODO: add description
	 */
	private static final long serialVersionUID = 1L;
	private String message;

	public InvalidCompositionException() {
		super();
	}
	
	public InvalidCompositionException(String message) {
		super(message);
		this.message = message;
	}
	
	public InvalidCompositionException(Throwable cause) {
        super(cause);
    }
	
	public InvalidCompositionException(IJSONComposer composer, String missing_attribute) {
		this.message = "JSON data not composed correctly for Composer" + composer.getName() + ". Required Atribute :[ " + missing_attribute + "] is missing";
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