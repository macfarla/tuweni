// Copyright The Tuweni Authors
// SPDX-License-Identifier: Apache-2.0
package org.apache.tuweni.crypto.mikuli;

import org.apache.milagro.amcl.BLS381.BIG;
import org.apache.milagro.amcl.BLS381.ECP;
import org.apache.tuweni.bytes.Bytes;

import java.util.Objects;

/**
 * G1 is a subgroup of an elliptic curve whose points are elements of the finite field Fp - simple
 * numbers mod some prime p. The curve is defined by: y^2 = x^3 + 4
 */
final class G1Point implements Group<G1Point> {

  private static final int fpPointSize = BIG.MODBYTES;

  static G1Point fromBytes(Bytes bytes) {
    return new G1Point(ECP.fromBytes(bytes.toArrayUnsafe()));
  }

  private final ECP point;

  G1Point(ECP point) {
    this.point = point;
  }

  @Override
  public G1Point add(G1Point other) {
    ECP sum = new ECP();
    sum.add(point);
    sum.add(other.point);
    sum.affine();
    return new G1Point(sum);
  }

  @Override
  public G1Point mul(Scalar scalar) {
    ECP newPoint = point.mul(scalar.value());
    return new G1Point(newPoint);
  }

  Bytes toBytes() {
    // Size of the byte array representing compressed ECP point for BLS12-381 is
    // 49 bytes in milagro
    // size of the point = 48 bytes
    // meta information (parity bit, curve type etc) = 1 byte
    byte[] bytes = new byte[fpPointSize + 1];
    point.toBytes(bytes, true);
    return Bytes.wrap(bytes);
  }

  ECP ecpPoint() {
    return point;
  }

  @Override
  public String toString() {
    return point.toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    long x = point.getX().norm();
    long y = point.getY().norm();
    result = prime * result + (int) (x ^ (x >>> 32));
    result = prime * result + (int) (y ^ (y >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (Objects.isNull(obj)) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof G1Point)) {
      return false;
    }
    G1Point other = (G1Point) obj;
    return point.equals(other.point);
  }
}
