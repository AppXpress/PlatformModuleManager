package com.gtnexus.appxpress.platform.module;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ModuleModelPointer  {

	private final ModulePointer root;
	private final File platformModuleXml;
	private final List<File> typeExtensions;
	private final List<File> designs;
	
	public static ModuleModelPointer make(ModulePointer root, File platformModuleXml, File[]typeExtensions, File[] designs) {
		List<File> designList = asList(designs);
		List<File> teList = asList(typeExtensions);
		return make(root, platformModuleXml, teList, designList);
	}
	
	private static <T> List<T> asList(T[] arr) {
		if(arr == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(arr);
	}
	
	public static ModuleModelPointer make(ModulePointer root, File platformModuleXml, List<File> typeExtensions, List<File> designs) {
		return new ModuleModelPointer(root,  platformModuleXml,typeExtensions, designs);
	}

	private ModuleModelPointer(ModulePointer root, File platformModuleXml, List<File> typeExtensions, List<File> designs) {
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

	public ModulePointer getRoot() {
		return root;
	}

}
