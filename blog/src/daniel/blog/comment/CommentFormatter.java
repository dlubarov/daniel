package daniel.blog.comment;

import daniel.web.html.Element;
import daniel.web.html.Tag;
import daniel.web.html.TextNode;

public final class CommentFormatter {
  private CommentFormatter() {}

  public static Element full(Comment comment) {
    Element author = new Element(Tag.P,
        TextNode.escapedText(comment.getAuthorName()),
        TextNode.escapedText(" says:"));
    Element content = new Element(Tag.P, TextNode.escapedText(comment.getContent()));
    return new Element(Tag.DIV, author, content);
  }
}
