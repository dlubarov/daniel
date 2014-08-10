package com.lubarov.daniel.data.source;

import com.lubarov.daniel.data.sequence.ImmutableArray;
import com.lubarov.daniel.data.sequence.Sequence;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RecursiveSourceTest {
  @Test
  public void testTryTake() {
    assertEquals(seq(),
        new RecursiveSource<String>().takeAll());
    assertEquals(seq(),
        new RecursiveSource<String>(src(), src(), src()).takeAll());
    assertEquals(seq("a", "b", "c"),
        new RecursiveSource<String>(src("a"), src("b", "c")).takeAll());
  }

  private static Sequence<String> seq(String... strings) {
    return ImmutableArray.create(strings);
  }

  private static Source<String> src(String... strings) {
    return seq(strings).getEnumerator();
  }
}
