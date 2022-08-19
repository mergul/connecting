package com.busra.connecting.service;

import org.springframework.stereotype.Service;

@Service
public interface NewsStreams {
    String PAGEVIEWS_OUT = "pageviews-topics";
    String REPORTS_OUT = "reports-topics";
    String NEWS_OUT = "news-topics";
    String USERS_OUT = "users-topics";
    String AUTHS_OUT = "auths-topics";
    String PAGEVIEWS_IN = "pageviews-counts";
    String REPORTS_IN = "reports-counts";
    String NEWS_IN = "news-topics";
    String USERS_IN = "users-topics";
}
