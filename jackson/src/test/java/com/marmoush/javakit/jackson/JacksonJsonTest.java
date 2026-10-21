package com.marmoush.javakit.jackson;

import com.marmoush.javakit.core.text.TextException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class JacksonJsonTest {
  @Test
  void serializeEngineer() {
    var yamlEngineerJson = TestUtils.json.serialize(Resources.BOB_ENGINEER);
    Assertions.assertThat(yamlEngineerJson).isEqualTo(Resources.BOB_ENGINEER_JSON);
  }

  @Test
  void serializeManager() {
    var engineerYaml = TestUtils.json.serialize(Resources.ANNIKA_MANAGER);
    Assertions.assertThat(engineerYaml).isEqualTo(Resources.ANNIKA_MANAGER_JSON);
  }

  @Test
  void serializeEmployees() {
    var employeesJson = TestUtils.json.serialize(Resources.EMPLOYEES);
    Assertions.assertThat(employeesJson).isEqualTo(Resources.EMPLOYEES_JSON);
  }

  @Test
  void deserializeDepartment() throws TextException {
    // Given
    var expectedDepartment = new Department(List.of(Resources.ANNIKA_MANAGER,
                                                    Resources.BOB_ENGINEER,
                                                    Resources.ALEX_ENGINEER));
    // When
    var actualDepartment = TestUtils.json.deserialize(Resources.DEPARTMENT_JSON, Department.class);
    // Then
    Assertions.assertThat(actualDepartment).isEqualTo(expectedDepartment);

  }

  @Test
  void deserializeEngineer() throws TextException {
    // When
    var engineer = TestUtils.json.deserialize(Resources.BOB_ENGINEER_JSON, Engineer.class);
    // Then
    Assertions.assertThat(engineer).isEqualTo(Resources.BOB_ENGINEER);
  }

  @Test
  void deserializeManager() throws TextException {
    // When
    var manager = TestUtils.json.deserialize(Resources.ANNIKA_MANAGER_JSON, Manager.class);
    // Then
    Assertions.assertThat(manager).isEqualTo(Resources.ANNIKA_MANAGER);
    Assertions.assertThat(manager.team()).isEqualTo(List.of(Resources.BOB_ENGINEER, Resources.ALEX_ENGINEER));
  }

  @Test
  void deserializeEmployees() throws TextException {
    // When
    var employees = TestUtils.json.deserialize(Resources.EMPLOYEES_JSON, Employee[].class);
    // Then
    Assertions.assertThat(employees).hasSameElementsAs(Arrays.stream(Resources.EMPLOYEES).toList());
  }
}
