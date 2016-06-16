package com.gtnexus.appxpress.pmbuilder.cli;

import java.io.File;
import java.util.Map;

import com.gtnexus.appxpress.cli.option.AppXpressOption;

public class PMBuilderVO {

	private final File localDir;
	private final File rootFile;
	private File workingDir;

	public PMBuilderVO(Map<AppXpressOption, String> map) {
		this.localDir = new File(map.get(BuilderOption.LOCAL_DIR));
		this.rootFile = localDir.toPath()
				.resolve(map.get(BuilderOption.CUSTOMER))
				.resolve(map.get(BuilderOption.PLATFORM)).toFile();
	}
	
	public File getLocalDir() {
		return localDir;
	}

	public File getRootFile() {
		return rootFile;
	}

	public File getWorkingDir() {
		return workingDir;
	}

	public void setWorkingDir(File workingDir) {
		this.workingDir = workingDir;
	}

}
