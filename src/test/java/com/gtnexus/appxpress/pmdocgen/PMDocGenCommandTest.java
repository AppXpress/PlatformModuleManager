package com.gtnexus.appxpress.pmdocgen;

import static org.junit.Assert.*;

import org.junit.Test;

public class PMDocGenCommandTest {

	@Test
	public void test() {
		PMDocGenCommand cmd = new PMDocGenCommand("-c", "gap", "-m", "ParcelTracking", "-ld", "/code/gtnexus/platform/customer");
		cmd.execute();
		assertTrue(true);
	}

}
