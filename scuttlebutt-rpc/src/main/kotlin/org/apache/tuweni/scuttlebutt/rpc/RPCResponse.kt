// Copyright The Tuweni Authors
// SPDX-License-Identifier: Apache-2.0
package org.apache.tuweni.scuttlebutt.rpc

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.tuweni.bytes.Bytes
import org.apache.tuweni.scuttlebutt.rpc.RPCFlag.BodyType
import java.io.IOException
import java.nio.charset.StandardCharsets

/**
 * A successful RPC response.
 * @param body the body of the response in bytes
 * @param bodyType the type of the response (e.g. JSON, UTF-8 or binary.)
 */
class RPCResponse(private val body: Bytes, private val bodyType: BodyType) {
  /**
   * Response body
   *
   * @return the RPC response body
   */
  fun body(): Bytes {
    return body
  }

  /**
   * Responsse body type
   *
   * @return The type of the data contained in the body.
   */
  fun bodyType(): BodyType {
    return bodyType
  }

  /**
   * Provides the body of the message as a UTF-8 string.
   *
   * @return the body of the message as a UTF-8 string
   */
  fun asString(): String {
    return String(body().toArrayUnsafe(), StandardCharsets.UTF_8)
  }

  /**
   * Provides the body of the message, marshalled as a JSON object.
   *
   * @param objectMapper the object mapper to deserialize with
   * @param clazz the JSON object class
   * @param <T> the matching JSON object class
   * @return a new instance of the JSON object class
   * @throws IOException if an error occurs during marshalling
   </T> */
  @Throws(IOException::class)
  fun <T> asJSON(objectMapper: ObjectMapper, clazz: Class<T>?): T {
    return objectMapper.readerFor(clazz).readValue(body().toArrayUnsafe())
  }

  @Throws(IOException::class)
  fun <T> asJSON(objectMapper: ObjectMapper, typeReference: TypeReference<T>?): T {
    return objectMapper.readerFor(typeReference).readValue(body().toArrayUnsafe())
  }
}
