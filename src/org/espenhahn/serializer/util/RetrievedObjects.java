package org.espenhahn.serializer.util;

public interface RetrievedObjects {

	/**
	 * Save the object as visited in the next available index
	 * @param obj The object to save
	 */
	void save(Object obj);
	
	/**
	 * Get an object at an already visited index
	 * @param ref The index to lookup the object in
	 * @return The object
	 * @throws IndexOutOfBoundsException If the object at the given index is not visited yet
	 */
	Object get(VisitedObjectRef ref) throws IndexOutOfBoundsException;
	
}
