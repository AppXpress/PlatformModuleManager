package com.gtnexus.pmm.pmextractor;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.gtnexus.pmm.AppXpressException;
import com.gtnexus.pmm.api.v100.command.AbstractSubCommand;
import com.gtnexus.pmm.api.v100.command.SubCommandMarker;
import com.gtnexus.pmm.api.v100.service.PlatformModuleManagerServices;
import com.gtnexus.pmm.cli.option.CommandOption;
import com.gtnexus.pmm.cli.option.CommandOption.StandardOptions;
import com.gtnexus.pmm.commons.CommandOptionCompleter;
import com.gtnexus.pmm.pmextractor.cli.ExtractorOption;
import com.gtnexus.pmm.pmextractor.gitmap.GitMapper;

@SubCommandMarker(
	name = "extract",
	description = "runs the platform module extractor tool. for more information please run pmm extract -h"
)
public class ExtractCommand extends AbstractSubCommand {

    private static final String NAME = "pmextractor";

    private final Set<CommandOption> requiredOptions = new ImmutableSet.Builder<CommandOption>()
	    .add(StandardOptions.LOCAL_DIR)
	    .add(StandardOptions.CUSTOMER)
	    .add(StandardOptions.MODULE)
	    .add(ExtractorOption.PLATFORM_ZIP)
	    .build();
    
    private final Set<CommandOption> options = new ImmutableSet.Builder<CommandOption>()
	    .addAll(requiredOptions)
	    .add(StandardOptions.HELP)
	    .add(ExtractorOption.OVERWRITE_FEF)
	    .add(ExtractorOption.OVERWRITE_SCRIPTS)
	    .build();
    
    private final Map<CommandOption, String> defaults = new ImmutableMap.Builder<CommandOption, String>()
	    .put(ExtractorOption.OVERWRITE_FEF, "Y")
	    .put(ExtractorOption.OVERWRITE_SCRIPTS, "Y")
	    .build();
    
    private final CommandOptionCompleter optionsCompleter;
    
    public ExtractCommand(PlatformModuleManagerServices services, String... args) {
	super(services, args);
	this.optionsCompleter = new CommandOptionCompleter(services, requiredOptions, defaults);
    }

    @Override
    public String getName() {
	return NAME;
    }

    @Override
    public Set<CommandOption> getOptions() {
	return options;
    }

    @Override
    public void execute() throws AppXpressException {
	Map<CommandOption, String> optionsMap = optionsCompleter.complete(this.parse());
	GitMapper tool = GitMapper.createMapper(this.getServices(), optionsMap);
	try {
	    tool.doMapping();
	    System.out.println("Success!");
	} catch (AppXpressException e) {
	    throw new AppXpressException("Failure when running pmextractor.", e);
	}
    }

}
