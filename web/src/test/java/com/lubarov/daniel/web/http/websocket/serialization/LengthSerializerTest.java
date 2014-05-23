package com.lubarov.daniel.web.http.websocket.serialization;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LengthSerializerTest {
  @Test
  public void testSeveral() {
    testLengthSerialization(0, 2);
    testLengthSerialization(1, 2);
    testLengthSerialization(25, 2);
    testLengthSerialization(0xFFFF, 2);
    testLengthSerialization(1234567, 8);
  }

  private void testLengthSerialization(int length, int numBytes) {
    byte[] bytes = LengthSerializer.encodeLength(length, numBytes);
    assertEquals(numBytes, bytes.length);
    int deserializedLength = LengthSerializer.decodeLength(bytes);
    assertEquals(length, deserializedLength);
  }
}
