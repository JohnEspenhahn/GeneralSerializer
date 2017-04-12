package org.espenhahn.serializer.dispatchingserializer;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;

import org.espenhahn.serializer.util.RetrievedObjects;
import org.espenhahn.serializer.util.VisitedObjects;

import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({ Comp533Tags.DISPATCHING_SERIALIZER })
public interface DispatchingSerializer {

	void objectToBuffer(Object out, Object obj, VisitedObjects visitedObjs) throws NotSerializableException;
	
	Object objectFromBuffer(Object in, RetrievedObjects retrievedObjs) throws StreamCorruptedException;
	
}
