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
	private final List<File> templates;
	private final List<File> designs;
	
	public static ModuleModelPointer make(ModulePointer root, File platformModuleXml, File[]customActions, File[]customLinks, File[]typeExtensions, File[] templates, File[] designs) {
		List<File> designList = asList(designs);
		List<File> caList = asList(customActions);
		List<File> clList = asList(customLinks);
		List<File> teList = asList(typeExtensions);
		List<File> tList = asList(templates);
		return make(root, platformModuleXml, caList, clList, teList, tList, designList);
	}
	
	private static <T> List<T> asList(T[] arr) {
		if(arr == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(arr);
	}
	
	public static ModuleModelPointer make(ModulePointer root, File platformModuleXml, List<File> customActions, List<File> customLinks, List<File> typeExtensions, List<File> templates, List<File> designs) {
		return new ModuleModelPointer(root,  platformModuleXml, customActions, customLinks, typeExtensions, templates, designs);
	}

	private ModuleModelPointer(ModulePointer root, File platformModuleXml, List<File> customActions, List<File> customLinks, List<File> typeExtensions, List<File> templates, List<File> designs) {
		this.root = root;
		this.platformModuleXml = platformModuleXml;
		this.customActions = customActions;
		this.customLinks = customLinks;
		this.typeExtensions = typeExtensions;
		this.templates = templates;
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

	public List<File> getTemplates() {
		return templates;
	}
	
	public List<File> getDesigns() {
		return designs;
	}

	public ModulePointer getRoot() {
		return root;
	}

}
