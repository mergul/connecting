package com.busra.connecting.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@JsonDeserialize(builder = News.Builder.class)
public final class News implements Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    private final ObjectId id;
    private final String ownerId;
    private final String topic;
  //  private List<ThumbModel> thumbnails;
    private final String summary;
    private final List<String> mediaParts;
    private final List<Review> mediaReviews;
    private final List<String> tags;
  //  private String highlights;
    private final Long count;
    private final Date date;
    private final String owner;
    private final Boolean clean;
    private final Boolean arrest;
    private final String ownerUrl;

    @JsonCreator(mode= JsonCreator.Mode.PROPERTIES)
    private News(ObjectId id, String ownerId, String topic/*, List<ThumbModel> thumbnails*/, String summary, List<String> mediaParts, List<Review> mediaReviews, List<String> tags/*, String highlights*/, Long count, Date date, String owner, Boolean clean, Boolean arrest, String ownerUrl){
        this.id=id;
        this.ownerId=ownerId;
        this.topic=topic;
     //   this.thumbnails=thumbnails;
        this.summary=summary;
        this.mediaParts=mediaParts;
        this.mediaReviews=mediaReviews;
        this.owner=owner;
        this.tags=tags;
    //    this.highlights=highlights;
        this.count=count;
        this.date = date;
        this.clean=clean;
        this.arrest=arrest;
        this.ownerUrl = ownerUrl;
    }

    public ObjectId getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getTopic() {
        return topic;
    }
//    public List<ThumbModel> getThumbnails() {
//        return thumbnails;
//    }
    public String getOwner() {
        return owner;
    }
    public String getSummary() {
        return summary;
    }
//    public String getHighlights() {
//        return highlights;
//    }
    public String getOwnerUrl() {
    return ownerUrl;
}

    public List<String> getMediaParts() {
        return mediaParts;
    }

    public List<Review> getMediaReviews() {
        return mediaReviews;
    }
    public List<String> getTags() {
        return tags;
    }
    public String getOwnerId() {
        return ownerId;
    }
    public Long getCount() {
        return count;
    }
    public Boolean getClean() {
        return clean;
    }
    public Boolean getArrest() {
        return arrest;
    }

    public static Builder of(){
        return new Builder();
    }
    public static Builder of(ObjectId id){
        return new Builder(id);
    }

    public static Builder from(News news){
        final Builder builder = new Builder();
        builder.id=news.id;
        builder.ownerId=news.ownerId;
        builder.topic=news.topic;
       // builder.thumbnails =news.thumbnails;
        builder.owner=news.owner;
        builder.summary=news.summary;
        builder.date =news.date;
        builder.mediaParts=news.mediaParts;
        builder.mediaReviews=news.mediaReviews;
        builder.tags=news.tags;
     //   builder.highlights=news.highlights;
        builder.count=news.count;
        builder.clean=news.clean;
        builder.arrest=news.arrest;
        builder.ownerUrl = news.ownerUrl;
        return builder;
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", ownerId='" + ownerId + '\'' +
                ", topic='" + topic + '\'' +
                ", summary='" + summary + '\'' +
                ", mediaParts=" + mediaParts +
                ", mediaReviews=" + mediaReviews +
                ", tags=" + tags +
                ", count=" + count +
                ", date=" + date +
                ", owner='" + owner + '\'' +
                ", clean=" + clean +
                ", arrest=" + arrest +
                ", ownerUrl='" + ownerUrl + '\'' +
                '}';
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    public static final class Builder {

        public Boolean clean;
        public Boolean arrest;
        private List<Review> mediaReviews;
        private ObjectId id;
        private String ownerId;
        private String topic;
      //  private List<ThumbModel> thumbnails;
        private String owner;
        private String summary;
        private List<String> mediaParts;
        private List<String> tags;
      //  private String highlights;
        private Long count;
        private Date date;
        private String ownerUrl;

        public Builder(){}
        public Builder(ObjectId id){
            this.id=id;
        }
        public Builder withId(ObjectId id){
            this.id =id;
            return this;
        }
        public Builder withOwnerId(String ownerId){
            this.ownerId =ownerId;
            return this;
        }
        public Builder withTopic(String topic){
            this.topic =topic;
            return this;
        }
//        public Builder withThumbnails(List<ThumbModel> thumbnails){
//            this.thumbnails =thumbnails;
//            return this;
//        }
        public Builder withSummary(String summary){
            this.summary =summary;
            return this;
        }
        public Builder withDate(Date date){
            this.date =date;
            return this;
        }
        public Builder withMediaParts(List<String> mediaParts){
            this.mediaParts = mediaParts;
            return this;
        }
        public Builder withMediaReviews(List<Review> mediaReviews){
            this.mediaReviews = mediaReviews;
            return this;
        }
        public Builder withTags(List<String> tags){
            this.tags = tags;
            return this;
        }
//        public Builder withHighlights(String highlights){
//            this.highlights =highlights;
//            return this;
//        }
        public Builder withCount(Long count){
            this.count =count;
            return this;
        }

        public Builder withOwner(String owner) {
            this.owner=owner;
            return this;
        }

        public Builder withClean(Boolean clean) {
            this.clean=clean;
            return this;
        }
        public Builder withArrest(Boolean arrest) {
            this.arrest=arrest;
            return this;
        }
        public Builder withOwnerUrl(String ownerUrl) {
            this.ownerUrl = ownerUrl;
            return this;
        }
        public News build(){
            return new News(id, ownerId, topic/*, thumbnails*/, summary, mediaParts, mediaReviews, tags/*, highlights*/, count, date, owner, clean, arrest, ownerUrl);
        }

    }
}
