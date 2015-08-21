package com.gtnexus.appxpress.pmbuilder.exception;

import com.gtnexus.appxpress.commons.exception.AppXpressException;

public class PMBuilderException extends AppXpressException {

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
