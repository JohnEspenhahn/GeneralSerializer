package org.espenhahn.serializer.specialvalueserializers;

import java.nio.ByteBuffer;

import org.espenhahn.serializer.util.RetrievedObjects;
import org.espenhahn.serializer.util.VisitedObjects;
import org.espenhahn.serializer.valueserializers.AValueSerializer;

public class VisitedRefSerializerImpl extends AValueSerializer implements SpecialValueSerializer {
	
	@Override
	public String getClassName() {
		return ".vr";
	}
	
	@Override
	protected void objectToStringBuffer(StringBuffer out, Object obj, VisitedObjects visitedObjs) {
		
		int idx = visitedObjs.getIndex(obj);
		if (idx == -1) throw new IllegalArgumentException("Not actually visited");
		
		out.append(idx);
	}

	@Override
	protected void objectToByteBuffer(ByteBuffer out, Object obj, VisitedObjects visitedObjs) {
		
		int idx = visitedObjs.getIndex(obj);
		if (idx == -1) throw new IllegalArgumentException("Not actually visited");
		
		out.putInt(idx);
	}

	@Override
	protected <T> T objectFromStringBuffer(StringBuffer in, Class<T> clazz, RetrievedObjects retrievedObjs) {
		// TODO
		throw new UnsupportedOperationException();
	}

	@Override
	@SuppressWarnings("unchecked")
	protected <T> T objectFromByteBuffer(ByteBuffer in, Class<T> clazz, RetrievedObjects retrievedObjs) {		
		// Class name will be read in DispatchingSerializer
		int offset = in.getInt();
		return (T) retrievedObjs.get(offset);
	}

}
