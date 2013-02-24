package daniel.logging;

import daniel.data.option.Option;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public final class Logger {
  private static final DateFormat dateFormat = new SimpleDateFormat("MMM d h:mm a");

  static {
    dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
  }

  private final Class<?> clazz;
  private final LogLevel logLevel;

  private Logger(Class<?> clazz, LogLevel logLevel) {
    this.clazz = clazz;
    this.logLevel = logLevel;
  }

  public static Logger forClass(Class<?> clazz, LogLevel logLevel) {
    return new Logger(clazz, logLevel);
  }

  public static Logger forClass(Class<?> clazz) {
    return new Logger(clazz, LogLevel.getDefault());
  }

  public void trace(Throwable throwable, String format, Object... args) {
    log(LogLevel.TRACE, Option.<Throwable>some(throwable), format, args);
  }

  public void trace(String format, Object... args) {
    log(LogLevel.TRACE, Option.<Throwable>none(), format, args);
  }

  public void debug(Throwable throwable, String format, Object... args) {
    log(LogLevel.DEBUG, Option.<Throwable>some(throwable), format, args);
  }

  public void debug(String format, Object... args) {
    log(LogLevel.DEBUG, Option.<Throwable>none(), format, args);
  }

  public void info(Throwable throwable, String format, Object... args) {
    log(LogLevel.INFO, Option.<Throwable>some(throwable), format, args);
  }

  public void info(String format, Object... args) {
    log(LogLevel.INFO, Option.<Throwable>none(), format, args);
  }

  public void warn(Throwable throwable, String format, Object... args) {
    log(LogLevel.WARN, Option.<Throwable>some(throwable), format, args);
  }

  public void warn(String format, Object... args) {
    log(LogLevel.WARN, Option.<Throwable>none(), format, args);
  }

  public void error(Throwable throwable, String format, Object... args) {
    log(LogLevel.ERROR, Option.<Throwable>some(throwable), format, args);
  }

  public void error(String format, Object... args) {
    log(LogLevel.ERROR, Option.<Throwable>none(), format, args);
  }

  public void fatal(Throwable throwable, String format, Object... args) {
    log(LogLevel.FATAL, Option.<Throwable>some(throwable), format, args);
  }

  public void fatal(String format, Object... args) {
    log(LogLevel.FATAL, Option.<Throwable>none(), format, args);
  }

  private void log(
      LogLevel logLevel, Option<Throwable> optThrowable,
      String format, Object[] args) {
    if (logLevel.ordinal() < this.logLevel.ordinal())
      return;

    String output = String.format("[%s] [%s] %s: %s",
        getDateString(),
        clazz.getName(),
        logLevel,
        String.format(format, args));

    if (optThrowable.isDefined()) {
      StringWriter stackTraceWriter = new StringWriter();
      optThrowable.getOrThrow().printStackTrace(new PrintWriter(stackTraceWriter));
      output += "\n" + stackTraceWriter.toString();
    }

    System.out.println(output);
  }

  private static String getDateString() {
    return dateFormat.format(new Date());
  }
}
