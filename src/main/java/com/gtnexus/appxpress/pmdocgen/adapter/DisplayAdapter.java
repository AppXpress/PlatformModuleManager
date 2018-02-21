package com.gtnexus.appxpress.pmdocgen.adapter;

import java.util.Iterator;
import java.util.Map;

import com.google.common.base.Function;

public abstract class DisplayAdapter<X> implements Iterable<String> {

    protected Map<String, Function<X, String>> adapterMap;

    protected DisplayAdapter(Map<String, Function<X, String>> adapterMap) {
	this.adapterMap = adapterMap;
    }

    public String display(X target, String key) {
	Function<X, String> adapterFn = adapterMap.get(key);
	if (adapterFn == null) {
	    throw new UnsupportedOperationException("There is no adapter mapping for " + key);
	}
	return adapterFn.apply(target);
    }

    @Deprecated
    public Iterator<String> iterateKeys() {
	return adapterMap.keySet().iterator();
    }

    @Override
    public Iterator<String> iterator() {
	return iterateKeys();
    }

    public int size() {
	return adapterMap.size();
    }

}
