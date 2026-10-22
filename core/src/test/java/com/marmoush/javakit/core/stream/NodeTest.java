package com.marmoush.javakit.core.stream;

import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

class NodeTest {

  @Test
  @DisplayName("Should block until tail is added")
  void tailBlocking() throws Exception {
    //given
    var node = new MemBlockingChain.Node<>("hello");
    var addedNode = new MemBlockingChain.Node<>("world");
    var started = new CountDownLatch(1);
    var tailResult = new CompletableFuture<MemBlockingChain.Node<String>>();

    //when
    var tailReader = Thread.startVirtualThread(() -> {
      started.countDown();
      try {
        tailResult.complete(node.tail().call());
      } catch (Exception exception) {
        tailResult.completeExceptionally(exception);
      }
    });

    try {
      Assertions.assertThat(started.await(1, TimeUnit.SECONDS)).isTrue();
      Awaitility.await()
                .atMost(Duration.ofSeconds(1))
                .untilAsserted(() -> Assertions.assertThat(tailReader.getState()).isEqualTo(Thread.State.WAITING));
      Assertions.assertThat(tailResult).isNotCompleted();
      var added = node.add(addedNode);

      //then
      Assertions.assertThat(added).isTrue();
      Assertions.assertThat(tailResult.get(1, TimeUnit.SECONDS)).isSameAs(addedNode);
    } finally {
      node.add(addedNode);
      tailReader.join(TimeUnit.SECONDS.toMillis(1));
    }
  }

  @Test
  @DisplayName("Should only add tail once and never change")
  void tailCAS() throws Exception {
    //given
    var node = new MemBlockingChain.Node<>("hello");
    var firstTail = new MemBlockingChain.Node<>("world");
    var secondTail = new MemBlockingChain.Node<>("again");

    //when
    var firstResult = node.add(firstTail);
    var secondResult = node.add(secondTail);

    //then
    Assertions.assertThat(firstResult).isTrue();
    Assertions.assertThat(secondResult).isFalse();
    Assertions.assertThat(node.tail().call()).isSameAs(firstTail);
    Assertions.assertThat(node.tail().call().head()).isEqualTo("world");
  }
}
