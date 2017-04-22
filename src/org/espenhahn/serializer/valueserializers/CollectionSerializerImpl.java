package org.espenhahn.serializer.valueserializers;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.util.Collection;
import java.util.Iterator;

import org.espenhahn.serializer.ValueSerializerRegistry;
import org.espenhahn.serializer.dispatchingserializer.DispatchingSerializer;
import org.espenhahn.serializer.util.RetrievedObjects;
import org.espenhahn.serializer.util.VisitedObjects;

import port.trace.serialization.extensible.ExtensibleBufferDeserializationFinished;
import port.trace.serialization.extensible.ExtensibleBufferDeserializationInitiated;
import port.trace.serialization.extensible.ExtensibleValueSerializationFinished;
import port.trace.serialization.extensible.ExtensibleValueSerializationInitiated;
import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({ Comp533Tags.VALUE_SERIALIZER })
public class CollectionSerializerImpl implements ValueSerializer {

	@Override
	public boolean isTerminal() {
		return false;
	}
	
	@Override
	public void objectToBuffer(Object out, Object obj, VisitedObjects visitedObjs) throws NotSerializableException {
		if (!(obj instanceof Collection)) throw new IllegalArgumentException("Expected Collection, got " + obj);
		ExtensibleValueSerializationInitiated.newCase(this, obj, out);
		
		Collection<?> collection = (Collection<?>) obj;
		visitedObjs.visit(collection);
		
		ValueSerializerRegistry.getValueSerializer(Integer.class)
			.objectToBuffer(out, collection.size(), visitedObjs);
		
		DispatchingSerializer dispatcher = ValueSerializerRegistry.getDispatchingSerializer();
		Iterator<?> it = collection.iterator();
		while (it.hasNext()) {
			Object component = it.next();
			dispatcher.objectToBuffer(out, component, visitedObjs);
		}
		
		ExtensibleValueSerializationFinished.newCase(this, obj, out, visitedObjs);		
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T objectFromBuffer(Object in, Class<T> clazz, RetrievedObjects retrievedObjs) throws StreamCorruptedException {
		if (!Collection.class.isAssignableFrom(clazz)) throw new IllegalArgumentException("Expected Collection, got " + clazz);
		ExtensibleBufferDeserializationInitiated.newCase(this, null, in, clazz);
		
		try {
			int length = ValueSerializerRegistry.getValueSerializer(Integer.class)
					.objectFromBuffer(in, Integer.class, retrievedObjs);
			
			T t = (T) ValueSerializerRegistry.getDeserializedInstance(clazz);
			retrievedObjs.save(t);
			
			DispatchingSerializer dispatcher = ValueSerializerRegistry.getDispatchingSerializer();
			Collection<Object> collection = (Collection<Object>) t;
			for (int i = 0; i < length; i++)
				collection.add(dispatcher.objectFromBuffer(in, retrievedObjs));
			
			ExtensibleBufferDeserializationFinished.newCase(this, "", in, collection, retrievedObjs);
			return t;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new StreamCorruptedException();
		}
	}

}
