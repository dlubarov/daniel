package com.lubarov.daniel.conniewedding;

import com.lubarov.daniel.common.Logger;
import com.lubarov.daniel.conniewedding.rsvp.RSVP;
import com.lubarov.daniel.conniewedding.rsvp.RSVPStorage;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.sequence.ImmutableArray;
import com.lubarov.daniel.data.sequence.Sequence;
import com.lubarov.daniel.data.table.sequential.SequentialTable;
import com.lubarov.daniel.data.unit.Instant;
import com.lubarov.daniel.web.html.Attribute;
import com.lubarov.daniel.web.html.Element;
import com.lubarov.daniel.web.html.InputBuilder;
import com.lubarov.daniel.web.html.ParagraphBuilder;
import com.lubarov.daniel.web.html.Tag;
import com.lubarov.daniel.web.html.TextNode;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpResponse;
import com.lubarov.daniel.web.http.HttpStatus;
import com.lubarov.daniel.web.http.server.PartialHandler;
import com.lubarov.daniel.web.http.server.util.HttpResponseFactory;

import java.util.UUID;

public class WeddingRSVPHandler implements PartialHandler {
  public static final WeddingRSVPHandler singleton = new WeddingRSVPHandler();

  private static final Logger logger = Logger.forClass(WeddingRSVPHandler.class);

  private static final Sequence<Entree> ENTREES = ImmutableArray.create(
      new Entree("Grilled Filet Mignon", "cabernet herb butter"),
      new Entree("Oven Roasted Halibut", "garden herb beurre blanc"),
      new Entree("Lavender & Pistachio Crusted Rack of Lamb", "cabernet - lamb reduction"),
      new Entree("Pecan Crusted Chicken Breast", "whole grain mustard Cream"));

  private static class Entree {
    private final String name;
    private final String details;

    Entree(String name, String details) {
      this.name = name;
      this.details = details;
    }
  }

  private WeddingRSVPHandler() {}

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    if (request.getResource().equals("/rsvp"))
      return Option.some(getRsvpResponse());
    if (request.getResource().equals("/rsvp/submit"))
      return Option.some(getRsvpSubmitResponse(request));
    if (request.getResource().equals("/rsvp/thanks"))
      return Option.some(getRsvpThanksResponse());
    return Option.none();
  }

  private HttpResponse getRsvpResponse() {
    Element intro = new ParagraphBuilder()
        .addEscapedText("Please RSVP by June 28, or let us know if you need more time.")
        .build();

    Element redAsterisk = new Element.Builder(Tag.SPAN)
        .setRawAttribute("style", "color: red;")
        .addEscapedText(" *")
        .build();

    Element nameLabel = new ParagraphBuilder()
        .addEscapedText("Your full name")
        .addChild(redAsterisk)
        .build();

    Element nameInput = new InputBuilder()
        .setType("text")
        .setName("name")
        .setRequired()
        .build();

    Element emailLabel = new ParagraphBuilder()
        .addEscapedText("Your email or phone number")
        .addChild(redAsterisk)
        .build();

    Element emailInput = new InputBuilder()
        .setType("text")
        .setName("email_or_phone")
        .setRequired()
        .build();

    Element attendingLabel = new ParagraphBuilder()
        .addEscapedText("Will you be attending?")
        .addChild(redAsterisk)
        .build();

    Element partySizeLabel = new ParagraphBuilder()
        .addEscapedText("How many people in your party will be attending?")
        .addChild(redAsterisk)
        .build();

    Element partySizeInput = new InputBuilder()
        .setType("text")
        .setName("party_size")
        .build();

    Element entreeList = new Element.Builder(Tag.UL)
        .addChildren(ENTREES.map(e -> new Element(Tag.LI,
            new Element.Builder(Tag.SPAN).setRawAttribute("class", "entree_name").addEscapedText(e.name).build(),
            TextNode.rawText(" &#8212; "),
            TextNode.escapedText(e.details))))
        .setRawAttribute(Attribute.STYLE, "margin-top: 0.5em;")
        .build();

    Element entreesLabel = new ParagraphBuilder()
        .addEscapedText("Please choose preferred dinner entrees for all guests:")
        .addChild(redAsterisk)
        .addChild(entreeList)
        .build();

    Element entreesInput = new Element.Builder(Tag.TEXTAREA)
        .setRawAttribute("name", "entrees")
        .build();

    Element kidsLabel = new ParagraphBuilder()
        .addEscapedText("Would you prefer kids meals for your kids, and if yes, how many?")
        .build();

    Element kidsInput = new InputBuilder()
        .setType("text")
        .setName("kids")
        .build();

    Element contactLabel = new ParagraphBuilder()
        .addEscapedText("Please feel free to contact us if you have any questions.")
        .build();

    Element attendingContainer = new Element.Builder(Tag.DIV)
        .setRawAttribute(Attribute.ID, "attending_container")
        .addChildren(partySizeLabel, partySizeInput, entreesLabel, entreesInput, kidsLabel, kidsInput)
        .build();

    Element submitButton = new Element.Builder(Tag.BUTTON).addEscapedText("Submit").build();

    Element form = new Element.Builder(Tag.FORM)
        .setRawAttribute("action", "/rsvp/submit")
        .setRawAttribute("method", "post")
        .addChildren(intro, nameLabel, nameInput, emailLabel, emailInput, attendingLabel,
            getAttendingRadios(), attendingContainer, contactLabel, submitButton)
        .build();

    Element script = new Element.Builder(Tag.SCRIPT)
        .addEscapedText(""
            // TODO
            + "function update() {"
            + "  var attending = $('#attending_yes').is(':checked');"
            + "  if (attending) { $('#attending_container').show({queue: true}); }"
            + "  else { $('#attending_container').hide('400'); }"
            + "  document.getElementsByName('entree').forEach("
            + "    function(e) { e.required = attending; });"
            + "  document.getElementsByName('guest_name')[0].required = attending;"
            + "}"
            + "$(document).ready(update);")
        .build();

    Element document = WeddingLayout.createDocument(Option.some("RSVP"), form, script);
    return HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document);
  }

  private Element getAttendingRadios() {
    String yesRadioId = "attending_yes";
    String noRadioId = "attending_no";

    Element guestAttendingYesInput = new InputBuilder()
        .setType("radio")
        .setId(yesRadioId)
        .setName("attending")
        .setValue("yes")
        .setOnChange("update()")
        .setRequired()
        .build();

    Element guestAttendingYesLabel = new Element.Builder(Tag.LABEL)
        .setRawAttribute("for", yesRadioId)
        .addEscapedText("Yes")
        .build();

    Element guestAttendingNoInput = new InputBuilder()
        .setType("radio")
        .setId(noRadioId)
        .setName("attending")
        .setValue("no")
        .setOnChange("update()")
        .setRequired()
        .build();

    Element guestAttendingNoLabel = new Element.Builder(Tag.LABEL)
        .setRawAttribute("for", noRadioId)
        .addEscapedText("No")
        .build();

    return new ParagraphBuilder()
        .addChild(guestAttendingYesInput)
        .addRawText(" ")
        .addChild(guestAttendingYesLabel)
        .addRawText(" &#160; ")
        .addChild(guestAttendingNoInput)
        .addRawText(" ")
        .addChild(guestAttendingNoLabel)
        .build();
  }

  private HttpResponse getRsvpSubmitResponse(HttpRequest request) {
    SequentialTable<String, String> postData = request.getUrlencodedPostData();
    logger.info("Received RSVP: %s", postData);

    String name = postData.getValues("name").tryGetOnlyElement().getOrThrow();
    String emailOrPhone = postData.getValues("email_or_phone").tryGetOnlyElement().getOrThrow();
    String attending = postData.getValues("attending").tryGetOnlyElement().getOrThrow();
    Option<String> partySize = postData.getValues("party_size").tryGetOnlyElement();
    Option<String> entrees = postData.getValues("entrees").tryGetOnlyElement();
    Option<String> kids = postData.getValues("kids").tryGetOnlyElement();

    RSVP rsvp = new RSVP.Builder()
        .setUUID(UUID.randomUUID().toString())
        .setCreatedAt(Instant.now())
        .setName(name)
        .setEmailOrPhone(emailOrPhone)
        .setAttending(yesOrNoToBoolean(attending))
        .setPartySize(partySize.getOrDefault(""))
        .setEntrees(entrees.getOrDefault(""))
        .setKids(kids.getOrDefault(""))
        .build();

    RSVPStorage.saveNewRSVP(rsvp);
    return HttpResponseFactory.redirectToGet("/rsvp/thanks");
  }

  private static boolean yesOrNoToBoolean(String yesOrNo) {
    switch (yesOrNo) {
      case "yes":
        return true;
      case "no":
        return false;
      default:
        throw new IllegalArgumentException("Expected 'yes' or 'no', got " + yesOrNo);
    }
  }

  private HttpResponse getRsvpThanksResponse() {
    Element thanks = new ParagraphBuilder()
        .addEscapedText("Thanks for RSVPing!")
        .build();

    Element document = WeddingLayout.createDocument(Option.some("RSVP"), thanks);
    return HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document);
  }
}
