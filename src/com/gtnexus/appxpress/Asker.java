package com.gtnexus.appxpress;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Created by jjdonov on 9/30/14.
 */
public class Asker {

	private final Scanner scanner;
	private final PrintStream out;

	public Asker(InputStream in, PrintStream out) {
		this.scanner = new Scanner(in);
		this.out = out;
	}

	public String ask(String message) {
		out.println(message);
		return scanner.next();
	}

}
