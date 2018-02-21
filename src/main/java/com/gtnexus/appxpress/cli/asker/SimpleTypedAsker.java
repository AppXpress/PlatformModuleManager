package com.gtnexus.appxpress.cli.asker;

import java.io.InputStream;
import java.io.PrintStream;

public abstract class SimpleTypedAsker<T> implements Asker<T> {

    private SimpleAsker asker;

    public SimpleTypedAsker(InputStream in, PrintStream out) {
	this.asker = new SimpleAsker(in, out);
    }

    public SimpleTypedAsker(SimpleAsker asker) {
	this.asker = asker;
    }

    @Override
    public T ask(String message) {
	String result = asker.ask(message);
	return convert(result);
    }

    protected abstract T convert(String m);

}
