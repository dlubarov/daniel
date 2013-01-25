package daniel.blog;

import daniel.blog.admin.Authenticator;
import daniel.blog.comment.Comment;
import daniel.blog.comment.CommentStorage;
import daniel.blog.post.Post;
import daniel.data.option.Option;
import daniel.data.unit.Instant;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.RequestMethod;
import daniel.web.http.server.Handler;
import daniel.web.http.server.HttpResponseFactory;

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
        ? Option.<String>none()
        : Option.some(authorEmail);
    String content = request.getUrlencodedPostData().getValues("content")
        .tryGetOnlyElement().getOrThrow();

    if (authorName.isEmpty()) {
      Notifications.addMessage(request, "Name cannot be empty.");
      return getRedirectResponse();
    }
    if (content.isEmpty()) {
      Notifications.addMessage(request, "Content cannot be empty");
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
