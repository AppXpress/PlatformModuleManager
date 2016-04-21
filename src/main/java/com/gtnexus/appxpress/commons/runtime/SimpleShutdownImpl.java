package com.gtnexus.appxpress.commons.runtime;

public class SimpleShutdownImpl implements SimpleShutdown {
	
	public SimpleShutdownImpl(){}

	@Override
	public void shutdown() {
		System.exit(0);
	}
	
	@Override
	public void shutdown(String message) {
		System.out.println(message);
		shutdown();
	}
}
