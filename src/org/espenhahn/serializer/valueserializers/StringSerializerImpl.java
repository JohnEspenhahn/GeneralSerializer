package org.espenhahn.serializer.valueserializers;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.io.StringReader;
import java.nio.ByteBuffer;

import org.espenhahn.serializer.util.RetrievedObjects;
import org.espenhahn.serializer.util.StaticStringSerializer;
import org.espenhahn.serializer.util.VisitedObjects;

import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({ Comp533Tags.VALUE_SERIALIZER })
public class StringSerializerImpl extends AValueSerializer {

	public StringSerializerImpl() {
		super(false);
	}
	
	@Override
	protected void objectToStringBuffer(StringBuffer out, Object obj, VisitedObjects visitedObjs) throws NotSerializableException {
		if (!(obj instanceof String)) throw new IllegalArgumentException("Expected String, got " + obj);
		StaticStringSerializer.writeString(out, (String) obj);
	}

	@Override
	protected void objectToByteBuffer(ByteBuffer out, Object obj, VisitedObjects visitedObjs) throws NotSerializableException {
		if (!(obj instanceof String)) throw new IllegalArgumentException("Expected String, got " + obj);
		StaticStringSerializer.writeString(out, (String) obj);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T> T objectFromStringReader(StringReader in, Class<T> clazz, RetrievedObjects retrievedObjs) throws StreamCorruptedException {
		if (!clazz.isAssignableFrom(String.class)) throw new IllegalArgumentException("Expected String, got " + clazz);
		
		try {
			return (T) StaticStringSerializer.readString(in);
		} catch (IOException e) {
			throw new StreamCorruptedException();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	protected <T> T objectFromByteBuffer(ByteBuffer in, Class<T> clazz, RetrievedObjects retrievedObjs) throws StreamCorruptedException {		
		if (!clazz.isAssignableFrom(String.class)) throw new IllegalArgumentException("Expected String, got " + clazz);
		return (T) StaticStringSerializer.readString(in);
	}

}
