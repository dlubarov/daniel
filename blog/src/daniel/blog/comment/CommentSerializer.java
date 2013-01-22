package daniel.blog.comment;

import daniel.data.option.Option;
import daniel.data.serialization.AbstractSerializer;
import daniel.data.serialization.BooleanSerializer;
import daniel.data.serialization.ByteSink;
import daniel.data.serialization.ByteSource;
import daniel.data.serialization.DateSerializer;
import daniel.data.serialization.OptionSerializer;
import daniel.data.serialization.Serializer;
import daniel.data.serialization.StringSerializer;

public final class CommentSerializer extends AbstractSerializer<Comment> {
  public static final CommentSerializer singleton = new CommentSerializer();

  private static final Serializer<Option<String>> optStringSerializer =
      new OptionSerializer<>(StringSerializer.singleton);

  private CommentSerializer() {}

  @Override
  public void writeToSink(Comment comment, ByteSink sink) {
    StringSerializer.singleton.writeToSink(comment.getUuid(), sink);
    DateSerializer.singleton.writeToSink(comment.getCreatedAt(), sink);
    StringSerializer.singleton.writeToSink(comment.getAuthorName(), sink);
    optStringSerializer.writeToSink(comment.getAuthorEmail(), sink);
    StringSerializer.singleton.writeToSink(comment.getContent(), sink);
    BooleanSerializer.singleton.writeToSink(comment.isApproved(), sink);
  }

  @Override
  public Comment readFromSource(ByteSource source) {
    return new Comment.Builder()
        .setUuid(StringSerializer.singleton.readFromSource(source))
        .setCreatedAt(DateSerializer.singleton.readFromSource(source))
        .setAuthorName(StringSerializer.singleton.readFromSource(source))
        .setAuthorEmail(optStringSerializer.readFromSource(source))
        .setContent(StringSerializer.singleton.readFromSource(source))
        .setApproved(BooleanSerializer.singleton.readFromSource(source))
        .build();
  }
}
