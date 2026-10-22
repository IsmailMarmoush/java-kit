package com.marmoush.javakit.jackson;

import com.marmoush.javakit.core.text.TextException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class JacksonTransformerErrorTest {
  @Test
  void wrapsDeserializationErrorsAsTextException() {
    //given
    var transformer = JacksonTransformerBuilder.json().asTextTransformer();

    //when
    var exception = Assertions.catchThrowable(() -> transformer.deserialize("{invalid", Object.class));

    //then
    Assertions.assertThat(exception).isInstanceOf(TextException.class).hasCauseInstanceOf(RuntimeException.class);
  }
}
