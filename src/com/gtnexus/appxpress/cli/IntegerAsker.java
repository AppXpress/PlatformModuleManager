package com.gtnexus.appxpress.cli;

import java.io.InputStream;
import java.io.PrintStream;

public class IntegerAsker extends SimpleTypedAsker<Integer> {
	
	public IntegerAsker(InputStream in, PrintStream out) {
		super(in, out);
	}
	
	@Override
	protected Integer convert(String m) {
		Integer r = null;
		try{
			r = Integer.valueOf(m);
		} catch (NumberFormatException e) {}
		return r;
	}

}
