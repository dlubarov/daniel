package com.lubarov.daniel.blog.admin;

import com.lubarov.daniel.blog.Layout;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.unit.Instant;
import com.lubarov.daniel.web.html.AnchorBuilder;
import com.lubarov.daniel.web.html.Element;
import com.lubarov.daniel.web.html.ParagraphBuilder;
import com.lubarov.daniel.web.html.Tag;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpResponse;
import com.lubarov.daniel.web.http.HttpStatus;
import com.lubarov.daniel.web.http.server.PartialHandler;
import com.lubarov.daniel.web.http.server.util.HttpResponseFactory;

final class AdminDashboardHandler implements PartialHandler {
  public static final AdminDashboardHandler singleton = new AdminDashboardHandler();

  private AdminDashboardHandler() {}

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    if (!request.getResource().equals("/admin"))
      return Option.none();

    Element aCreatePost = new AnchorBuilder()
        .setHref("admin/create-post")
        .addEscapedText("Create a new post")
        .build();
    Element aReviewComments = new AnchorBuilder()
        .setHref("admin/review-comments")
        .addEscapedText("Review comments")
        .build();

    Element linkList = new Element.Builder(Tag.UL)
        .addChild(new Element(Tag.LI, aCreatePost))
        .addChild(new Element(Tag.LI, aReviewComments))
        .build();
    Element whatsUp = new ParagraphBuilder()
        .addEscapedText("What's up?")
        .build();

    Element document = Layout.createDocument(
        request, Option.some("Admin Control Panel"),
        Option.none(), whatsUp, linkList);
    return Option.some(HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document));
  }
}
