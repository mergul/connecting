package com.busra.connecting.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Date;

@JsonDeserialize(builder = CommentsFeed.Builder.class)
public class CommentsFeed {
    private String newsId;
    private String userId;
    private String comment;
    private Date date;

    public String getNewsId() {
        return newsId;
    }
    public String getUserId() {
        return userId;
    }
    public String getComment() {
        return comment;
    }
    public Date getDate() {
        return date;
    }
    public CommentsFeed(){ }
    @JsonCreator(mode= JsonCreator.Mode.PROPERTIES)
    public CommentsFeed(String newsId, String userId, String comment, Date date){
        this.newsId = newsId;
        this.userId=userId;
        this.comment=comment;
        this.date=date;
    }
    public static Builder of() {
        return new Builder();
    }
    public static Builder of(String newsId) {
        return new Builder(newsId);
    }

    public static Builder from(CommentsFeed news) {
        final Builder builder = new Builder();
        builder.newsId = news.newsId;
        builder.userId = news.userId;
        builder.comment = news.comment;
        builder.date = news.date;
        return builder;
    }
    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    public static final class Builder {
        public String newsId;
        public String userId;
        public String comment;
        public Date date;

        public Builder(){}

        public Builder(String newsId){
            this.newsId=newsId;
        }

        public Builder withNewsId(String newsId){
            this.newsId =newsId;
            return this;
        }
        public Builder withUserId(String userId){
            this.userId =userId;
            return this;
        }

        public Builder withComment(String comment){
            this.comment =comment;
            return this;
        }
        public Builder withDate(Date date){
            this.date =date;
            return this;
        }
        public CommentsFeed build(){
            return new CommentsFeed(newsId, userId,comment,date);
        }
    }
}
