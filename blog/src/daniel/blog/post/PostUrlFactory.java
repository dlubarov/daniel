package daniel.blog.post;

public final class PostUrlFactory {
  private PostUrlFactory() {}

  public static String getViewUrl(Post post) {
    return post.getUrlFriendlySubject();
  }
}
