package daniel.data.source;

import daniel.data.function.Function;
import daniel.data.option.Option;
import daniel.data.sequence.Sequence;
import daniel.data.stack.DynamicArray;
import daniel.data.stack.MutableStack;
import java.util.NoSuchElementException;

public abstract class AbstractSource<A> implements Source<A> {
  @Override
  public Sequence<A> takeUpTo(int n) {
    MutableStack<A> stack = DynamicArray.create();
    for (int i = 0; i < n; ++i) {
      Option<A> optElement = tryTake();
      if (optElement.isEmpty())
        break;
      stack.pushBack(optElement.getOrThrow());
    }
    return stack;
  }

  @Override
  public Sequence<A> takeExactly(int n) {
    Sequence<A> sequence = takeUpTo(n);
    if (sequence.getSize() < n)
      throw new NoSuchElementException(String.format(
          "Source became exhausted before %d elements could be taken.", n));
    return sequence;
  }

  @Override
  public Sequence<A> takeAll() {
    MutableStack<A> stack = DynamicArray.create();
    for (;;) {
      Option<A> optElement = tryTake();
      if (optElement.isEmpty())
        break;
      stack.pushBack(optElement.getOrThrow());
    }
    return stack;
  }

  @Override
  public <B> Source<B> map(Function<? super A, ? extends B> transformation) {
    return new MappedSource<>(this, transformation);
  }

  @Override
  public Source<A> filter(Function<? super A, Boolean> predicate) {
    return new FilteredSource<>(this, predicate);
  }
}
