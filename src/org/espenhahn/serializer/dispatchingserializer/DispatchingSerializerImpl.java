package org.espenhahn.serializer.dispatchingserializer;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;

import org.espenhahn.serializer.ValueSerializerRegistry;
import org.espenhahn.serializer.util.ClassNameSerializer;
import org.espenhahn.serializer.util.RetrievedObjects;
import org.espenhahn.serializer.util.VisitedObjectRef;
import org.espenhahn.serializer.util.VisitedObjectRefImpl;
import org.espenhahn.serializer.util.VisitedObjects;
import org.espenhahn.serializer.valueserializers.ValueSerializer;
import org.espenhahn.serializer.valueserializers.VisitedRefSerializer;

import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({ Comp533Tags.DISPATCHING_SERIALIZER })
public class DispatchingSerializerImpl implements DispatchingSerializer {

	@Override
	public void objectToBuffer(Object out, Object obj, VisitedObjects visitedObjs) throws NotSerializableException {
		// TODO check for enum, array, bean, list-pattern
		
		// Is visited?
		if (visitedObjs.isVisited(obj)) {
			// Handle visited
			VisitedRefSerializer vrSerializer = ValueSerializerRegistry.getVisitedRefSerializer();
			
			int idx = visitedObjs.getIndex(obj);
			vrSerializer.objectToBuffer(out, new VisitedObjectRefImpl(idx), visitedObjs);
		} else {
			// Is atomic?
			ValueSerializer valueSerializer = ValueSerializerRegistry.getValueSerializer(obj.getClass());
			if (valueSerializer != null) {
				// Handle atomic
				valueSerializer.objectToBuffer(out, obj, visitedObjs);
			} else {
				// TODO for each component, handle component
				throw new NotSerializableException();
			}
		}
	}

	@Override
	public Object objectFromBuffer(Object in, RetrievedObjects retrievedObjs) 
			throws StreamCorruptedException, NotSerializableException {
		VisitedRefSerializer vrSerializer = ValueSerializerRegistry.getVisitedRefSerializer();
		ClassNameSerializer cns = ValueSerializerRegistry.getClassNameSerializer();
		
		// TODO check for enum, array, bean, list-pattern
		
		String className = cns.readClassName(in);
		// Is visited?
		if (className.equals(vrSerializer.getClassName())) {
			VisitedObjectRef ref = vrSerializer.objectFromBuffer(in, VisitedObjectRef.class, retrievedObjs);
			
			return retrievedObjs.get(ref);				
		} else {
			Class<?> clazz;
			try {
				clazz = Class.forName(className);
			} catch (ClassNotFoundException e) {
				throw new NotSerializableException();
			}
			
			ValueSerializer valueSerializer = ValueSerializerRegistry.getValueSerializer(clazz);
			if (valueSerializer != null) { // Is atomic?
				return valueSerializer.objectFromBuffer(in, clazz, retrievedObjs);
			} else {
				// TODO for each component, handle component
				throw new NotSerializableException();
			}
		}		
	}

}
