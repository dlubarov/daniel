package com.lubarov.daniel.conniewedding.rsvp;

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
    private Option<String> emailOrPhone = Option.none();
    private Option<Boolean> attending = Option.none();
    private Option<String> partySize = Option.none();
    private Option<String> entrees = Option.none();
    private Option<String> kids = Option.none();

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

    public Builder setEmailOrPhone(String emailOrPhone) {
      this.emailOrPhone = Option.some(emailOrPhone);
      return this;
    }

    public Builder setAttending(boolean attending) {
      this.attending = Option.some(attending);
      return this;
    }

    public Builder setPartySize(String partySize) {
      this.partySize = Option.some(partySize);
      return this;
    }

    public Builder setEntrees(String entrees) {
      this.entrees = Option.some(entrees);
      return this;
    }

    public Builder setKids(String kids) {
      this.kids = Option.some(kids);
      return this;
    }

    public RSVP build() {
      return new RSVP(this);
    }
  }

  public final String uuid;
  public final Instant createdAt;
  public final String name;
  public final String emailOrPhone;
  public final boolean attending;
  public final String partySize;
  public final String entrees;
  public final String kids;

  private RSVP(Builder builder) {
    uuid = builder.uuid.getOrThrow("Missing UUID");
    createdAt = builder.createdAt.getOrThrow("Missing created at");
    name = builder.name.getOrThrow("Missing name");
    emailOrPhone = builder.emailOrPhone.getOrThrow("Missing emailOrPhone");
    attending = builder.attending.getOrThrow("Missing attending");
    partySize = builder.partySize.getOrThrow("Missing partySize");
    entrees = builder.entrees.getOrThrow("Missing entrees");
    kids = builder.kids.getOrThrow("Missing kids");
  }
}
