package com.lubarov.daniel.parsing;

import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.sequence.ImmutableArray;
import com.lubarov.daniel.data.sequence.ImmutableSequence;

public final class DisjunctiveParser<A> extends Parser<A> {
  private final ImmutableSequence<Parser<A>> delegates;

  @SafeVarargs
  public DisjunctiveParser(Parser<A>... delegates) {
    this.delegates = ImmutableArray.create(delegates);
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
