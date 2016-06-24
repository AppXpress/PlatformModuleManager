package com.gtnexus.appxpress.platform.module;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ModuleModelPointer  {

	private final ModulePointer root;
	private final File platformModuleXml;
	private final List<File> customActions;
	private final List<File> customLinks;
	private final List<File> typeExtensions;
	private final List<File> designs;
	
	public static ModuleModelPointer make(ModulePointer root, File platformModuleXml, File[]customActions, File[]customLinks, File[]typeExtensions, File[] designs) {
		List<File> designList = asList(designs);
		List<File> caList = asList(customActions);
		List<File> clList = asList(customLinks);
		List<File> teList = asList(typeExtensions);
		return make(root, platformModuleXml, caList, clList, teList, designList);
	}
	
	private static <T> List<T> asList(T[] arr) {
		if(arr == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(arr);
	}
	
	public static ModuleModelPointer make(ModulePointer root, File platformModuleXml, List<File> customActions, List<File> customLinks, List<File> typeExtensions, List<File> designs) {
		return new ModuleModelPointer(root,  platformModuleXml, customActions, customLinks, typeExtensions, designs);
	}

	private ModuleModelPointer(ModulePointer root, File platformModuleXml, List<File> customActions, List<File> customLinks, List<File> typeExtensions, List<File> designs) {
		this.root = root;
		this.platformModuleXml = platformModuleXml;
		this.customActions = customActions;
		this.customLinks = customLinks;
		this.typeExtensions = typeExtensions;
		this.designs = designs;
	}

	public File getPlatformModuleXml() {
		return platformModuleXml;
	}

	public List<File> getCustomActions() {
		return customActions;
	}
	
	public List<File> getCustomLinks() {
		return customLinks;
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
