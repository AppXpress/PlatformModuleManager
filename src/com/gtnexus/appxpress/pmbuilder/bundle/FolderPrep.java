package com.gtnexus.appxpress.pmbuilder.bundle;

import static com.gtnexus.appxpress.AppXpressConstants.$;
import static com.gtnexus.appxpress.AppXpressConstants.CUSTOM_LINK_D1;
import static com.gtnexus.appxpress.AppXpressConstants.CUSTOM_OBJECT_MODULE;
import static com.gtnexus.appxpress.AppXpressConstants.CUSTOM_UI;
import static com.gtnexus.appxpress.AppXpressConstants.SCRIPTS;
import static com.gtnexus.appxpress.AppXpressConstants.SCRIPT_DESIGN;
import static com.gtnexus.appxpress.AppXpressConstants.TYPE_EXTENSION_D1;

import java.io.File;
import java.util.Arrays;

import com.gtnexus.appxpress.file.FileFilterFactory;
import com.gtnexus.appxpress.file.FileService;
import com.gtnexus.appxpress.file.FilterChain;

public class FolderPrep implements Preparation {
	
	private final FileService fs;
	
	public FolderPrep() {
		fs = new FileService();
	}

	/**
	 * Renames directories, and handle's special cases for specific directories.
	 */
	@Override
	public void prepare(File root) {
		for (File dir : root.listFiles(FileFilterFactory.directoriesOnly())) {
			// TODO this is whack yo. directly ported from original
			String directoryName = dir.getName();
			if (directoryName.endsWith(CUSTOM_LINK_D1)) {
				fs.renameFile(dir, CUSTOM_LINK_D1);
			} else if (directoryName.endsWith(TYPE_EXTENSION_D1)) {
				fs.renameFile(dir, $ + TYPE_EXTENSION_D1);
			} else if (directoryName.endsWith(CUSTOM_UI)) {
				fs.prependToName(Arrays.asList(dir.listFiles()), $);
			} else if (directoryName.endsWith(CUSTOM_OBJECT_MODULE)) {
				fixCustomObjectModule(dir);
			} 
		}
	}
	
	/**
	 * 
	 * @param directory
	 */
	private void fixCustomObjectModule(File directory) {
		// TODO refactor me! But I am better than before :)
		File designFolder = new File(directory.getAbsolutePath()
				+ File.separator + "designs");
		if (designFolder.exists()) {
			fs.renameSetOfFiles(Arrays.asList(designFolder.listFiles()),
					"Design_", "Design_$");
		}
		File scriptFolder = new File(directory.getAbsolutePath()
				+ File.separator + SCRIPTS);
		if (scriptFolder.exists()) {
			File[] files = scriptFolder.listFiles(new FilterChain(
					FileFilterFactory.directoriesOnly(), 
					FileFilterFactory.FileNameDoesNotContain("_$")));
			fs.renameSetOfFiles(Arrays.asList(files), 
					SCRIPT_DESIGN, SCRIPT_DESIGN + $);
		}
		File xsdFolder = new File(directory.getAbsoluteFile() + File.separator
				+ "xsd");
		if (xsdFolder.exists()) {
			fs.prependToName(Arrays.asList(xsdFolder.listFiles()), $);
		}
	}

}
