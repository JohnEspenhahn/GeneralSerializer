package org.espenhahn.serializer.util;

import java.util.HashMap;
import java.util.Map;

public class VisitedObjectsImpl implements VisitedObjects {

	private Map<Object, Integer> map;
	
	public VisitedObjectsImpl() {
		this.map = new HashMap<Object, Integer>();
	}
	
	@Override
	public void visit(Object obj) {
		if (!isVisited(obj))
			map.put(obj, map.size());
	}

	@Override
	public boolean isVisited(Object obj) {
		return map.containsKey(obj);
	}

	@Override
	public int getIndex(Object obj) {
		return map.getOrDefault(obj, -1);
	}
	
	public void reset() {
		map.clear();
	}

}
