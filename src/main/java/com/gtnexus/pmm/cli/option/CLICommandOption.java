package com.gtnexus.pmm.cli.option;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.gtnexus.pmm.cli.option.options.CustomerOption;
import com.gtnexus.pmm.cli.option.options.HelpOpt;
import com.gtnexus.pmm.cli.option.options.LocalDirOpt;
import com.gtnexus.pmm.cli.option.options.ModuleOption;
import com.gtnexus.pmm.cli.option.options.SelectOption;

/**
 * Extension of typical command line options interface. Holds AppXpress specific
 * logic.
 * 
 * @author jdonovan
 *
 */
public interface CLICommandOption {
    @Override
    public String toString();

    public String getLongName();

    public String getFlag();

    public Class<?> getType();

    public String getDescription();

    public boolean hasArg();

    public boolean isHelpFlag();

    // TODO: remove
    public String getMessage();

    // TODO: remove
    public boolean isAppXpressMandatory();

    // TODO: remove
    public boolean shouldBeOmitted();

    // TODO:remove
    public boolean isValid(String val);

    public String getDefaultValue();

    // TODO: remove
    public boolean isStoreableProperty();

    public class Helper {

	public static CLICommandOption HELP = new HelpOpt();
	public static CLICommandOption CUSTOMER = new CustomerOption();
	public static CLICommandOption MODULE = new ModuleOption();
	public static CLICommandOption LOCAL_DIR = new LocalDirOpt();
	public static CLICommandOption SELECT = new SelectOption();

	private static final Set<CLICommandOption> standardOpts = new ImmutableSet.Builder<CLICommandOption>()
		.add(HELP)
		.add(CUSTOMER)
		.add(MODULE)
		.add(LOCAL_DIR)
		.add(SELECT)
		.build();

	public static Set<CLICommandOption> standardOptions() {
	    return standardOpts;
	}
    }

}
