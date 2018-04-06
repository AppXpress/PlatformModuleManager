package com.gtnexus.pmm.commons.command;

import java.util.Set;

import com.google.common.base.Preconditions;
import com.gtnexus.pmm.AppXpressException;
import com.gtnexus.pmm.PlatformModuleManagerServices;
import com.gtnexus.pmm.cli.option.CLICommandOption;
import com.gtnexus.pmm.cli.option.CLICommandOptionParser;
import com.gtnexus.pmm.cli.option.ParsedOptions;

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
    
    protected ParsedOptions parse() throws AppXpressException {
	return CLICommandOptionParser
		.createParser(this.getOptions())
		.parse(this.args);
    }
    
    @Override
    public Set<CLICommandOption> getOptions() {
	return CLICommandOption.Helper.standardOptions();
    }
    
}
