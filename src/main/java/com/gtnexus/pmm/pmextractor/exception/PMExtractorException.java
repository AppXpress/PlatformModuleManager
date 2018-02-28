package com.gtnexus.pmm.pmextractor.exception;

import com.gtnexus.pmm.AppXpressException;

/**
 * Created by jjdonov on 9/29/14.
 */
public class PMExtractorException extends AppXpressException {

    private static final long serialVersionUID = 5468857470158908903L;

    public PMExtractorException(String message) {
	super(message);
    }

    public PMExtractorException(Exception e) {
	super(e);
    }

    public PMExtractorException(String message, Exception e) {
	super(message, e);
    }

}
