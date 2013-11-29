package daniel.chat;

import daniel.data.option.Option;
import daniel.web.html.AnchorBuilder;
import daniel.web.html.Attribute;
import daniel.web.html.Element;
import daniel.web.html.InputBuilder;
import daniel.web.html.JavaScriptUtils;
import daniel.web.html.ParagraphBuilder;
import daniel.web.html.StylesheetUtils;
import daniel.web.html.Tag;
import daniel.web.html.TitleBuilder;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.HttpStatus;
import daniel.web.http.server.PartialHandler;
import daniel.web.http.server.util.HttpResponseFactory;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public final class ChatPageHandler implements PartialHandler {
  public static final ChatPageHandler singleton = new ChatPageHandler();

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    Element html = new Element.Builder(Tag.HTML)
        .setRawAttribute("xmlns", "http://www.w3.org/1999/xhtml")
        .setRawAttribute("xml:lang", "en")
        .addChild(getHead())
        .addChild(getBody(request))
        .build();
    return Option.some(HttpResponseFactory.xhtmlResponse(HttpStatus.OK, html));
  }

  private static Element getHead() {
    Element description = new Element.Builder(Tag.META)
        .setRawAttribute(Attribute.NAME, "description")
        .setRawAttribute(Attribute.CONTENT, "Chat about anything! To create a new chat room, just append its name to the URL.")
        .build();
    Element keywords = new Element.Builder(Tag.META)
        .setRawAttribute(Attribute.NAME, "keywords")
        .setRawAttribute(Attribute.CONTENT, "chat, chat room, online chat, jabberings")
        .build();
    Element base = new Element.Builder(Tag.BASE)
        .setRawAttribute(Attribute.HREF, Config.getBaseUrl())
        .build();
    return new Element.Builder(Tag.HEAD)
        .addChild(description)
        .addChild(keywords)
        .addChild(base)
        .addChild(StylesheetUtils.createCssLink("reset.css"))
        .addChild(StylesheetUtils.createCssLink("style.css"))
        .addChild(JavaScriptUtils.createJavaScriptLink("chat.js"))
        .addChild(new TitleBuilder().addEscapedText("Jabberings.net Online Chat").build())
        .build();
  }

  private static Element getBody(HttpRequest request) {
    String decodedResource;
    try {
      decodedResource = URLDecoder.decode(request.getResource(), "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new AssertionError();
    }
    String title = request.getResource().length() > 1
        ? decodedResource
        : "Main Room";

    Element content = new Element.Builder(Tag.DIV)
        .setRawAttribute(Attribute.ID, "content")
        .addChild(new Element.Builder(Tag.H1).addEscapedText(title).build())
        .addChild(getContentPane())
        .addChild(getInputBar())
        .addChild(getNotice())
        .build();
    return new Element.Builder(Tag.BODY)
        .addChild(content)
        .build();
  }

  private static Element getContentPane() {
    return new Element.Builder(Tag.DIV)
        .setRawAttribute(Attribute.ID, "messages")
        .build();
  }

  private static Element getInputBar() {
    Element nameBox = new InputBuilder()
        .setId("name")
        .setType("text")
        .setPlaceholder("Your Name")
        .build();
    Element messageBox = new InputBuilder()
        .setId("message")
        .setType("text")
        .setPlaceholder("Enter your message here...")
        .setOnKeyDown("if (event.keyCode == 13) { send(); }")
        .build();
    Element sendButton = new Element.Builder(Tag.BUTTON)
        .setRawAttribute(Attribute.ID, "sendButton")
        .setRawAttribute("onclick", "javascript:send();")
        .addEscapedText("Send")
        .build();
    return new Element.Builder(Tag.DIV)
        .setRawAttribute(Attribute.ID, "inputBar")
        .addChild(nameBox)
        .addChild(messageBox)
        .addChild(sendButton)
        .build();
  }

  private static Element getNotice() {
    Element exampleLink = new AnchorBuilder()
        .setHref(Config.getBaseUrl() + "/math")
        .addEscapedText(Config.getBaseUrl().substring(7) + "/math")
        .build();
    return new ParagraphBuilder()
        .setId("notice")
        .addRawText("To start a new chat room, just append its name to the URL.<br />For example: ")
        .addChild(exampleLink)
        .addEscapedText(".")
        .build();
  }
}
