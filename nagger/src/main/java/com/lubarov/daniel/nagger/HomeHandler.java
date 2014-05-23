package com.lubarov.daniel.nagger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.nagger.model.Alert;
import com.lubarov.daniel.nagger.model.Recipient;
import com.lubarov.daniel.nagger.storage.AlertStorage;
import com.lubarov.daniel.nagger.storage.RecipientStorage;
import com.lubarov.daniel.web.html.*;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpResponse;
import com.lubarov.daniel.web.http.HttpStatus;
import com.lubarov.daniel.web.http.server.PartialHandler;
import com.lubarov.daniel.web.http.server.util.HttpResponseFactory;

import java.util.HashMap;
import java.util.Map;

public final class HomeHandler implements PartialHandler {
  public static final HomeHandler singleton = new HomeHandler();

  private HomeHandler() {}

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    Element html = new Element.Builder(Tag.HTML)
        .setRawAttribute("xmlns", "http://www.w3.org/1999/xhtml")
        .setRawAttribute("xml:lang", "en")
        .addChild(getHead())
        .addChild(getBody())
        .build();
    return Option.some(HttpResponseFactory.xhtmlResponse(HttpStatus.OK, html));
  }

  private static Element getHead() {
    return new Element.Builder(Tag.HEAD)
        .addChild(new TitleBuilder().addEscapedText("Nagger").build())
        .addChild(new Element.Builder(Tag.BASE)
            .setRawAttribute(Attribute.HREF, Config.getBaseUrl())
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
            .setRawAttribute(Attribute.TYPE, "text/javascript")
            .addEscapedText("var alerts = " + gson.toJson(alertsByUuid) + ";")
            .addEscapedText("var recipients = " + gson.toJson(recipientsByUuid) + ";")
            .build())
        .build();
  }
}
