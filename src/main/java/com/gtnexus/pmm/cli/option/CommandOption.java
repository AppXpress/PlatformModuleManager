package com.gtnexus.pmm.cli.option;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.gtnexus.pmm.cli.option.options.CustomerOption;
import com.gtnexus.pmm.cli.option.options.HelpOption;
import com.gtnexus.pmm.cli.option.options.LocalDirOption;
import com.gtnexus.pmm.cli.option.options.ModuleOption;
import com.gtnexus.pmm.cli.option.options.SelectOption;

/**
 * Extension of typical command line options interface. Holds AppXpress specific
 * logic.
 * 
 * @author jdonovan
 *
 */
public interface CommandOption {
    @Override
    public String toString();

    public String getLongName();

    public String getFlag();

    public Class<?> getType();

    public String getDescription();

    public boolean hasArg();

    public boolean isHelpFlag();

    // TODO: remove
    public boolean isAppXpressMandatory();

    // TODO: remove
    public boolean shouldBeOmitted();

    // TODO:remove
    public boolean isValid(String val);

    public String getDefaultValue();

    // TODO: remove
    public boolean isStoreableProperty();

    public class StandardOptions {

	public static CommandOption HELP = new HelpOption();
	public static CommandOption CUSTOMER = new CustomerOption();
	public static CommandOption MODULE = new ModuleOption();
	public static CommandOption LOCAL_DIR = new LocalDirOption();
	public static CommandOption SELECT = new SelectOption();

	private static final Set<CommandOption> standardOpts = new ImmutableSet.Builder<CommandOption>()
		.add(HELP)
		.add(CUSTOMER)
		.add(MODULE)
		.add(LOCAL_DIR)
		.add(SELECT)
		.build();

	public static Set<CommandOption> set() {
	    return standardOpts;
	}
    }

}
