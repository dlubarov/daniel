package daniel.blog.comment;

import daniel.blog.post.Post;
import daniel.web.html.Attribute;
import daniel.web.html.Element;
import daniel.web.html.Tag;

public final class CommentFormFormatter {
  private CommentFormFormatter() {}

  public static Element getAddCommentForm(Post post) {
    Element.Builder formBuilder = new Element.Builder(Tag.FORM);
    formBuilder.setAttribute(Attribute.ACTION, post.getUrlFriendlySubject() + "/comment");
    formBuilder.setAttribute(Attribute.METHOD, "post");
    formBuilder.setAttribute(Attribute.CLASS, "add-comment");
    formBuilder.addChild(new Element.Builder(Tag.H4).addEscapedText("Post a Comment").build());
    formBuilder.addChild(getAuthorNamePart());
    formBuilder.addChild(getAuthorEmailPart());
    formBuilder.addChild(getContentPart());
    formBuilder.addChild(getSubmitPart());
    return formBuilder.build();
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

  private static Element getSubmitPart() {
    return new Element.Builder(Tag.INPUT)
        .setAttribute(Attribute.TYPE, "submit")
        .setAttribute(Attribute.VALUE, "Post Comment")
        .setAttribute(Attribute.CLASS, "submit-button")
        .build();
  }
}
