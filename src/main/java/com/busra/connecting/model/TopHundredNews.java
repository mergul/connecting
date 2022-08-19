package com.busra.connecting.model;

import java.util.*;

/**
 * Used in aggregations to keep track of the Top Hundred songs
 */
public class TopHundredNews implements Iterable<RecordSSE> {
  private final Map<String, RecordSSE> currentNews = new HashMap<String, RecordSSE>();
  private final TreeSet<RecordSSE> topHundred = new TreeSet<RecordSSE>((o1, o2) -> {
    final int result = o2.getValue().compareTo(o1.getValue());
    if (result != 0) {
      return result;
    }
    return o1.getKey().compareTo(o2.getKey());
  });

  public void add(final RecordSSE newsCount) {
    if(currentNews.containsKey(newsCount.getKey())) {
      topHundred.remove(currentNews.remove(newsCount.getKey()));
    }
    topHundred.add(newsCount);
    currentNews.put(newsCount.getKey(), newsCount);
    if (topHundred.size() > 300) {
      final RecordSSE last = topHundred.last();
      currentNews.remove(last.getKey());
      topHundred.remove(last);
    }
  }

  public void remove(final RecordSSE value) {
    topHundred.remove(value);
    currentNews.remove(value.getKey());
  }


  @Override
  public Iterator<RecordSSE> iterator() {
    return topHundred.iterator();
  }

  public Collection<RecordSSE> getList() {
    return this.topHundred;
  }

}
