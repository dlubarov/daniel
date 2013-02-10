package daniel.data.serialization;

import daniel.data.option.Option;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OptionSerializerTest {
  @Test
  public void testVarious() {
    test(Option.some(""));
    test(Option.some("abc"));
    test(Option.<String>none());
  }

  private static void test(Option<String> original) {
    byte[] bytes = OptionSerializer.optStringSerializer.writeToByteArray(original);
    Option<String> deserialized = OptionSerializer.optStringSerializer.readFromByteArray(bytes);
    assertEquals(original, deserialized);
  }
}
