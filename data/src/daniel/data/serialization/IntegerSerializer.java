package daniel.data.serialization;

/**
 * An integer serializer which uses a little-endian binary encoding.
 */
public final class IntegerSerializer extends AbstractSerializer<Integer> {
  public static final IntegerSerializer singleton = new IntegerSerializer();

  private IntegerSerializer() {}

  @Override
  public void writeToSink(Integer n, ByteSink sink) {
    for (int i = 0; i < 4; ++i)
      sink.give((byte) (n >>> (i * 8)));
  }

  @Override
  public Integer readFromSource(ByteSource source) {
    int n = 0;
    for (int i = 0; i < 4; ++i)
      // The "& 0xFF" is needed to remove the bits from sign extension after the byte is promoted
      // to an int.
      n |= (source.take() & 0xFF) << (i * 8);
    return n;
  }
}
