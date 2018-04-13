package com.gtnexus.pmm.api.v100.cli.prompt;

import java.io.InputStream;
import java.io.PrintStream;

public abstract class SimpleTypedPrompt<T> implements Prompt<T> {

    private SimplePrompt asker;

    public SimpleTypedPrompt(InputStream in, PrintStream out) {
	this.asker = new SimplePrompt(in, out);
    }

    public SimpleTypedPrompt(SimplePrompt asker) {
	this.asker = asker;
    }

    @Override
    public T prompt(String message) {
	String result = asker.prompt(message);
	return convert(result);
    }

    protected abstract T convert(String m);

}
