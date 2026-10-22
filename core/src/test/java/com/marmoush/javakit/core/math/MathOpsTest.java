package com.marmoush.javakit.core.math;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class MathOpsTest {
  @Test
  void calculatesMean() {
    //given
    var values = List.of(2.0, 4.0, 6.0);

    //when
    var mean = MathOps.mean(values);

    //then
    Assertions.assertThat(mean).isEqualTo(4.0);
  }

  @Test
  void calculatesSampleStandardDeviation() {
    //given
    var values = List.of(2.0, 4.0, 6.0);

    //when
    var deviation = MathOps.standardDeviation(values, 4.0);

    //then
    Assertions.assertThat(deviation).isEqualTo(2.0);
  }

  @Test
  void findsValuesOutsideThreeStandardDeviations() {
    //given
    var values = new ArrayList<>(Collections.nCopies(98, 0.0));
    values.add(-100.0);
    values.add(100.0);

    //when
    var outliers = MathOps.findStdDevOutliers(values);

    //then
    Assertions.assertThat(outliers).containsExactly(-100.0, 100.0);
  }

  @Test
  void returnsNoOutliersForEvenlyDistributedValues() {
    //given
    var values = List.of(1.0, 2.0, 3.0, 4.0, 5.0);

    //when
    var outliers = MathOps.findStdDevOutliers(values);

    //then
    Assertions.assertThat(outliers).isEmpty();
  }
}
