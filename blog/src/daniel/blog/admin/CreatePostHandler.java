package daniel.blog.admin;

import daniel.blog.Config;
import daniel.blog.Layout;
import daniel.blog.post.Post;
import daniel.blog.post.PostStorage;
import daniel.blog.post.PostUrlFactory;
import daniel.data.option.Option;
import daniel.data.unit.Instant;
import daniel.web.html.Attribute;
import daniel.web.html.Element;
import daniel.web.html.Tag;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.HttpStatus;
import daniel.web.http.server.PartialHandler;
import daniel.web.http.server.util.HttpResponseFactory;

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
        Option.some("Create a Post"), Option.<Instant>none(), form);
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
