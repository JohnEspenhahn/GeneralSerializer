package org.espenhahn.serializer.valueserializers;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.io.StringReader;
import java.nio.ByteBuffer;

import org.espenhahn.serializer.specialvalueserializers.StaticStringSerializer;
import org.espenhahn.serializer.util.RetrievedObjects;
import org.espenhahn.serializer.util.VisitedObjects;

import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({ Comp533Tags.VALUE_SERIALIZER })
public class LongSerializerImpl extends AValueSerializer {
	
	public LongSerializerImpl() {
		super(true);
	}
	
	@Override
	protected void objectToStringBuffer(StringBuffer out, Object obj, VisitedObjects visitedObjs) {
		StaticStringSerializer.writeString(out, obj.toString(), DELIM);
	}

	@Override
	protected void objectToByteBuffer(ByteBuffer out, Object obj, VisitedObjects visitedObjs) {
		out.putLong((long) obj);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T> T objectFromStringReader(StringReader in, Class<T> clazz, RetrievedObjects retrevedObjs) throws StreamCorruptedException {
		try {
			String lngString = StaticStringSerializer.readString(in, DELIM);
			return (T) (Long) Long.parseLong(lngString);
		} catch (IOException e) {
			throw new StreamCorruptedException();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	protected <T> T objectFromByteBuffer(ByteBuffer in, Class<T> clazz, RetrievedObjects retrevedObjs) {
		return (T) (Long) in.getLong();
	}

}
