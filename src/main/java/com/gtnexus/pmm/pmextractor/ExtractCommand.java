package com.gtnexus.pmm.pmextractor;

import java.util.Map;
import java.util.Set;

import com.gtnexus.pmm.AppXpressException;
import com.gtnexus.pmm.api.v100.command.AbstractSubCommand;
import com.gtnexus.pmm.api.v100.service.PlatformModuleManagerServices;
import com.gtnexus.pmm.cli.option.CommandOption;
import com.gtnexus.pmm.pmextractor.cli.ExtractorOption;
import com.gtnexus.pmm.pmextractor.gitmap.GitMapper;

public class ExtractCommand extends AbstractSubCommand {

    private static final String NAME = "pmextractor";

    public ExtractCommand(PlatformModuleManagerServices services, String... args) {
	super(services, args);
    }

    @Override
    public String getName() {
	return NAME;
    }

    @Override
    public String getHelpHeader() {
	return "";
    }

    @Override
    public String getHelpFooter() {
	return "";
    }

    @Override
    public Set<CommandOption> getOptions() {
	return ExtractorOption.getAllOptions();
    }

    @Override
    public void execute() throws AppXpressException {
	Map<CommandOption, String> optionsMap = this.parse().getOptionsMap();
	GitMapper tool = GitMapper.createMapper(this.getServices(), optionsMap);
	try {
	    tool.doMapping();
	    System.out.println("Success!");
	} catch (AppXpressException e) {
	    throw new AppXpressException("Failure when running pmextractor.", e);
	}
    }

}
