package org.espenhahn.serializer;

import serialization.Serializer;
import serialization.SerializerFactory;

public class MySerializerFactory implements SerializerFactory {

	private static boolean didInit = false;
	
	public void markInit() {
		didInit = true;
	}
	
	@Override
	public Serializer createSerializer() {
		if (!didInit) {
			ValueSerializerRegistry.initDefault();
			markInit();
		}
		
		// return new BinarySerializerImpl();
		return new TextualSerializerImpl();
	}

}
