package com.gtnexus.appxpress.pmdocgen;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class CollectedDocGenVO  {

	private final ModulePointer root;
	private final File platformModuleXml;
	private final List<File> typeExtensions;
	private final List<File> designs;
	
	public static CollectedDocGenVO make(ModulePointer root, File platformModuleXml, File[]typeExtensions, File[] designs) {
		return make(root, platformModuleXml, Arrays.asList(typeExtensions), Arrays.asList(designs));
	}
	
	public static CollectedDocGenVO make(ModulePointer root, File platformModuleXml, List<File> typeExtensions, List<File> designs) {
		return new CollectedDocGenVO(root,  platformModuleXml,typeExtensions, designs);
	}

	private CollectedDocGenVO(ModulePointer root, File platformModuleXml, List<File> typeExtensions, List<File> designs) {
		this.root = root;
		this.platformModuleXml = platformModuleXml;
		this.typeExtensions = typeExtensions;
		this.designs = designs;
	}

	public File getPlatformModuleXml() {
		return platformModuleXml;
	}

	public List<File> getTypeExtensions() {
		return typeExtensions;
	}

	public List<File> getDesigns() {
		return designs;
	}

}
