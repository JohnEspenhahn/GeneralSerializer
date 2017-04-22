package org.espenhahn.serializer;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.io.StringReader;
import java.nio.ByteBuffer;

import org.espenhahn.serializer.util.RetrievedObjectsImpl;
import org.espenhahn.serializer.util.VisitedObjectsImpl;

import serialization.Serializer;

public class TextualSerializerImpl implements Serializer {
	
	private RetrievedObjectsImpl retrievedObjs;
	private VisitedObjectsImpl visitedObjs;
	private StringBuffer resultBuffer;
	private ByteBuffer resultByteBuffer;
	
	public TextualSerializerImpl() {
		this.visitedObjs = new VisitedObjectsImpl();
		this.retrievedObjs = new RetrievedObjectsImpl();
		this.resultBuffer = new StringBuffer();
		this.resultByteBuffer = ByteBuffer.allocate(16384);
	}

	@Override
	public Object objectFromInputBuffer(ByteBuffer in) throws StreamCorruptedException {
		retrievedObjs.reset();
		
		byte[] bytes = new byte[in.remaining()];
		in.get(bytes);
		String str = new String(bytes);
		// System.out.println(str);
		
		StringReader inReader = new StringReader(str);
		return ValueSerializerRegistry.getDispatchingSerializer().objectFromBuffer(inReader, retrievedObjs);
	}

	@Override
	public ByteBuffer outputBufferFromObject(Object obj) throws NotSerializableException {
		resultBuffer.setLength(0);
		resultByteBuffer.clear();
		visitedObjs.reset();
		
		ValueSerializerRegistry.getDispatchingSerializer().objectToBuffer(resultBuffer, obj, visitedObjs);
		
		// Convert to bytebuffer
		byte[] res = resultBuffer.toString().getBytes();
		resultByteBuffer.put(res);
		
		resultByteBuffer.flip();
		return resultByteBuffer;
	}

}
