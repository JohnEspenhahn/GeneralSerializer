package org.espenhahn.util.pool;

public interface ObjectInstantiator<T> {

	T newInstance(Object... args);
	
}
