package daniel.blog.comment;

import daniel.data.option.Option;
import daniel.data.util.DigestUtils;
import daniel.web.html.Attribute;
import daniel.web.html.Element;
import daniel.web.html.EscapeUtils;
import daniel.web.html.HtmlUtils;
import daniel.web.html.Node;
import daniel.web.html.ParagraphBuilder;
import daniel.web.html.Tag;
import daniel.web.http.DateUtils;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public final class CommentFormatter {
  private static final DateFormat dateFormat = new SimpleDateFormat("MMMMM d, yyyy");

  private CommentFormatter() {}

  public static Element full(Comment comment) {
    return new Element.Builder(Tag.DIV)
        .setAttribute(Attribute.CLASS, "comment")
        .addChild(getAvatar(comment.getAuthorEmail()))
        .addChild(getPrefixAndContent(comment))
        .addChild(HtmlUtils.getClearDiv())
        .build();
  }

  private static Node getPrefixAndContent(Comment comment) {
    return new Element.Builder(Tag.ARTICLE)
        .setAttribute(Attribute.STYLE, "float: left;")
        .addChild(formatAuthorAndCreatedAt(comment))
        .addChild(formatContent(comment))
        .build();
  }

  private static Node getAvatar(Option<String> authorEmail) {
    String email = authorEmail.getOrDefault("").trim().toLowerCase();
    String hash = DigestUtils.md5Hex(email.getBytes(Charset.forName("CP1252")));
    String src = String.format("http://www.gravatar.com/avatar/%s?s=54&amp;d=mm", hash);
    return new Element.Builder(Tag.IMG)
        .setAttribute(Attribute.CLASS, "gravatar")
        .setAttribute(Attribute.SRC, src)
        .setAttribute(Attribute.ALT, "user avatar")
        .build();
  }

  private static Node formatAuthorAndCreatedAt(Comment comment) {
    return new Element.Builder(Tag.H5)
        .addChild(formatAuthor(comment))
        .addEscapedText(" on ")
        .addChild(formatCreatedAt(comment))
        .build();
  }

  private static Node formatAuthor(Comment comment) {
    // TODO: Microdata
    return new Element.Builder(Tag.STRONG)
        .addEscapedText(comment.getAuthorName())
        .build();
  }

  private static Element formatCreatedAt(Comment comment) {
    return new Element.Builder(Tag.TIME)
        .setAttribute(Attribute.DATETIME, DateUtils.formatIso8601(comment.getCreatedAt()))
        .addEscapedText(dateFormat.format(comment.getCreatedAt().toDate()))
        .build();
  }

  private static Element formatContent(Comment comment) {
    String content = comment.getContent();
    content = EscapeUtils.htmlEncode(content);
    content = content.replaceAll("\n", "<br />");
    return new ParagraphBuilder().addRawText(content).build();
  }
}
