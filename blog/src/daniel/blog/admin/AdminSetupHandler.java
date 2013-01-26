package daniel.blog.admin;

import daniel.blog.Layout;
import daniel.blog.MiscStorage;
import daniel.data.option.Option;
import daniel.data.util.Check;
import daniel.web.html.Attribute;
import daniel.web.html.Xhtml5Document;
import daniel.web.html.Element;
import daniel.web.html.Tag;
import daniel.web.html.TextNode;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.HttpStatus;
import daniel.web.http.server.Handler;
import daniel.web.http.server.HttpResponseFactory;

final class AdminSetupHandler implements Handler {
  public static final AdminSetupHandler singleton = new AdminSetupHandler();

  private AdminSetupHandler() {}

  @Override
  public HttpResponse handle(HttpRequest request) {
    Check.that(MiscStorage.tryGetAdminPassword().isEmpty(),
        "Admin password already set.");

    switch (request.getMethod()) {
      case GET:
      case HEAD:
        return handleGet(request);
      case POST:
        return handlePost(request);
      default:
        throw new RuntimeException();
    }
  }

  private HttpResponse handlePost(HttpRequest request) {
    String adminPassword = request.getUrlencodedPostData().getValues("admin_password")
        .tryGetOnlyElement().getOrThrow("Expected exactly one password in post data.");
    MiscStorage.setAdminPassword(adminPassword);
    Element document = Layout.createDocument(request,
        Option.some("Success"), Option.<String>none(),
        TextNode.escapedText("Password has been set."));
    return HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document);
  }

  private HttpResponse handleGet(HttpRequest request) {
    Element document = Layout.createDocument(
        request,
        Option.some("Admin Setup"),
        Option.<String>none(),
        new Element.Builder(Tag.FORM)
            .setAttribute(Attribute.ACTION, "admin")
            .setAttribute(Attribute.METHOD, "post")
            .addChild(TextNode.escapedText("Create Admin Password:"))
            .addChild(new Element(Tag.BR))
            .addChild(new Element.Builder(Tag.INPUT)
                .setAttribute(Attribute.TYPE, "password")
                .setAttribute(Attribute.NAME, "admin_password")
                .build())
            .addChild(new Element(Tag.BR))
            .addChild(new Element.Builder(Tag.INPUT)
                .setAttribute(Attribute.TYPE, "submit")
                .build())
            .build());
    return HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document);
  }
}
