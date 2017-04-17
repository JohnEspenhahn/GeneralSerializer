package org.espenhahn.serializer.valueserializers;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.io.StringReader;
import java.nio.ByteBuffer;

import org.espenhahn.serializer.util.RetrievedObjects;
import org.espenhahn.serializer.util.VisitedObjects;

import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({ Comp533Tags.VALUE_SERIALIZER })
public class BooleanSerializerImpl extends AValueSerializer {

	public BooleanSerializerImpl() {
		super(true);
	}

	@Override
	protected void objectToStringBuffer(StringBuffer out, Object obj, VisitedObjects visitedObjs) {
		out.append((Boolean) obj == true ? 'T' : 'F');
	}

	@Override
	protected void objectToByteBuffer(ByteBuffer out, Object obj, VisitedObjects visitedObjs) {
		out.put((boolean) obj ? (byte) 1 : (byte) 0);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T> T objectFromStringReader(StringReader in, Class<T> clazz, RetrievedObjects retrevedObjs) throws StreamCorruptedException {
		try {
			char c = (char) in.read();
			return (T) (Boolean) (c == 'T' ? true : false);
		} catch (IOException e) {
			throw new StreamCorruptedException();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	protected <T> T objectFromByteBuffer(ByteBuffer in, Class<T> clazz, RetrievedObjects retrevedObjs) {
		return (T) (Boolean) ((byte) in.get() == 1 ? true : false);
	}

}
