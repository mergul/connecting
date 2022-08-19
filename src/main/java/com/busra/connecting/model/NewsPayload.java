package com.busra.connecting.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.Date;
import java.util.List;

@JsonDeserialize(builder = NewsPayload.Builder.class)
public class NewsPayload {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId newsId;
    private String newsOwner;
    private List<String> tags;
    private List<String> topics;
    private Boolean clean;
    private String newsOwnerId;
    private Date date;
    private String topic;
    private Long count;
    private String thumb;
    private String ownerUrl;

    public NewsPayload(List<String> topics) {
        this.topics = topics;
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public NewsPayload(ObjectId newsId, String newsOwnerId, List<String> tags, Boolean clean, String newsOwner, List<String> topics, Date date,
                       String topic, Long count, String thumb, String ownerUrl) {
        this.newsId = newsId;
        this.newsOwnerId = newsOwnerId;
        this.tags = tags;
        this.clean = clean;
        this.newsOwner = newsOwner;
        this.topics = topics;
        this.count = count;
        this.thumb = thumb;
        this.date = date;
        this.topic = topic;
        this.ownerUrl = ownerUrl;
    }

    public String getNewsOwner() {
        return newsOwner;
    }

    public void setNewsOwner(String newsOwner) {
        this.newsOwner = newsOwner;
    }

    public ObjectId getNewsId() {
        return newsId;
    }

    public void setNewsId(ObjectId newsId) {
        this.newsId = newsId;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Boolean getClean() {
        return clean;
    }

    public void setClean(Boolean clean) {
        this.clean = clean;
    }

    public String getNewsOwnerId() {
        return newsOwnerId;
    }

    public void setNewsOwnerId(String newsOwnerId) {
        this.newsOwnerId = newsOwnerId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
    public String getOwnerUrl() {
        return ownerUrl;
    }

    public void setOwnerUrl(String ownerUrl) {
        this.ownerUrl = ownerUrl;
    }

    public static Builder of() {
        return new Builder();
    }

    public static Builder of(ObjectId id) {
        return new Builder(id);
    }

    public static Builder from(NewsPayload news) {
        final Builder builder = new Builder();
        builder.newsId = news.newsId;
        builder.newsOwnerId = news.newsOwnerId;
        builder.tags = news.tags;
        builder.topics = news.topics;
        builder.clean = news.clean;
        builder.newsOwner = news.newsOwner;
        builder.date = news.date;
        builder.topic = news.topic;
        builder.count = news.count;
        builder.thumb = news.thumb;
        builder.ownerUrl = news.ownerUrl;
        return builder;
    }

    @Override
    public String toString() {
        return "NewsPayload{" +
                "newsId=" + newsId +
                ", newsOwner='" + newsOwner + '\'' +
                ", tags=" + tags +
                ", topics=" + topics +
                ", clean=" + clean +
                ", newsOwnerId='" + newsOwnerId + '\'' +
                ", date=" + date +
                ", topic='" + topic + '\'' +
                ", count=" + count +
                ", thumb='" + thumb + '\'' +
                ", ownerUrl='" + ownerUrl + '\'' +
                '}';
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    public static final class Builder {

        public Boolean clean;
        private ObjectId newsId;
        private String newsOwnerId;
        private String topic;
        private String newsOwner;
        private String ownerUrl;
        private String thumb;
        private List<String> tags;
        private List<String> topics;
        private Long count;
        private Date date;

        public Builder() {
        }

        public Builder(ObjectId newsId) {
            this.newsId = newsId;
        }

        public Builder withNewsId(ObjectId newsId) {
            this.newsId = newsId;
            return this;
        }

        public Builder withNewsOwnerId(String newsOwnerId) {
            this.newsOwnerId = newsOwnerId;
            return this;
        }

        public Builder withTopic(String topic) {
            this.topic = topic;
            return this;
        }

        public Builder withThumb(String thumb) {
            this.thumb = thumb;
            return this;
        }

        public Builder withDate(Date date) {
            this.date = date;
            return this;
        }

        public Builder withTags(List<String> tags) {
            this.tags = tags;
            return this;
        }

        public Builder withTopics(List<String> topics) {
            this.topics = topics;
            return this;
        }

        public Builder withCount(Long count) {
            this.count = count;
            return this;
        }

        public Builder withNewsOwner(String newsOwner) {
            this.newsOwner = newsOwner;
            return this;
        }

        public Builder withClean(Boolean clean) {
            this.clean = clean;
            return this;
        }
        public Builder withOwnerUrl(String ownerUrl) {
            this.ownerUrl = ownerUrl;
            return this;
        }
        public NewsPayload build() {
            return new NewsPayload(newsId, newsOwnerId, tags, clean, newsOwner, topics, date, topic, count, thumb, ownerUrl);
        }

    }
}
