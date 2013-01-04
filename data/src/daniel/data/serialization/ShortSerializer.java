package daniel.data.serialization;

public final class ShortSerializer extends AbstractSerializer<Short> {
  public static final ShortSerializer singleton = new ShortSerializer();

  private ShortSerializer() {}

  @Override
  public void writeToSink(Short n, ByteSink sink) {
    for (int i = 0; i < 2; ++i)
      sink.give((byte) (n >>> (i * 8)));
  }

  @Override
  public Short readFromSource(ByteSource source) {
    short n = 0;
    for (int i = 0; i < 2; ++i)
      // The "& 0xFF" is needed to remove the bits from sign extension after the byte is promoted
      // to an int.
      n |= (source.take() & 0xFF) << (i * 8);
    return n;
  }
}

