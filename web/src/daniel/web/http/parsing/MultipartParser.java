package daniel.web.http.parsing;

import daniel.data.option.Option;
import daniel.data.sequence.Sequence;
import daniel.data.stack.DynamicArray;
import daniel.data.stack.MutableStack;
import daniel.data.util.ArrayUtils;
import daniel.parsing.ParseResult;
import daniel.parsing.Parser;
import daniel.web.http.HttpHeader;
import daniel.web.http.multipart.Part;
import java.nio.charset.StandardCharsets;

public final class MultipartParser extends Parser<Sequence<Part>> {
  private static final byte[] CRLFCRLF = "\r\n\r\n".getBytes(StandardCharsets.US_ASCII);

  private final byte[] ddBoundaryCrlf;
  private final byte[] crlfDdBoundary;

  public MultipartParser(String boundary) {
    this.ddBoundaryCrlf = ("--" + boundary + "\r\n").getBytes(StandardCharsets.US_ASCII);
    this.crlfDdBoundary = ("\r\n--" + boundary).getBytes(StandardCharsets.US_ASCII);
  }

  @Override
  public Option<ParseResult<Sequence<Part>>> tryParse(byte[] data, int p) {
    if (ArrayUtils.firstIndexOf(ddBoundaryCrlf, data, p).getOrDefault(-1) != p)
      return Option.none();
    p += ddBoundaryCrlf.length;

    MutableStack<Part> parts = DynamicArray.create();
    for (;;) {
      ParseResult<Sequence<HttpHeader>> resHeaders =
          HeaderSectionParser.singleton.tryParse(data, p).getOrThrow();
      p = resHeaders.getRem();
      if (ArrayUtils.firstIndexOf(CRLFCRLF, data, p).getOrDefault(-1) != p)
        throw new RuntimeException("Expected CRLFCRLF after header section.");
      p = resHeaders.getRem() + CRLFCRLF.length;

      int pEnd = ArrayUtils.firstIndexOf(crlfDdBoundary, data, p)
          .getOrThrow("Expected CRLF--[boundary].");
      byte[] body = new byte[pEnd - p];
      System.arraycopy(data, p, body, 0, body.length);
      parts.pushBack(Part.fromHeaders(resHeaders.getValue(), body));
      p = pEnd + crlfDdBoundary.length;

      if (data[p] == '-' && data[p + 1] == '-')
        return Option.some(new ParseResult<Sequence<Part>>(parts, p + 2));
      if (data[p] != '\r' || data[p + 1] != '\n')
        throw new RuntimeException("Expected CRLF after --[boundary].");
      p += 2;
    }
  }
}
