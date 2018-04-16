package com.gtnexus.pmm.app.platform.module.interpretation;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import com.gtnexus.pmm.api.v100.command.SubCommandException;
import com.gtnexus.pmm.app.common.file.filter.FileFilterFactory;
import com.gtnexus.pmm.app.platform.module.ModuleModelPointer;
import com.gtnexus.pmm.app.platform.module.ModulePointer;
import com.gtnexus.pmm.app.platform.module.ModuleVO;
import com.gtnexus.pmm.app.platform.module.unmarshal.ModuleUnmarshaller;

public class PlatformModuleInterpreter {

    private static final Pattern CA_FILENAME_REGEX = Pattern.compile("\\d+.xml$");
    private static final Pattern CL_FILENAME_REGEX = Pattern.compile("\\d+.xml$");
    private static final Pattern TE_FILENAME_REGEX = Pattern.compile("[a-zA-z-_0-9]+__Rank-\\d+\\.xml$");
    private static final Pattern TM_FILENAME_REGEX = Pattern.compile("^templateMedia.xml$");
    private static final Pattern DESIGN_FILENAME_REGEX = Pattern.compile("^Design_\\w+\\.xml$");

    private final ModulePointer root;

    public PlatformModuleInterpreter(ModulePointer root) {
	this.root = root;
    }

    public ModuleVO interpret() throws SubCommandException {
	return getVO();
    }

    protected ModuleVO getVO() throws SubCommandException {
	ModuleModelPointer filesForDocumentation = collectFilesForDocumentation(root);
	ModuleUnmarshaller unmarshaller = new ModuleUnmarshaller();
	return unmarshaller.unmarshall(filesForDocumentation);
    }

    protected ModuleModelPointer collectFilesForDocumentation(ModulePointer vo) {
	File targetModule = vo.getTargetModule();
	File platModXml = null;
	File[] customActions = null;
	File[] customLinks = null;
	File[] typeExtensions = null;
	File[] templates = null;
	File[] designs = null;
	for (File f : targetModule.listFiles()) {
	    String fileName = f.getName();
	    if ("PlatformModule.xml".equals(fileName)) {
		platModXml = f;
	    } else if ("CustomActionD1".equals(fileName)) {
		customActions = f.listFiles(FileFilterFactory.fileNameMatches(CA_FILENAME_REGEX));
	    } else if ("CustomLinkD1".equals(fileName)) {
		customLinks = f.listFiles(FileFilterFactory.fileNameMatches(CL_FILENAME_REGEX));
	    } else if ("TypeExtensionD1".equals(fileName)) {
		typeExtensions = f.listFiles(FileFilterFactory.fileNameMatches(TE_FILENAME_REGEX));
	    } else if ("template".equals(fileName)) {
		List<File> files = new LinkedList<File>();
		for (File template : f.listFiles()) {
		    if (template.exists() && template.isDirectory()) {
			files.addAll(Arrays
				.asList(template.listFiles(FileFilterFactory.fileNameMatches(TM_FILENAME_REGEX))));
		    }
		}
		templates = files.toArray(new File[files.size()]);
	    } else if ("CustomObjectModule".equals(fileName)) {
		File d = f.toPath().resolve("designs").toFile();
		if (d.exists()) {
		    designs = d.listFiles(FileFilterFactory.fileNameMatches(DESIGN_FILENAME_REGEX));
		}
	    }
	}
	if (platModXml == null) {
	    throw new IllegalStateException("There is no PlatformModule.xml in this module.");
	}
	return ModuleModelPointer.make(vo, platModXml, customActions, customLinks, typeExtensions, templates, designs);
    }
}
