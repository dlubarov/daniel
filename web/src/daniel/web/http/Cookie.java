package daniel.web.http;

import daniel.data.option.Option;
import java.util.Date;

/**
 * An HTTP cookie.
 */
public class Cookie {
  public static class Builder {
    private Option<String> name = Option.none();
    private Option<String> value = Option.none();
    private Option<Date> expires = Option.none();

    public Builder setName(String name) {
      this.name = Option.some(name);
      return this;
    }

    public Builder setValue(String value) {
      this.value = Option.some(value);
      return this;
    }

    public Builder setExpires(Date expires) {
      this.expires = Option.some(expires);
      return this;
    }
  }

  private final String name;
  private final String value;
  private final Option<Date> expires;

  public Cookie(Builder builder) {
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

  public Option<Date> getExpires() {
    return expires;
  }
}
