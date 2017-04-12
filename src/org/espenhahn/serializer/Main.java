package org.espenhahn.serializer;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;

import serialization.Serializer;

public class Main {

	public static void main(String[] args) {
		ValueSerializerRegistry.initDefault();
		Serializer s = new BinarySerializerImpl();
		
		Object[] arr = new Object[3];
		arr[0] = 1;
		arr[1] = "test";
		arr[2] = arr;
		
		try {
			ByteBuffer bb = s.outputBufferFromObject(arr);
			byte[] content = new byte[bb.remaining()];
			bb.get(content);
			System.out.println(new String(content));
			
			bb.flip();
			Object[] deserialized = (Object[]) s.objectFromInputBuffer(bb);
			System.out.println(deserialized);
			System.out.println(deserialized[0]);
			System.out.println(deserialized[1]);
			System.out.println(deserialized[2]);
		} catch (NotSerializableException e) {
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		}
	}

}
