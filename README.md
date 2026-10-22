# java-kit

> [!WARNING]
> This repo is under construction and major refactoring at the moment

[![Release](https://github.com/ismailmarmoush/java-kit/workflows/Release/badge.svg)](https://github.com/ismailmarmoush/java-kit/actions?query=workflow%3ARelease)
[![Version](https://img.shields.io/maven-central/v/com.marmoush.java-kit/?label=Version&logo=apachemaven)](https://central.sonatype.com/artifact/com.marmoush.java-kit/)
[![Last Update](https://img.shields.io/maven-central/last-update/com.marmoush.java-kit/?label=Updated&logo=apachemaven)](https://central.sonatype.com/artifact/com.marmoush.java-kit/)
[![License](https://img.shields.io/badge/License-Apache--2.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)

[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=ismailmarmoush_java-kit&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=ismailmarmoush_java-kit)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=ismailmarmoush_java-kit&metric=bugs)](https://sonarcloud.io/summary/new_code?id=ismailmarmoush_java-kit)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=ismailmarmoush_java-kit&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=ismailmarmoush_java-kit)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=ismailmarmoush_java-kit&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=ismailmarmoush_java-kit)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=ismailmarmoush_java-kit&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=ismailmarmoush_java-kit)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ismailmarmoush_java-kit&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=ismailmarmoush_java-kit)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=ismailmarmoush_java-kit&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=ismailmarmoush_java-kit)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=ismailmarmoush_java-kit&metric=coverage)](https://sonarcloud.io/summary/new_code?id=ismailmarmoush_java-kit)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=ismailmarmoush_java-kit&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=ismailmarmoush_java-kit)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=ismailmarmoush_java-kit&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=ismailmarmoush_java-kit)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=ismailmarmoush_java-kit&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=ismailmarmoush_java-kit)

> هذا العلم والعمل وقف للّه تعالي اسأل اللّه ان يرزقنا الاخلاص فالقول والعمل
>
> This work and knowledge is for the sake of Allah, may Allah grant us sincerity in what we say or do.

## Introduction

**Disclaimer:**
> java-kit is under active development and its APIs may change. It is a learning effort and is not yet recommended for
> production use. Contributions and issue reports are welcome.

java-kit is a collection of reusable Java libraries for application development.

The project currently targets JDK `27`.

## Architecture

* [Documentation directory](.docs)

The Maven reactor currently contains three modules:

* `core` provides shared primitives and utilities, including result/value-object types, file and environment helpers,
  text transformation interfaces, stream utilities, validation, math, and security helpers.
* `eventsourcing` provides event-sourcing building blocks: commands, events, states, deciders, evolvers, synchronous
  aggregates, saga handling, event repositories, and partitioning.
* `jackson` provides JSON and YAML text transformers and Jackson configuration helpers. Jackson libraries are declared
  as `provided`, so applications using this module must supply the compatible Jackson dependencies themselves.

## User Guide

Add the modules your application uses to its Maven dependencies. The published artifacts share the project version:

```xml

<dependency>
    <groupId>com.marmoush.java-kit</groupId>
    <artifactId>core</artifactId>
    <version>27.0.1</version>
</dependency>
```

For example, use `eventsourcing` or `jackson` as the artifact ID to add those modules. The `eventsourcing` module
depends on `core`. The `jackson` module also requires compatible Jackson 3 databind and YAML dependencies on the
application classpath.

Create a JSON or YAML transformer with the Jackson module:

```java
var json = JacksonTransformerBuilder.json().withDefaults().asTextTransformer();
```

### Versioning

The version format is `JDK-version.major.minor`; for example, `27.0.1` targets JDK 27.

Format Example: `JDK_Version.major.minor`

## Developer Guide

* [ ] Increase test coverage to >90%
* [x] Publish to maven central instead of Github
* Event-sourcing
    * [x] Id refactoring
* [ ] Performance and benchmarks
* [ ] JVM Profiling
* [ ] More structured releasing once library is stable

### Release notes

* `27.0.1`
    * Targets JDK 27.
    * Includes the `core`, `eventsourcing`, and `jackson` modules.
    * Published to Maven Central.

