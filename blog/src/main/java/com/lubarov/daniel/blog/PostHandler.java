package com.lubarov.daniel.blog;

import com.lubarov.daniel.blog.post.Post;
import com.lubarov.daniel.blog.post.PostStorage;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpResponse;
import com.lubarov.daniel.web.http.server.PartialHandler;

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
        if (rest.startsWith("/delete"))
          return Option.some(new DeletePostHandler(post).handle(request));
      }
    }
    return Option.none();
  }
}
