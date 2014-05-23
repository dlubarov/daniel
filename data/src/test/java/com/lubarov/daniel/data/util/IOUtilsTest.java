package com.lubarov.daniel.data.util;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertArrayEquals;

public class IOUtilsTest {
  @Test
  public void testReadFromStream() throws IOException {
    InputStream stream = new ByteArrayInputStream(new byte[] {1, 2, 3, 4});
    byte[] firstTwo = IOUtils.readFromStream(stream, 2);
    byte[] rest = IOUtils.readEntireStream(stream);
    assertArrayEquals(new byte[] {1, 2}, firstTwo);
    assertArrayEquals(new byte[] {3, 4}, rest);
  }
}
