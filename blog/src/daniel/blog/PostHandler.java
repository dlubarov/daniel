package daniel.blog;

import daniel.blog.post.Post;
import daniel.blog.post.PostStorage;
import daniel.data.option.Option;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.server.PartialHandler;

final class PostHandler implements PartialHandler {
  public static final PostHandler singleton = new PostHandler();

  private PostHandler() {}

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    for (Post post : PostStorage.getAllPosts()) {
      String prefix = "/" + post.getUrlFriendlySubject();
      if (request.getResource().startsWith(prefix)) {
        String rest = request.getResource().substring(prefix.length());
        if (rest.isEmpty())
          return Option.some(new ViewPostHandler(post).handle(request));
        if (rest.startsWith("/edit"))
          return Option.some(new EditPostHandler(post).handle(request));
        if (rest.startsWith("/comment"))
          return Option.some(new AddCommentHandler(post).handle(request));
      }
    }
    return Option.none();
  }
}
