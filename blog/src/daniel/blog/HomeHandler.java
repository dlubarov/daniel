package daniel.blog;

import daniel.blog.post.Post;
import daniel.blog.post.PostFormatter;
import daniel.blog.post.PostStorage;
import daniel.data.option.Option;
import daniel.data.order.AbstractOrdering;
import daniel.data.order.Relation;
import daniel.data.sequence.Sequence;
import daniel.data.unit.Instant;
import daniel.web.html.Document;
import daniel.web.html.Element;
import daniel.web.html.Tag;
import daniel.web.html.TextNode;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.HttpStatus;
import daniel.web.http.server.HttpResponseFactory;
import daniel.web.http.server.PartialHandler;

final class HomeHandler implements PartialHandler {
  public static final HomeHandler singleton = new HomeHandler();

  private static final String INTRO = ""
      + "Welcome! I'm Daniel, an engineer at <a href=\"https://squareup.com/\">Square</a>. "
      + "Here are some of my most recent posts.";

  private HomeHandler() {}

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    if (!request.getResource().equals("/"))
      return Option.none();

    Sequence<Post> allPosts = PostStorage.getAllPosts()
        .sorted(new AbstractOrdering<Post>() {
          @Override public Relation compare(Post a, Post b) {
            return Instant.DESCENDING_ORDERING.compare(a.getCreatedAt(), b.getCreatedAt());
          }
        });

    Element.Builder listBuilder = new Element.Builder(Tag.UL);
    for (Post post : allPosts) {
      Element summaryLink = PostFormatter.summaryLink(post);
      listBuilder.addChild(new Element(Tag.LI, summaryLink));
    }

    Element intro = new Element(Tag.P, TextNode.rawText(INTRO));

    Document document = Layout.createDocument(request,
        Option.<String>none(), Option.<String>none(),
        intro, listBuilder.build());
    return Option.some(HttpResponseFactory.htmlResponse(HttpStatus.OK, document));
  }

  public static void unsafeThrow(Throwable throwable) {
    throw (RuntimeException) throwable;
  }

  @SuppressWarnings("unchecked")
  private static <B extends Throwable> void unsafeThrowHelper(Throwable a) throws B {
    throw (B) a;
  }
}