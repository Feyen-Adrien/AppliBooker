package model.entity;
//On utilise que des Wrapper comme ça une valeur pourra être égale à null
public class Book {
    private Integer id;
    private Integer author_id;
    private Integer subject_id;
    private String title;
    private String isbn;
    private Integer page_count;
    private Integer stock_quantity;
    private Float price;
    private Integer publish_year;

    public Book(Integer id, Integer author_id, Integer subject_id, String title, String isbn, Integer page_count, Integer stock_quantity, Float price, Integer publish_year) {
        this.id = id;
        this.author_id = author_id;
        this.subject_id = subject_id;
        this.title = title;
        this.isbn = isbn;
        this.page_count = page_count;
        this.stock_quantity = stock_quantity;
        this.price = price;
        this.publish_year = publish_year;
    }

    public Integer getId() {
        return id;
    }

    public Integer getAuthor_id() {
        return author_id;
    }

    public Integer getSubject_id() {
        return subject_id;
    }

    public String getTitle() {
        return title;
    }

    public String getIsbn() {
        return isbn;
    }

    public Integer getPage_count() {
        return page_count;
    }

    public Integer getStock_quantity() {
        return stock_quantity;
    }

    public Float getPrice() {
        return price;
    }

    public Integer getPublish_year() {
        return publish_year;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAuthor_id(Integer author_id) {
        this.author_id = author_id;
    }

    public void setSubject_id(Integer subject_id) {
        this.subject_id = subject_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setPage_count(Integer page_count) {
        this.page_count = page_count;
    }

    public void setStock_quantity(Integer stock_quantity) {
        this.stock_quantity = stock_quantity;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public void setPublish_year(Integer publish_year) {
        this.publish_year = publish_year;
    }
}
