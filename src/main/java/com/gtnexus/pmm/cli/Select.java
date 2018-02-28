package com.gtnexus.pmm.cli;

import java.util.Collection;

public interface Select<T> {

    public T select(Collection<T> choices);

}
