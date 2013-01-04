package daniel.parsing;

import daniel.data.option.Option;
import daniel.data.set.ImmutableHashSet;
import daniel.data.set.ImmutableSet;
import daniel.data.set.MutableHashSet;
import daniel.data.stack.DynamicArray;
import daniel.data.stack.MutableStack;

public final class CharacterParser extends Parser<Character> {
  public static final CharacterParser letterParser, digitParser, letterOrDigitParser;

  static {
    MutableStack<Character> letterParserWhitelist = DynamicArray.create();
    MutableStack<Character> digitParserWhitelist = DynamicArray.create();
    MutableStack<Character> letterOrDigitParserWhitelist = DynamicArray.create();

    for (char c = 'a'; c <= 'z'; ++c) {
      letterParserWhitelist.pushBack(c);
      letterOrDigitParserWhitelist.pushBack(c);
    }
    for (char c = 'A'; c <= 'Z'; ++c) {
      letterParserWhitelist.pushBack(c);
      letterOrDigitParserWhitelist.pushBack(c);
    }
    for (char c = '0'; c <= '9'; ++c) {
      digitParserWhitelist.pushBack(c);
      letterOrDigitParserWhitelist.pushBack(c);
    }

    letterParser = new CharacterParser(letterParserWhitelist);
    digitParser = new CharacterParser(digitParserWhitelist);
    letterOrDigitParser = new CharacterParser(letterOrDigitParserWhitelist);
  }

  private static final Option<ParseResult<Character>> noResult = Option.none();

  private final ImmutableSet<Byte> characterWhitelist;

  public CharacterParser(char... characterWhitelist) {
    MutableHashSet<Byte> whitelistBuilder = MutableHashSet.create();
    for (char c : characterWhitelist)
      whitelistBuilder.tryAdd((byte) checkChar(c));
    this.characterWhitelist = ImmutableHashSet.copyOf(whitelistBuilder);
  }

  public CharacterParser(Iterable<Character> characterWhitelist) {
    MutableHashSet<Byte> whitelistBuilder = MutableHashSet.create();
    for (char c : characterWhitelist)
      whitelistBuilder.tryAdd((byte) checkChar(c));
    this.characterWhitelist = ImmutableHashSet.copyOf(whitelistBuilder);
  }

  @Override
  public Option<ParseResult<Character>> tryParse(byte[] data, int p) {
    if (characterWhitelist.contains(data[p]))
      return Option.some(new ParseResult<>((char) data[p], p + 1));
    return noResult;
  }

  private char checkChar(char c) {
    if (c > 127)
      throw new IllegalArgumentException("Only compatible with 7-bit ASCII characters.");
    return c;
  }
}
