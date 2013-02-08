package daniel.cms.account;

import daniel.cms.Config;
import daniel.cms.Layout;
import daniel.data.option.Option;
import daniel.web.html.Element;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.HttpStatus;
import daniel.web.http.server.PartialHandler;
import daniel.web.http.server.util.HttpResponseFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ViewAccountHandler implements PartialHandler {
  public static final ViewAccountHandler singleton = new ViewAccountHandler();

  private static final Pattern resourcePattern = Pattern.compile("/user/([0-9a-zA-Z-]+)/?(.*)");

  private ViewAccountHandler() {}

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    Matcher matcher = resourcePattern.matcher(request.getResource());
    if (!matcher.matches())
      return Option.none();

    String uuid = matcher.group(1);
    String username = matcher.group(2);

    Option<Account> optAccount = AccountStorage.getAccountByUuid(uuid);
    if (optAccount.isEmpty())
      return Option.none();
    Account account = optAccount.getOrThrow();

    String preferredResource = String.format("/user/%s/%s", uuid, account.getUsername());
    if (!request.getResource().equals(preferredResource)) {
      String location = Config.getBaseUrl() + preferredResource;
      return Option.some(HttpResponseFactory.permanentRedirect(location));
    }

    Element html = Layout.createHtml(AccountFormatter.formatAccountPage(account));
    return Option.some(HttpResponseFactory.xhtmlResponse(HttpStatus.OK, html));
  }
}
