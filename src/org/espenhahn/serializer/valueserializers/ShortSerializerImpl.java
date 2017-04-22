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
public class ShortSerializerImpl extends AValueSerializer {

	public ShortSerializerImpl() {
		super(true);
	}
	
	@Override
	protected void objectToStringBuffer(StringBuffer out, Object obj, VisitedObjects visitedObjs) {
		StaticStringSerializer.writeString(out, obj.toString(), DELIM);
	}

	@Override
	protected void objectToByteBuffer(ByteBuffer out, Object obj, VisitedObjects visitedObjs) {
		out.putShort((short) obj);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T> T objectFromStringReader(StringReader in, Class<T> clazz, RetrievedObjects retrevedObjs) throws StreamCorruptedException {
		try {
			String shtString = StaticStringSerializer.readString(in, DELIM);
			return (T) (Short) Short.parseShort(shtString);
		} catch (IOException e) {
			throw new StreamCorruptedException();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	protected <T> T objectFromByteBuffer(ByteBuffer in, Class<T> clazz, RetrievedObjects retrevedObjs) {		
		return (T) (Short) in.getShort();
	}

}
