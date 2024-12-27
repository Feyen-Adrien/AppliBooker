package model.entity;

import java.io.Serializable;

public class Caddies implements Serializable {
    private Integer id;
    private Integer caddyId;
    private String date;
    private double amount;
    private Boolean payed;

    public Caddies(Integer id, Integer caddyId, String date, double amount, Boolean payed) {
        this.id = id;
        this.caddyId = caddyId;
        this.date = date;
        this.amount = amount;
        this.payed = payed;
    }

    public Integer getId() {
        return id;
    }

    public Integer getCaddyId() {
        return caddyId;
    }

    public String getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public Boolean isPayed() {
        return payed;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCaddyId(Integer caddyId) {
        this.caddyId = caddyId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setPayed(Boolean payed) {
        this.payed = payed;
    }
}
