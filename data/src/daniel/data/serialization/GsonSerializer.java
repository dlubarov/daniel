package daniel.data.serialization;

import com.google.gson.Gson;

public final class GsonSerializer<A> extends AbstractSerializer<A> {
  private final Class<A> clazz;
  private final Gson gson;

  public GsonSerializer(Class<A> clazz) {
    this(clazz, new Gson());
  }

  public GsonSerializer(Class<A> clazz, Gson gson) {
    this.clazz = clazz;
    this.gson = gson;
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
