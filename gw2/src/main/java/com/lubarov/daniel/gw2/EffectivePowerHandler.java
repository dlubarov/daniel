package com.lubarov.daniel.gw2;

import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.sequence.ImmutableArray;
import com.lubarov.daniel.web.html.Attribute;
import com.lubarov.daniel.web.html.Element;
import com.lubarov.daniel.web.html.InputBuilder;
import com.lubarov.daniel.web.html.ParagraphBuilder;
import com.lubarov.daniel.web.html.Tag;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpResponse;
import com.lubarov.daniel.web.http.HttpStatus;
import com.lubarov.daniel.web.http.server.PartialHandler;
import com.lubarov.daniel.web.http.server.util.HttpResponseFactory;

public class EffectivePowerHandler implements PartialHandler {
  public static final EffectivePowerHandler singleton = new EffectivePowerHandler();

  private EffectivePowerHandler() {}

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    if (!request.getResource().equals("/effective-power"))
      return Option.none();

    Element table = new Element.Builder(Tag.TABLE)
        .addChild(createInputRow("Power", "power", 1000))
        .addChild(createInputRow("Precision", "precision", 1000))
        .addChild(createInputRow("Ferocity", "ferocity", 0))
        .addChild(createInputRow("Average might stacks", "might", 10))
        .addChild(createInputRow("Average fury uptime (as a percentage)", "fury", 50))
        .addChild(createOutputRow("Average power (non-critical)", "non_critical_power"))
        .addChild(createOutputRow("Average power (critical)", "critical_power"))
        .addChild(createOutputRow("Average critical chance", "critical_chance"))
        .addChild(createOutputRow("Overall effective power", "effective_power"))
        .addChild(createOutputRow("Adding 1 point of power will increase effective power by", "increase_power"))
        .addChild(createOutputRow("Adding 1 point of precision will increase effective power by", "increase_precision"))
        .addChild(createOutputRow("Adding 1 point of ferocity will increase effective power by", "increase_ferocity"))
        .build();

    Element title = new Element.Builder(Tag.H1).addEscapedText("Effective Power Calculator").build();
    Element intro = new ParagraphBuilder()
        .addEscapedText("Deciding between Berserker's and Assassin's gear? Want to see how much offensive power you sacrifice with Celestial gear? Try this calculator!")
        .build();
    Element disclaimer = new ParagraphBuilder()
        .addEscapedText("These calculations assume a level 80 character.")
        .build();
    Element document = Gw2Layout.createDocument(request, Option.none(),
        ImmutableArray.create("effective-power.js"), title, intro, disclaimer, table);
    return Option.some(HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document));
  }

  private static Element createInputRow(String name, String id, int defaultValue) {
    return new Element.Builder(Tag.TR)
        .addChild(new Element.Builder(Tag.TD)
            .setEscapedAttribtue(Attribute.CLASS, "tr")
            .addEscapedText(name)
            .build())
        .addChild(new Element.Builder(Tag.TD)
            .addChild(new InputBuilder()
                .setId(id)
                .setType("number")
                .setValue(Integer.toString(defaultValue))
                .setOnChange("refresh()")
                .build())
            .build())
        .build();
  }

  private static Element createOutputRow(String name, String id) {
    return new Element.Builder(Tag.TR)
        .addChild(new Element.Builder(Tag.TD)
            .setEscapedAttribtue(Attribute.CLASS, "tr")
            .addEscapedText(name)
            .build())
        .addChild(new Element.Builder(Tag.TD)
            .setEscapedAttribtue(Attribute.ID, id)
            .build())
        .build();
  }
}
