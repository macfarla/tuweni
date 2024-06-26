// Copyright The Tuweni Authors
// SPDX-License-Identifier: Apache-2.0
package org.apache.tuweni.scuttlebutt

import org.apache.tuweni.bytes.Bytes
import org.apache.tuweni.crypto.SECP256K1
import org.apache.tuweni.crypto.sodium.Signature
import java.util.Objects

/**
 * SECP256K1 Scuttlebutt identity backed by a public key.
 *
 * This representation doesn't support signing messages.
 */
internal class SECP256K1PublicKeyIdentity(private val publicKey: SECP256K1.PublicKey) : Identity {
  override fun sign(message: Bytes): Bytes {
    throw UnsupportedOperationException("Cannot sign messages with a public key identity")
  }

  override fun verify(signature: Bytes, message: Bytes): Boolean {
    return SECP256K1.verify(message, SECP256K1.Signature.fromBytes(signature), publicKey)
  }

  override fun publicKeyAsBase64String(): String {
    return publicKey.bytes().toBase64String()
  }

  override fun curve(): Identity.Curve {
    return Identity.Curve.SECP256K1
  }

  override fun ed25519PublicKey(): Signature.PublicKey? {
    throw UnsupportedOperationException()
  }

  override fun secp256k1PublicKey(): SECP256K1.PublicKey {
    return publicKey
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || javaClass != other.javaClass) return false
    val identity = other as SECP256K1PublicKeyIdentity
    return publicKey == identity.publicKey
  }

  override fun hashCode(): Int {
    return Objects.hash(publicKey)
  }

  override fun toString(): String {
    return toCanonicalForm()
  }
}
