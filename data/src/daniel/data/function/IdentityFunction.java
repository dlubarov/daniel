package daniel.data.function;

public class IdentityFunction<A> implements Function<A, A> {
  @Override
  public A apply(A input) {
    return input;
  }
}
