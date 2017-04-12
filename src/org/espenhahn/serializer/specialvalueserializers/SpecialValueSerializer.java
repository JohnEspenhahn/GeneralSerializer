package org.espenhahn.serializer.specialvalueserializers;

import org.espenhahn.serializer.valueserializers.ValueSerializer;

public interface SpecialValueSerializer extends ValueSerializer {

	String getClassName();
	
}
