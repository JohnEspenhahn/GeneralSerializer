package org.espenhahn.serializer.dispatchingserializer;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.io.StringReader;
import java.nio.ByteBuffer;

import org.espenhahn.serializer.specialvalueserializers.StaticStringSerializer;
import org.espenhahn.serializer.util.RetrievedObjects;
import org.espenhahn.serializer.util.VisitedObjects;
import org.espenhahn.serializer.valueserializers.AValueSerializer;

import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({ Comp533Tags.ENUM_SERIALIZER })
public class EnumSerializerImpl extends AValueSerializer {

	public EnumSerializerImpl() {
		super(true);
	}

	@Override
	protected void objectToStringBuffer(StringBuffer out, Object obj, VisitedObjects visitedObjs) {
		if (!obj.getClass().isEnum()) throw new IllegalArgumentException("Expected Enum, got " + obj);
		
		StaticStringSerializer.writeString(out, obj.toString(), DELIM);
	}

	@Override
	protected void objectToByteBuffer(ByteBuffer out, Object obj, VisitedObjects visitedObjs) {
		if (!obj.getClass().isEnum()) throw new IllegalArgumentException("Expected Enum, got " + obj);
		
		StaticStringSerializer.writeString(out, obj.toString());
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected <T> T objectFromStringReader(StringReader in, Class<T> clazz, RetrievedObjects retrievedObjs)
			throws StreamCorruptedException {
		if (!clazz.isEnum()) throw new IllegalArgumentException("Expected Enum, got " + clazz);
		
		try {
			String name = StaticStringSerializer.readString(in, DELIM);
			return (T) Enum.valueOf((Class<Enum>) clazz, name);
		} catch (IOException e) {
			e.printStackTrace();
			throw new StreamCorruptedException();
		}
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected <T> T objectFromByteBuffer(ByteBuffer in, Class<T> clazz, RetrievedObjects retrievedObjs) {
		if (!clazz.isEnum()) throw new IllegalArgumentException("Expected Enum, got " + clazz);
		
		String name = StaticStringSerializer.readString(in);
		return (T) Enum.valueOf((Class<Enum>) clazz, name);
	}

}
