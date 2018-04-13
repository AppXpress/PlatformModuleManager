package com.gtnexus.pmm.app.common;

public interface HasPrerequisite<T> {
    public boolean isMet(T obj);

    public class EmptyCondition<Y> implements HasPrerequisite<Y> {

	@Override
	public boolean isMet(Y obj) {
	    return true;
	}

    }

}