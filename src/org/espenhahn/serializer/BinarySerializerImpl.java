package org.espenhahn.serializer;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;

import org.espenhahn.serializer.util.ClassNameSerializer;
import org.espenhahn.serializer.util.RetrievedObjectsImpl;
import org.espenhahn.serializer.util.VisitedObjectsImpl;
import org.espenhahn.serializer.valueserializers.ValueSerializer;

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
		
		// Get class
		ClassNameSerializer cns = ValueSerializerRegistry.getClassNameSerializer();
		String className = cns.readClassName(in);
		
		Class<?> clazz;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new StreamCorruptedException();
		}
		
		// Is atomic?
		ValueSerializer valueSerializer = ValueSerializerRegistry.getValueSerializer(clazz);
		if (valueSerializer != null) {
			// Handle atomic
			try {
				return valueSerializer.objectFromBuffer(in, clazz, retrievedObjs);
			} catch (NotSerializableException e) {
				throw new StreamCorruptedException();
			}
		} else {
			// Dispatching
			try {
				return ValueSerializerRegistry.getDispatchingSerializer().objectFromBuffer(in, retrievedObjs);
			} catch (NotSerializableException e) {
				throw new StreamCorruptedException();
			}
		}
	}

	@Override
	public ByteBuffer outputBufferFromObject(Object obj) throws NotSerializableException {
		resultBuffer.clear();
		visitedObjs.reset();
		
		// Is atomic?
		ValueSerializer valueSerializer = ValueSerializerRegistry.getValueSerializer(obj.getClass());
		if (valueSerializer != null) {
			// Write class
			ClassNameSerializer cns = ValueSerializerRegistry.getClassNameSerializer();
			cns.writeClassName(resultBuffer, obj.getClass().getName());
			
			// Handle atomic
			valueSerializer.objectToBuffer(resultBuffer, obj, visitedObjs);
		} else {
			// Dispatching
			ValueSerializerRegistry.getDispatchingSerializer().objectToBuffer(resultBuffer, obj, visitedObjs);
		}
		
		resultBuffer.flip();
		return resultBuffer;
	}

}
