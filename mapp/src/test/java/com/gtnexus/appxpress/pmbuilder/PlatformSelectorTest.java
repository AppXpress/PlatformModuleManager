package com.gtnexus.appxpress.pmbuilder;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.PrintStream;
import java.util.Arrays;

import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.gtnexus.appxpress.NullOutputStream;
import com.gtnexus.pmm.api.v100.cli.FileSelector;
import com.gtnexus.pmm.api.v100.cli.prompt.BoundIntegerAsker;

public class PlatformSelectorTest {

    @Test
    public void testSelect() {
	PrintStream out = null;
	try {
	    File[] files = new File[10];
	    for (int i = 0; i < 10; i++) {
		files[i] = new File("File" + i);
	    }
	    BoundIntegerAsker asker = Mockito.mock(BoundIntegerAsker.class);
	    Mockito.when(asker.prompt(Matchers.anyString())).thenReturn(1);
	    out = new PrintStream(new NullOutputStream());
	    FileSelector selector = new FileSelector(asker);
	    File f = selector.select(Arrays.asList(files));
	    assertEquals(files[0], f);
	} finally {
	    out.close();
	}
    }
}
