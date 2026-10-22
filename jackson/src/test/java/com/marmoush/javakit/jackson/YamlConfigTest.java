package com.marmoush.javakit.jackson;

import com.marmoush.javakit.core.file.ConfigFile;
import com.marmoush.javakit.core.text.TextException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class YamlConfigTest {
  @Test
  @DisplayName("App config nested values should be deserialized correctly")
  void appConfig() throws IOException, TextException {
    // Given
    var config = new ConfigFile("cases/config/yaml/AppConfigs.yaml").read();

    // When
    var appConfig = TestUtils.yaml.deserialize(config, AppConfig.class);

    // Then
    Assertions.assertThat(appConfig).isNotNull();
    Assertions.assertThat(appConfig.subName()).isEqualTo("hello world");
    Assertions.assertThat(appConfig.subList()).isEqualTo(List.of("hi", "hello", "bye"));
  }
}
