package daniel.data.serialization;

import java.nio.charset.StandardCharsets;

public class StringSerializer extends AbstractSerializer<String> {
  public static final StringSerializer singleton = new StringSerializer();

  private StringSerializer() {}

  @Override
  public void writeToSink(String s, ByteSink sink) {
    byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
    IntegerSerializer.singleton.writeToSink(bytes.length, sink);
    sink.giveAll(bytes);
  }

  @Override
  public String readFromSource(ByteSource source) {
    int numBytes = IntegerSerializer.singleton.readFromSource(source);
    byte[] bytes = source.takeMany(numBytes);
    return new String(bytes, StandardCharsets.UTF_8);
  }
}
