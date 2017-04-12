package org.espenhahn.serializer.valueserializers;

import java.nio.ByteBuffer;

import org.espenhahn.serializer.ValueSerializerRegistry;
import org.espenhahn.serializer.util.ClassNameSerializer;
import org.espenhahn.serializer.util.RetrievedObjects;
import org.espenhahn.serializer.util.VisitedObjectRef;
import org.espenhahn.serializer.util.VisitedObjectRefImpl;
import org.espenhahn.serializer.util.VisitedObjects;

public class VisitedRefSerializerImpl extends AValueSerializer implements SpecialValueSerializer {
	
	@Override
	public String getClassName() {
		return ".vr";
	}
	
	@Override
	protected void objectToStringBuffer(StringBuffer out, Object obj, VisitedObjects visitedObjs) {
		
		int idx = visitedObjs.getIndex(obj);
		if (idx == -1) throw new IllegalArgumentException("Not actually visited");
		
		ClassNameSerializer cns = ValueSerializerRegistry.getClassNameSerializer();		
		cns.writeClassName(out, getClassName());
		out.append(idx);
	}

	@Override
	protected void objectToByteBuffer(ByteBuffer out, Object obj, VisitedObjects visitedObjs) {
		
		int idx = visitedObjs.getIndex(obj);
		if (idx == -1) throw new IllegalArgumentException("Not actually visited");
		
		ClassNameSerializer cns = ValueSerializerRegistry.getClassNameSerializer();
		cns.writeClassName(out, getClassName());
		out.putInt(idx);
	}

	@Override
	protected <T> T objectFromStringBuffer(StringBuffer in, Class<T> clazz, RetrievedObjects retrevedObjs) {
		// TODO
		throw new UnsupportedOperationException();
	}

	@Override
	@SuppressWarnings("unchecked")
	protected <T> T objectFromByteBuffer(ByteBuffer in, Class<T> clazz, RetrievedObjects retrevedObjs) {		
		// Class name will be read in DispatchingSerializer
		VisitedObjectRef obj = new VisitedObjectRefImpl();
		obj.setOffset(in.getInt());
		return (T) obj;
	}

}
