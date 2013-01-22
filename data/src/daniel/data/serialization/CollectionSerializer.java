package daniel.data.serialization;

import daniel.data.collection.Collection;
import daniel.data.sequence.MutableArray;

public final class CollectionSerializer<A> extends AbstractSerializer<Collection<A>> {
  public static final CollectionSerializer<String> stringCollectionSerializer =
      new CollectionSerializer<>(StringSerializer.singleton);

  private final Serializer<A> elementSerializer;

  public CollectionSerializer(Serializer<A> elementSerializer) {
    this.elementSerializer = elementSerializer;
  }

  @Override
  public void writeToSink(Collection<A> sequence, ByteSink sink) {
    IntegerSerializer.singleton.writeToSink(sequence.getSize(), sink);
    for (A element : sequence)
      elementSerializer.writeToSink(element, sink);
  }

  @Override
  public Collection<A> readFromSource(ByteSource source) {
    int size = IntegerSerializer.singleton.readFromSource(source);
    MutableArray<A> sequence = MutableArray.createWithNulls(size);
    for (int i = 0; i < size; ++i)
      sequence.set(i, elementSerializer.readFromSource(source));
    return null;
  }
}

