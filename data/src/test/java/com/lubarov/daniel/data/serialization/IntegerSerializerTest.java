package com.lubarov.daniel.data.serialization;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IntegerSerializerTest {
  @Test
  public void testVarious() {
    test(Integer.MIN_VALUE);
    test(-1);
    test(0);
    test(1);
    test(7);
    test(Integer.MAX_VALUE);
  }

  private static void test(int n) {
    byte[] bytes = IntegerSerializer.singleton.writeToByteArray(n);
    int nRead = IntegerSerializer.singleton.readFromByteArray(bytes);
    assertEquals(n, nRead);
  }
}
