package com.gtnexus.pmm.api.v100.cli;

import java.util.Collection;

public interface Select<T> {

    public T select(Collection<T> choices);

}
