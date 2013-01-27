package daniel.logging;

public enum LogLevel {
  TRACE, DEBUG, INFO, WARN, ERROR, FATAL;

  public static LogLevel getDefault() {
    String logLevel = System.getenv("LOG");
    if (logLevel == null)
      return INFO;
    return LogLevel.valueOf(logLevel.toUpperCase());
  }
}
