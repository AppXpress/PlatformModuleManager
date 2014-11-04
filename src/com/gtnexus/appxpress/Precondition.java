package com.gtnexus.appxpress;

public interface Precondition<T> {
	public boolean isMet(T obj);
}
