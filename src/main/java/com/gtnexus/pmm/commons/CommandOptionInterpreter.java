package com.gtnexus.pmm.commons;

import java.util.Map;

import com.gtnexus.pmm.AppXpressException;
import com.gtnexus.pmm.cli.option.CommandOption;

public interface CommandOptionInterpreter {

    public Map<CommandOption, String> interpret() throws AppXpressException;

}
