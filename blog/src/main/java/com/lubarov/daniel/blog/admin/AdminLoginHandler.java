package com.lubarov.daniel.blog.admin;

import com.lubarov.daniel.blog.Layout;
import com.lubarov.daniel.blog.MiscStorage;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.unit.Duration;
import com.lubarov.daniel.data.unit.Instant;
import com.lubarov.daniel.web.html.*;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpResponse;
import com.lubarov.daniel.web.http.HttpStatus;
import com.lubarov.daniel.web.http.cookies.Cookie;
import com.lubarov.daniel.web.http.cookies.CookieManager;
import com.lubarov.daniel.web.http.server.Handler;
import com.lubarov.daniel.web.http.server.util.HttpResponseFactory;

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
          Option.some("Success"), Option.<Instant>none(),
          new ParagraphBuilder().addEscapedText("You have been signed in.").build()
      );
      return HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document);
    } else {
      Element document = Layout.createDocument(request,
          Option.some("Oops"), Option.<Instant>none(),
          new ParagraphBuilder().addEscapedText("Wrong password.").build()
      );
      return HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document);
    }
  }

  private HttpResponse handleGet(HttpRequest request) {
    Element document = Layout.createDocument(request,
        Option.some("Admin Login"), Option.<Instant>none(), getForm());
    return HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document);
  }

  private Element getForm() {
    Element password = new InputBuilder()
        .setType("password")
        .setName("admin_password")
        .build();
    Element submit = new InputBuilder()
        .setType("submit")
        .build();

    return new Element.Builder(Tag.FORM)
        .setRawAttribute(Attribute.ACTION, "admin")
        .setRawAttribute(Attribute.METHOD, "post")
        .addEscapedText("Password:")
        .addChild(new Element(Tag.BR))
        .addChild(password)
        .addChild(new Element(Tag.BR))
        .addChild(submit)
        .build();
  }
}
