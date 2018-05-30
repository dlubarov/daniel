package com.lubarov.daniel.blog.comment;

import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.util.DigestUtils;
import com.lubarov.daniel.web.html.Attribute;
import com.lubarov.daniel.web.html.Element;
import com.lubarov.daniel.web.html.EscapeUtils;
import com.lubarov.daniel.web.html.HtmlUtils;
import com.lubarov.daniel.web.html.Node;
import com.lubarov.daniel.web.html.ParagraphBuilder;
import com.lubarov.daniel.web.html.Tag;
import com.lubarov.daniel.web.http.DateUtils;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public final class CommentFormatter {
  private static final DateFormat dateFormat = new SimpleDateFormat("MMMMM d, yyyy");

  private CommentFormatter() {}

  public static Element full(Comment comment) {
    return new Element.Builder(Tag.DIV)
        .setRawAttribute(Attribute.CLASS, "comment")
        .addChild(getAvatar(comment.getAuthorEmail()))
        .addChild(getPrefixAndContent(comment))
        .addChild(HtmlUtils.getClearDiv())
        .build();
  }

  private static Node getPrefixAndContent(Comment comment) {
    return new Element.Builder(Tag.ARTICLE)
        .setRawAttribute(Attribute.STYLE, "float: left;")
        .addChild(formatAuthorAndCreatedAt(comment))
        .addChild(formatContent(comment))
        .build();
  }

  private static Node getAvatar(Option<String> authorEmail) {
    String email = authorEmail.getOrDefault("").trim().toLowerCase();
    String hash = DigestUtils.md5Hex(email.getBytes(Charset.forName("CP1252")));
    String src = String.format("http://www.gravatar.com/avatar/%s?s=54&amp;d=mm", hash);
    return new Element.Builder(Tag.IMG)
        .setRawAttribute(Attribute.CLASS, "gravatar")
        .setRawAttribute(Attribute.SRC, src)
        .setRawAttribute(Attribute.ALT, "user avatar")
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
        .setRawAttribute(Attribute.DATETIME, DateUtils.formatIso8601(comment.getCreatedAt()))
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
