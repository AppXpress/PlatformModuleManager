package com.gtnexus.pmm.cli.option;

import java.util.Map;

import com.gtnexus.pmm.AppXpressException;

public interface CommandOptionInterpreter {

    public Map<CommandOption, String> interpret() throws AppXpressException;

}
