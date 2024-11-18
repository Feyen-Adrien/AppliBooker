package model.entity;

import java.io.Serializable;

public class CaddyItems implements Serializable {
    private Integer id;
    private Integer caddyId;
    private Integer bookId;
    private Integer quantity;

    public CaddyItems(Integer id, Integer caddyId, Integer bookId, Integer quantity) {
        this.id = id;
        this.caddyId = caddyId;
        this.bookId = bookId;
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public Integer getCaddyId() {
        return caddyId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCaddyId(Integer caddyId) {
        this.caddyId = caddyId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
