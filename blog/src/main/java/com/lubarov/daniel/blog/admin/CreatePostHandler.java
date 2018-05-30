package com.lubarov.daniel.blog.admin;

import com.lubarov.daniel.blog.Config;
import com.lubarov.daniel.blog.Layout;
import com.lubarov.daniel.blog.post.Post;
import com.lubarov.daniel.blog.post.PostStorage;
import com.lubarov.daniel.blog.post.PostUrlFactory;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.unit.Instant;
import com.lubarov.daniel.web.html.Attribute;
import com.lubarov.daniel.web.html.Element;
import com.lubarov.daniel.web.html.Tag;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpResponse;
import com.lubarov.daniel.web.http.HttpStatus;
import com.lubarov.daniel.web.http.server.PartialHandler;
import com.lubarov.daniel.web.http.server.util.HttpResponseFactory;

final class CreatePostHandler implements PartialHandler {
  public static final CreatePostHandler singleton = new CreatePostHandler();

  private CreatePostHandler() {}

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    if (!request.getResource().equals("/admin/create-post")) {
      return Option.none();
    }

    switch (request.getMethod()) {
      case GET:
      case HEAD:
        return Option.some(handleGet(request));
      case POST:
        return Option.some(handlePost(request));
      default:
        return Option.none();
    }
  }

  private static HttpResponse handleGet(HttpRequest request) {
    Element subject = new Element.Builder(Tag.INPUT)
        .setRawAttribute(Attribute.NAME, "subject")
        .setRawAttribute(Attribute.TYPE, "text")
        .setRawAttribute(Attribute.CLASS, "wide")
        .setRawAttribute(Attribute.STYLE, "margin-bottom: 1em")
        .build();
    Element content = new Element.Builder(Tag.TEXTAREA)
        .setRawAttribute(Attribute.NAME, "content")
        .setRawAttribute(Attribute.CLASS, "wide")
        .setRawAttribute(Attribute.ROWS, "30")
        .build();
    Element submit = new Element.Builder(Tag.INPUT)
        .setRawAttribute(Attribute.TYPE, "submit")
        .setRawAttribute(Attribute.VALUE, "Create Post")
        .setRawAttribute(Attribute.STYLE, "display: block; margin: 0px auto;")
        .build();
    Element form = new Element.Builder(Tag.FORM)
        .setRawAttribute(Attribute.ACTION, "admin/create-post")
        .setRawAttribute(Attribute.METHOD, "post")
        .addChild(subject)
        .addChild(content)
        .addChild(new Element(Tag.BR))
        .addChild(submit)
        .build();

    Element document = Layout.createDocument(request,
        Option.some("Create a Post"), Option.none(), form);
    return HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document);
  }

  private static HttpResponse handlePost(HttpRequest request) {
    String subject = request.getUrlencodedPostData().getValues("subject")
        .tryGetOnlyElement().getOrThrow();
    String content = request.getUrlencodedPostData().getValues("content")
        .tryGetOnlyElement().getOrThrow();

    Post post = new Post.Builder()
        .setRandomUiid()
        .setCreatedAt(Instant.now())
        .setSubject(subject)
        .setContent(content)
        .setPublished(true)
        .build();
    PostStorage.saveNewPost(post);

    String location = String.format("%s/%s",
        Config.getBaseUrl(), PostUrlFactory.getViewUrl(post));
    return HttpResponseFactory.redirectToGet(location);
  }
}
