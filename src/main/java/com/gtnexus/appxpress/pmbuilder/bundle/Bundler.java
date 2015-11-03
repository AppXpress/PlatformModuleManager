package com.gtnexus.appxpress.pmbuilder.bundle;

import java.io.File;

import com.gtnexus.appxpress.AppXpressException;

public interface Bundler {

	public void bundle(File directory) throws AppXpressException;
	
}
