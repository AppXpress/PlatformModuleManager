package com.gtnexus.appxpress.pmbuilder.cli;

import java.io.File;
import java.util.Map;

import com.gtnexus.appxpress.cli.option.CLICommandOption;

public class PMBuilderVO {

    private final File localDir;
    private final File rootFile;
    private File workingDir;

    public PMBuilderVO(Map<CLICommandOption, String> optionMap) {
	this.localDir = new File(optionMap.get(BuilderOption.LOCAL_DIR));
	this.rootFile = localDir
		.toPath()
		.resolve(optionMap.get(BuilderOption.CUSTOMER))
		.resolve(optionMap.get(BuilderOption.MODULE))
		.toFile();
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
