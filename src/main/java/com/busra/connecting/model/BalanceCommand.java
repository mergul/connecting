package com.busra.connecting.model;

public class BalanceCommand {
    private String id;
    private String value;
    BalanceCommand(){}
    public BalanceCommand(String id, String value){
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
