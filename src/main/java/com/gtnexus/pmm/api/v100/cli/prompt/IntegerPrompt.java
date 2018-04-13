package com.gtnexus.pmm.api.v100.cli.prompt;

import java.io.InputStream;
import java.io.PrintStream;

public class IntegerPrompt extends SimpleTypedPrompt<Integer> {

    public IntegerPrompt(InputStream in, PrintStream out) {
	super(in, out);
    }

    public IntegerPrompt(SimplePrompt asker) {
	super(asker);
    }

    @Override
    protected Integer convert(String m) {
	Integer r = null;
	try {
	    r = Integer.valueOf(m);
	} catch (NumberFormatException e) {}
	return r;
    }

}
