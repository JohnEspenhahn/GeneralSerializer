package org.espenhahn.serializer.valueserializers;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;

import org.espenhahn.serializer.ValueSerializerRegistry;
import org.espenhahn.serializer.util.ClassNameSerializer;
import org.espenhahn.serializer.util.RetrievedObjects;
import org.espenhahn.serializer.util.VisitedObjectRef;
import org.espenhahn.serializer.util.VisitedObjectRefImpl;
import org.espenhahn.serializer.util.VisitedObjects;

public class VisitedRefSerializerImpl extends AValueSerializer implements VisitedRefSerializer {
	
	@Override
	public String getClassName() {
		return ".vr";
	}
	
	@Override
	protected void objectToStringBuffer(StringBuffer out, Object obj, VisitedObjects visitedObjs)
			throws NotSerializableException {
		if (!(obj instanceof VisitedObjectRef)) throw new IllegalArgumentException("Expected VisitedObjectRef, got " + obj);
		ClassNameSerializer cns = ValueSerializerRegistry.getClassNameSerializer();
		
		cns.writeClassName(out, getClassName());
		out.append(((VisitedObjectRef) obj).getOffset());
	}

	@Override
	protected void objectToByteBuffer(ByteBuffer out, Object obj, VisitedObjects visitedObjs)
			throws NotSerializableException {
		if (!(obj instanceof VisitedObjectRef)) throw new IllegalArgumentException("Expected VisitedObjectRef, got " + obj);
		ClassNameSerializer cns = ValueSerializerRegistry.getClassNameSerializer();
		
		cns.writeClassName(out, getClassName());
		out.putInt(((VisitedObjectRef) obj).getOffset());
	}

	@Override
	protected <T> T objectFromStringBuffer(StringBuffer in, Class<T> clazz, RetrievedObjects retrevedObjs)
			throws StreamCorruptedException, NotSerializableException {
		// TODO
		throw new UnsupportedOperationException();
	}

	@Override
	@SuppressWarnings("unchecked")
	protected <T> T objectFromByteBuffer(ByteBuffer in, Class<T> clazz, RetrievedObjects retrevedObjs)
			throws StreamCorruptedException, NotSerializableException {
		if (!clazz.isAssignableFrom(VisitedObjectRef.class)) throw new IllegalArgumentException("Expected VisitedObjectRef, got " + clazz);
		
		// Class name will be read in DispatchingSerializer
		VisitedObjectRef obj = new VisitedObjectRefImpl();
		obj.setOffset(in.getInt());
		return (T) obj;
	}

}
