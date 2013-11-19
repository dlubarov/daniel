package daniel.data.source;

import daniel.data.sequence.ImmutableArray;
import daniel.data.sequence.Sequence;
import org.junit.Test;

import static org.junit.Assert.*;

public class RecursiveSourceTest {
  @Test
  public void testTryTake() {
    assertEquals(seq(),
        new RecursiveSource<String>().takeAll());
    assertEquals(seq(),
        new RecursiveSource<>(src(), src(), src()).takeAll());
    assertEquals(seq("a", "b", "c"),
        new RecursiveSource<>(src("a"), src("b", "c")).takeAll());
  }

  private static Sequence<String> seq(String... strings) {
    return ImmutableArray.create(strings);
  }

  private static Source<String> src(String... strings) {
    return seq(strings).getEnumerator();
  }
}
