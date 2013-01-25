package daniel.blog.post;

import daniel.blog.comment.Comment;
import daniel.blog.comment.CommentFormFormatter;
import daniel.blog.comment.CommentFormatter;
import daniel.data.collection.Collection;
import daniel.data.function.Function;
import daniel.data.order.AbstractOrdering;
import daniel.data.order.Relation;
import daniel.data.sequence.Sequence;
import daniel.data.unit.Instant;
import daniel.web.html.Attribute;
import daniel.web.html.Element;
import daniel.web.html.Tag;
import daniel.web.html.TextNode;

public final class PostFormatter {
  private PostFormatter() {}

  public static Element full(Post post, Collection<Comment> comments) {
    Sequence<Comment> commentsByDate = comments.sorted(new AbstractOrdering<Comment>() {
      @Override public Relation compare(Comment a, Comment b) {
        return Instant.ASCENDING_ORDERING.compare(a.getCreatedAt(), b.getCreatedAt());
      }
    });
    Sequence<Element> commentElements = commentsByDate.map(new Function<Comment, Element>() {
      @Override public Element apply(Comment comment) {
        return CommentFormatter.full(comment);
      }
    });

    Element.Builder builder = new Element.Builder(Tag.DIV);
    builder.addChild(TextNode.rawText(post.getContent()));
    if (!commentElements.isEmpty()) {
      String headerText = String.format("%d Comments", commentElements.getSize());
      builder.addChild(new Element(Tag.H4, TextNode.escapedText(headerText)));
      builder.addChildren(commentElements);
    }
    builder.addChild(CommentFormFormatter.getAddCommentForm(post));
    return builder.build();
  }

  public static Element summaryLink(Post post) {
    return new Element.Builder(Tag.A)
        .setAttribute(Attribute.HREF, PostUrlFactory.getViewUrl(post))
        .addChild(TextNode.escapedText(post.getSubject()))
        .build();
  }
}
