package com.marmoush.javakit.core.text;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class SerializableTransformerTest {
  @Test
  void serializableTest() throws TextException {
    // Given
    var transformer = new SerializableTransformer();
    var personObj = new Person("bob", 19, new Location(10, 20));

    // When
    var str = transformer.serialize(personObj);
    var obj = transformer.deserialize(str, Person.class);

    // Then
    Assertions.assertThat(personObj).isEqualTo(obj);
    Assertions.assertThat(personObj.i()).isEmpty();
  }

  @Test
  void wrapsSerializationFailure() {
    //given
    var transformer = new SerializableTransformer();
    var nonSerializable = new Object();

    //when
    var exception = Assertions.catchThrowable(() -> transformer.serialize(nonSerializable));

    //then
    Assertions.assertThat(exception)
              .isInstanceOf(TextRuntimeException.class)
              .hasCauseInstanceOf(java.io.NotSerializableException.class);
  }

  @Test
  void wrapsDeserializationFailure() {
    //given
    var transformer = new SerializableTransformer();
    var invalidSerializedData = "bm90IGEgc2VyaWFsaXplZCBvYmplY3Q=";

    //when
    var exception = Assertions.catchThrowable(() -> transformer.deserialize(invalidSerializedData, Object.class));

    //then
    Assertions.assertThat(exception)
              .isInstanceOf(TextException.class)
              .hasCauseInstanceOf(java.io.StreamCorruptedException.class);
  }
}
