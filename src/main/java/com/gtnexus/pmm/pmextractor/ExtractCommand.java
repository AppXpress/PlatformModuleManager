package com.gtnexus.pmm.pmextractor;

import java.util.Map;
import java.util.Set;

import com.gtnexus.pmm.AppXpressException;
import com.gtnexus.pmm.PlatformModuleManagerServices;
import com.gtnexus.pmm.cli.option.CLICommandOption;
import com.gtnexus.pmm.commons.Mapper;
import com.gtnexus.pmm.commons.command.AbstractSubCommand;
import com.gtnexus.pmm.context.ContextBasedCleanUp;
import com.gtnexus.pmm.context.PmmContext;
import com.gtnexus.pmm.pmextractor.cli.ExtractorOption;
import com.gtnexus.pmm.pmextractor.gitmap.GitMapper;

public class ExtractCommand extends AbstractSubCommand {

    private static final String NAME = "pmextractor";

    private String[] args;

    public ExtractCommand(PlatformModuleManagerServices services, String... args) {
	super(services, args);
	this.args = args;
    }

    private void attachCleanUpHook(PmmContext ctx) {
	Runtime.getRuntime().addShutdownHook(new Thread(new ContextBasedCleanUp(ctx)));
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
    public Class<?> getContextType() {
	return ExtractorOption.class;
    }

    @Override
    public Set<CLICommandOption> getOptions() {
	return ExtractorOption.getAllOptions();
    }

    @Override
    public void execute() throws AppXpressException {
	// attachCleanUpHook(context);
	Map<CLICommandOption, String> optionsMap = this.parse().getOptionsMap();
	Mapper tool = GitMapper.createMapper(this.getServices(), optionsMap);
	try {
	    tool.doMapping();
	    System.out.println("Success!");
	} catch (AppXpressException e) {
	    throw new AppXpressException("Failure when running pmextractor.", e);
	}
    }

}
