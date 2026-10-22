package com.marmoush.javakit.jackson;

import java.time.LocalDate;
import java.util.List;

public record Engineer(String id, String name, LocalDate birthday, List<String> tasks) implements Employee {}
