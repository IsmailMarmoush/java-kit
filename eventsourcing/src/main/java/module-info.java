module com.marmoush.javakit.eventsourcing {
  requires com.marmoush.javakit.core;
  requires org.slf4j;
  exports com.marmoush.javakit.eventsourcing.aggregate;
  exports com.marmoush.javakit.eventsourcing.aggregate.store;
  exports com.marmoush.javakit.eventsourcing.command;
  exports com.marmoush.javakit.eventsourcing.event;
  exports com.marmoush.javakit.eventsourcing.event.repo;
  exports com.marmoush.javakit.eventsourcing.state;
  exports com.marmoush.javakit.eventsourcing.aggregate.error;
  exports com.marmoush.javakit.eventsourcing.aggregate.exception;
}