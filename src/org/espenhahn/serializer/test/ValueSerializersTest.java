package org.espenhahn.serializer.test;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.espenhahn.serializer.BinarySerializerImpl;
import org.espenhahn.serializer.TextualSerializerImpl;
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
		s = new TextualSerializerImpl();
	}
	
	@Test
	public void alternateDeserialize() {
		ArrayList<String> l1 = new ArrayList<String>();
		l1.add("1");
		l1.add("2");
		l1.add("3");
		
		try {
			System.out.println("Showing deserializtion to alternate class");
			System.out.println(l1.getClass());
			System.out.println(l1);
			
			ValueSerializerRegistry.registerDeserializingClass(ArrayList.class, Vector.class);
			ByteBuffer bb = s.outputBufferFromObject(l1);
			
			
			@SuppressWarnings("unchecked")
			Vector<String> v1 = (Vector<String>) s.objectFromInputBuffer(bb);
			System.out.println(v1.getClass());
			System.out.println(v1);
		} catch (NotSerializableException e) {
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Done deserializing to alternate class");
	}
	
	@Test
	public void testWithPrint() {
		MyObject obj = new MyObject();
		MyObject obj2 = new MyObject();
		
		obj.obj1 = obj2;
		obj2.obj1 = obj;
		
		obj.anInt = 1;
		obj2.anInt = 2;
		
		obj.objects = new ArrayList();
		obj.objects.add(10);
		obj.objects.add("Hello world");
		obj.objects.add(obj2);
		
		obj2.objects = obj.objects;
		
		obj2.maps = new HashMap();
		obj2.maps.put("key1", "value1");
		obj2.maps.put("key2", "value2");
		
		try {
			ByteBuffer textB = (new TextualSerializerImpl()).outputBufferFromObject(obj);
			byte[] bytes = new byte[textB.limit()];
			textB.get(bytes);
			String s = new String(bytes);
			System.out.println(s);
			
			ByteBuffer b = (new BinarySerializerImpl()).outputBufferFromObject(obj);
			System.out.println(b.limit());
			
			while (b.hasRemaining())
				System.out.print(b.get() + " ");
		} catch (NotSerializableException e) {
			e.printStackTrace();
		}
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
		assertSavesEquals("sa.�����");
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
		Object obj = new TestObject("test1");
		a.add(obj);
		assertSavesEquals(a);
		a.add(obj);
		assertSavesEquals(a);
		a.add(null);
		assertSavesEquals(a);
		a.add(null);
		assertSavesEquals(a);
		a.add(obj);
		assertSavesEquals(a);
	}
	
	@Test
	public void testHashMap() {
		Map<Object, Object> a = new HashMap<Object, Object>();
		assertSavesEquals(a);
		a.put("test", 1);
		assertSavesEquals(a);
		a.put(1, "test");
		assertSavesEquals(a);
		a.put(1, null);
		assertSavesEquals(a);
	}
	
	@Test
	public void testBean() {
		Object obj = new TestObject("test1", 10);
		assertSavesEquals(obj);
		
		obj = new TestObject(null, 10);
		assertSavesEquals(obj);
	}
	
	@Test
	public void testArray() {
		assertArraySavesEquals(new Object[] { 1, 2, 3, 4 });
		
		Object obj = new TestObject("test1", 10);
		assertArraySavesEquals(new Object[] { obj, 1, obj, null, obj, "test" });
		
		assertArraySavesEquals(new Object[] { "string" });
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
	
	void assertArraySavesEquals(Object[] obj) {
		Object[] out = (Object[]) apply(obj);
		Iterator i1 = Arrays.asList(obj).iterator();
		Iterator i2 = Arrays.asList(out).iterator();
		
		while (i1.hasNext() && i2.hasNext()) {
			Assert.assertEquals(i1.next(), i2.next());
		}
		
		if (i1.hasNext() || i2.hasNext())
			Assert.fail();
	}

}
