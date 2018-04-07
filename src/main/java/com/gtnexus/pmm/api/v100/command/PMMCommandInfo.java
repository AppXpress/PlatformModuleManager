package com.gtnexus.pmm.api.v100.command;

import java.util.Set;

import com.gtnexus.pmm.cli.option.CommandOption;

//TODO: remove once we are ok to drop legacy 
// * CommandOptionInterpreterImpl
// * DocumentGeneratorOptionInterpreter
// * ExtractorOptionInterpreter
@Deprecated
public interface PMMCommandInfo {

    public String getName();

    public String getHelpHeader();

    public String getHelpFooter();

    public  Set<CommandOption> getOptions();
    
}
