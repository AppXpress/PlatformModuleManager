package com.gtnexus.appxpress.cli;

/**
 * 
 * @author jdonovan
 *
 * @param <T>
 */
public interface Asker<T> {
	
	public static final String INVALID_INPUT = "Invalid input. Please try again.";
	
	public T ask(String message);
	
}
