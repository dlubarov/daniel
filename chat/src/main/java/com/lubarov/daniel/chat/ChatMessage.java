package com.lubarov.daniel.chat;

import com.lubarov.daniel.data.util.ToStringBuilder;

final class ChatMessage {
  private final String name;
  private final String message;

  public ChatMessage(String name, String message) {
    this.name = name;
    this.message = message;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("name", name)
        .append("message", message)
        .toString();
  }
}
