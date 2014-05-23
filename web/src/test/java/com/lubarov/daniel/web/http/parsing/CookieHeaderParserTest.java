package com.lubarov.daniel.web.http.parsing;

import com.lubarov.daniel.data.dictionary.KeyValuePair;
import com.lubarov.daniel.data.table.sequential.SequentialTable;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class CookieHeaderParserTest {
  @Test
  public void testTryParse_basic() {
    String cookieHeader = "$Version=1; Skin=new;";
    byte[] cookieHeaderBytes = cookieHeader.getBytes(StandardCharsets.US_ASCII);
    SequentialTable<String, String> result =
        CookieHeaderParser.singleton.tryParse(cookieHeaderBytes, 0)
            .getOrThrow().getValue();

    assertEquals(2, result.getSize());
    assertEquals(new KeyValuePair<>("$Version", "1"), result.get(0));
    assertEquals(new KeyValuePair<>("Skin", "new"), result.get(1));
  }

  @Test
  public void testTryParse_googleAnalytics() {
    String cookieHeader = "__utma=11106849.1694674570.1364289598.1364289598.1364289598.1; __utmc=11106849; __utmz=11106849.1364289598.1.1.utmcsr=stackoverflow.com|utmccn=(referral)|utmcmd=referral|utmcct=/users/714009/daniel";
    byte[] cookieHeaderBytes = cookieHeader.getBytes(StandardCharsets.US_ASCII);
    SequentialTable<String, String> result = CookieHeaderParser.singleton
        .tryParse(cookieHeaderBytes, 0).getOrThrow().getValue();

    assertEquals(3, result.getSize());
    assertEquals(
        new KeyValuePair<>("__utma", "11106849.1694674570.1364289598.1364289598.1364289598.1"),
        result.get(0));
    assertEquals(
        new KeyValuePair<>("__utmc", "11106849"),
        result.get(1));
    assertEquals(
        new KeyValuePair<>("__utmz", "11106849.1364289598.1.1.utmcsr=stackoverflow.com|utmccn=(referral)|utmcmd=referral|utmcct=/users/714009/daniel"),
        result.get(2));
  }
}
