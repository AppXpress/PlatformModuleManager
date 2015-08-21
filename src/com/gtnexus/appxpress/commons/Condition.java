package com.gtnexus.appxpress.commons;

public interface Condition<T> {
	public boolean isMet(T obj);
	
	public class EmptyCondition<Y> implements Condition<Y> {

		@Override
		public boolean isMet(Y obj) {
			return true;
		}
		
	}
		
}
