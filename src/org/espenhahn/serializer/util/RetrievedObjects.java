package org.espenhahn.serializer.util;

public interface RetrievedObjects {

	/**
	 * Save the object as visited in the next available index
	 * @param obj The object to save
	 */
	void save(Object obj);
	
	Object get(int offset) throws IndexOutOfBoundsException;
	
}
