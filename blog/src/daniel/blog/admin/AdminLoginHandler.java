package daniel.blog.admin;

import daniel.blog.Layout;
import daniel.blog.MiscStorage;
import daniel.data.option.Option;
import daniel.data.unit.Duration;
import daniel.data.unit.Instant;
import daniel.web.html.Attribute;
import daniel.web.html.Xhtml5Document;
import daniel.web.html.Element;
import daniel.web.html.Tag;
import daniel.web.html.TextNode;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.HttpStatus;
import daniel.web.http.cookies.Cookie;
import daniel.web.http.cookies.CookieManager;
import daniel.web.http.server.Handler;
import daniel.web.http.server.HttpResponseFactory;

final class AdminLoginHandler implements Handler {
  public AdminLoginHandler() {}

  private static final long EXPIRATION_DAYS = 2;

  @Override
  public HttpResponse handle(HttpRequest request) {
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
    if (adminPassword.equals(MiscStorage.tryGetAdminPassword().getOrThrow())) {
      Cookie cookie = new Cookie.Builder()
          .setName("admin_password")
          .setValue(adminPassword)
          .setExpires(Instant.now().plus(Duration.fromDays(EXPIRATION_DAYS)))
          .build();
      CookieManager.setCooke(cookie);
      Element document = Layout.createDocument(request,
          Option.some("Success"), Option.<String>none(),
          TextNode.escapedText("You have been signed in."));
      return HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document);
    } else {
      Element document = Layout.createDocument(request,
          Option.some("Oops"), Option.<String>none(),
          TextNode.escapedText("Wrong password."));
      return HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document);
    }
  }

  private HttpResponse handleGet(HttpRequest request) {
    Element document = Layout.createDocument(request,
        Option.some("Admin Login"), Option.<String>none(),
        new Element.Builder(Tag.FORM)
            .setAttribute(Attribute.ACTION, "admin")
            .setAttribute(Attribute.METHOD, "post")
            .addChild(TextNode.escapedText("Password:"))
            .addChild(new Element(Tag.BR))
            .addChild(new Element.Builder(Tag.INPUT)
                .setAttribute(Attribute.TYPE, "password")
                .setAttribute(Attribute.NAME, "admin_password")
                .build())
            .addChild(new Element(Tag.BR))
            .addChild(new Element.Builder(Tag.INPUT)
                .setAttribute(Attribute.TYPE, "submit").build())
            .build());
    return HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document);
  }
}
