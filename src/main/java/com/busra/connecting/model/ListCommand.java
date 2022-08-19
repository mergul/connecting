package com.busra.connecting.model;

public class ListCommand {
    private String id;
    private String value;
    ListCommand() {}
    public ListCommand(String id, String value){
        this.id=id;
        this.value=value;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }
}
