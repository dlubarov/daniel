package com.lubarov.daniel.blog.post;

import com.lubarov.daniel.blog.comment.Comment;
import com.lubarov.daniel.blog.comment.CommentFormFormatter;
import com.lubarov.daniel.blog.comment.CommentFormatter;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.sequence.Sequence;
import com.lubarov.daniel.web.html.*;

public final class PostFormatter {
  private PostFormatter() {}

  public static Element full(Post post, Sequence<Comment> commentsByDate) {
    Element.Builder builder = new Element.Builder(Tag.DIV);
    builder.addRawText(post.getContent());
    Option<Element> optCommentsSection = getCommentsSection(commentsByDate);
    if (optCommentsSection.isDefined())
      builder.addChild(optCommentsSection.getOrThrow());
    return builder
        .addChild(HtmlUtils.getClearDiv())
        .addChild(CommentFormFormatter.getAddCommentForm(post))
        .build();
  }

  private static Option<Element> getCommentsSection(Sequence<Comment> commentsByDate) {
    if (commentsByDate.isEmpty())
      return Option.none();

    String headerText = String.format("%d Comments", commentsByDate.getSize());
    Element commentsHeader = new Element.Builder(Tag.H2)
        .addEscapedText(headerText)
        .build();
    return Option.some(new Element.Builder(Tag.SECTION)
        .setRawAttribute(Attribute.CLASS, "comments")
        .addChild(commentsHeader)
        .addChildren(commentsByDate.map(CommentFormatter::full))
        .build());
  }

  public static Element summaryLink(Post post) {
    AnchorBuilder anchorBuilder = new AnchorBuilder()
        .setHref(PostUrlFactory.getViewUrl(post))
        .addEscapedText(post.getSubject());
    if (!post.isPublished())
      anchorBuilder.setStyle("color: #aaa;");
    return anchorBuilder.build();
  }
}
