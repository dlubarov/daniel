package com.lubarov.daniel;

import com.lubarov.daniel.data.collection.Collection;
import com.lubarov.daniel.data.sequence.Sequence;
import com.lubarov.daniel.data.table.MutableHashTable;
import com.lubarov.daniel.data.table.Table;

public class Ngrams {
  private static final int MAX_LOOKBACK = 4; // Use up to 4-grams.

  private final Table<Sequence<String>, Collection<String>> contextToNextWord;

  public Ngrams(Sequence<String> corpus) {
    contextToNextWord = MutableHashTable.create();
    for (int lookback = 1; lookback <= MAX_LOOKBACK; ++lookback) {
      ;
    }
  }

//  public Collection<String> getOptions(Sequence<String> context) {
//    for (int lookback = MAX_LOOKBACK; lookback >= 0; --lookback) {
//      ;
//    }
//  }
}
