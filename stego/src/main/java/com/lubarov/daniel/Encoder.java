package com.lubarov.daniel;

import java.util.List;

public class Encoder {


//  public static String encode(List<Boolean> bits) {
//    List<String> words = new ArrayList<>();
//  }

//  public static List<Boolean> decode(String encodedText) {
//    ;
//  }

  private static List<String> getNextWordOptions(List<String> words) {
    int l = words.size();
    for (int lookback = Math.min(NGRAM_SIZE, l); lookback >= 0; --lookback) {
      List<String> context = words.subList(l - lookback, l);
      if (context.isEmpty()) context.add(".");
      ;
    }
    throw new RuntimeException("No options (shouldn't get here).");
  }
}
