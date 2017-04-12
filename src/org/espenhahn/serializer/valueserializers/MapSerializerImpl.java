package org.espenhahn.serializer.valueserializers;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.espenhahn.serializer.ValueSerializerRegistry;
import org.espenhahn.serializer.dispatchingserializer.DispatchingSerializer;
import org.espenhahn.serializer.util.RetrievedObjects;
import org.espenhahn.serializer.util.VisitedObjects;

import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({ Comp533Tags.VALUE_SERIALIZER })
public class MapSerializerImpl extends AValueSerializer {

	@Override
	protected void objectToStringBuffer(StringBuffer out, Object obj, VisitedObjects visitedObjs)
			throws NotSerializableException {
		if (!(obj instanceof Map)) throw new IllegalArgumentException("Expected Map, got " + obj);
		Map<?,?> map = (Map<?,?>) obj;

		visitedObjs.visit(map);
		out.append(map.size());
		emitBody(out, map, visitedObjs);
	}

	@Override
	protected void objectToByteBuffer(ByteBuffer out, Object obj, VisitedObjects visitedObjs)
			throws NotSerializableException {
		if (!(obj instanceof Map)) throw new IllegalArgumentException("Expected Map, got " + obj); 
		Map<?,?> map = (Map<?,?>) obj;
		
		visitedObjs.visit(map);
		out.putInt(map.size());
		emitBody(out, map, visitedObjs);
	}
	
	private void emitBody(Object out, Map<?,?> map, VisitedObjects visitedObjs) throws NotSerializableException {		
		DispatchingSerializer dispatcher = ValueSerializerRegistry.getDispatchingSerializer();
		
		Iterator<?> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<?,?> entry = (Entry<?,?>) it.next();
			dispatcher.objectToBuffer(out, entry.getKey(), visitedObjs);
			dispatcher.objectToBuffer(out, entry.getValue(), visitedObjs);
		}
	}

	@Override
	protected <T> T objectFromStringBuffer(StringBuffer in, Class<T> clazz, RetrievedObjects retrievedObjs)
			throws StreamCorruptedException {
		if (!Map.class.isAssignableFrom(clazz)) throw new IllegalArgumentException("Expected Map, got " + clazz);
		
		try {
			Object obj = clazz.newInstance();
			retrievedObjs.save(obj);
			
			// TODO
			throw new UnsupportedOperationException();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new StreamCorruptedException();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	protected <T> T objectFromByteBuffer(ByteBuffer in, Class<T> clazz, RetrievedObjects retrievedObjs)
			throws StreamCorruptedException {		
		if (!Map.class.isAssignableFrom(clazz)) throw new IllegalArgumentException("Expected Map, got " + clazz);
		DispatchingSerializer dispatcher = ValueSerializerRegistry.getDispatchingSerializer();
		
		try {
			T t = (T) ValueSerializerRegistry.getDeserializedInstance(clazz);
			retrievedObjs.save(t);
			
			Map<Object,Object> map = (Map<Object,Object>) t;
			
			int length = in.getInt();
			for (int i = 0; i < length; i++) {
				Object key = dispatcher.objectFromBuffer(in, retrievedObjs);
				Object value = dispatcher.objectFromBuffer(in, retrievedObjs);
				map.put(key, value);
			}
			
			return t;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new StreamCorruptedException();
		}
	}

}
