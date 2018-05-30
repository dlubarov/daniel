package com.lubarov.daniel.blog;

import com.lubarov.daniel.blog.admin.Authenticator;
import com.lubarov.daniel.blog.comment.Comment;
import com.lubarov.daniel.blog.comment.CommentStorage;
import com.lubarov.daniel.blog.post.Post;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.unit.Instant;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpResponse;
import com.lubarov.daniel.web.http.RequestMethod;
import com.lubarov.daniel.web.http.server.Handler;
import com.lubarov.daniel.web.http.server.util.HttpResponseFactory;

final class AddCommentHandler implements Handler {
  private final Post post;

  AddCommentHandler(Post post) {
    this.post = post;
  }

  @Override
  public HttpResponse handle(HttpRequest request) {
    if (request.getMethod() != RequestMethod.POST)
      throw new RuntimeException("Unexpected method: " + request.getMethod());

    String authorName = request.getUrlencodedPostData().getValues("author_name")
        .tryGetOnlyElement().getOrThrow();
    String authorEmail = request.getUrlencodedPostData().getValues("author_email")
        .tryGetOnlyElement().getOrThrow();
    Option<String> optAuthorEmail = authorEmail.isEmpty()
        ? Option.none()
        : Option.some(authorEmail);
    String content = request.getUrlencodedPostData().getValues("content")
        .tryGetOnlyElement().getOrThrow();
    String challenge = request.getUrlencodedPostData().getValues("challenge")
        .tryGetOnlyElement().getOrThrow();

    if (authorName.isEmpty()) {
      Notifications.addMessage(request, "Name cannot be empty.");
      return getRedirectResponse();
    }
    if (content.isEmpty()) {
      Notifications.addMessage(request, "Content cannot be empty.");
      return getRedirectResponse();
    }
    if (challenge.isEmpty()) {
      Notifications.addMessage(request,
          "Please answer \"What do cows drink?\" to show that you're not a generic bot.");
      return getRedirectResponse();
    }
    if (!(challenge.equalsIgnoreCase("milk") || challenge.equalsIgnoreCase("water"))) {
      Notifications.addMessage(request, "No, that's now what cows drink! Comment rejected!");
      return getRedirectResponse();
    }

    Comment comment = new Comment.Builder()
        .setRandomUiid()
        .setPostUuid(post.getUuid())
        .setCreatedAt(Instant.now())
        .setAuthorName(authorName)
        .setAuthorEmail(optAuthorEmail)
        .setContent(content)
        .setApproved(Authenticator.isAdmin(request))
        .build();
    CommentStorage.saveNewComment(comment);
    Notifications.addMessage(request,
        "Your comment has been placed in a moderation queue and should appear shortly.");
    return getRedirectResponse();
  }

  private HttpResponse getRedirectResponse() {
    String location = String.format("%s/%s", Config.getBaseUrl(), post.getUrlFriendlySubject());
    return HttpResponseFactory.redirectToGet(location);
  }
}
