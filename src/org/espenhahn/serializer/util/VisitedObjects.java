package org.espenhahn.serializer.util;

public interface VisitedObjects {

	/**
	 * Mark the object as visited and save it's index
	 * @param obj The object to mark
	 */
	void visit(Object obj);
	
	/**
	 * Check if the given object is already visited
	 * @param obj The object to visit
	 * @return True if already visited
	 */
	boolean isVisited(Object obj);
	
	/**
	 * Get the index of an already visited object
	 * @param obj The object to lookup
	 * @return The index or -1 if not already visited
	 */
	int getIndex(Object obj);
	
}
