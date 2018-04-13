package com.gtnexus.pmm.app;

import java.io.File;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

/**
 * Constants used across the PlatformModuleBuilder tool set. <b>PLEASE KEEP IN
 * ALPHABETICAL ORDER</b>
 */
public interface AppXpressConstants {

    public static final String $ = "$";
    public static final String APPXRESS_FOLDER = ".appxpress";
    public static final String BACKUP_FLDR = "PM_Git_Backup";
    public static final String BUNDLE = "Bundle";
    public static final String CUSTOM_ACTION_D1 = "CustomActionD1";
    public static final String CUSTOM_LINK_D1 = "CustomLinkD1";
    public static final String CUSTOM_OBJECT_MODULE = "CustomObjectModule";
    public static final String CUSTOM_UI = "customUi";
    public static final String CUSTOMER = "customer";
    public static final String DESIGNS = "designs";
    public static final String DESIGN_ = "Design_";
    public static final String DIR_TEMPLATE = "%s" + File.separator + APPXRESS_FOLDER;
    public static final String IMPORT_FLAG = "!import";
    public static final String JS_EXTENSION = ".js";
    public static final String LIB = "lib";
    public static final String PLATFORM_MODULE_UNZIP_NAME = "PlatModX";
    public static final String PROPERTIES_EXTENSION = ".properties";
    public static final String PROPERTIES_FILE_NAME = "pmm" + AppXpressConstants.PROPERTIES_EXTENSION;
    public static final String SCRIPTS = "scripts";
    public static final String SCRIPT_DESIGN = "ScriptDesign_";
    public static final String SCRIPTING_FEATURE = "scriptingFeature";
    public static final String TYPE_EXTENSION_D1 = "TypeExtensionD1";
    public static final String UI_BUNDLE_PREFIX = "__";
    public static final String XML_EXTENSION = ".xml";
    public static final String ZIP_EXTENSION = ".zip";

    public static final Set<String> IGNORE_SET = new ImmutableSet.Builder<String>()
	    .add("_MACOSX")
	    .add("__MACOSX")
	    .add(".DS_Store")
	    .build();
}
