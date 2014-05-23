package com.lubarov.daniel.web.http.compression;

import com.lubarov.daniel.data.set.MutableHashSet;
import com.lubarov.daniel.data.set.Set;
import com.lubarov.daniel.data.table.Table;
import com.lubarov.daniel.web.http.RequestHeaderName;

public final class AcceptEncodingParser {
  private AcceptEncodingParser() {}

  public static Set<String> getAcceptedEncodings(Table<String, String> headers) {
    MutableHashSet<String> acceptedEncodings = MutableHashSet.create();
    for (String encodings : headers.getValues(RequestHeaderName.ACCEPT_ENCODING.getStandardName()))
      for (String s : encodings.split(","))
        acceptedEncodings.tryAdd(s.trim());
    return acceptedEncodings;
  }
}
