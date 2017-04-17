package org.espenhahn.serializer.util.pool;

public interface ObjectInstantiator<T> {

	T newInstance(Object... args);
	
}
