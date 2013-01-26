package daniel.blog.admin;

import daniel.blog.Layout;
import daniel.data.option.Option;
import daniel.web.html.Attribute;
import daniel.web.html.Element;
import daniel.web.html.Tag;
import daniel.web.html.Xhtml5Document;
import daniel.web.html.TextNode;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.HttpStatus;
import daniel.web.http.server.HttpResponseFactory;
import daniel.web.http.server.PartialHandler;

final class AdminDashboardHandler implements PartialHandler {
  public static final AdminDashboardHandler singleton = new AdminDashboardHandler();

  private AdminDashboardHandler() {}

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    if (!request.getResource().equals("/admin"))
      return Option.none();

    Element aCreatePost = new Element.Builder(Tag.A)
        .setAttribute(Attribute.HREF, "admin/create-post")
        .addChild(TextNode.escapedText("Create a new post"))
        .build();
    Element aReviewComments = new Element.Builder(Tag.A)
        .setAttribute(Attribute.HREF, "admin/review-comments")
        .addChild(TextNode.escapedText("Review comments"))
        .build();

    Element linkList = new Element.Builder(Tag.UL)
        .addChild(new Element(Tag.LI, aCreatePost))
        .addChild(new Element(Tag.LI, aReviewComments))
        .build();
    Element whatsUp = new Element(Tag.P, TextNode.escapedText("What's up?"));

    Element document = Layout.createDocument(
        request, Option.some("Admin Control Panel"),
        Option.<String>none(), whatsUp, linkList);
    return Option.some(HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document));
  }
}
