package daniel.parsing;

import daniel.data.option.Option;
import daniel.data.sequence.Sequence;
import daniel.data.stack.DynamicArray;
import daniel.data.stack.MutableStack;

public final class DelimitedRepetitionParser<A> extends Parser<Sequence<A>> {
  private final Parser<A> elementParser;
  private final Parser<?> delimiterParser;
  private final int minTimes;
  private final int maxTimes;

  private DelimitedRepetitionParser(
      Parser<A> elementParser,
      Parser<?> delimiterParser,
      int minTimes, int maxTimes) {
    this.elementParser = elementParser;
    this.delimiterParser = delimiterParser;
    this.minTimes = minTimes;
    this.maxTimes = maxTimes;
  }

  public static <A> DelimitedRepetitionParser<A> repeatArbitrarily(
      Parser<A> elementParser, Parser<?> delimiterParser) {
    return new DelimitedRepetitionParser<>(elementParser, delimiterParser, 0, Integer.MAX_VALUE);
  }

  public static <A> DelimitedRepetitionParser<A> repeatExactly(
      Parser<A> elementParser, Parser<?> delimiterParser, int times) {
    return new DelimitedRepetitionParser<>(elementParser, delimiterParser, times, times);
  }

  public static <A> DelimitedRepetitionParser<A> repeatAtLeast(
      Parser<A> elementParser, Parser<?> delimiterParser, int minTimes) {
    return new DelimitedRepetitionParser<>(elementParser, delimiterParser, minTimes, Integer.MAX_VALUE);
  }

  public static <A> DelimitedRepetitionParser<A> repeatAtMost(
      Parser<A> elementParser, Parser<?> delimiterParser, int maxTimes) {
    return new DelimitedRepetitionParser<>(elementParser, delimiterParser, 0, maxTimes);
  }

  public static <A> DelimitedRepetitionParser<A> repeatBetween(
      Parser<A> elementParser, Parser<?> delimiterParser, int minTimes, int maxTimes) {
    return new DelimitedRepetitionParser<>(elementParser, delimiterParser, minTimes, maxTimes);
  }

  @Override
  public Option<ParseResult<Sequence<A>>> tryParse(byte[] data, int p) {
    MutableStack<A> stack = DynamicArray.create();
    while (stack.getSize() < maxTimes) {
      int pAfterDelimiter = p;
      if (!stack.isEmpty()) {
        ParseResult<?> optResDelim = delimiterParser.tryParse(data, p).getOrNull();
        if (optResDelim == null)
          break;
        pAfterDelimiter = optResDelim.getRem();
      }

      Option<ParseResult<A>> optResult = elementParser.tryParse(data, pAfterDelimiter);
      if (optResult.isEmpty())
        break;
      ParseResult<A> result = optResult.getOrThrow();
      stack.pushBack(result.getValue());
      p = result.getRem();
    }

    if (minTimes <= stack.getSize())
      return Option.some(new ParseResult<Sequence<A>>(stack, p));
    return Option.none();
  }
}
