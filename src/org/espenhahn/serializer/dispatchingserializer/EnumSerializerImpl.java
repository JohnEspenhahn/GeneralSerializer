package org.espenhahn.serializer.dispatchingserializer;

import java.io.StreamCorruptedException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.espenhahn.serializer.util.RetrievedObjects;
import org.espenhahn.serializer.util.VisitedObjects;
import org.espenhahn.serializer.valueserializers.AValueSerializer;

import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({ Comp533Tags.ENUM_SERIALIZER })
public class EnumSerializerImpl extends AValueSerializer {
	private static final String ENCODING = "UTF16";

	@Override
	protected void objectToStringBuffer(StringBuffer out, Object obj, VisitedObjects visitedObjs) {
		if (!obj.getClass().isEnum()) throw new IllegalArgumentException("Expected Enum, got " + obj);
		
		String s = obj.toString();
		out.append(s.length());
		out.append(':');
		out.append(s);
	}

	@Override
	protected void objectToByteBuffer(ByteBuffer out, Object obj, VisitedObjects visitedObjs) {
		if (!obj.getClass().isEnum()) throw new IllegalArgumentException("Expected Enum, got " + obj);
		
		try {
			byte[] buff = out.toString().getBytes(ENCODING);
			out.putInt(buff.length);
			out.put(buff);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected <T> T objectFromStringBuffer(StringBuffer in, Class<T> clazz, RetrievedObjects retrievedObjs)
			throws StreamCorruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected <T> T objectFromByteBuffer(ByteBuffer in, Class<T> clazz, RetrievedObjects retrievedObjs)
			throws StreamCorruptedException {
		if (!clazz.isEnum()) throw new IllegalArgumentException("Expected Enum, got " + clazz);
		
		int lng = in.getInt();
		byte[] res = new byte[lng];
		in.get(res);
		try {
			String name = new String(res, ENCODING);
			return (T) Enum.valueOf((Class<Enum>) clazz, name);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new StreamCorruptedException();
		}
	}

}
