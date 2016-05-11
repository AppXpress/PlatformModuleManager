package com.gtnexus.appxpress.pmdocgen;

import java.io.File;
import java.util.regex.Pattern;

import com.gtnexus.appxpress.Exception.AppXpressException;
import com.gtnexus.appxpress.commons.file.filter.FileFilterFactory;

public class PlatformModuleInterpreter {
	
	private static final Pattern TE_FILENAME_REGEX = Pattern.compile("[a-zA-Z]+__Rank-\\d+\\.xml$");
	private static final Pattern DESIGN_FILENAME_REGEX = Pattern.compile("^Design_[a-zA-Z]+\\.xml$");
	
	private final ModulePointer root;
	
	public PlatformModuleInterpreter(ModulePointer root) {
		this.root = root;
	}
	
	public ModuleVO interpret() throws AppXpressException {
		return getVO();
	}

	protected ModuleVO getVO() throws AppXpressException {
		CollectedDocGenVO filesForDocumentation = collectFilesForDocumentation(root);
		ModuleUnmarshaller unmarshaller = new ModuleUnmarshaller();
		return unmarshaller.unmarshall(filesForDocumentation);
	}

	protected CollectedDocGenVO collectFilesForDocumentation(ModulePointer vo) {
		File targetModule = vo.getTargetModule();
		File platModXml = null;
		File[] typeExtensions = null;
		File[] designs = null;
		for (File f : targetModule.listFiles()) {
			String fileName = f.getName();
			if ("PlatformModule.xml".equals(fileName)) {
				platModXml = f;
			} else if ("TypeExtensionD1".equals(fileName)) {
				typeExtensions = f.listFiles(FileFilterFactory.fileNameMatches(TE_FILENAME_REGEX));
			} else if ("CustomObjectModule".equals(fileName)) {
				designs = f.listFiles(FileFilterFactory.fileNameMatches(DESIGN_FILENAME_REGEX));
			}
		}
		return CollectedDocGenVO.make(vo, platModXml, typeExtensions, designs);
	}
}
