package org.espenhahn.serializer;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;

import org.espenhahn.serializer.util.RetrievedObjectsImpl;
import org.espenhahn.serializer.util.VisitedObjectsImpl;

import serialization.Serializer;

public class BinarySerializerImpl implements Serializer {
	
	private RetrievedObjectsImpl retrievedObjs;
	private VisitedObjectsImpl visitedObjs;
	private ByteBuffer resultBuffer;
	
	public BinarySerializerImpl() {
		this.visitedObjs = new VisitedObjectsImpl();
		this.retrievedObjs = new RetrievedObjectsImpl();
		this.resultBuffer = ByteBuffer.allocate(2048);
	}

	@Override
	public Object objectFromInputBuffer(ByteBuffer in) throws StreamCorruptedException {
		retrievedObjs.reset();
		
		return ValueSerializerRegistry.getDispatchingSerializer().objectFromBuffer(in, retrievedObjs);
	}

	@Override
	public ByteBuffer outputBufferFromObject(Object obj) throws NotSerializableException {
		resultBuffer.clear();
		visitedObjs.reset();
		
		ValueSerializerRegistry.getDispatchingSerializer().objectToBuffer(resultBuffer, obj, visitedObjs);
		
		resultBuffer.flip();
		return resultBuffer;
	}

}
