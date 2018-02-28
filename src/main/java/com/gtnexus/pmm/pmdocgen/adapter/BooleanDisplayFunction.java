package com.gtnexus.pmm.pmdocgen.adapter;

import com.google.common.base.Function;

public abstract class BooleanDisplayFunction<F> implements Function<F, String> {

    @Override
    public final String apply(F input) {
	if (input == null) {
	    return Boolean.FALSE.toString();
	}
	return applyToNonNull(input);
    }

    public abstract String applyToNonNull(F input);

}