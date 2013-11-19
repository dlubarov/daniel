package daniel.web.http.websocket;

import org.junit.Test;

import static org.junit.Assert.*;

public class AcceptKeyGeneratorTest {
  @Test
  public void testGenerateAcceptKey() {
    String accept = AcceptKeyGenerator.generateAcceptKey("x3JJHMbDL1EzLkh9GBhXDw==");
    assertEquals("HSmrc0sMlYUkAGmm5OPpG2HaGWk=", accept);
  }
}
