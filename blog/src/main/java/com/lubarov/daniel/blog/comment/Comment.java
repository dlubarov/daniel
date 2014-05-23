package com.lubarov.daniel.blog.comment;

import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.order.AbstractOrdering;
import com.lubarov.daniel.data.order.Ordering;
import com.lubarov.daniel.data.order.Relation;
import com.lubarov.daniel.data.unit.Instant;
import com.lubarov.daniel.web.util.UuidUtils;

public final class Comment {
  public static final Ordering<Comment> ASCENDING_CREATED_AT_ORDERING =
      new AbstractOrdering<Comment>() {
        @Override public Relation compare(Comment a, Comment b) {
          return Instant.ASCENDING_ORDERING.compare(a.getCreatedAt(), b.getCreatedAt());
        }
      };

  public static final Ordering<Comment> DESCENDING_CREATED_AT_ORDERING =
      ASCENDING_CREATED_AT_ORDERING.reverse();

  public static final class Builder {
    private Option<String> uuid = Option.none();
    private Option<String> postUuid = Option.none();
    private Option<Instant> createdAt = Option.none();
    private Option<String> authorName = Option.none();
    private Option<String> authorEmail = Option.none();
    private Option<String> content = Option.none();
    private Option<Boolean> approved = Option.none();

    public Builder() {}

    public Builder(Comment comment) {
      uuid = Option.some(comment.uuid);
      postUuid = Option.some(comment.postUuid);
      createdAt = Option.some(comment.createdAt);
      authorName = Option.some(comment.authorName);
      authorEmail = comment.authorEmail;
      content = Option.some(comment.content);
      approved = Option.some(comment.approved);
    }

    public Builder setUuid(String uuid) {
      this.uuid = Option.some(uuid);
      return this;
    }

    public Builder setPostUuid(String postUuid) {
      this.postUuid = Option.some(postUuid);
      return this;
    }

    public Builder setRandomUiid() {
      return setUuid(UuidUtils.randomAlphanumericUuid());
    }

    public Builder setCreatedAt(Instant createdAt) {
      this.createdAt = Option.some(createdAt);
      return this;
    }

    public Builder setAuthorName(String authorName) {
      this.authorName = Option.some(authorName);
      return this;
    }

    public Builder setAuthorEmail(String authorEmail) {
      return setAuthorEmail(Option.some(authorEmail));
    }

    public Builder setAuthorEmail(Option<String> authorEmail) {
      this.authorEmail = authorEmail;
      return this;
    }

    public Builder setContent(String content) {
      this.content = Option.some(content);
      return this;
    }

    public Builder setApproved(boolean approved) {
      this.approved = Option.some(approved);
      return this;
    }

    public Comment build() {
      return new Comment(this);
    }
  }

  private final String uuid;
  private final String postUuid;
  private final Instant createdAt;
  private final String authorName;
  private final Option<String> authorEmail;
  private final String content;
  private final boolean approved;

  private Comment(Builder builder) {
    uuid = builder.uuid.getOrDefault("No UUID was set.");
    postUuid = builder.postUuid.getOrThrow("No post UUID was set.");
    createdAt = builder.createdAt.getOrThrow("No created at date was set.");
    authorName = builder.authorName.getOrThrow("No author name was set.");
    authorEmail = builder.authorEmail;
    content = builder.content.getOrThrow("No content was set.");
    approved = builder.approved.getOrThrow("Approved was not set.");
  }

  public String getUuid() {
    return uuid;
  }

  public String getPostUuid() {
    return postUuid;
  }

  public Instant getCreatedAt() {
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

  public boolean isApproved() {
    return approved;
  }
}
