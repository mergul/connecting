package com.busra.connecting.model;

public class HistoryCommand {
    private String id;
    private String value;
    HistoryCommand(){}
    public HistoryCommand(String id, String value){
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
