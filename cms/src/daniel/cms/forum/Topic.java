package daniel.cms.forum;

import daniel.data.option.Option;
import daniel.data.unit.Instant;
import daniel.data.util.ToStringBuilder;
import daniel.web.util.UuidUtils;

public final class Topic {
  public static final class Builder {
    private Option<String> uuid = Option.none();
    private Option<String> creatorUuid = Option.none();
    private Option<Instant> createdAt = Option.none();
    private Option<String> subject = Option.none();
    private Option<String> content = Option.none();

    public Builder setUuid(String uuid) {
      this.uuid = Option.some(uuid);
      return this;
    }

    public Builder setRandomUiid() {
      return setUuid(UuidUtils.randomAlphanumericUuid());
    }

    public Builder setCreatorUuid(String creatorUuid) {
      this.creatorUuid = Option.some(creatorUuid);
      return this;
    }

    public Builder setCreatedAt(Instant createdAt) {
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

    public Topic build() {
      return new Topic(this);
    }
  }

  private final String uuid;
  private final String creatorUuid;
  private final Instant createdAt;
  private final String subject;
  private final String content;

  private Topic(Builder builder) {
    uuid = builder.uuid.getOrThrow();
    creatorUuid = builder.creatorUuid.getOrThrow();
    createdAt = builder.createdAt.getOrThrow();
    subject = builder.subject.getOrThrow();
    content = builder.content.getOrThrow();
  }

  public String getUuid() {
    return uuid;
  }

  public String getCreatorUuid() {
    return creatorUuid;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public String getSubject() {
    return subject;
  }

  public String getContent() {
    return content;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("uuid", uuid)
        .append("creatorUuid", creatorUuid)
        .append("createdAt", createdAt)
        .append("subject", subject)
        .append("content", content)
        .toString();
  }
}
