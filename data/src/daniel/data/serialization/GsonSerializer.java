package daniel.data.serialization;

import com.google.gson.Gson;

public final class GsonSerializer<A> extends AbstractSerializer<A> {
  private static final Gson gson = new Gson();

  private final Class<A> clazz;

  public GsonSerializer(Class<A> clazz) {
    this.clazz = clazz;
  }

  @Override
  public void writeToSink(A object, ByteSink sink) {
    StringSerializer.singleton.writeToSink(gson.toJson(object), sink);
  }

  @Override
  public A readFromSource(ByteSource source) {
    return gson.fromJson(StringSerializer.singleton.readFromSource(source), clazz);
  }
}
