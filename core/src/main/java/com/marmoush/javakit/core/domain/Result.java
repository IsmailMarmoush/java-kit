package com.marmoush.javakit.core.domain;

import java.util.function.Function;

public sealed interface Result<T, E> permits Result.Ok, Result.Err {
  record Ok<T, E>(T value) implements Result<T, E> {}

  record Err<T, E>(E error) implements Result<T, E> {}

  static <T, E> Result<T, E> ok(T value) {return new Ok<>(value);}

  static <T, E> Result<T, E> err(E error) {return new Err<>(error);}

  default <U> Result<U, E> map(Function<? super T, ? extends U> f) {
    return switch (this) {
      case Ok<T, E> ok -> new Ok<>(f.apply(ok.value()));
      case Err<T, E> err -> new Err<>(err.error());
    };
  }

  default <U> Result<T, U> mapError(Function<? super E, ? extends U> f) {
    return switch (this) {
      case Ok<T, E> ok -> new Ok<>(ok.value());
      case Err<T, E> err -> new Err<T, U>(f.apply(err.error()));
    };
  }

  default <U> Result<U, E> flatMap(Function<? super T, Result<U, E>> f) {
    return switch (this) {
      case Ok<T, E> ok -> f.apply(ok.value());
      case Err<T, E> err -> new Err<>(err.error());
    };
  }
}