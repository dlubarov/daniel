package daniel.blog;

import daniel.blog.admin.Authenticator;
import daniel.blog.post.Post;
import daniel.blog.post.PostStorage;
import daniel.blog.post.PostUrlFactory;
import daniel.data.option.Option;
import daniel.data.unit.Instant;
import daniel.web.html.Attribute;
import daniel.web.html.Element;
import daniel.web.html.ParagraphBuilder;
import daniel.web.html.Tag;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.HttpStatus;
import daniel.web.http.server.Handler;
import daniel.web.http.server.util.HttpResponseFactory;

final class EditPostHandler implements Handler {
  private final Post post;

  EditPostHandler(Post post) {
    this.post = post;
  }

  @Override
  public HttpResponse handle(HttpRequest request) {
    if (!Authenticator.isAdmin(request)) {
      Element content = new ParagraphBuilder()
          .addEscapedText("You are not allowed to edit posts.")
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
        .setRawAttribute(Attribute.ACTION, post.getUrlFriendlySubject() + "/edit")
        .setRawAttribute(Attribute.METHOD, "post")
        .addChild(new Element.Builder(Tag.INPUT)
            .setRawAttribute(Attribute.NAME, "subject")
            .setRawAttribute(Attribute.TYPE, "text")
            .setEscapedAttribtue(Attribute.VALUE, post.getSubject())
            .setRawAttribute(Attribute.CLASS, "wide")
            .setRawAttribute(Attribute.STYLE, "margin-bottom: 1em")
            .build())
        .addChild(new Element.Builder(Tag.TEXTAREA)
            .setRawAttribute(Attribute.NAME, "content")
            .setRawAttribute(Attribute.CLASS, "wide")
            .setRawAttribute(Attribute.ROWS, "30")
            .addEscapedText(post.getContent())
            .build())
        .addChild(getPublishedSection())
//        .addChild(new Element(Tag.BR))
        .addChild(new Element.Builder(Tag.INPUT)
            .setRawAttribute(Attribute.TYPE, "submit")
            .setRawAttribute(Attribute.VALUE, "Update Post")
            .setRawAttribute(Attribute.STYLE, "display: block; margin: 0px auto;")
            .build())
        .build();
    Element document = Layout.createDocument(request,
        Option.some("Edit Post"), Option.<Instant>none(), form);
    return HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document);
  }

  private Element getPublishedSection() {
    Element.Builder checkboxBuilder = new Element.Builder(Tag.INPUT)
        .setRawAttribute(Attribute.TYPE, "checkbox")
        .setRawAttribute(Attribute.ID, "published")
        .setRawAttribute(Attribute.NAME, "published")
        .setRawAttribute(Attribute.VALUE, "yes");
    if (post.isPublished())
      checkboxBuilder.setRawAttribute("checked", "checked");
    Element checkbox = checkboxBuilder.build();

    Element label = new Element.Builder(Tag.LABEL)
        .setRawAttribute(Attribute.FOR, "published")
        .addEscapedText("Published")
        .build();

    return new Element.Builder(Tag.DIV)
        .addChild(checkbox)
        .addChild(label)
        .build();
  }

  private HttpResponse handlePost(HttpRequest request) {
    String subject = request.getUrlencodedPostData().getValues("subject")
        .tryGetOnlyElement().getOrThrow();
    String content = request.getUrlencodedPostData().getValues("content")
        .tryGetOnlyElement().getOrThrow();
    boolean published = !request.getUrlencodedPostData().getValues("published").isEmpty();
    Post editedPost = new Post.Builder()
        .setUuid(post.getUuid())
        .setCreatedAt(post.getCreatedAt())
        .setSubject(subject)
        .setContent(content)
        .setPublished(published)
        .build();
    PostStorage.updatePost(editedPost);

    String location = String.format("%s/%s",
        Config.getBaseUrl(), PostUrlFactory.getViewUrl(editedPost));
    return HttpResponseFactory.redirectToGet(location);
  }
}
