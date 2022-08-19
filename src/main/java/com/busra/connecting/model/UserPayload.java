package com.busra.connecting.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.List;
@JsonDeserialize(builder = UserPayload.Builder.class)
public class UserPayload {
    public String id;
    public List<String> tags;
    public List<String> users;
    public Integer index;
    public String random;
    private Boolean isAdmin;

    public UserPayload(){}
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public UserPayload(String id, List<String> tags, List<String> users, Integer index, String random, Boolean isAdmin) {
        this.id = id;
        this.tags = tags;
        this.users = users;
        this.index = index;
        this.random = random;
        this.isAdmin = isAdmin;
    }

    public List<String> getTags() {
        return tags;
    }
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    public List<String> getUsers() {
        return users;
    }
    public void setUsers(List<String> users) {
        this.users = users;
    }
    public Integer getIndex() {
        return index;
    }
    public void setIndex(Integer index) {
        this.index = index;
    }
    public String getRandom() {
        return random;
    }
    public void setRandom(String random) {
        this.random = random;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Boolean getIsAdmin() {
        return isAdmin;
    }
    public void setIsAdmin(Boolean admin) {
        isAdmin = admin;
    }
    @Override
    public String toString() {
        return "UserPayload{" +
                "id='" + id + '\'' +
                ", tags=" + tags +
                ", users=" + users +
                ", index=" + index +
                ", random=" + random +
                ", isAdmin=" + isAdmin +
                '}';
    }
    public static Builder of() {
        return new Builder();
    }

    public static Builder of(String id) {
        return new Builder(id);
    }
    public static Builder from(UserPayload userPayload) {
        final Builder builder = new Builder();
        builder.id = userPayload.id;
        builder.index = userPayload.index;
        builder.tags = userPayload.tags;
        builder.users = userPayload.users;
        builder.random = userPayload.random;
        builder.isAdmin = userPayload.isAdmin;
        return builder;
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    public static final class Builder {

        public String id;
        public List<String> tags;
        public List<String> users;
        public Integer index;
        public String random;
        public Boolean isAdmin;


        public Builder() {
        }

        public Builder(String id) {
            this.id = id;
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }
        public Builder withIndex(Integer index) {
            this.index = index;
            return this;
        }
        public Builder withRandom(String random) {
            this.random = random;
            return this;
        }
        public Builder withIsAdmin(Boolean isAdmin) {
            this.isAdmin = isAdmin;
            return this;
        }
        public Builder withTags(List<String> tags) {
            this.tags = tags;
            return this;
        }

        public Builder withUsers(List<String> users) {
            this.users = users;
            return this;
        }
        public UserPayload build() {
            return new UserPayload(id, tags, users, index, random, isAdmin);
        }
    }
}
