package org.espenhahn.serializer.valueserializers;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.espenhahn.serializer.util.RetrievedObjects;
import org.espenhahn.serializer.util.VisitedObjects;

import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({ Comp533Tags.VALUE_SERIALIZER })
public class StringSerializerImpl extends AValueSerializer {
	private static final String ENCODING = "UTF16";

	@Override
	protected void objectToStringBuffer(StringBuffer out, Object obj, VisitedObjects visitedObjs)
			throws NotSerializableException {
		if (!(obj instanceof String)) throw new IllegalArgumentException("Expected String, got " + obj);
		String str = (String) obj;
		
		out.append(str.length());
		out.append(':');
		out.append(str);
	}

	@Override
	protected void objectToByteBuffer(ByteBuffer out, Object obj, VisitedObjects visitedObjs)
			throws NotSerializableException {
		if (!(obj instanceof String)) throw new IllegalArgumentException("Expected String, got " + obj); 
		String str = (String) obj;
		
		try {
			byte[] bytes = str.getBytes(ENCODING);
			out.putInt(bytes.length);
			out.put(bytes);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected <T> T objectFromStringBuffer(StringBuffer in, Class<T> clazz, RetrievedObjects retrievedObjs)
			throws StreamCorruptedException {
		if (!clazz.isAssignableFrom(String.class)) throw new IllegalArgumentException("Expected String, got " + clazz);
		
		throw new UnsupportedOperationException();
	}

	@Override
	@SuppressWarnings("unchecked")
	protected <T> T objectFromByteBuffer(ByteBuffer in, Class<T> clazz, RetrievedObjects retrievedObjs)
			throws StreamCorruptedException {
		
		if (!clazz.isAssignableFrom(String.class)) throw new IllegalArgumentException("Expected String, got " + clazz);
		
		int lng = in.getInt();
		byte[] arr = new byte[lng];
		in.get(arr);
		
		try {
			return (T) new String(arr, ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new StreamCorruptedException();
		}
	}

}
