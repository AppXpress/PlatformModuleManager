package com.gtnexus.appxpress.pmbuilder.bundle;

import java.io.File;

import com.gtnexus.appxpress.pmbuilder.exception.PMBuilderException;

public interface Preparation {

	public void prepare(File f) throws PMBuilderException;
	
}
