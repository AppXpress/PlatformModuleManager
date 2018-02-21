package com.gtnexus.appxpress.cli.asker;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Created by jjdonov on 9/30/14.
 */
public class SimpleAsker implements Asker<String> {

    private final Scanner scanner;
    private final PrintStream out;

    public SimpleAsker(InputStream in, PrintStream out) {
	this.scanner = new Scanner(in);
	this.out = out;
    }

    @Override
    public String ask(String message) {
	out.println(message);
	return scanner.next();
    }

}
