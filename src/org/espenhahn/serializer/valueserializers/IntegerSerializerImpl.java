package org.espenhahn.serializer.valueserializers;

import java.nio.ByteBuffer;

import org.espenhahn.serializer.util.RetrievedObjects;
import org.espenhahn.serializer.util.VisitedObjects;

import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({ Comp533Tags.VALUE_SERIALIZER })
public class IntegerSerializerImpl extends AValueSerializer {

	@Override
	protected void objectToStringBuffer(StringBuffer out, Object obj, VisitedObjects visitedObjs) {
		out.append((int) obj);
	}

	@Override
	protected void objectToByteBuffer(ByteBuffer out, Object obj, VisitedObjects visitedObjs) {
		out.putInt((int) obj);
	}

	@Override
	protected <T> T objectFromStringBuffer(StringBuffer in, Class<T> clazz, RetrievedObjects retrevedObjs) {
		// TODO
		throw new UnsupportedOperationException();
	}

	@Override
	@SuppressWarnings("unchecked")
	protected <T> T objectFromByteBuffer(ByteBuffer in, Class<T> clazz, RetrievedObjects retrevedObjs) {
		return (T) (Integer) in.getInt();
	}

}
