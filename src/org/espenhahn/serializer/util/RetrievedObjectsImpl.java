package org.espenhahn.serializer.util;

import java.util.ArrayList;
import java.util.List;

public class RetrievedObjectsImpl implements RetrievedObjects {

	private List<Object> objects;
	
	public RetrievedObjectsImpl() {
		objects = new ArrayList<Object>();
	}
	
	@Override
	public void save(Object obj) {
		objects.add(obj);
	}

	@Override
	public Object get(int offset) throws IndexOutOfBoundsException {
		return objects.get(offset);
	}
	
	public void reset() {
		objects.clear();
	}

}
