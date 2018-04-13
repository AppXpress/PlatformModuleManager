package com.gtnexus.pmm.app.docgen.adapter;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

/**
 * Place holder, while we wait to write decorators for our value objs. Immutable
 * Maps do not allow for null values. Since we are using this for display
 * purposes only, we can say that a null value is approx the same as the empty
 * string.
 * 
 * This is the worst class name ever.
 *
 * @param <X>
 *            The Key type for this map.
 */
public class NullSafeStringEntryImmutableMapBuilder<X> extends ImmutableMap.Builder<X, String> {

    @Override
    public Builder<X, String> put(X key, String value) {
	return super.put(key, orEmptyString(value));
    }

    @Override
    public Builder<X, String> put(Entry<? extends X, ? extends String> entry) {
	throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Builder<X, String> putAll(Map<? extends X, ? extends String> map) {
	throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Builder<X, String> putAll(Iterable<? extends Entry<? extends X, ? extends String>> entries) {
	// return super.putAll(entries);
	throw new UnsupportedOperationException("Not yet implemented");
    }

    private String orEmptyString(String maybeNull) {
	return maybeNull == null ? "" : maybeNull;
    }

}
