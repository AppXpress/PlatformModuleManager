package com.gtnexus.appxpress.pmbuilder.scriptimport;

import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

/**
 * Wrapper class to internalize some logic around what file's may contain
 * import directives.
 * 
 * @author jdonovan
 *
 */
public class ImportFile extends File {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7530032818432622334L;
	// TODO we should maintain a whitelist, not a blacklist.
	final List<String> invalidExtensions = Arrays.asList("zip", "xml", "xsd",
			"xlf");

	public ImportFile(File parent, String child) {
		super(parent, child);
	}

	public ImportFile(String parent, String child) {
		super(parent, child);
	}

	public ImportFile(String pathname) {
		super(pathname);
	}

	public ImportFile(URI uri) {
		super(uri);
	}
	
	public ImportFile(File f) {
		super(f.toURI());
	}

	/**
	 * Checks file by extension to determine if this file should be scanned for
	 * !import statements.
	 * 
	 * @return True if this file should be scanned, else false.
	 */
	public boolean couldHaveImports() {
		if (isDirectory()) {
			return false;
		}
		String[] splitName = getName().split("\\.");
		if (splitName.length > 1
				&& !invalidExtensions.contains(splitName[splitName.length - 1])) {
			return true;
		}
		return false;
	}
	
	@Override
	public ImportFile[] listFiles() {
		return (ImportFile[])super.listFiles();
	}

}
