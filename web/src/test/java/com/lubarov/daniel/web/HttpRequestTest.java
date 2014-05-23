package com.lubarov.daniel.web;

import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpVersion;
import com.lubarov.daniel.web.http.RequestMethod;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HttpRequestTest {
  @Test
  public void testGetHost() {
    HttpRequest request = new HttpRequest.Builder()
        .setMethod(RequestMethod.GET)
        .setResource("/resource")
        .setHttpVersion(HttpVersion._1_1)
        .addHeader("host", "www.example.com")
        .build();
    assertEquals("www.example.com", request.getHost());
  }
}
