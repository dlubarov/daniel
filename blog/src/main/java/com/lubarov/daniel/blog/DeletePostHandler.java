package com.lubarov.daniel.blog;

import com.lubarov.daniel.blog.admin.Authenticator;
import com.lubarov.daniel.blog.post.Post;
import com.lubarov.daniel.blog.post.PostStorage;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.unit.Instant;
import com.lubarov.daniel.web.html.Attribute;
import com.lubarov.daniel.web.html.Element;
import com.lubarov.daniel.web.html.ParagraphBuilder;
import com.lubarov.daniel.web.html.Tag;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpResponse;
import com.lubarov.daniel.web.http.HttpStatus;
import com.lubarov.daniel.web.http.server.Handler;
import com.lubarov.daniel.web.http.server.util.HttpResponseFactory;

final class DeletePostHandler implements Handler {
  private final Post post;

  DeletePostHandler(Post post) {
    this.post = post;
  }

  @Override
  public HttpResponse handle(HttpRequest request) {
    if (!Authenticator.isAdmin(request)) {
      Element content = new ParagraphBuilder()
          .addEscapedText("You are not allowed to delete posts.")
          .build();
      Element html = Layout.createDocument(request,
          Option.some("Forbidden"), Option.<Instant>none(), content);
      return HttpResponseFactory.xhtmlResponse(HttpStatus.FORBIDDEN, html);
    }

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

  private HttpResponse handleGet(HttpRequest request) {
    Element form = new Element.Builder(Tag.FORM)
        .setRawAttribute(Attribute.ACTION, post.getUrlFriendlySubject() + "/delete")
        .setRawAttribute(Attribute.METHOD, "post")
        .addChild(new ParagraphBuilder().addEscapedText("Really delete this post?").build())
        .addChild(new Element.Builder(Tag.INPUT)
            .setRawAttribute(Attribute.TYPE, "submit")
            .setRawAttribute(Attribute.VALUE, "Delete Post")
            .setRawAttribute(Attribute.STYLE, "display: block; margin: 0px auto;")
            .build())
        .build();
    Element document = Layout.createDocument(request,
        Option.some("Delete Post"), Option.<Instant>none(), form);
    return HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document);
  }

  private HttpResponse handlePost(HttpRequest request) {
    PostStorage.deletePost(post);
    Notifications.addMessage(request, "The post has been deleted.");
    return HttpResponseFactory.redirectToGet(Config.getBaseUrl());
  }
}
