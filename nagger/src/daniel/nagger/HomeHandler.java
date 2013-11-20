package daniel.nagger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import daniel.data.option.Option;
import daniel.nagger.model.Alert;
import daniel.nagger.model.Recipient;
import daniel.nagger.storage.AlertStorage;
import daniel.nagger.storage.RecipientStorage;
import daniel.web.html.Attribute;
import daniel.web.html.Element;
import daniel.web.html.JavaScriptUtils;
import daniel.web.html.StylesheetUtils;
import daniel.web.html.Tag;
import daniel.web.html.TitleBuilder;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.HttpStatus;
import daniel.web.http.server.PartialHandler;
import daniel.web.http.server.util.HttpResponseFactory;
import java.util.HashMap;
import java.util.Map;

public final class HomeHandler implements PartialHandler {
  public static final HomeHandler singleton = new HomeHandler();

  private HomeHandler() {}

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    Element html = new Element.Builder(Tag.HTML)
        .setAttribute("xmlns", "http://www.w3.org/1999/xhtml")
        .setAttribute("xml:lang", "en")
        .addChild(getHead())
        .addChild(getBody())
        .build();
    return Option.some(HttpResponseFactory.xhtmlResponse(HttpStatus.OK, html));
  }

  private static Element getHead() {
    return new Element.Builder(Tag.HEAD)
        .addChild(new TitleBuilder().addEscapedText("Nagger").build())
        .addChild(new Element.Builder(Tag.BASE)
            .setAttribute(Attribute.HREF, Config.getBaseUrl())
            .build())
        .addChild(StylesheetUtils.createCssLink("reset.css"))
        .addChild(StylesheetUtils.createCssLink("style.css"))
        .addChild(JavaScriptUtils.createJavaScriptLink("nagger.js"))
        .build();
  }

  private static Element getBody() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    Map<String, Alert> alertsByUuid = new HashMap<>();
    for (Alert alert : AlertStorage.getAllAlerts())
      alertsByUuid.put(alert.uuid, alert);

    Map<String, Recipient> recipientsByUuid = new HashMap<>();
    for (Recipient recipient : RecipientStorage.getAllRecipients())
      recipientsByUuid.put(recipient.uuid, recipient);

    return new Element.Builder(Tag.BODY)
        .addChild(new Element.Builder(Tag.P)
            .addEscapedText("Loading...")
            .build())
        .addChild(new Element.Builder(Tag.SCRIPT)
            .setAttribute(Attribute.TYPE, "text/javascript")
            .addEscapedText("var alerts = " + gson.toJson(alertsByUuid) + ";")
            .addEscapedText("var recipients = " + gson.toJson(recipientsByUuid) + ";")
            .build())
        .build();
  }
}
