package daniel.blog.post;

import daniel.bdb.SerializingDatabase;
import daniel.blog.Config;
import daniel.data.option.Option;
import daniel.data.serialization.StringSerializer;
import java.util.Date;
import java.util.UUID;

public class Post {
  public static final SerializingDatabase<String, Post> database = new SerializingDatabase<>(
      Config.getDatabaseHome("posts"), StringSerializer.singleton, PostSerializer.singleton);

  public static class Builder {
    private Option<String> uuid = Option.none();
    private Option<Date> createdAt = Option.none();
    private Option<String> subject = Option.none();
    private Option<String> content = Option.none();

    public Builder setUuid(String uuid) {
      this.uuid = Option.some(uuid);
      return this;
    }

    public Builder setRandomUiid() {
      return setUuid(UUID.randomUUID().toString());
    }

    public Builder setCreatedAt(Date createdAt) {
      this.createdAt = Option.some(createdAt);
      return this;
    }

    public Builder setSubject(String subject) {
      this.subject = Option.some(subject);
      return this;
    }

    public Builder setContent(String content) {
      this.content = Option.some(content);
      return this;
    }

    public Post build() {
      return new Post(this);
    }
  }

  private final String uuid;
  private final Date createdAt;
  private final String subject;
  private final String content;

  private Post(Builder builder) {
    uuid = builder.uuid.getOrThrow("No UUID was set.");
    createdAt = builder.createdAt.getOrThrow("No created at date was set.");
    subject = builder.subject.getOrThrow("No subject was set.");
    content = builder.content.getOrThrow("No content was set.");
  }

  public String getUuid() {
    return uuid;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public String getSubject() {
    return subject;
  }

  public String getContent() {
    return content;
  }
}
