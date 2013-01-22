package daniel.blog.post;

import daniel.blog.comment.CommentFormFormatter;
import daniel.web.html.Attribute;
import daniel.web.html.Element;
import daniel.web.html.Tag;
import daniel.web.html.TextNode;

public final class PostFormatter {
  private PostFormatter() {}

  public static Element full(Post post) {
    return new Element.Builder(Tag.DIV)
        .addChild(new Element(Tag.P, TextNode.rawText(post.getContent())))
        .addChild(CommentFormFormatter.getAddCommentForm(post))
        .build();
  }

  public static Element summaryLink(Post post) {
    return new Element.Builder(Tag.A)
        .setAttribute(Attribute.HREF, PostUrlFactory.getViewUrl(post))
        .addChild(TextNode.escapedText(post.getSubject()))
        .build();
  }
}
