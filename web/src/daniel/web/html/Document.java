package daniel.web.html;

public final class Document {
  private final String doctype;
  private final Element html;

  public Document(String doctype, Element html) {
    this.doctype = doctype;
    this.html = html;
  }

  @Override
  public String toString() {
    return String.format("%s\n%s", doctype, html);
  }
}
