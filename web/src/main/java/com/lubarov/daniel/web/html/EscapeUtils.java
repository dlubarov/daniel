package com.lubarov.daniel.web.html;

public final class EscapeUtils {
  private EscapeUtils() {}

  public static String htmlEncode(String s) {
    // Based on https://www.owasp.org/index.php/XSS_(Cross_Site_Scripting)_Prevention_Cheat_Sheet
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < s.length(); ++i) {
      char c = s.charAt(i);
      switch (c) {
        case '&':
          sb.append("&amp;");
          break;
        case '<':
          sb.append("&lt;");
          break;
        case '>':
          sb.append("&gt;");
          break;
        case '"':
          sb.append("&quot;");
          break;
        case '\'':
          sb.append("&#x27;");
          break;
        case '/':
          sb.append("&#x2F;");
          break;
        case '|':
          sb.append("&#x7C;");
          break;
        default:
          sb.append(c);
      }
    }
    return sb.toString();
  }
}
