package com.marmoush.javakit.core.domain;

import com.marmoush.javakit.core.domain.Result.Success;
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
      return Result.success(Integer.parseInt(s));
    } catch (NumberFormatException e) {
      return Result.failure(new Invalid("not a number: " + s));
    }
  }

  private static Result<Integer, Failure> positive(int n) {
    return n > 0 ? Result.success(n) : Result.failure(new Invalid("not positive: " + n));
  }

  // ---------------------------------------------------------------------------------------------
  // Factories and equality
  // ---------------------------------------------------------------------------------------------

  @Nested
  class Factories {

    @Test
    void ok_createsOkHoldingValue() {
      // Given
      Result<Integer, Failure> result = Result.success(7);

      // When
      assertInstanceOf(Success.class, result);
      assertEquals(new Success<Integer, Failure>(7), result);
    }

    @Test
    void err_createsErrHoldingError() {
      // Given
      Result<Integer, Failure> result = Result.failure(new NotFound("x"));

      // When
      assertInstanceOf(Result.Failure.class, result);
      assertEquals(new Result.Failure<Integer, Failure>(new NotFound("x")), result);
    }

    @Test
    void equality_isStructural() {
      assertEquals(Result.success(1), Result.success(1));
      assertNotEquals(Result.success(1), Result.success(2));
      assertEquals(Result.failure(new Invalid("a")), Result.failure(new Invalid("a")));
      assertNotEquals(Result.failure(new Invalid("a")), Result.failure(new Invalid("b")));
    }

    @Test
    void okAndErr_neverEqual() {
      assertNotEquals(Result.success(1), Result.failure(1));
    }
  }

  // ---------------------------------------------------------------------------------------------
  // map
  // ---------------------------------------------------------------------------------------------

  @Nested
  class Map {

    @Test
    void onOk_transformsValue() {
      assertEquals(Result.success(42), parse("21").map(n -> n * 2));
    }

    @Test
    void onOk_canChangeValueType() {
      // Given
      Result<String, Failure> result = Result.<Integer, Failure>success(5).map(n -> "v" + n);

      // When
      assertEquals(Result.success("v5"), result);
    }

    @Test
    void onErr_keepsErrorUnchanged() {
      assertEquals(Result.failure(new Invalid("not a number: abc")), parse("abc").map(n -> n * 2));
    }

    @Test
    void onErr_neverCallsFunction() {
      // Given
      var calls = new AtomicInteger();
      Result<Integer, Failure> failed = Result.failure(new NotFound("x"));

      // When
      failed.map(n -> {
        calls.incrementAndGet();
        return n + 1;
      });

      // Then
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
      assertEquals(Result.success(5), parse("5").flatMap(ResultTest::positive));
    }

    @Test
    void onOk_canReturnErr() {
      assertEquals(Result.failure(new Invalid("not positive: -3")), parse("-3").flatMap(ResultTest::positive));
    }

    @Test
    void onErr_shortCircuitsWithoutCallingFunction() {
      // Given
      var calls = new AtomicInteger();

      // When
      var result = parse("abc").flatMap(n -> {
        calls.incrementAndGet();
        return positive(n);
      });

      // Then
      assertEquals(Result.failure(new Invalid("not a number: abc")), result);
      assertEquals(0, calls.get());
    }

    @Test
    void chain_stopsAtFirstError() {
      // Given
      var calls = new AtomicInteger();

      // When
      var result = parse("-1").flatMap(ResultTest::positive).flatMap(n -> {
        calls.incrementAndGet();
        return positive(n * 2);
      }).map(n -> {
        calls.incrementAndGet();
        return n + 1;
      });

      // Then
      assertEquals(Result.failure(new Invalid("not positive: -1")), result);
      assertEquals(0, calls.get());
    }

    @Test
    void chain_runsEveryStepOnSuccess() {
      var result = parse("21").flatMap(ResultTest::positive).map(n -> n * 2);

      assertEquals(Result.success(42), result);
    }
  }

  // ---------------------------------------------------------------------------------------------
  // Functor / monad laws
  // ---------------------------------------------------------------------------------------------

  @Nested
  class Laws {

    private final Function<Integer, Result<Integer, Failure>> f = ResultTest::positive;

    private final Function<Integer, Result<Integer, Failure>> g = n -> n < 100 ? Result.success(n * 2)
                                                                               : Result.failure(new Invalid("too big: "
                                                                                                            + n));

    private final Result<Integer, Failure> okValue = Result.success(5);
    private final Result<Integer, Failure> errValue = Result.failure(new NotFound("x"));

    @Test
    void map_identity() {
      assertEquals(okValue, okValue.map(Function.identity()));
      assertEquals(errValue, errValue.map(Function.identity()));
    }

    @Test
    void flatMap_leftIdentity() {
      for (int n : new int[]{-1, 5, 150}) {
        assertEquals(f.apply(n), Result.<Integer, Failure>success(n).flatMap(f));
      }
    }

    @Test
    void flatMap_rightIdentity() {
      assertEquals(okValue, okValue.flatMap(Result::<Integer, Failure>success));
      assertEquals(errValue, errValue.flatMap(Result::<Integer, Failure>success));
    }

    @Test
    void flatMap_associativity() {
      for (int n : new int[]{-1, 5, 60, 150}) {
        Result<Integer, Failure> m = Result.success(n);

        assertEquals(m.flatMap(f).flatMap(g), m.flatMap(x -> f.apply(x).flatMap(g)));
      }
    }
  }
}