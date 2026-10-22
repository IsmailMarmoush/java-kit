package com.marmoush.javakit.core.security;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class EncryptionConfigTest {
  @Test
  void createUsesDefaultSettings() {
    //given

    //when
    var config = EncryptionConfig.create();

    //then
    Assertions.assertThat(config.keyAlg()).isEqualTo(EncryptionConfig.DEFAULT_KEY_ALG);
    Assertions.assertThat(config.cipherAlg()).isEqualTo(EncryptionConfig.DEFAULT_CIPHER_ALG);
    Assertions.assertThat(config.secretKeySpecAlg()).isEqualTo(EncryptionConfig.DEFAULT_SECRET_KEY_SPEC_ALG);
    Assertions.assertThat(config.iterationCount()).isEqualTo(EncryptionConfig.DEFAULT_ITERATION_COUNT);
    Assertions.assertThat(config.keyLength()).isEqualTo(EncryptionConfig.DEFAULT_KEY_LENGTH);
  }

  @Test
  void withIterationCountPreservesOtherSettings() {
    //given
    var config = EncryptionConfig.create();
    var iterations = 1000;

    //when
    var updated = config.withIterationCount(iterations);

    //then
    Assertions.assertThat(updated)
              .isEqualTo(new EncryptionConfig(config.keyAlg(),
                                              config.cipherAlg(),
                                              config.secretKeySpecAlg(),
                                              iterations,
                                              config.keyLength()));
    Assertions.assertThat(config.iterationCount()).isEqualTo(EncryptionConfig.DEFAULT_ITERATION_COUNT);
  }
}
