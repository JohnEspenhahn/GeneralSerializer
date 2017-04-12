package org.espenhahn.serializer.dispatchingserializer;

import java.io.NotSerializableException;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;

import org.espenhahn.serializer.ValueSerializerRegistry;
import org.espenhahn.serializer.util.RetrievedObjects;
import org.espenhahn.serializer.util.VisitedObjects;
import org.espenhahn.serializer.valueserializers.AValueSerializer;

import util.annotations.Comp533Tags;
import util.annotations.Tags;
import util.misc.RemoteReflectionUtility;

@Tags({ Comp533Tags.LIST_PATTERN_SERIALIZER })
public class ListSerializerImpl extends AValueSerializer {

	@Override
	protected void objectToStringBuffer(StringBuffer out, Object obj, VisitedObjects visitedObjs)
			throws NotSerializableException {
		if (!RemoteReflectionUtility.isList(obj.getClass())) throw new IllegalArgumentException("Expected List, got " + obj);
		else if (!(obj instanceof Serializable)) throw new NotSerializableException();
		
		visitedObjs.visit(obj);
		out.append(RemoteReflectionUtility.listSize(obj));
		emitBody(out, obj, visitedObjs);
	}

	@Override
	protected void objectToByteBuffer(ByteBuffer out, Object obj, VisitedObjects visitedObjs)
			throws NotSerializableException {
		if (!RemoteReflectionUtility.isList(obj.getClass())) throw new IllegalArgumentException("Expected List, got " + obj);
		else if (!(obj instanceof Serializable)) throw new NotSerializableException();
		
		visitedObjs.visit(obj);
		out.putInt(RemoteReflectionUtility.listSize(obj));
		emitBody(out, obj, visitedObjs);
	}
	
	private void emitBody(Object out, Object list, VisitedObjects visitedObjs) throws NotSerializableException {		
		DispatchingSerializer dispatcher = ValueSerializerRegistry.getDispatchingSerializer();
		
		int lng = RemoteReflectionUtility.listSize(list);
		for (int i = 0; i < lng; i++) {
			Object component = RemoteReflectionUtility.listGet(list, i);
			dispatcher.objectToBuffer(out, component, visitedObjs);
		}
	}

	@Override
	protected <T> T objectFromStringBuffer(StringBuffer in, Class<T> clazz, RetrievedObjects retrievedObjs)
			throws StreamCorruptedException {
		if (!RemoteReflectionUtility.isList(clazz)) throw new IllegalArgumentException("Expected List, got " + clazz);
		
		// TODO
		throw new UnsupportedOperationException();
		
	}

	@Override
	@SuppressWarnings("unchecked")
	protected <T> T objectFromByteBuffer(ByteBuffer in, Class<T> clazz, RetrievedObjects retrievedObjs) throws StreamCorruptedException {
		if (!RemoteReflectionUtility.isList(clazz)) throw new IllegalArgumentException("Expected List, got " + clazz);		
		DispatchingSerializer dispatcher = ValueSerializerRegistry.getDispatchingSerializer();
		
		// Create instance
		Object list;
		try {
			list = ValueSerializerRegistry.getDeserializedInstance(clazz);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new StreamCorruptedException();
		}
		retrievedObjs.save(list);
		
		// Load components
		int length = in.getInt();
		for (int i = 0; i < length; i++)
			RemoteReflectionUtility.listAdd(list, dispatcher.objectFromBuffer(in, retrievedObjs));
		
		return (T) list;
	}

}
