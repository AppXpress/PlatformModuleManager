package com.gtnexus.appxpress.context;

import java.io.File;
import java.util.List;

/**
 * Holds references to temp files that should be deleted by a shutdown hook or
 * some other means.
 * 
 * @author jdonovan
 *
 */
public interface TempResourceHolder {
	
	
	public void deleteOnExit(File f);
	
	public List<File> getFilesToDeleteOnExit();
	

}
