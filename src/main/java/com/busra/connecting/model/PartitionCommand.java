package com.busra.connecting.model;

public class PartitionCommand {
    private String id;
    private String value;
    public PartitionCommand(){}
    public PartitionCommand(String id, String value){
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
