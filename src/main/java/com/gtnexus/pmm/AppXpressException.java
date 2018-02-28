package com.gtnexus.pmm;

public class AppXpressException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 2917655270465043358L;

    public AppXpressException() {
	super();
    }

    public AppXpressException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
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

    public String getAppXpressMessage() {
	StringBuilder sb = new StringBuilder();
	sb.append(getMessage());
	Throwable t = getCause();
	while (t != null) {
	    sb.append("\n");
	    sb.append(t.getMessage());
	    t = t.getCause();
	}
	return sb.toString().trim();
    }

}
