package com.gtnexus.appxpress.cli.option;

import static org.junit.Assert.fail;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gtnexus.pmm.AppXpressDirResolver;
import com.gtnexus.pmm.AppXpressException;
import com.gtnexus.pmm.cli.FileSelector;
import com.gtnexus.pmm.commons.command.PMMCommandInfo;
import com.gtnexus.pmm.commons.properties.PMProperties;
import com.gtnexus.pmm.commons.runtime.SimpleShutdown;
import com.gtnexus.pmm.pmbuilder.cli.BuilderOption;
import com.gtnexus.pmm.pmbuilder.cli.BuilderOptionInterpreter;

@RunWith(MockitoJUnitRunner.class)
public class BuilderOptionInterpreterTest {

    @Mock
    PMMCommandInfo app;
    @Mock
    SimpleShutdown shutdown;
    @Mock
    PMProperties properties;
    @Mock
    FileSelector selector;

    @Mock
    AppXpressDirResolver resolver;

    BuilderOptionInterpreter interpreter;
    HashMap<BuilderOption, String> optMap = new HashMap<>();

    @Before
    public void setup() {

    }

    @Test(expected = NullPointerException.class)
    public void testPerformCustomInterpretationWithNull() throws Exception {
	interpreter.performCustomInterpretation(null);
    }

    @Ignore("Not yet implemented")
    @Test
    public void testPerformCustomInterpretationWithoutSPFlag() throws AppXpressException {
	// ParsedOptions<BuilderOption> parsedOptions = new ParsedOptions<>(
	// new Options(), new HashMap<BuilderOption, String>(),
	// EnumSet.allOf(BuilderOption.class));
	// BuilderOptionInterpreter interpeter = new BuilderOptionInterpreter(app,
	// shutdown, parsedOptions, properties, selector, resolver);
	fail("unimplemented");
    }

    @Ignore("Not yet implemented")
    @Test
    public void testPerformCustomInterpretationWithSPFlag() throws AppXpressException {

    }
}
