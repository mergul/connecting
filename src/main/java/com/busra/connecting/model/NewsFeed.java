package com.busra.connecting.model;

import java.util.Date;
import java.util.List;

public class NewsFeed {

    private String topic;
    private String summary;
    private List<String> tags;
    private Date date;
    private List<Review> mediaReviews;
    private List<String> mediaParts;

    public NewsFeed() {
    }

    public NewsFeed(String summary, String topic, List<String> tags, List<Review> mediaReviews, List<String> mediaParts, Date date) {
        this.summary = summary;
        this.topic = topic;
        this.tags = tags;
        this.date = date;
        this.mediaReviews = mediaReviews;
        this.mediaParts = mediaParts;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<Review> getMediaReviews() {
        return mediaReviews;
    }

    public void setMediaReviews(List<Review> mediaReviews) {
        this.mediaReviews = mediaReviews;
    }

    /*    public List<ThumbModel> get_thumbnails() {
            return _thumbnails;
        }

        public void set_thumbnails(List<ThumbModel> _thumbnails) {
            this._thumbnails = _thumbnails;
        }
       */
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getMediaParts() {
        return mediaParts;
    }

    public void setMediaParts(List<String> mediaParts) {
        this.mediaParts = mediaParts;
    }

   /* public String get_highlights() {
        return _highlights;
    }

    public void set_highlights(String _highlights) {
        this._highlights = _highlights;
    }*/
}
