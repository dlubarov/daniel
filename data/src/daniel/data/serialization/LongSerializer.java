package daniel.data.serialization;

public final class LongSerializer extends AbstractSerializer<Long> {
  public static final LongSerializer singleton = new LongSerializer();

  private LongSerializer() {}

  @Override
  public void writeToSink(Long n, ByteSink sink) {
    for (int i = 0; i < 8; ++i)
      sink.give((byte) (n >>> (i * 8)));
  }

  @Override
  public Long readFromSource(ByteSource source) {
    long n = 0;
    for (int i = 0; i < 8; ++i)
      // The "& 0xFFL" is needed to remove the bits from sign extension after the byte is promoted
      // to a long.
      n |= (source.take() & 0xFFL) << (i * 8);
    return n;
  }
}

