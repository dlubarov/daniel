package daniel.web.http.parsing;

import daniel.data.option.Option;
import daniel.data.util.ArrayUtils;
import daniel.parsing.ParseResult;
import daniel.parsing.Parser;
import daniel.web.http.HttpVersion;
import daniel.web.http.RequestLine;
import daniel.web.http.RequestMethod;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RequestLineParser extends Parser<RequestLine> {
  public static final RequestLineParser singleton = new RequestLineParser();

  private static final Pattern pattern = Pattern.compile("([A-Z]+) ([^ ]+) HTTP/([0-9.]+)");

  private RequestLineParser() {}

  @Override
  public Option<ParseResult<RequestLine>> tryParse(byte[] data, int p) {
    int lineLen = ArrayUtils.firstIndexOf((byte) '\r', data, p)
        .getOrDefault(data.length - p);
    byte[] lineBytes = new byte[lineLen];
    System.arraycopy(data, p, lineBytes, 0, lineLen);
    String line = new String(lineBytes, StandardCharsets.US_ASCII);

    Matcher matcher = pattern.matcher(line);
    if (!matcher.matches())
      return Option.none();

    RequestLine result = new RequestLine.Builder()
        .setMethod(RequestMethod.valueOf(matcher.group(1)))
        .setResource(matcher.group(2))
        .setHttpVersion(HttpVersion.fromVersionString(matcher.group(3)))
        .build();

    return Option.some(new ParseResult<>(result, p + lineLen));
  }
}
