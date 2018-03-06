package com.gtnexus.pmm.commons.command;

import com.google.common.base.Preconditions;
import com.gtnexus.pmm.AppXpressException;
import com.gtnexus.pmm.PlatformModuleManagerServices;
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
	//TODO: parse() should take the args, not the constructor.
	return CLICommandOptionParser.createParser(this.getOptions(), this.args).parse();
    }
    
}
