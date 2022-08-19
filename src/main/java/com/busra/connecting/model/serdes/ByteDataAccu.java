package com.busra.connecting.model.serdes;

import java.util.ArrayList;

public class ByteDataAccu {

    ArrayList<byte[]> list;
    public ByteDataAccu() { this.list = new ArrayList<byte[]>(); }
    public ByteDataAccu add(byte[] val) {
        this.list.add(val);
        return this;
    }

    public ArrayList<byte[]> getList() {
        return this.list;
    }
}
