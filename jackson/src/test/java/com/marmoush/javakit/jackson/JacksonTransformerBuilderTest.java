package com.marmoush.javakit.jackson;

import com.marmoush.javakit.core.text.TextException;
import com.marmoush.javakit.core.text.TextTransformer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class JacksonTransformerBuilderTest {
  @Test
  void rejectsNullMapperBuilder() {
    //given

    //when / then
    Assertions.assertThatNullPointerException().isThrownBy(() -> new JacksonTransformerBuilder(null));
  }

  @Test
  void wrapperObjectMixInSerializesAndDeserializesSubtypes() throws TextException {
    //given
    TextTransformer transformer = JacksonTransformerBuilder.json()
                                                           .withMixInWrapperObjectFormat(Employee.class)
                                                           .withSubtypes(Engineer.class, Manager.class)
                                                           .asTextTransformer();
    Employee employee = new Engineer("id", "Alex", null, java.util.List.of());

    //when
    var serialized = transformer.serialize(employee);
    var deserialized = transformer.deserialize(serialized, Employee.class);

    //then
    Assertions.assertThat(serialized).contains("\"Engineer\"");
    Assertions.assertThat(deserialized).isEqualTo(employee);
  }

  @Test
  void exposesConfiguredObjectMapper() {
    //given
    var builder = JacksonTransformerBuilder.json().withPrettyFormat();

    //when
    var mapper = builder.objectMapper();
    var transformer = builder.asTextTransformer();

    //then
    Assertions.assertThat(mapper).isNotNull();
    Assertions.assertThat(transformer.serialize(new Engineer("id", "Alex", null, java.util.List.of())))
              .contains(System.lineSeparator());
  }
}
