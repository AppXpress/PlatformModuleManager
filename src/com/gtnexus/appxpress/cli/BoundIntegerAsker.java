package com.gtnexus.appxpress.cli;

import java.io.InputStream;
import java.io.PrintStream;

public class BoundIntegerAsker extends IntegerAsker {
	
	Integer high, low;

	public BoundIntegerAsker(InputStream in, PrintStream out, int low, int high) {
		super(in, out);
		this.high = high;
		this.low = low;
	}
	
	@Override
	protected Integer convert(String m) {
		Integer result = super.convert(m);
		if(result == null || !isInBounds(result)) {
			return null;
		}
		return result;
	}
	
	private boolean isInBounds(Integer integer) {
		return (integer <= high && integer >= low);
	}

	
}
