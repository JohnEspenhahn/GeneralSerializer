package org.espenhahn.serializer.specialvalueserializers;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;

import org.espenhahn.serializer.ValueSerializerRegistry;
import org.espenhahn.serializer.util.RetrievedObjects;
import org.espenhahn.serializer.util.VisitedObjects;

public class VisitedRefSerializerImpl implements SpecialValueSerializer {
	
	@Override
	public boolean isTerminal() {
		return false;
	}
	
	@Override
	public String getClassName() {
		return ".vr";
	}
	
	@Override
	public void objectToBuffer(Object out, Object obj, VisitedObjects visitedObjs) throws NotSerializableException {		
		int idx = visitedObjs.getIndex(obj);
		if (idx == -1) throw new IllegalArgumentException("Not actually visited");
		
		ValueSerializerRegistry.getValueSerializer(Integer.class)
			.objectToBuffer(out, idx, visitedObjs);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T objectFromBuffer(Object in, Class<T> clazz, RetrievedObjects retrievedObjs) throws StreamCorruptedException {		
		// Class name will be read in DispatchingSerializer
		int offset = ValueSerializerRegistry.getValueSerializer(Integer.class).objectFromBuffer(in, Integer.class, retrievedObjs);
		return (T) retrievedObjs.get(offset);
	}

}
