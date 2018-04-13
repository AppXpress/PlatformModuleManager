package com.gtnexus.pmm.build.cli;

import java.io.File;

public class PMBuilderVO {

    private final File localDir;
    private final File rootFile;
    private File workingDir;
    
    public PMBuilderVO(File localDir, File rootFile) {
	this.localDir = localDir;
	this.rootFile = rootFile;
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
