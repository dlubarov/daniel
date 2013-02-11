package daniel.web.http.compression;

import daniel.data.table.Table;
import daniel.data.set.MutableHashSet;
import daniel.data.set.Set;
import daniel.web.http.RequestHeaderName;

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
