package ProtocoleBSPP;

import ServeurTCP.Reponse;
import model.entity.Subject;

import java.util.ArrayList;

public class ReponseGetSubjects implements Reponse {
    private ArrayList<Subject> subjects;

    public ReponseGetSubjects() {

    }

    public ArrayList<Subject> getSubjects() {
        return subjects;
    }
    public void setSubjects(ArrayList<Subject> subjects) {
        this.subjects = subjects;
    }
}
