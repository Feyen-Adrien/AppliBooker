package model.viewmodel;

public class CaddyItemsSearchVM {
    private Integer bookId;
    private Integer caddyId;

    public CaddyItemsSearchVM(Integer bookId, Integer caddyId) {
        this.bookId = bookId;
        this.caddyId = caddyId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public Integer getCaddyId() {
        return caddyId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public void setCaddyId(Integer caddyId) {
        this.caddyId = caddyId;
    }
}
