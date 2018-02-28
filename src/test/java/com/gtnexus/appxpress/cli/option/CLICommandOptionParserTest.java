package com.gtnexus.appxpress.cli.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.EnumSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.gtnexus.appxpress.pmextractor.cli.DummyOption;
import com.gtnexus.pmm.AppXpressException;
import com.gtnexus.pmm.cli.option.CLICommandOption;
import com.gtnexus.pmm.cli.option.CLICommandOptionParser;
import com.gtnexus.pmm.cli.option.ParsedOptions;

public class CLICommandOptionParserTest {

    CLICommandOptionParser cli;
    String appName = "ParserTest";
    Set<CLICommandOption> optSet = new ImmutableSet.Builder<CLICommandOption>().add(DummyOption.DUMMY).build();

    @Before
    public void setUp() {
	String[] args = { "-Dummy" };
	cli = CLICommandOptionParser.createParser(optSet, args);
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullArgs() {
	new CLICommandOptionParser(null, null, null);
    }

    @Test
    public void testParse() throws Exception {
	ParsedOptions parsedOpts = cli.parse();
	assertTrue(parsedOpts.hasOption(DummyOption.DUMMY));
    }

    @Test(expected = AppXpressException.class)
    public void testParseWithInvalidOpt() throws Exception {
	String[] args = { "-invalidOpt" };
	CLICommandOptionParser cli2 = CLICommandOptionParser.createParser(optSet, args);
	cli2.parse();
    }

    @Test
    public void testGetCliOptionSet() throws Exception {
	assertEquals(EnumSet.allOf(DummyOption.class), cli.getCliOptionSet());
    }

}
