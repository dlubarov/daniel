package daniel.blog.comment;

import daniel.blog.post.Post;
import daniel.web.html.Attribute;
import daniel.web.html.Element;
import daniel.web.html.Tag;

public final class CommentFormFormatter {
  private CommentFormFormatter() {}

  public static Element getAddCommentForm(Post post) {
    Element form = new Element.Builder(Tag.FORM)
        .setAttribute(Attribute.ACTION, post.getUrlFriendlySubject() + "/comment")
        .setAttribute(Attribute.METHOD, "post")
        .setAttribute(Attribute.CLASS, "add-comment")
        .addChild(new Element.Builder(Tag.H2)
            .addEscapedText("Post a Comment")
            .build())
        .addChild(getAuthorNamePart())
        .addChild(getAuthorEmailPart())
        .addChild(getContentPart())
        .addChild(getChallengePart())
        .addChild(getSubmitPart())
        .build();
    return new Element.Builder(Tag.ASIDE)
        .addChild(form)
        .build();
  }

  private static Element getAuthorNamePart() {
    Element input = new Element.Builder(Tag.INPUT)
        .setAttribute(Attribute.NAME, "author_name")
        .setAttribute(Attribute.TYPE, "text")
        .setAttribute(Attribute.PLACEHOLDER, "Your Name")
        .build();
    return new Element.Builder(Tag.DIV)
        .addChild(input)
        .build();
  }

  private static Element getAuthorEmailPart() {
    Element input = new Element.Builder(Tag.INPUT)
        .setAttribute(Attribute.NAME, "author_email")
        .setAttribute(Attribute.TYPE, "text")
        .setAttribute(Attribute.PLACEHOLDER, "Your Email (Optional)")
        .build();
    return new Element.Builder(Tag.DIV)
        .addChild(input)
        .build();
  }

  private static Element getContentPart() {
    Element textarea = new Element.Builder(Tag.TEXTAREA)
        .setAttribute(Attribute.NAME, "content")
        .setAttribute(Attribute.ROWS, "5")
        .setAttribute(Attribute.PLACEHOLDER, "Comment")
        .build();
    return new Element.Builder(Tag.DIV)
        .addChild(textarea)
        .build();
  }

  private static Element getChallengePart() {
    Element text = new Element.Builder(Tag.SPAN)
        .addEscapedText("What do cows drink?")
        .setAttribute(Attribute.STYLE, "display: table-cell; width: 1px; white-space: nowrap;")
        .build();
    Element filler = new Element.Builder(Tag.SPAN)
        .addRawText("&#160;")
        .setAttribute(Attribute.STYLE, "display: table-cell; width: 0.5em;")
        .build();
    Element input = new Element.Builder(Tag.INPUT)
        .setAttribute(Attribute.NAME, "challenge")
        .setAttribute(Attribute.TYPE, "text")
        .setAttribute(Attribute.STYLE, "display: table-cell; width: 100%; margin: 0;")
        .build();
    return new Element.Builder(Tag.DIV)
        .setAttribute(Attribute.STYLE, "display: table; width: 100%; margin: 6px 0;")
        .addChild(text)
        .addChild(filler)
        .addChild(input)
        .build();
  }

  private static Element getSubmitPart() {
    return new Element.Builder(Tag.INPUT)
        .setAttribute(Attribute.TYPE, "submit")
        .setAttribute(Attribute.VALUE, "Post Comment")
        .build();
  }
}
