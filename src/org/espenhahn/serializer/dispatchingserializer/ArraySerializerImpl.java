package org.espenhahn.serializer.dispatchingserializer;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;

import org.espenhahn.serializer.ValueSerializerRegistry;
import org.espenhahn.serializer.util.RetrievedObjects;
import org.espenhahn.serializer.util.VisitedObjects;
import org.espenhahn.serializer.valueserializers.AValueSerializer;

import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({ Comp533Tags.VALUE_SERIALIZER })
public class ArraySerializerImpl extends AValueSerializer {

	@Override
	protected void objectToStringBuffer(StringBuffer out, Object obj, VisitedObjects visitedObjs)
			throws NotSerializableException {
		if (!obj.getClass().isArray()) throw new IllegalArgumentException("Expected Array, got " + obj);
		
		visitedObjs.visit(obj);
		out.append(Array.getLength(obj));
		emitBody(out, obj, visitedObjs);
	}

	@Override
	protected void objectToByteBuffer(ByteBuffer out, Object obj, VisitedObjects visitedObjs)
			throws NotSerializableException {
		if (!obj.getClass().isArray()) throw new IllegalArgumentException("Expected Array, got " + obj); 
		
		visitedObjs.visit(obj);
		out.putInt(Array.getLength(obj));
		emitBody(out, obj, visitedObjs);
	}
	
	private void emitBody(Object out, Object arr, VisitedObjects visitedObjs) throws NotSerializableException {		
		DispatchingSerializer dispatcher = ValueSerializerRegistry.getDispatchingSerializer();
		
		int lng = Array.getLength(arr);
		for (int i = 0; i < lng; i++) {
			Object component = Array.get(arr, i);
			dispatcher.objectToBuffer(out, component, visitedObjs);
		}
	}

	@Override
	protected <T> T objectFromStringBuffer(StringBuffer in, Class<T> clazz, RetrievedObjects retrievedObjs)
			throws StreamCorruptedException, NotSerializableException {
		if (!clazz.isArray()) throw new IllegalArgumentException("Expected Array, got " + clazz);
		
		// TODO
		throw new UnsupportedOperationException();
		
	}

	@Override
	@SuppressWarnings("unchecked")
	protected <T> T objectFromByteBuffer(ByteBuffer in, Class<T> clazz, RetrievedObjects retrievedObjs)
			throws StreamCorruptedException, NotSerializableException {
		if (!clazz.isArray()) throw new IllegalArgumentException("Expected Array, got " + clazz);
		
		DispatchingSerializer dispatcher = ValueSerializerRegistry.getDispatchingSerializer();
		
		int length = in.getInt();
		Object arr = Array.newInstance(clazz.getComponentType(), length);
		retrievedObjs.save(arr);
		
		// Load components
		for (int i = 0; i < length; i++)
			Array.set(arr, i, dispatcher.objectFromBuffer(in, retrievedObjs));
		
		return (T) arr;
	}

}
