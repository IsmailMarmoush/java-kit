module com.marmoush.javakit.eventsourcing {
  requires com.marmoush.javakit.core;
  requires org.slf4j;
  requires static lombok;
  exports com.marmoush.javakit.eventsourcing.handler;
  exports com.marmoush.javakit.eventsourcing.command;
  exports com.marmoush.javakit.eventsourcing.event;
  exports com.marmoush.javakit.eventsourcing.event.repo;
  exports com.marmoush.javakit.eventsourcing.state;
  exports com.marmoush.javakit.eventsourcing.error;
  exports com.marmoush.javakit.eventsourcing.exception;
  exports com.marmoush.javakit.eventsourcing;
  exports com.marmoush.javakit.eventsourcing.processor;
  exports com.marmoush.javakit.eventsourcing.command.repo;
}