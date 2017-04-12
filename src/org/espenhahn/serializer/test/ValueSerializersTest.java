package org.espenhahn.serializer.test;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.espenhahn.serializer.BinarySerializerImpl;
import org.espenhahn.serializer.ValueSerializerRegistry;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import serialization.Serializer;

public class ValueSerializersTest {

	static Serializer s;
	
	@BeforeClass
	public static void init() {
		ValueSerializerRegistry.initDefault();
		s = new BinarySerializerImpl();
	}
	
	@Test
	public void testInt() {
		assertSavesEquals(1);
		assertSavesEquals(0);
		assertSavesEquals(Integer.MAX_VALUE);
		assertSavesEquals(Integer.MIN_VALUE);
	}
	
	@Test
	public void testShort() {
		assertSavesEquals(Short.MAX_VALUE);
		assertSavesEquals(Short.MIN_VALUE);
	}
	
	@Test
	public void testDouble() {
		assertSavesEquals(Double.MAX_VALUE);
		assertSavesEquals(Double.MIN_VALUE);
	}
	
	@Test
	public void testFloat() {
		assertSavesEquals(Float.MAX_VALUE);
		assertSavesEquals(Float.MIN_VALUE);
	}
	
	@Test
	public void testBoolean() {
		assertSavesEquals(true);
		assertSavesEquals(false);
	}
	
	@Test
	public void testString() {
		assertSavesEquals("test");
		assertSavesEquals("");
		assertSavesEquals("sa.‰‰ˆﬂ‹");
	}
	
	@Test
	public void testNull() {
		assert(apply(null) == null);
	}
	
	@Test
	public void testHashSet() {
		List<Object> l = new ArrayList<Object>();
		l.add(3);
		l.add("string");
		
		HashSet<Object> a = new HashSet<Object>();
		assertSavesEquals(a);
		a.add(null);
		assertSavesEquals(a);
		a.add(1);
		assertSavesEquals(a);
		a.add(l);
		assertSavesEquals(a);		
	}
	
	@Test
	public void testArrayList() {
		List<Object> a = new ArrayList<Object>();
		assertSavesEquals(a);		
		a.add(new TestObject("test1"));
		assertSavesEquals(a);
	}
	
	Object apply(Object obj) {
		try {
			ByteBuffer bb = s.outputBufferFromObject(obj);
			return s.objectFromInputBuffer(bb);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	void assertSavesEquals(Object obj) {
		Object out = apply(obj);
		Assert.assertEquals(out, obj);
	}

}
