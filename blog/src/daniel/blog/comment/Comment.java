package daniel.blog.comment;

import daniel.data.option.Option;
import java.util.Date;
import java.util.UUID;

public final class Comment {
  public static class Builder {
    private Option<String> uuid = Option.none();
    private Option<Date> createdAt = Option.none();
    private Option<String> authorName = Option.none();
    private Option<String> authorEmail = Option.none();
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

    public Builder setAuthorName(String authorName) {
      this.authorName = Option.some(authorName);
      return this;
    }

    public Builder setAuthorEmail(String authorEmail) {
      this.authorEmail = Option.some(authorEmail);
      return this;
    }

    public Builder setContent(String content) {
      this.content = Option.some(content);
      return this;
    }

    public Comment build() {
      return new Comment(this);
    }
  }

  private final String uuid;
  private final Date createdAt;
  private final String authorName;
  private final Option<String> authorEmail;
  private final String content;

  private Comment(Builder builder) {
    uuid = builder.uuid.getOrDefault("No uuid was set.");
    createdAt = builder.createdAt.getOrThrow("No created at date was set.");
    authorName = builder.authorName.getOrThrow("No author name was set.");
    authorEmail = builder.authorEmail;
    content = builder.content.getOrThrow("No content was set.");
  }

  public String getUuid() {
    return uuid;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public String getAuthorName() {
    return authorName;
  }

  public Option<String> getAuthorEmail() {
    return authorEmail;
  }

  public String getContent() {
    return content;
  }
}
