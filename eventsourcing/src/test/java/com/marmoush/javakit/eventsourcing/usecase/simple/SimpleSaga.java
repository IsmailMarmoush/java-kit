package com.marmoush.javakit.eventsourcing.usecase.simple;

import com.marmoush.javakit.eventsourcing.Saga;
import com.marmoush.javakit.eventsourcing.command.CreationCommand;
import com.marmoush.javakit.eventsourcing.command.CreationCommandMeta;
import com.marmoush.javakit.eventsourcing.command.EvolutionCommand;
import com.marmoush.javakit.eventsourcing.command.EvolutionCommandMeta;
import com.marmoush.javakit.eventsourcing.event.CreationEvent;
import com.marmoush.javakit.eventsourcing.event.EvolutionEvent;
import com.marmoush.javakit.eventsourcing.id.Id;

import java.util.Optional;
import java.util.function.Supplier;

public record SimpleSaga(Supplier<Id> IdSupplier) implements Saga {

  @Override
  public Optional<CreationCommand> react(CreationEvent event, CreationCommandMeta creationCommandMeta) {
    return Optional.of(new CreateState(creationCommandMeta));
  }

  @Override
  public Optional<CreationCommand> react(EvolutionEvent event, CreationCommandMeta creationCommandMeta) {
    return Optional.empty();
  }

  @Override
  public Optional<EvolutionCommand> react(CreationEvent event, EvolutionCommandMeta evolutionCommandMeta) {
    return Optional.empty();
  }

  @Override
  public Optional<EvolutionCommand> react(EvolutionEvent event, EvolutionCommandMeta evolutionCommandMeta) {
    return Optional.of(new ChangeState(evolutionCommandMeta));
  }
}
