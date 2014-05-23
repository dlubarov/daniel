package com.lubarov.daniel.web.html;

public final class Xhtml5Document {
  private final Element html;

  public Xhtml5Document(Element html) {
    this.html = html;
  }

  @Override
  public String toString() {
    return String.format("%s\n%s\n%s",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
        "<!DOCTYPE html>",
        html);
  }
}
