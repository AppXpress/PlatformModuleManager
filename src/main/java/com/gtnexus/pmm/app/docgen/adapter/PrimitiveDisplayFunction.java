package com.gtnexus.pmm.app.docgen.adapter;

import com.google.common.base.Function;

public abstract class PrimitiveDisplayFunction<F> implements Function<F, String> {

    @Override
    public final String apply(F input) {
	if (input == null) {
	    return "";
	}
	return applyToNonNull(input);
    }

    public abstract String applyToNonNull(F input);

}
