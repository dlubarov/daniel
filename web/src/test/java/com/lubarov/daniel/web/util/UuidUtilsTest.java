package com.lubarov.daniel.web.util;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class UuidUtilsTest {
  @Test
  public void testRandomAlphanumericUuid() {
    String uuid = UuidUtils.randomAlphanumericUuid();
    for (char c : uuid.toCharArray())
      assertTrue("Not alphanumeric: " + c, Character.isLetterOrDigit(c));
  }
}
