package com.lubarov.daniel.blog;

import com.lubarov.daniel.data.collection.Collection;
import com.lubarov.daniel.data.sequence.SinglyLinkedList;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.MemorySessionData;

public final class Notifications {
  private static final MemorySessionData<SinglyLinkedList<String>> messageData =
      new MemorySessionData<>();

  private Notifications() {}

  public static void addMessage(HttpRequest request, String message) {
    messageData.set(request, getMessages(request).plusFront(message));
  }

  public static Collection<String> getAndClearMessages(HttpRequest request) {
    Collection<String> messages = getMessages(request);
    clearMessages(request);
    return messages;
  }

  private static SinglyLinkedList<String> getMessages(HttpRequest request) {
    return messageData.tryGet(request).getOrDefault(SinglyLinkedList.<String>create());
  }

  private static void clearMessages(HttpRequest request) {
    messageData.tryClear(request);
  }
}
