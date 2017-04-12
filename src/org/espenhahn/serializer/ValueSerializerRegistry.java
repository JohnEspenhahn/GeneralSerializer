package org.espenhahn.serializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import org.espenhahn.serializer.dispatchingserializer.ArraySerializerImpl;
import org.espenhahn.serializer.dispatchingserializer.DispatchingSerializer;
import org.espenhahn.serializer.dispatchingserializer.DispatchingSerializerImpl;
import org.espenhahn.serializer.dispatchingserializer.EnumSerializerImpl;
import org.espenhahn.serializer.util.ClassNameSerializer;
import org.espenhahn.serializer.util.ClassNameSerializerImpl;
import org.espenhahn.serializer.valueserializers.CollectionSerializerImpl;
import org.espenhahn.serializer.valueserializers.IntegerSerializerImpl;
import org.espenhahn.serializer.valueserializers.MapSerializerImpl;
import org.espenhahn.serializer.valueserializers.NullSerializer;
import org.espenhahn.serializer.valueserializers.NullSerializerImpl;
import org.espenhahn.serializer.valueserializers.ShortSerializerImpl;
import org.espenhahn.serializer.valueserializers.ValueSerializer;
import org.espenhahn.serializer.valueserializers.VisitedRefSerializer;
import org.espenhahn.serializer.valueserializers.VisitedRefSerializerImpl;

import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({ Comp533Tags.SERIALIZER_REGISTRY })
public class ValueSerializerRegistry {
	private static ClassNameSerializer classNameSerializer = new ClassNameSerializerImpl();
	
	private static Map<Class<?>, ValueSerializer> serializers = new HashMap<Class<?>, ValueSerializer>();
	private static VisitedRefSerializer visitedRefSerializer = new VisitedRefSerializerImpl();
	private static DispatchingSerializer dispatchingSerializer = new DispatchingSerializerImpl();
	private static ValueSerializer arraySerializer = new ArraySerializerImpl();
	private static ValueSerializer enumSerializer = new EnumSerializerImpl();
	private static NullSerializer nullSerializer = new NullSerializerImpl();
	
	public static void initDefault() {
		registerValueSerializer(Hashtable.class, new MapSerializerImpl());
		registerValueSerializer(HashMap.class, new MapSerializerImpl());
		
		registerValueSerializer(Vector.class, new CollectionSerializerImpl());
		registerValueSerializer(HashSet.class, new CollectionSerializerImpl());
		registerValueSerializer(ArrayList.class, new CollectionSerializerImpl());
		
		registerValueSerializer(Integer.class, new IntegerSerializerImpl());
		registerValueSerializer(Short.class, new ShortSerializerImpl());
	}
	
	public static void registerValueSerializer(Class<?> clazz, ValueSerializer serializer) {
		serializers.put(clazz, serializer);
	}
	
	public static ValueSerializer getValueSerializer(Class<?> clazz) {
		return serializers.getOrDefault(clazz, null);
	}
	
	public static VisitedRefSerializer getVisitedRefSerializer() {
		return visitedRefSerializer;
	}
	
	public static void registerVisitedRefSerializer(VisitedRefSerializer vs) {
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
	
	public static NullSerializer getNullSerializer() { 
		return nullSerializer;
	}
	
	public static void registerNullSerializer(NullSerializer n) {
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
	
}
