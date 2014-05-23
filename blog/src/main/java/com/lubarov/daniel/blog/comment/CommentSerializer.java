package com.lubarov.daniel.blog.comment;

import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.serialization.*;

public final class CommentSerializer extends AbstractSerializer<Comment> {
  public static final CommentSerializer singleton = new CommentSerializer();

  private static final Serializer<Option<String>> optStringSerializer =
      new OptionSerializer<>(StringSerializer.singleton);

  private CommentSerializer() {}

  @Override
  public void writeToSink(Comment comment, ByteSink sink) {
    StringSerializer.singleton.writeToSink(comment.getUuid(), sink);
    StringSerializer.singleton.writeToSink(comment.getPostUuid(), sink);
    InstantSerializer.singleton.writeToSink(comment.getCreatedAt(), sink);
    StringSerializer.singleton.writeToSink(comment.getAuthorName(), sink);
    optStringSerializer.writeToSink(comment.getAuthorEmail(), sink);
    StringSerializer.singleton.writeToSink(comment.getContent(), sink);
    BooleanSerializer.singleton.writeToSink(comment.isApproved(), sink);
  }

  @Override
  public Comment readFromSource(ByteSource source) {
    return new Comment.Builder()
        .setUuid(StringSerializer.singleton.readFromSource(source))
        .setPostUuid(StringSerializer.singleton.readFromSource(source))
        .setCreatedAt(InstantSerializer.singleton.readFromSource(source))
        .setAuthorName(StringSerializer.singleton.readFromSource(source))
        .setAuthorEmail(optStringSerializer.readFromSource(source))
        .setContent(StringSerializer.singleton.readFromSource(source))
        .setApproved(BooleanSerializer.singleton.readFromSource(source))
        .build();
  }
}
