package com.lubarov.daniel.junkmail;

import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpResponse;
import com.lubarov.daniel.web.http.server.PartialHandler;

public class InboxHandler implements PartialHandler {
  public static final InboxHandler singleton = new InboxHandler();

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    return Option.none();
  }
}
