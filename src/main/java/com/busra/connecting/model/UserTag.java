package com.busra.connecting.model;

public class UserTag {

    private String id;
    private String email;
    private String tag;
    public UserTag(){}
    public UserTag(String id, String email, String tag){
        this.id = id;
        this.email = email;
        this.tag = tag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
