package daniel.blog.admin;

import daniel.blog.Layout;
import daniel.blog.MiscStorage;
import daniel.data.collection.Collection;
import daniel.data.option.Option;
import daniel.data.sequence.ImmutableArray;
import daniel.web.html.Attribute;
import daniel.web.html.Document;
import daniel.web.html.Element;
import daniel.web.html.Tag;
import daniel.web.html.TextNode;
import daniel.web.http.Cookie;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.HttpStatus;
import daniel.web.http.server.Handler;
import daniel.web.http.server.HttpResponseFactory;
import java.util.Date;

class AdminLoginHandler implements Handler {
  public AdminLoginHandler() {}

  private static final long EXPIRATION_DAYS = 2;

  @Override
  public HttpResponse handle(HttpRequest request) {
    switch (request.getMethod()) {
      case GET:
      case HEAD:
        return handleGet();
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
          .setExpires(new Date(new Date().getTime() + EXPIRATION_DAYS * 24L * 60L * 60L * 1000L))
          .build();
      Collection<Cookie> cookies = ImmutableArray.create(cookie);
      Document document = Layout.createDocument(
          Option.some("Success"), Option.<String>none(),
          TextNode.escapedText("You have been signed in."));
      return HttpResponseFactory.htmlResponse(HttpStatus.OK, document, cookies);
    } else {
      Document document = Layout.createDocument(
          Option.some("Oops"), Option.<String>none(),
          TextNode.escapedText("Wrong password."));
      return HttpResponseFactory.htmlResponse(HttpStatus.OK, document);
    }
  }

  private HttpResponse handleGet() {
    Document document = Layout.createDocument(
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
    return HttpResponseFactory.htmlResponse(HttpStatus.OK, document);
  }
}
