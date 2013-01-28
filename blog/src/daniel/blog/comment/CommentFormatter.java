package daniel.blog.comment;

import daniel.web.html.Attribute;
import daniel.web.html.Element;
import daniel.web.html.EscapeUtils;
import daniel.web.html.Node;
import daniel.web.html.ParagraphBuilder;
import daniel.web.html.Tag;
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
    return new Element.Builder(Tag.H5)
        .addChild(formatAuthor(comment))
        .addEscapedText(" on ")
        .addEscapedText(formatCreatedAt(comment))
        .build();
  }

  private static Node formatAuthor(Comment comment) {
    return new Element.Builder(Tag.STRONG)
        .addEscapedText(comment.getAuthorName())
        .build();
  }

  private static String formatCreatedAt(Comment comment) {
    return dateFormat.format(comment.getCreatedAt().toDate());
  }

  private static Element formatContent(Comment comment) {
    String content = comment.getContent();
    content = EscapeUtils.htmlEncode(content);
    content = content.replaceAll("\n", "<br />");
    return new ParagraphBuilder().addRawText(content).build();
  }
}
