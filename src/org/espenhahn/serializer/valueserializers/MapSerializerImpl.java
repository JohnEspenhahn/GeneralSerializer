package org.espenhahn.serializer.valueserializers;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
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
public class MapSerializerImpl implements ValueSerializer {

	@Override
	public boolean isTerminal() {
		return false;
	}
	
	@Override
	public void objectToBuffer(Object out, Object obj, VisitedObjects visitedObjs) throws NotSerializableException {
		if (!(obj instanceof Map)) throw new IllegalArgumentException("Expected Map, got " + obj);
		Map<?,?> map = (Map<?,?>) obj;
		
		visitedObjs.visit(map);
		ValueSerializerRegistry.getValueSerializer(Integer.class)
			.objectToBuffer(out, map.size(), visitedObjs);
		
		DispatchingSerializer dispatcher = ValueSerializerRegistry.getDispatchingSerializer();
		Iterator<?> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<?,?> entry = (Entry<?,?>) it.next();
			dispatcher.objectToBuffer(out, entry.getKey(), visitedObjs);
			dispatcher.objectToBuffer(out, entry.getValue(), visitedObjs);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T objectFromBuffer(Object in, Class<T> clazz, RetrievedObjects retrievedObjs) throws StreamCorruptedException {		
		if (!Map.class.isAssignableFrom(clazz)) throw new IllegalArgumentException("Expected Map, got " + clazz);
		
		try {
			T t = (T) ValueSerializerRegistry.getDeserializedInstance(clazz);
			retrievedObjs.save(t);
			
			int length = ValueSerializerRegistry.getValueSerializer(Integer.class)
					.objectFromBuffer(in, Integer.class, retrievedObjs);
			
			DispatchingSerializer dispatcher = ValueSerializerRegistry.getDispatchingSerializer();
			Map<Object,Object> map = (Map<Object,Object>) t;
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
