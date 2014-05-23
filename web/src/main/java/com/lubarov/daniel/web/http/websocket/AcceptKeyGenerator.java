package com.lubarov.daniel.web.http.websocket;

import com.lubarov.daniel.data.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class AcceptKeyGenerator {
  private static final String magicString = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";

  private static final MessageDigest sha1;

  static {
    try {
      sha1 = MessageDigest.getInstance("SHA-1");
    } catch (NoSuchAlgorithmException e) {
      throw new AssertionError("No SHA-1");
    }
  }

  private AcceptKeyGenerator() {}

  /**
   * Generates the "Sec-WebSocket-Accept" response as defined in RFC 6455.
   */
  public static String generateAcceptKey(String clientKey) {
    String concatenation = clientKey + magicString;
    byte[] sha1Bytes = sha1.digest(concatenation.getBytes(StandardCharsets.US_ASCII));
    return Base64.encode(sha1Bytes);
  }
}
