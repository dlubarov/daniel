package daniel.blog;

import daniel.blog.comment.Comment;
import daniel.blog.comment.CommentStorage;
import daniel.blog.post.Post;
import daniel.blog.post.PostFormatter;
import daniel.data.function.Function;
import daniel.data.option.Option;
import daniel.data.sequence.Sequence;
import daniel.web.html.Element;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.HttpStatus;
import daniel.web.http.server.Handler;
import daniel.web.http.server.HttpResponseFactory;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

final class ViewPostHandler implements Handler {
  private static final DateFormat dateFormat = new SimpleDateFormat("MMMMM d, yyyy");

  private final Post post;

  ViewPostHandler(Post post) {
    this.post = post;
  }

  @Override
  public HttpResponse handle(HttpRequest request) {
    Sequence<Comment> comments = CommentStorage.getCommentsByPost(post.getUuid())
        .filter(new Function<Comment, Boolean>() {
          @Override public Boolean apply(Comment comment) {
            return comment.isApproved();
          }
        })
        .sorted(Comment.ASCENDING_CREATED_AT_ORDERING);
    Element document = Layout.createDocument(request,
        Option.some(post.getSubject()),
        Option.some(formatDate(post.getCreatedAt().toDate())),
        PostFormatter.full(post, comments));
    return HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document);
  }

  private static String formatDate(Date date) {
    synchronized (dateFormat) {
      return dateFormat.format(date);
    }
  }
}
