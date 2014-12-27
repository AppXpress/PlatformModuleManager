package com.gtnexus.appxpress.commons;

public interface Precondition<T> {
	public boolean isMet(T obj);
	
	public class EmptyCondition<Y> implements Precondition<Y> {

		@Override
		public boolean isMet(Y obj) {
			return true;
		}
		
	}
		
}
