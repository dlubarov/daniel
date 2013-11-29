package daniel.blog.post;

import daniel.data.serialization.AbstractSerializer;
import daniel.data.serialization.BooleanSerializer;
import daniel.data.serialization.ByteSink;
import daniel.data.serialization.ByteSource;
import daniel.data.serialization.InstantSerializer;
import daniel.data.serialization.StringSerializer;

public final class PostSerializer extends AbstractSerializer<Post> {
  public static final PostSerializer singleton = new PostSerializer();

  private PostSerializer() {}

  @Override
  public void writeToSink(Post post, ByteSink sink) {
    StringSerializer.singleton.writeToSink(post.getUuid(), sink);
    InstantSerializer.singleton.writeToSink(post.getCreatedAt(), sink);
    StringSerializer.singleton.writeToSink(post.getSubject(), sink);
    StringSerializer.singleton.writeToSink(post.getContent(), sink);
    BooleanSerializer.singleton.writeToSink(post.isPublished(), sink);
  }

  @Override
  public Post readFromSource(ByteSource source) {
    Post.Builder builder = new Post.Builder()
        .setUuid(StringSerializer.singleton.readFromSource(source))
        .setCreatedAt(InstantSerializer.singleton.readFromSource(source))
        .setSubject(StringSerializer.singleton.readFromSource(source))
        .setContent(StringSerializer.singleton.readFromSource(source));
    // TODO: Remove once 'published' is backfilled.
    if (source.hasNext())
      builder.setPublished(BooleanSerializer.singleton.readFromSource(source));
    else
      builder.setPublished(true);
    return builder.build();
  }
}
