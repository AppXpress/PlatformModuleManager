package com.gtnexus.pmm.commons;

import com.gtnexus.pmm.AppXpressException;

public interface Preparation<T> {

    public void prepare(T t) throws AppXpressException;

}
