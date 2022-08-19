package com.busra.connecting.model;

import java.util.List;

public class PaymentCommand {
    private  String id;
    private  List<String> value;
    public PaymentCommand(){}
    public PaymentCommand(String id, List<String> value){
        this.id=id;
        this.value=value;
    }

    public String getId() {
        return id;
    }

    public List<String> getValue() {
        return value;
    }
}
