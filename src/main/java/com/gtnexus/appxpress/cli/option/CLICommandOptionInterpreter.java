package com.gtnexus.appxpress.cli.option;

import java.util.Map;

import com.gtnexus.appxpress.exception.AppXpressException;

public interface CLICommandOptionInterpreter {

    public Map<CLICommandOption, String> interpret() throws AppXpressException;

}
