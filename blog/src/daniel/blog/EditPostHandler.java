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
        .setAttribute(Attribute.ACTION, post.getUrlFriendlySubject() + "/edit")
        .setAttribute(Attribute.METHOD, "post")
        .addChild(new Element.Builder(Tag.INPUT).setAttribute(Attribute.NAME, "subject")
            .setAttribute(Attribute.TYPE, "text")
            .setEscapedAttribtue(Attribute.VALUE, post.getSubject())
            .setAttribute(Attribute.CLASS, "wide")
            .setAttribute(Attribute.STYLE, "margin-bottom: 1em")
            .build())
        .addChild(new Element.Builder(Tag.TEXTAREA).setAttribute(Attribute.NAME, "content")
            .setAttribute(Attribute.CLASS, "wide")
            .setAttribute(Attribute.ROWS, "30")
            .addEscapedText(post.getContent())
            .build())
        .addChild(new Element(Tag.BR))
        .addChild(new Element.Builder(Tag.INPUT)
            .setAttribute(Attribute.TYPE, "submit")
            .setAttribute(Attribute.VALUE, "Update Post")
            .setAttribute(Attribute.STYLE, "display: block; margin: 0px auto;")
            .build())
        .build();
    Element document = Layout.createDocument(request,
        Option.some("Edit Post"), Option.<Instant>none(), form);
    return HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document);
  }

  private HttpResponse handlePost(HttpRequest request) {
    String subject = request.getUrlencodedPostData().getValues("subject")
        .tryGetOnlyElement().getOrThrow();
    String content = request.getUrlencodedPostData().getValues("content")
        .tryGetOnlyElement().getOrThrow();
    Post editedPost = new Post.Builder()
        .setUuid(post.getUuid())
        .setCreatedAt(post.getCreatedAt())
        .setSubject(subject)
        .setContent(content)
        .build();
    PostStorage.updatePost(editedPost);

    String location = String.format("%s/%s",
        Config.getBaseUrl(), PostUrlFactory.getViewUrl(editedPost));
    return HttpResponseFactory.redirectToGet(location);
  }
}
