// Copyright The Tuweni Authors
// SPDX-License-Identifier: Apache-2.0
package org.apache.tuweni.crypto.blake2bf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.tuweni.bytes.Bytes;

import org.bouncycastle.util.Pack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test vectors adapted from
 * https://github.com/keep-network/blake2b/blob/master/compression/f_test.go
 */
public class Blake2bfMessageDigestTest {

  private Blake2bfMessageDigest messageDigest;

  // output when input is all 0
  private static final Bytes BLAKE2F_ALL_ZERO =
      Bytes.wrap(
          new byte[] {
            8, -55, -68, -13, 103, -26, 9, 106, 59, -89, -54, -124, -123, -82, 103, -69, 43, -8,
            -108, -2, 114, -13, 110, 60, -15, 54, 29, 95, 58, -11, 79, -91, -47, -126, -26, -83,
            127, 82, 14, 81, 31, 108, 62, 43, -116, 104, 5, -101, 107, -67, 65, -5, -85, -39, -125,
            31, 121, 33, 126, 19, 25, -51, -32, 91
          });

  // output when input is all 0 for 4294967295 rounds
  private static final Bytes BLAKE2F_ALL_ZERO_NEGATIVE_ROUNDS =
      Bytes.wrap(
          new byte[] {
            -111, -99, -124, 115, 29, 109, 127, 118, 18, 21, 75, -89, 60, 35, 112, 81, 110, 78, -8,
            40, -102, 19, -73, -97, 57, 69, 69, -89, 83, 66, 124, -43, -92, 78, 115, 115, 117, 123,
            -105, -25, 25, -74, -1, -94, -127, 14, 87, 123, -26, 84, -75, -82, -78, 54, 48, -125,
            38, -58, 7, -61, 120, -93, -42, -38
          });

  @BeforeEach
  public void setUp() {
    messageDigest = new Blake2bfMessageDigest();
  }

  @Test
  public void digestIfUpdatedCorrectlyWithBytes() {
    for (int i = 0; i < 213; i++) {
      messageDigest.update((byte) 0);
    }
    assertEquals(BLAKE2F_ALL_ZERO, Bytes.wrap(messageDigest.digest()));
  }

  @Test
  public void digestIfUpdatedCorrectlyWithByteArray() {
    final byte[] update = new byte[213];
    messageDigest.update(update, 0, 213);
    assertEquals(BLAKE2F_ALL_ZERO, Bytes.wrap(messageDigest.digest()));
  }

  @Test
  public void digestIfUpdatedCorrectlyMixed() {
    final byte[] update = new byte[213];
    messageDigest.update((byte) 0);
    messageDigest.update(update, 2, 211);
    messageDigest.update((byte) 0);
    assertEquals(BLAKE2F_ALL_ZERO, Bytes.wrap(messageDigest.digest()));
  }

  @Test
  public void digestWithMaxRounds() {
    // equal to unsigned int max value (4294967295, or signed -1)
    final byte[] rounds = Pack.intToBigEndian(Integer.MIN_VALUE);
    messageDigest.update(rounds, 0, 4);
    messageDigest.update(new byte[213], 0, 209);
    assertEquals(BLAKE2F_ALL_ZERO_NEGATIVE_ROUNDS, Bytes.wrap(messageDigest.digest()));
  }

  @Test
  public void throwsIfBufferUpdatedWithLessThat213Bytes() {
    for (int i = 0; i < 212; i++) {
      messageDigest.update((byte) 0);
    }
    assertThrows(
        IllegalStateException.class,
        () -> {
          messageDigest.digest();
        });
  }

  @Test
  public void throwsIfBufferUpdatedWithMoreThat213Bytes() {
    for (int i = 0; i < 213; i++) {
      messageDigest.update((byte) 0);
    }
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          messageDigest.update((byte) 0);
        });
  }

  @Test
  public void throwsIfBufferUpdatedLargeByteArray() {
    final byte[] update = new byte[213];
    messageDigest.update((byte) 0);
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          messageDigest.update(update, 0, 213);
        });
  }

  @Test
  public void throwsIfEmptyBufferUpdatedLargeByteArray() {
    final byte[] update = new byte[214];
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          messageDigest.update(update, 0, 214);
        });
  }
}
