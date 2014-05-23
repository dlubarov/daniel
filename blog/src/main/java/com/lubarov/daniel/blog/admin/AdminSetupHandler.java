package com.lubarov.daniel.blog.admin;

import com.lubarov.daniel.blog.Layout;
import com.lubarov.daniel.blog.MiscStorage;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.unit.Instant;
import com.lubarov.daniel.data.util.Check;
import com.lubarov.daniel.web.html.*;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpResponse;
import com.lubarov.daniel.web.http.HttpStatus;
import com.lubarov.daniel.web.http.server.Handler;
import com.lubarov.daniel.web.http.server.util.HttpResponseFactory;

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
        Option.some("Success"), Option.<Instant>none(),
        new ParagraphBuilder().addEscapedText("Password has been set.").build());
    return HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document);
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

    Element document = Layout.createDocument(request,
        Option.some("Admin Setup"), Option.<Instant>none(), content);
    return HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document);
  }
}
