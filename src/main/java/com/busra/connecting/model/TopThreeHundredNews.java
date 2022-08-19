package com.busra.connecting.model;

import java.util.*;

public class TopThreeHundredNews implements Iterable<NewsPayload> {
    private final Map<String, NewsPayload> currentNews = new HashMap<String, NewsPayload>();
    private final TreeSet<NewsPayload> topHundred = new TreeSet<NewsPayload>((o1, o2) -> {
        final int result = o2.getCount().compareTo(o1.getCount());
        if (result != 0) {
            return result;
        }
        return o1.getDate().compareTo(o2.getDate());
    });

    public void add(final NewsPayload newsCount) {
        if(currentNews.containsKey(newsCount.getNewsId().toHexString())) {
            topHundred.remove(currentNews.remove(newsCount.getNewsId().toHexString()));
        }
        topHundred.add(newsCount);
        currentNews.put(newsCount.getNewsId().toHexString(), newsCount);
        if (topHundred.size() > 300) {
            final NewsPayload last = topHundred.last();
            currentNews.remove(last.getNewsId().toHexString());
            topHundred.remove(last);
        }
    }

    public void remove(final NewsPayload value) {
        topHundred.remove(value);
        currentNews.remove(value.getNewsId().toHexString());
    }
    @Override
    public Iterator<NewsPayload> iterator() {
        return topHundred.iterator();
    }

    public Collection<NewsPayload> getList() {
        return this.topHundred;
    }

}
