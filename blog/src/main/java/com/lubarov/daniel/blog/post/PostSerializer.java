package com.lubarov.daniel.blog.post;

import com.lubarov.daniel.data.serialization.*;

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
    return new Post.Builder()
        .setUuid(StringSerializer.singleton.readFromSource(source))
        .setCreatedAt(InstantSerializer.singleton.readFromSource(source))
        .setSubject(StringSerializer.singleton.readFromSource(source))
        .setContent(StringSerializer.singleton.readFromSource(source))
        .setPublished(BooleanSerializer.singleton.readFromSource(source))
        .build();
  }
}
