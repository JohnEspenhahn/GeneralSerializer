package org.espenhahn.serializer;

import java.util.HashMap;
import java.util.Map;

import org.espenhahn.serializer.dispatchingserializer.DispatchingSerializer;
import org.espenhahn.serializer.dispatchingserializer.DispatchingSerializerImpl;
import org.espenhahn.serializer.util.ClassNameSerializer;
import org.espenhahn.serializer.util.ClassNameSerializerImpl;
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
	
	public static ClassNameSerializer getClassNameSerializer() { 
		return classNameSerializer;
	}
	
	public static void registerClassNameSerializer(ClassNameSerializer w) {
		classNameSerializer = w;
	}
	
}
