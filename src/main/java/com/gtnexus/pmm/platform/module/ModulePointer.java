package com.gtnexus.pmm.platform.module;

import java.io.File;
import java.util.Map;

import com.gtnexus.pmm.AppXpressException;
import com.gtnexus.pmm.api.v100.cli.option.CommandOption;
import com.gtnexus.pmm.api.v100.cli.option.CommandOption.StandardOptions;

public class ModulePointer {

    private final File localDir;
    private final File targetModule;

    public static ModulePointer make(Map<CommandOption, String> optMap) throws AppXpressException {
	File localDir = new File(optMap.get(StandardOptions.LOCAL_DIR));
	File target = localDir.toPath().resolve(optMap.get(StandardOptions.CUSTOMER))
		.resolve(optMap.get(StandardOptions.MODULE)).toFile();
	if (!localDir.exists()) {
	    throw new RuntimeException("Fatal error. LocalDir should be set prior to execution.");
	}
	if (!target.exists()) {
	    throw new AppXpressException("PMM was unable to locate the module (" + target.toString() + ")."
		    + " Are you sure this is the correct customer and module name?");
	}

	return new ModulePointer(localDir, target);
    }

    public ModulePointer(File localDir, File targetModule) {
	super();
	this.localDir = localDir;
	this.targetModule = targetModule;
    }

    public File getLocalDir() {
	return localDir;
    }

    public File getTargetModule() {
	return targetModule;
    }

}
