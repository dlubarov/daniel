package daniel.web.http;

import daniel.data.unit.Instant;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public final class DateUtils {
  private static final DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");

  static {
    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
  }

  private DateUtils() {}

  public static String formatDate(Date date) {
    synchronized (dateFormat) {
      return dateFormat.format(date);
    }
  }

  public static String formatInstant(Instant instant) {
    return formatDate(instant.toDate());
  }

  public static Date parseDate(String dateString) throws ParseException {
    synchronized (dateFormat) {
      return dateFormat.parse(dateString);
    }
  }

  public static Instant parseInstant(String dateString) throws ParseException {
    return Instant.fromDate(parseDate(dateString));
  }
}
