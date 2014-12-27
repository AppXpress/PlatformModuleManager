package com.gtnexus.appxpress.cli.asker;

import java.io.InputStream;
import java.io.PrintStream;

public class BoundIntegerAsker extends IntegerAsker {

	private Integer high, low;

	public BoundIntegerAsker(InputStream in, PrintStream out, int low, int high) {
		super(in, out);
		this.high = high;
		this.low = low;
	}

	public BoundIntegerAsker(InputStream in, PrintStream out) {
		super(in, out);
	}
	
	public BoundIntegerAsker(SimpleAsker asker) {
		super(asker);
	}

	@Override
	protected Integer convert(String m) {
		Integer result = super.convert(m);
		if (result == null || !isInBounds(result)) {
			return null;
		}
		return result;
	}

	private boolean isInBounds(Integer integer) {
		checkBoundState();
		return (integer <= high && integer >= low);
	}

	public Integer getUpperBound() {
		checkBoundState();
		return high;
	}
	
	public void setUpperBound(Integer newUpper) {
		high = newUpper;
	}
	
	public Integer getLowerBound() {
		checkBoundState();
		return low;
	}
	
	public void setLowerBound(Integer newLower) {
		low = newLower;
	}
	
	private void checkBoundState() {
		if (high == null || low == null) {
			throw new IllegalStateException(
					"Bounds not appropriately set, cannot determine if the input is within.");
		}
	}

}
