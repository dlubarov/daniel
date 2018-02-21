package com.lubarov.daniel.wedding;

import com.lubarov.daniel.common.Logger;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.sequence.ImmutableArray;
import com.lubarov.daniel.data.sequence.ImmutableSequence;
import com.lubarov.daniel.data.table.sequential.SequentialTable;
import com.lubarov.daniel.data.unit.Instant;
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
import com.lubarov.daniel.wedding.rsvp.RSVP;
import com.lubarov.daniel.wedding.rsvp.RSVPStorage;

import java.util.UUID;

// TODO validate required fields
public class WeddingRSVPHandler implements PartialHandler {
  public static final WeddingRSVPHandler singleton = new WeddingRSVPHandler();

  private static final Logger logger = Logger.forClass(WeddingRSVPHandler.class);
  private static final ImmutableSequence<String> ENTREES =
      ImmutableArray.create("Filet mignon", "Salmon", "Risotto");

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
        .addEscapedText("Please RSVP by May 3, or let us know if you need more time.")
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
        .addEscapedText("Your email")
        .addChild(redAsterisk)
        .build();

    Element emailInput = new InputBuilder()
        .setType("email")
        .setName("email")
        .setRequired()
        .build();

    Element attendingLabel = new ParagraphBuilder()
        .addEscapedText("Will you be attending?")
        .addChild(redAsterisk)
        .build();

    Element entreeLabel = new ParagraphBuilder()
        .addEscapedText("Preferred dinner entree")
        .addChild(redAsterisk)
        .build();
    Element entreeRadios = getEntreeRadios(false);

    Element guestAttendingLabel = new ParagraphBuilder()
        .addEscapedText("Will you be bringing a guest?")
        .addChild(redAsterisk)
        .build();

    Element guestNameLabel = new ParagraphBuilder()
        .addEscapedText("Your guest's full name")
        .addChild(redAsterisk)
        .build();

    Element guestNameInput = new InputBuilder()
        .setType("text")
        .setName("guest_name")
        .build();

    Element guestEntreeLabel = new ParagraphBuilder()
        .addEscapedText("Guest's preferred dinner entree")
        .addChild(redAsterisk)
        .build();
    Element guestEntreeRadios = getEntreeRadios(true);

    Element guestAttendingContainer = new Element.Builder(Tag.DIV)
        .setRawAttribute(Attribute.ID, "guest_attending_container")
        .addChildren(guestNameLabel, guestNameInput, guestEntreeLabel, guestEntreeRadios)
        .build();

    Element attendingContainer = new Element.Builder(Tag.DIV)
        .setRawAttribute(Attribute.ID, "attending_container")
        .addChildren(entreeLabel, entreeRadios, guestAttendingLabel, getAttendingRadios(true),
            guestAttendingContainer)
        .build();

    Element notesLabel = new ParagraphBuilder().addEscapedText("Notes (optional)").build();
    Element notesInput = new Element.Builder(Tag.TEXTAREA).setRawAttribute("name", "notes").build();

    Element submitButton = new Element.Builder(Tag.BUTTON).addEscapedText("Submit").build();

    Element form = new Element.Builder(Tag.FORM)
        .setRawAttribute("action", "/rsvp/submit")
        .setRawAttribute("method", "post")
        .addChildren(intro, nameLabel, nameInput, emailLabel, emailInput, attendingLabel,
            getAttendingRadios(false), attendingContainer, notesLabel, notesInput, submitButton)
        .build();

    Element script = new Element.Builder(Tag.SCRIPT)
        .addEscapedText(""
            + "function update() {"
            + "  var attending = $('#attending_yes').is(':checked');"
            + "  var guestAttending = $('#guest_attending_yes').is(':checked');"
            + "  if (attending) { $('#attending_container').show({queue: true}); }"
            + "  else { $('#attending_container').hide('400'); }"
            + "  if (guestAttending) { $('#guest_attending_container').show({queue: true}); }"
            + "  else { $('#guest_attending_container').hide('400'); }"
            + "  document.getElementsByName('entree').forEach("
            + "    function(e) { e.required = attending; });"
            + "  document.getElementsByName('guest_attending').forEach("
            + "    function(e) { e.required = attending; });"
            + "  document.getElementsByName('guest_name')[0].required = guestAttending;"
            + "  document.getElementsByName('guest_entree').forEach("
            + "    function(e) { e.required = guestAttending; });"
            + "}"
            + "$(document).ready(update);")
        .build();

    Element document = WeddingLayout.createDocument(Option.some("RSVP"), form, script);
    return HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document);
  }

  private Element getEntreeRadios(boolean guest) {
    String inputName = guest ? "guest_entree" : "entree";
    ParagraphBuilder paragraphBuilder = new ParagraphBuilder();

    ENTREES.forEach(entree -> {
      if (!entree.equals(ENTREES.getFront())) {
        paragraphBuilder.addRawText("<br />");
      }

      String inputId = inputName + "_" + entree.toLowerCase().replace(" ", "_");
      Element radio = new InputBuilder()
          .setType("radio")
          .setId(inputId)
          .setName(inputName)
          .setValue(entree)
          .setRequired()
          .build();
      String labelText = entree;
      if (entree.equalsIgnoreCase("filet mignon"))
        labelText += " with mashed potatoes";
      if (entree.equalsIgnoreCase("salmon"))
        labelText += " with rice pilaf";
      if (entree.equalsIgnoreCase("risotto"))
        labelText += " (vegetarian)";
      Element label = new Element.Builder(Tag.LABEL)
          .setRawAttribute("for", inputId)
          .addEscapedText(labelText)
          .build();
      paragraphBuilder.addChild(radio).addRawText(" ").addChild(label);
    });

    return paragraphBuilder.build();
  }

  private Element getAttendingRadios(boolean guest) {
    String inputName = guest ? "guest_attending" : "attending";
    String yesRadioId = inputName + "_yes";
    String noRadioId = inputName + "_no";

    Element guestAttendingYesInput = new InputBuilder()
        .setType("radio")
        .setId(yesRadioId)
        .setName(inputName)
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
        .setName(inputName)
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
    String email = postData.getValues("email").tryGetOnlyElement().getOrThrow();
    String attending = postData.getValues("attending").tryGetOnlyElement().getOrThrow();
    Option<String> entree = postData.getValues("entree").tryGetOnlyElement();
    Option<String> guestAttending = postData.getValues("guest_attending").tryGetOnlyElement();
    Option<String> guestName = postData.getValues("guest_name").tryGetOnlyElement();
    Option<String> guestEntree = postData.getValues("guest_entree").tryGetOnlyElement();
    String notes = postData.getValues("notes").tryGetOnlyElement().getOrThrow();

    RSVP rsvp = new RSVP.Builder()
        .setUUID(UUID.randomUUID().toString())
        .setCreatedAt(Instant.now())
        .setName(name)
        .setEmail(email)
        .setAttending(yesOrNoToBoolean(attending))
        .setEntree(entree.getOrDefault(""))
        .setGuestAttending(guestAttending.isDefined() && yesOrNoToBoolean(guestAttending.getOrThrow()))
        .setGuestName(guestName.getOrDefault(""))
        .setGuestEntree(guestEntree.getOrDefault(""))
        .setNotes(notes)
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
