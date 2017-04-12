package org.espenhahn.serializer.valueserializers;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;

import org.espenhahn.serializer.util.RetrievedObjects;
import org.espenhahn.serializer.util.VisitedObjects;

import port.trace.serialization.extensible.ExtensibleBufferDeserializationFinished;
import port.trace.serialization.extensible.ExtensibleBufferDeserializationInitiated;
import port.trace.serialization.extensible.ExtensibleValueSerializationFinished;
import port.trace.serialization.extensible.ExtensibleValueSerializationInitiated;
import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({ Comp533Tags.VALUE_SERIALIZER })
public abstract class AValueSerializer implements ValueSerializer {
	public void objectToBuffer(Object out, Object obj, VisitedObjects visitedObjs) throws NotSerializableException {
		ExtensibleValueSerializationInitiated.newCase(this, obj, out);
		if (out instanceof StringBuffer) {
			objectToStringBuffer((StringBuffer) out, obj, visitedObjs);
		} else if (out instanceof ByteBuffer) {
			objectToByteBuffer((ByteBuffer) out, obj, visitedObjs);
		} else {
			throw new IllegalArgumentException("Invalid output buffer provided");
		}
		ExtensibleValueSerializationFinished.newCase(this, obj, out, visitedObjs);
	}

	protected abstract void objectToStringBuffer(StringBuffer out, Object obj, VisitedObjects visitedObjs)
			throws NotSerializableException;
	
	protected abstract void objectToByteBuffer(ByteBuffer out, Object obj, VisitedObjects visitedObjs)
			throws NotSerializableException;

	@SuppressWarnings("unchecked")
	public <T> T objectFromBuffer(Object in, Class<T> clazz, RetrievedObjects retrievedObjs)
			throws StreamCorruptedException, NotSerializableException {
		Object obj;
		ExtensibleBufferDeserializationInitiated.newCase(this, "", in, clazz);
		if (in instanceof StringBuffer) {
			obj = objectFromStringBuffer((StringBuffer) in, clazz, retrievedObjs);
		} else if (in instanceof ByteBuffer) {
			obj = objectFromByteBuffer((ByteBuffer) in, clazz, retrievedObjs);
		} else {
			throw new IllegalArgumentException("Invalid input buffer provided");
		}
		ExtensibleBufferDeserializationFinished.newCase(this, "", in, obj, retrievedObjs);
		return (T) obj;
	}
	
	protected abstract <T> T objectFromStringBuffer(StringBuffer in, Class<T> clazz, RetrievedObjects retrievedObjs)
			throws StreamCorruptedException, NotSerializableException;
	
	protected abstract <T> T objectFromByteBuffer(ByteBuffer in, Class<T> clazz, RetrievedObjects retrievedObjs)
			throws StreamCorruptedException, NotSerializableException;
}
