package org.espenhahn.serializer.dispatchingserializer;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;

import org.espenhahn.serializer.ValueSerializerRegistry;
import org.espenhahn.serializer.specialvalueserializers.SpecialValueSerializer;
import org.espenhahn.serializer.util.ClassNameSerializer;
import org.espenhahn.serializer.util.RetrievedObjects;
import org.espenhahn.serializer.util.VisitedObjects;
import org.espenhahn.serializer.valueserializers.ValueSerializer;

import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({ Comp533Tags.DISPATCHING_SERIALIZER })
public class DispatchingSerializerImpl implements DispatchingSerializer {

	@Override
	public void objectToBuffer(Object out, Object obj, VisitedObjects visitedObjs) throws NotSerializableException {
		ClassNameSerializer cns = ValueSerializerRegistry.getClassNameSerializer();		
		SpecialValueSerializer specialSerializer = ValueSerializerRegistry.getSpecialValueSerializer(obj, visitedObjs);
		if (specialSerializer != null) {
			cns.writeClassName(out, specialSerializer.getClassName());
			specialSerializer.objectToBuffer(out, obj, visitedObjs);
		} else {
			Class<?> clazz = obj.getClass();
			cns.writeClassName(out, clazz.getName());
			
			ValueSerializer valueSerializer = ValueSerializerRegistry.getValueSerializer(clazz);
			if (valueSerializer != null) {
				valueSerializer.objectToBuffer(out, obj, visitedObjs);
			} else {
				ValueSerializer typeSerializer = ValueSerializerRegistry.getTypeIndependentSerializer(clazz);
				if (typeSerializer != null) {
					typeSerializer.objectToBuffer(out, obj, visitedObjs);
				} else {
					throw new NotSerializableException();
				}
			}
		}
	}

	@Override
	public Object objectFromBuffer(Object in, RetrievedObjects retrievedObjs) throws StreamCorruptedException {		
		ClassNameSerializer cns = ValueSerializerRegistry.getClassNameSerializer();		
		String className = cns.readClassName(in);
		SpecialValueSerializer specialSerialier = ValueSerializerRegistry.getSpecialValueSerializer(className);
		if (specialSerialier != null) {
			return specialSerialier.objectFromBuffer(in, Object.class, retrievedObjs);
		} else {
			Class<?> clazz;
			try {
				clazz = Class.forName(className);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new StreamCorruptedException();
			}
			
			ValueSerializer valueSerializer = ValueSerializerRegistry.getValueSerializer(clazz);
			if (valueSerializer != null) {
				return valueSerializer.objectFromBuffer(in, clazz, retrievedObjs);
			} else {
				ValueSerializer typeSerializer = ValueSerializerRegistry.getTypeIndependentSerializer(clazz);
				if (typeSerializer != null) {
					return typeSerializer.objectFromBuffer(in, clazz, retrievedObjs);
				} else {
					throw new StreamCorruptedException();
				}
			}
		}		
	}

}
