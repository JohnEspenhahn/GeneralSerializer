package org.espenhahn.serializer.specialvalueserializers;

import java.io.StringReader;
import java.nio.ByteBuffer;

import org.espenhahn.serializer.util.RetrievedObjects;
import org.espenhahn.serializer.util.VisitedObjects;
import org.espenhahn.serializer.valueserializers.AValueSerializer;

import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({ Comp533Tags.NULL_SERIALIZER })
public class NullSerializerImpl extends AValueSerializer implements SpecialValueSerializer {
	
	public NullSerializerImpl() {
		super(false);
	}

	@Override
	public String getClassName() {
		return ".nl";
	}
	
	@Override
	protected void objectToStringBuffer(StringBuffer out, Object obj, VisitedObjects visitedObjs) {

	}

	@Override
	protected void objectToByteBuffer(ByteBuffer out, Object obj, VisitedObjects visitedObjs) {

	}

	@Override
	protected <T> T objectFromStringReader(StringReader in, Class<T> clazz, RetrievedObjects retrevedObjs) {
		return null;
	}

	@Override
	protected <T> T objectFromByteBuffer(ByteBuffer in, Class<T> clazz, RetrievedObjects retrevedObjs) {
		return null;
	}

}
