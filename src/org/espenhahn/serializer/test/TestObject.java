package org.espenhahn.serializer.test;

import java.io.Serializable;

public class TestObject implements Serializable {
	private static final long serialVersionUID = -3495487564560178602L;
	
	private String s;
	private int i;
	
	public TestObject() { }
	
	public TestObject(String s) {
		setS(s);
	}
	
	public TestObject(String s, int i) {
		this(s);
		setI(i);
	}
	
	public int getI() {
		return i;
	}
	
	public void setI(int i) {
		this.i = i;
	}
	
	public String getS() {
		return s;
	}
	
	public void setS(String s) {
		this.s = s;
	}
	
	@Override
	public int hashCode() {
		return (s == null ? 0 : s.hashCode()) + (i << 7);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof TestObject) {
			return ((((TestObject) o).getS() == null && getS() == null) || ((TestObject) o).getS().equals(getS()))
					&& ((TestObject) o).getI() == getI();
		} else {
			return false;
		}
	}
	
}
