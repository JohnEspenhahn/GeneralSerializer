package org.espenhahn.serializer.valueserializers;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Iterator;

import org.espenhahn.serializer.ValueSerializerRegistry;
import org.espenhahn.serializer.dispatchingserializer.DispatchingSerializer;
import org.espenhahn.serializer.util.RetrievedObjects;
import org.espenhahn.serializer.util.VisitedObjects;

import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({ Comp533Tags.VALUE_SERIALIZER })
public class CollectionSerializerImpl extends AValueSerializer {

	@Override
	protected void objectToStringBuffer(StringBuffer out, Object obj, VisitedObjects visitedObjs)
			throws NotSerializableException {
		if (!(obj instanceof Collection)) throw new IllegalArgumentException("Expected Collection, got " + obj);
		Collection<?> collection = (Collection<?>) obj;

		visitedObjs.visit(collection);
		out.append(collection.size());
		emitBody(out, collection, visitedObjs);
	}

	@Override
	protected void objectToByteBuffer(ByteBuffer out, Object obj, VisitedObjects visitedObjs)
			throws NotSerializableException {
		if (!(obj instanceof Collection)) throw new IllegalArgumentException("Expected Collection, got " + obj); 
		Collection<?> collection = (Collection<?>) obj;
		
		visitedObjs.visit(collection);
		out.putInt(collection.size());
		emitBody(out, collection, visitedObjs);
	}
	
	private void emitBody(Object out, Collection<?> collection, VisitedObjects visitedObjs) throws NotSerializableException {		
		DispatchingSerializer dispatcher = ValueSerializerRegistry.getDispatchingSerializer();
		
		Iterator<?> it = collection.iterator();
		while (it.hasNext()) {
			Object component = it.next();
			dispatcher.objectToBuffer(out, component, visitedObjs);
		}
	}

	@Override
	protected <T> T objectFromStringBuffer(StringBuffer in, Class<T> clazz, RetrievedObjects retrievedObjs) throws StreamCorruptedException {
		if (!clazz.isAssignableFrom(Collection.class)) throw new IllegalArgumentException("Expected Collection, got " + clazz);
		
		try {
			Object obj = clazz.newInstance();
			retrievedObjs.save(obj);
			
			// TODO
			throw new UnsupportedOperationException();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected <T> T objectFromByteBuffer(ByteBuffer in, Class<T> clazz, RetrievedObjects retrievedObjs) throws StreamCorruptedException {
		if (!clazz.isAssignableFrom(Collection.class)) throw new IllegalArgumentException("Expected Collection, got " + clazz);
		DispatchingSerializer dispatcher = ValueSerializerRegistry.getDispatchingSerializer();
		
		try {
			T t = (T) ValueSerializerRegistry.getDeserializedInstance(clazz);
			retrievedObjs.save(t);
			
			int length = in.getInt();
			for (int i = 0; i < length; i++) {
				((Collection<Object>) t).add(dispatcher.objectFromBuffer(in, retrievedObjs));
			}
			
			return t;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
