package com.gtnexus.pmm.build.exception;

import com.gtnexus.pmm.api.v100.command.SubCommandException;

public class PMBuilderException extends SubCommandException {

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
	super(message, e);
    }
}
