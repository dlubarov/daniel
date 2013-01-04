package daniel.blog.comment;

import daniel.data.option.Option;
import java.util.Date;

public class Comment {
  public static class Builder {
    private Option<Date> createdAt;
    private Option<String> content;

    public Builder setCreatedAt(Date createdAt) {
      this.createdAt = Option.some(createdAt);
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

  private final Date createdAt;
  private final String content;

  private Comment(Builder builder) {
    createdAt = builder.createdAt.getOrThrow("No created at date was set.");
    content = builder.content.getOrThrow("No content was set.");
  }
}
