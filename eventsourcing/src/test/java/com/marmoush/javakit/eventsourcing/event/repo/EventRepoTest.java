package com.marmoush.javakit.eventsourcing.event.repo;

import com.marmoush.javakit.eventsourcing.event.Event;
import com.marmoush.javakit.eventsourcing.event.EventMeta;
import com.marmoush.javakit.eventsourcing.id.CommandId;
import com.marmoush.javakit.eventsourcing.id.EventId;
import com.marmoush.javakit.eventsourcing.id.StateId;
import com.marmoush.javakit.eventsourcing.usecase.simple.StateCreated;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class EventRepoTest {
  @Test
  void appendsAndFetchesEventsPerState() {
    //given
    var repo = EventRepo.inMemory();
    var firstState = StateId.of("first");
    var secondState = StateId.of("second");
    Event firstEvent = event("event-1", firstState, "command-1");
    Event secondEvent = event("event-2", firstState, "command-2");

    //when
    repo.append(firstEvent);
    repo.append(secondEvent);

    //then
    Assertions.assertThat(repo.fetch(firstState)).containsExactly(firstEvent, secondEvent);
    Assertions.assertThat(repo.fetch(secondState)).isEmpty();
    Assertions.assertThat(repo.size(firstState)).isEqualTo(2);
    Assertions.assertThat(repo.size(secondState)).isZero();
  }

  @Test
  void usesProvidedEventMapAsInitialStorage() {
    //given
    var stateId = StateId.of("state");
    Event seeded = event("seeded", stateId, "command-1");
    var events = new ArrayList<Event>(List.of(seeded));
    var initialData = new HashMap<StateId, List<Event>>();
    initialData.put(stateId, events);
    var repo = EventRepo.inMemory(initialData);
    Event appended = event("appended", stateId, "command-2");

    //when
    repo.append(appended);

    //then
    Assertions.assertThat(repo.fetch(stateId)).containsExactly(seeded, appended);
    Assertions.assertThat(initialData.get(stateId)).containsExactly(seeded, appended);
    Assertions.assertThat(repo.size(stateId)).isEqualTo(2);
  }

  private static Event event(String eventId, StateId stateId, String commandId) {
    return new StateCreated(new EventMeta(EventId.of(eventId), 0, stateId, CommandId.of(commandId)));
  }
}
