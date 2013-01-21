package daniel.web.http;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtils {
  private static final DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");

  private DateUtils() {}

  public static String formatDate(Date date) {
    synchronized (dateFormat) {
      return dateFormat.format(date);
    }
  }
}
