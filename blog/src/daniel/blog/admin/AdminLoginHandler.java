package daniel.blog.admin;

import daniel.blog.Layout;
import daniel.blog.MiscStorage;
import daniel.data.option.Option;
import daniel.data.unit.Duration;
import daniel.data.unit.Instant;
import daniel.web.html.Attribute;
import daniel.web.html.Element;
import daniel.web.html.InputBuilder;
import daniel.web.html.ParagraphBuilder;
import daniel.web.html.Tag;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.HttpStatus;
import daniel.web.http.cookies.Cookie;
import daniel.web.http.cookies.CookieManager;
import daniel.web.http.server.Handler;
import daniel.web.http.server.util.HttpResponseFactory;

final class AdminLoginHandler implements Handler {
  public AdminLoginHandler() {}

  private static final long EXPIRATION_DAYS = 30;

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
          new ParagraphBuilder().addEscapedText("You have been signed in.").build()
      );
      return HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document);
    } else {
      Element document = Layout.createDocument(request,
          Option.some("Oops"), Option.<String>none(),
          new ParagraphBuilder().addEscapedText("Wrong password.").build()
      );
      return HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document);
    }
  }

  private HttpResponse handleGet(HttpRequest request) {
    Element document = Layout.createDocument(request,
        Option.some("Admin Login"), Option.<String>none(), getForm());
    return HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document);
  }

  private Element getForm() {
    return new Element.Builder(Tag.FORM)
        .setAttribute(Attribute.ACTION, "admin")
        .setAttribute(Attribute.METHOD, "post")
        .addEscapedText("Password:")
        .addChild(new Element(Tag.BR))
        .addChild(new InputBuilder()
            .setType("password")
            .setName("admin_password")
            .build())
        .addChild(new Element(Tag.BR))
        .addChild(new InputBuilder().setType("submit").build())
        .build();
  }
}
