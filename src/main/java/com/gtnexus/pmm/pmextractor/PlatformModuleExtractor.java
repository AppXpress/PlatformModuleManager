package com.gtnexus.pmm.pmextractor;

import java.util.Set;

import com.gtnexus.pmm.AppXpressException;
import com.gtnexus.pmm.cli.option.CLICommandOption;
import com.gtnexus.pmm.commons.Mapper;
import com.gtnexus.pmm.commons.command.PMMCommandInfo;
import com.gtnexus.pmm.context.ContextBasedCleanUp;
import com.gtnexus.pmm.context.PmmContext;
import com.gtnexus.pmm.pmextractor.cli.ExtractorOption;
import com.gtnexus.pmm.pmextractor.gitmap.GitMapper;

/**
 * 
 * @author jdonovan
 *
 */
public class PlatformModuleExtractor implements PMMCommandInfo {

    private static final String NAME = "pmextractor";

    public PlatformModuleExtractor() {
    }

    public void extract(PmmContext context) throws AppXpressException {
	attachCleanUpHook(context);
	Mapper tool = GitMapper.createMapper(context);
	try {
	    tool.doMapping();
	    System.out.println("Success!");
	} catch (AppXpressException e) {
	    context.setTerminatedRegulary(false);
	    throw new AppXpressException("Failure when running pmextractor.", e);
	}
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

}
