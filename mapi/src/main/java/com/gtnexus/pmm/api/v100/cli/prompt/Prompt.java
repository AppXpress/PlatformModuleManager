package com.gtnexus.pmm.api.v100.cli.prompt;

/**
 * 
 * @author jdonovan
 *
 * @param <T>
 */
public interface Prompt<T> {

    public static final String INVALID_INPUT = "Invalid input. Please try again.";

    public T prompt(String message);

}
