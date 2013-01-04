package daniel.parsing;

import daniel.data.option.Option;
import daniel.data.sequence.ImmutableArray;
import daniel.data.sequence.ImmutableSequence;

public final class DisjunctiveParser<A> extends Parser<A> {
  private final ImmutableSequence<Parser<A>> delegates;

  public DisjunctiveParser(Parser<A>... delegates) {
    this.delegates = ImmutableArray.<Parser<A>>create(delegates);
  }

  public DisjunctiveParser(Iterable<Parser<A>> delegates) {
    this.delegates = ImmutableArray.copyOf(delegates);
  }

  @Override
  public Option<ParseResult<A>> tryParse(byte[] data, int p) {
    for (Parser<A> delegate : delegates) {
      Option<ParseResult<A>> optResult = delegate.tryParse(data, p);
      if (optResult.isDefined())
        return optResult;
    }
    return Option.none();
  }
}
