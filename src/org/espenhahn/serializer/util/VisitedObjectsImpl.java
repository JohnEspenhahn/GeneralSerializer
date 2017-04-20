package org.espenhahn.serializer.util;

import java.util.ArrayList;
import java.util.List;

public class VisitedObjectsImpl implements VisitedObjects {

	private List<Object> list;
	
	public VisitedObjectsImpl() {
		this.list = new ArrayList<Object>();
	}
	
	@Override
	public void visit(Object obj) {
		if (!isVisited(obj))
			list.add(obj);
	}

	@Override
	public boolean isVisited(Object obj) {
		return list.contains(obj);
	}

	@Override
	public int getIndex(Object obj) {
		return list.indexOf(obj);
	}
	
	public void reset() {
		list.clear();
	}

}
