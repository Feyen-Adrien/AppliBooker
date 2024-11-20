package model.viewmodel;

import java.io.Serializable;

public class BookSearchVM implements Serializable {
    private String authorName;
    private String title;
    private String subjectName;
    private double maxPrice;

    public BookSearchVM(String authorName, String title, String subjectName, double maxPrice) {
        this.authorName = authorName;
        this.title = title;
        this.subjectName = subjectName;
        this.maxPrice = maxPrice;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getTitle() {
        return title;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }
}
