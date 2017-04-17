package org.espenhahn.serializer.dispatchingserializer;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.lang.reflect.Array;

import org.espenhahn.serializer.ValueSerializerRegistry;
import org.espenhahn.serializer.util.RetrievedObjects;
import org.espenhahn.serializer.util.VisitedObjects;
import org.espenhahn.serializer.valueserializers.ValueSerializer;

import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({ Comp533Tags.VALUE_SERIALIZER })
public class ArraySerializerImpl implements ValueSerializer {

	@Override
	public boolean isTerminal() {
		return false;
	}
	
	@Override
	public void objectToBuffer(Object out, Object obj, VisitedObjects visitedObjs) throws NotSerializableException {
		if (!obj.getClass().isArray()) throw new IllegalArgumentException("Expected Array, got " + obj);
		
		visitedObjs.visit(obj);
		ValueSerializerRegistry.getValueSerializer(Integer.class)
			.objectToBuffer(out, Array.getLength(obj), visitedObjs);
		
		DispatchingSerializer dispatcher = ValueSerializerRegistry.getDispatchingSerializer();
		
		int lng = Array.getLength(obj);
		for (int i = 0; i < lng; i++) {
			Object component = Array.get(obj, i);
			dispatcher.objectToBuffer(out, component, visitedObjs);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T objectFromBuffer(Object in, Class<T> clazz, RetrievedObjects retrievedObjs) throws StreamCorruptedException {
		if (!clazz.isArray()) throw new IllegalArgumentException("Expected Array, got " + clazz);		
		DispatchingSerializer dispatcher = ValueSerializerRegistry.getDispatchingSerializer();
		
		int length = ValueSerializerRegistry.getValueSerializer(Integer.class)
				.objectFromBuffer(in, Integer.class, retrievedObjs);
		
		Object arr = Array.newInstance(clazz.getComponentType(), length);
		retrievedObjs.save(arr);
		
		// Load components
		for (int i = 0; i < length; i++)
			Array.set(arr, i, dispatcher.objectFromBuffer(in, retrievedObjs));
		
		return (T) arr;
	}

}
