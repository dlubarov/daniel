package daniel.web.util;

import java.util.regex.Pattern;

public final class UserAgentUtils {
  private static final Pattern pattern = Pattern.compile(
      "Android|BlackBerry|iPhone|iPad|iPod|Opera Mini|IEMobile",
      Pattern.CASE_INSENSITIVE);

  private UserAgentUtils() {}

  public static boolean isMobile(String userAgent) {
    return pattern.matcher(userAgent).find();
  }
}
