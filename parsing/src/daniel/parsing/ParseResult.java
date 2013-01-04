package daniel.parsing;

public final class ParseResult<A> {
  private final A value;
  private final int rem;

  public ParseResult(A value, int rem) {
    this.value = value;
    this.rem = rem;
  }

  public A getValue() {
    return value;
  }

  public int getRem() {
    return rem;
  }
}
