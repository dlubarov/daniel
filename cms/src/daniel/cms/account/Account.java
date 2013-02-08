package daniel.cms.account;

import daniel.data.option.Option;
import daniel.data.unit.Instant;
import daniel.data.util.ToStringBuilder;
import daniel.web.util.UuidUtils;

public final class Account {
  public static final class Builder {
    private Option<String> uuid = Option.none();
    private Option<String> email = Option.none();
    private Option<String> username = Option.none();
    private Option<String> password = Option.none();
    private Option<Instant> registeredAt = Option.none();
    private Option<Instant> lastActiveAt = Option.none();

    public Builder setUuid(String uuid) {
      this.uuid = Option.some(uuid);
      return this;
    }

    public Builder setRandomUiid() {
      return setUuid(UuidUtils.randomAlphanumericUuid());
    }

    public Builder setEmail(String email) {
      this.email = Option.some(email);
      return this;
    }

    public Builder setUsername(String username) {
      this.username = Option.some(username);
      return this;
    }

    public Builder setPassword(String password) {
      this.password = Option.some(password);
      return this;
    }

    public Builder setRegisteredAt(Instant registeredAt) {
      this.registeredAt = Option.some(registeredAt);
      return this;
    }

    public Builder setLastActiveAt(Instant lastActiveAt) {
      this.lastActiveAt = Option.some(lastActiveAt);
      return this;
    }

    public Account build() {
      return new Account(this);
    }
  }

  private final String uuid;
  private final String email;
  private final String username;
  private final String password;
  private final Instant registeredAt;
  private final Instant lastActiveAt;

  private Account(Builder builder) {
    uuid = builder.uuid.getOrThrow();
    email = builder.email.getOrThrow();
    username = builder.username.getOrThrow();
    password = builder.password.getOrThrow();
    registeredAt = builder.registeredAt.getOrThrow();
    lastActiveAt = builder.lastActiveAt.getOrThrow();
  }

  public String getUuid() {
    return uuid;
  }

  public String getEmail() {
    return email;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public Instant getRegisteredAt() {
    return registeredAt;
  }

  public Instant getLastActiveAt() {
    return lastActiveAt;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("uuid", uuid)
        .append("email", email)
        .append("username", username)
        .append("password", password)
        .append("registeredAt", registeredAt)
        .append("lastActiveAt", lastActiveAt)
        .toString();
  }
}
