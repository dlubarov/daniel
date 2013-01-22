package daniel.web.http.cookies;

import daniel.data.option.Option;
import daniel.data.unit.Instant;

/**
 * An HTTP cookie.
 */
public final class Cookie {
  public static final class Builder {
    private Option<String> name = Option.none();
    private Option<String> value = Option.none();
    private Option<Instant> expires = Option.none();

    public Builder setName(String name) {
      this.name = Option.some(name);
      return this;
    }

    public Builder setValue(String value) {
      this.value = Option.some(value);
      return this;
    }

    public Builder setExpires(Instant expires) {
      this.expires = Option.some(expires);
      return this;
    }

    public Cookie build() {
      return new Cookie(this);
    }
  }

  private final String name;
  private final String value;
  private final Option<Instant> expires;

  private Cookie(Builder builder) {
    name = builder.name.getOrThrow("No name was set.");
    value = builder.value.getOrThrow("No value was set.");
    expires = builder.expires;
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }

  public Option<Instant> getExpires() {
    return expires;
  }
}
