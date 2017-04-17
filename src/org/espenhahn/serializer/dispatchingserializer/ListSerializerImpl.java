package org.espenhahn.serializer.dispatchingserializer;

import java.io.NotSerializableException;
import java.io.Serializable;
import java.io.StreamCorruptedException;

import org.espenhahn.serializer.ValueSerializerRegistry;
import org.espenhahn.serializer.util.RetrievedObjects;
import org.espenhahn.serializer.util.VisitedObjects;
import org.espenhahn.serializer.valueserializers.ValueSerializer;

import util.annotations.Comp533Tags;
import util.annotations.Tags;
import util.misc.RemoteReflectionUtility;

@Tags({ Comp533Tags.LIST_PATTERN_SERIALIZER })
public class ListSerializerImpl implements ValueSerializer {

	@Override
	public boolean isTerminal() {
		return false;
	}
	
	@Override
	public void objectToBuffer(Object out, Object obj, VisitedObjects visitedObjs) throws NotSerializableException {
		if (!RemoteReflectionUtility.isList(obj.getClass())) throw new IllegalArgumentException("Expected List, got " + obj);
		else if (!(obj instanceof Serializable)) throw new NotSerializableException();
		
		visitedObjs.visit(obj);
		ValueSerializerRegistry.getValueSerializer(Integer.class)
				.objectToBuffer(out, RemoteReflectionUtility.listSize(obj), visitedObjs);
		
		DispatchingSerializer dispatcher = ValueSerializerRegistry.getDispatchingSerializer();
		
		int lng = RemoteReflectionUtility.listSize(obj);
		for (int i = 0; i < lng; i++) {
			Object component = RemoteReflectionUtility.listGet(obj, i);
			dispatcher.objectToBuffer(out, component, visitedObjs);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T objectFromBuffer(Object in, Class<T> clazz, RetrievedObjects retrievedObjs) throws StreamCorruptedException {
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
		int length = ValueSerializerRegistry.getValueSerializer(Integer.class).objectFromBuffer(in, Integer.class, retrievedObjs);
		for (int i = 0; i < length; i++)
			RemoteReflectionUtility.listAdd(list, dispatcher.objectFromBuffer(in, retrievedObjs));
		
		return (T) list;
	}

}
