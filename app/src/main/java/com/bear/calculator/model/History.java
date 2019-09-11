package com.bear.calculator.model;

import org.litepal.crud.LitePalSupport;

public class History extends LitePalSupport {

    private int id;

    private String str;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
