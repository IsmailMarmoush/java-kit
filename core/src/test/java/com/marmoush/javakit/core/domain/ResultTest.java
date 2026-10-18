package com.marmoush.javakit.core.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ResultTest {

  // ---------------------------------------------------------------------------------------------
  // Test fixtures
  // ---------------------------------------------------------------------------------------------

  sealed interface Failure permits NotFound, Invalid {}

  record NotFound(String id) implements Failure {}

  record Invalid(String reason) implements Failure {}

  private static Result<Integer, Failure> parse(String s) {
    try {
      return Result.ok(Integer.parseInt(s));
    } catch (NumberFormatException e) {
      return Result.err(new Invalid("not a number: " + s));
    }
  }

  private static Result<Integer, Failure> positive(int n) {
    return n > 0 ? Result.ok(n) : Result.err(new Invalid("not positive: " + n));
  }

  private static String describe(Result<Integer, Failure> result) {
    return switch (result) {
      case Result.Ok<Integer, Failure>(var value) -> "ok:" + value;
      case Result.Err<Integer, Failure>(var error) -> "err:" + error;
    };
  }

  // ---------------------------------------------------------------------------------------------
  // Factories and equality
  // ---------------------------------------------------------------------------------------------

  @Nested
  class Factories {

    @Test
    void ok_createsOkHoldingValue() {
      Result<Integer, Failure> result = Result.ok(7);

      assertInstanceOf(Result.Ok.class, result);
      assertEquals(new Result.Ok<Integer, Failure>(7), result);
    }

    @Test
    void err_createsErrHoldingError() {
      Result<Integer, Failure> result = Result.err(new NotFound("x"));

      assertInstanceOf(Result.Err.class, result);
      assertEquals(new Result.Err<Integer, Failure>(new NotFound("x")), result);
    }

    @Test
    void equality_isStructural() {
      assertEquals(Result.ok(1), Result.ok(1));
      assertNotEquals(Result.ok(1), Result.ok(2));
      assertEquals(Result.err(new Invalid("a")), Result.err(new Invalid("a")));
      assertNotEquals(Result.err(new Invalid("a")), Result.err(new Invalid("b")));
    }

    @Test
    void okAndErr_neverEqual() {
      assertNotEquals(Result.ok(1), Result.err(1));
    }
  }

  // ---------------------------------------------------------------------------------------------
  // map
  // ---------------------------------------------------------------------------------------------

  @Nested
  class Map {

    @Test
    void onOk_transformsValue() {
      assertEquals(Result.ok(42), parse("21").map(n -> n * 2));
    }

    @Test
    void onOk_canChangeValueType() {
      Result<String, Failure> result = Result.<Integer, Failure>ok(5).map(n -> "v" + n);

      assertEquals(Result.ok("v5"), result);
    }

    @Test
    void onErr_keepsErrorUnchanged() {
      assertEquals(Result.err(new Invalid("not a number: abc")), parse("abc").map(n -> n * 2));
    }

    @Test
    void onErr_neverCallsFunction() {
      var calls = new AtomicInteger();
      Result<Integer, Failure> failed = Result.err(new NotFound("x"));

      failed.map(n -> {
        calls.incrementAndGet();
        return n + 1;
      });

      assertEquals(0, calls.get());
    }
  }

  // ---------------------------------------------------------------------------------------------
  // flatMap
  // ---------------------------------------------------------------------------------------------

  @Nested
  class FlatMap {

    @Test
    void onOk_returnsFunctionResult() {
      assertEquals(Result.ok(5), parse("5").flatMap(ResultTest::positive));
    }

    @Test
    void onOk_canReturnErr() {
      assertEquals(Result.err(new Invalid("not positive: -3")), parse("-3").flatMap(ResultTest::positive));
    }

    @Test
    void onErr_shortCircuitsWithoutCallingFunction() {
      var calls = new AtomicInteger();

      var result = parse("abc").flatMap(n -> {
        calls.incrementAndGet();
        return positive(n);
      });

      assertEquals(Result.err(new Invalid("not a number: abc")), result);
      assertEquals(0, calls.get());
    }

    @Test
    void chain_stopsAtFirstError() {
      var calls = new AtomicInteger();

      var result = parse("-1").flatMap(ResultTest::positive).flatMap(n -> {
        calls.incrementAndGet();
        return positive(n * 2);
      }).map(n -> {
        calls.incrementAndGet();
        return n + 1;
      });

      assertEquals(Result.err(new Invalid("not positive: -1")), result);
      assertEquals(0, calls.get());
    }

    @Test
    void chain_runsEveryStepOnSuccess() {
      var result = parse("21").flatMap(ResultTest::positive).map(n -> n * 2);

      assertEquals(Result.ok(42), result);
    }
  }

  // ---------------------------------------------------------------------------------------------
  // Functor / monad laws
  // ---------------------------------------------------------------------------------------------

  @Nested
  class Laws {

    private final Function<Integer, Result<Integer, Failure>> f = ResultTest::positive;

    private final Function<Integer, Result<Integer, Failure>> g = n -> n < 100 ? Result.ok(n * 2)
                                                                               : Result.err(new Invalid("too big: "
                                                                                                        + n));

    private final Result<Integer, Failure> okValue = Result.ok(5);
    private final Result<Integer, Failure> errValue = Result.err(new NotFound("x"));

    @Test
    void map_identity() {
      assertEquals(okValue, okValue.map(Function.identity()));
      assertEquals(errValue, errValue.map(Function.identity()));
    }

    @Test
    void flatMap_leftIdentity() {
      for (int n : new int[]{-1, 5, 150}) {
        assertEquals(f.apply(n), Result.<Integer, Failure>ok(n).flatMap(f));
      }
    }

    @Test
    void flatMap_rightIdentity() {
      assertEquals(okValue, okValue.flatMap(Result::<Integer, Failure>ok));
      assertEquals(errValue, errValue.flatMap(Result::<Integer, Failure>ok));
    }

    @Test
    void flatMap_associativity() {
      for (int n : new int[]{-1, 5, 60, 150}) {
        Result<Integer, Failure> m = Result.ok(n);

        assertEquals(m.flatMap(f).flatMap(g), m.flatMap(x -> f.apply(x).flatMap(g)));
      }
    }
  }

  // ---------------------------------------------------------------------------------------------
  // Pattern matching
  // ---------------------------------------------------------------------------------------------

  @Nested
  class PatternMatching {

    @Test
    void switch_handlesOk() {
      assertEquals("ok:1", describe(Result.ok(1)));
    }

    @Test
    void switch_handlesErr() {
      assertEquals("err:NotFound[id=a]", describe(Result.err(new NotFound("a"))));
    }
  }
}