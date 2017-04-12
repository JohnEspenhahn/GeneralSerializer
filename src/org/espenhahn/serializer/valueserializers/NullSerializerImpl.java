package org.espenhahn.serializer.valueserializers;

import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;

import org.espenhahn.serializer.ValueSerializerRegistry;
import org.espenhahn.serializer.util.ClassNameSerializer;
import org.espenhahn.serializer.util.RetrievedObjects;
import org.espenhahn.serializer.util.VisitedObjects;

public class NullSerializerImpl extends AValueSerializer implements SpecialValueSerializer {
	
	@Override
	public String getClassName() {
		return ".nl";
	}
	
	@Override
	protected void objectToStringBuffer(StringBuffer out, Object obj, VisitedObjects visitedObjs) {
		ClassNameSerializer cns = ValueSerializerRegistry.getClassNameSerializer();
		cns.writeClassName(out, getClassName());
	}

	@Override
	protected void objectToByteBuffer(ByteBuffer out, Object obj, VisitedObjects visitedObjs) {
		ClassNameSerializer cns = ValueSerializerRegistry.getClassNameSerializer();		
		cns.writeClassName(out, getClassName());
	}

	@Override
	protected <T> T objectFromStringBuffer(StringBuffer in, Class<T> clazz, RetrievedObjects retrevedObjs)
			throws StreamCorruptedException {
		return null;
	}

	@Override
	protected <T> T objectFromByteBuffer(ByteBuffer in, Class<T> clazz, RetrievedObjects retrevedObjs)
			throws StreamCorruptedException {
		return null;
	}

}
