package daniel.blog.post;

import daniel.data.serialization.AbstractSerializer;
import daniel.data.serialization.ByteSink;
import daniel.data.serialization.ByteSource;
import daniel.data.serialization.DateSerializer;
import daniel.data.serialization.StringSerializer;

public class PostSerializer extends AbstractSerializer<Post> {
  public static final PostSerializer singleton = new PostSerializer();

  private PostSerializer() {}

  @Override
  public void writeToSink(Post post, ByteSink sink) {
    StringSerializer.singleton.writeToSink(post.getUuid(), sink);
    DateSerializer.singleton.writeToSink(post.getCreatedAt(), sink);
    StringSerializer.singleton.writeToSink(post.getSubject(), sink);
    StringSerializer.singleton.writeToSink(post.getContent(), sink);
  }

  @Override
  public Post readFromSource(ByteSource source) {
    return new Post.Builder()
        .setUuid(StringSerializer.singleton.readFromSource(source))
        .setCreatedAt(DateSerializer.singleton.readFromSource(source))
        .setSubject(StringSerializer.singleton.readFromSource(source))
        .setContent(StringSerializer.singleton.readFromSource(source))
        .build();
  }
}
