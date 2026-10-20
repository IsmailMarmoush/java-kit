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
      case Success(T t) -> new Success<>(f.apply(t));
      case Failure(E e) -> new Failure<>(e);
    };
  }

  default <U> Result<T, U> mapError(Function<? super E, ? extends U> f) {
    return switch (this) {
      case Success(T t) -> new Success<>(t);
      case Failure(E e) -> new Failure<>(f.apply(e));
    };
  }

  default <U> Result<U, E> flatMap(Function<? super T, Result<U, E>> f) {
    return switch (this) {
      case Success(T t) -> f.apply(t);
      case Failure(E e) -> new Failure<>(e);
    };
  }
}