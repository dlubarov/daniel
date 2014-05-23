package com.lubarov.daniel.blog.comment;

import com.lubarov.daniel.blog.post.Post;
import com.lubarov.daniel.web.html.Attribute;
import com.lubarov.daniel.web.html.Element;
import com.lubarov.daniel.web.html.Tag;

public final class CommentFormFormatter {
  private CommentFormFormatter() {}

  public static Element getAddCommentForm(Post post) {
    Element form = new Element.Builder(Tag.FORM)
        .setRawAttribute(Attribute.ACTION, post.getUrlFriendlySubject() + "/comment")
        .setRawAttribute(Attribute.METHOD, "post")
        .setRawAttribute(Attribute.CLASS, "add-comment")
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
        .setRawAttribute(Attribute.NAME, "author_name")
        .setRawAttribute(Attribute.TYPE, "text")
        .setRawAttribute(Attribute.PLACEHOLDER, "Your Name")
        .build();
    return new Element.Builder(Tag.DIV)
        .addChild(input)
        .build();
  }

  private static Element getAuthorEmailPart() {
    Element input = new Element.Builder(Tag.INPUT)
        .setRawAttribute(Attribute.NAME, "author_email")
        .setRawAttribute(Attribute.TYPE, "text")
        .setRawAttribute(Attribute.PLACEHOLDER, "Your Email (Optional)")
        .build();
    return new Element.Builder(Tag.DIV)
        .addChild(input)
        .build();
  }

  private static Element getContentPart() {
    Element textarea = new Element.Builder(Tag.TEXTAREA)
        .setRawAttribute(Attribute.NAME, "content")
        .setRawAttribute(Attribute.ROWS, "5")
        .setRawAttribute(Attribute.PLACEHOLDER, "Comment")
        .build();
    return new Element.Builder(Tag.DIV)
        .addChild(textarea)
        .build();
  }

  private static Element getChallengePart() {
    Element text = new Element.Builder(Tag.SPAN)
        .addEscapedText("What do cows drink?")
        .setRawAttribute(Attribute.STYLE, "display: table-cell; width: 1px; white-space: nowrap;")
        .build();
    Element filler = new Element.Builder(Tag.SPAN)
        .addRawText("&#160;")
        .setRawAttribute(Attribute.STYLE, "display: table-cell; width: 0.5em;")
        .build();
    Element input = new Element.Builder(Tag.INPUT)
        .setRawAttribute(Attribute.NAME, "challenge")
        .setRawAttribute(Attribute.TYPE, "text")
        .setRawAttribute(Attribute.STYLE, "display: table-cell; width: 100%; margin: 0;")
        .build();
    return new Element.Builder(Tag.DIV)
        .setRawAttribute(Attribute.STYLE, "display: table; width: 100%; margin: 6px 0;")
        .addChild(text)
        .addChild(filler)
        .addChild(input)
        .build();
  }

  private static Element getSubmitPart() {
    return new Element.Builder(Tag.INPUT)
        .setRawAttribute(Attribute.TYPE, "submit")
        .setRawAttribute(Attribute.VALUE, "Post Comment")
        .build();
  }
}
