package com.lubarov.daniel.data.util;

import com.lubarov.daniel.data.option.Option;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ArrayUtilsTest {
  @Test
  public void testContains() {
    assertFalse(ArrayUtils.contains('a', new char[0]));
    assertFalse(ArrayUtils.contains('b', new char[] {'a', 'c'}));
    assertTrue(ArrayUtils.contains('b', new char[] {'a', 'b', 'c'}));
  }

  @Test
  public void testFirstIndexOf() {
    assertEquals(Option.<Integer>none(),
        ArrayUtils.firstIndexOf(new byte[] {1}, new byte[] {}));
    assertEquals(Option.<Integer>none(),
        ArrayUtils.firstIndexOf(new byte[] {1}, new byte[] {0, 2}));
    assertEquals(Option.some(0),
        ArrayUtils.firstIndexOf(new byte[] {1}, new byte[] {1}));
    assertEquals(Option.some(1),
        ArrayUtils.firstIndexOf(new byte[] {1, 2}, new byte[] {0, 1, 2, 3}));
  }
}
