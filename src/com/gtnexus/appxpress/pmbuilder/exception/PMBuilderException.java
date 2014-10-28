package com.gtnexus.appxpress.pmbuilder.exception;

public class PMBuilderException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5468857470158908903L;

	public PMBuilderException(String message) {
		super(message);
	}
	
	public PMBuilderException(Exception e) {
		super(e);
	}
	
	public PMBuilderException(String message, Exception e) {
		super(message,e);
	}
}
