package org.espenhahn.serializer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.espenhahn.serializer.dispatchingserializer.ArraySerializerImpl;
import org.espenhahn.serializer.dispatchingserializer.BeanSerializerImpl;
import org.espenhahn.serializer.dispatchingserializer.DispatchingSerializer;
import org.espenhahn.serializer.dispatchingserializer.DispatchingSerializerImpl;
import org.espenhahn.serializer.dispatchingserializer.EnumSerializerImpl;
import org.espenhahn.serializer.dispatchingserializer.ListSerializerImpl;
import org.espenhahn.serializer.specialvalueserializers.ClassNameSerializer;
import org.espenhahn.serializer.specialvalueserializers.ClassNameSerializerImpl;
import org.espenhahn.serializer.specialvalueserializers.NullSerializerImpl;
import org.espenhahn.serializer.specialvalueserializers.SpecialValueSerializer;
import org.espenhahn.serializer.specialvalueserializers.VisitedRefSerializerImpl;
import org.espenhahn.serializer.util.VisitedObjects;
import org.espenhahn.serializer.valueserializers.BooleanSerializerImpl;
import org.espenhahn.serializer.valueserializers.CollectionSerializerImpl;
import org.espenhahn.serializer.valueserializers.DoubleSerializerImpl;
import org.espenhahn.serializer.valueserializers.FloatSerializerImpl;
import org.espenhahn.serializer.valueserializers.IntegerSerializerImpl;
import org.espenhahn.serializer.valueserializers.LongSerializerImpl;
import org.espenhahn.serializer.valueserializers.MapSerializerImpl;
import org.espenhahn.serializer.valueserializers.ShortSerializerImpl;
import org.espenhahn.serializer.valueserializers.StringSerializerImpl;
import org.espenhahn.serializer.valueserializers.ValueSerializer;

import util.annotations.Comp533Tags;
import util.annotations.Tags;
import util.misc.RemoteReflectionUtility;

@Tags({ Comp533Tags.SERIALIZER_REGISTRY })
public class ValueSerializerRegistry {
	private static ClassNameSerializer classNameSerializer = new ClassNameSerializerImpl();
	static {
		// Run HuffmanBuilder then uncomment this to enable class name compression for BinarySerialization
//		try {
//			classNameSerializer = new ClassNameSerializerImpl(new File("huffman.dat"));
//		} catch (IOException e) {
//			e.printStackTrace();
//			System.exit(4);
//		}
	}
	
	private static Map<Class<?>, ValueSerializer> serializers = new HashMap<Class<?>, ValueSerializer>();
	private static Map<Class<?>, Class<?>> deserializingClass = new HashMap<Class<?>, Class<?>>();
	
	private static DispatchingSerializer dispatchingSerializer = new DispatchingSerializerImpl();
	
	private static SpecialValueSerializer visitedRefSerializer = new VisitedRefSerializerImpl();
	private static SpecialValueSerializer nullSerializer = new NullSerializerImpl();
	
	private static ValueSerializer arraySerializer = new ArraySerializerImpl();
	private static ValueSerializer enumSerializer = new EnumSerializerImpl();
	private static ValueSerializer listSerializer = new ListSerializerImpl();
	private static ValueSerializer beanSerializer = new BeanSerializerImpl();
	
	public static void initDefault() {
		registerValueSerializer(Hashtable.class, new MapSerializerImpl());
		registerValueSerializer(HashMap.class, new MapSerializerImpl());
		
		registerValueSerializer(Vector.class, new CollectionSerializerImpl());
		registerValueSerializer(HashSet.class, new CollectionSerializerImpl());
		registerValueSerializer(ArrayList.class, new CollectionSerializerImpl());
		
		registerValueSerializer(Integer.TYPE, new IntegerSerializerImpl());
		registerValueSerializer(Integer.class, new IntegerSerializerImpl());
		registerValueSerializer(Short.TYPE, new ShortSerializerImpl());
		registerValueSerializer(Short.class, new ShortSerializerImpl());
		registerValueSerializer(Long.TYPE, new LongSerializerImpl());
		registerValueSerializer(Long.class, new LongSerializerImpl());
		registerValueSerializer(Double.TYPE, new DoubleSerializerImpl());
		registerValueSerializer(Double.class, new DoubleSerializerImpl());
		registerValueSerializer(Float.TYPE, new FloatSerializerImpl());
		registerValueSerializer(Float.class, new FloatSerializerImpl());
		registerValueSerializer(Boolean.TYPE, new BooleanSerializerImpl());
		registerValueSerializer(Boolean.class, new BooleanSerializerImpl());
		
		registerValueSerializer(String.class, new StringSerializerImpl());
		
		// Specify instance to create for interfaces
		registerDeserializingClass(List.class, ArrayList.class);
		registerDeserializingClass(Map.class, HashMap.class);
	}
	
	public static void registerDeserializingClass(Class<?> foundClass, Class<?> targetClass) {
		ValueSerializer serializer = getValueSerializer(targetClass);
		if (serializer == null) {
			serializer = getTypeIndependentSerializer(targetClass);
			if (serializer == null) throw new IllegalArgumentException();
		}
		
		deserializingClass.put(foundClass, targetClass);
		registerValueSerializer(foundClass, serializer);
	}
	
	public static Object getDeserializedInstance(Class<?> foundClass) throws InstantiationException, IllegalAccessException {
		return deserializingClass.getOrDefault(foundClass, foundClass).newInstance();
	}
	
	public static void registerValueSerializer(Class<?> clazz, ValueSerializer serializer) {
		serializers.put(clazz, serializer);
	}
	
	public static ValueSerializer getValueSerializer(Class<?> clazz) {
		ValueSerializer valueSerializer = serializers.getOrDefault(clazz, null);
		if (valueSerializer != null) return valueSerializer;
		else return null;
	}
	
	public static ValueSerializer getTypeIndependentSerializer(Class<?> clazz) {
		if (clazz.isEnum()) return getEnumSerializer();
		else if (clazz.isArray()) return getArraySerializer();
		else if (RemoteReflectionUtility.isList(clazz)) return getListSerializer();
		else return getBeanSerializer();
	}
	
	public static SpecialValueSerializer getSpecialValueSerializer(Object obj, VisitedObjects visitedObjs) {
		if (obj == null) return getNullSerializer();
		else if (visitedObjs.isVisited(obj)) return getVisitedRefSerializer();
		else return null;
	}
	
	public static SpecialValueSerializer getSpecialValueSerializer(String className) {
		if (className.equals(getNullSerializer().getClassName()))
			return getNullSerializer();
		else if (className.equals(getVisitedRefSerializer().getClassName()))
			return getVisitedRefSerializer();
		else
			return null;
	}
	
	public static SpecialValueSerializer getVisitedRefSerializer() {
		return visitedRefSerializer;
	}
	
	public static void registerVisitedRefSerializer(SpecialValueSerializer vs) {
		if (vs == null) throw new IllegalArgumentException();
		visitedRefSerializer = vs;
	}
	
	public static DispatchingSerializer getDispatchingSerializer() {
		return dispatchingSerializer;
	}
	
	public static void registerDispatchingSerializer(DispatchingSerializer vs) {
		if (vs == null) throw new IllegalArgumentException();
		dispatchingSerializer = vs;
	}
	
	public static SpecialValueSerializer getNullSerializer() { 
		return nullSerializer;
	}
	
	public static void registerNullSerializer(SpecialValueSerializer n) {
		if (n == null) throw new IllegalArgumentException();
		nullSerializer = n;
	}
	
	public static ClassNameSerializer getClassNameSerializer() { 
		return classNameSerializer;
	}
	
	public static void registerClassNameSerializer(ClassNameSerializer w) {
		if (w == null) throw new IllegalArgumentException();
		classNameSerializer = w;
	}
	
	public static ValueSerializer getEnumSerializer() { 
		return enumSerializer;
	}
	
	public static void registerEnumSerializer(ValueSerializer w) {
		if (w == null) throw new IllegalArgumentException();
		enumSerializer = w;
	}
	
	public static ValueSerializer getArraySerializer() { 
		return arraySerializer;
	}
	
	public static void registerArraySerializer(ValueSerializer w) {
		if (w == null) throw new IllegalArgumentException();
		arraySerializer = w;
	}
	
	public static ValueSerializer getListSerializer() { 
		return listSerializer;
	}
	
	public static void registerListSerializer(ValueSerializer w) {
		if (w == null) throw new IllegalArgumentException();
		listSerializer = w;
	}
	
	public static ValueSerializer getBeanSerializer() { 
		return beanSerializer;
	}
	
	public static void registerBeanSerializer(ValueSerializer w) {
		if (w == null) throw new IllegalArgumentException();
		beanSerializer = w;
	}
	
}
