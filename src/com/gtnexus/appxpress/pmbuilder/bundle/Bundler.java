package com.gtnexus.appxpress.pmbuilder.bundle;

import java.io.File;

import com.gtnexus.appxpress.pmbuilder.exception.PMBuilderException;

public interface Bundler {

	public void bundle(File directory) throws PMBuilderException;
	
}
