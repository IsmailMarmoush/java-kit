package com.marmoush.javakit.eventsourcing.state;

import com.marmoush.javakit.eventsourcing.Partitioned;
import com.marmoush.javakit.eventsourcing.id.StateId;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class StateMeta implements Partitioned, Serializable {
  @Serial
  private static final long serialVersionUID = 0L;
  private final StateId stateId;

  public StateMeta(StateId stateId) {
    this.stateId = stateId;
  }

  @Override
  public StateId stateId() {return stateId;}

  @Override
  public boolean equals(Object obj) {
    if (obj == this)
      return true;
    if (obj == null || obj.getClass() != this.getClass())
      return false;
    var that = (StateMeta) obj;
    return Objects.equals(this.stateId, that.stateId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stateId);
  }

  @Override
  public String toString() {
    return "StateMeta[" + "stateId=" + stateId + ']';
  }
}
