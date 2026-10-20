package com.marmoush.javakit.core.domain;

import com.marmoush.javakit.core.domain.Result.Failure;
import com.marmoush.javakit.core.domain.Result.Success;

import java.util.function.Function;

public sealed interface Result<T, E> permits Success, Failure {
  record Success<T, E>(T value) implements Result<T, E> {}

  record Failure<T, E>(E error) implements Result<T, E> {}

  static <T, E> Result<T, E> success(T value) {return new Success<>(value);}

  static <T, E> Result<T, E> failure(E error) {return new Failure<>(error);}

  default <U> Result<U, E> map(Function<? super T, ? extends U> f) {
    return switch (this) {
      case Result.Success<T, E> success -> new Success<>(f.apply(success.value()));
      case Result.Failure<T, E> failure -> new Failure<>(failure.error());
    };
  }

  default <U> Result<T, U> mapError(Function<? super E, ? extends U> f) {
    return switch (this) {
      case Result.Success<T, E> success -> new Success<>(success.value());
      case Result.Failure<T, E> failure -> new Failure<T, U>(f.apply(failure.error()));
    };
  }

  default <U> Result<U, E> flatMap(Function<? super T, Result<U, E>> f) {
    return switch (this) {
      case Result.Success<T, E> success -> f.apply(success.value());
      case Result.Failure<T, E> failure -> new Failure<>(failure.error());
    };
  }
}