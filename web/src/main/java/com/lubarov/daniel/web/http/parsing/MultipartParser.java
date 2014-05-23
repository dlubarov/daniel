package com.lubarov.daniel.web.http.parsing;

import com.lubarov.daniel.data.dictionary.KeyValuePair;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.sequence.Sequence;
import com.lubarov.daniel.data.stack.DynamicArray;
import com.lubarov.daniel.data.stack.MutableStack;
import com.lubarov.daniel.data.util.ArrayUtils;
import com.lubarov.daniel.parsing.ParseResult;
import com.lubarov.daniel.parsing.Parser;
import com.lubarov.daniel.web.http.multipart.Part;

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
      ParseResult<Sequence<KeyValuePair<String, String>>> resHeaders =
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
        return Option.some(new ParseResult<>(parts, p + 2));
      if (data[p] != '\r' || data[p + 1] != '\n')
        throw new RuntimeException("Expected CRLF after --[boundary].");
      p += 2;
    }
  }
}
