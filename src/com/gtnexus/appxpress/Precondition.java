package com.gtnexus.appxpress;

public interface Precondition<T> {
	public boolean isMet(T obj);
	
	public class EmptyCondition<Y> implements Precondition<Y> {

		@Override
		public boolean isMet(Y obj) {
			return true;
		}
		
	}
		
}
