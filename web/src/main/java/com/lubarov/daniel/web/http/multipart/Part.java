package com.lubarov.daniel.web.http.multipart;

import com.lubarov.daniel.data.dictionary.KeyValuePair;
import com.lubarov.daniel.data.sequence.ImmutableArray;
import com.lubarov.daniel.data.table.sequential.ImmutableArrayTable;
import com.lubarov.daniel.data.table.sequential.SequentialTable;
import com.lubarov.daniel.data.util.EqualsBuilder;
import com.lubarov.daniel.data.util.HashCodeBuilder;

import java.util.Arrays;

public final class Part {
  private final SequentialTable<String, String> headers;
  private final byte[] body;

  private Part(SequentialTable<String, String> headers, byte[] body) {
    this.headers = headers;
    this.body = body;
  }

  public static Part fromHeaders(Iterable<KeyValuePair<String, String>> headers, byte[] body) {
    return fromKeyValuePairs(ImmutableArray.copyOf(headers), body);
  }

  public static Part fromKeyValuePairs(Iterable<KeyValuePair<String, String>> headers, byte[] body) {
    return new Part(ImmutableArrayTable.copyOf(headers), body);
  }

  public SequentialTable<String, String> getHeaders() {
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
