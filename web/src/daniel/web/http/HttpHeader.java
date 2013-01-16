package daniel.web.http;

import daniel.data.dictionary.KeyValuePair;
import daniel.data.function.Function;
import daniel.data.util.EqualsBuilder;
import daniel.data.util.HashCodeBuilder;

public class HttpHeader {
  public static final Function<HttpHeader, String> getNameFunction =
      new Function<HttpHeader, String>() {
        @Override public String apply(HttpHeader header) {
          return header.getName();
        }
      };

  public static final Function<HttpHeader, String> getValueFunction =
      new Function<HttpHeader, String>() {
        @Override public String apply(HttpHeader header) {
          return header.getValue();
        }
      };

  public static final Function<HttpHeader, KeyValuePair<String, String>> toKeyValuePairFunction =
      new Function<HttpHeader, KeyValuePair<String, String>>() {
        @Override public KeyValuePair<String, String> apply(HttpHeader header) {
          return header.toKeyValuePair();
        }
      };

  private final String name;
  private final String value;

  public HttpHeader(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public HttpHeader(RequestHeaderName name, String value) {
    this(name.getStandardName(), value);
  }

  public HttpHeader(ResponseHeaderName name, String value) {
    this(name.getStandardName(), value);
  }

  public HttpHeader(KeyValuePair<String, String> keyValuePair) {
    this(keyValuePair.getKey(), keyValuePair.getValue());
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }

  public KeyValuePair<String, String> toKeyValuePair() {
    return new KeyValuePair<>(name, value);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof HttpHeader))
      return false;

    HttpHeader that = (HttpHeader) o;
    return new EqualsBuilder()
        .append(this.name, that.name)
        .append(this.value, that.value)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(name)
        .append(value)
        .toHashCode();
  }

  @Override
  public String toString() {
    return String.format("%s: %s", name, value);
  }
}
