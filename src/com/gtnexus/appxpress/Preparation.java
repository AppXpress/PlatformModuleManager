package com.gtnexus.appxpress;

import com.gtnexus.appxpress.pmbuilder.exception.PMBuilderException;

public interface Preparation<T> {

	public void prepare(T t) throws PMBuilderException;
	
}
