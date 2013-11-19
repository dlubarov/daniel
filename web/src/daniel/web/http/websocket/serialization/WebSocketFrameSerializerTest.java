package daniel.web.http.websocket.serialization;

import daniel.web.http.websocket.WebSocketFrame;
import daniel.web.http.websocket.WebSocketOpcode;
import org.junit.Before;
import org.junit.Test;
import java.util.Random;

import static org.junit.Assert.*;

public class WebSocketFrameSerializerTest {
  private Random random;

  @Before
  public void setUp() {
    random = new Random(42);
  }

  @Test
  public void testEmptyFrame() {
    testSerialization(new WebSocketFrame.Builder().setFinalFragment(true)
        .setOpcode(WebSocketOpcode.TEXT_FRAME)
        .setPayload(new byte[0])
        .build());
  }

  @Test
  public void testSeveralRandomFrames() {
    for (int i = 0; i < 10; ++i)
      testRandomFrame();
  }

  private void testRandomFrame() {
    byte[] maskingKey = new byte[4];
    byte[] payload = new byte[random.nextInt(300)];
    random.nextBytes(maskingKey);
    random.nextBytes(payload);

    testSerialization(new WebSocketFrame.Builder()
        .setFinalFragment(random.nextBoolean())
        .setOpcode(WebSocketOpcode.values()[random.nextInt(WebSocketOpcode.values().length)])
        .setMaskingKey(maskingKey)
        .setPayload(payload)
        .build());
  }

  private static void testSerialization(WebSocketFrame frame) {
    byte[] bytes = WebSocketFrameSerializer.singleton.writeToByteArray(frame);
    WebSocketFrame deserializedFrame = WebSocketFrameSerializer.singleton.readFromByteArray(bytes);
    assertEquals(frame, deserializedFrame);
  }
}
