package daniel.blog;

import daniel.blog.post.Post;
import daniel.blog.post.PostFormatter;
import daniel.data.option.Option;
import daniel.web.html.Document;
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
    Document document = Layout.createDocument(
        Option.some(post.getSubject()),
        Option.some(formatDate(post.getCreatedAt())),
        PostFormatter.full(post));
    return HttpResponseFactory.htmlResponse(HttpStatus.OK, document);
  }

  private static String formatDate(Date date) {
    synchronized (dateFormat) {
      return dateFormat.format(date);
    }
  }
}
