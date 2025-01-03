package model.dao;

import model.entity.Subject;
import model.viewmodel.SubjectSearchVM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static model.dao.ConnectDB.close;

public class SubjectDAO {
    private static SubjectDAO instance;
    private ConnectDB connexion;

    private SubjectDAO() {
    }

    public static synchronized SubjectDAO getInstance() {
        if (instance == null) {
            instance = new SubjectDAO();
        }
        return instance;
    }

    //CREATE
    public void addSubject(Subject subject) {
        try {
            String sql = "INSERT INTO subjects (name) VALUES (?)";

            connexion = new ConnectDB();
            PreparedStatement ps = connexion.getConnection().prepareStatement(sql);
            ps.setString(1, subject.getSubject_name());
            ps.executeUpdate();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //READ ALL
    public ArrayList<Subject> getAllSubjects() throws SQLException {
        try {
            ArrayList<Subject> subjects = new ArrayList<>();
            String sql = "SELECT * FROM subjects";

            connexion = new ConnectDB();
            Statement stmt = connexion.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                subjects.add(new Subject(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            }
            close();
            return subjects;

        } catch (SQLException e) {
            throw new SQLException();
        }
    }

    //READ ONE
    public Subject getSubjectById(int id) throws SQLException {
        try {
            String sql = "SELECT * FROM subjects WHERE id = ?";
            connexion = new ConnectDB();
            PreparedStatement ps = connexion.getConnection().prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Subject(
                        rs.getInt("id"),
                        rs.getString("name")
                );
            }
            close();
            return null;
        } catch (SQLException e) {
            throw new SQLException();
        }
    }

    //UPDATE
    public void updateSubject(Subject subject) throws SQLException {
        try {
            String sql = "UPDATE subjects SET name = ? WHERE id = ?";
            connexion = new ConnectDB();
            PreparedStatement ps = connexion.getConnection().prepareStatement(sql);
            ps.setString(1, subject.getSubject_name());
            ps.setInt(2, subject.getSubject_id());
            ps.executeUpdate();
            close();
        } catch (SQLException e) {
            throw new SQLException();
        }
    }

    //DELETE
    public void deleteSubject(int id) throws SQLException {
        try {
            String sql = "DELETE FROM subjects WHERE id = ?";
            connexion = new ConnectDB();
            PreparedStatement ps = connexion.getConnection().prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            close();
        } catch ( SQLException e ) {
            throw new SQLException();
        }
    }
    //Filter
    public ArrayList<Subject> searchSubject(SubjectSearchVM filter) throws SQLException {
        ArrayList<Subject> subjects = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM subjects WHERE 1=1");
        if(filter.getSubject() != null && !filter.getSubject().isEmpty() && !filter.getSubject().equals(" ")) {
            sql.append(" and name LIKE ?");
        }

        connexion =new ConnectDB();
        PreparedStatement ps = connexion.getConnection().prepareStatement(sql.toString());
        int i = 1;

        if(filter.getSubject() != null && !filter.getSubject().isEmpty() && !filter.getSubject().equals(" "))
        {
            ps.setString(i, "%"+filter.getSubject()+"%");
        }

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            subjects.add(new Subject(
                    rs.getInt("id"),
                    rs.getString("name")
            ));
        }
        close();
        return subjects;
    }
}
