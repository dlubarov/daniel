package com.lubarov.daniel.data.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class DigestUtils {
  private static final MessageDigest md5;

  static {
    try {
      md5 = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      throw new AssertionError(e);
    }
  }

  private DigestUtils() {}

  public static String md5Hex(byte[] data) {
    return toHex(md5.digest(data));
  }

  private static String toHex(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (byte b : bytes)
      sb.append(Integer.toHexString(b & 0xFF | 0x100).substring(1, 3));
    return sb.toString();
  }
}
