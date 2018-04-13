package com.gtnexus.pmm.api.v100.command;

import java.util.Map;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.gtnexus.pmm.AppXpressException;
import com.gtnexus.pmm.api.v100.cli.option.CommandOption;
import com.gtnexus.pmm.api.v100.cli.option.CommandOptionParser;
import com.gtnexus.pmm.api.v100.service.PlatformModuleManagerServices;

public abstract class AbstractSubCommand implements SubCommand {
    
    private final PlatformModuleManagerServices services;
    private final String[] args;
    
    public AbstractSubCommand(PlatformModuleManagerServices services, String ...args) {
	Preconditions.checkNotNull(services);
	Preconditions.checkNotNull(args);
	this.services = services;
	this.args = args;
    }
    
    protected final PlatformModuleManagerServices getServices() {
	return this.services; 
    }
    
    protected final String[] getArgs() {
	return this.args;
    }
    
    protected Map<CommandOption, String> parse() throws AppXpressException {
	return CommandOptionParser
		.createParser(this.getOptions())
		.parse(this.args);
    }
    
    @Override
    public Set<CommandOption> getOptions() {
	return CommandOption.StandardOptions.set();
    }
    
    @Override
    public String getHelpHeader() {
	return "";
    }

    @Override
    public String getHelpFooter() {
	return "";
    }
}
