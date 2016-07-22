package com.gtnexus.appxpress.cli;

import java.util.Collection;

public interface Select<T> {

	public T select(Collection<T> choices);
	
}
