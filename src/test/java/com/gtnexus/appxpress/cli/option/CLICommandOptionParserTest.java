package com.gtnexus.appxpress.cli.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.gtnexus.appxpress.pmextractor.cli.DummyOption;
import com.gtnexus.pmm.AppXpressException;
import com.gtnexus.pmm.cli.option.CommandOption;
import com.gtnexus.pmm.cli.option.CommandOptionParser;

public class CLICommandOptionParserTest {

    private final Set<CommandOption> optSet = new ImmutableSet.Builder<CommandOption>().add(DummyOption.DUMMY).build();
    private final CommandOptionParser parser = CommandOptionParser.createParser(optSet);

    @Test
    public void testParse() throws Exception {
	Map<CommandOption, String> parsedOpts = parser.parse(new String[] {"-Dummy"});
	assertTrue(parsedOpts.containsKey(DummyOption.DUMMY));
    }

    @Test(expected = AppXpressException.class)
    public void testParseWithInvalidOpt() throws Exception {
	parser.parse(new String[] {"-invalidOpt"});
    }

    @Test
    public void testGetCliOptionSet() throws Exception {
	assertEquals(EnumSet.allOf(DummyOption.class), parser.getCliOptionSet());
    }

}
