package daniel.web.http.multipart;

import daniel.data.sequence.ImmutableArray;
import daniel.data.sequence.ImmutableSequence;
import daniel.data.util.EqualsBuilder;
import daniel.data.util.HashCodeBuilder;
import daniel.web.http.HttpHeader;
import java.util.Arrays;

public final class Part {
  private final ImmutableSequence<HttpHeader> headers;
  private final byte[] body;

  public Part(Iterable<HttpHeader> headers, byte[] body) {
    this.headers = ImmutableArray.copyOf(headers);
    this.body = body;
  }

  public ImmutableSequence<HttpHeader> getHeaders() {
    return headers;
  }

  public byte[] getBody() {
    return body;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Part))
      return false;

    Part that = (Part) o;
    return new EqualsBuilder()
        .append(this.headers, that.headers)
        .append(Arrays.equals(this.body, that.body))
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(headers)
        .append(Arrays.hashCode(body))
        .toHashCode();
  }
}
