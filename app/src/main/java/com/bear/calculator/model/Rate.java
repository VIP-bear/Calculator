package com.bear.calculator.model;

public class Rate {

    private String scur;

    private String tcur;

    private String rate;

    private String update;

    public Rate(){}

    public Rate(String scur, String tcur, String rate, String update) {
        this.scur = scur;
        this.tcur = tcur;
        this.rate = rate;
        this.update = update;
    }

    public String getScur() {
        return scur;
    }

    public void setScur(String scur) {
        this.scur = scur;
    }

    public String getTcur() {
        return tcur;
    }

    public void setTcur(String tcur) {
        this.tcur = tcur;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }
}
