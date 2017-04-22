package org.espenhahn.serializer.specialvalueserializers;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;

import org.espenhahn.serializer.ValueSerializerRegistry;
import org.espenhahn.serializer.util.RetrievedObjects;
import org.espenhahn.serializer.util.VisitedObjects;

import port.trace.serialization.extensible.ExtensibleBufferDeserializationFinished;
import port.trace.serialization.extensible.ExtensibleBufferDeserializationInitiated;
import port.trace.serialization.extensible.ExtensibleValueSerializationFinished;
import port.trace.serialization.extensible.ExtensibleValueSerializationInitiated;
import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({ Comp533Tags.VALUE_SERIALIZER })
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
		ExtensibleValueSerializationInitiated.newCase(this, obj, out);
		
		ValueSerializerRegistry.getValueSerializer(Integer.class)
			.objectToBuffer(out, idx, visitedObjs);
		
		ExtensibleValueSerializationFinished.newCase(this, obj, out, visitedObjs);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T objectFromBuffer(Object in, Class<T> clazz, RetrievedObjects retrievedObjs) throws StreamCorruptedException {
		ExtensibleBufferDeserializationInitiated.newCase(this, "", in, clazz);
		
		// Class name will be read in DispatchingSerializer
		int offset = ValueSerializerRegistry.getValueSerializer(Integer.class).objectFromBuffer(in, Integer.class, retrievedObjs);
		T res = (T) retrievedObjs.get(offset);
		
		ExtensibleBufferDeserializationFinished.newCase(this, "", in, res, retrievedObjs);
		return res;
	}

}
