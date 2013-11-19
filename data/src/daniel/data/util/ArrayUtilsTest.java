package daniel.data.util;

import daniel.data.option.Option;
import org.junit.Test;

import static org.junit.Assert.*;

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
    assertEquals(Option.<Integer>some(0),
        ArrayUtils.firstIndexOf(new byte[] {1}, new byte[] {1}));
    assertEquals(Option.<Integer>some(1),
        ArrayUtils.firstIndexOf(new byte[] {1, 2}, new byte[] {0, 1, 2, 3}));
  }
}
