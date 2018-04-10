package com.gtnexus.pmm.commons;

import com.gtnexus.pmm.api.v100.command.AbstractSubCommand;
import com.gtnexus.pmm.api.v100.command.SubCommand;
import com.gtnexus.pmm.api.v100.service.PlatformModuleManagerServices;

public abstract class InternalSubCommand extends AbstractSubCommand implements SubCommand {

    public InternalSubCommand(PlatformModuleManagerServices services, String[] args) {
	super(services, args);
    }
    

}
