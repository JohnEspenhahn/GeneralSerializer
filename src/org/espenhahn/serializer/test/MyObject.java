package org.espenhahn.serializer.test;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public class MyObject implements Serializable {

	public MyObject obj1;
	public int anInt;
	public Collection<Object> objects;
	public Map<String,Object> maps;
	
	
	public MyObject getObj1() {
		return obj1;
	}
	
	public void setObj1(MyObject o) {
		obj1 = o;
	}
	
	public int getAnInt() {
		return this.anInt;
	}
	
	public void setAnInt(int i) {
		anInt = i;
	}
	
	public Collection<Object> getObjects() {
		return objects;
	}
	
	public void setObjects(Collection<Object> objs) {
		this.objects = objs;
	}
	
	public Map<String,Object> getMaps() {
		return maps;
	}
	
	public void setMaps(Map<String,Object> m) {
		maps = m;
	}
	
}
