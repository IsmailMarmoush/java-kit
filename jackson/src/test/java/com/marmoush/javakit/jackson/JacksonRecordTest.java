package com.marmoush.javakit.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.marmoush.javakit.core.text.TextException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.dataformat.yaml.YAMLMapper;

import java.util.List;

import static com.marmoush.javakit.jackson.TestUtils.json;

/**
 * A Copy from <a
 * href="https://github.com/FasterXML/jackson-databind/blob/977fbb3dcfa7ad7fda4f45f7f0c1afa0916b702e/src/test/java/com/fasterxml/jackson/databind/BaseMapTest.java">BaseMapTest</a>
 */
class JacksonRecordTest {

  private static JsonMapper jsonMapper;

  public String asYaml(String jsonString) {
    // parse JSON
    var jsonNodeTree = new ObjectMapper().readTree(jsonString);
    // save it as YAML
    return new YAMLMapper().writeValueAsString(jsonNodeTree);
  }

  @Test
  void testDeserializeRecordWithConstructor() {
    //given
    var json = "{\"id\":123,\"name\":\"Bob\"}";

    //when
    var value = jsonMapper.readValue(json, RecordWithConstructor.class);

    //then
    Assertions.assertThat(value).isEqualTo(new RecordWithConstructor(123, "Bob"));
  }

  @Test
  void testDeserializeSimpleRecord() {
    //given
    var json = "{\"id\":123,\"name\":\"Bob\"}";

    //when
    var value = jsonMapper.readValue(json, SimpleRecord.class);

    //then
    Assertions.assertThat(value).isEqualTo(new SimpleRecord(123, "Bob"));
  }

  @Test
  void testSerializeJsonIgnoreRecord() throws JacksonException {
    //given
    JsonIgnoreRecord value = new JsonIgnoreRecord(123, "Bob");

    //when
    var json = jsonMapper.writeValueAsString(value);

    //then
    Assertions.assertThat(json).isEqualTo("{\"id\":123}");
  }

  @Test
  void testSerializeRecordOfRecord() throws JacksonException {
    //given
    RecordOfRecord value = new RecordOfRecord(new SimpleRecord(123, "Bob"));

    //when
    var json = jsonMapper.writeValueAsString(value);

    //then
    Assertions.assertThat(json).isEqualTo("{\"record\":{\"id\":123,\"name\":\"Bob\"}}");
  }

  @Test
  void testSerializeSimpleRecord() throws JacksonException {
    //given
    SimpleRecord value = new SimpleRecord(123, "Bob");

    //when
    var json = jsonMapper.writeValueAsString(value);

    //then
    Assertions.assertThat(json).isEqualTo("{\"id\":123,\"name\":\"Bob\"}");
  }

  @Test
  void toList() throws TextException {
    //given
    var expected = List.of("mercedes", "chevy", "porsche");

    //when
    var list = json.deserialize(Resources.JSON_LIST, String[].class);

    //then
    Assertions.assertThat(list).containsExactlyElementsOf(expected);
  }

  @BeforeAll
  static void setUp() {
    jsonMapper = new JsonMapper();
  }

  record JsonIgnoreRecord(int id, @JsonIgnore String name) {}

  record RecordOfRecord(SimpleRecord simpleRecord) {}

  record RecordWithConstructor(int id, String name) {}

  record SimpleRecord(int id, String name) {}
}
