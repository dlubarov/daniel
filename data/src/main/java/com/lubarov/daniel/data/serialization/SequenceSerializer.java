package com.lubarov.daniel.data.serialization;

import com.lubarov.daniel.data.sequence.MutableArray;
import com.lubarov.daniel.data.sequence.Sequence;

public final class SequenceSerializer<A> extends AbstractSerializer<Sequence<A>> {
  public static final SequenceSerializer<String> stringSequenceSerializer =
      new SequenceSerializer<>(StringSerializer.singleton);

  private final Serializer<A> elementSerializer;

  public SequenceSerializer(Serializer<A> elementSerializer) {
    this.elementSerializer = elementSerializer;
  }

  @Override
  public void writeToSink(Sequence<A> sequence, ByteSink sink) {
    IntegerSerializer.singleton.writeToSink(sequence.getSize(), sink);
    for (A element : sequence)
      elementSerializer.writeToSink(element, sink);
  }

  @Override
  public Sequence<A> readFromSource(ByteSource source) {
    int size = IntegerSerializer.singleton.readFromSource(source);
    MutableArray<A> sequence = MutableArray.createWithNulls(size);
    for (int i = 0; i < size; ++i)
      sequence.set(i, elementSerializer.readFromSource(source));
    return sequence;
  }
}

