package com.gtnexus.appxpress;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.gtnexus.pmm.api.v100.command.SubCommandException;

public class AppXpressExceptionTest {

    @Test
    public void testGetMessage() {
	StringBuilder sb = new StringBuilder();
	String[] ss = {"1", "2", "3", "4" };
	for (String s : ss) {
	    sb.append(s);
	    sb.append("\n");
	}
	String expected = sb.toString().trim();
	SubCommandException root = new SubCommandException(ss[ss.length - 1]);
	for (int i = ss.length - 2; i >= 0; i--) {
	    root = new SubCommandException(ss[i], root);
	}
	String actual = root.getAppXpressMessage();
	assertEquals(expected, actual);
    }

}
