package com.lubarov.daniel.wedding;

import com.lubarov.daniel.common.Logger;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.table.sequential.SequentialTable;
import com.lubarov.daniel.data.unit.Instant;
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
    Element redAsterisk = new Element.Builder(Tag.SPAN)
        .setRawAttribute("style", "color: red;")
        .addEscapedText(" *")
        .build();

    Element nameLabel = new ParagraphBuilder()
        .addEscapedText("Your name")
        .addChild(redAsterisk)
        .build();

    Element nameInput = new InputBuilder()
        .setType("text")
        .setName("name")
        .build();

    Element emailLabel = new ParagraphBuilder()
        .addEscapedText("Your email")
        .addChild(redAsterisk)
        .build();

    Element emailInput = new InputBuilder()
        .setType("text")
        .setName("email")
        .build();

    Element attendingLabel = new ParagraphBuilder()
        .addEscapedText("Will you be attending?")
        .addChild(redAsterisk)
        .build();

    Element guestAttendingLabel = new ParagraphBuilder()
        .addEscapedText("Will you be bringing a guest?")
        .addChild(redAsterisk)
        .build();

    Element guestNameLabel = new ParagraphBuilder()
        .addEscapedText("Your guest's name")
        .build();

    Element guestNameInput = new InputBuilder()
        .setType("text")
        .setName("guest_name")
        .build();

    Element notesLabel = new ParagraphBuilder().addEscapedText("Notes (optional)").build();
    Element notesInput = new Element.Builder(Tag.TEXTAREA).setRawAttribute("name", "notes").build();

    Element submitButton = new Element.Builder(Tag.BUTTON).addEscapedText("Submit").build();

    Element form = new Element.Builder(Tag.FORM)
        .setRawAttribute("action", "/rsvp/submit")
        .setRawAttribute("method", "post")
        .addChildren(nameLabel, nameInput, emailLabel, emailInput, attendingLabel,
            getAttendingInputs(), guestAttendingLabel, getGuestAttendingInputs(), guestNameLabel,
            guestNameInput, notesLabel, notesInput, submitButton)
        .build();

    Element document = WeddingLayout.createDocument(Option.some("RSVP"), form);

    return HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document);
  }

  private Element getAttendingInputs() {
    Element attendingYesInput = new InputBuilder()
        .setType("radio")
        .setId("attending_yes")
        .setName("attending")
        .setValue("yes")
        .build();

    Element attendingYesLabel = new Element.Builder(Tag.LABEL)
        .setRawAttribute("for", "attending_yes")
        .addEscapedText("Yes")
        .build();

    Element attendingNoInput = new InputBuilder()
        .setType("radio")
        .setId("attending_no")
        .setName("attending")
        .setValue("no")
        .build();

    Element attendingNoLabel = new Element.Builder(Tag.LABEL)
        .setRawAttribute("for", "attending_no")
        .addEscapedText("No")
        .build();

    return new ParagraphBuilder()
        .addChild(attendingYesInput)
        .addRawText(" ")
        .addChild(attendingYesLabel)
        .addRawText(" &#160; ")
        .addChild(attendingNoInput)
        .addRawText(" ")
        .addChild(attendingNoLabel)
        .build();
  }

  private Element getGuestAttendingInputs() {
    Element guestAttendingYesInput = new InputBuilder()
        .setType("radio")
        .setId("guest_attending_yes")
        .setName("guest_attending")
        .setValue("yes")
        .build();

    Element guestAttendingYesLabel = new Element.Builder(Tag.LABEL)
        .setRawAttribute("for", "guest_attending_yes")
        .addEscapedText("Yes")
        .build();

    Element guestAttendingNoInput = new InputBuilder()
        .setType("radio")
        .setId("guest_attending_no")
        .setName("guest_attending")
        .setValue("no")
        .build();

    Element guestAttendingNoLabel = new Element.Builder(Tag.LABEL)
        .setRawAttribute("for", "guest_attending_no")
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
    String guestAttending = postData.getValues("guest_attending").tryGetOnlyElement().getOrThrow();
    String guestName = postData.getValues("guest_name").tryGetOnlyElement().getOrThrow();
    String notes = postData.getValues("notes").tryGetOnlyElement().getOrThrow();

    RSVP rsvp = new RSVP.Builder()
        .setUUID(UUID.randomUUID().toString())
        .setCreatedAt(Instant.now())
        .setName(name)
        .setEmail(email)
        .setAttending(yesOrNoToBoolean(attending))
        .setGuestAttending(yesOrNoToBoolean(guestAttending))
        .setGuestName(guestName)
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
