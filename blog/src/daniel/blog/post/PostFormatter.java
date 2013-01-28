package daniel.blog.post;

import daniel.blog.comment.Comment;
import daniel.blog.comment.CommentFormFormatter;
import daniel.blog.comment.CommentFormatter;
import daniel.data.function.Function;
import daniel.data.sequence.Sequence;
import daniel.web.html.AnchorBuilder;
import daniel.web.html.Element;
import daniel.web.html.Tag;

public final class PostFormatter {
  private PostFormatter() {}

  public static Element full(Post post, Sequence<Comment> commentsByDate) {
    Sequence<Element> commentElements = commentsByDate.map(new Function<Comment, Element>() {
      @Override public Element apply(Comment comment) {
        return CommentFormatter.full(comment);
      }
    });

    Element.Builder builder = new Element.Builder(Tag.DIV);
    builder.addRawText(post.getContent());
    if (!commentElements.isEmpty()) {
      String headerText = String.format("%d Comments", commentElements.getSize());
      builder.addChild(new Element.Builder(Tag.H4).addEscapedText(headerText).build());
      builder.addChildren(commentElements);
    }
    builder.addChild(CommentFormFormatter.getAddCommentForm(post));
    return builder.build();
  }

  public static Element summaryLink(Post post) {
    return new AnchorBuilder()
        .setHref(PostUrlFactory.getViewUrl(post))
        .addEscapedText(post.getSubject())
        .build();
  }
}
