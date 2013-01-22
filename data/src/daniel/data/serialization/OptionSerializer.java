package daniel.data.serialization;

import daniel.data.option.Option;

public final class OptionSerializer<A> extends AbstractSerializer<Option<A>> {
  private final Serializer<A> valueSerializer;

  public OptionSerializer(Serializer<A> valueSerializer) {
    this.valueSerializer = valueSerializer;
  }

  @Override
  public void writeToSink(Option<A> option, ByteSink sink) {
    if (option.isDefined()) {
      sink.give((byte) 1);
      valueSerializer.writeToSink(option.getOrThrow(), sink);
    } else
      sink.give((byte) 0);
  }

  @Override
  public Option<A> readFromSource(ByteSource source) {
    boolean isDefined;
    switch (source.take()) {
      case 0:
        isDefined = false;
        break;
      case 1:
        isDefined = true;
        break;
      default:
        throw new RuntimeException("Unexpected byte.");
    }

    return isDefined
        ? Option.some(valueSerializer.readFromSource(source))
        : Option.<A>none();
  }
}
