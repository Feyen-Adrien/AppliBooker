package model.viewmodel;

import java.io.Serializable;

public class SubjectSearchVM implements Serializable {
    private String subject;

    public SubjectSearchVM(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }

}
