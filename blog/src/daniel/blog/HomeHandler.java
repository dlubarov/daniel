package daniel.blog;

import daniel.blog.post.Post;
import daniel.blog.post.PostFormatter;
import daniel.data.function.Function;
import daniel.data.option.Option;
import daniel.data.sequence.Sequence;
import daniel.web.html.Document;
import daniel.web.html.Element;
import daniel.web.html.Tag;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.HttpStatus;
import daniel.web.http.server.HttpResponseFactory;
import daniel.web.http.server.PartialHandler;

public class HomeHandler implements PartialHandler {
  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    if (!request.getResource().equals("/"))
      return Option.none();

    Sequence<Element> postSummaries = Post.database.getAllValues().map(new Function<Post, Element>() {
      @Override public Element apply(Post post) {
        return PostFormatter.summary(post);
      }
    });

    Document document = Layout.createDocument(new Element(Tag.DIV, postSummaries));
    return Option.some(HttpResponseFactory.htmlResponse(HttpStatus.OK, document));
  }
}
