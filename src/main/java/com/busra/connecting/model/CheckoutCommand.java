package com.busra.connecting.model;

public class CheckoutCommand {
    private String id;
    private String value;
    CheckoutCommand(){}
    public CheckoutCommand(String id, String value){
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
