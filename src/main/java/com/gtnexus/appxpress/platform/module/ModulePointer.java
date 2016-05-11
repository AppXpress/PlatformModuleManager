package com.gtnexus.appxpress.platform.module;

import java.io.File;
import java.util.Map;

import com.gtnexus.appxpress.Exception.AppXpressException;
import com.gtnexus.appxpress.cli.option.CLICommandOption;
import com.gtnexus.appxpress.pmdocgen.PlatformModuleDocumentGenerator;

public class ModulePointer {

	private final File localDir;
	private final File targetModule;

	public static ModulePointer make(Map<CLICommandOption, String> optMap) throws AppXpressException {
		File localDir = new File(optMap.get(PlatformModuleDocumentGenerator.localDirOpt));
		File target = localDir.toPath().resolve(optMap.get(PlatformModuleDocumentGenerator.customerOpt))
				.resolve(optMap.get(PlatformModuleDocumentGenerator.moduleOpt)).toFile();
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
