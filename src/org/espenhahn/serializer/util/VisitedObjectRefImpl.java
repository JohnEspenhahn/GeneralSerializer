package org.espenhahn.serializer.util;

public class VisitedObjectRefImpl implements VisitedObjectRef {

	private int offset;
	
	public VisitedObjectRefImpl() { }
	
	public VisitedObjectRefImpl(int idx) {
		setOffset(idx);
	}

	@Override
	public int getOffset() {
		return this.offset;
	}

	@Override
	public void setOffset(int offset) {
		this.offset = offset;
	}	

}
