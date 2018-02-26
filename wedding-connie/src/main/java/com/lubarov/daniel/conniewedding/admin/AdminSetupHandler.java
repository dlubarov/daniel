package com.lubarov.daniel.conniewedding.admin;

import com.lubarov.daniel.conniewedding.MiscStorage;
import com.lubarov.daniel.conniewedding.WeddingLayout;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.unit.Duration;
import com.lubarov.daniel.data.unit.Instant;
import com.lubarov.daniel.data.util.Check;
import com.lubarov.daniel.web.html.Attribute;
import com.lubarov.daniel.web.html.Element;
import com.lubarov.daniel.web.html.Node;
import com.lubarov.daniel.web.html.ParagraphBuilder;
import com.lubarov.daniel.web.html.Tag;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpResponse;
import com.lubarov.daniel.web.http.HttpStatus;
import com.lubarov.daniel.web.http.cookies.Cookie;
import com.lubarov.daniel.web.http.cookies.CookieManager;
import com.lubarov.daniel.web.http.server.Handler;
import com.lubarov.daniel.web.http.server.util.HttpResponseFactory;

final class AdminSetupHandler implements Handler {
  public static final AdminSetupHandler singleton = new AdminSetupHandler();

  private static final long EXPIRATION_DAYS = 30;

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
    Cookie cookie = new Cookie.Builder()
        .setName("admin_password")
        .setValue(adminPassword)
        .setExpires(Instant.now().plus(Duration.fromDays(EXPIRATION_DAYS)))
        .build();
    CookieManager.setCooke(cookie);
    return HttpResponseFactory.redirectToGet("/admin");
  }

  private HttpResponse handleGet(HttpRequest request) {
    Element passwordInput = new Element.Builder(Tag.INPUT)
        .setRawAttribute(Attribute.TYPE, "password")
        .setRawAttribute(Attribute.NAME, "admin_password")
        .build();
    Element submit = new Element.Builder(Tag.INPUT)
        .setRawAttribute(Attribute.TYPE, "submit")
        .build();
    Node content = new Element.Builder(Tag.FORM)
        .setRawAttribute(Attribute.ACTION, "admin")
        .setRawAttribute(Attribute.METHOD, "post")
        .addEscapedText("Create Admin Password:")
        .addChild(new Element(Tag.BR))
        .addChild(passwordInput)
        .addChild(new Element(Tag.BR))
        .addChild(submit)
        .build();

    Element document = WeddingLayout.createDocument(Option.some("Admin Setup"), content);
    return HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document);
  }
}
