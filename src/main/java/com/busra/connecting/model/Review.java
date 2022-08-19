package com.busra.connecting.model;

import java.io.Serializable;

public class Review implements Serializable {
    private String doc_name;
    private String doc_description;
    private String file_name;
    private String file_type;
    private Boolean has_medium;

    public  Review(){}

    public Review(String doc_name,String doc_description,String file_name,String file_type, Boolean has_medium){
        this.setDoc_description(doc_description);
        this.setDoc_name(doc_name);
        this.setFile_name(file_name);
        this.setFile_type(file_type);
        this.setHas_medium(has_medium);
    }

    public String getDoc_name() {
        return doc_name;
    }

    public void setDoc_name(String doc_name) {
        this.doc_name = doc_name;
    }

    public String getDoc_description() {
        return doc_description;
    }

    public void setDoc_description(String doc_description) {
        this.doc_description = doc_description;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }
    public Boolean getHas_medium() {
        return has_medium;
    }

    public void setHas_medium(Boolean has_medium) {
        this.has_medium = has_medium;
    }

}
