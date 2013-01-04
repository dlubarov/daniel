package daniel.data.serialization;

public final class DoubleSerializer extends AbstractSerializer<Double> {
  public static final DoubleSerializer singleton = new DoubleSerializer();

  private DoubleSerializer() {}

  @Override
  public void writeToSink(Double x, ByteSink sink) {
    LongSerializer.singleton.writeToSink(Double.doubleToLongBits(x), sink);
  }

  @Override
  public Double readFromSource(ByteSource source) {
    return Double.longBitsToDouble(LongSerializer.singleton.readFromSource(source));
  }
}

