package org.espenhahn.serializer.test;

import java.io.Serializable;

public class TestObject implements Serializable {

	private String s;
	
	public TestObject() { }
	
	public TestObject(String s) {
		this.s = s;
	}
	
	public String getS() {
		return s;
	}
	
	public void setS(String s) {
		this.s = s;
	}
	
	@Override
	public int hashCode() {
		return s.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof TestObject) {
			return ((TestObject) o).getS().equals(getS());
		} else {
			return false;
		}
	}
	
}
