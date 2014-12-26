package com.gtnexus.appxpress.context;

public class SimpleShutdownImpl implements SimpleShutdown {
	
	public SimpleShutdownImpl(){}

	public void shutdown() {
		System.exit(0);
	}
	
	public void shutdown(String message) {
		System.out.println(message);
		shutdown();
	}
}
