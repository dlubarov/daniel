package com.lubarov.daniel.parsing;

import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.sequence.Sequence;
import com.lubarov.daniel.data.stack.DynamicArray;
import com.lubarov.daniel.data.stack.MutableStack;

public final class RepetitionParser<A> extends Parser<Sequence<A>> {
  private final Parser<A> delegate;
  private final int minTimes;
  private final int maxTimes;

  private RepetitionParser(Parser<A> delegate, int minTimes, int maxTimes) {
    this.delegate = delegate;
    this.minTimes = minTimes;
    this.maxTimes = maxTimes;
  }

  public static <A> RepetitionParser<A> repeatArbitrarily(Parser<A> delegate) {
    return new RepetitionParser<>(delegate, 0, Integer.MAX_VALUE);
  }

  public static <A> RepetitionParser<A> repeatExactly(Parser<A> delegate, int times) {
    return new RepetitionParser<>(delegate, times, times);
  }

  public static <A> RepetitionParser<A> repeatAtLeast(Parser<A> delegate, int minTimes) {
    return new RepetitionParser<>(delegate, minTimes, Integer.MAX_VALUE);
  }

  public static <A> RepetitionParser<A> repeatAtMost(Parser<A> delegate, int maxTimes) {
    return new RepetitionParser<>(delegate, 0, maxTimes);
  }

  public static <A> RepetitionParser<A> repeatBetween(Parser<A> delegate, int minTimes, int maxTimes) {
    return new RepetitionParser<>(delegate, minTimes, maxTimes);
  }

  @Override
  public Option<ParseResult<Sequence<A>>> tryParse(byte[] data, int p) {
    MutableStack<A> stack = DynamicArray.create();
    while (stack.getSize() < maxTimes && p < data.length) {
      Option<ParseResult<A>> optResult = delegate.tryParse(data, p);
      if (optResult.isEmpty())
        break;
      ParseResult<A> result = optResult.getOrThrow();
      stack.pushBack(result.getValue());
      p = result.getRem();
    }

    if (minTimes <= stack.getSize())
      return Option.some(new ParseResult<>(stack, p));
    return Option.none();
  }
}
