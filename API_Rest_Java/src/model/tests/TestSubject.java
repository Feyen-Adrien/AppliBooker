package model.tests;

import model.dao.SubjectDAO;
import model.entity.Subject;
import model.viewmodel.SubjectSearchVM;

import java.sql.SQLException;
import java.util.ArrayList;

public class TestSubject {
    public static void main(String[] args) throws SQLException {
        SubjectSearchVM s = new SubjectSearchVM("in");
        ArrayList<Subject> subjectArrayList = new ArrayList<>();
        subjectArrayList = SubjectDAO.getInstance().searchSubject(s);

        for(int i = 1; i < subjectArrayList.size(); i++){
            System.out.println(subjectArrayList.get(i).getSubject_id()+ ": " + subjectArrayList.get(i).getSubject_name());
        }
    }
}
