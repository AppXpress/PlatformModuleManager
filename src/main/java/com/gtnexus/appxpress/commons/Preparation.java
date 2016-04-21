package com.gtnexus.appxpress.commons;

import com.gtnexus.appxpress.Exception.AppXpressException;


public interface Preparation<T> {

	public void prepare(T t) throws AppXpressException;
	
}
