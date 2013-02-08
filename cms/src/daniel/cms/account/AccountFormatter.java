package daniel.cms.account;

import daniel.web.html.Element;
import daniel.web.html.Tag;

public final class AccountFormatter {
  private AccountFormatter() {}

  public static Element formatAccountPage(Account account) {
    Element heading = new Element.Builder(Tag.H1)
        .addEscapedText(account.getUsername())
        .build();
    return new Element.Builder(Tag.DIV)
        .addChild(heading)
        .build();
  }
}
