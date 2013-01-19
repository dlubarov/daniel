package daniel.data.function;

public final class IdentityFunction<A> implements Function<A, A> {
  @Override
  public A apply(A input) {
    return input;
  }
}
