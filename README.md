# GeneralSerializer
A serializer for arbitrary objects, implemented in Java

Can use ValueSerializerRegistry to customized the (de)serializers

## Package: org.espenhahn.serializer.huffman
Huffman encoding optimized for ascii-based Java class names

## Sample Encoding
The object to be serialized

```Java
MyObject obj = new MyObject();
MyObject obj2 = new MyObject();

obj.obj1 = obj2;
obj2.obj1 = obj;

obj.anInt = 1;
obj2.anInt = 2;

obj.objects = new ArrayList();
obj.objects.add(10);
obj.objects.add("Hello world");
obj.objects.add(obj2);

obj2.objects = obj.objects;

obj2.maps = new HashMap();
obj2.maps.put("key1", "value1");
obj2.maps.put("key2", "value2");
```

Result using TextualSerializerImpl
```
org.espenhahn.serializer.test.MyObject:1,.nl:org.espenhahn.serializer.test.MyObject:2,java.util.HashMap:2,java.lang.String:4:key1java.lang.String:6:value1java.lang.String:4:key2java.lang.String:6:value2.vr:0,java.util.ArrayList:3,java.lang.Integer:10,java.lang.String:11:Hello world.vr:1,.vr:3,
```

The result using BinarySerializerImpl is 360 bytes, or if class name compression is enabled (see ValueSerializerRegistry) 227 bytes.
