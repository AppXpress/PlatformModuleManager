package com.gtnexus.pmm.api.v100.cli.prompt;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Created by jjdonov on 9/30/14.
 */
public class SimplePrompt implements Prompt<String> {

    private final Scanner scanner;
    private final PrintStream out;

    public SimplePrompt(InputStream in, PrintStream out) {
	this.scanner = new Scanner(in);
	this.out = out;
    }

    @Override
    public String prompt(String message) {
	out.println(message);
	return scanner.next();
    }

}
