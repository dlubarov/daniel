package com.lubarov.daniel.wedding.rsvp;

import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.order.AbstractOrdering;
import com.lubarov.daniel.data.order.Ordering;
import com.lubarov.daniel.data.order.Relation;
import com.lubarov.daniel.data.unit.Instant;

public class RSVP {
  public static final Ordering<RSVP> ASCENDING_CREATED_AT_ORDERING =
      new AbstractOrdering<RSVP>() {
        @Override
        public Relation compare(RSVP a, RSVP b) {
          return Instant.ASCENDING_ORDERING.compare(a.createdAt, b.createdAt);
        }
      };

  public static final class Builder {
    private Option<String> uuid = Option.none();
    private Option<Instant> createdAt = Option.none();
    private Option<String> name = Option.none();
    private Option<String> email = Option.none();
    private Option<Boolean> attending = Option.none();
    private Option<Boolean> guestAttending = Option.none();
    private Option<String> guestName = Option.none();
    private Option<String> notes = Option.none();

    public Builder setUUID(String uuid) {
      this.uuid = Option.some(uuid);
      return this;
    }

    public Builder setCreatedAt(Instant createdAt) {
      this.createdAt = Option.some(createdAt);
      return this;
    }

    public Builder setName(String name) {
      this.name = Option.some(name);
      return this;
    }

    public Builder setEmail(String email) {
      this.email = Option.some(email);
      return this;
    }

    public Builder setAttending(boolean attending) {
      this.attending = Option.some(attending);
      return this;
    }

    public Builder setGuestAttending(Boolean guestAttending) {
      this.guestAttending = Option.some(guestAttending);
      return this;
    }

    public Builder setGuestName(String guestName) {
      this.guestName = Option.some(guestName);
      return this;
    }

    public Builder setNotes(String notes) {
      this.notes = Option.some(notes);
      return this;
    }

    public RSVP build() {
      return new RSVP(this);
    }
  }

  public final String uuid;
  public final Instant createdAt;
  public final String name;
  public final String email;
  public final boolean attending;
  public final boolean guestAttending;
  public final String guestName;
  public final String notes;

  private RSVP(Builder builder) {
    uuid = builder.uuid.getOrThrow("Missing UUID");
    createdAt = builder.createdAt.getOrThrow("Missing created at");
    name = builder.name.getOrThrow("Missing name");
    email = builder.email.getOrThrow("Missing email");
    attending = builder.attending.getOrThrow("Missing attending");
    guestAttending = builder.guestAttending.getOrThrow("Missing guest attending");
    guestName = builder.guestName.getOrThrow("Missing guest name");
    notes = builder.notes.getOrThrow("Missing notes");
  }
}
