package com.marmoush.javakit.eventsourcing.id;

import java.util.UUID;

public class PartitionId extends DefaultId {

  public PartitionId(String value) {
    super(value);
  }

  public static PartitionId of(String value) {
    return new PartitionId(value);
  }

  public static PartitionId of(long value) {
    return new PartitionId(String.valueOf(value));
  }

  public static PartitionId of(UUID uuid) {
    return new PartitionId(uuid.toString());
  }

  public static PartitionId of(Id id) {
    return new PartitionId(id.value());
  }

  public static PartitionId randomUUID() {
    return PartitionId.of(UUID.randomUUID().toString());
  }
}
