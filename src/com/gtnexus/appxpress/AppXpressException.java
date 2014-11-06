package com.gtnexus.appxpress;

public class AppXpressException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2917655270465043358L;

	public AppXpressException() {
		super();
	}

	public AppXpressException(String arg0, Throwable arg1, boolean arg2,
			boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public AppXpressException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public AppXpressException(String arg0) {
		super(arg0);
	}

	public AppXpressException(Throwable arg0) {
		super(arg0);
	}

}
