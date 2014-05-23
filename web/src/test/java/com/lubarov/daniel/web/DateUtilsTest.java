package com.lubarov.daniel.web;

import com.lubarov.daniel.data.unit.Instant;
import com.lubarov.daniel.web.http.DateUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DateUtilsTest {
  @Test
  public void testFormatRfc1123() {
    assertEquals("Thu, 01 Jan 1970 00:00:00 GMT", DateUtils.formatRfc1123(Instant.UNIX_EPOCH));
  }

  @Test
  public void testFormatIso8601() {
    assertEquals("1970-01-01T00:00Z", DateUtils.formatIso8601(Instant.UNIX_EPOCH));
  }
}
