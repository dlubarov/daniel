package daniel.web.html;

public enum Attribute {
  // Taken from https://developer.mozilla.org/en-US/docs/HTML/Attributes
  ACCEPT, ACCEPT_CHARSET, ACCESSKEY, ACTION, ALIGN, ALT, ASYNC, AUTOCOMPLETE, AUTOFOCUS, AUTOPLAY,
  BGCOLOR, BORDER, BUFFERED, CHALLENGE, CHARSET, CHECKED, CITE, CLASS, CODE, CODEBASE, COLOR, COLS,
  COLSPAN, CONTENT, CONTENTEDITABLE, CONTEXTMENU, CONTROLS, COORDS, DATA, DATETIME, DEFAULT, DEFER,
  DIR, DIRNAME, DISABLED, DRAGGABLE, DROPZONE, ENCTYPE, FOR, FORM, HEADERS, HEIGHT, HIDDEN, HIGH,
  HREF, HREFLANG, HTTP_EQUIV, ICON, ID, ISMAP, ITEMPROP, KEYTYPE, KIND, LABEL, LANG, LANGUAGE, LIST,
  LOOP, LOW, MANIFEST, MAX, MAXLENGTH, MEDIA, METHOD, MIN, MULTIPLE, NAME, NOVALIDATE, OPEN,
  OPTIMUM, PATTERN, PING, PLACEHOLDER, POSTER, PRELOAD, PUBDATE, RADIOGROUP, READONLY, REL,
  REQUIRED, REVERSED, ROWS, ROWSPAN, SANDBOX, SPELLCHECK, SCOPE, SCOPED, SEAMLESS, SELECTED, SHAPE,
  SIZE, SIZES, SPAN, SRC, SRCDOC, SRCLANG, START, STEP, STYLE, SUMMARY, TABINDEX, TARGET, TITLE,
  TYPE, USEMAP, VALUE, WIDTH, WRAP;

  @Override
  public String toString() {
    return name().replace('_', '-').toLowerCase();
  }
}
