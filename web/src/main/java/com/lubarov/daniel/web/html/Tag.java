package com.lubarov.daniel.web.html;

public enum Tag {
  // Taken from https://developer.mozilla.org/en-US/docs/HTML/HTML5/HTML5_element_list
  HTML, HEAD, TITLE, BASE, LINK, META, STYLE, SCRIPT, NOSCRIPT, BODY, SECTION, NAV, ARTICLE, ASIDE,
  H1, H2, H3, H4, H5, H6, HGROUP, HEADER, FOOTER, ADDRESS, P, HR, PRE, BLOCKQUOTE, OL, UL, LI, DL,
  DT, DD, FIGURE, FIGCAPTION, DIV, A, EM, STRONG, SMALL, S, CITE, Q, DFN, ABBR, DATA, TIME, CODE,
  VAR, SAMP, KBD, SUB, SUP, I, B, U, MARK, RUBY, RT, RP, BDI, BDO, SPAN, BR, WBR, INS, DEL, IMG,
  IFRAME, EMBED, OBJECT, PARAM, VIDEO, AUDIO, SOURCE, TRACK, CANVAS, MAP, AREA, SVG, MATH, TABLE,
  CAPTION, COLGROUP, COL, TBODY, THEAD, TFOOT, TR, TD, TH, FORM, FIELDSET, LEGEND, LABEL, INPUT,
  BUTTON, SELECT, DATALIST, OPTGROUP, OPTION, TEXTAREA, KEYGEN, OUTPUT, PROGRESS, METER, DETAILS,
  SUMMARY, COMMAND, MENU;

  @Override
  public String toString() {
    return name().toLowerCase();
  }
}
