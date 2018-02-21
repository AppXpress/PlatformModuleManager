package com.gtnexus.appxpress.commons.command;

import com.gtnexus.appxpress.exception.AppXpressException;

public interface Command {
    public void execute() throws AppXpressException;
}
