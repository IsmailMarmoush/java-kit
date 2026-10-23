# java-kit

> [!WARNING]
> This repo is under construction and major refactoring at the moment

[![Release](https://github.com/ismailmarmoush/java-kit/workflows/Release/badge.svg)](https://github.com/ismailmarmoush/java-kit/actions?query=workflow%3ARelease)
[![Version](https://img.shields.io/maven-central/v/com.marmoush.java-kit/core?label=Version&logo=apachemaven)](https://central.sonatype.com/artifact/com.marmoush.java-kit/core)
[![Last Update](https://img.shields.io/maven-central/last-update/com.marmoush.java-kit/core?label=Updated&logo=apachemaven)](https://central.sonatype.com/artifact/com.marmoush.java-kit/core)
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

The Maven reactor contains three modules:

| Module                                         | Purpose                                                                                |
|------------------------------------------------|----------------------------------------------------------------------------------------|
| [`core`](core/src/main/java)                   | Shared domain, file, math, security, stream, text, and utility APIs.                   |
| [`eventsourcing`](eventsourcing/src/main/java) | Commands, events, state, aggregate handling, event repositories, and saga integration. |
| [`jackson`](jackson/src/main/java)             | Jackson-based JSON/YAML transformers and value-object serializers.                     |

The [`core`](core/src/main/java) module provides reusable building blocks:

* `domain.Result<T, E>` represents either a successful value or an error and supports mapping and flat-mapping.
* `domain.ValueObject` is the common contract for value objects.
* `file.ConfigFile` reads resource or filesystem configuration, can include nested files with `#include:`, and supports
  `${NAME:-default}` interpolation from environment variables and Java system properties.
* `text.TextTransformer` and related types define text serialization/deserialization contracts.
* `stream.BlockingChain` exposes an append-only in-memory chain whose fetch stream waits for elements as they arrive.
* `security`, `math`, and `utils` provide shared hashing/encryption, numeric, and general-purpose helpers.

### Event sourcing

The `eventsourcing` module models a state as a versioned projection of its events. Application code supplies the domain
rules through:

* A `Decider`, which evaluates a command and returns a `DecisionResult` containing either an event or a domain error.
* An `Evolver`, which applies an event to produce a state.
* A `Saga`, which may react to an event by returning a command.

`Command` and `Event` carry metadata that associates them with a `StateId`; commands have a `CommandId`, while events
also have an `EventId` and state version. The first event for a state has version `0`; later events advance the version.
Both metadata types include a timestamp and optional `sagaSource`. IDs are distinct value types (`CommandId`,
`EventId`, and `StateId`) rather than untyped strings.

`Aggregate.create(...)` creates a synchronous aggregate using a decider, evolver, and `EventRepo`. The provided
`EventRepo.inMemory()` implementation is process-local; applications needing durable storage must provide and configure
their own persistence integration. `Aggregates` coordinates aggregate lookup through an `AggregateStore`.

`SagaHandler` delegates to a saga and publishes a command when the saga returns one. It uses the supplied
`CommandPublisher`; transport, retries, and durable delivery are the caller's responsibility.

Commands and events implement `Partitioned`. Their partition key is derived from their state ID, so related messages
can be assigned consistently to the same partition.

The module provides in-process abstractions and an in-memory repository, not a distributed event store, broker,
replication protocol, or automatic saga replay service.

### Jackson

The `jackson` module adapts Jackson 3 JSON and YAML mappers to the core `TextTransformer` contract. Use
`JacksonTransformerBuilder.json()` or `.yaml()`, then configure it before calling `asTextTransformer()`. Builder
options include default date formatting, duration formatting, pretty output, subtype registration, mix-in type
formats, and serializers for value objects.

Jackson dependencies are declared as `provided`; consuming applications must add compatible Jackson databind and YAML
dependencies themselves. See
[`JacksonTransformerBuilder`](jackson/src/main/java/com/marmoush/javakit/jackson/JacksonTransformerBuilder.java)
for the configuration API.

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
* Event sourcing
    * [x] ID refactoring
* [ ] Performance and benchmarks
* [ ] JVM Profiling
* [ ] More structured releasing once library is stable

### Release notes

* `27.0.1`
    * Targets JDK 27.
    * Includes the `core`, `eventsourcing`, and `jackson` modules.
    * Published to Maven Central.
