package daniel.blog.post;

import daniel.web.html.Attribute;
import daniel.web.html.Element;
import daniel.web.html.Tag;
import daniel.web.html.TextNode;

public final class PostFormatter {
  private PostFormatter() {}

  public static Element full(Post post) {
    return new Element.Builder(Tag.DIV)
        .addChild(new Element(Tag.H2,
            TextNode.escapedText(post.getSubject())))
        .addChild(new Element(Tag.P,
            TextNode.escapedText(post.getCreatedAt().toString())))
        .addChild(new Element(Tag.P,
            TextNode.escapedText(post.getContent())))
        .build();
  }

  public static Element summary(Post post) {
    return new Element.Builder(Tag.A)
        .setAttribute(Attribute.HREF, PostUrlFactory.getViewUrl(post))
        .addChild(TextNode.escapedText(post.getSubject()))
        .build();
  }
}
