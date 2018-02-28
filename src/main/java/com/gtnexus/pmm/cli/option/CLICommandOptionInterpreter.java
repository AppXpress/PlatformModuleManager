package com.gtnexus.pmm.cli.option;

import java.util.Map;

import com.gtnexus.pmm.AppXpressException;

public interface CLICommandOptionInterpreter {

    public Map<CLICommandOption, String> interpret() throws AppXpressException;

}
