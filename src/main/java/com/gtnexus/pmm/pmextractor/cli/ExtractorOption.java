package com.gtnexus.pmm.pmextractor.cli;

import java.util.EnumSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.gtnexus.pmm.cli.option.CommandOption;

/**
 * Enumeration of options that can be stored in the Properties file or read in
 * from the CLI.
 */
public enum ExtractorOption implements CommandOption {

    HELP("h", "help", String.class, "Display usage for this tool", false, false, null),
    PLATFORM_ZIP("pz", "platformZip", String.class, "Exported Platform Module Name", true, true, null),
    LOCAL_DIR("ld", "localDir", String.class, "Relative Path of GIT staging folder", true, true, null),
    CUSTOMER("c", "customer", String.class, "Customer of Platform Module", true, true, null),
    SELECT("s", "select", Integer.class, "Select platform from the folders present in the cwd.", false, false, null),
    MODULE("m", "module", String.class, "Platform Module that is being exported", true, true, null),
    OVERWRITE_SCRIPTS("os", "overwriteScripts", Boolean.class, "If Y -> overwriteScripts = true", true, false, "Y"),
    OVERWRITE_FEF("of", "overwriteFef", Boolean.class, "If Y -> overwriteFEF = true", true, false, "Y");

    private final String flag;
    private final String longName;
    private final Class<?> type;
    private final boolean hasArg;
    private final String description;

    private final static Set<CommandOption> allOptions = new ImmutableSet.Builder<CommandOption>()
	    .addAll(EnumSet.allOf(ExtractorOption.class))
	    .build();

    /**
     * 
     * @param longName
     *            The name of this ExtractorOption.
     * @param type
     *            The ExtractorOption type.
     * @param description
     * @param isMandatory
     * @param defaulValue
     */
    private ExtractorOption(String flag, String longName, Class<?> type, String description, boolean hasArg,
	    boolean isMandatory, String defaulValue) {
	this.flag = flag;
	this.longName = longName;
	this.type = type;
	this.hasArg = hasArg;
	this.description = description;
    }

    @Override
    public String toString() {
	return longName;
    }

    @Override
    public String getLongName() {
	return longName;
    }

    @Override
    public Class<?> getType() {
	return type;
    }

    @Override
    public String getDescription() {
	return description;
    }

    @Override
    public boolean hasArg() {
	return hasArg;
    }

    @Override
    public String getFlag() {
	return flag;
    }

    public static Set<CommandOption> getAllOptions() {
	return allOptions;
    }

}
