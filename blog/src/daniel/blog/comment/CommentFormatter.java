package daniel.blog.comment;

import daniel.web.html.Attribute;
import daniel.web.html.Element;
import daniel.web.html.EscapeUtils;
import daniel.web.html.Node;
import daniel.web.html.Tag;
import daniel.web.html.TextNode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public final class CommentFormatter {
  private static final DateFormat dateFormat = new SimpleDateFormat("MMMMM d, yyyy");

  private CommentFormatter() {}

  public static Element full(Comment comment) {
    return new Element.Builder(Tag.DIV)
        .setAttribute(Attribute.CLASS, "comment")
        .addChild(formatAuthorAndCreatedAt(comment))
        .addChild(formatContent(comment))
        .build();
  }

  private static Node formatAuthorAndCreatedAt(Comment comment) {
    return new Element(Tag.H5,
        formatAuthor(comment),
        TextNode.escapedText(" on "),
        formatCreatedAt(comment));
  }

  private static Node formatAuthor(Comment comment) {
    return new Element(Tag.STRONG, TextNode.escapedText(comment.getAuthorName()));
  }

  private static Node formatCreatedAt(Comment comment) {
    String createdAtString = dateFormat.format(comment.getCreatedAt().toDate());
    return TextNode.escapedText(createdAtString);
  }

  private static Element formatContent(Comment comment) {
    String content = comment.getContent();
    content = EscapeUtils.htmlEncode(content);
    content = content.replaceAll("\n", "<br />");
    return new Element(Tag.P, TextNode.rawText(content));
  }
}
